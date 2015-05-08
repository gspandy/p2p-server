package com.vcredit.jdev.p2p.capital.controller;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.capital.modal.AccountBankCardManager;
import com.vcredit.jdev.p2p.capital.modal.CapitalAccountManager;
import com.vcredit.jdev.p2p.chinapnr.account.UserManager;
import com.vcredit.jdev.p2p.chinapnr.deal.BidInfoManager;
import com.vcredit.jdev.p2p.chinapnr.deal.CreditManager;
import com.vcredit.jdev.p2p.chinapnr.deal.FundManager;
import com.vcredit.jdev.p2p.chinapnr.deal.LoansManager;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;
import com.vcredit.jdev.p2p.deal.service.BondPackageInvestManager;
import com.vcredit.jdev.p2p.deal.service.CapitalTopupServiceManager;
import com.vcredit.jdev.p2p.deal.service.CapitalWithdrawServiceManager;
import com.vcredit.jdev.p2p.dto.CapitalTopupReturnDto;
import com.vcredit.jdev.p2p.dto.ManualInvestReturnDto;
import com.vcredit.jdev.p2p.dto.ThirdPayUserRequestDto;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.FeeRateEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountTypeEnum;
import com.vcredit.jdev.p2p.repository.AccountBankCardRecordRepository;
import com.vcredit.jdev.p2p.repository.CapitalRecordRepository;
import com.vcredit.jdev.p2p.util.DateUtil;
import com.vcredit.jdev.p2p.util.FinanceUtil;

/**
 * 中转提交到汇付的controller
 * 
 * @author 周佩
 *
 */
@Path("/channelPay")
public class MiddleChannelPayController {

	private static Logger logger = Logger.getLogger(MiddleChannelPayController.class);
	@Autowired
	UserManager userManager;
	@Autowired
	BidInfoManager bidInfoManager;
	@Autowired
	CreditManager creditManager;
	@Autowired
	FundManager fundManager;
	@Autowired
	LoansManager loansManager;
	@Autowired
	private CapitalAccountManager capitalAccountManager;
	@Autowired
	private SignUtils signUtils;
	@Autowired
	private CapitalRecordRepository capitalRecordRepository;
	@Autowired
	private BondPackageInvestManager bondPackageInvestManager;
	@Autowired
	private P2pSessionContext p2pSessionContext;
	@Autowired
	private AccountBankCardRecordRepository accountBankCardRecordRepository;
	@Autowired
	private CapitalTopupServiceManager capitalTopupServiceManager;
	@Autowired
	private CapitalWithdrawServiceManager capitalWithdrawServiceManager;
	@Autowired
	private AccountBankCardManager accountBankCardManager;
	@Autowired
	private DictionaryUtil dictionaryUtil;

	/**
	 * 三方用户登录
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/thirdUserLogin")
	public Response thirdUserLogin(Map<String, String> paramMap) {
		Long usrId = p2pSessionContext.getCurrentAid();
		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
		if (tpa == null) {
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_491, ResponseConstants.CommonCode.RESPONSE_CODE_491);
		}
		Map<String, String> res = userManager.getUserLoginParam(tpa.getThirdPaymentUniqueId().toString());
		res.put(SignUtils.URL, signUtils.getServerPath());
		return Response.successResponse(res);
	}

	/**
	 * web端用户注册：给前端：投资人
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/registerparam")
	public Response getRegisterParam(Map<String, Object> paramMap) {
		logger.info("web端用户注册begin.................................................");
		Map<String, String> res = null;
		try {
			Long usrId = p2pSessionContext.getCurrentAid();
			res = capitalAccountManager.regeditCreditCapitalAccountBefore(usrId,usrId+"");
			logger.info("web端用户注册getContent1:" + res);
		} catch (Exception e) {
			logger.error("registerparam is error",e);
		}
		res.put(SignUtils.URL, signUtils.getServerPath());
		logger.info("web端用户注册getContent2:" + res);
		logger.info("web端用户注册end####################################.................................................");
		return Response.successResponse(res);
	}

	/**
	 * 注册：给平台使用：便于贷款人开户
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getRegisterParamPlatform")
	public Response getRegisterParamPlatform(Map<String, Object> paramMap) {
		logger.info("借款用户注册begin.................................................平台开户开始 系统时间："+System.currentTimeMillis());
		ThirdPayUserRequestDto dto = new ThirdPayUserRequestDto();
		Map<String, String> res = null;
		try {
			Long usrId = Long.valueOf((String)paramMap.get("usrId"));
			BeanUtils.copyProperties(dto, paramMap);
			res = capitalAccountManager.regeditCreditCapitalAccountBefore(usrId,usrId+"");
			logger.info("web端用户注册getContent1:" + res);
		} catch (Exception e) {
			logger.error("registerparam is error",e);
		}
		res.put(SignUtils.URL, signUtils.getServerPath());
		logger.info("借款用户注册getContent2:" + res);
		logger.info("借款用户注册end####################################.................................................平台开户结束 系统时间："+System.currentTimeMillis());
		return Response.successResponse(res);
	}

	/**
	 * 绑卡
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/bindCard")
	public Response bindCard(Map<String, Object> paramMap) {
		Long usrId = p2pSessionContext.getCurrentAid();
		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
		if (tpa == null) {
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_491, ResponseConstants.CommonCode.RESPONSE_CODE_491);
		}
		Map<String, String> res = userManager.getUserBindCardParam(tpa.getThirdPaymentUniqueId().toString());
		res.put(SignUtils.URL, signUtils.getServerPath());
		return Response.successResponse(res);
	}
	
	/**
	 * 绑卡-后台平台绑卡
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/bindCardPlatform")
	public Response bindCardPlatform(Map<String, Object> paramMap) {
		Long usrId = Long.valueOf((String) paramMap.get("usrId"));
		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
		if (tpa == null) {
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_491, ResponseConstants.CommonCode.RESPONSE_CODE_491);
		}
		Map<String, Object> res = new HashMap<String,Object>();
		try {
			accountBankCardManager.bindCardBg(usrId);
		} catch (Exception e) {
			res.put("error", e);
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_487, ResponseConstants.CommonMessage.RESPONSE_MSG_487,res);
		}
		return Response.successResponse(res);
	}
	
	/**
	 * 绑卡-后台平台走web页面绑卡
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/bindCardPlatformWeb")
	public Response bindCardPlatformWeb(Map<String, Object> paramMap) {
		Long usrId = Long.valueOf((String) paramMap.get("usrId"));
		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
		if (tpa == null) {
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_491, ResponseConstants.CommonCode.RESPONSE_CODE_491);
		}
		Map<String, String> res = userManager.getUserBindCardParam(tpa.getThirdPaymentUniqueId().toString());
		res.put(SignUtils.URL, signUtils.getServerPath());
		return Response.successResponse(res);
	}

	/**
	 * 删卡
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/delBindCard")
	public Response delBindCard(Map<String, Object> paramMap) {
		Long usrId = p2pSessionContext.getCurrentAid();
		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
		if (tpa == null) {
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_491, ResponseConstants.CommonCode.RESPONSE_CODE_491);
		}
		//		Map<String,String> res=userManager.getDelCardParam(tpa.getThirdPaymentUniqueId().toString(),"");
		//		res.put(SignUtils.URL, signUtils.getServerPath());
		return Response.successResponse(null);
	}

	/**
	 * 充值前端投资人充值
	 * 
	 * @param
	 * @return
	 */
	@POST
	@Path("/rechargeCapital")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response netSave(Map<String, Object> paramMap) {
		ThirdPayUserRequestDto dto = new ThirdPayUserRequestDto();
		try {
			BeanUtils.copyProperties(dto, paramMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		//TODO 2015.4.7 dk begin 充值金额没有小数位  ##.00
		BigDecimal transAmt = new BigDecimal((String) paramMap.get("transAmt"));
		transAmt=transAmt.setScale(2);
		dto.setTransAmt(transAmt.toString());
		//TODO 2015.4.7 dk end
		
		Long usrId = p2pSessionContext.getCurrentAid();
		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
		if (tpa == null) {
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_491, ResponseConstants.CommonCode.RESPONSE_CODE_491);
		}

		//  充值处理
		CapitalTopupReturnDto returnDto = capitalTopupServiceManager.capitalTopup(usrId, new BigDecimal(dto.getTransAmt()));
		if (!returnDto.isResult()) {
			return Response.response(returnDto.getStatus(), returnDto.getMsg());
		}
		dto.setOrderId(String.valueOf(returnDto.getOrderId()));
		dto.setOrdDate(DateUtil.format(returnDto.getOrderDate(),DateUtil.YYYYMMDD));

		//设置银行卡
		if(dto.getOpenBankId()!=null){
			String openBankId = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.T_DIC_BANK_ENAME.getCode()
                        + dto.getOpenBankId());
			dto.setOpenBankId(openBankId);
		}
		Map<String, String> res = fundManager.getNetSaveParam(tpa.getThirdPaymentUniqueId().toString(), dto.getOrderId(), dto.getOrdDate(),
				dto.getTransAmt(),dto.getOpenBankId(),null,1);
		res.put(SignUtils.URL, signUtils.getServerPath());
		return Response.successResponse(res);
	}

	/**
	 * 充值后端充值
	 * 
	 * @param
	 * @return
	 */
	@POST
	@Path("/rechargeCapitalPlatform")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response netSavePlatform(Map<String, Object> paramMap) {
		ThirdPayUserRequestDto dto = new ThirdPayUserRequestDto();
		Long usrId = Long.valueOf((String) paramMap.get("usrId"));
		BigDecimal transAmt = new BigDecimal((String) paramMap.get("transAmt"));
		transAmt=transAmt.setScale(2);
		dto.setUsrId(usrId.toString());
		dto.setTransAmt(transAmt.toString());
		if(paramMap.get("openBankId")!=null){
			String openBankId = String.valueOf(paramMap.get("openBankId"));
			dto.setOpenBankId(openBankId.toString());
		}
		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(Long.valueOf(dto.getUsrId()));
		if (tpa == null) {
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_491, ResponseConstants.CommonCode.RESPONSE_CODE_491);
		}else if(ThirdPaymentAccountTypeEnum.CUSTOMER_ACCOUNT.getCode().equals(tpa.getThirdPaymentIdType())){
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_490, ResponseConstants.CommonCode.RESPONSE_CODE_490);
		}
		//  充值处理
		CapitalTopupReturnDto returnDto = capitalTopupServiceManager.capitalTopup(Long.valueOf(dto.getUsrId()), new BigDecimal(dto.getTransAmt()));
		if (!returnDto.isResult()) {
			return Response.response(returnDto.getStatus(), returnDto.getMsg());
		}
		dto.setOrderId(String.valueOf(returnDto.getOrderId()));
		dto.setOrdDate(DateUtil.format(returnDto.getOrderDate(),DateUtil.YYYYMMDD));	
		//给商户充值
		//设置银行卡
		if(dto.getOpenBankId()!=null){
			String openBankId = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.T_DIC_BANK_ENAME.getCode()
                        + dto.getOpenBankId());
			dto.setOpenBankId(openBankId);
		}
		Map<String, String> res = fundManager.getNetSaveParam(signUtils.getMerCustId(), dto.getOrderId(), dto.getOrdDate(),
				dto.getTransAmt(),dto.getOpenBankId(),usrId.toString(),0);
		//添加银行简称
		res.put(SignUtils.URL, signUtils.getServerPath());
		return Response.successResponse(res);
	}

	/**
	 * 取现
	 * 
	 * @param
	 * @return
	 */
	@POST
	@Path("/depositCapital")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response depositCapital(Map<String, Object> parameter) {
		//参数1.账户Id
		Long usrId = p2pSessionContext.getCurrentAid();
		//参数2.金额
		BigDecimal amount=new BigDecimal((String)parameter.get("transAmt"));
		//参数3。银行卡
		Object bankCardSeq=parameter.get("bankCardSeq");
		Long bankCardId= null;
		if(bankCardSeq!=null){
			bankCardId=Long.valueOf((String) bankCardSeq);
		}else{
			//银行卡不能为空
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_489, ResponseConstants.CommonCode.RESPONSE_CODE_489);
		}
		Map<String, String> res = null;
		try {
			res = capitalAccountManager.depositCapitalWeb(usrId, bankCardId,amount,null, false);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_491, ResponseConstants.CommonCode.RESPONSE_CODE_491);
		}
		return Response.successResponse(res);
	}
	
	/**
	 * 取现后端取现,商户取现到MDT
	 * 
	 * @param
	 * @return
	 */
	@POST
	@Path("/depositCapitalPlatform")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response depositCapitalPlatform(Map<String, Object> parameter) {
		//参数1.账户Id
		Long usrId=Long.valueOf((String) parameter.get("usrId"));
		//参数2.金额
		BigDecimal amount=new BigDecimal((String)parameter.get("transAmt"));
		//参数3。取现方式
		String cashChl=(String) parameter.get("cashChl");
		Map<String, String> res = null;
		try {
			res = capitalAccountManager.depositCapitalWeb(usrId, null,amount,cashChl, true);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonMessage.RESPONSE_MSG_491, ResponseConstants.CommonCode.RESPONSE_CODE_491);
		}
		return Response.successResponse(res);
	}

	/**
	 * 主动投标
	 * 
	 * @param
	 * @return
	 */
	@POST
	@Path("/getInitiativeTender")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getInitiativeTenderParams(Map<String, Object> parameter) {
		ThirdPayUserRequestDto dto = new ThirdPayUserRequestDto();
		try {
			BeanUtils.copyProperties(dto, parameter);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		Long investmentId = Long.valueOf((String) parameter.get("investmentSequence"));
		Long usrId = p2pSessionContext.getCurrentAid();
		BigDecimal transAmt = new BigDecimal((String) parameter.get("amount")).setScale(2);
		dto.setTransAmt(transAmt.toString());//转小数位2位
		ManualInvestReturnDto manualInvestReturnDto = bondPackageInvestManager.manualInvest(investmentId, usrId, transAmt);
		if (!manualInvestReturnDto.isResult()) {
			return new Response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, manualInvestReturnDto.getMsg(), null);
		}
		dto.setOrderId(manualInvestReturnDto.getInvestOrderId().toString());
		dto.setFreezeOrdId(manualInvestReturnDto.getFreezeOrderId().toString());

		ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(usrId);
		dto.setUsrCustId(tpa.getThirdPaymentUniqueId().toString());
		Map<String, String> res = capitalAccountManager.getInitiativeTenderParams(investmentId, dto);
		res.put(SignUtils.URL, signUtils.getServerPath());
		return Response.successResponse(res);
	}

	/**
	 * 自动投标计划开始
	 * 
	 * @param
	 * @return
	 */
	@POST
	@Path("/autoInvestmentStart")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response autoInvestmentStart(Map<String, Object> parameter) {
		Long usrId = p2pSessionContext.getCurrentAid();
		String tenderPlanType = (String) parameter.get("tenderPlanType");
		Map<String, String> res = capitalAccountManager.getAutoTenderPlanParams(usrId, tenderPlanType,usrId.toString());
		res.put(SignUtils.URL, signUtils.getServerPath());
		return Response.successResponse(res);
	}

	/**
	 * 自动投标计划关闭
	 * 
	 * @param
	 * @return
	 */
	@POST
	@Path("/autoInvestmentClose")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response autoInvestmentClose(Map<String, Object> parameter) {
		Long usrId = p2pSessionContext.getCurrentAid();
		Map<String, String> res = capitalAccountManager.getAutoTenderPlanCloseParams(usrId,usrId.toString());
		res.put(SignUtils.URL, signUtils.getServerPath());
		return Response.successResponse(res);
	}
	
	/**
	 * 计算手续费率
	 * 
	 * @param
	 * @return
	 */
	@POST
	@Path("/feeIncome")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response feeIncome(Map<String, Object> parameter){
		BigDecimal drawP2pFee = BigDecimal.ZERO;
		try{
			Double d=Double.valueOf((String)parameter.get("amount"));
			 //取得费率
	        double r = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.P2P_DRAW_FRATE.getCode()
	                        + FeeRateEnum.P2P_DRAW_FRATE.getCode()));
	        //提现手续费（投资用户）
	        drawP2pFee = BigDecimal.valueOf(FinanceUtil.calcI2P_WithdrawFee(d.doubleValue(), r));
	        drawP2pFee = drawP2pFee.setScale(2,BigDecimal.ROUND_HALF_UP);
	        return Response.successResponse(drawP2pFee);
		}catch(Exception e){
			logger.info("math feeIncome is error",e);
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_486,ResponseConstants.CommonMessage.RESPONSE_MSG_486,drawP2pFee);
		}
	}
}
