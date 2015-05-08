package com.vcredit.jdev.p2p.capital.modal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.chinapnr.account.UserManager;
import com.vcredit.jdev.p2p.chinapnr.deal.BidInfoManager;
import com.vcredit.jdev.p2p.chinapnr.deal.FundManager;
import com.vcredit.jdev.p2p.chinapnr.util.ChinapnrConstans;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;
import com.vcredit.jdev.p2p.deal.service.CapitalWithdrawServiceManager;
import com.vcredit.jdev.p2p.dto.BorrowerDetailsDto;
import com.vcredit.jdev.p2p.dto.CapitalWithdrawReturnDto;
import com.vcredit.jdev.p2p.dto.ForzenCapitalDto;
import com.vcredit.jdev.p2p.dto.ManualInvestBackMessageDto;
import com.vcredit.jdev.p2p.dto.MessageEventResponseDto;
import com.vcredit.jdev.p2p.dto.PayResult;
import com.vcredit.jdev.p2p.dto.ReqExtCashDto;
import com.vcredit.jdev.p2p.dto.ThirdAccountView;
import com.vcredit.jdev.p2p.dto.ThirdPayCashRequestDto;
import com.vcredit.jdev.p2p.dto.ThirdPayUserRequestDto;
import com.vcredit.jdev.p2p.dto.ThirdPayUserResponseDto;
import com.vcredit.jdev.p2p.dto.UnForzenCapitalDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountBankCard;
import com.vcredit.jdev.p2p.entity.AccountInvestment;
import com.vcredit.jdev.p2p.entity.ClaimGatherPlan;
import com.vcredit.jdev.p2p.entity.ClaimGatherRecord;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.AccountBankCardEnum;
import com.vcredit.jdev.p2p.enums.ClaimGatherPlanStatusEnum;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.IsFreeze;
import com.vcredit.jdev.p2p.enums.LoanDataStatusEnum;
import com.vcredit.jdev.p2p.enums.OrderStatusEnum;
import com.vcredit.jdev.p2p.enums.ThirdChannelEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountTypeEnum;
import com.vcredit.jdev.p2p.enums.TradeTypeEnum;
import com.vcredit.jdev.p2p.recommend.repository.AccountRecommendRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountBankCardRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountInvestmentRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
import com.vcredit.jdev.p2p.repository.CapitalRecordRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherRecordRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;

/**
 * 资金管理--用户贷前
 * 
 * @author 周佩
 *
 */
@Component
@MessageEndpoint
public class CapitalAccountManager extends CapitalManager {
	private Logger logger = LoggerFactory.getLogger(CapitalAccountManager.class);

	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;
	@Autowired
	private InvestmentRepository investmentRepository;
	@Autowired
	private DictionaryUtil dictionaryUtil;
	@Autowired
	private LoanDataRepository loanDataRepository;
	@Autowired
	private CapitalRecordRepository capitalRecordRepository;
	@Autowired
	private BidInfoManager bidInfoManager;
	@Autowired
	private AccountBankCardRecordRepository accountBankCardRecordRepository;
	@Autowired
	private CapitalWithdrawServiceManager capitalWithdrawServiceManager;
	@Autowired
	FundManager fundManager;
	@Autowired
	UserManager userManager;
	@Autowired
	private SignUtils signUtils;
	@Autowired
	private CapitalPlatformManager capitalPlatformManager;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AccountBankCardManager accountBankCardManager;
	@Autowired
	private AccountThirdRepository accountThirdRepository;
	@Autowired
	private AccountInvestmentRepository accountInvestmentRepository;
	@Autowired
	private ClaimGatherPlanRepository claimGatherPlanRepository;
	@Autowired
	private ClaimGatherRecordRepository claimGatherRecordRepository;
	@Autowired
	private AccountOrderRepository accountOrderRepository;
	@Autowired
	private AccountRecommendRecordRepository accountRecommendRecordRepository;

	/**
	 * 资金交易 投资（手动投资、自动投资） 第三方平台内：资金冻结
	 * 
	 * @param orig
	 *            投资人
	 * @param price
	 *            交易金额
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public MessageEventResponseDto forzenCapital(ForzenCapitalDto orig) {
		MessageEventResponseDto rtnDto = new MessageEventResponseDto();
		BigDecimal price1 = orig.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
		String amount = price1.toString();
		logger.info("接受到请求：资金冻结：项目流水：" + orig.getInvestmentSequence() + "订单号：" + orig.getAccountOrderSequence() + ",金额：" + amount);
		//调用第三方接口
		//***************************************
		String jsonStr = getFundManager().getUsrFreezeBg(orig.getAccountOrderSequence().toString(),
				DateFormatUtil.dateToString(new Date(), DateFormatUtil.YYYYMMDD), orig.getOrig().getThirdPaymentUniqueId().toString(), amount);
		ThirdPayUserResponseDto response = null;
		try {
			response = ThirdPayUserResponseDto.formateJson(jsonStr);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//***************************************
		boolean returnVal = false;
		rtnDto.setResult(false);
		rtnDto.setMsg(response.getRespDesc());
		if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(response.getRespCode())) {
			returnVal = true;//资金冻结成功
			rtnDto.setResult(returnVal);
		}
		logger.info("结束请求：资金冻结：项目流水：" + orig.getInvestmentSequence() + "订单号：" + orig.getAccountOrderSequence() + ",金额：" + amount);
		return rtnDto;
	}

	/**
	 * 资金交易 流标 解除投资（手动投资、自动投资） 第三方平台内：解除资金冻结
	 * 
	 * @param orig
	 *            投资人
	 * @param price
	 *            交易金额
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public ManualInvestBackMessageDto disForzenCapital(UnForzenCapitalDto dto) {
		//调用第三方接口 
		//***************************************
		String dateStr = DateFormatUtil.dateToString(new Date(), DateFormatUtil.YYYYMMDD);
		String trxId = dto.getTrxId();
		String jsonStr = getFundManager().getUsrUnFreeze(dto.getOrderId().toString(), dateStr, trxId);
		ThirdPayUserResponseDto response = null;
		try {
			response = ThirdPayUserResponseDto.formateJson(jsonStr);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//***************************************
		ManualInvestBackMessageDto resultDto = new ManualInvestBackMessageDto();
		resultDto.setResult(false);
		resultDto.setInvestmentSequence(dto.getInvestmentSequence());
		resultDto.setOrderId(dto.getOrderId());
		if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(response.getRespCode())) {
			//资金冻结成功
			resultDto.setResult(true);
		}
		logger.info("结束请求：资金冻结：项目流水：" + dto.getInvestmentSequence() + "订单号：" + dto.getOrderId() + ",金额：" + dto.getPrice());
		return resultDto;
	}

	/**
	 * 客户开户：开通p2p平台的资金账户之后 调用三方接口创建资金账户返回之后回调此方法
	 * 
	 * @param investment
	 * @return
	 * @throws Exception
	 */
	public ThirdPaymentAccount openCreditCapitalAccount(ThirdPayUserResponseDto dto) throws Exception {
		//查询当前id所对应的的账户名
		String merStr = dto.getMerPriv();
		Long accId = Long.valueOf(merStr);
		Account account = accountRepository.findOne(accId);
		if (account == null) {
			throw new Exception("用户不存在");
		}
		//身份证、邮箱、真实姓名
		logger.info("回调：开始创建用户的三方账号......");
		ThirdPaymentAccount tpa = openCreditCapitalAccount(accId, dto.getUsrId(), Long.valueOf(dto.getUsrCustId()), dto.getIdNo(), dto.getUsrEmail(),
				dto.getUsrName(), dto.getUsrMp());
		logger.info("回调：结束创建用户的三方账号......");
		//如果是借款人
		if (ConstantsUtil.AccountRole.BORROWER == account.getAccountRole()) {
			logger.info("回调-后台绑卡：开始创建借款人的银行卡绑定......");
			List<InvestmentAccountReference> res = investmentAccountReferenceRepository.findByAccountSequence(account.getAccountSequence());
			for (InvestmentAccountReference i : res) {
				//将loanData数据状态更新
				LoanData ld = loanDataRepository.findOne(i.getLoanDataSequence());
				Investment inv = investmentRepository.findOne(i.getInvestmentSequence());
				try {
					//更新投资项目状态将待激活变成激活中：1->2
					if (InvestmentStatusEnum.ACTIVE_WAIT.getCode().equals(inv.getInvestmentStatus())) {//如果是激活中的状态1将被修改为2
						inv.setInvestmentStatus(InvestmentStatusEnum.ACTIVEING.getCode());
						investmentRepository.save(inv);
						ld.setProgressStauts(LoanDataStatusEnum.COMPLETE_THIRDACCOUNT_CREATE.getCode());
						ld.setRecordOperateDate(new Date());
						loanDataRepository.save(ld);
					}
					//后台绑卡*********************************************begin
					accountBankCardManager.bindCardBg(tpa.getThirdPaymentUniqueId().toString(), accId);
					//后台绑卡*********************************************end
				} catch (Exception e) {
					inv.setInvestmentStatus(InvestmentStatusEnum.ACTIVE_WAIT.getCode());
					investmentRepository.save(inv);
					logger.error("create bankcard is error bankNumber[" + ld.getBankCardNumber() + "]:", e);
				}
			}
			logger.info("回调-后台绑卡：结束创建借款人的银行卡绑定......");
		}
		return tpa;
	}

	/**
	 * 将投资金额进行分摊到每个投资项目中的借款人中去
	 * 
	 * @param amount
	 *            投资金额
	 * @param irate
	 *            年利率
	 * @param refes
	 *            投资项目关联的借款项目
	 * @return
	 */
	private List<BorrowerDetailsDto> capitalCalculate(Investment invest, BigDecimal amount, String irate, List<InvestmentAccountReference> refes)
			throws Exception {
		BigDecimal countPrice = new BigDecimal(0);//总金额
		List<BorrowerDetailsDto> borroweres = new ArrayList<BorrowerDetailsDto>();
		for (InvestmentAccountReference ref : refes) {
			LoanData loanData = loanDataRepository.findOne(ref.getLoanDataSequence());
			ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(ref.getAccountSequence());
			countPrice = countPrice.add(loanData.getAccountLoanAmount());
			BorrowerDetailsDto d = new BorrowerDetailsDto();
			d.setBorrowerAmt(loanData.getAccountLoanAmount().toString());
			d.setBorrowerCustId(tpa.getThirdPaymentUniqueId().toString());
			d.setBorrowerRate(irate);
			d.setProId(invest.getInvestmentNumber());
			borroweres.add(d);
		}
		//将投资金额分摊到每个借款人那里去
		BigDecimal surplusPrice = new BigDecimal(amount.toString()).setScale(2);//剩余金额
		for (int i = borroweres.size() - 1; i >= 0; i--) {
			BorrowerDetailsDto b = borroweres.get(i);
			if (i == 0) {
				b.setBorrowerAmt(surplusPrice.toString());
			} else {
				//当前借款金额
				BigDecimal bigdec = new BigDecimal(b.getBorrowerAmt());
				bigdec = countPrice.divide(bigdec).multiply(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
				b.setBorrowerAmt(bigdec.toString());
				surplusPrice = surplusPrice.subtract(bigdec);
			}
		}
		return borroweres;
	}

	/**
	 * 主动投标
	 * 
	 * @param investmentId
	 *            项目id
	 * @param dto
	 *            汇付接口信息
	 * @return
	 */
	public Map<String, String> getInitiativeTenderParams(Long investmentId, ThirdPayUserRequestDto dto) {
		Investment invest = investmentRepository.findOne(investmentId);
		//拼装借款人信息
		List<InvestmentAccountReference> refes = investmentAccountReferenceRepository.findByInvestmentSequence(investmentId);

		String irate = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_IRATE.getCode()
				+ invest.getInvestmentAnnualInterestRate());
		String jsonBorrowerStr;
		Map<String, String> res = null;
		try {
			List<BorrowerDetailsDto> borroweres = capitalCalculate(invest, new BigDecimal(dto.getTransAmt()), irate, refes);
			if (dto.getOrdDate() == null || dto.getOrdDate().trim().length() == 0) {
				dto.setOrdDate(DateFormatUtil.dateToString(new Date(), DateFormatUtil.YYYYMMDD));
			}
			jsonBorrowerStr = P2pUtil.getBeanToJson(borroweres);
			res = bidInfoManager.getInitiativeTenderParams(jsonBorrowerStr, dto.getOrderId(), dto.getOrdDate(), dto.getTransAmt(),
					dto.getUsrCustId(), irate, IsFreeze.Y.toString(), dto.getFreezeOrdId());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 自动投标
	 * 
	 * @param investmentId
	 *            项目id
	 * @param dto
	 *            汇付接口信息
	 * @return
	 * @throws Exception
	 */
	public PayResult autoTender(Long investmentId, ThirdPayUserRequestDto dto, Long userId) throws Exception {
		logger.info("investmentId:" + investmentId);
		PayResult pay = new PayResult();
		Investment invest = investmentRepository.findOne(investmentId);
		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(userId);
		if (tpa == null) {
			throw new Exception("the bowrrower id[" + userId + "] have not thirdAccount and it investment id[" + investmentId + "]!");
		}
		dto.setUsrCustId(tpa.getThirdPaymentUniqueId().toString());
		//拼装借款人信息
		List<InvestmentAccountReference> refes = investmentAccountReferenceRepository.findByInvestmentSequence(investmentId);

		String irate = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_IRATE.getCode()
				+ invest.getInvestmentAnnualInterestRate());
		String jsonBorrowerStr;
		ThirdPayUserResponseDto response = null;
		boolean returnVal = false;
		List<BorrowerDetailsDto> borroweres = capitalCalculate(invest, new BigDecimal(dto.getTransAmt()), irate, refes);
		if (dto.getOrdDate() == null || dto.getOrdDate().trim().length() == 0) {
			dto.setOrdDate(DateFormatUtil.dateToString(new Date(), DateFormatUtil.YYYYMMDD));
		}
		jsonBorrowerStr = P2pUtil.getBeanToJson(borroweres);
		String jsonStr = bidInfoManager.autoTender(jsonBorrowerStr, dto.getOrderId(), dto.getOrdDate(), dto.getTransAmt(), dto.getUsrCustId(), irate,
				IsFreeze.Y.toString(), dto.getFreezeOrdId());
		response = ThirdPayUserResponseDto.formateJson(jsonStr);
		if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(response.getRespCode())) {
			returnVal = true;
		}
		pay.setOrdId(response.getOrdId());
		pay.setFreezeId(response.getFreezeOrdId());
		pay.setMessage(response.getRespDesc());
		pay.setFreezeTrxId(response.getFreezeTrxId());
		pay.setResult(returnVal);
		pay.setStrTrxId(response.getTrxId());
		return pay;
	}

	/**
	 * 自动投标计划的参数
	 * 
	 * @param investmentId
	 *            项目id
	 * @param dto
	 *            汇付接口信息
	 * @return
	 */
	public Map<String, String> getAutoTenderPlanParams(Long usrId, String tenderPlanType, String merPriv) {
		Map<String, String> res = null;
		try {
			ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
			res = bidInfoManager.getAutoTenderPlanParams(tpa.getThirdPaymentUniqueId().toString(), tenderPlanType, merPriv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 自动投标计划的关闭
	 * 
	 * @param investmentId
	 *            项目id
	 * @param dto
	 *            汇付接口信息
	 * @return
	 */
	public Map<String, String> getAutoTenderPlanCloseParams(Long usrId, String merPriv) {
		Map<String, String> res = null;
		try {
			ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
			res = bidInfoManager.getAutoTenderPlanCloseParams(tpa.getThirdPaymentUniqueId().toString(), merPriv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 
	 * 取现 前端后端总接口
	 * 
	 * @param usrId
	 *            用户ID
	 * @param bankCardId
	 *            银行卡ID
	 * @param amount
	 *            金额
	 * @param cashChl
	 * @param isMerCust
	 *            是否是商户取现
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> depositCapitalWeb(Long usrId, Long bankCardId, BigDecimal amount, String cashChl, boolean isMerCust) throws Exception {
		ThirdPayCashRequestDto dto = new ThirdPayCashRequestDto();
		AccountBankCard abc = null;
		String reqExt = null;
		String usrCustId = "";
		//设置提现金额
		BigDecimal tmpamount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		dto.setTransAmt(tmpamount.toString());
		//检索当前用户下的资金账户
		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
		boolean noPayFee=false;//收费
		if(tpa.getThirdPaymentIdType()>1&&tpa.getThirdPaymentIdType()<8){//平台账号
			noPayFee=true;//不收费
		}
		//生成订单 和手续费
		CapitalWithdrawReturnDto returnDto = capitalWithdrawServiceManager.withdraw(usrId, new BigDecimal(dto.getTransAmt()), "0", 1,noPayFee);
		dto.setOrdId(returnDto.getOrderId().toString());
		BigDecimal tmp = returnDto.getServFee().setScale(2, BigDecimal.ROUND_HALF_UP);
		int type = 1;//p2p是1
		//设置手续费
		if (tmp.intValue() > 0) {//手续费超过0元
			dto.setServFee(tmp.toString());
			//商户账号
			ThirdPaymentAccount tpa1 = capitalRecordRepository.findByAccountSequence(Long.valueOf(returnDto.getServFeeAcctId()));
			dto.setServFeeAcctId(tpa1.getThirdPaymentId().toUpperCase());
		}
		//设置取现渠道
		try {
			List<ReqExtCashDto> arr = new ArrayList<ReqExtCashDto>();
			ReqExtCashDto reqext = new ReqExtCashDto();
			//如果向商户收取服务费找出MDT账户对MDT账户进行收取服务费
			ThirdPaymentAccount mdt = capitalRecordRepository.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_MDT_ACCOUNT.getCode());
			reqext.setFeeAcctId(mdt.getThirdPaymentId().toUpperCase());
			if (tpa.getThirdPaymentIdType()>1&&tpa.getThirdPaymentIdType()<8) {//平台账户
				reqext.setCashChl(ChinapnrConstans.CashChl.GENERAL);//平台取现都是GENERALmodify20150402
			}else{
				reqext.setCashChl(ChinapnrConstans.CashChl.FAST);//投资人取现都是FAST
			}
			
			arr.add(reqext);
			reqExt = P2pUtil.getBeanToJson(arr);
		} catch (JsonProcessingException e) {
			logger.error("取现渠道设置失败",e);
		}
		if (isMerCust) {
			//mdt账户
			ThirdPaymentAccount mdtaccount = capitalRecordRepository.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_MDT_ACCOUNT.getCode());
			//2设置账户信息为商户
			usrCustId = signUtils.getMerCustId();
			//调用接口进行转账****************************************************SDT->MDT
			PayResult pay = capitalPlatformManager.transfer2Platform(dto.getOrdId(), mdtaccount.getThirdPaymentId().toUpperCase(), tpa
					.getThirdPaymentId().toUpperCase(), new BigDecimal(dto.getTransAmt()), "");
			//调用接口进行转账****************************************************
			if (pay.isResult()) {
				synchronizeMerAccount();
			}
			type = 0;
		} else {
			//投资人的则必须要有银行卡
			//取得当前账户下的默认银行卡
			if (bankCardId != null) {//传参银行卡Id
				abc = accountBankCardRecordRepository.findOne(bankCardId);
				if (!abc.getAccountSequnece().equals(usrId)) {//确定当前银行卡是属于此用户
					throw new Exception("depositCapital:usrId [" + usrId + "] have not bankCard [" + abc.getBankCardNumber() + "]");
				}
			} else {
				abc = accountBankCardRecordRepository.findByThirdPaymentIdAndBankCardDefaultDrawStatus(tpa.getThirdPaymentId(),
						AccountBankCardEnum.DEFAULT_CARD.getCode());
			}
			dto.setOpenAcctId(abc.getBankCardNumber().toString());//取现必须要有银行卡
			usrCustId = tpa.getThirdPaymentUniqueId().toString();
		}

		//根据收费id获得
		Map<String, String> res = fundManager.getCashParam(usrCustId, String.valueOf(dto.getOrdId()), dto.getTransAmt(), dto.getServFee(),
				dto.getServFeeAcctId(), dto.getOpenAcctId(), reqExt, dto.getMerPriv(), type);
		res.put(SignUtils.URL, signUtils.getServerPath());
		return res;
	}

	/**
	 * 资金交易 充值 第三方平台内：银行卡-资金账户
	 * 
	 * @param orig
	 *            投资人
	 * @param bankCard
	 *            银行卡号
	 * @param price
	 *            交易金额
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void updateCapital(ThirdPayUserResponseDto dto) {//modify 20150414 因为出现nullpointexception，是在TRANSFER转账的时候没有mercustId导致
		logger.info("更新资金账户资金余额信息:" + dto.getCmdId() + ";UsrCustId:"
				+ dto.getUsrCustId() + ";inCustId:" + dto.getInCustId()
				+ ";outCustId:" + dto.getOutCustId());
		if (ThirdChannelEnum.NETSAVE.getCode().equals(dto.getCmdId())) {// 充值
			// 如果是商户
			if (dto.getMerCustId().equals(dto.getUsrCustId())) {
				boolean result = false;
				// 那么判断私有域是否存在，如果私有域有值，而且还是充值
				if (dto.getMerPriv() != null) {
					// 如果存在那么进行子账户的转账
					// 子账户,商户下的虚拟账户
					ThirdPaymentAccount childthirdaccount = capitalRecordRepository
							.findByAccountSequence(Long.valueOf(dto
									.getMerPriv()));
					// mdt账户
					ThirdPaymentAccount mdtaccount = capitalRecordRepository
							.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_MDT_ACCOUNT
									.getCode());
					PayResult pay = capitalPlatformManager.transfer2Platform(
							dto.getOrdId(),
							childthirdaccount.getThirdPaymentId(),
							mdtaccount.getThirdPaymentId(),
							new BigDecimal(dto.getTransAmt()), "");
					result = pay.isResult();
				}
				// 同步商户下的所有虚拟账户
				synchronizeMerAccount();
			} else {
				ThirdPaymentAccount third = capitalRecordRepository
						.findByThirdPaymentUniqueId(Long.valueOf(dto
								.getUsrCustId()));
				synchronizeCapitalAccount(third);
			}
		} else if (ThirdChannelEnum.CASH.getCode().equals(dto.getCmdId())) {// 取现
			// 如果是商户
			if (dto.getMerCustId().equals(dto.getUsrCustId())) {
				// 同步商户下的所有虚拟账户
				synchronizeMerAccount();
			} else {
				ThirdPaymentAccount third = capitalRecordRepository
						.findByThirdPaymentUniqueId(Long.valueOf(dto
								.getUsrCustId()));
				synchronizeCapitalAccount(third);
			}
		} else if (ThirdChannelEnum.REPAYMENT.getCode().equals(dto.getCmdId())
				|| // 还款
				ThirdChannelEnum.LOANS.getCode().equals(dto.getCmdId())) {// 放款
			// 出账账户如果不是商户
			if (!dto.getMerCustId().equals(dto.getOutCustId())) {
				ThirdPaymentAccount third = capitalRecordRepository
						.findByThirdPaymentUniqueId(Long.valueOf(dto
								.getOutCustId()));
				synchronizeCapitalAccount(third);
			} else {// 是商户
				synchronizeMerAccount();
			}
			// 入账账户如果不是商户
			if (!dto.getMerCustId().equals(dto.getInCustId())) {
				ThirdPaymentAccount third = capitalRecordRepository
						.findByThirdPaymentUniqueId(Long.valueOf(dto
								.getInCustId()));
				synchronizeCapitalAccount(third);
			} else {// 是商户
				synchronizeMerAccount();
			}
		} else if (ThirdChannelEnum.TRANSFER.getCode().equals(dto.getCmdId())) {// 转账
			// 入账账户如果不是商户
			ThirdPaymentAccount third = capitalRecordRepository.findByThirdPaymentUniqueId(Long.valueOf(dto
								.getInCustId()));
			if(third!=null){
				synchronizeCapitalAccount(third);
			}
			// 出账账户是商户
			synchronizeMerAccount();
		} else if (ThirdChannelEnum.MERCASH.getCode().equals(dto.getCmdId())) {// 商户代取现
			ThirdPaymentAccount third = capitalRecordRepository
					.findByThirdPaymentUniqueId(Long.valueOf(dto.getUsrCustId()));
			synchronizeCapitalAccount(third);
		} else if (ThirdChannelEnum.BATCHREPAYMENT.getCode().equals(
				dto.getCmdId())) {// 批量还款
			// 出账账户如果不是商户
			if (!dto.getMerCustId().equals(dto.getOutCustId())) {
				ThirdPaymentAccount third = capitalRecordRepository
						.findByThirdPaymentUniqueId(Long.valueOf(dto
								.getOutCustId()));
				synchronizeCapitalAccount(third);
			}
			// 入账账户如果不是商户
			if (!dto.getMerCustId().equals(dto.getInCustId())) {
				ThirdPaymentAccount third = capitalRecordRepository
						.findByThirdPaymentUniqueId(Long.valueOf(dto
								.getInCustId()));
				synchronizeCapitalAccount(third);
			}
			// 如果由一方是商户
			ThirdPaymentAccount tpa = capitalRecordRepository
					.findByThirdPaymentId(dto.getInCustId());
			ThirdPaymentAccount tpa1 = capitalRecordRepository
					.findByThirdPaymentId(dto.getInCustId());
			if (ThirdPaymentAccountTypeEnum.CUSTOMER_ACCOUNT.getCode() < tpa
					.getThirdPaymentIdType()
					|| ThirdPaymentAccountTypeEnum.CUSTOMER_ACCOUNT.getCode() < tpa1
							.getThirdPaymentIdType()) {
				// 同步商户下的所有虚拟账户
				synchronizeMerAccount();
			}
		}
	}

	/**
	 * 根据当前用户Id获得相应的参数以便登陆到汇付
	 * 
	 * @param usrId
	 * @return
	 */
	public Map<String, String> getUserLoginParam(Long usrId) {
		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
		String usrCusId = null;
		if (tpa.getThirdPaymentUniqueId() != null) {
			usrCusId = tpa.getThirdPaymentUniqueId().toString();
		} else {
			usrCusId = tpa.getThirdPaymentId();
		}
		return userManager.getUserLoginParam(usrCusId);
	}

	/**
	 * 根据当前用户Id获得相应的资金信息
	 * 
	 * @param usrId
	 * @return
	 */
	public Response queryUserDetail(Long id) {
		BigDecimal capitalPercent = BigDecimal.ZERO;
		BigDecimal capital = BigDecimal.ZERO;
		BigDecimal futureEarningsPercent = BigDecimal.ZERO;
		BigDecimal allEarningsPercent = BigDecimal.ZERO;
		BigDecimal recommendFriendRecord = BigDecimal.ZERO;
		//预期总收益
		BigDecimal investCapital = BigDecimal.ZERO;
		//今日总收益
		BigDecimal yield = BigDecimal.ZERO;
		ThirdAccountView tav = new ThirdAccountView();
		Account acc = new Account();
		acc.setAccountSequence(id);
		ThirdPaymentAccount result = queryCapitalInfo(acc);
		if (result != null) {
			tav.setThirdAccount(result.getThirdPaymentId());
			tav.setAccountBalance(result.getThirdPaymentIdBalance() == null ? BigDecimal.ZERO.toString() : result.getThirdPaymentIdBalance()
					.toString());
		} else {//如果三方账户没有数据，那么
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_491, ResponseConstants.CommonMessage.RESPONSE_MSG_491, null);
		}
		List<AccountInvestment> ai = accountInvestmentRepository.findAccountInvestmentHolder(id);
		List<ClaimGatherRecord> cl = claimGatherRecordRepository.findByGatherAccountSequence(id);
		if (ai != null) {
			//持有项目
			Long holdProject = accountInvestmentRepository.countByAccountSequenceAndAccountInvestmentStatus(id);
			tav.setHoldProject("" + holdProject);
			//累计净收益
			for (AccountInvestment a : ai) {
				//预期总收益=t_clm_gplan表clm_gpaint
				//债权收款计划
				//List<ClaimGatherPlan> claim = claimGatherPlanRepository.findByAccountInvestmentSequenceAndClaimGatherPlanStatus(
				//		a.getAccountInvestmentSequence(), ClaimGatherPlanStatusEnum.NOT_PAID.getCode());
				List<ClaimGatherPlan> claim = claimGatherPlanRepository.findByAccountInvestmentSequenceAndClaimGatherPlanStatusNot(
						a.getAccountInvestmentSequence(), ClaimGatherPlanStatusEnum.PAID.getCode());

				for (ClaimGatherPlan c : claim) {
					//计划收益
					BigDecimal income = c.getClaimGatherPlanPretendInterest();
					investCapital = investCapital.add(income);
					//如果今天是收款日，那么计算到今日总收益
					if (DateFormatUtil.getCurrentTime(DateFormatUtil.YYYY_MM_DD).equals(
							DateFormatUtil.dateToString(c.getClaimGatherPlanNatureDate(), DateFormatUtil.YYYY_MM_DD))) {
						yield = yield.add(income);
					}
					//在投本金,不等于已收的都是未收
					if (!ClaimGatherPlanStatusEnum.PAID.getCode().equals(c.getClaimGatherPlanStatus())) {
						capital = capital.add(c.getClaimGatherPlanPretendPrincipal());
					}
				}

			}
			tav.setInvestCapital(investCapital.toString());//预期总收益
			tav.setYield(yield.toString());
		}
		//累计净收益
		BigDecimal totalIncome = new BigDecimal(0);
		if (cl != null) {
			for (ClaimGatherRecord c : cl) {
				totalIncome = totalIncome.add(c.getClaimGatherPretendInterest()).add(c.getClaimGatherPretendJusticeInterest());
			}
			tav.setTotalIncome(totalIncome.toString());
		}

		// add by zhuqiu start
		// 4.累计净收益=罚息+利息-投资管理费-提现手续费
		BigDecimal accumulatedIncome = claimGatherPlanRepository.sumBenifitByAcctSeqAndStatus(id, ClaimGatherPlanStatusEnum.PAID.getCode());
		// -提现手续费
		BigDecimal sumWithFeeAmt = accountOrderRepository.sumWithdrawFeeByAccountSequence(id, TradeTypeEnum.P2P_WITHDRAW_FEE.getCode(),
				OrderStatusEnum.PAYMENT_SUCCESS.getCode());
		accumulatedIncome = accumulatedIncome.subtract(sumWithFeeAmt);
		tav.setAccumulatedIncome(accumulatedIncome.toString());
		// add by zhuqiu end

		//冻结资金
		BigDecimal forzenPrice = accountOrderRepository.findFreezeApplySuccessAmt(id, OrderStatusEnum.FREEZE_SUCCESS.getCode(),
				TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
		//用户冻结成功的投资项目数量
		Long investmentCount = accountOrderRepository.getCountFreezeApplySuccess(id, OrderStatusEnum.FREEZE_SUCCESS.getCode(),
				TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
		tav.setInvestmentCount("" + investmentCount);
		//在投本金
		tav.setCurrentInvestCapital(capital.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		//冻结资金
		tav.setForzenCapital(forzenPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());//TODO
		//可用余额用当前账户的总余额-冻结资金
		tav.setAvailableBalance((result.getThirdPaymentIdBalance().subtract(forzenPrice)).toString());
		//在投本金占比 = [在投本金/（在投本金+预期总收益+累计总收益）]*100%；
		BigDecimal sum = capital.add(investCapital).add(totalIncome);
		if (sum.compareTo(BigDecimal.ZERO) > 0) {
			capitalPercent = capital.divide(sum, 8, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100l))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		tav.setCapitalPercent(capitalPercent.toString());
		//预期总收益占比
		if (sum.compareTo(BigDecimal.ZERO) > 0) {
			futureEarningsPercent = investCapital.divide(sum, 8, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100l))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		tav.setFutureEarningsPercent(futureEarningsPercent.toString());
		//累计总收益占比
		if (sum.compareTo(BigDecimal.ZERO) > 0) {
			allEarningsPercent = totalIncome.divide(sum, 8, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100l))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		tav.setAllEarningsPercent(allEarningsPercent.toString());
		//累计推荐好友数
		Long count = accountRecommendRecordRepository.findAccountRecommendRecordCountByUserId(id);
		recommendFriendRecord = BigDecimal.valueOf(count);
		tav.setRecommendFriendRecord(recommendFriendRecord.toString());
		return Response.successResponse(tav);
	}
}
