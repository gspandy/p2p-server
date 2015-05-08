package com.vcredit.jdev.p2p.capital.modal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vcredit.jdev.p2p.base.util.StringUtil;
import com.vcredit.jdev.p2p.chinapnr.query.QueryManager;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;
import com.vcredit.jdev.p2p.dto.DivDetailDto;
import com.vcredit.jdev.p2p.dto.InDetails;
import com.vcredit.jdev.p2p.dto.LoansDto;
import com.vcredit.jdev.p2p.dto.LoansHaveFeeDto;
import com.vcredit.jdev.p2p.dto.LoansNoFeeDto;
import com.vcredit.jdev.p2p.dto.MerCashBatchDto;
import com.vcredit.jdev.p2p.dto.MerCashDto;
import com.vcredit.jdev.p2p.dto.PayResult;
import com.vcredit.jdev.p2p.dto.RePayDto;
import com.vcredit.jdev.p2p.dto.ReqExt;
import com.vcredit.jdev.p2p.dto.ReqExtLoans;
import com.vcredit.jdev.p2p.dto.ThirdPayUserResponseDto;
import com.vcredit.jdev.p2p.dto.TransferDto;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.ThirdChannelEnum;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.CapitalRecordRepository;
import com.vcredit.jdev.p2p.util.DateUtil;

/**
 * 资金管理--平台贷后
 * 
 * @author 周佩
 *
 */
@Component
@MessageEndpoint
public class CapitalPlatformManager extends CapitalManager {
	private Logger logger = LoggerFactory.getLogger(CapitalPlatformManager.class);

	@Autowired
	private SignUtils signUtils;
	@Autowired
	private CapitalRecordRepository capitalRecordRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private CapitalAccountManager capitalAccountManager;
	@Autowired
	private QueryManager queryManager;

	/**
	 * 还款：无手续费
	 * 
	 * @param ordId
	 *            订单Id
	 * @param ordDate
	 *            订单日期
	 * @param inAccountId
	 *            入账账户Id
	 * @param outAccountId
	 *            出账账户Id
	 * @param amount
	 *            金额
	 * @param subOrdDate
	 *            关联投标订单的订单日期
	 * @param subOrdId
	 *            投标订单
	 * @return
	 */
	public PayResult normalRepaymentNoFee(String ordId, Date ordDate, Long inAccountId, Long outAccountId, BigDecimal amount, Date subOrdDate,
			String subOrdId) {
		return normalRepayment(ordId, ordDate, inAccountId, outAccountId, amount, new BigDecimal(0), null, subOrdDate, subOrdId, "I");
	}

	/**
	 * 还款
	 * 
	 * @param ordId
	 *            订单Id
	 * @param ordDate
	 *            订单日期
	 * @param inAccountId
	 *            入账账户Id
	 * @param outAccountId
	 *            出账账户Id
	 * @param amount
	 *            金额
	 * @param fee
	 *            手续费
	 * @param divDetails
	 *            分账账户
	 * @param subOrdDate
	 *            关联投标订单的订单日期
	 * @param subOrdId
	 *            投标订单
	 * @return
	 */
	public PayResult normalRepayment(String ordId, Date ordDate, Long inAccountId, Long outAccountId, BigDecimal amount, BigDecimal fee,
			List<DivDetailDto> divDetails, Date subOrdDate, String subOrdId, String feeObjFlag) {
		ThirdPaymentAccount in = capitalRecordRepository.findByAccountSequence(inAccountId);
		ThirdPaymentAccount out = capitalRecordRepository.findByAccountSequence(outAccountId);

		RePayDto dto = new RePayDto();
		dto.setFee(fee);
		if (dto.getFee().doubleValue() != 0) {
			//如果手续费不为0
			dto.setDivDetails(divDetails);
		}
		//收取手续费的对象标志
		if (feeObjFlag == null) {
			dto.setFeeObjFlag("I");
		} else {
			dto.setFeeObjFlag(feeObjFlag);
		}

		dto.setInCustId(in.getThirdPaymentUniqueId().toString());
		dto.setOrdDate(ordDate);
		dto.setOrdId(ordId);
		dto.setOutCustId(out.getThirdPaymentUniqueId().toString());
		dto.setSubOrdDate(subOrdDate);
		dto.setSubOrdId(subOrdId);
		dto.setTransAmt(amount);
		return normalRepayment(dto);
	}
	
	/**
	 * 还款
	 * 
	 * @param ordId
	 *            订单Id
	 * @param ordDate
	 *            订单日期
	 * @param inAccountId
	 *            入账账户Id
	 * @param outAccountId
	 *            出账账户Id
	 * @param amount
	 *            金额
	 * @param fee
	 *            手续费
	 * @param divDetails
	 *            分账账户
	 * @param subOrdDate
	 *            关联投标订单的订单日期
	 * @param subOrdId
	 *            投标订单
	 * @return
	 * @throws Exception 
	 */
	public PayResult normalRepayment(Long inAccountId, Long outAccountId,Long merchantId,String proId, RePayDto repayDto) throws Exception {
		ThirdPaymentAccount in = capitalRecordRepository.findByAccountSequence(inAccountId);
		ThirdPaymentAccount out = capitalRecordRepository.findByAccountSequence(outAccountId);
		ThirdPaymentAccount merchant = null;
		if(merchantId!=null){
			merchant = capitalRecordRepository.findByAccountSequence(merchantId);
		}
		if(repayDto.getFee()!=null&&repayDto.getFee().doubleValue()>0){
			List<DivDetailDto> divDetails = new ArrayList<DivDetailDto>();
			DivDetailDto e = new DivDetailDto();
			e.setDivCustId(signUtils.getMerCustId());
			e.setDivAcctId(merchant.getThirdPaymentId());
			e.setDivAmt(repayDto.getFee().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
			divDetails.add(e);
			repayDto.setDivDetails(divDetails);
		}
		if(proId==null){
			throw new Exception("repayDto->ReqExt->ProId is null");
		}
		ReqExt reqExt =new ReqExt();
		reqExt.setProId(proId);
		repayDto.setReqExt(reqExt);
		repayDto.setInCustId(in.getThirdPaymentUniqueId().toString());
		String outCustId=null;
		//如果执行垫付的时候是商户出钱，所以可能getThirdPaymentUniqueId为空
		if(out.getThirdPaymentUniqueId()==null){//垫付,是商户账户
			outCustId=signUtils.getMerCustId();
			repayDto.setOutAcctId(out.getThirdPaymentId());
		}else{//正常还款
			outCustId=out.getThirdPaymentUniqueId().toString();
		}
		repayDto.setOutCustId(outCustId);
		return normalRepayment(repayDto);
	}

	/**
	 * 
	 * 贷后交易 还款
	 * 
	 * @param repayDto
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult normalRepayment(RePayDto repayDto) {
		logger.info("还款：订单号：" + repayDto.getOrdId() + "订单日期：" + repayDto.getOrdDate());
		String jsonStr = getLoansManager().repay(StringUtils.trimToEmpty(repayDto.getOrdId()), StringUtils.trimToEmpty(repayDto.getOrdDateStr()),
				StringUtils.trimToEmpty(repayDto.getOutCustId()), StringUtils.trimToEmpty(repayDto.getOutAcctId()),
				StringUtils.trimToEmpty(repayDto.getTransAmt().toString()), StringUtils.trimToEmpty(repayDto.getFee2Formate().toString()),
				StringUtils.trimToEmpty(repayDto.getSubOrdId()), StringUtils.trimToEmpty(repayDto.getSubOrdDateStr()),
				StringUtils.trimToEmpty(repayDto.getInCustId()), StringUtils.trimToEmpty(repayDto.getDivDetails2Json()),
				StringUtils.trimToEmpty(repayDto.getFeeObjFlag()), StringUtils.trimToEmpty(repayDto.getReqExt2Json()));
		ThirdPayUserResponseDto res = null;
		PayResult pr = new PayResult();
		String message = "";
		try {
			res = ThirdPayUserResponseDto.formateJson(jsonStr);
			message=StringUtil.decode(res.getRespDesc(),null);
		} catch (JsonParseException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (JsonMappingException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			message=e.getMessage();
			e.printStackTrace();
		}
		pr.setMessage(message);
		pr.setOrdId(repayDto.getOrdId());
		if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(res.getRespCode())) {
			//成功
			pr.setResult(true);
			capitalAccountManager.updateCapital(res);
		}
		return pr;
	}

	/**
	 * 
	 * 贷后交易 放款 不收取手续费,同时必须调用商户代取现
	 * 
	 * @param ordId
	 *            订单Id
	 * @param ordDate
	 *            订单日期
	 * @param outAccountId
	 *            出账账户ID
	 * @param transAmt
	 *            交易金额
	 * @param subOrdId
	 *            投标订单Id
	 * @param subOrdDate
	 *            投标订单日期
	 * @param inAccountId
	 *            入账账户ID
	 * @param unFreezeOrdId
	 *            冻结ID
	 * @param freezeTrxId
	 *            冻结标示（投标冻结时汇付返回的结果）
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult normalLoansNoFee(LoansNoFeeDto nofee) {
		return normalLoans(nofee.getOrdId(), nofee.getOrdDate(), nofee.getOutAccountId(), nofee.getTransAmt(), new BigDecimal(0),
				nofee.getSubOrdId(), nofee.getSubOrdDate(), nofee.getInAccountId(), null, "Y", "Y", nofee.getUnFreezeOrdId(), nofee.getFreezeTrxId(),
				"I", nofee.getReqExt());
	}

	/**
	 * 
	 * 贷后交易 放款 收取手续费,同时必须调用商户代取现
	 * 
	 * @param ordId
	 *            订单号
	 * @param ordDate
	 *            订单日期
	 * @param outAccountId
	 *            出账账号Id
	 * @param transAmt
	 *            金额
	 * @param fee
	 *            手续费
	 * @param subOrdId
	 *            投标订单号
	 * @param subOrdDate
	 *            投标订单日期
	 * @param inAccountId
	 *            入账账户号Id
	 * @param merId
	 *            分账详细
	 * @param unFreezeOrdId
	 *            冻结订单号
	 * @param freezeTrxId
	 *            冻结标示（在投标中冻结成功后返回的结果中）
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult normalLoansHaveFee(LoansHaveFeeDto dto) {
		Map<Long, BigDecimal> routingAmount = dto.getRoutingAmount();
		List<DivDetailDto> divDetails = new ArrayList<DivDetailDto>();
		//根据merId商户的收取Id
		Set<Long> userId = routingAmount.keySet();
		for (Long id : userId) {
			DivDetailDto obj = new DivDetailDto();
			ThirdPaymentAccount in = capitalRecordRepository.findByAccountSequence(id);
			obj.setDivCustId(signUtils.getMerCustId());
			BigDecimal amt = routingAmount.get(id).setScale(2,BigDecimal.ROUND_HALF_UP);
			obj.setDivAcctId(in.getThirdPaymentId());
			obj.setDivAmt(amt.toString());
			divDetails.add(obj);
		}
		return normalLoans(dto.getOrdId(), dto.getOrdDate(), dto.getOutAccountId(), dto.getTransAmt(), dto.getFee(), dto.getSubOrdId(),
				dto.getSubOrdDate(), dto.getInAccountId(), divDetails, "Y", "Y", dto.getUnFreezeOrdId(), dto.getFreezeTrxId(), dto.getFeeObjFlag(),
				dto.getReqExt());
	}

	/**
	 * 
	 * 贷后交易 放款
	 * 
	 * @param ordId
	 *            订单号
	 * @param ordDate
	 *            订单日期
	 * @param outAccountId
	 *            出账账号Id
	 * @param transAmt
	 *            金额
	 * @param fee
	 *            手续费
	 * @param subOrdId
	 *            投标订单号
	 * @param subOrdDate
	 *            投标订单日期
	 * @param inAccountId
	 *            入账账户号Id
	 * @param divDetails
	 *            分账详细
	 * @param isDefault
	 *            是否默认转账到用户账号下面Y：直接转入、N：必须调用商户代取现
	 * @param isUnFreeze
	 *            是否解冻
	 * @param unFreezeOrdId
	 *            冻结订单号
	 * @param freezeTrxId
	 *            冻结标示（在投标中冻结成功后返回的结果中）
	 * @param feeObjFlag
	 *            手续费手续收取对象
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult normalLoans(String ordId, Date ordDate, Long outAccountId, BigDecimal transAmt, BigDecimal fee, String subOrdId,
			Date subOrdDate, Long inAccountId, List<DivDetailDto> divDetails, String isDefault, String isUnFreeze, String unFreezeOrdId,
			String freezeTrxId, String feeObjFlag, ReqExtLoans reqExt) {
		ThirdPaymentAccount in = capitalRecordRepository.findByAccountSequence(inAccountId);
		ThirdPaymentAccount out = capitalRecordRepository.findByAccountSequence(outAccountId);
		LoansDto dto = new LoansDto();
		dto.setFee(fee);
		if (dto.getFee() != null) {
			//如果手续费不为0，那么需要设置分账账户信息
			dto.setDivDetails(divDetails);
		}
		//收取手续费的对象标志
		if (feeObjFlag == null) {
			dto.setFeeObjFlag("I");
		} else {
			dto.setFeeObjFlag(feeObjFlag);
		}
		dto.setFreezeTrxId(freezeTrxId);
		dto.setInCustId(in.getThirdPaymentUniqueId().toString());
		//是否直接转账到用户账户Y：需要调用代取现接口，N：直接转账到用户账号下
		dto.setIsDefault(isDefault);
		dto.setIsUnFreeze(isUnFreeze);
		dto.setOrdDate(subOrdDate);
		dto.setOrdId(ordId);
		dto.setOutCustId(out.getThirdPaymentUniqueId().toString());
		dto.setSubOrdDate(subOrdDate);
		dto.setSubOrdId(subOrdId);
		dto.setTransAmt(transAmt);
		dto.setUnFreezeOrdId(unFreezeOrdId);
		dto.setReqExt(reqExt);

		return normalLoans(dto);
	}

	/**
	 * 贷后交易 放款
	 * 
	 * @param repayDto
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult normalLoans(LoansDto loansDto) {
		logger.info("放款：订单号：" + loansDto.getOrdId() + "订单日期：" + loansDto.getOrdDate());
		String jsonStr = getLoansManager().loans(StringUtils.trimToEmpty(loansDto.getOrdId()), StringUtils.trimToEmpty(loansDto.getOrdDateStr()),
				StringUtils.trimToEmpty(loansDto.getOutCustId()), StringUtils.trimToEmpty(loansDto.getTransAmt().toString()),
				StringUtils.trimToEmpty(loansDto.getFee().toString()), StringUtils.trimToEmpty(loansDto.getSubOrdId()),
				StringUtils.trimToEmpty(loansDto.getSubOrdDateStr()), StringUtils.trimToEmpty(loansDto.getInCustId()),
				StringUtils.trimToEmpty(loansDto.getDivDetails2Json()), StringUtils.trimToEmpty(loansDto.getIsDefault()),
				StringUtils.trimToEmpty(loansDto.getIsUnFreeze()), StringUtils.trimToEmpty(loansDto.getUnFreezeOrdId()),
				StringUtils.trimToEmpty(loansDto.getFreezeTrxId()), StringUtils.trimToEmpty(loansDto.getFeeObjFlag()),
				StringUtils.trimToEmpty(loansDto.getReqExt2Json()));
		ThirdPayUserResponseDto res = null;
		String message="";
		try {
			res = ThirdPayUserResponseDto.formateJson(jsonStr);
			message=StringUtil.decode(res.getRespDesc(),null);
		} catch (JsonParseException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (JsonMappingException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			message=e.getMessage();
			e.printStackTrace();
		}
		PayResult pr = new PayResult();
		pr.setMessage(message);
		pr.setOrdId(loansDto.getOrdId());
		pr.setFreezeTrxId(loansDto.getFreezeTrxId());
		pr.setUnFreezeOrdId(loansDto.getUnFreezeOrdId());
		if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(res.getRespCode())) {
			//成功
			pr.setResult(true);
		}
		return pr;
	}

	/**
	 * 
	 * 贷后交易 商户专用自动扣款转账：平台扣除给用户
	 * 
	 * @param ordId
	 *            订单号
	 * @param inAccountId
	 *            入账用户ID
	 * @param outAccounttId
	 *            出账用户ID
	 * @param amount
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult transfer2User(String ordId, Long inUsrId, Long outUsrId, BigDecimal amount,String merPriv) {
		ThirdPaymentAccount in = capitalRecordRepository.findByAccountSequence(inUsrId);
		ThirdPaymentAccount out = capitalRecordRepository.findByAccountSequence(outUsrId);
		TransferDto transferDto = new TransferDto();
		transferDto.setOrdId(ordId);

		transferDto.setInCustId(in.getThirdPaymentUniqueId().toString());

		String outCustId = signUtils.getMerCustId();//商户号
		transferDto.setOutCustId(outCustId);
		transferDto.setOutAcctId(out.getThirdPaymentId());
		transferDto.setMerPriv(merPriv);
		transferDto.setTransAmt(amount);
		return transfer(transferDto);
	}

	/**
	 * 
	 * 贷后交易 商户专用自动扣款转账：平台扣除给平台内部账户
	 * 
	 * @param ordId
	 *            订单号
	 * @param inAccountId
	 *            入账用户ID
	 * @param outAccounttId
	 *            出账用户ID
	 * @param amount
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult transfer2Platform(String ordId, Long inUsrId, Long outUsrId, BigDecimal amount,String merPriv) {
		TransferDto transferDto = new TransferDto();
		ThirdPaymentAccount in = capitalRecordRepository.findByAccountSequence(inUsrId);
		ThirdPaymentAccount out = capitalRecordRepository.findByAccountSequence(outUsrId);
		transferDto.setOrdId(ordId);
		String custId = signUtils.getMerCustId();//商户号
		transferDto.setInCustId(custId);
		transferDto.setOutCustId(custId);
		transferDto.setTransAmt(amount);
		transferDto.setInAcctId(in.getThirdPaymentId().toUpperCase());
		transferDto.setOutAcctId(out.getThirdPaymentId().toUpperCase());
		transferDto.setMerPriv(merPriv);
		return transfer(transferDto);
	}
	
	/**
	 * 
	 * 贷后交易 商户专用自动扣款转账：平台扣除给平台内部账户
	 * 
	 * @param ordId
	 *            订单号
	 * @param inAccountId
	 *            入账用户ID
	 * @param outAccounttId
	 *            出账用户ID
	 * @param amount
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult transfer2Platform(String ordId, String inAcctId, String outAcctId, BigDecimal amount,String merPriv) {
		TransferDto transferDto = new TransferDto();
		transferDto.setOrdId(ordId);
		String custId = signUtils.getMerCustId();//商户号
		transferDto.setInCustId(custId);
		transferDto.setOutCustId(custId);
		transferDto.setTransAmt(amount);
		transferDto.setInAcctId(inAcctId.toUpperCase());
		transferDto.setOutAcctId(outAcctId.toUpperCase());
		transferDto.setMerPriv(merPriv);
		return transfer(transferDto);
	}

	/**
	 * 贷后交易 商户专用自动扣款转账
	 * 
	 * @param repayDto
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult transfer(TransferDto repayDto) {
		logger.info("商户专用自动扣款转账：" + repayDto.getOrdId() + "用户号OutCustId：" + repayDto.getOutCustId() + "，金额：" + repayDto.getTransAmt()
				+ ",用户号InCustId：" + repayDto.getInCustId());
		String jsonStr = getLoansManager().transfer(StringUtils.trimToEmpty(repayDto.getOrdId()), StringUtils.trimToEmpty(repayDto.getOutCustId()),
				StringUtils.trimToEmpty(repayDto.getOutAcctId()), StringUtils.trimToEmpty(repayDto.getTransAmt().toString()),
				StringUtils.trimToEmpty(repayDto.getInCustId()), repayDto.getInAcctId(),repayDto.getMerPriv());
		ThirdPayUserResponseDto res = null;
		String message="";
		try {
			res = ThirdPayUserResponseDto.formateJson(jsonStr);
			message=StringUtil.decode(res.getRespDesc(),null);
		} catch (JsonParseException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (JsonMappingException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			message=e.getMessage();
			e.printStackTrace();
		}
		PayResult pr = new PayResult();
		pr.setMessage(message);
		pr.setOrdId(repayDto.getOrdId());
		if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(res.getRespCode())) {
			//成功
			pr.setResult(true);
		}
		return pr;
	}

	/**
	 * 
	 * 贷后交易 商户代取现.商户收手续费
	 * 
	 * @param ordId
	 *            订单Id
	 * @param userId
	 *            用户ID
	 * @param transAmt
	 *            金额
	 * @param servFee
	 *            服务费
	 * @param serverId
	 *            商户账户Id
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult merCash(String ordId, Long userId, BigDecimal transAmt, BigDecimal servFee, Long serverId) {
		ThirdPaymentAccount in = capitalRecordRepository.findByAccountSequence(userId);
		MerCashDto merCashDto = new MerCashDto();
		merCashDto.setOrdId(ordId);
		if(servFee!=null&&servFee.doubleValue()>0){//手续费大于0
			ThirdPaymentAccount out = capitalRecordRepository.findByAccountSequence(serverId);
			BigDecimal tmp = servFee.setScale(2,BigDecimal.ROUND_HALF_UP);
			merCashDto.setServFee(tmp.toString());
			merCashDto.setServFeeAcctId(out.getThirdPaymentId());
		}
		BigDecimal temp = transAmt.setScale(2,BigDecimal.ROUND_HALF_UP);
		merCashDto.setTransAmt(temp.toString());
		merCashDto.setUsrCustId(in.getThirdPaymentUniqueId().toString());
		return merCash(merCashDto);
	}

	/**
	 * 贷后交易 商户代取现
	 * 
	 * @param ThirdPayUserResponseDto
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	private PayResult merCash(MerCashDto merCashDto) {
		logger.info("商户专用代取现：" + merCashDto.getOrdId() + "商户子账号：" + merCashDto.getServFeeAcctId());
		String jsonStr = getFundManager().getMerCash(StringUtils.trimToEmpty(merCashDto.getOrdId()),
				StringUtils.trimToEmpty(merCashDto.getUsrCustId()), StringUtils.trimToEmpty(merCashDto.getTransAmt()),
				StringUtils.trimToEmpty(merCashDto.getServFee()), StringUtils.trimToEmpty(merCashDto.getServFeeAcctId()));
		ThirdPayUserResponseDto res = null;
		String message="";
		 try {
			res=ThirdPayUserResponseDto.formateJson(jsonStr);
			message=StringUtil.decode(res.getRespDesc(),null);
		} catch (JsonParseException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (JsonMappingException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			message=e.getMessage();
			e.printStackTrace();
		}
		PayResult pr = new PayResult();
		pr.setMessage(message);
		pr.setOrdId(merCashDto.getOrdId());
		if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(res.getRespCode())) {
			//成功
			pr.setResult(true);
		}
		pr.setMessage(res.getRespDesc());
		return pr;
	}
	
	/**
	 * 
	 * 贷后交易 批量还款
	 * 
	 * @param borrowerUsrId		借款人ID
	 * @param isMerchant		是否是商户
	 * @param investores		投资人列表
	 * @param transAmountes		投资人应还款金额key:投资人ID、value：费用
	 * @param fees				针对投资人应收手续费key:投资人ID、value：费用
	 * @param subOrderids		投标订单
	 * @param orderIds			还款订单
	 * @param proId				标的号
	 * @param date				订单日期
	 * @param batchId			批次号自动生成跟ordId生成方式一致
	 * @param thirdFeeId		所还款三方账号
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult merCashBatch(Long borrowerUsrId,boolean isMerchant,List<Long> investores
			,Map<Long,BigDecimal> transAmountes,Map<Long,BigDecimal> fees,Map<Long,String> subOrderids
			,Map<Long,String> orderIds,String proId
			,Date date,String batchId,Long thirdFeeId)throws Exception {
		ThirdPaymentAccount borrower=capitalRecordRepository.findOne(borrowerUsrId);
		ThirdPaymentAccount thirdFee=capitalRecordRepository.findOne(thirdFeeId);
		MerCashBatchDto mcbd=new MerCashBatchDto();
		mcbd.setBatchId(batchId);
		mcbd.setMerOrdDate(DateUtil.format(date, DateUtil.YYYYMMDD));
		if(isMerchant){//如果是商户
			mcbd.setOutAcctId(borrower.getThirdPaymentId());
			mcbd.setOutCustId(signUtils.getMerCustId());
		}else{//借款人
			mcbd.setOutCustId(borrower.getThirdPaymentUniqueId().toString());
		}
		mcbd.setProId(proId);
		//组合投资人
		List<InDetails> inDetails = new ArrayList<InDetails>();
		for(Long investor:investores){
			InDetails inDetail = new InDetails();
			BigDecimal transAmount=transAmountes.get(investor).setScale(2,BigDecimal.ROUND_HALF_UP);
			BigDecimal fee=fees.get(investor);
			String orderId=orderIds.get(investor);
			String subOrderId=subOrderids.get(investor);
			inDetail.setOrdId(orderId);
			ThirdPaymentAccount inv=capitalRecordRepository.findOne(investor);
			inDetail.setInCustId(inv.getThirdPaymentUniqueId().toString());
			inDetail.setSubOrdId(subOrderId);
			inDetail.setFeeObjFlag("I");//入账账户
			inDetail.setTransAmt(transAmount);
			if(fee!=null&&fee.doubleValue()>0){//手续费大于0
				fee=fee.setScale(2,BigDecimal.ROUND_HALF_UP);
				inDetail.setFee(fee);
				List<DivDetailDto> dtoes=new ArrayList<DivDetailDto>();
				DivDetailDto dto=new DivDetailDto();
				dto.setDivCustId(signUtils.getMerCustId());
				dto.setDivAmt(fee.toString());
				dto.setDivAcctId(thirdFee.getThirdPaymentId());
				dtoes.add(dto);
				inDetail.setDivDetails(dtoes);
			}
			inDetails.add(inDetail);
		}
		mcbd.setInDetails(inDetails);
		//正式进入批量还款
		return repaymentBatch(mcbd.getOutCustId(), mcbd.getOutAcctId(), mcbd.getBatchId(), mcbd.getMerOrdDate(), mcbd.getInDetails2Json(), mcbd.getProId());
	}

	/**
	 * 贷后交易 批量还款
	 * 
	 * @param ThirdPayUserResponseDto
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public PayResult repaymentBatch(String outCustId, String outAcctId, String batchId, String merOrdDate, String inDetails, String proId) {
		logger.info("商户专用代取现：还款批次号" + batchId + "出账用户交易号：" + outCustId + "出账用户子账号：" + outAcctId);
		String jsonStr = getLoansManager().batchRepayment(StringUtils.trimToEmpty(outCustId), StringUtils.trimToEmpty(outAcctId),
				StringUtils.trimToEmpty(batchId), StringUtils.trimToEmpty(merOrdDate), StringUtils.trimToEmpty(inDetails),
				StringUtils.trimToEmpty(proId));
		ThirdPayUserResponseDto res = null;
		String message="";
		 try {
			res=ThirdPayUserResponseDto.formateJson(jsonStr);
			message=StringUtil.decode(res.getRespDesc(),null);
		} catch (JsonParseException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (JsonMappingException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			message=e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			message=e.getMessage();
			e.printStackTrace();
		}
		PayResult pr = new PayResult();
		pr.setMessage(message);
		pr.setOrdId(batchId);
		if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(res.getRespCode())) {
			//成功
			pr.setResult(true);
		}
		return pr;
	}
	
}
