package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.capital.modal.CapitalPlatformManager;
import com.vcredit.jdev.p2p.dto.PayResult;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.AccountOrderHistory;
import com.vcredit.jdev.p2p.entity.ClaimGatherPlan;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.entity.ClaimPayPlanHistory;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;
import com.vcredit.jdev.p2p.entity.LoanCut;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.ClaimGatherPlanStatusEnum;
import com.vcredit.jdev.p2p.enums.ClaimPayPlanStatusEnum;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.MerPrivEnum;
import com.vcredit.jdev.p2p.enums.OrderIDRuleEnum;
import com.vcredit.jdev.p2p.enums.OrderStatusEnum;
import com.vcredit.jdev.p2p.enums.RecStatusEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountTypeEnum;
import com.vcredit.jdev.p2p.enums.TradeTypeEnum;
import com.vcredit.jdev.p2p.repository.AccountOrderHistoryRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanHistoryRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanCutRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;
import com.vcredit.jdev.p2p.util.CollectionUtil;

/**
 * p2p平台债权垫付 。
 * 
 * @author zhuqiu
 *
 */
@Component
public class BondTransferManager {
	private static final Integer OVERDUE_PAY_DAYS = 7;
	/** 投资项目 */
	@Autowired
	private InvestmentRepository investmentRepository;

	/** 债权还款计划 */
	@Autowired
	private ClaimPayPlanRepository claimPayPlanRepository;

	/** 交易模块Repository */
	@Autowired
	private DealRepository dealRepository;

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;

	/** 用户订单状态历史 */
	@Autowired
	private AccountOrderHistoryRepository accountOrderHistoryRepository;

	/** 债权收款计划 */
	@Autowired
	private ClaimGatherPlanRepository claimGatherPlanRepository;

	@Autowired
	private AccountThirdRepository accountThirdRepository;

	@Autowired
	private CapitalPlatformManager capitalPlatformManager;

	@Autowired
	private BondPayOffManager bondPayOffManager;

	@Autowired
	private ClaimPayPlanHistoryRepository claimPayPlanHistoryRepository;

	@Autowired
	private LoanCutRepository loanCutRepository;

	@Autowired
	private LoanDataRepository loanDataRepository;

	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;

	@Autowired
	private DictionaryUtil dictionaryUtil;

	/**
	 * 查询垫付清单历史的逾期天数和罚息
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Map<String, String> searchPrepaidList(Long claimPayPlanSequence) {
		//可以逾期天数
		int claimPayPlanDelayDays = 0;
		BigDecimal claimPayPlanJusticeInterest = BigDecimal.ZERO;

		// 查询逾期可垫付
		//List<ClaimPayPlan> claimPayPlanList = claimPayPlanRepository.findBondForTranfer(ClaimPayPlanStatusEnum.OVERDUE.getCode());
		//ClaimPayPlan claimPayPlan = claimPayPlanRepository.findOne(claimPayPlanSequence);
		//查询垫付清单
		ClaimPayPlanHistory claimPayPlanHistory = claimPayPlanHistoryRepository.findOverduePaidPlanHistoryByStatus(claimPayPlanSequence,
				OVERDUE_PAY_DAYS, ClaimPayPlanStatusEnum.OVERDUE.getCode());

		if (claimPayPlanHistory != null) {
			//用垫付用历史记录清单的罚息，逾期天数，执行垫付。
			claimPayPlanDelayDays = claimPayPlanHistory.getClaimPayPlanDelayDays();
			claimPayPlanJusticeInterest = claimPayPlanHistory.getClaimPayPlanJusticeInterest();
		}

		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("claimPayPlanDelayDays", String.valueOf(claimPayPlanDelayDays));
		resultMap.put("claimPayPlanJusticeInterest", String.valueOf(claimPayPlanJusticeInterest));
		return resultMap;
	}

	/**
	 * 垫付平台管理费和风险备用金
	 * 
	 * @return
	 */
	public Response bondPrepaid(Long claimPayPlanSequence) {
		AccountOrder feeOrder = null;
		AccountOrder riskfeeOrder = null;
		PayResult payResultFee;
		PayResult payResultRisk;

		ClaimPayPlan claimPayPlan = claimPayPlanRepository.findOne(claimPayPlanSequence);

		if (claimPayPlan == null) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "债权还款计划不存在");
		}

		if (ClaimPayPlanStatusEnum.OVERDUE.getCode().intValue() != claimPayPlan.getClaimPayPlanStatus().intValue()) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "债权还款计划状态不正确");
		}

		//查询垫付清单
		ClaimPayPlanHistory claimPayPlanHistory = claimPayPlanHistoryRepository.findOverduePaidPlanHistoryByStatus(claimPayPlanSequence,
				OVERDUE_PAY_DAYS, ClaimPayPlanStatusEnum.OVERDUE.getCode());
		if (claimPayPlanHistory == null) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "垫付清单没有生成");
		}
		//用垫付用历史记录清单的罚息，逾期天数，执行垫付。
		claimPayPlan.setClaimPayPlanDelayDays(claimPayPlanHistory.getClaimPayPlanDelayDays());
		claimPayPlan.setClaimPayPlanJusticeInterest(claimPayPlanHistory.getClaimPayPlanJusticeInterest());

		Long p2pPrepaidAccount = null;//SDT1垫付户
		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_PAYFORSOMEBODY_ACCOUNT.getCode());

		if (CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			p2pPrepaidAccount = 0000L;
		} else {
			p2pPrepaidAccount = thirdPaymentAccountList.get(0).getAccountSequence();
		}

		Investment investment = investmentRepository.findOne(claimPayPlan.getInvestmentSequence());

		//检查是否已经存在 订单
		List<AccountOrder> feeOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndTradeType(
				claimPayPlan.getClaimPayPlanSequence(), TradeTypeEnum.P2P_ACCOUNT_SERVICE.getCode());
		if (!CollectionUtil.isEmpty(feeOrderList)) {
			feeOrder = feeOrderList.get(0);

			if (OrderStatusEnum.PAYMENT_FALIUE.getCode().compareTo(feeOrder.getOrderStatus()) == 0) {
				//失败场合 补单
				//2.1. 调用第三方支付
				payResultFee = hFTransfer2Platform(feeOrder, p2pPrepaidAccount);
				bondPayOffManager.platformManagementAfter(payResultFee.getOrdId(), payResultFee.isResult(), payResultFee.getMessage());
			} else if (OrderStatusEnum.PAYING.getCode().compareTo(feeOrder.getOrderStatus()) == 0) {
				return Response.response(ResponseConstants.CommonCode.SUCCESS_CODE, "P2P平台账户管理服务费正在处理中...");
			} else {
				payResultFee = new PayResult();
				payResultFee.setResult(true);
				payResultFee.setOrdId(String.valueOf(feeOrder.getCashFlowId()));
			}
		} else {
			Long p2pPfAccount = null;//平台资金账户
			thirdPaymentAccountList = accountThirdRepository.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_ACCOUNT.getCode());
			if (CollectionUtil.isEmpty(thirdPaymentAccountList)) {
				p2pPfAccount = 0000L;
			} else {
				p2pPfAccount = thirdPaymentAccountList.get(0).getAccountSequence();
			}

			// 误差调整
			BigDecimal pmFee = doBalance(claimPayPlan, p2pPrepaidAccount, investment, p2pPfAccount);

			//1.1  生成订单P2P平台管理费
			feeOrder = crePlatformManagementFeesOrder(claimPayPlan, p2pPrepaidAccount, p2pPfAccount, investment, pmFee);
			payResultFee = hFTransfer2Platform(feeOrder, p2pPrepaidAccount);
			bondPayOffManager.platformManagementAfter(payResultFee.getOrdId(), payResultFee.isResult(), payResultFee.getMessage());
		}

		//取得失败  订单信息
		List<AccountOrder> riskfeeOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndTradeType(
				claimPayPlan.getClaimPayPlanSequence(), TradeTypeEnum.P2P_ACCOUNT_RISK_RESERVE_FUND.getCode());
		if (!CollectionUtil.isEmpty(riskfeeOrderList)) {
			riskfeeOrder = riskfeeOrderList.get(0);
			if (OrderStatusEnum.PAYMENT_FALIUE.getCode().compareTo(riskfeeOrder.getOrderStatus()) == 0) {
				//失败场合 补单
				//2.1. 调用第三方支付
				payResultRisk = hFTransfer2Platform(riskfeeOrder, p2pPrepaidAccount);
				bondPayOffManager.riskReserveFundFeeAfter(payResultRisk.getOrdId(), payResultRisk.isResult(), payResultRisk.getMessage());

			} else if (OrderStatusEnum.PAYING.getCode().compareTo(riskfeeOrder.getOrderStatus()) == 0) {
				return Response.response(ResponseConstants.CommonCode.SUCCESS_CODE, "p2p平台账户还款风险备用金正在处理中...");
			} else {
				payResultRisk = new PayResult();
				payResultRisk.setOrdId(String.valueOf(riskfeeOrder.getCashFlowId()));
				payResultRisk.setResult(true);
			}
		} else {
			//1.2 生成订单P2P风险备用金
			riskfeeOrder = creRiskFundFeesOrder(claimPayPlan, p2pPrepaidAccount, investment);
			payResultRisk = hFTransfer2Platform(riskfeeOrder, p2pPrepaidAccount);
			bondPayOffManager.riskReserveFundFeeAfter(payResultRisk.getOrdId(), payResultRisk.isResult(), payResultRisk.getMessage());
		}

		//4.
		if (!payResultFee.isResult() || !payResultRisk.isResult()) {
			//有支付失败的场合
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, payResultFee.getMessage() + " " + payResultRisk.getMessage());
		}

		//5.债权还款计划状态
		this.updateClaimPayPlan(payResultRisk.getOrdId(), true);

		return Response.response(ResponseConstants.CommonCode.SUCCESS_CODE, "平台管理费和风险备用金垫付成功");
	}

	public BigDecimal doBalance(ClaimPayPlan claimPayPlan, Long p2pPrepaidAccount, Investment investment, Long p2pPfAccount) {
		// 误差调整
		// 投资人利息罚息总和
		BigDecimal sumGatherInterest = claimGatherPlanRepository.sumInterestByInvSeqAndPeriod(claimPayPlan.getInvestmentSequence(),
				claimPayPlan.getClaimPayPlanNumber());
		// 借款人利息罚息总和
		BigDecimal sumPayInterest = claimPayPlan.getClaimPayPlanTotalAmount().add(claimPayPlan.getClaimPayPlanJusticeInterest());

		BigDecimal pmFee = claimPayPlan.getClaimPayPlanPlatformManagementFee();
		BigDecimal balance = BigDecimal.ZERO;

		// 利息罚息尾数误差处理
		if (sumPayInterest.compareTo(sumGatherInterest) > 0) {
			balance = sumPayInterest.subtract(sumGatherInterest);
			pmFee = pmFee.add(balance);

			creBalanceOrder(claimPayPlan, investment, pmFee, p2pPrepaidAccount, p2pPfAccount);
		} else if (sumPayInterest.compareTo(sumGatherInterest) < 0) {
			balance = sumGatherInterest.subtract(sumPayInterest);
			pmFee = pmFee.subtract(balance);

			creBalanceOrder(claimPayPlan, investment, pmFee, p2pPfAccount, p2pPrepaidAccount);
		}
		return pmFee;
	}

	private PayResult hFTransfer2Platform(AccountOrder feeOrder, Long p2pPrepaidAccount) {
		PayResult payResultFee;
		try {
			payResultFee = capitalPlatformManager.transfer2Platform(String.valueOf(feeOrder.getCashFlowId()), feeOrder.getGatherAccountSequence(),
					p2pPrepaidAccount, feeOrder.getTradeAmount(), MerPrivEnum.PREPAID.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			payResultFee = new PayResult();
			payResultFee.setResult(false);
			payResultFee.setMessage(e.getMessage());
		}
		return payResultFee;
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public synchronized void updateClaimPayPlan(String orderId, boolean isResult) {
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);
		AccountOrder accountOrder = accountOrderList.get(0);

		Long count = accountOrderRepository.getCountPayPlanNotSuccess(accountOrder.getCommodityTablePrimaryKeyValue());
		if (count == 0) {
			ClaimPayPlan claimPayPlan = claimPayPlanRepository.findOne(accountOrder.getCommodityTablePrimaryKeyValue());

			//取得项目的借款人
			Long loanDataSequence = null;
			LoanData loanData = null;
			LoanCut loanCut = null;

			List<InvestmentAccountReference> investmentAccountReferenceList = investmentAccountReferenceRepository
					.findByInvestmentSequence(claimPayPlan.getInvestmentSequence());
			if (!CollectionUtil.isEmpty(investmentAccountReferenceList)) {
				loanDataSequence = investmentAccountReferenceList.get(0).getLoanDataSequence();
				loanData = loanDataRepository.findOne(loanDataSequence);

				if (loanData != null) {
					loanCut = loanCutRepository.findByInvestmentBusinessCodeAndLoanPeriod(loanData.getInvestmentBusinessCode(),
							claimPayPlan.getClaimPayPlanNumber());
				}
			}

			if (loanCut != null && loanCut.getTotalShouldGet().compareTo(loanCut.getTotalActulGet()) == 0) {
				//若垫付时借款人已还
				claimPayPlan.setClaimPayPlanStatus(ClaimPayPlanStatusEnum.PAID.getCode());
				claimPayPlan.setRecordEditDate(new Date());
				claimPayPlanRepository.save(claimPayPlan);

				Investment investment = investmentRepository.findOne(claimPayPlan.getInvestmentSequence());
				//期数
				final int investmentPeriod = Integer.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode()
						+ DictionaryEnum.T_INV_INV_PERIOD.getCode() + investment.getInvestmentPeriod()));
				//最后一期还款成功
				if (investmentPeriod == claimPayPlan.getClaimPayPlanNumber().intValue()) {
					investment.setInvestmentStatus(InvestmentStatusEnum.REPAY_FINISH.getCode());
					investment.setInvestmentOverDate(new Date());
					investmentRepository.save(investment);
				}
			} else {
				//final Date sysDate = dealRepository.getSystemTime();
				// 更新债权还款计划状态-〉 逾期已垫付
				claimPayPlan.setClaimPayPlanStatus(ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode());
				claimPayPlan.setRecordEditDate(new Date());
				claimPayPlanRepository.save(claimPayPlan);

				insertClaimPayPlanHistory(new Date(), claimPayPlan);
			}
		}
	}

	/**
	 * 汇付回调。
	 * 
	 * @return
	 */
	public void transferHFAfterForPrepaid(String orderId, boolean isSuccess, String msg) {

		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);
		AccountOrder accountOrder = accountOrderList.get(0);
		if (accountOrder.getTradeType().intValue() == TradeTypeEnum.P2P_ACCOUNT_SERVICE.getCode().intValue()) {
			bondPayOffManager.platformManagementAfter(orderId, isSuccess, msg);
		} else if (accountOrder.getTradeType().intValue() == TradeTypeEnum.P2P_ACCOUNT_RISK_RESERVE_FUND.getCode().intValue()) {
			bondPayOffManager.riskReserveFundFeeAfter(orderId, isSuccess, msg);
		}
		updateClaimPayPlan(orderId, isSuccess);
	}

	/**
	 * 垫付收款处理。（垫付账户—〉投资人账户）
	 * 
	 * @param claimGatherPlanSequence
	 *            收款计划流水
	 * 
	 * @return
	 */
	public Response prepaidGather(Long claimGatherPlanSequence) {
		ClaimGatherPlan claimGatherPlan = claimGatherPlanRepository.findOne(claimGatherPlanSequence);

		if (ClaimGatherPlanStatusEnum.NOT_PAID.getCode().intValue() != claimGatherPlan.getClaimGatherPlanStatus().intValue()
				&& ClaimGatherPlanStatusEnum.OVERDUE.getCode().intValue() != claimGatherPlan.getClaimGatherPlanStatus().intValue()
				&& ClaimGatherPlanStatusEnum.PAID_FAILURE.getCode().intValue() != claimGatherPlan.getClaimGatherPlanStatus().intValue()) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "债权收款只能未收和收款失败");
		}

		Long p2pPrepaidAccount = null;//SDT1垫付户
		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_PAYFORSOMEBODY_ACCOUNT.getCode());

		if (CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "SDT1垫付账户不存在");
		} else {
			p2pPrepaidAccount = thirdPaymentAccountList.get(0).getAccountSequence();
		}

		return bondPayOffManager.exeGather(claimGatherPlan, p2pPrepaidAccount, TradeTypeEnum.PREPAID_GATHERING);

	}

	/**
	 * 平台管理费订单。
	 * 
	 * @param claimPayPlan
	 *            债券还款计划
	 * @param payAccountSequence
	 *            付款用户账号
	 * 
	 * @return {@link AccountOrder}
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public AccountOrder crePlatformManagementFeesOrder(ClaimPayPlan claimPayPlan, Long payAccountSequence, Long p2pPfAccount, Investment investment,
			BigDecimal pmFee) {
		BigDecimal thirdPaymentIdBalance = BigDecimal.ZERO;
		//P2P平台管理费
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(claimPayPlan.getPayAccountSequence());
		if (thirdPaymentAccount != null) {
			thirdPaymentIdBalance = thirdPaymentAccount.getThirdPaymentIdBalance();
		}

		//新增订单
		AccountOrder accountOrder = new AccountOrder();
		accountOrder.setTradeDate(new Date());
		accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
		accountOrder.setTradeType(TradeTypeEnum.P2P_ACCOUNT_SERVICE.getCode());//交易类型
		accountOrder.setTradeAmount(pmFee);
		accountOrder.setCommodityTablePrimaryKeyValue(claimPayPlan.getClaimPayPlanSequence());
		accountOrder.setPayAccountSequence(payAccountSequence);
		accountOrder.setGatherAccountSequence(p2pPfAccount);

		accountOrder.setTradeDescription(investment.getInvestmentName());
		accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FEE));
		accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentIdBalance);
		accountOrder.setGatherThirdPaymentIdBalance(BigDecimal.ZERO);
		accountOrder.setOrderEditDate(new Date());
		accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
		accountOrder = accountOrderRepository.save(accountOrder);
		//生成用户订单状态历史
		AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
		accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
		accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
		accountOrderHistory.setOrderStatusChangeDate(new Date());
		accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

		return accountOrder;
	}

	/**
	 * 风险备用金订单。
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public AccountOrder creRiskFundFeesOrder(ClaimPayPlan claimPayPlan, Long payAccountSequence, Investment investment) {
		BigDecimal thirdPaymentIdBalance = BigDecimal.ZERO;
		//P2P平台管理费
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(claimPayPlan.getPayAccountSequence());
		if (thirdPaymentAccount != null) {
			thirdPaymentIdBalance = thirdPaymentAccount.getThirdPaymentIdBalance();
		}
		// 风险备用金
		BigDecimal claimPayPlanRiskFee = claimPayPlan.getClaimPayPlanRiskFee();

		Long p2pGatherAccount = null;
		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_RISK_ACCOUNT.getCode());
		if (CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			p2pGatherAccount = 0000L;
		} else {
			p2pGatherAccount = thirdPaymentAccountList.get(0).getAccountSequence();
		}

		//新增订单
		AccountOrder accountOrder = new AccountOrder();
		accountOrder.setTradeDate(new Date());
		accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
		accountOrder.setTradeType(TradeTypeEnum.P2P_ACCOUNT_RISK_RESERVE_FUND.getCode());//交易类型
		accountOrder.setTradeAmount(claimPayPlanRiskFee);
		accountOrder.setCommodityTablePrimaryKeyValue(claimPayPlan.getClaimPayPlanSequence());
		accountOrder.setPayAccountSequence(payAccountSequence);
		accountOrder.setGatherAccountSequence(p2pGatherAccount);

		accountOrder.setTradeDescription(investment.getInvestmentName());
		accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FEE));
		accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentIdBalance);
		accountOrder.setGatherThirdPaymentIdBalance(BigDecimal.ZERO);
		accountOrder.setOrderEditDate(new Date());
		accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
		accountOrder = accountOrderRepository.save(accountOrder);
		//生成用户订单状态历史
		AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
		accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
		accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
		accountOrderHistory.setOrderStatusChangeDate(new Date());
		accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

		return accountOrder;
	}

	/**
	 * 利息和罚息误差记录。
	 * 
	 * @param claimPayPlan
	 *            债券还款计划
	 * 
	 * @return null
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void creBalanceOrder(ClaimPayPlan claimPayPlan, Investment investment, BigDecimal pmFee, Long payAccountSequence,
			Long gatherAccountSequence) {
		//新增订单
		AccountOrder balanceOrder = new AccountOrder();
		balanceOrder.setTradeDate(new Date());
		balanceOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());//订单状态
		balanceOrder.setTradeType(TradeTypeEnum.DEFAULT_INTEREST_BALANCE.getCode());//交易类型
		balanceOrder.setTradeAmount(pmFee);
		balanceOrder.setCommodityTablePrimaryKeyValue(claimPayPlan.getClaimPayPlanSequence());
		balanceOrder.setPayAccountSequence(payAccountSequence);
		balanceOrder.setGatherAccountSequence(gatherAccountSequence);

		balanceOrder.setTradeDescription(investment.getInvestmentName());
		balanceOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FEE));
		balanceOrder.setPayerThirdPaymentIdBalance(BigDecimal.ZERO);
		balanceOrder.setGatherThirdPaymentIdBalance(BigDecimal.ZERO);
		balanceOrder.setOrderEditDate(new Date());
		balanceOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
		accountOrderRepository.save(balanceOrder);
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void insertClaimPayPlanHistory(final Date sysDate, ClaimPayPlan claimPayPlan) {
		ClaimPayPlanHistory claimPayPlanHistory = new ClaimPayPlanHistory();
		claimPayPlanHistory.setClaimPayPlanCreateDate(claimPayPlan.getClaimPayPlanCreateDate());
		claimPayPlanHistory.setClaimPayPlanDelayDays(claimPayPlan.getClaimPayPlanDelayDays());
		claimPayPlanHistory.setClaimPayPlanInterest(claimPayPlan.getClaimPayPlanInterest());
		claimPayPlanHistory.setClaimPayPlanJusticeInterest(claimPayPlan.getClaimPayPlanJusticeInterest());
		claimPayPlanHistory.setClaimPayPlanNatureDate(claimPayPlan.getClaimPayPlanNatureDate());
		claimPayPlanHistory.setClaimPayPlanNumber(claimPayPlan.getClaimPayPlanNumber());
		claimPayPlanHistory.setClaimPayPlanPeriod(claimPayPlan.getClaimPayPlanPeriod());
		claimPayPlanHistory.setClaimPayPlanPlatformManagementFee(claimPayPlan.getClaimPayPlanPlatformManagementFee());
		claimPayPlanHistory.setClaimPayPlanPrincipal(claimPayPlan.getClaimPayPlanPrincipal());
		claimPayPlanHistory.setClaimPayPlanRiskFee(claimPayPlan.getClaimPayPlanRiskFee());
		claimPayPlanHistory.setClaimPayPlanSequence(claimPayPlan.getClaimPayPlanSequence());
		claimPayPlanHistory.setClaimPayPlanStatus(claimPayPlan.getClaimPayPlanStatus());
		claimPayPlanHistory.setClaimPayPlanSubsidy(claimPayPlan.getClaimPayPlanSubsidy());
		claimPayPlanHistory.setClaimPayPlanSurplus(claimPayPlan.getClaimPayPlanSurplus());
		claimPayPlanHistory.setClaimPayPlanTotalAmount(claimPayPlan.getClaimPayPlanTotalAmount());
		claimPayPlanHistory.setClaimPayPlanUnkownDays(claimPayPlan.getClaimPayPlanUnkownDays());
		claimPayPlanHistory.setInvestmentSequence(claimPayPlan.getInvestmentSequence());
		claimPayPlanHistory.setPayAccountSequence(claimPayPlan.getPayAccountSequence());
		claimPayPlanHistory.setPayRiskAccountSequence(claimPayPlan.getPayRiskAccountSequence());
		claimPayPlanHistory.setPayServiceAccountSequence(claimPayPlan.getPayServiceAccountSequence());
		claimPayPlanHistory.setRecordCreateDate(sysDate);
		claimPayPlanHistory.setRecordEditDate(sysDate);
		claimPayPlanHistoryRepository.save(claimPayPlanHistory);
	}
}
