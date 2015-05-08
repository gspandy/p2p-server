package com.vcredit.jdev.p2p.capital.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.capital.modal.CapitalAccountManager;
import com.vcredit.jdev.p2p.capital.modal.CapitalPlatformManager;
import com.vcredit.jdev.p2p.chinapnr.deal.BidInfoManager;
import com.vcredit.jdev.p2p.chinapnr.deal.FundManager;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;
import com.vcredit.jdev.p2p.deal.service.DealServiceReferenceManager;
import com.vcredit.jdev.p2p.dto.InvestmentInfoDto;
import com.vcredit.jdev.p2p.dto.ThirdAccountView;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountInvestment;
import com.vcredit.jdev.p2p.entity.ClaimGatherPlan;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.ClaimGatherPlanStatusEnum;
import com.vcredit.jdev.p2p.repository.AccountInvestmentRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherRecordRepository;
import com.vcredit.jdev.p2p.util.FinanceUtil;

/**
 * 资金账户管理controller
 * 
 * @author 周佩 创建时间：20141211
 */
@Path("/capital")
public class CapitalController {

	@Autowired
	private CapitalAccountManager capitalAccountManager;
	@Autowired
	private CapitalPlatformManager capitalPlatformManager;
	@Autowired
	private P2pSessionContext p2pSessionContext;
	@Autowired
	private AccountThirdRepository accountThirdRepository;
	@Autowired
	private BidInfoManager bidInfoManager;
	@Autowired
	private FundManager fundManager;
	@Autowired
	private AccountInvestmentRepository accountInvestmentRepository;
	@Autowired
	private ClaimGatherPlanRepository claimGatherPlanRepository;
	@Autowired
	private ClaimGatherRecordRepository claimGatherRecordRepository;
	@Autowired
	private AccountOrderRepository accountOrderRepository;
	@Autowired
	private SignUtils signUtils;
	@Autowired
	private DealServiceReferenceManager dealServiceReferenceManager;

	/**
	 * 检索获得当前用户的资金账户
	 * 
	 * @param userid
	 * @return
	 */
	@POST
	@Path("/queryCapitalInfo")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryCapitalInfo() {
		Long id = p2pSessionContext.getCurrentAid();
		return capitalAccountManager.queryUserDetail(id);
	}

	/**
	 * 检索获得当前用户的资金账户
	 * 
	 * @param userid
	 * @return
	 */
	@POST
	@Path("/queryCapitalInfoPlatform")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryCapitalInfoPlatform(Map<String, Object> paramMap) {
		Long id = Long.valueOf((String) paramMap.get("usrId"));
		return capitalAccountManager.queryUserDetail(id);
	}

	/**
	 * 检索获得当前用户的资金账户
	 * 
	 * @param userid
	 * @return
	 */
	@POST
	@Path("/queryCapitalInfoisExists")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryCapitalInfoisExists() {
		try {
			Long id = p2pSessionContext.getCurrentAid();
			ThirdPaymentAccount result = capitalAccountManager.queryCapitalInfo(id);
			if (result != null) {
				return Response.successResponse(result);
			} else {//如果三方账户没有数据，那么
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_491, ResponseConstants.CommonMessage.RESPONSE_MSG_491, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_499, ResponseConstants.CommonMessage.RESPONSE_MSG_499);
		}
	}

	/**
	 * 月收益变动
	 * 
	 * @return
	 */
	@POST
	@Path("/queryincomeChange")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryincomeChange(Map<String, Object> paramMap) {
		try {
			ThirdAccountView tav = new ThirdAccountView();
			Account acc = new Account();
			Long id = p2pSessionContext.getCurrentAid();
			acc.setAccountSequence(id);
			//逾期
			List<ClaimGatherPlan> ai = claimGatherPlanRepository.findByGatherAccountSequenceAndClaimGatherPlanStatus(id,
					ClaimGatherPlanStatusEnum.OVERDUE.getCode());
			Date current = new Date();
			Date minDate = DateFormatUtil.getFirstMonth();
			Date maxDate = DateFormatUtil.getLastMonth();

			//本月未收
			List<ClaimGatherPlan> wait = claimGatherPlanRepository
					.findByGatherAccountSequenceAndClaimGatherPlanStatusAndClaimGatherPlanNatureDateLessThanAndClaimGatherPlanNatureDateGreaterThan(
							id, ClaimGatherPlanStatusEnum.NOT_PAID.getCode(), maxDate, current);
			//本月已收
			List<ClaimGatherPlan> have = claimGatherPlanRepository
					.findByGatherAccountSequenceAndClaimGatherPlanStatusAndClaimGatherPlanNatureDateLessThanAndClaimGatherPlanNatureDateGreaterThan(
							id, ClaimGatherPlanStatusEnum.PAID.getCode(), current, minDate);

			//今日逾期投资项目
			Integer investProjectOverdue;
			//今日逾期投资金额
			BigDecimal investCapitalOverdue = new BigDecimal(0);
			if (ai != null) {
				investProjectOverdue = ai.size();
				tav.setInvestProjectOverdue(investProjectOverdue == null ? "" : investProjectOverdue.toString());
				for (ClaimGatherPlan c : ai) {
					investCapitalOverdue = investCapitalOverdue.add(c.getClaimGatherPlanPretendPrincipal())
							.add(c.getClaimGatherPlanPretendInterest()).add(c.getClaimGatherPlanPretendJusticeInterest());
				}
				tav.setInvestCapitalOverdue(investCapitalOverdue == null ? "" : investCapitalOverdue.toString());
			}
			//预计本月待收
			BigDecimal totalIncomeMonth = new BigDecimal(0);
			if (wait != null) {
				totalIncomeMonth.add(investCapitalOverdue);
				for (ClaimGatherPlan c : wait) {
					totalIncomeMonth.add(c.getClaimGatherPlanPretendPrincipal()).add(c.getClaimGatherPlanPretendInterest())
							.add(c.getClaimGatherPlanPretendJusticeInterest());
				}
				tav.setTotalIncomeMonth(totalIncomeMonth == null ? "" : totalIncomeMonth.toString());
			}
			//本月已收
			BigDecimal haveIncomeMonth = new BigDecimal(0);
			if (have != null) {
				for (ClaimGatherPlan c : have) {
					haveIncomeMonth.add(c.getClaimGatherPlanPretendPrincipal()).add(c.getClaimGatherPlanPretendInterest())
							.add(c.getClaimGatherPlanPretendJusticeInterest());
				}
				tav.setHaveIncomeMonth(haveIncomeMonth == null ? "" : haveIncomeMonth.toString());
			}
			return Response.successResponse(tav);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_499, ResponseConstants.CommonMessage.RESPONSE_MSG_499);
		}
	}

	@GET
	@Path("/queryCapitalInfoGet")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryCapitalInfoGet() {
		return queryCapitalInfo();
	}

	//	/**
	//	 * 资金账户开户
	//	 * @param acc
	//	 * @return
	//	 * @throws Exception 
	//	 */
	//	@POST
	//	@Path("/openCapitalAccount")
	//	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	//	@Consumes(MediaType.APPLICATION_JSON)
	//	public Response openCapitalAccount(Map<String, Object> paramMap) throws Exception {
	//		try{
	//			Long accId = p2pSessionContext.getCurrentAid();
	//			String thirdPpaymentName=(String) paramMap.get("thirdPpaymentName");//开户名
	//			Long thirdPaymentUniqueId=(Long) paramMap.get("thirdPaymentUniqueId");//账户名
	//			ThirdPaymentAccount  thirdPaymentAccount = capitalAccountManager.openCreditCapitalAccount(accId,thirdPpaymentName,thirdPaymentUniqueId);
	//			return Response.successResponse(thirdPaymentAccount);
	//		}catch(Exception e){
	//			e.printStackTrace();
	//			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_498,ResponseConstants.CommonMessage.RESPONSE_MSG_498);
	//		}
	//	}

	//	/**
	//	 * 创建贷款人账户后，开始创建借款人的资金账户 激活，绑定资金账号的银行卡
	//	 * 
	//	 * @param userid
	//	 * @return
	//	 * @throws Exception 
	//	 */
	//	@POST
	//	@Path("/activateCapitalInfo")
	//	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	//	@Consumes(MediaType.APPLICATION_JSON)
	//	public Response activateCapitalInfo(Map<String,Object> parameter) throws Exception {
	//		try{
	//			Long investmentId = Long.parseLong((String) parameter.get("investmentId"));
	//			String thirdAccountName = (String) parameter.get("thirdAccountName");
	//			Long thirdPaymentUniqueId=(Long) parameter.get("thirdPaymentUniqueId");//账户名
	//			Investment result = capitalAccountManager.activateCapitalInfo(investmentId,thirdAccountName,thirdPaymentUniqueId);
	//			return Response.successResponse(result);
	//		}catch(Exception e){
	//			e.printStackTrace();
	//			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_497,ResponseConstants.CommonMessage.RESPONSE_MSG_497);
	//		}
	//	}

	/**
	 * 计算每期的本金以及总收益
	 * 
	 * @param
	 * @return
	 */
	@POST
	@Path("/capitalMath")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response capitalMath(Map<String, Object> parameter) {
		/** 金额 */
		BigDecimal price = new BigDecimal(String.valueOf(parameter.get("price")));
		/** 期数 */
		Integer periods = Integer.valueOf(String.valueOf(parameter.get("periods")));
		/** 年利率 */
		BigDecimal yearRate = new BigDecimal(String.valueOf(parameter.get("yearRate")));
		/** 月利率 */
		double monthRate = yearRate.divide(new BigDecimal(12), ConstantsUtil.FLOD_INDEX, ConstantsUtil.ROUND_HALF_UP).doubleValue();
		BigDecimal totalPeriods = new BigDecimal(0);//总期数	本息
		BigDecimal firstPeriods = new BigDecimal(0);//一期	本息
		for (int i = periods; i > 0; i--) {
			BigDecimal everyPeriods = new BigDecimal(0);//每期还款	利息
			BigDecimal capital = new BigDecimal(0);//每期还款	本金
			everyPeriods = new BigDecimal(FinanceUtil.IPMT(monthRate, i, periods, price.doubleValue())).setScale(ConstantsUtil.FLOD_INDEX,
					BigDecimal.ROUND_HALF_UP);
			capital = new BigDecimal(FinanceUtil.PPMT(monthRate, i, periods, price.doubleValue())).setScale(ConstantsUtil.FLOD_INDEX,
					BigDecimal.ROUND_HALF_UP);
			firstPeriods = capital.add(everyPeriods);
			totalPeriods = totalPeriods.add(firstPeriods);
		}
		Map<String, String> res = new HashMap<String, String>();
		res.put("totalPeriods", totalPeriods.toString());
		res.put("firstPeriods", firstPeriods.toString());
		return Response.successResponse(res);
	}

	/**
	 * 登陆到汇付
	 * 
	 * @param userid
	 * @return
	 */
	@POST
	@Path("/getUserLoginParam")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserLoginParam(Map<String, String> parameter) {
		Map<String, String> returnVal = null;
		try {
			Long usrId = p2pSessionContext.getCurrentAid();
			returnVal = capitalAccountManager.getUserLoginParam(usrId);
			returnVal.put(SignUtils.URL, signUtils.getServerPath());
			return Response.successResponse(returnVal);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_499, ResponseConstants.CommonMessage.RESPONSE_MSG_499);
		}
	}

	/**
	 * 登陆到汇付
	 * 
	 * @param userid
	 * @return
	 */
	@POST
	@Path("/getUserLoginParamPlatform")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserLoginParamPlatform(Map<String, String> parameter) {
		Map<String, String> returnVal = null;
		try {
			Long usrId = Long.valueOf((String) parameter.get("usrId"));
			returnVal = capitalAccountManager.getUserLoginParam(usrId);
			returnVal.put(SignUtils.URL, signUtils.getServerPath());
			return Response.successResponse(returnVal);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_499, ResponseConstants.CommonMessage.RESPONSE_MSG_499);
		}
	}

	/**
	 * 检索获得当前用户的投资项目信息
	 * 
	 * @param userid
	 * @return
	 */
	@POST
	@Path("/queryUserInvestmentInfoForPlatform")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryUserInvestmentInfoForPlatform(Map<String, Object> paramMap) {
		Long id = Long.valueOf((String) paramMap.get("accountInvestmentSeq"));//accountinvSeq

		AccountInvestment accountInvestment = accountInvestmentRepository.findOne(id);

		ClaimGatherPlan claimGatherPlan = claimGatherPlanRepository.findByAccountInvestmentSequenceAndClaimGatherPlanNumber(
				accountInvestment.getAccountInvestmentSequence(), accountInvestment.getAccountInvestmentPayedPeriod() + 1);
		// 剩余本金    投资总额-已收本金/剩余本金+应还本金
		BigDecimal surplusCapital = BigDecimal.ZERO;
		if (claimGatherPlan != null) {
			// 剩余本金
			surplusCapital = claimGatherPlan.getClaimGatherPlanPretendSurplus().add(claimGatherPlan.getClaimGatherPlanPretendPrincipal());
		}

		claimGatherPlan = claimGatherPlanRepository.findByAccountInvestmentSequenceAndClaimGatherPlanNumber(
				accountInvestment.getAccountInvestmentSequence(), 1);
		BigDecimal monthRepayCapital = BigDecimal.ZERO;
		if (claimGatherPlan != null) {
			// 未收本金
			monthRepayCapital = claimGatherPlan.getClaimGatherPlanPretendTotalAmount();
		}

		BigDecimal surplusPrincipalAndInterest = claimGatherPlanRepository.sumNotGatherGpptotal(id,
				accountInvestment.getAccountInvestmentPayedPeriod());

		// Integer payedPeriod = dealServiceReferenceManager.wytz0101PayedPeriod(accountInvestment.getInvestmentSequence());
		Integer payedPeriod = accountInvestment.getAccountInvestmentPayedPeriod();

		InvestmentInfoDto investmentInfoDto = new InvestmentInfoDto();
		investmentInfoDto.setInitialCapital(String.valueOf(accountInvestment.getAccountInvestmentQuota()));// 投资成功的金额
		investmentInfoDto.setSurplusCapital(String.valueOf(surplusCapital));// 未收本金
		investmentInfoDto.setMonthRepayCapital(String.valueOf(monthRepayCapital));// 月还款额
		investmentInfoDto.setSurplusPrincipalAndInterest(String.valueOf(surplusPrincipalAndInterest));////未收本息
		investmentInfoDto.setPayedPeriod(String.valueOf(payedPeriod));

		return Response.successResponse(investmentInfoDto);
	}
}