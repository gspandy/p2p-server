package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.capital.modal.CapitalPlatformManager;
import com.vcredit.jdev.p2p.dto.AlertDto;
import com.vcredit.jdev.p2p.dto.PayResult;
import com.vcredit.jdev.p2p.dto.RePayDto;
import com.vcredit.jdev.p2p.entity.AccountFeePaymentRecord;
import com.vcredit.jdev.p2p.entity.AccountInvestment;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.AccountOrderHistory;
import com.vcredit.jdev.p2p.entity.ClaimGatherPlan;
import com.vcredit.jdev.p2p.entity.ClaimGatherRecord;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.entity.ClaimPayPlanHistory;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.AccountInvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.AprStatusEnum;
import com.vcredit.jdev.p2p.enums.AprTypeEnum;
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
import com.vcredit.jdev.p2p.repository.AccountFeePaymentRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountInvestmentRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderHistoryRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherRecordRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanHistoryRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayRecordRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanCutRepository;
import com.vcredit.jdev.p2p.util.CollectionUtil;
import com.vcredit.jdev.p2p.util.DateUtil;

/**
 * 正常还款，逾期还款。
 * 
 * @author zhuqiu
 *
 */
@Component
public class BondPayOffManager {
	private final Logger logger = LoggerFactory.getLogger(BondPayOffManager.class);
	private static final int OVERDUE_PAY_DAYS = 7;

	/** 投资项目 */
	@Autowired
	private InvestmentRepository investmentRepository;
	/** 用户获得的项目 */
	@Autowired
	private AccountInvestmentRepository accountInvestmentRepository;
	/** 债权还款计划 */
	@Autowired
	private ClaimPayPlanRepository claimPayPlanRepository;

	@Autowired
	private ClaimPayPlanHistoryRepository claimPayPlanHistoryRepository;

	/** 债权还款记录 */
	@Autowired
	private ClaimPayRecordRepository claimPayRecordRepository;
	/** 小贷公司扣款信息 */
	@Autowired
	private LoanCutRepository loanCutRepository;

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;

	/** 用户订单状态历史 */
	@Autowired
	private AccountOrderHistoryRepository accountOrderHistoryRepository;

	/** 债权收款计划 */
	@Autowired
	private ClaimGatherPlanRepository claimGatherPlanRepository;

	/** 债权收款计划 */
	@Autowired
	private ClaimGatherRecordRepository claimGatherRecordRepository;

	/** 投资项目与贷款人关系 */
	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;

	/** 用户缴费记录 */
	@Autowired
	private AccountFeePaymentRecordRepository accountFeePaymentRecordRepository;

	@Autowired
	private AccountThirdRepository accountThirdRepository;

	@Autowired
	private CapitalPlatformManager capitalPlatformManager;

	@Autowired
	private DictionaryUtil dictionaryUtil;

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private EventMessageGateway eventMessageGateway;

	/**
	 * T+1日还款清单（登记）。
	 * <p>
	 * 查询从VBS正常还款的期次的清单，包括管理费，本息，罚息。
	 * </p>
	 * 
	 * @return
	 */
	//	@Transactional
	//	public List<ClaimPayPlan> payoffList() {
	//		Long investmentSequence = null;//投资项目流水号
	//		String investmentBusinessCode = null;
	//		Integer peroidOfTime = null;
	//		List<ClaimPayPlan> claimPayPlanList = new ArrayList<ClaimPayPlan>();
	//		//1.取得VBS反馈结果
	//		List<LoanCut> loanCutList = loanCutRepository.findByRecordStatus(RecStatEnum.UNTREATED.getCode());
	//		for (LoanCut loanCut : loanCutList) {
	//			//2.VBS扣款结果【已还】
	//			if (loanCut.getTotalActulGet().compareTo(loanCut.getTotalShouldGet()) == 0) {
	//				investmentBusinessCode = loanCut.getInvestmentBusinessCode();
	//				peroidOfTime = loanCut.getLoanPeriod();
	//					InvestmentAccountReference investmentAccountReference = autoInvestRepository
	//							.findInvestmentSequenceByBusinessCode(investmentBusinessCode);
	//				investmentSequence = investmentAccountReference.getInvestmentSequence();
	//				Long accountSequence = investmentAccountReference.getAccountSequence();
	//
	//				// 查询债权还款计划
	//				ClaimPayPlan claimPayPlan = claimPayPlanRepository.findByInvestmentSequenceAndClaimPayPlanNumberAndPayAccountSequence(
	//						investmentSequence, peroidOfTime, accountSequence);
	//
	//				claimPayPlanList.add(claimPayPlan);
	//			}
	//		}
	//
	//		return claimPayPlanList;
	//	}

	/**
	 * T+1日还款清单（登记）。
	 * <p>
	 * 查询从VBS正常还款的期次的清单，包括管理费，本息，罚息。
	 * </p>
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<ClaimPayPlan> tDayPayoffList(Integer claimPayPlanStatus1) {
		return claimPayPlanRepository.findTDayPayList(claimPayPlanStatus1);

	}

	/**
	 * 正常还款处理 ：T+1日根据贷款人还款计划，支付本金，利息，逾期罚金。 </br>
	 * <p>
	 * 如果状态[已垫付]-〉维信平台 </br> 如果状态[未垫付]-〉给各投资人
	 * </p>
	 * 
	 * @param claimPayPlanSequence
	 *            债权还款计划流水号
	 * 
	 * @return
	 */
	public Response normalPay(Long claimPayPlanSequence) {

		// p2p还款资金账户
		Long p2pPayAccount = null;
		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_RECHARGE_WITHDRAW_ACCOUNT.getCode());
		if (CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			p2pPayAccount = 1L;
		} else {
			p2pPayAccount = thirdPaymentAccountList.get(0).getAccountSequence();
		}

		ClaimPayPlan claimPayPlan = claimPayPlanRepository.findOne(claimPayPlanSequence);
		if (claimPayPlan == null) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "债权还款计划不存在");
		}

		Investment investment = investmentRepository.findOne(claimPayPlan.getInvestmentSequence());

		Long borrowerSequence = claimPayPlan.getPayAccountSequence();
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(borrowerSequence);
		if (thirdPaymentAccount == null) {
			return new Response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "第三方支付账号未开通!", null);
		}

		int claimPayPlanStatus = claimPayPlan.getClaimPayPlanStatus().intValue();
		if (ClaimPayPlanStatusEnum.VBS_PAID.getCode().intValue() != claimPayPlanStatus
				&& ClaimPayPlanStatusEnum.PAY_FAILURE.getCode().intValue() != claimPayPlanStatus
				&& ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode().intValue() != claimPayPlanStatus
				&& ClaimPayPlanStatusEnum.OVERDUE.getCode().intValue() != claimPayPlanStatus) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "债权还款状态只能未付和付款失败！");
		}

		AccountOrder accountOrder = null;
		AccountOrder feeOrder = null;
		AccountOrder riskfeeOrder = null;
		if (ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode().intValue() == claimPayPlanStatus) {
			//已经垫付
			updateClaimPayPlan(claimPayPlan, ClaimPayPlanStatusEnum.PAID);

		} else if (ClaimPayPlanStatusEnum.VBS_PAID.getCode().intValue() == claimPayPlanStatus
				|| ClaimPayPlanStatusEnum.OVERDUE.getCode().intValue() == claimPayPlanStatus) {

			Long p2pPfAccount = null;//平台资金账户
			thirdPaymentAccountList = accountThirdRepository.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_ACCOUNT.getCode());
			if (CollectionUtil.isEmpty(thirdPaymentAccountList)) {
				p2pPfAccount = 0000L;
			} else {
				p2pPfAccount = thirdPaymentAccountList.get(0).getAccountSequence();
			}

			// 投资人本息罚息总和
			BigDecimal sumGatherInterest = claimGatherPlanRepository.sumInterestByInvSeqAndPeriod(claimPayPlan.getInvestmentSequence(),
					claimPayPlan.getClaimPayPlanNumber());
			// 误差调整
			BigDecimal pmFee = doBalance(claimPayPlan, investment, p2pPfAccount, sumGatherInterest);

			//1.1  分账订单生成
			accountOrder = createSeparateAccountsOrder(borrowerSequence, claimPayPlan, thirdPaymentAccount, p2pPayAccount, investment,
					sumGatherInterest);
			//1.2 P2P平台管理费
			feeOrder = crePlatformManagementFeesOrder(claimPayPlan, investment, pmFee, p2pPfAccount);
			//1.3 P2P风险备用金
			riskfeeOrder = creRiskFundFeesOrder(claimPayPlan, investment);

			//2.1. 调用第三方支付接口，分账   P2P还款资金账户→各贷款客户第三方支付账户
			PayResult payResult = null;
			try {
				payResult = capitalPlatformManager.transfer2User(String.valueOf(accountOrder.getCashFlowId()), borrowerSequence, p2pPayAccount,
						accountOrder.getTradeAmount(), MerPrivEnum.NORMAL_PAID.getCode());
			} catch (Exception e) {
				e.printStackTrace();
				payResult.setResult(false);
				payResult.setOrdId(String.valueOf(accountOrder.getCashFlowId()));
				payResult.setMessage("异常" + e.getMessage());
			}

			//2.2. 后处理
			this.separateAccountsAfter(payResult.getOrdId(), payResult.isResult(), payResult.getMessage());

			//3.1. 调用第三方支付接口
			PayResult payResultFee = null;
			try {
				payResultFee = capitalPlatformManager.transfer2Platform(String.valueOf(feeOrder.getCashFlowId()),
						feeOrder.getGatherAccountSequence(), p2pPayAccount, feeOrder.getTradeAmount(), MerPrivEnum.NORMAL_PAID.getCode());
			} catch (Exception e) {
				e.printStackTrace();
				payResultFee.setResult(false);
				payResultFee.setOrdId(String.valueOf(feeOrder.getCashFlowId()));
				payResultFee.setMessage("异常" + e.getMessage());
			}

			//3.2 
			this.platformManagementAfter(payResultFee.getOrdId(), payResultFee.isResult(), payResultFee.getMessage());

			//4.1. 调用第三方支付接口
			PayResult payResultRisk = null;
			try {
				payResultRisk = capitalPlatformManager.transfer2Platform(String.valueOf(riskfeeOrder.getCashFlowId()),
						riskfeeOrder.getGatherAccountSequence(), p2pPayAccount, riskfeeOrder.getTradeAmount(), MerPrivEnum.NORMAL_PAID.getCode());
			} catch (Exception e) {
				e.printStackTrace();
				payResultRisk.setResult(false);
				payResultRisk.setOrdId(String.valueOf(riskfeeOrder.getCashFlowId()));
				payResultRisk.setMessage("异常" + e.getMessage());
			}

			//4.2 
			this.riskReserveFundFeeAfter(payResultRisk.getOrdId(), payResultRisk.isResult(), payResultRisk.getMessage());

			if (!payResult.isResult() || !payResultFee.isResult() || !payResultRisk.isResult()) {

				this.updateClaimPayPlan(payResultRisk.getOrdId(), false);

				//有支付失败的场合
				return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, payResult.getMessage() + " " + payResultFee.getMessage() + " "
						+ payResultRisk.getMessage());
			} else {
				this.updateClaimPayPlan(payResultRisk.getOrdId(), true);
			}
		} else {
			// 有失败的场合
			boolean isSuccess = true;
			StringBuffer errorMsg = new StringBuffer();
			List<String> errorMsgList = new ArrayList<String>();
			String tmpOrderId = null;

			List<AccountOrder> accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
					claimPayPlan.getClaimPayPlanSequence(), OrderStatusEnum.PAYMENT_FALIUE.getCode(), TradeTypeEnum.BOND_PAY_BACK.getCode());

			if (!CollectionUtil.isEmpty(accountOrderList)) {
				accountOrder = accountOrderList.get(0);

				//1.2. 调用第三方支付接口，分账   P2P还款资金账户→各贷款客户第三方支付账户
				PayResult payResult = null;
				try {
					payResult = capitalPlatformManager.transfer2User(String.valueOf(accountOrder.getCashFlowId()), borrowerSequence, p2pPayAccount,
							accountOrder.getTradeAmount(), "");
				} catch (Exception e) {
					e.printStackTrace();
					payResult.setResult(false);
					payResult.setOrdId(String.valueOf(accountOrder.getCashFlowId()));
					payResult.setMessage("异常" + e.getMessage());
				}

				//1.3. 后处理
				this.separateAccountsAfter(payResult.getOrdId(), payResult.isResult(), payResult.getMessage());

				if (!payResult.isResult()) {
					//有支付失败的场合
					isSuccess = false;
					errorMsg.append(" ").append(payResult.getMessage());
					errorMsgList.add(payResult.getMessage());
				}

				tmpOrderId = accountOrder.getCashFlowId();
			}

			accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
					claimPayPlan.getClaimPayPlanSequence(), OrderStatusEnum.PAYMENT_FALIUE.getCode(), TradeTypeEnum.P2P_ACCOUNT_SERVICE.getCode());

			if (!CollectionUtil.isEmpty(accountOrderList)) {
				feeOrder = accountOrderList.get(0);
				//2.2. 调用第三方支付接口
				PayResult payResultFee = null;
				try {

					payResultFee = capitalPlatformManager.transfer2Platform(String.valueOf(feeOrder.getCashFlowId()),
							feeOrder.getGatherAccountSequence(), p2pPayAccount, feeOrder.getTradeAmount(), "");
				} catch (Exception e) {
					e.printStackTrace();
					payResultFee.setResult(false);
					payResultFee.setOrdId(String.valueOf(feeOrder.getCashFlowId()));
					payResultFee.setMessage("异常" + e.getMessage());
				}
				//2.3 
				this.platformManagementAfter(payResultFee.getOrdId(), payResultFee.isResult(), payResultFee.getMessage());

				if (!payResultFee.isResult()) {
					//有支付失败的场合
					isSuccess = false;
					errorMsg.append(" ").append(payResultFee.getMessage());
					errorMsgList.add(payResultFee.getMessage());
				}
				tmpOrderId = feeOrder.getCashFlowId();
			}

			accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
					claimPayPlan.getClaimPayPlanSequence(), OrderStatusEnum.PAYMENT_FALIUE.getCode(),
					TradeTypeEnum.P2P_ACCOUNT_RISK_RESERVE_FUND.getCode());

			if (!CollectionUtil.isEmpty(accountOrderList)) {
				riskfeeOrder = accountOrderList.get(0);
				//3.2. 调用第三方支付接口
				PayResult payResultRisk = null;
				try {
					payResultRisk = capitalPlatformManager.transfer2Platform(String.valueOf(riskfeeOrder.getCashFlowId()),
							riskfeeOrder.getGatherAccountSequence(), p2pPayAccount, riskfeeOrder.getTradeAmount(), "");
				} catch (Exception e) {
					e.printStackTrace();
					payResultRisk.setResult(false);
					payResultRisk.setOrdId(String.valueOf(riskfeeOrder.getCashFlowId()));
					payResultRisk.setMessage("异常" + e.getMessage());
				}
				//3.3 
				this.riskReserveFundFeeAfter(payResultRisk.getOrdId(), payResultRisk.isResult(), payResultRisk.getMessage());

				if (!payResultRisk.isResult()) {
					//有支付失败的场合
					isSuccess = false;
					errorMsg.append(" ").append(payResultRisk.getMessage());
					errorMsgList.add(payResultRisk.getMessage());
				}

				tmpOrderId = riskfeeOrder.getCashFlowId();
			}

			if (!isSuccess) {
				return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, errorMsg.toString(), errorMsgList);
			}

			if (tmpOrderId == null) {
				return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "数据有异常，没有失败的订单，补单失败。");
			}

			this.updateClaimPayPlan(tmpOrderId, true);
		}

		return Response.response(ResponseConstants.CommonCode.SUCCESS_CODE, "项目分账成功");
	}

	public BigDecimal doBalance(ClaimPayPlan claimPayPlan, Investment investment, Long p2pPfAccount, BigDecimal sumGatherInterest) {
		//		// 投资人本息罚息总和
		//		BigDecimal sumGatherInterest = claimGatherPlanRepository.sumInterestByInvSeqAndPeriod(claimPayPlan.getInvestmentSequence(),
		//				claimPayPlan.getClaimPayPlanNumber());
		// 借款人本息罚息总和
		BigDecimal sumPayInterest = claimPayPlan.getClaimPayPlanTotalAmount().add(claimPayPlan.getClaimPayPlanJusticeInterest());

		BigDecimal pmFee = claimPayPlan.getClaimPayPlanPlatformManagementFee();
		BigDecimal balance = BigDecimal.ZERO;
		//BigDecimal sumPayInterest = BigDecimal.ZERO;
		// 利息罚息尾数误差处理
		if (sumPayInterest.compareTo(sumGatherInterest) > 0) {
			balance = sumPayInterest.subtract(sumGatherInterest);
			pmFee = pmFee.add(balance);

			//sumPayInterest = sumGatherInterest;
			creBalanceOrder(claimPayPlan, investment, pmFee, claimPayPlan.getPayAccountSequence(), p2pPfAccount);
		} else if (sumPayInterest.compareTo(sumGatherInterest) < 0) {
			balance = sumGatherInterest.subtract(sumPayInterest);
			pmFee = pmFee.subtract(balance);
			//sumPayInterest = sumGatherInterest;
			creBalanceOrder(claimPayPlan, investment, pmFee, p2pPfAccount, claimPayPlan.getPayAccountSequence());
		}
		return pmFee;
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
	public void updateClaimPayPlan(ClaimPayPlan claimPayPlan, ClaimPayPlanStatusEnum claimPayPlanStatusEnum) {
		// 更新债权还款计划状态-〉 已还
		claimPayPlan.setClaimPayPlanStatus(claimPayPlanStatusEnum.getCode());
		claimPayPlan.setRecordEditDate(new Date());
		claimPayPlanRepository.save(claimPayPlan);

	}

	public synchronized void updateClaimPayPlan(String orderId, boolean isResult) {
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);
		AccountOrder accountOrder = accountOrderList.get(0);

		ClaimPayPlan claimPayPlan = claimPayPlanRepository.findOne(accountOrder.getCommodityTablePrimaryKeyValue());
		//查询失败订单
		Long count = accountOrderRepository.getCountPayPlanNotSuccess(accountOrder.getCommodityTablePrimaryKeyValue());
		if (count.intValue() == 0) {
			//全部成功
			updateClaimPayPlan(claimPayPlan, ClaimPayPlanStatusEnum.PAID);

			Investment investment = investmentRepository.findOne(claimPayPlan.getInvestmentSequence());
			//期数
			final int investmentPeriod = Integer.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode()
					+ DictionaryEnum.T_INV_INV_PERIOD.getCode() + investment.getInvestmentPeriod()));
			//若最后一期还款成功
			if (investmentPeriod == claimPayPlan.getClaimPayPlanNumber().intValue()) {
				investment.setInvestmentStatus(InvestmentStatusEnum.REPAY_FINISH.getCode());
				investment.setInvestmentOverDate(new Date());
				investmentRepository.save(investment);
			}

		} else {
			// 更新债权还款计划状态-〉 失败
			updateClaimPayPlan(claimPayPlan, ClaimPayPlanStatusEnum.PAY_FAILURE);
		}
	}

	/**
	 * 还款户—〉贷款人账户分账订单。
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public AccountOrder createSeparateAccountsOrder(Long accountSequence, ClaimPayPlan claimPayPlan, ThirdPaymentAccount thirdPaymentAccount,
			Long outAccounttId, Investment investment, BigDecimal totalAmount) {
		// 更新债权还款计划状态-〉 还款分账中
		claimPayPlan.setClaimPayPlanStatus(ClaimPayPlanStatusEnum.PAYING.getCode());
		claimPayPlanRepository.save(claimPayPlan);

		totalAmount = totalAmount.setScale(ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);
		//1.分账
		//新增订单
		AccountOrder accountOrder = new AccountOrder();
		accountOrder.setTradeDate(new Date());
		accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
		accountOrder.setTradeType(TradeTypeEnum.BOND_PAY_BACK.getCode());//交易类型
		accountOrder.setTradeAmount(totalAmount);
		accountOrder.setCommodityTablePrimaryKeyValue(claimPayPlan.getClaimPayPlanSequence());

		accountOrder.setPayAccountSequence(outAccounttId);
		accountOrder.setGatherAccountSequence(accountSequence);
		accountOrder.setTradeDescription(investment.getInvestmentName());
		accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.INTEREST));
		accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
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
	 * 还款户SDT4—〉SDT3账户订单。
	 * 
	 * @param claimPayPlan
	 *            债券还款计划
	 * @param inAccountSeq
	 *            收款用户账号
	 * @param outAccountSeq
	 *            还款用户账号
	 * 
	 * @return {@link AccountOrder}
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public AccountOrder crePlatformManagementFeesOrder(ClaimPayPlan claimPayPlan, Investment investment, BigDecimal pmFee, Long p2pPfAccount) {
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
		accountOrder.setPayAccountSequence(claimPayPlan.getPayAccountSequence());
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
	 * 还款户SDT4—〉SDT2账户订单。
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public AccountOrder creRiskFundFeesOrder(ClaimPayPlan claimPayPlan, Investment investment) {
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
		accountOrder.setPayAccountSequence(claimPayPlan.getPayAccountSequence());
		accountOrder.setGatherAccountSequence(p2pGatherAccount);

		accountOrder.setTradeDescription(investment.getInvestmentName());
		accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FEE));
		accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentIdBalance);
		accountOrder.setGatherThirdPaymentIdBalance(new BigDecimal(0));
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
	 * 汇付回调。
	 * 
	 * @return
	 */
	public void transferHFAfterForNormalpay(String orderId, boolean isSuccess, String msg) {

		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);

		AccountOrder accountOrder = accountOrderList.get(0);

		if (accountOrder.getTradeType().intValue() == TradeTypeEnum.BOND_PAY_BACK.getCode().intValue()) {
			separateAccountsAfter(orderId, isSuccess, msg);
		} else if (accountOrder.getTradeType().intValue() == TradeTypeEnum.P2P_ACCOUNT_SERVICE.getCode().intValue()) {
			platformManagementAfter(orderId, isSuccess, msg);
		} else if (accountOrder.getTradeType().intValue() == TradeTypeEnum.P2P_ACCOUNT_RISK_RESERVE_FUND.getCode().intValue()) {
			riskReserveFundFeeAfter(orderId, isSuccess, msg);
		}
		updateClaimPayPlan(orderId, isSuccess);
	}

	/**
	 * 还款户SDT4—〉贷款人账户分账调用第三方支付处理后处理。
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public synchronized void separateAccountsAfter(String orderId, boolean isResult, String msg) {

		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);
		if (CollectionUtil.isEmpty(accountOrderList)) {
			logger.debug("订单号:" + orderId + "不存在。");
			return;
		}

		AccountOrder accountOrder = accountOrderList.get(0);
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountOrder.getPayAccountSequence());
		int orderStatus = accountOrder.getOrderStatus().intValue();
		if (isResult
				&& (orderStatus == OrderStatusEnum.PAYING.getCode().intValue() || orderStatus == OrderStatusEnum.PAYMENT_FALIUE.getCode().intValue())) {
			//分账成功
			//更新用户订单的订单状态-〉成功
			accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrderRepository.save(accountOrder);
			//生成用户订单状态历史
			AccountOrderHistory entity = new AccountOrderHistory();
			entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			entity.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
			entity.setOrderStatusChangeDate(new Date());
			accountOrderHistoryRepository.save(entity);

		} else if (!isResult && orderStatus == OrderStatusEnum.PAYING.getCode().intValue()) {

			//平台更新交易记录
			//更新用户订单的订单状态-〉失败 
			accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
			accountOrder.setTradeComment(msg);
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrderRepository.save(accountOrder);
			//生成用户订单状态历史
			AccountOrderHistory entity = new AccountOrderHistory();
			entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			entity.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
			entity.setOrderStatusChangeDate(new Date());
			accountOrderHistoryRepository.save(entity);
		}
	}

	/**
	 * 平台管理缴费处理后处理。
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public synchronized void platformManagementAfter(String orderId, boolean isSuccess, String msg) {
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);

		for (AccountOrder accountOrder : accountOrderList) {

			ClaimPayPlan claimPayPlan = claimPayPlanRepository.findOne(accountOrder.getCommodityTablePrimaryKeyValue());
			// p2p收费 SDT3

			int orderStatus = accountOrder.getOrderStatus().intValue();
			ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(claimPayPlan.getPayAccountSequence());
			if (isSuccess
					&& (orderStatus == OrderStatusEnum.PAYING.getCode().intValue() || orderStatus == OrderStatusEnum.PAYMENT_FALIUE.getCode()
							.intValue())) {
				// 平台账号管理服务费 
				BigDecimal claimPayPlanPlatformManagementFee = claimPayPlan.getClaimPayPlanPlatformManagementFee();

				//分账成功
				//更新用户订单的订单状态-〉成功
				accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
				accountOrder.setOrderEditDate(new Date());
				accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());

				accountOrderRepository.save(accountOrder);
				//生成用户订单状态历史
				AccountOrderHistory entity = new AccountOrderHistory();
				entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
				entity.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
				entity.setOrderStatusChangeDate(new Date());
				accountOrderHistoryRepository.save(entity);

				//生成用户缴费记录-管理服务费
				AccountFeePaymentRecord accountFeePaymentRecord = new AccountFeePaymentRecord();
				accountFeePaymentRecord.setActualPaymentDate(new Date());// 实际缴费时间
				accountFeePaymentRecord.setGatherAccountSequence(accountOrder.getGatherAccountSequence());//收费用户P2P平台账号流水号	
				accountFeePaymentRecord.setGatherDate(new Date());// 收费用户收款时间
				accountFeePaymentRecord.setGatherStatus(AprStatusEnum.PAY_SUCCESS.getCode());// 缴费状态 
				accountFeePaymentRecord.setPayAccountSequence(accountOrder.getPayAccountSequence());//付费用户P2P平台账号
				accountFeePaymentRecord.setPaymentAmount(claimPayPlanPlatformManagementFee);
				accountFeePaymentRecord.setPaymentDescription("TODO 管理服务费");// 缴费描述
				accountFeePaymentRecord.setPaymentName("TODO 管理服务费");// 缴费名称
				accountFeePaymentRecord.setPaymentType(AprTypeEnum.P2P_NORMALPAID_FEE.getCode());
				accountFeePaymentRecord.setPreparePaymentDate(new Date());// 应缴时间
				accountFeePaymentRecord.setCashFlowId(orderId);
				accountFeePaymentRecordRepository.save(accountFeePaymentRecord);

			} else if (!isSuccess && orderStatus == OrderStatusEnum.PAYING.getCode().intValue()) {
				//平台更新交易记录
				//更新用户订单的订单状态-〉失败 
				accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
				accountOrder.setTradeComment(msg);
				accountOrder.setOrderEditDate(new Date());
				accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());

				accountOrderRepository.save(accountOrder);
				//生成用户订单状态历史
				AccountOrderHistory entity = new AccountOrderHistory();
				entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
				entity.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
				entity.setOrderStatusChangeDate(new Date());
				accountOrderHistoryRepository.save(entity);
			}
		}
	}

	/**
	 * 风险备用金费处理后处理。
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public synchronized void riskReserveFundFeeAfter(String orderId, boolean isSuccess, String msg) {
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);

		AccountOrder accountOrder = accountOrderList.get(0);

		ClaimPayPlan claimPayPlan = claimPayPlanRepository.findOne(accountOrder.getCommodityTablePrimaryKeyValue());
		// p2p风险备用金 SDT2

		int orderStatus = accountOrder.getOrderStatus().intValue();
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(claimPayPlan.getPayAccountSequence());
		if (isSuccess
				&& (orderStatus == OrderStatusEnum.PAYING.getCode().intValue() || orderStatus == OrderStatusEnum.PAYMENT_FALIUE.getCode().intValue())) {
			// 风险备用金
			BigDecimal claimPayPlanRiskFee = claimPayPlan.getClaimPayPlanRiskFee();

			//分账成功
			//更新用户订单的订单状态-〉成功
			accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrderRepository.save(accountOrder);
			//生成用户订单状态历史
			AccountOrderHistory entity = new AccountOrderHistory();
			entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			entity.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
			entity.setOrderStatusChangeDate(new Date());
			accountOrderHistoryRepository.save(entity);

			//生成用户缴费记录-风险备用金
			AccountFeePaymentRecord accountFeePaymentRecord = new AccountFeePaymentRecord();
			accountFeePaymentRecord.setActualPaymentDate(new Date());// 实际缴费时间
			accountFeePaymentRecord.setGatherAccountSequence(accountOrder.getGatherAccountSequence());//收费用户P2P平台账号流水号	
			accountFeePaymentRecord.setGatherDate(new Date());// 收费用户收款时间
			accountFeePaymentRecord.setGatherStatus(AprStatusEnum.PAY_SUCCESS.getCode());// 缴费状态 
			accountFeePaymentRecord.setPayAccountSequence(accountOrder.getPayAccountSequence());//付费用户P2P平台账号
			accountFeePaymentRecord.setPaymentAmount(claimPayPlanRiskFee);
			accountFeePaymentRecord.setPaymentDescription("TODO 风险备用金");// 缴费描述
			accountFeePaymentRecord.setPaymentName("TODO 风险备用金");// 缴费名称
			accountFeePaymentRecord.setPaymentType(AprTypeEnum.P2P_RISK_RESERVE_FUND_FEE.getCode());
			accountFeePaymentRecord.setPreparePaymentDate(new Date());// 应缴时间
			accountFeePaymentRecord.setCashFlowId(orderId);
			accountFeePaymentRecordRepository.save(accountFeePaymentRecord);

		} else if (!isSuccess && orderStatus == OrderStatusEnum.PAYING.getCode().intValue()) {
			//平台更新交易记录
			//更新用户订单的订单状态-〉失败 
			accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
			accountOrder.setTradeComment(msg);
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrderRepository.save(accountOrder);
			//生成用户订单状态历史
			AccountOrderHistory entity = new AccountOrderHistory();
			entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			entity.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
			entity.setOrderStatusChangeDate(new Date());
			accountOrderHistoryRepository.save(entity);
		}

	}

	/**
	 * 正常收款处理。（贷款人账户—〉投资人账户）
	 * 
	 * @param claimGatherPlanSequence
	 *            收款计划流水
	 * 
	 * @return
	 */
	public Response normalgather(Long claimGatherPlanSequence) {
		ClaimGatherPlan claimGatherPlan = claimGatherPlanRepository.findOne(claimGatherPlanSequence);

		if (ClaimGatherPlanStatusEnum.NOT_PAID.getCode().intValue() != claimGatherPlan.getClaimGatherPlanStatus().intValue()
				&& ClaimGatherPlanStatusEnum.PAID_FAILURE.getCode().intValue() != claimGatherPlan.getClaimGatherPlanStatus().intValue()
				&& ClaimGatherPlanStatusEnum.OVERDUE.getCode().intValue() != claimGatherPlan.getClaimGatherPlanStatus().intValue()) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "债权收款只能未收和收款失败");
		}

		return this.exeGather(claimGatherPlan, claimGatherPlan.getPayAccountSequence(), TradeTypeEnum.BOND_GATHERING);

	}

	/**
	 * 执行投资人收款业务逻辑（含投资管理费）
	 * 
	 * @param claimGatherPlan
	 *            债权收款计划
	 * @param payAccountSequence
	 *            付款用户
	 * @param tradeTypeEnum
	 *            订单类型（垫付收款、正常收款）
	 * @return
	 */
	public Response exeGather(ClaimGatherPlan claimGatherPlan, final Long payAccountSequence, final TradeTypeEnum tradeTypeEnum) {
		Long p2pSDT3 = null; // 平台资金账户(收平台管理费)
		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_ACCOUNT.getCode());
		if (CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			p2pSDT3 = 0000L;
		} else {
			p2pSDT3 = thirdPaymentAccountList.get(0).getAccountSequence();
		}

		RePayDto rePayDto = new RePayDto();
		if (ClaimGatherPlanStatusEnum.NOT_PAID.getCode().intValue() == claimGatherPlan.getClaimGatherPlanStatus().intValue()
				|| ClaimGatherPlanStatusEnum.OVERDUE.getCode().intValue() == claimGatherPlan.getClaimGatherPlanStatus().intValue()) {
			//1. 生成投资人收款订单
			rePayDto = creGatheringOrder(claimGatherPlan, payAccountSequence, tradeTypeEnum, p2pSDT3);
		} else {

			List<AccountOrder> accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
					claimGatherPlan.getClaimGatherPlanSequence(), OrderStatusEnum.PAYMENT_FALIUE.getCode(), tradeTypeEnum.getCode());
			if (!CollectionUtil.isEmpty(accountOrderList)) {
				AccountOrder accountOrder = accountOrderList.get(0);
				AccountOrder investOrder = accountOrderRepository.findOne(claimGatherPlan.getAccountOrderSequence());
				BigDecimal manageFee = claimGatherPlan.getAccountClaimActualManageFee();
				manageFee = manageFee.setScale(2, BigDecimal.ROUND_HALF_UP);

				rePayDto.setOrdId(String.valueOf(accountOrder.getCashFlowId()));
				rePayDto.setOrdDate(new Date());
				rePayDto.setTransAmt(accountOrder.getTradeAmount());
				rePayDto.setFee(manageFee);
				rePayDto.setSubOrdDate(new Date());
				rePayDto.setSubOrdId(String.valueOf(investOrder.getCashFlowId()));//投标订单

			} else {
				//有支付失败的场合
				return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE,
						"数据有异常，没有失败的订单，" + claimGatherPlan.getClaimGatherPlanSequence() + "补单失败。");
			}

		}

		Investment investment = investmentRepository.findInvestmentByAccountInvestmentSequence(claimGatherPlan.getAccountInvestmentSequence());

		//2. 支付接口
		PayResult payResult;
		try {
			payResult = capitalPlatformManager.normalRepayment(claimGatherPlan.getGatherAccountSequence(), payAccountSequence, p2pSDT3,
					investment.getInvestmentNumber(), rePayDto);
		} catch (Exception e) {
			logger.info("还款失败");
			e.printStackTrace();
			payResult = new PayResult();
			payResult.setResult(false);
			payResult.setOrdId(rePayDto.getOrdId());
			payResult.setMessage(e.getMessage());
		}

		//3.支付后处理
		gatheringAfter(payResult.getOrdId(), payResult.isResult(), payResult.getMessage());

		if (!payResult.isResult()) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, payResult.getMessage(), null);
		} else {
			return Response.successResponse();
		}
	}

	/**
	 * 投资人 收款订单
	 * 
	 * @param claimGatherPlan
	 *            债权收款计划
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public RePayDto creGatheringOrder(ClaimGatherPlan claimGatherPlan, Long payAccountSequence, final TradeTypeEnum tradeTypeEnum, Long p2pSDT3) {

		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(claimGatherPlan.getGatherAccountSequence());

		BigDecimal balanceAmt = thirdPaymentAccount.getThirdPaymentIdBalance();

		Investment investment = investmentRepository.findInvestmentByAccountInvestmentSequence(claimGatherPlan.getAccountInvestmentSequence());

		BigDecimal totalAmt = BigDecimal.ZERO;
		// p2p平台投资管理费（四舍五入）	
		BigDecimal manageFee = claimGatherPlan.getAccountClaimActualManageFee();
		// 计划收总额（四舍五入）
		BigDecimal claimGatherPlanPretendTotalAmount = claimGatherPlan.getClaimGatherPlanPretendTotalAmount();
		//计划收罚息（四舍五入）
		BigDecimal claimGatherPlanPretendJusticeInterest = claimGatherPlan.getClaimGatherPlanPretendJusticeInterest();

		totalAmt = claimGatherPlanPretendTotalAmount.add(claimGatherPlanPretendJusticeInterest);
		manageFee = manageFee.setScale(ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);
		totalAmt = totalAmt.setScale(ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);

		String cashFlowId = P2pUtil.generate20Random(OrderIDRuleEnum.INTEREST);
		//新增本息订单
		AccountOrder accountOrder = new AccountOrder();
		accountOrder.setTradeDate(new Date());
		accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
		accountOrder.setTradeType(tradeTypeEnum.getCode());//交易类型
		accountOrder.setTradeAmount(claimGatherPlanPretendTotalAmount);
		accountOrder.setCommodityTablePrimaryKeyValue(claimGatherPlan.getClaimGatherPlanSequence());

		accountOrder.setTradeDescription(investment.getInvestmentName());
		accountOrder.setCashFlowId(cashFlowId);
		accountOrder.setPayerThirdPaymentIdBalance(BigDecimal.ZERO);
		accountOrder.setGatherThirdPaymentIdBalance(balanceAmt);

		accountOrder.setPayAccountSequence(payAccountSequence);
		accountOrder.setGatherAccountSequence(claimGatherPlan.getGatherAccountSequence());
		accountOrder.setOrderEditDate(new Date());
		accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
		accountOrder = accountOrderRepository.save(accountOrder);

		//生成用户订单状态历史
		AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
		accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
		accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
		accountOrderHistory.setOrderStatusChangeDate(new Date());
		accountOrderHistoryRepository.save(accountOrderHistory);

		//新增投资管理费订单
		accountOrder = new AccountOrder();
		accountOrder.setTradeDate(new Date());
		accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
		accountOrder.setTradeType(TradeTypeEnum.P2P_INVEST.getCode());//交易类型
		accountOrder.setTradeAmount(manageFee);
		accountOrder.setCommodityTablePrimaryKeyValue(claimGatherPlan.getClaimGatherPlanSequence());

		accountOrder.setTradeDescription(investment.getInvestmentName());
		accountOrder.setCashFlowId(cashFlowId);
		accountOrder.setPayerThirdPaymentIdBalance(BigDecimal.ZERO);
		accountOrder.setGatherThirdPaymentIdBalance(balanceAmt);

		accountOrder.setPayAccountSequence(claimGatherPlan.getGatherAccountSequence());
		accountOrder.setGatherAccountSequence(p2pSDT3);
		accountOrder.setOrderEditDate(new Date());
		accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
		accountOrder = accountOrderRepository.save(accountOrder);

		//新增罚息订单
		if (claimGatherPlanPretendJusticeInterest.compareTo(BigDecimal.ZERO) > 0) {
			accountOrder = new AccountOrder();
			accountOrder.setTradeDate(new Date());
			accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
			accountOrder.setTradeType(TradeTypeEnum.DEFAULT_INTEREST.getCode());//交易类型
			accountOrder.setTradeAmount(claimGatherPlanPretendJusticeInterest);
			accountOrder.setCommodityTablePrimaryKeyValue(claimGatherPlan.getClaimGatherPlanSequence());
			accountOrder.setTradeDescription(investment.getInvestmentName());
			accountOrder.setCashFlowId(cashFlowId);
			accountOrder.setPayerThirdPaymentIdBalance(BigDecimal.ZERO);
			accountOrder.setGatherThirdPaymentIdBalance(balanceAmt);
			accountOrder.setPayAccountSequence(payAccountSequence);
			accountOrder.setGatherAccountSequence(claimGatherPlan.getGatherAccountSequence());
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder = accountOrderRepository.save(accountOrder);
		}

		claimGatherPlan.setClaimGatherPlanStatus(ClaimGatherPlanStatusEnum.GATHERING.getCode());
		claimGatherPlanRepository.save(claimGatherPlan);

		AccountOrder investOrder = accountOrderRepository.findOne(claimGatherPlan.getAccountOrderSequence());

		RePayDto rePayDto = new RePayDto();
		rePayDto.setOrdId(String.valueOf(cashFlowId));
		rePayDto.setOrdDate(new Date());
		rePayDto.setTransAmt(totalAmt);
		rePayDto.setFee(manageFee);
		rePayDto.setSubOrdDate(new Date());
		rePayDto.setSubOrdId(String.valueOf(investOrder.getCashFlowId()));//投标订单

		return rePayDto;
	}

	/**
	 * 投资人收款处理后处理。
	 * 
	 * 1.更新订单处理后状态 2.更新债权收款计划 3.生成用户缴费记录-投资管理费
	 * 
	 * @return
	 */
	public synchronized void gatheringAfter(String orderId, boolean isSuccess, String msg) {
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);
		if (CollectionUtil.isEmpty(accountOrderList)) {
			logger.error("订单号:" + orderId + "不存在。");
			return;
		}

		boolean flag = false;
		ClaimGatherPlan claimGatherPlan = null;
		BigDecimal gatherBalanceAmt = BigDecimal.ZERO;
		ThirdPaymentAccount thirdPaymentAccount = null;
		for (AccountOrder accountOrder : accountOrderList) {

			if (claimGatherPlan == null) {
				claimGatherPlan = claimGatherPlanRepository.findOne(accountOrder.getCommodityTablePrimaryKeyValue());
				thirdPaymentAccount = accountThirdRepository.findByAccountSequence(claimGatherPlan.getGatherAccountSequence());
				//冻结总额
				BigDecimal forzenAmt = accountOrderRepository.findFreezeApplySuccessAmt(claimGatherPlan.getGatherAccountSequence(),
						OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
				//投资人可用余额
				gatherBalanceAmt = thirdPaymentAccount.getThirdPaymentIdBalance().subtract(forzenAmt);
			}

			// 调用第三方支付接口，贷款客户资金账户 →投资人第三方账户
			int orderStatus = accountOrder.getOrderStatus().intValue();
			if (isSuccess
					&& (orderStatus == OrderStatusEnum.PAYING.getCode().intValue() || orderStatus == OrderStatusEnum.PAYMENT_FALIUE.getCode()
							.intValue())) {
				//计算用户余额
				accountOrder.setGatherThirdPaymentIdBalance(gatherBalanceAmt);

				//平台更新交易记录
				//更新用户订单的订单状态-〉成功
				updateOrder(accountOrder, OrderStatusEnum.PAYMENT_SUCCESS, msg);

				flag = true;
				// 收款成功后发送站内信
				try {
					//发站内信 限定订单类型 20150407 星期二 chenchang
					if (accountOrder.getTradeType().intValue() == TradeTypeEnum.BOND_GATHERING.getCode().intValue()
							|| accountOrder.getTradeType().intValue() == TradeTypeEnum.PREPAID_GATHERING.getCode().intValue()) {
						callSendMsg(claimGatherPlan);
					}

				} catch (Exception e) {
					e.printStackTrace();
					logger.debug("用户获得项目流水号：" + claimGatherPlan.getAccountInvestmentSequence() + "，发送还款站内信失败");
				}

			} else if (!isSuccess && orderStatus == OrderStatusEnum.PAYING.getCode().intValue()) {
				//平台更新交易记录
				//失败 
				updateOrder(accountOrder, OrderStatusEnum.PAYMENT_FALIUE, msg);

				//  收款计划状态 ->失败
				if (ClaimGatherPlanStatusEnum.PAID_FAILURE.getCode().intValue() != claimGatherPlan.getClaimGatherPlanStatus().intValue()) {
					gatherFaliue(claimGatherPlan);
				}
			}
		}

		if (flag) {
			gatherSuccess(orderId, claimGatherPlan);
		}

	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void updateOrder(AccountOrder accountOrder, OrderStatusEnum orderStatusEnum, String msg) {

		accountOrder.setOrderStatus(orderStatusEnum.getCode());
		accountOrder.setOrderEditDate(new Date());
		accountOrder.setTradeComment(msg);
		accountOrderRepository.save(accountOrder);
		//生成用户订单状态历史
		AccountOrderHistory entity = new AccountOrderHistory();
		entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
		entity.setOrderStatus(orderStatusEnum.getCode());
		entity.setOrderStatusChangeDate(new Date());
		accountOrderHistoryRepository.save(entity);
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void callSendMsg(ClaimGatherPlan claimGatherPlan) throws Exception {
		Investment investment = investmentRepository.findInvestmentByAccountInvestmentSequence(claimGatherPlan.getAccountInvestmentSequence());

		ClaimPayPlan claimPayPlan = claimPayPlanRepository.findByInvestmentSequenceAndClaimPayPlanNumber(investment.getInvestmentSequence(),
				claimGatherPlan.getClaimGatherPlanNumber());

		if (investment == null || claimPayPlan == null) {
			throw new Exception("标的不存在");
		}

		AccountMessageTemplateEnum accountMessageTemplateEnum;
		//查询垫付清单
		ClaimPayPlanHistory claimPayPlanHistory = claimPayPlanHistoryRepository.findOverduePaidPlanHistoryByStatus(
				claimPayPlan.getClaimPayPlanSequence(), OVERDUE_PAY_DAYS, ClaimPayPlanStatusEnum.OVERDUE.getCode());

		if (ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode().intValue() == claimPayPlan.getClaimPayPlanStatus().intValue()
				|| claimPayPlanHistory != null) {
			//如已生成垫付清单
			accountMessageTemplateEnum = AccountMessageTemplateEnum.DIAN_FU_TONG_ZHI;
		} else {
			//			if (claimGatherPlan.getClaimGatherPlanPretendJusticeInterest().compareTo(BigDecimal.ZERO) > 0) {
			//20150413 chenchang 改为按状态判定			
			//	if (claimGatherPlan.getClaimGatherPlanStatus() == ClaimGatherPlanStatusEnum.OVERDUE.getCode()) {

			//20150414 chenchang 改为 当前日期>账单日期 
			if (DateUtil.getCurrentDate().compareTo(DateUtil.format(claimGatherPlan.getClaimGatherPlanNatureDate(), DateUtil.DEFAULT_FORMAT)) > 0) {
				accountMessageTemplateEnum = AccountMessageTemplateEnum.YU_QI_HUAN_KUAN;
			} else {
				accountMessageTemplateEnum = AccountMessageTemplateEnum.ZHENG_CHANG_HUAN_KUAN;
			}
		}

		// 发送逾期站内信  通知投资用户
		AccountMessageTemplateData data = new AccountMessageTemplateData();
		data.setProjectName(investment.getInvestmentName());
		data.setProjectIdURL("#/investitem/" + investment.getInvestmentSequence());
		data.setSum(String.valueOf(claimGatherPlan.getClaimGatherPlanPretendTotalAmount()));//本息
		data.setPeriod(String.valueOf(claimGatherPlan.getClaimGatherPlanNumber()));

		AlertDto alertDto = new AlertDto();
		alertDto.setAccountMessageTemplateData(data);
		alertDto.setAccountMessageTemplateEnum(accountMessageTemplateEnum);
		alertDto.setToUser(claimGatherPlan.getGatherAccountSequence());

		eventMessageGateway.publishEvent(alertDto);
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void gatherFaliue(ClaimGatherPlan claimGatherPlan) {
		claimGatherPlan.setClaimGatherPlanStatus(ClaimGatherPlanStatusEnum.PAID_FAILURE.getCode());
		claimGatherPlan.setRecordEditDate(new Date());
		claimGatherPlanRepository.save(claimGatherPlan);
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void gatherSuccess(String orderId, ClaimGatherPlan claimGatherPlan) {
		//更新债权收款计划
		//  收款计划状态 ->成功
		claimGatherPlan.setRecordEditDate(new Date());
		claimGatherPlan.setClaimGatherPlanStatus(ClaimGatherPlanStatusEnum.PAID.getCode());
		claimGatherPlanRepository.save(claimGatherPlan);

		//债权收款记录
		ClaimGatherRecord claimGatherRecord = new ClaimGatherRecord();
		claimGatherRecord.setAccountClaimActualManageFee(claimGatherPlan.getAccountClaimActualManageFee());//p2p平台投资管理费（四舍五入）
		claimGatherRecord.setAccountClaimSequence(claimGatherPlan.getAccountInvestmentSequence());// 投资人债权流水号
		claimGatherRecord.setAccountClaimShouldManageFee(claimGatherPlan.getAccountClaimShouldManageFee());//p2p平台投资管理费	
		claimGatherRecord.setClaimGatherActualInterest(claimGatherPlan.getClaimGatherPlanActualInterest());//计划收利息
		claimGatherRecord.setClaimGatherActualJusticeInterest(claimGatherPlan.getClaimGatherPlanActualInterest());//计划收利息
		claimGatherRecord.setClaimGatherActualPrincipal(claimGatherPlan.getClaimGatherPlanActualPrincipal());//计划收本金
		claimGatherRecord.setClaimGatherActualSurplus(claimGatherPlan.getClaimGatherPlanActualSurplus());//计划剩余本金
		claimGatherRecord.setClaimGatherActualTatolAmount(claimGatherPlan.getClaimGatherPlanActualTotalAmount());//计划收总额
		claimGatherRecord.setClaimGatherDate(new Date());// 收款时间
		claimGatherRecord.setClaimGatherNumber(claimGatherPlan.getClaimGatherPlanNumber());// 收款期数序号
		claimGatherRecord.setClaimGatherPeriod(claimGatherPlan.getClaim_gather_plan_period());// 收款期数
		claimGatherRecord.setClaimGatherPretendInterest(claimGatherPlan.getClaimGatherPlanPretendInterest());//计划收利息（四舍五入）
		claimGatherRecord.setClaimGatherPretendJusticeInterest(claimGatherPlan.getClaimGatherPlanPretendJusticeInterest());//计划收罚息（四舍五入）
		claimGatherRecord.setClaimGatherPretendPrincipal(claimGatherPlan.getClaimGatherPlanPretendPrincipal());//计划收本金（四舍五入）
		claimGatherRecord.setClaimGatherPretendSurplus(claimGatherPlan.getClaimGatherPlanPretendSurplus());//计划剩余本金（四舍五入）
		claimGatherRecord.setClaimGatherPretendTatolAmount(claimGatherPlan.getClaimGatherPlanPretendTotalAmount());//计划收总额（四舍五入）
		claimGatherRecord.setClaimGatherStartDate(claimGatherPlan.getClaimGatherPlanNatureDate());// 起息日

		claimGatherRecord.setGatherAccountSequence(claimGatherPlan.getGatherAccountSequence());
		claimGatherRecord.setPayAccountSequence(claimGatherPlan.getPayAccountSequence());
		claimGatherRecordRepository.save(claimGatherRecord);

		//生成用户缴费记录-投资管理费
		Long p2pSDT3 = null;
		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_ACCOUNT.getCode());
		if (CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			p2pSDT3 = 0000L;
		} else {
			p2pSDT3 = thirdPaymentAccountList.get(0).getAccountSequence();
		}
		AccountFeePaymentRecord accountFeePaymentRecord = new AccountFeePaymentRecord();
		accountFeePaymentRecord.setActualPaymentDate(new Date());// 实际缴费时间
		accountFeePaymentRecord.setGatherAccountSequence(p2pSDT3);//收费用户P2P平台账号流水号	
		accountFeePaymentRecord.setGatherDate(new Date());// 收费用户收款时间
		accountFeePaymentRecord.setGatherStatus(AprStatusEnum.PAY_SUCCESS.getCode());// 缴费状态 
		accountFeePaymentRecord.setPayAccountSequence(claimGatherPlan.getGatherAccountSequence());//付费用户P2P平台账号
		accountFeePaymentRecord.setPaymentAmount(claimGatherPlan.getAccountClaimActualManageFee());
		accountFeePaymentRecord.setPaymentDescription("TODO 投资管理费");// 缴费描述
		accountFeePaymentRecord.setPaymentName("TODO 投资管理费");// 缴费名称
		accountFeePaymentRecord.setPaymentType(AprTypeEnum.P2P_INVEST_FEE.getCode());
		accountFeePaymentRecord.setPreparePaymentDate(new Date());// 应缴时间
		accountFeePaymentRecord.setCashFlowId(orderId);
		accountFeePaymentRecordRepository.save(accountFeePaymentRecord);

		//更新用户获得的项目 已还期数+1
		AccountInvestment accountInvestment = accountInvestmentRepository.findOne(claimGatherPlan.getAccountInvestmentSequence());
		Investment investment = investmentRepository.findInvestmentByAccountInvestmentSequence(claimGatherPlan.getAccountInvestmentSequence());
		//期数
		final int investmentPeriod = Integer.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode()
				+ DictionaryEnum.T_INV_INV_PERIOD.getCode() + investment.getInvestmentPeriod()));

		if (claimGatherPlan.getClaimGatherPlanNumber().intValue() == investmentPeriod) {
			//如果最后一期
			accountInvestment.setAccountInvestmentStatus(AccountInvestmentStatusEnum.INVESTMENT_END.getCode());
			accountInvestment.setAccountInvestmentEndDate(new Date());

			// 发送结清站内信  通知投资用户
			AccountMessageTemplateData data = new AccountMessageTemplateData();
			data.setProjectName(investment.getInvestmentName());
			data.setProjectIdURL("#/investitem/" + investment.getInvestmentSequence());

			AlertDto alertDto = new AlertDto();
			alertDto.setAccountMessageTemplateData(data);
			alertDto.setAccountMessageTemplateEnum(AccountMessageTemplateEnum.JIE_QING_TONG_ZHI);
			alertDto.setToUser(claimGatherPlan.getGatherAccountSequence());

			eventMessageGateway.publishEvent(alertDto);

		} else {
			accountInvestment.setAccountInvestmentStatus(AccountInvestmentStatusEnum.REPAY_NORMAL.getCode());
		}
		accountInvestment.setAccountInvestmentPayedPeriod(claimGatherPlan.getClaimGatherPlanNumber());
		accountInvestment.setRecordEditDate(new Date());

		accountInvestmentRepository.save(accountInvestment);
	}
}
