package com.vcredit.jdev.p2p.deal.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.dto.ClaimGatherAmtDto;
import com.vcredit.jdev.p2p.dto.ManualInvestReturnDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountInvestment;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.ClaimGatherPlan;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.entity.ClaimPayRecord;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.AccountInvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.ClaimGatherPlanStatusEnum;
import com.vcredit.jdev.p2p.enums.ClaimPayPlanStatusEnum;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.OrderStatusEnum;
import com.vcredit.jdev.p2p.enums.TradeTypeEnum;
import com.vcredit.jdev.p2p.repository.AccountClaimRepository;
import com.vcredit.jdev.p2p.repository.AccountFeePaymentRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountInvestmentRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherRecordRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayRecordRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;
import com.vcredit.jdev.p2p.util.CollectionUtil;
import com.vcredit.jdev.p2p.util.DateUtil;
import com.vcredit.jdev.p2p.util.FinanceUtil;

/**
 * 提供前台查询订单内容的接口 。
 * 
 * @author zhuqiu
 *
 */
@Component
public class DealServiceReferenceManager {

	/** 用户获得的项目 */
	@Autowired
	private AccountInvestmentRepository accountInvestmentRepository;

	/** 用户获得债权 */
	@Autowired
	private AccountClaimRepository accountClaimRepository;

	@Autowired
	private ClaimGatherPlanRepository claimGatherPlanRepository;

	@Autowired
	private ClaimPayPlanRepository claimPayPlanRepository;

	@Autowired
	private ClaimPayRecordRepository claimPayRecordRepository;

	@Autowired
	private ClaimGatherRecordRepository claimGatherRecordRepository;

	@Autowired
	private InvestmentRepository investmentRepository;

	@Autowired
	private LoanDataRepository loanDataRepository;

	@Autowired
	private DictionaryUtil dictionaryUtil;

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountThirdRepository accountThirdRepository;
	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;

	@Autowired
	private AccountFeePaymentRecordRepository accountFeePaymentRecordRepository;
	@Autowired
	private DealRepository dealRepository;

	/**
	 * wdzh_02_我的投资(合计在投本金,预期总收益,今日总收益,累计总收益)
	 * 
	 * @param accountSequence
	 *            用户获得的项目流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Map<String, BigDecimal> wdzh02MyInvest(Long accountSequence) {
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		BigDecimal investTtlAmt = BigDecimal.ZERO;

		List<ClaimGatherPlan> claimGatherPlanList = claimGatherPlanRepository.findByGatherAccountSequenceNotPaid(accountSequence,
				ClaimGatherPlanStatusEnum.PAID.getCode());
		//1.合计在投本金
		//2.预期总收益
		BigDecimal expectedReturnAmt = BigDecimal.ZERO;
		for (ClaimGatherPlan claimGatherPlan : claimGatherPlanList) {
			expectedReturnAmt = expectedReturnAmt.add(claimGatherPlan.getClaimGatherPlanPretendInterest());
			investTtlAmt = investTtlAmt.add(claimGatherPlan.getClaimGatherPlanPretendPrincipal());
		}

		//3.今日总收益
		BigDecimal benefitAmt = BigDecimal.ZERO;
		claimGatherPlanList = claimGatherPlanRepository.findTodayGatherAmt(accountSequence, ClaimGatherPlanStatusEnum.PAID.getCode());
		for (ClaimGatherPlan claimGatherPlan : claimGatherPlanList) {
			benefitAmt = benefitAmt.add(claimGatherPlan.getClaimGatherPlanPretendInterest()).add(
					claimGatherPlan.getClaimGatherPlanPretendJusticeInterest());
		}

		// 4.累计总收益=罚息+利息-投资管理费-提现手续费
		BigDecimal accumulatedIncome = claimGatherPlanRepository.sumBenifitByAcctSeqAndStatus(accountSequence,
				ClaimGatherPlanStatusEnum.PAID.getCode());
		//		List<ClaimGatherRecord> claimGatherRecordList = claimGatherRecordRepository.findByGatherAccountSequence(accountSequence);
		//		for (ClaimGatherRecord claimGatherRecord : claimGatherRecordList) {
		//			//罚息+利息-投资管理费
		//			accumulatedIncome = accumulatedIncome.add(claimGatherRecord.getClaimGatherPretendJusticeInterest())
		//					.add(claimGatherRecord.getClaimGatherPretendInterest()).subtract(claimGatherRecord.getAccountClaimActualManageFee());
		//		}
		// -提现手续费
		BigDecimal sumWithFeeAmt = accountOrderRepository.sumWithdrawFeeByAccountSequence(accountSequence, TradeTypeEnum.P2P_WITHDRAW_FEE.getCode(),
				OrderStatusEnum.PAYMENT_SUCCESS.getCode());
		accumulatedIncome = accumulatedIncome.subtract(sumWithFeeAmt);

		map.put("investTtlAmt", investTtlAmt);
		map.put("expectedReturnAmt", expectedReturnAmt);
		map.put("benefitAmt", benefitAmt);
		map.put("accumulatedIncome", accumulatedIncome);
		return map;
	}

	/**
	 * wdzh_02_我的投资(已持有)
	 * 
	 * @param accountSequence
	 *            用户获得的项目流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Map<String, Object>> wdzh02MyInvestList(Long accountSequence) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		//chenchang 20150323
		List<AccountInvestment> accountInvestmentList = accountInvestmentRepository.findAccountInvestmentHolder(accountSequence);

		for (AccountInvestment accountInvestment : accountInvestmentList) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 投资项目流水号
			Long investmentSequence = accountInvestment.getInvestmentSequence();
			Investment investment = investmentRepository.findOne(investmentSequence);

			//合同编号
			String contractId = null;
			List<InvestmentAccountReference> investmentAccountReferenceList = investmentAccountReferenceRepository
					.findByInvestmentSequence(investmentSequence);
			if (!CollectionUtil.isEmpty(investmentAccountReferenceList)) {
				LoanData loanData = loanDataRepository.findOne(investmentAccountReferenceList.get(0).getLoanDataSequence());
				contractId = loanData.getContractId();
			}
			map.put("contractId", "V".concat(contractId));

			ClaimGatherPlan claimGatherPlan = claimGatherPlanRepository.findByAccountInvestmentSequenceAndClaimGatherPlanNumber(
					accountInvestment.getAccountInvestmentSequence(), accountInvestment.getAccountInvestmentPayedPeriod() + 1);
			//总期数 项目名称      投资金额(元)  
			map.put("Investment", investment);
			//当前状态:用户获得的项目状态  已还:已收款（还款）期数 
			map.put("AccountInvestment", accountInvestment);
			// 剩余应收本息(元) 月还款额(元)  下期还款日
			map.put("ClaimGatherPlan", claimGatherPlan);
			List<ClaimGatherPlan> claimGatherPlanList = claimGatherPlanRepository.findByAccountInvestmentSequenceAndClaimGatherPlanStatusNot(
					accountInvestment.getAccountInvestmentSequence(), ClaimGatherPlanStatusEnum.PAID.getCode());

			BigDecimal notPaid = BigDecimal.ZERO;
			for (ClaimGatherPlan loopDto : claimGatherPlanList) {
				notPaid = notPaid.add(loopDto.getClaimGatherPlanPretendTotalAmount());
			}
			//剩余应收本息(元)
			map.put("RemainPrincipalAndInterest", notPaid);

			//总期数
			Integer investmentPeriod = Integer.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode()
					+ DictionaryEnum.T_INV_INV_PERIOD.getCode() + investment.getInvestmentPeriod()));
			map.put("totalPeriod", investmentPeriod);
			//当前状态
			String accountInvestmentStatus = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_ACT_INV.getCode()
					+ DictionaryEnum.T_ACT_INV_ACT_INV_STAT.getCode() + accountInvestment.getAccountInvestmentStatus());
			map.put("accountInvestmentStatus", accountInvestmentStatus);

			// 下期还款日
			ClaimPayPlan claimPayPlan = claimPayPlanRepository.findByInvestmentSequenceAndClaimPayPlanNumber(investmentSequence,
					accountInvestment.getAccountInvestmentPayedPeriod() + 1);

			//20150316 chenchang
			if (null != claimPayPlan && null != claimPayPlan.getClaimPayPlanNatureDate()) {
				map.put("nextPayDay", DateUtil.formatDefault(claimPayPlan.getClaimPayPlanNatureDate()));
			}
			mapList.add(map);
		}

		return mapList;
	}

	/**
	 * wdzh_02_我的投资(申请中)
	 * 
	 * @param accountSequence
	 *            用户获得的项目流水号
	 * @return
	 */
	///@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Map<String, Object>> wdzh02MyInvestApplyList(Long accountSequence) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		//取得投资人订单信息
		List<AccountOrder> accountOrderList = new ArrayList<AccountOrder>();

		accountOrderList = accountOrderRepository.findFreezeApply(accountSequence, OrderStatusEnum.FREEZE_SUCCESS.getCode(),
				OrderStatusEnum.UNFREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
		for (AccountOrder accountOrder : accountOrderList) {
			Long investmentSequence = accountOrder.getCommodityTablePrimaryKeyValue();
			Investment investment = investmentRepository.findOne(investmentSequence);
			int investmentStatus = investment.getInvestmentStatus().intValue();
			if (InvestmentStatusEnum.ON_LINE.getCode().intValue() != investmentStatus
					&& InvestmentStatusEnum.RELEASE_CASH.getCode().intValue() != investmentStatus
					&& InvestmentStatusEnum.RELEASE_CASH_FALIUE.getCode().intValue() != investmentStatus
					&& InvestmentStatusEnum.TENDER_FAIL.getCode().intValue() != investmentStatus
					&& InvestmentStatusEnum.TENDER_FINISH.getCode().intValue() != investmentStatus
					&& InvestmentStatusEnum.TENDERING.getCode().intValue() != investmentStatus) {
				continue;
			}
			Map<String, Object> map = new HashMap<String, Object>();

			// 项目名称       借款本金(元) 年利率   期数   还款方式   投资进度       投标状态
			map.put("Investment", investment);
			// 投资金额(元) 
			map.put("AccountOrder", accountOrder);
			// 应收本息 DictionaryEnum
			Integer investmentPeriod = Integer.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode()
					+ DictionaryEnum.T_INV_INV_PERIOD.getCode() + investment.getInvestmentPeriod()));

			double yearRate = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_IRATE.getCode()
					+ investment.getInvestmentAnnualInterestRate()));//年利率

			//double pmt = FinanceUtil.PMT_ALL(yearRate / 12, investmentPeriod, accountOrder.getTradeAmount().doubleValue());//每期应还本息
			//预期等额本息
			map.put("PMT", this.calculatePMT(investmentPeriod, yearRate, accountOrder.getTradeAmount()));

			//年利率
			map.put("YearRate", yearRate);

			//还款方式
			String investmentPayType = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_PTYPE.getCode()
					+ investment.getInvestmentPayType());

			map.put("InvestmentPayType", investmentPayType);

			//投标状态
			String investmentStatusName = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_STAT.getCode()
					+ investment.getInvestmentStatus());
			map.put("InvestmentStatus", investmentStatusName);

			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * wdzh_02_我的投资(已结束)
	 * 
	 * @param accountSequence
	 *            用户获得的项目流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Map<String, Object>> wdzh02MyInvestFinishList(Long accountSequence) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		List<AccountInvestment> accountInvestmentList = accountInvestmentRepository.findFinishInvestmentByAccountSequence(accountSequence,
				AccountInvestmentStatusEnum.TANSFER_SUCCESS.getCode(), AccountInvestmentStatusEnum.INVESTMENT_END.getCode());

		for (AccountInvestment accountInvestment : accountInvestmentList) {
			Long investmentSequence = accountInvestment.getInvestmentSequence();
			Investment investment = investmentRepository.findOne(investmentSequence);
			Map<String, Object> map = new HashMap<String, Object>();
			// 项目名称 ， 期数， 还款方式   ，结清日（清贷时间） ，放款日（满标时间）
			map.put("Investment", investment);
			// 投资金额(元)， 结清方式（用户获得的项目状态）
			map.put("AccountInvestment", accountInvestment);
			// 实收本息(元)   债权收款记录t_clm_grecord
			BigDecimal clmGptotal = claimGatherRecordRepository.findSumGatherByAccountInvestmentSequence(accountInvestment
					.getAccountInvestmentSequence());
			map.put("ClmGptotal", clmGptotal);
			//年利率
			double yearRate = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_IRATE.getCode()
					+ investment.getInvestmentAnnualInterestRate()));//年利率
			map.put("YearRate", yearRate);

			//还款方式
			String investmentPayType = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_PTYPE.getCode()
					+ investment.getInvestmentPayType());

			map.put("InvestmentPayType", investmentPayType);

			// 结清方式 
			String accountInvestmentEndForm = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_ACT_INV.getCode()
					+ DictionaryEnum.T_ACT_INV_ACT_INV_EFORM.getCode() + accountInvestment.getAccountInvestmentEndForm());
			map.put("AccountInvestmentEndForm", accountInvestmentEndForm);

			mapList.add(map);
		}

		return mapList;
	}

	/**
	 * wytz0101_投标记录
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Map<String, Object>> wytz0101TenderingRecordList(Long investmentSequence) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		//取得投资人订单信息
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
				investmentSequence, OrderStatusEnum.FREEZE_SUCCESS.getCode(), OrderStatusEnum.UNFREEZE_SUCCESS.getCode(),
				TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());

		for (AccountOrder accountOrder : accountOrderList) {
			Map<String, Object> map = new HashMap<String, Object>();
			//	投资用户
			Account account = accountRepository.findOne(accountOrder.getPayAccountSequence());

			String userName = "";
			if (account != null) {
				userName = account.getUserName();
			}

			map.put("userName", userName);
			//	投资金额  投资时间
			map.put("accountOrder", accountOrder);
			//	 状态
			String orderStatus = null;
			if (OrderStatusEnum.FREEZE_SUCCESS.getCode().intValue() == accountOrder.getOrderStatus().intValue()
					|| OrderStatusEnum.UNFREEZE_SUCCESS.getCode().intValue() == accountOrder.getOrderStatus().intValue()) {
				orderStatus = "成功";
			} else {
				orderStatus = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_ACT_ORDER.getCode() + DictionaryEnum.T_ACT_ORDER_ORDER_STAT.getCode()
						+ accountOrder.getOrderStatus());
			}
			map.put("orderStatus", orderStatus);

			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * wytz0101_债权信息
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Object> wytz0101CreditorList(Long accountSequence, Long investmentSequence) {
		List<Object> mapList = new ArrayList<Object>();
		//		List<AccountInvestment> accountInvestmentList = accountInvestmentRepository.findByInvestmentSequenceAndAccountInvestmentStatus(
		//				investmentSequence, AccountInvestmentStatusEnum.REPAY_NORMAL.getCode());

		//chenchang 20150320
		List<AccountInvestment> accountInvestmentList = accountInvestmentRepository.findAccountInvestmentHolder(accountSequence, investmentSequence);

		Integer i = 0;
		//初始债权人，目前债权持有人，待收债权本金（元）
		for (AccountInvestment accountInvestment : accountInvestmentList) {
			//待收债权本金（元）
			BigDecimal accountInvestmentQuota = accountInvestment.getAccountInvestmentQuota();
			//目前债权持有人
			Long currentAccount = accountInvestment.getAccountSequence();
			// 初始债权人
			Long initAccount = accountInvestment.getAccountFirstSequence();

			//	投资用户
			Account account = accountRepository.findOne(currentAccount);
			String currentAccountName = account.getUserName();
			account = accountRepository.findOne(initAccount);
			String initAccountName = account.getUserName();

			//			Map<Object, Object> map = new HashMap<Object, Object>();
			//			map.put("number", ++i);
			//			map.put("capitalToGather", accountInvestmentQuota);
			//			map.put("initAccount", initAccountName);
			//			map.put("currentAccount", currentAccountName);
			//			mapList.add(map);

			CreditorDto creditorDto = new CreditorDto();
			creditorDto.setNumber(++i);
			creditorDto.setCapitalToGather(accountInvestmentQuota);
			creditorDto.setInitAccount(initAccountName);
			creditorDto.setCurrentAccount(currentAccountName);
			mapList.add(creditorDto);
		}
		return mapList;
	}

	@SuppressWarnings("unused")
	private class CreditorDto {
		private Integer number;
		private BigDecimal capitalToGather;
		private String initAccount;
		private String currentAccount;

		public Integer getNumber() {
			return number;
		}

		public void setNumber(Integer number) {
			this.number = number;
		}

		public BigDecimal getCapitalToGather() {
			return capitalToGather;
		}

		public void setCapitalToGather(BigDecimal capitalToGather) {
			this.capitalToGather = capitalToGather;
		}

		public String getInitAccount() {
			return initAccount;
		}

		public void setInitAccount(String initAccount) {
			this.initAccount = initAccount;
		}

		public String getCurrentAccount() {
			return currentAccount;
		}

		public void setCurrentAccount(String currentAccount) {
			this.currentAccount = currentAccount;
		}
	}

	/**
	 * wytz0101_已还期数
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Integer wytz0101PayedPeriod(Long investmentSequence) {

		Integer accountInvestmentPayedPeriod = 0;

		//		ClaimPayPlan claimPayPlan = claimPayPlanRepository.findPayedPeriodByInvestmentSequenceAndStatus(investmentSequence,
		//				ClaimPayPlanStatusEnum.PAID.getCode(), ClaimPayPlanStatusEnum.PAYING.getCode(), ClaimPayPlanStatusEnum.PAY_FAILURE.getCode(),
		//				ClaimPayPlanStatusEnum.VBS_PAID.getCode());

		ClaimPayPlan claimPayPlan = claimPayPlanRepository.findPayedPeriodByInvestmentSequenceAndStatus(investmentSequence,
				ClaimPayPlanStatusEnum.PAID.getCode(), ClaimPayPlanStatusEnum.PAYING.getCode(), ClaimPayPlanStatusEnum.VBS_PAID.getCode(),
				ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode());
		// 已还期数
		if (claimPayPlan != null) {
			accountInvestmentPayedPeriod = claimPayPlan.getClaimPayPlanNumber();
		}
		return accountInvestmentPayedPeriod;
	}

	/**
	 * wytz0101_下期还款日
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public String wytz0101NextNatureDate(Long investmentSequence) {
		// 下期还款日
		ClaimPayPlan claimPayPlan = claimPayPlanRepository.findNatureDateByInvestmentSequenceAndPayAccountSequence(investmentSequence,
				ClaimPayPlanStatusEnum.NOT_PAID.getCode());

		String claimPayPlanNatureDate = "";
		if (claimPayPlan != null) {
			claimPayPlanNatureDate = DateUtil.formatDefault(claimPayPlan.getClaimPayPlanNatureDate()).replaceAll("-", ".");
		}

		return claimPayPlanNatureDate;
	}

	/**
	 * wytz0101_判断用户是否投过该项目
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param accountSequence
	 *            用户流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public boolean wytz0101ChkHasInvested(Long investmentSequence, Long accountSequence) {
		boolean hasInvested = false;

		List<AccountOrder> accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
				investmentSequence, OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());

		for (AccountOrder accountOrder : accountOrderList) {
			if (accountSequence.compareTo(accountOrder.getPayAccountSequence()) == 0) {
				hasInvested = true;
			}
		}

		//
		boolean isGetInvest = false;
		List<AccountInvestment> accountInvestmentList = accountInvestmentRepository.findByInvestmentSequenceAndAccountSequence(investmentSequence,
				accountSequence);
		if (!CollectionUtil.isEmpty(accountInvestmentList)) {
			isGetInvest = true;
		}

		return hasInvested || isGetInvest;
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public BigDecimal wytz0101SumAmtHasInvested(Long investmentSequence, Long accountSequence) {
		//		BigDecimal tradeAmount = accountOrderRepository.sumHasInvestedByInvestmentSequenceAndAccountSequence(investmentSequence,
		//				OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode(), accountSequence);

		BigDecimal tradeAmount = accountInvestmentRepository.sumByInvestmentSequenceAndAccountSequence(investmentSequence, accountSequence);

		return tradeAmount;
	}

	/**
	 * wytz0101_用户注册以来投过的项目总数
	 * 
	 * @param accountSequence
	 *            用户流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Long wytz0101SumHasInvested(Long accountSequence) {
		return accountInvestmentRepository.getCountByAccountSequence(accountSequence);
	}

	/**
	 * 用户是否存在投资（开始投资第一笔 无论是否成功只要有了投资 就算有理财）
	 * 
	 * @param accountSequence
	 *            用户流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Long isCurrentAccountInvested(Long accountSequence) {
		return accountOrderRepository.getCountInvested(accountSequence, OrderStatusEnum.FREEZE_SUCCESS.getCode(),
				OrderStatusEnum.UNFREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
	}

	/**
	 * wytz0101_未满标时 (已满标、招标中)
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Map<String, Object>> wytz0101PayPlanNotFinishList(Long investmentSequence) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		//		Investment investment = investmentRepository.findOne(investmentSequence);
		//		if (InvestmentStatusEnum.TENDER_FINISH.getCode() == investment.getInvestmentStatus()
		//				|| InvestmentStatusEnum.ON_LINE.getCode() == investment.getInvestmentStatus()) {

		List<ClaimPayPlan> claimPayPlanList = claimPayPlanRepository.findByInvestmentSequence(investmentSequence);

		for (ClaimPayPlan claimPayPlan : claimPayPlanList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("claimPayPlan", claimPayPlan);
			mapList.add(map);
		}
		//		}

		return mapList;
	}

	/**
	 * wytz0101_已满标时 (非已满标、非招标中)
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Map<String, Object>> wytz0101PayPlanFinishedList(Long investmentSequence) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		List<ClaimPayPlan> claimPayPlanList = claimPayPlanRepository.findByInvestmentSequence(investmentSequence);

		for (ClaimPayPlan claimPayPlan : claimPayPlanList) {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("claimPayPlan", claimPayPlan);

			ClaimPayRecord claimPayRecord = null;
			// 已垫付
			if (ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode().intValue() == claimPayPlan.getClaimPayPlanStatus().intValue()) {
				claimPayRecord = new ClaimPayRecord();
				// 实际还款日
				claimPayRecord.setClaimPayDate(claimPayPlan.getRecordEditDate());
				// 实际还款额
				claimPayRecord.setClaimPayTatolSmount(claimPayPlan.getClaimPayPlanTotalAmount().add(claimPayPlan.getClaimPayPlanJusticeInterest()));
				// 罚息
				claimPayRecord.setClaimPayJusticeInterest(claimPayPlan.getClaimPayPlanJusticeInterest());
				// 实际剩余本金
				claimPayRecord.setClaimPaySurplus(claimPayPlan.getClaimPayPlanSurplus());

			} else if (ClaimPayPlanStatusEnum.OVERDUE.getCode().intValue() == claimPayPlan.getClaimPayPlanStatus().intValue()) {
				claimPayRecord = new ClaimPayRecord();
				// 罚息
				claimPayRecord.setClaimPayJusticeInterest(claimPayPlan.getClaimPayPlanJusticeInterest());

			} else {
				claimPayRecord = claimPayRecordRepository.findByInvestmentSequenceAndClaimPayNumber(investmentSequence,
						claimPayPlan.getClaimPayPlanNumber());
				if (claimPayRecord != null) {
					// 实际还款额
					claimPayRecord.setClaimPayTatolSmount(claimPayPlan.getClaimPayPlanTotalAmount()
							.add(claimPayPlan.getClaimPayPlanJusticeInterest()));
				}
			}
			//实际还款日，实际还款额，罚息，
			map.put("claimPayRecord", claimPayRecord);

			// 还款计划状态 常量
			String claimPayPlanStatus = null;
			if (ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode().intValue() == claimPayPlan.getClaimPayPlanStatus().intValue()
					|| ClaimPayPlanStatusEnum.VBS_PAID.getCode().intValue() == claimPayPlan.getClaimPayPlanStatus().intValue()) {
				claimPayPlanStatus = "已还";
			} else {
				claimPayPlanStatus = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_CLM_PPLAN.getCode()
						+ DictionaryEnum.T_CLM_PPLAN_CLM_PP_STAT.getCode() + claimPayPlan.getClaimPayPlanStatus());

			}
			map.put("claimPayPlanStatus", claimPayPlanStatus);
			mapList.add(map);
		}

		/*
		 * Map<String, Object> map = new HashMap<String, Object>(); for (int i =
		 * 0; i < 3; i++) { ClaimPayPlan p = new ClaimPayPlan();
		 * p.setClaimPayPlanNumber(1); map.put("claimPayPlan", p);
		 * 
		 * ClaimPayRecord claimPayRecord = new ClaimPayRecord();
		 * claimPayRecord.setClaimPayJusticeInterest(new BigDecimal(888));
		 * //实际还款日，实际还款额，罚息， map.put("claimPayRecord", claimPayRecord);
		 * 
		 * mapList.add(map); }
		 */
		return mapList;
	}

	/**
	 * 用户手动投资前检查
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param accoutSequence
	 *            用户流水号
	 * @param amount
	 *            投资金额
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public ManualInvestReturnDto manualInvestChk(Long investmentSequence, Long accoutSequence, BigDecimal amount) {
		ManualInvestReturnDto returnDto = new ManualInvestReturnDto();

		//投资金额必须为50整数倍，最低不少于50
		if (amount.compareTo(new BigDecimal(amount.intValue())) != 0) {
			//不是整数
			returnDto.setStatus(ResponseConstants.CommonCode.NOT_DEFINE_CODE);
			returnDto.setResult(false);
			returnDto.setMsg("投资金额必须是整数");
			return returnDto;
		}

		if (amount.intValue() < 50 || amount.intValue() % 50 != 0) {
			//小于50或不是50的整数倍
			returnDto.setStatus(ResponseConstants.CommonCode.NOT_DEFINE_CODE);
			returnDto.setResult(false);
			returnDto.setMsg("投资金额必须大于50并且是50的整数倍");
			return returnDto;
		}

		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(accoutSequence);
		if (thirdPaymentAccount == null) {
			returnDto.setStatus(ResponseConstants.CommonCode.RESPONSE_CODE_300);//开通账号画面
			returnDto.setResult(false);
			returnDto.setMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_300);
			return returnDto;
		}
		// 账号可用余额
		BigDecimal thirdPaymentIdBalance = thirdPaymentAccount.getThirdPaymentIdBalance();
		if (amount.compareTo(thirdPaymentIdBalance) > 0) {
			returnDto.setStatus(ResponseConstants.CommonCode.RESPONSE_CODE_301);//充值画面
			returnDto.setResult(false);
			returnDto.setMsg(ResponseConstants.CommonMessage.RESPONSE_MSG_301);
			return returnDto;
		}
		Investment investment = investmentRepository.findOne(investmentSequence);
		//如果投资金额大于剩余可投资金额，则直接返回
		if (amount.compareTo(investment.getInvestmentSurplus()) > 0) {
			returnDto.setStatus(ResponseConstants.CommonCode.NOT_DEFINE_CODE);
			returnDto.setResult(false);
			returnDto.setMsg("投资金额必须小于项目可投资金额");
			return returnDto;
		}

		returnDto.setStatus(ResponseConstants.CommonCode.SUCCESS_CODE);
		returnDto.setResult(true);
		return returnDto;
	}

	/**
	 * 投资人累计获得收益(利息+罚息+TODO 红包收入)
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param accoutSequence
	 *            用户流水号
	 * @param amount
	 *            投资金额
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Map<String, Double>> calcBenifitMonth(Long accoutSequence) {
		List<Map<String, Double>> mapList = new ArrayList<Map<String, Double>>();
		//利息+罚息
		List<ClaimGatherAmtDto> claimGatherRecordList = dealRepository.findSumByGatherAccountSequence(accoutSequence);

		Date sysDate = DateUtil.getSysDate();
		int i = 0;
		int j = 0;
		while (i < 6) {
			Map<String, Double> map = new HashMap<String, Double>();
			String yyyymm = DateUtil.format(DateUtil.addMonth(sysDate, j), DateUtil.DATE_FORMAT_YEAR_MONTH_SHORT);
			map.put(yyyymm, Double.valueOf("0"));
			for (ClaimGatherAmtDto claimGatherRecord : claimGatherRecordList) {
				Integer claimGatherNumber = claimGatherRecord.getClmGnum();
				if (yyyymm.equals(String.valueOf(claimGatherNumber))) {
					map.put(yyyymm, claimGatherRecord.getClmGptotal());
				}
			}

			mapList.add(map);
			i++;
			j--;
		}

		return mapList;
	}

	/**
	 * 资金明细画面
	 * 
	 * @param accoutSequence
	 *            用户流水号
	 * @param startDate
	 *            开始日
	 * @param endDate
	 *            结束日
	 * @param orderStatusList
	 *            订单状态
	 * @param tradeTypeList
	 *            订单类型
	 * @param currentPage
	 *            当前页
	 * @param pageNumber
	 *            每页记录数
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<AccountOrder> findAccountOrderList(Long accountSeq, Date startDate, Date endDate, List<Integer> orderStatusList,
			List<Integer> tradeTypeList, int currentPage, int pageSize) {

		Page<AccountOrder> page = accountOrderRepository
				.findByPayAccountSequenceAndTradeDateBetweenAndOrderStatusInAndTradeTypeInOrGatherAccountSequenceAndTradeDateBetweenAndOrderStatusInAndTradeTypeIn(
						accountSeq, startDate, endDate, orderStatusList, tradeTypeList, accountSeq, startDate, endDate, orderStatusList,
						tradeTypeList, new PageRequest(currentPage, pageSize, new Sort(Direction.DESC, "tradeDate")));

		List<AccountOrder> rtnList = new ArrayList<AccountOrder>();
		String tradeComment = "";

		if (page != null && page.getSize() > 0) {
			for (AccountOrder pageOrder : page.getContent()) {

				AccountOrder accountOrder = new AccountOrder();
				try {
					BeanUtils.copyProperties(accountOrder, pageOrder);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

				//投资放款,本息收入,罚息收入,TODO 提前清贷本金收入,提前清贷补偿金收入
				if (accountOrder.getTradeType().intValue() == TradeTypeEnum.BOND_GATHERING.getCode().intValue()
						|| accountOrder.getTradeType().intValue() == TradeTypeEnum.DEFAULT_INTEREST.getCode().intValue()) {
					//tradeComment = "<借款人>";
					//	tradeComment = accountRepository.findOne(accountOrder.getGatherAccountSequence()).getUserName();

					tradeComment = accountRepository.findOne(accountOrder.getPayAccountSequence()).getUserName();

				} else if (accountOrder.getTradeType().intValue() == TradeTypeEnum.RELEASE_CASH.getCode().intValue()) {

					tradeComment = accountRepository.findOne(accountOrder.getGatherAccountSequence()).getUserName();

				} else if (accountOrder.getTradeType().intValue() == TradeTypeEnum.PREPAID_GATHERING.getCode().intValue()) {
					// 垫付本息收入
					//tradeComment = "垫付账户";

					//					List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
					//							.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_PAYFORSOMEBODY_ACCOUNT.getCode());
					//					Long p2pPrepaidAccount = thirdPaymentAccountList.get(0).getAccountSequence(); //SDT1垫付户
					//					tradeComment = accountRepository.findOne(p2pPrepaidAccount).getUserName();

					tradeComment = "垫付资金账户";

				} else if (accountOrder.getTradeType().intValue() == TradeTypeEnum.P2P_INVEST.getCode()) {
					//投资管理费支出
					//	P2P_INVEST
					tradeComment = "P2P平台收费资金账户";
				}

				else {
					tradeComment = "无";
				}
				//  else if (accountOrder.getTradeType().intValue() == TradeTypeEnum.PREPAID_GATHERING.getCode().intValue()) {
				//	// 回购收入
				//					tradeComment = "风险备用金";
				//				}

				accountOrder.setTradeComment(tradeComment);
				rtnList.add(accountOrder);
			}
		}
		return rtnList;
	}

	/**
	 * 等额本息
	 * 
	 * @param investmentPeriod
	 *            项目期次
	 * @param yearRate
	 *            年利率
	 * @param tradeAmount
	 *            投资金额
	 * @return
	 */
	private BigDecimal calculatePMT(Integer investmentPeriod, double yearRate, BigDecimal tradeAmount) {

		int i = 1;
		BigDecimal tmpAmt = BigDecimal.ZERO;
		BigDecimal tmpAmtRound = BigDecimal.ZERO;
		double monthRate = yearRate / 12; // 月利率
		BigDecimal pmt = BigDecimal.ZERO;

		final BigDecimal PMT_NORMAL = BigDecimal.valueOf(FinanceUtil.PMT(monthRate, investmentPeriod, tradeAmount.doubleValue()));
		while (i <= investmentPeriod) {
			// 利息
			BigDecimal actualInterest = null;
			BigDecimal actualInterestRound = null;
			if (i == 1) {
				actualInterest = BigDecimal.valueOf(tradeAmount.doubleValue() * monthRate);
				actualInterestRound = BigDecimal.valueOf(tradeAmount.doubleValue() * monthRate);
				actualInterestRound = actualInterestRound.setScale(ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);
			} else {
				actualInterest = BigDecimal.valueOf(tmpAmt.doubleValue() * monthRate);
				actualInterestRound = BigDecimal.valueOf(tmpAmtRound.doubleValue() * monthRate);
				actualInterestRound = actualInterestRound.setScale(ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);
			}

			// 本息
			BigDecimal actualTotalAmount = null;
			// 本息(四舍五入)
			BigDecimal actualTotalAmountRound = null;
			// 本金
			BigDecimal actualQuotaRound = BigDecimal.ZERO;
			BigDecimal actualQuota = BigDecimal.ZERO;
			if (i == investmentPeriod) {
				// 本金
				actualQuota = tmpAmt;
				actualQuotaRound = tmpAmtRound;

				actualTotalAmount = actualQuota.add(actualInterest);
				actualTotalAmountRound = actualQuotaRound.add(actualInterestRound);
			} else {
				// 本息
				actualTotalAmount = PMT_NORMAL;
				// 本息(四舍五入)
				actualTotalAmountRound = actualTotalAmount.setScale(ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);

				// 本金=本息-利息
				actualQuota = actualTotalAmount.subtract(actualInterest);
				// 本金(四舍五入)   本息-利息
				actualQuotaRound = actualTotalAmountRound.subtract(actualInterestRound);
			}

			//剩余本金
			BigDecimal actualSurplus = null;
			BigDecimal actualSurplusRound = null;
			if (i == 1) {
				actualSurplus = tradeAmount.subtract(actualQuota);
				actualSurplusRound = tradeAmount.subtract(actualQuotaRound);
			} else if (i == investmentPeriod) {
				actualSurplus = BigDecimal.ZERO;
				actualSurplusRound = BigDecimal.ZERO;
			} else {
				// 剩余本金 = 上期剩余本金-当期本金
				actualSurplus = tmpAmt.subtract(actualQuota);
				actualSurplusRound = tmpAmtRound.subtract(actualQuotaRound);
				actualSurplusRound = actualSurplusRound.setScale(ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);
			}
			tmpAmt = actualSurplus;
			tmpAmtRound = actualSurplusRound;

			pmt = pmt.add(actualTotalAmountRound);

			i++;
		}

		return pmt;
	}
}
