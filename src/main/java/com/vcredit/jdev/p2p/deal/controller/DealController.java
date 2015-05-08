package com.vcredit.jdev.p2p.deal.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.deal.service.BondPackageInvestManager;
import com.vcredit.jdev.p2p.deal.service.BondPayOffManager;
import com.vcredit.jdev.p2p.deal.service.BondTransferManager;
import com.vcredit.jdev.p2p.deal.service.CapitalTopupServiceManager;
import com.vcredit.jdev.p2p.deal.service.CapitalWithdrawServiceManager;
import com.vcredit.jdev.p2p.deal.service.DealServiceReferenceManager;
import com.vcredit.jdev.p2p.deal.service.RedEnvelopeManager;
import com.vcredit.jdev.p2p.deal.service.ReleaseCashServiceManager;
import com.vcredit.jdev.p2p.dto.CapitalTopupReturnDto;
import com.vcredit.jdev.p2p.dto.CapitalWithdrawReturnDto;
import com.vcredit.jdev.p2p.dto.ManualInvestReturnDto;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.enums.AccountInvestRuleTypeEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;

/**
 * 交易模块 。
 * 
 * @author zhuqiu
 * 
 */
@Controller
@Path("/deal")
public class DealController {

	@Autowired
	private BondPackageInvestManager bondPackageInvestManager;

	@Autowired
	private BondPayOffManager bondPayOffManager;

	@Autowired
	private BondTransferManager bondTransferManager;

	@Autowired
	private ReleaseCashServiceManager releaseCashServiceManager;

	@Autowired
	private P2pSessionContext p2pSessionContext;

	@Autowired
	private CapitalWithdrawServiceManager capitalWithdrawServiceManager;

	@Autowired
	private CapitalTopupServiceManager capitalTopupServiceManager;
	@Autowired
	private RedEnvelopeManager redEnvelopeManager;
	@Autowired
	private DealServiceReferenceManager dealServiceReferenceManager;

	@Autowired
	private InvestmentRepository investmentRepository;

	//	@Autowired
	//	private BondPayOffManagerActivator bondPayOffManagerActivator;

	/**
	 * 投资人每日状态_日历
	 * 
	 */
	@POST
	@Path("/dailyPaymentStatus")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response dailyPaymentStatus(Map<String, Object> parameter) {

		Long accountSequence = getAid();

		if (accountSequence == null) {
			return Response.noLoginResponse();
		}

		try {
			String begin = parameter.get("beginDate") == null ? "" : parameter.get("beginDate").toString();
			String end = parameter.get("endDate") == null ? "" : parameter.get("endDate").toString();

			Date beginDate = null;
			if (!"".equals(begin)) {
				beginDate = new Date(begin);
			}

			Date endDate = null;
			if (!"".equals(end)) {
				endDate = new Date(end);
			}

			if (null == beginDate || null == endDate) {
				return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "开始结束日期不能为空");
			}

			return releaseCashServiceManager.dailyPaymentStatus(accountSequence, beginDate, endDate);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.EXCEPTION_CODE, "投资人每日状态_日历 查询失败");
		}

	}

	//TODO DELETE TEST CODE
	private Long getAid() {
		Long accountSequence = p2pSessionContext.getCurrentAid();
		//		if (null == accountSequence) {
		//			accountSequence = 102588L;
		//			accountSequence = 102292L;//rpp4001
		//		}
		return accountSequence;
	}

	/**
	 * 放款
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@POST
	@Path("/releaseCash")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response releaseCash(Map<String, Object> parameter) {

		if (parameter.get("investmentSequence") == null || StringUtil.isEmpty(String.valueOf(parameter.get("investmentSequence")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "业务订单号不能为空");
		}

		Long investmentSequence = Long.parseLong(String.valueOf(parameter.get("investmentSequence")));
		return releaseCashServiceManager.releaseCash(investmentSequence);
	}

	/**
	 * 放款 自动提现，p2p平台扣取手续费
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@POST
	@Path("/autoWithdraw")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response autoWithdraw(Map<String, Object> parameter) {

		if (parameter.get("investmentSequence") == null || StringUtil.isEmpty(String.valueOf(parameter.get("investmentSequence")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "业务订单号不能为空");
		}

		Long investmentSequence = Long.parseLong(String.valueOf(parameter.get("investmentSequence")));
		//investmentSequence = 10L;
		return capitalWithdrawServiceManager.autoWithdrawAndFee(investmentSequence);
	}

	/**
	 * 提现
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@POST
	@Path("/withdraw")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response withdraw(Map<String, Object> parameter) {
		//		Long accountSequence = 6666L;
		BigDecimal amount = null;
		String bankCardNumber = null;
		Integer bankCode = null;

		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}

		if (parameter.get("amount") == null || StringUtil.isEmpty(String.valueOf(parameter.get("amount")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "金额不能为空");
		}
		if (parameter.get("bankCardNumber") == null || StringUtil.isEmpty(String.valueOf(parameter.get("bankCardNumber")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "银行卡号码不能为空");
		}
		if (parameter.get("bankCode") == null || StringUtil.isEmpty(String.valueOf(parameter.get("bankCode")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "所属银行不能为空");
		}

		amount = BigDecimal.valueOf((Long.parseLong(String.valueOf(parameter.get("amount")))));
		bankCardNumber = String.valueOf((Long.parseLong(String.valueOf(parameter.get("bankCardNumber")))));
		bankCode = Integer.parseInt((String.valueOf(parameter.get("bankCode"))));

		CapitalWithdrawReturnDto returnDto = capitalWithdrawServiceManager.withdraw(accountSequence, amount, bankCardNumber, bankCode, false);
		return Response.response(returnDto.getStatus(), returnDto.getMsg());
	}

	/**
	 * 充值
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@POST
	@Path("/topup")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response topup(Map<String, Object> parameter) {
		BigDecimal amount = null;

		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}

		if (parameter.get("amount") == null || StringUtil.isEmpty(String.valueOf(parameter.get("amount")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "金额不能为空");
		}
		if (parameter.get("bankCardNumber") == null || StringUtil.isEmpty(String.valueOf(parameter.get("bankCardNumber")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "银行卡号码不能为空");
		}
		if (parameter.get("bankCode") == null || StringUtil.isEmpty(String.valueOf(parameter.get("bankCode")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "所属银行不能为空");
		}

		amount = BigDecimal.valueOf((Long.parseLong(String.valueOf(parameter.get("amount")))));

		CapitalTopupReturnDto returnDto = capitalTopupServiceManager.capitalTopup(accountSequence, amount);

		return Response.response(returnDto.getStatus(), returnDto.getMsg());
	}

	/**
	 * 开启自助式自动投资
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@POST
	@Path("/selfHelpInvest")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response selfHelpInvest(Map<String, Object> parameter) {
		//		Long accountSequence = 6666L;
		BigDecimal perMaxAmount = null;
		String period = null;
		String creditLevel = null;

		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}
		if (parameter.get("perMaxAmount") == null || StringUtil.isEmpty(String.valueOf(parameter.get("perMaxAmount")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "金额不能为空");
		}
		if (parameter.get("period") == null || StringUtil.isEmpty(String.valueOf(parameter.get("period")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "期次不能为空");
		}
		if (parameter.get("creditLevel") == null || StringUtil.isEmpty(String.valueOf(parameter.get("creditLevel")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "信用等级不能为空");
		}

		perMaxAmount = BigDecimal.valueOf((Long.parseLong(String.valueOf(parameter.get("perMaxAmount")))));
		period = String.valueOf(parameter.get("period"));
		creditLevel = String.valueOf(parameter.get("creditLevel"));

		return bondPackageInvestManager.chkInvestStrategy(accountSequence, perMaxAmount, period, creditLevel, AccountInvestRuleTypeEnum.SELF_HELP);
	}

	/**
	 * 开启一键式自动投资
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@POST
	@Path("/oneKeyInvest")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response oneKeyInvest() {
		//		Long accountSequence = 10L;
		BigDecimal perMaxAmount = null;
		String period = null;
		String creditLevel = null;

		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}

		perMaxAmount = new BigDecimal("1000");
		period = "6,12,18,24,36";
		creditLevel = "1,2,3,4,5,6,0";

		return bondPackageInvestManager.chkInvestStrategy(accountSequence, perMaxAmount, period, creditLevel, AccountInvestRuleTypeEnum.ONE_KEY_TYPE);
	}

	/**
	 * 关闭一键式自动投资
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@POST
	@Path("/colseAutoInvest")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response colseAutoInvest() {
		//		Long accountSequence = 10L;

		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}

		return bondPackageInvestManager.closeInvestStrategy(accountSequence);
	}

	/**
	 * 兑换红包
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@POST
	@Path("/consumeRedEnvelope")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response consumeRedEnvelope(Map<String, Object> parameter) {
		//		Long accountSequence = 10L;

		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}

		BigDecimal tradeAmount = null;
		if (parameter.get("amount") == null || StringUtil.isEmpty(String.valueOf(parameter.get("amount")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "金额不能为空");
		}
		tradeAmount = BigDecimal.valueOf((Long.parseLong(String.valueOf(parameter.get("amount")))));
		return redEnvelopeManager.useRedEnvelope(accountSequence, tradeAmount);
	}

	/**
	 * wdzh_02_我的投资<BR>
	 * 计算并返回：合计在投本金、预期总收益、今日总收益、累计总收益
	 * 
	 * @return
	 */
	@POST
	@Path("/wdzh02MyInvest")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response wdzh02MyInvest() {
		//		Long accountSequence = 10L;

		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}

		Map<String, BigDecimal> map = dealServiceReferenceManager.wdzh02MyInvest(accountSequence);
		return Response.successResponse(map);
	}

	/**
	 * wdzh_02_我的投资(已持有，申请中，已结束)
	 * 
	 * @return
	 */
	@POST
	@Path("/wdzh02MyInvestList")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response wdzh02MyInvestList() {
		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> hold = dealServiceReferenceManager.wdzh02MyInvestList(accountSequence);
		List<Map<String, Object>> allying = dealServiceReferenceManager.wdzh02MyInvestApplyList(accountSequence);
		List<Map<String, Object>> end = dealServiceReferenceManager.wdzh02MyInvestFinishList(accountSequence);

		map.put("hold", hold);
		map.put("allying", allying);
		map.put("end", end);
		return Response.successResponse(map);
	}

	/**
	 * wytz0101_我要投资(投标记录，还款计划 ，债权信息 )
	 * 
	 * @return
	 */
	@POST
	@Path("/wytz0101List")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response wytz0101List(Map<String, Object> parameter) {
		if (parameter == null || parameter.get("investmentSequence") == null
				|| StringUtil.isEmpty(String.valueOf(parameter.get("investmentSequence")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "投资项目不能为空");
		}
		Long investmentSequence = Long.parseLong(String.valueOf(parameter.get("investmentSequence")));

		//Long accountSequence = 10L;
		Long accountSequence = p2pSessionContext.getCurrentAid();
		/*
		 * if (accountSequence == null) { return Response.noLoginResponse(); }
		 */
		List<Map<String, Object>> tenderingRecord = dealServiceReferenceManager.wytz0101TenderingRecordList(investmentSequence);
		List<Map<String, Object>> payPlanFinished = dealServiceReferenceManager.wytz0101PayPlanFinishedList(investmentSequence);
		List<Map<String, Object>> payPlanNotFinish = dealServiceReferenceManager.wytz0101PayPlanNotFinishList(investmentSequence);
		List<Object> creditor = dealServiceReferenceManager.wytz0101CreditorList(accountSequence, investmentSequence);
		boolean chkHasInvested = false;
		long sumHasInvested = 0;
		BigDecimal sumInvestmentAmt = BigDecimal.ZERO;
		if (null != accountSequence) {
			chkHasInvested = dealServiceReferenceManager.wytz0101ChkHasInvested(investmentSequence, accountSequence);

			//sumHasInvested = dealServiceReferenceManager.wytz0101SumHasInvested(accountSequence);
			sumHasInvested = dealServiceReferenceManager.isCurrentAccountInvested(accountSequence);
			if (chkHasInvested) {
				//已投资过该标的的金额
				sumInvestmentAmt = dealServiceReferenceManager.wytz0101SumAmtHasInvested(investmentSequence, accountSequence);
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		//		map.put("tenderingRecord", tenderingRecord);
		map.put("tRecord", tenderingRecord);

		Investment investment = investmentRepository.findOne(investmentSequence);

		if (investment == null) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "标的" + investmentSequence + "不存在");
		}

		//项目状态为放款后
		if (InvestmentStatusEnum.RELEASE_CASH_SUCCESS.getCode() == investment.getInvestmentStatus()
				|| InvestmentStatusEnum.OVERDUE.getCode() == investment.getInvestmentStatus()
				|| InvestmentStatusEnum.REPAYMENT.getCode() == investment.getInvestmentStatus()
				|| InvestmentStatusEnum.AUTO_WITHDRAW_SUCCESS.getCode() == investment.getInvestmentStatus()) {
			map.put("planStatus", "1");
			if (chkHasInvested) {
				map.put("planF", payPlanFinished);
			} else {
				map.put("planNf", payPlanNotFinish);
			}
		} else if (InvestmentStatusEnum.TENDER_FINISH.getCode() == investment.getInvestmentStatus()
				|| InvestmentStatusEnum.ON_LINE.getCode() == investment.getInvestmentStatus()) {
			//上线后 放款前
			map.put("planNf", payPlanNotFinish);
			map.put("planStatus", "0");
		} else {
			//暂不做处理
			map.put("planNf", payPlanNotFinish);
			map.put("planStatus", "0");
		}

		//		if (payPlanFinished.size() > 0 && payPlanNotFinish.size() == 0) {
		//			map.put("planF", payPlanFinished);
		//			map.put("planStatus", "1");
		//
		//			if (has) {
		//
		//			}
		//		} else if (payPlanNotFinish.size() > 0 && payPlanFinished.size() == 0) {
		//			map.put("planNf", payPlanNotFinish);
		//			map.put("planStatus", "0");
		//		}

		map.put("creditor", creditor);
		map.put("chkHasInvested", chkHasInvested); //用户是否已投资该标的
		map.put("sumHasInvested", sumHasInvested); //用户已经投资的
		map.put("sumInvestmentAmt", sumInvestmentAmt);//已投资过该标的的金额

		map.put("nextPaidDate", dealServiceReferenceManager.wytz0101NextNatureDate(investmentSequence));
		map.put("payedPeriod", dealServiceReferenceManager.wytz0101PayedPeriod(investmentSequence));
		return Response.successResponse(map);
	}

	/**
	 * 用户手动投资前检查
	 * 
	 * @return
	 */
	@POST
	@Path("/manualInvestChk")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response manualInvestChk(Map<String, Object> parameter) {

		if (parameter.get("investmentSequence") == null || StringUtil.isEmpty(String.valueOf(parameter.get("investmentSequence")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "投资项目不能为空");
		}
		if (parameter.get("amount") == null || StringUtil.isEmpty(String.valueOf(parameter.get("amount")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "金额不能为空");
		}
		BigDecimal amount = BigDecimal.valueOf((Long.parseLong(String.valueOf(parameter.get("amount")))));
		Long investmentSequence = Long.parseLong(String.valueOf(parameter.get("investmentSequence")));
		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}
		//		Long accountSequence = 1L;
		ManualInvestReturnDto dto = dealServiceReferenceManager.manualInvestChk(investmentSequence, accountSequence, amount);

		if (dto.isResult()) {
			return Response.successResponse();
		} else {
			return new Response(dto.getStatus(), dto.getMsg(), null);
		}
	}

	/**
	 * 月总收益金额变动趋势
	 * 
	 * @return
	 */
	@POST
	@Path("/calcProfitMonth")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response calcProfitMonth(Map<String, Object> parameter) {

		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}
		List<Map<String, Double>> map = dealServiceReferenceManager.calcBenifitMonth(accountSequence);
		return Response.successResponse(map);
	}

	@GET
	@Path("/autoInvest")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response autoInvest() {
		bondPackageInvestManager.autoInvest();
		return Response.successResponse();
	}

	@GET
	@Path("/investFailure/{investmentSequence}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response investFailure(@PathParam("investmentSequence") Long investmentSequence) {
		bondPackageInvestManager.investFailure(investmentSequence);
		return Response.successResponse();
	}

	/**
	 * 用户是否存在投资（开始投资第一笔 无论是否成功只要有了投资 就算有理财）
	 * 
	 * @return
	 */
	@POST
	@Path("/isCurrentAccountInvested")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response isInvested(Map<String, Object> parameter) {
		Long accountSequence = p2pSessionContext.getCurrentAid();
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}

		long sumHasInvested = dealServiceReferenceManager.isCurrentAccountInvested(accountSequence);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isInvested", sumHasInvested > 0 ? true : false); //用户已经投资的
		return Response.successResponse(map);
	}

	/**
	 * 借款人还款（平台还款户-〉借款人户，含风险金，平台管理费）
	 * 
	 * @param claimPayPlanSequence
	 *            还款流水号
	 * @return
	 */
	@POST
	@Path("/normalPay")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response normalPay(Map<String, Object> parameter) {
		if (parameter.get("claimPayPlanSequence") == null || StringUtil.isEmpty(String.valueOf(parameter.get("claimPayPlanSequence")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "还款流水号不能为空");
		}
		Long claimPayPlanSequence = Long.parseLong(String.valueOf(parameter.get("claimPayPlanSequence")));

		return bondPayOffManager.normalPay(claimPayPlanSequence);
	}

	//	@GET
	//	@Path("/normalPay/{claimPayPlanSequence}")
	//	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	//	@Consumes(MediaType.APPLICATION_JSON)
	//	public Response normalPay(@PathParam("claimPayPlanSequence") Long claimPayPlanSequence) {
	//		if (claimPayPlanSequence == null) {
	//			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "还款流水号不能为空");
	//		}
	//		return bondPayOffManager.normalPay(claimPayPlanSequence);
	//	}

	/**
	 * 投资人收款（含投资管理费）
	 * 
	 * @param claimGatherPlanSequence
	 *            收款流水号
	 * @return
	 */
	@POST
	@Path("/normalGather")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response normalGather(Map<String, Object> parameter) {
		if (parameter.get("claimGatherPlanSequence") == null || StringUtil.isEmpty(String.valueOf(parameter.get("claimGatherPlanSequence")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "收款流水号不能为空");
		}

		Long claimGatherPlanSequence = Long.parseLong(String.valueOf(parameter.get("claimGatherPlanSequence")));

		return bondPayOffManager.normalgather(claimGatherPlanSequence);
	}

	/**
	 * 垫付（平台管理费和风险备用金）
	 * 
	 * @param claimPayPlanSequence
	 *            还款流水号
	 * @return
	 */
	@POST
	@Path("/bondPrepaid")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response bondPrepaid(Map<String, Object> parameter) {
		if (parameter.get("claimPayPlanSequence") == null || StringUtil.isEmpty(String.valueOf(parameter.get("claimPayPlanSequence")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "还款流水号不能为空");
		}

		Long claimPayPlanSequence = Long.parseLong(String.valueOf(parameter.get("claimPayPlanSequence")));

		return bondTransferManager.bondPrepaid(claimPayPlanSequence);
	}

	/**
	 * 垫付（投资人）
	 * 
	 * @param claimGatherPlanSequence
	 *            收款流水号
	 * @return
	 */
	@POST
	@Path("/prepaidGather")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response prepaidGather(Map<String, Object> parameter) {
		if (parameter.get("claimGatherPlanSequence") == null || StringUtil.isEmpty(String.valueOf(parameter.get("claimGatherPlanSequence")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "还款流水号不能为空");
		}

		Long claimGatherPlanSequence = Long.parseLong(String.valueOf(parameter.get("claimGatherPlanSequence")));

		return bondTransferManager.prepaidGather(claimGatherPlanSequence);
	}

	//	@GET
	//	@Path("/normalGather/{claimGatherPlanSequence}")
	//	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	//	@Consumes(MediaType.APPLICATION_JSON)
	//	public Response normalGather(@PathParam("claimGatherPlanSequence") Long claimGatherPlanSequence) {
	//		if (claimGatherPlanSequence == null) {
	//			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "收款流水号不能为空");
	//		}
	//		return bondPayOffManager.normalgather(claimGatherPlanSequence);
	//	}

	/**
	 * 分账
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param peroidOfTime
	 *            项目期次
	 * @return
	 */
	//	@GET
	//	@Path("/payoffNormal/{investmentSequence}/{peroidOfTime}")
	//	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	//	@Consumes(MediaType.APPLICATION_JSON)
	//	public Response payoffNormal(@PathParam("investmentSequence") String investmentSequence, @PathParam("peroidOfTime") String peroidOfTime) {
	//
	//		if (StringUtil.isEmpty(investmentSequence)) {
	//			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "业务订单号不能为空");
	//		}
	//		if (StringUtil.isEmpty(peroidOfTime)) {
	//			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "期次不能为空");
	//		}
	//
	//		return bondPayOffManager.separateAccounts(Long.parseLong(investmentSequence), Integer.parseInt(peroidOfTime));
	//
	//	}

	/**
	 * T+1日还款清单（未还，逾期，已垫付）
	 * 
	 * @param claimPayPlanStatus
	 *            收款流水号
	 * 
	 * @return
	 */
	@POST
	@Path("/tDayPayoffList")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response tDayPayoffList(Map<String, Object> parameter) {
		if (parameter.get("claimPayPlanStatus") == null || StringUtil.isEmpty(String.valueOf(parameter.get("claimPayPlanStatus")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "还款状态不能为空");
		}

		Integer claimPayPlanStatus = Integer.parseInt(String.valueOf(parameter.get("claimPayPlanStatus")));

		List<ClaimPayPlan> claimPayPlanList = bondPayOffManager.tDayPayoffList(claimPayPlanStatus);

		return Response.successResponse(claimPayPlanList);
	}

	/**
	 * 资金明细画面（查询资金流水）
	 * 
	 * @param startDate
	 * @return
	 */
	@POST
	@Path("/findAccountOrderList")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAccountOrderList(Map<String, Object> parameter) {
		Long accountSequence = p2pSessionContext.getCurrentAid();
		//accountSequence = 102452L;
		if (accountSequence == null) {
			return Response.noLoginResponse();
		}

		if (parameter.get("dateFrom") == null || parameter.get("dateTo") == null) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "日期不能为空");
		}
		if (parameter.get("currentPage") == null || parameter.get("pageNumber") == null) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "页数不能为空");
		}
		if (parameter.get("status") == null || parameter.get("types") == null) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "状态和类型不能为空");
		}

		Date startDate = null;
		Date endDate = null;
		List<Integer> orderStatusList = null;
		List<Integer> tradeTypeList = null;
		int currentPage;
		int pageSize;

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			//df.setTimeZone(TimeZone.getTimeZone("GMT8"));
			startDate = df.parse(String.valueOf(parameter.get("dateFrom")));
			endDate = df.parse(String.valueOf(parameter.get("dateTo")));
			currentPage = Integer.parseInt(String.valueOf(parameter.get("currentPage")));
			pageSize = Integer.parseInt(String.valueOf(parameter.get("pageNumber")));

			orderStatusList = (ArrayList<Integer>) parameter.get("status");
			tradeTypeList = (ArrayList<Integer>) parameter.get("types");
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, e.getMessage());
		}

		List<AccountOrder> rsltList = dealServiceReferenceManager.findAccountOrderList(accountSequence, startDate, endDate, orderStatusList,
				tradeTypeList, currentPage, pageSize);
		List<AccountOrder> totalList = dealServiceReferenceManager.findAccountOrderList(accountSequence, startDate, endDate, orderStatusList,
				tradeTypeList, 0, 10000);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", totalList.size());
		resultMap.put("items", rsltList);
		return Response.successResponse(resultMap);
	}

	/**
	 * 查询垫付清单（历史逾期天数和罚息）
	 * 
	 * @param claimPayPlanSequence
	 * @return
	 */
	@POST
	@Path("/searchPrepaidList")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response searchPrepaidList(Map<String, Object> parameter) {

		if (parameter.get("claimPayPlanSequence") == null || StringUtil.isEmpty(String.valueOf(parameter.get("claimPayPlanSequence")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "还款流水号不能为空");
		}
		Long claimPayPlanSequence = Long.parseLong(String.valueOf(parameter.get("claimPayPlanSequence")));

		Map<String, String> resultMap = bondTransferManager.searchPrepaidList(claimPayPlanSequence);
		return Response.successResponse(resultMap);
	}
	//	@POST
	//	@Path("/afterloanImport")
	//	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	//	@Consumes(MediaType.APPLICATION_JSON)
	//	@Deprecated
	//	public Response afterloanImport(Map<String, Object> parameter) {
	//		BondPayOffManagerMessageDto dto = new BondPayOffManagerMessageDto();
	//
	//		List<LoanCut> loanCutList = new ArrayList<LoanCut>();
	//		LoanCut loanCut = new LoanCut();
	//		loanCut.setLoanPeriod(6);
	//		loanCut.setTotalShouldGet(BigDecimal.TEN);
	//		loanCut.setTotalActulGet(BigDecimal.TEN);
	//		loanCut.setInvestmentBusinessCode("557750");
	//
	//		loanCutList.add(loanCut);
	//
	//		dto.setLoanCutList(loanCutList);
	//
	//		bondPayOffManagerActivator.beforePayoff(dto);
	//		return Response.successResponse();
	//	}
}
