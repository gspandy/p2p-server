package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.dto.AlertDto;
import com.vcredit.jdev.p2p.dto.BondPayOffManagerMessageDto;
import com.vcredit.jdev.p2p.entity.ClaimGatherPlan;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.entity.ClaimPayPlanHistory;
import com.vcredit.jdev.p2p.entity.ClaimPayRecord;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;
import com.vcredit.jdev.p2p.entity.LoanCut;
import com.vcredit.jdev.p2p.enums.AccountInvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.ClaimGatherPlanStatusEnum;
import com.vcredit.jdev.p2p.enums.ClaimPayPlanStatusEnum;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.FeeRateEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.repository.AccountClaimRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanHistoryRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayRecordRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanCutRepository;
import com.vcredit.jdev.p2p.util.CollectionUtil;
import com.vcredit.jdev.p2p.util.DateUtil;
import com.vcredit.jdev.p2p.util.FinanceUtil;

/**
 * 扣款信息处理类（逾期登记）。
 * 
 * @author zhuqiu
 *
 */
@Component
@MessageEndpoint
public class BondPayOffManagerActivator {

	private final Logger logger = LoggerFactory.getLogger(ReleaseCashServiceManager.class);

	private static final int OVERDUE_PAY_DAYS = 7;

	@Autowired
	private DictionaryUtil dictionaryUtil;

	/** 小贷公司扣款信息 */
	@Autowired
	private LoanCutRepository loanCutRepository;

	/** 债权还款计划 */
	@Autowired
	private ClaimPayPlanRepository claimPayPlanRepository;

	/** 债权还款记录 */
	@Autowired
	private ClaimPayRecordRepository claimPayRecordRepository;

	/** 交易模块Repository */
	@Autowired
	private DealRepository dealRepository;
	@Autowired
	private AccountClaimRepository accountClaimRepository;

	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;

	@Autowired
	private EventMessageGateway eventMessageGateway;

	/** 债权收款计划 */
	@Autowired
	private ClaimGatherPlanRepository claimGatherPlanRepository;

	@Autowired
	private InvestmentRepository investmentRepository;

	@Autowired
	private ClaimPayPlanHistoryRepository claimPayPlanHistoryRepository;

	@ServiceActivator(inputChannel = "asyncEventPublishChannel")
	public void handle(Object event) throws Exception {
		if (event instanceof BondPayOffManagerMessageDto) {
			long start = System.currentTimeMillis();
			logger.info("逾期登记处理开始。");

			BondPayOffManagerMessageDto dto = (BondPayOffManagerMessageDto) event;
			beforePayoff(dto);

			long end = System.currentTimeMillis();
			logger.info("逾期登记处理结束。" + (end - start) / 1000 + "S");
		}
	}

	/**
	 * VBS反馈T日扣款结果，p2p平台根据扣款结果定时做【逾期】登记的处理。
	 * <p>
	 * 更新债权状态，用户获得项目状态，投资项目表。
	 * </p>
	 * <p>
	 * 更新债权计划表 逾期：贷款人罚金</br> 未决：维信付罚金
	 * </p>
	 * 
	 * @param investmentBusinessCode
	 *            业务订单号
	 * @param peroidOfTime
	 *            期次
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public boolean beforePayoff(BondPayOffManagerMessageDto dto) {
		Long investmentSequence = null;//投资项目流水号
		Integer peroidOfTime = null;

		final Date sysDate = dealRepository.getSystemTime();
		//取得费率
		double rate = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.LC_JFRATE.getCode()
				+ FeeRateEnum.LC_JFRATE.getCode()));

		//1.取得VBS反馈结果
		List<LoanCut> loanCutList = dto.getLoanCutList();

		Map<String, Long> loanCutMap = findInvSeqByBussCd(loanCutList);

		//取得今天应还债务(含T日前未还)
		List<ClaimPayPlan> claimPayPlanList = claimPayPlanRepository.findTodayAllInvestmentNotPaid(ClaimPayPlanStatusEnum.NOT_PAID.getCode(),
				ClaimPayPlanStatusEnum.OVERDUE.getCode(), ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode(),
				InvestmentStatusEnum.AUTO_WITHDRAW_SUCCESS.getCode());

		List<ClaimPayPlan> overdueClaimList = new ArrayList<ClaimPayPlan>();
		for (ClaimPayPlan claimPayPlan : claimPayPlanList) {
			boolean isExsist = false;
			for (LoanCut loanCut : loanCutList) {
				investmentSequence = loanCutMap.get(loanCut.getInvestmentBusinessCode());

				if (investmentSequence == null) {
					//数据异常，找不到项目信息
					continue;
				}

				if (investmentSequence.compareTo(claimPayPlan.getInvestmentSequence()) == 0
						&& loanCut.getLoanPeriod() == claimPayPlan.getClaimPayPlanNumber().intValue()
						&& loanCut.getTotalShouldGet().compareTo(loanCut.getTotalActulGet()) == 0) {
					isExsist = true;

					// 逾期天数
					int intDelayDays = DateUtil.diffDay(claimPayPlan.getClaimPayPlanNatureDate(), sysDate);
					// 当天的债权逾期登记之后再还款，该债权罚息为-1天。
					if (ClaimPayPlanStatusEnum.NOT_PAID.getCode().intValue() != claimPayPlan.getClaimPayPlanStatus().intValue()) {
						this.return2Original(claimPayPlan, rate, sysDate, intDelayDays);
					}

					// VBS反馈已还款
					if (intDelayDays > OVERDUE_PAY_DAYS) {
						if (ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode().intValue() == claimPayPlan.getClaimPayPlanStatus().intValue()) {
							// 若 垫付清单已经生成后还款(账单日逾期大于7),且已垫付
							claimPayPlan.setClaimPayPlanStatus(ClaimPayPlanStatusEnum.PAID.getCode());
							claimPayPlan.setRecordEditDate(sysDate);
							claimPayPlanRepository.save(claimPayPlan);

							Investment investment = investmentRepository.findOne(claimPayPlan.getInvestmentSequence());
							//期数
							final int investmentPeriod = Integer.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode()
									+ DictionaryEnum.T_INV_INV_PERIOD.getCode() + investment.getInvestmentPeriod()));
							//若是最后一期还款成功
							if (investmentPeriod == claimPayPlan.getClaimPayPlanNumber().intValue()) {
								investment.setInvestmentStatus(InvestmentStatusEnum.REPAY_FINISH.getCode());
								investment.setInvestmentOverDate(new Date());
								investmentRepository.save(investment);
							}
						} else {
							//第8天，VBS推送扣款成功时，未垫付
						}
					} else {
						// 正常还款，逾期还款
						claimPayPlan.setClaimPayPlanStatus(ClaimPayPlanStatusEnum.VBS_PAID.getCode());
						claimPayPlan.setRecordEditDate(sysDate);
						claimPayPlanRepository.save(claimPayPlan);

						// 还款时生成 正常还款清单,逾期还款清单,状态都是：VBS已还 
						insertClaimPayPlanHistory(sysDate, claimPayPlan);
						// 若当天已经先生成了垫付清单，则删除
						ClaimPayPlanHistory claimPayPlanHistory = claimPayPlanHistoryRepository.findOverduePaidPlanHistoryByNowDate(
								claimPayPlan.getClaimPayPlanSequence(), OVERDUE_PAY_DAYS);
						if (claimPayPlanHistory != null) {
							claimPayPlanHistoryRepository.delete(claimPayPlanHistory);
						}
					}

					// TODO 如果没有逾期项目
					// 更新用户获得的项目状态-〉正常还款
					dealRepository.updateOverdueActInv(investmentSequence, AccountInvestmentStatusEnum.REPAY_NORMAL);

					insertClaimPayRecord(sysDate, claimPayPlan);
					break;
				}
			}

			if (!isExsist) {
				//逾期债权
				overdueClaimList.add(claimPayPlan);
			}
		}

		if (CollectionUtil.isEmpty(overdueClaimList)) {
			return true;
		}

		//逾期债权进行逾期登记
		for (ClaimPayPlan claimPayPlan : overdueClaimList) {
			investmentSequence = claimPayPlan.getInvestmentSequence();
			peroidOfTime = claimPayPlan.getClaimPayPlanNumber();

			final int intDelayDays = DateUtil.diffDay(claimPayPlan.getClaimPayPlanNatureDate(), sysDate);

			//2.2 根据用户获得的项目流水号、债权收款期数,更新【用户获得的债权】债权状态->4(逾期)，债权状态变化时间,逾期天数+1
			// 当逾期天数小于等于7天时，投资人才登记罚息，大于7天之后不再记录罚息
			if (ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode().intValue() != claimPayPlan.getClaimPayPlanStatus().intValue()
					&& intDelayDays <= OVERDUE_PAY_DAYS) {
				dealRepository.updateOverdueActInv(investmentSequence, AccountInvestmentStatusEnum.OVERDUE);
				dealRepository.updateOverdueActClm(investmentSequence, peroidOfTime, intDelayDays);

				// 更新当期【债权收款计划】
				List<ClaimGatherPlan> claimGatherPlanList = dealRepository.findClaimGatherPlan(investmentSequence, peroidOfTime);
				for (ClaimGatherPlan claimGatherPlan : claimGatherPlanList) {
					// 每期应还本息(本金+利息)
					double pmt = claimGatherPlan.getClaimGatherPlanPretendTotalAmount().doubleValue();
					//计划收罚息 pmt*逾期天数*0.0005
					BigDecimal claimGatherPlanPretendJusticeInterest = BigDecimal.valueOf(FinanceUtil
							.calcO2I_PenaltyInterest(pmt, rate, intDelayDays));
					claimGatherPlan.setClaimGatherPlanActualJusticeInterest(claimGatherPlanPretendJusticeInterest);
					//计划收罚息（四舍五入）
					claimGatherPlanPretendJusticeInterest.setScale(ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);
					claimGatherPlan.setClaimGatherPlanPretendJusticeInterest(claimGatherPlanPretendJusticeInterest);

					claimGatherPlan.setClaimGatherPlanStatus(ClaimGatherPlanStatusEnum.OVERDUE.getCode());
					claimGatherPlan.setRecordEditDate(sysDate);

					// 加入投资人列表
					if (ClaimPayPlanStatusEnum.NOT_PAID.getCode().intValue() == claimPayPlan.getClaimPayPlanStatus().intValue() && intDelayDays == 1) {
						sendOverdueMsg(investmentSequence, claimGatherPlan);
					}
				}

				if (!CollectionUtil.isEmpty(claimGatherPlanList)) {
					claimGatherPlanRepository.save(claimGatherPlanList);
				}
			}

			//2.3 根据投资项目流水号，更新当期【债权还款计划】.逾期天数+1,，应还罚息，
			// 调用金额计算API
			double pmt = claimPayPlan.getClaimPayPlanTotalAmount().doubleValue();
			// 罚息 pmt*逾期天数*0.0005
			BigDecimal justiceInterest = BigDecimal.valueOf(FinanceUtil.calcO2I_PenaltyInterest(pmt, rate, intDelayDays));
			// 罚息（四舍五入）
			justiceInterest.setScale(ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);
			claimPayPlan.setClaimPayPlanJusticeInterest(justiceInterest);
			claimPayPlan.setClaimPayPlanDelayDays(intDelayDays);
			if (ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode().intValue() != claimPayPlan.getClaimPayPlanStatus().intValue()) {
				claimPayPlan.setClaimPayPlanStatus(ClaimPayPlanStatusEnum.OVERDUE.getCode());
			}
			claimPayPlan.setRecordEditDate(sysDate);

			//逾期天数=7天时，生成垫付清单 状态是逾期:3
			if (intDelayDays == OVERDUE_PAY_DAYS
					&& ClaimPayPlanStatusEnum.OVERDUE.getCode().intValue() == claimPayPlan.getClaimPayPlanStatus().intValue()) {
				// 当天重复推送将不再生成清单
				ClaimPayPlanHistory claimPayPlanHistory = claimPayPlanHistoryRepository.findOverduePaidPlanHistoryByNowDate(
						claimPayPlan.getClaimPayPlanSequence(), OVERDUE_PAY_DAYS);
				if (claimPayPlanHistory == null) {
					insertClaimPayPlanHistory(sysDate, claimPayPlan);
				}
			}
		}

		claimPayPlanRepository.save(overdueClaimList);
		return true;
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void insertClaimPayRecord(final Date sysDate, ClaimPayPlan claimPayPlan) {
		//生成债权还款记录
		ClaimPayRecord claimPayRecord = new ClaimPayRecord();
		claimPayRecord.setClaimPayDate(sysDate);// 还款时间
		claimPayRecord.setClaimPayInterest(claimPayPlan.getClaimPayPlanInterest());// 实际还利息
		claimPayRecord.setClaimPayJusticeInterest(claimPayPlan.getClaimPayPlanJusticeInterest());// 实际还罚息
		claimPayRecord.setClaimPayNumber(claimPayPlan.getClaimPayPlanNumber());// 还款期数序号
		claimPayRecord.setClaimPayPeriod(claimPayPlan.getClaimPayPlanPeriod());// 还款期数
		claimPayRecord.setClaimPayPlanRiskFee(claimPayPlan.getClaimPayPlanRiskFee());//风险备用金
		claimPayRecord.setClaimPayPlatformManagementFee(claimPayPlan.getClaimPayPlanPlatformManagementFee());// 平台账号管理服务费
		claimPayRecord.setClaimPayPrincipal(claimPayPlan.getClaimPayPlanPrincipal());// 实际还本金

		claimPayRecord.setClaimPayStartDate(claimPayPlan.getClaimPayPlanCreateDate());// 起息日
		claimPayRecord.setClaimPaySubsidy(claimPayPlan.getClaimPayPlanSubsidy());// 提前清贷补贴 
		claimPayRecord.setClaimPaySurplus(claimPayPlan.getClaimPayPlanSurplus());// 实际剩余本金
		claimPayRecord.setClaimPayTatolSmount(claimPayPlan.getClaimPayPlanTotalAmount());// 实际还总额
		claimPayRecord.setInvestmentSequence(claimPayPlan.getInvestmentSequence());// 投资项目流水号
		claimPayRecord.setPayAccountSequence(claimPayPlan.getPayAccountSequence());//贷款用户P2P平台账号流水号	
		claimPayRecord.setPayRiskAccountSequence(claimPayPlan.getPayRiskAccountSequence());//vmoney-风险备用金p2p平台账号
		claimPayRecord.setPayServiceAccountSequence(claimPayPlan.getPayServiceAccountSequence());//vmoney-还款服务p2p平台账号	
		claimPayRecordRepository.save(claimPayRecord);
	}

	public Map<String, Long> findInvSeqByBussCd(List<LoanCut> loanCutList) {
		Map<String, Long> loanCutMap = new HashMap<String, Long>();
		for (LoanCut loanCut : loanCutList) {
			// 小贷公司推送扣款_s43  贷款人基础信息和贷款业务信息 用户P2P平台账号  投资项目与贷款人关系表 债权还款计划_s41
			InvestmentAccountReference investmentAccountReference = investmentAccountReferenceRepository.findInvestmentByBusinessCode(
					loanCut.getInvestmentBusinessCode(), InvestmentStatusEnum.RELEASE_CASH_SUCCESS.getCode(),
					InvestmentStatusEnum.AUTO_WITHDRAW_SUCCESS.getCode());

			if (investmentAccountReference == null) {
				// 数据异常，找不到项目信息
				continue;
			}

			if (!loanCutMap.containsKey(loanCut.getInvestmentBusinessCode())) {
				loanCutMap.put(loanCut.getInvestmentBusinessCode(), investmentAccountReference.getInvestmentSequence());
			}
		}
		return loanCutMap;
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

	/**
	 * 第一次逾期，给投资人发送逾期通知。
	 * 
	 * @param investmentBusinessCode
	 *            业务订单号
	 * @return
	 */
	public void sendOverdueMsg(Long investmentSequence, ClaimGatherPlan claimGatherPlan) {
		try {
			Investment investment = investmentRepository.findOne(investmentSequence);
			//计算垫付日
			String debourDate = DateUtil.formatDefault(DateUtil.addDay(claimGatherPlan.getClaimGatherPlanNatureDate(), 6));

			// 发送逾期站内信  通知投资用户
			AccountMessageTemplateData data = new AccountMessageTemplateData();
			data.setProjectName(investment.getInvestmentName());
			data.setProjectIdURL("#/investitem/" + investment.getInvestmentSequence());
			data.setSum(String.valueOf(claimGatherPlan.getClaimGatherPlanPretendTotalAmount()));//本息
			data.setPeriod(String.valueOf(claimGatherPlan.getClaimGatherPlanNumber()));
			data.setDebourDate(debourDate);//垫付日

			AlertDto alertDto = new AlertDto();
			alertDto.setAccountMessageTemplateData(data);
			alertDto.setAccountMessageTemplateEnum(AccountMessageTemplateEnum.YU_QI_TONG_ZHI);
			alertDto.setToUser(claimGatherPlan.getGatherAccountSequence());

			eventMessageGateway.publishEvent(alertDto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("项目流水号：" + investmentSequence + "，发送逾期还款站内信失败");
		}
	}

	/**
	 * 已经还款处理。
	 * <p>
	 * 更新债权状态，用户获得项目状态，投资项目表。
	 * </p>
	 * <p>
	 * </br>
	 * </p>
	 * 
	 * @param investmentBusinessCode
	 *            业务订单号
	 * @return
	 */
	private void return2Original(ClaimPayPlan claimPayPlan, double rate, Date sysDate, int intDelayDays) {
		Long investmentSequence = claimPayPlan.getInvestmentSequence();
		Integer peroidOfTime = claimPayPlan.getClaimPayPlanNumber();
		//int intDelayDays = DateUtil.diffDay(claimPayPlan.getClaimPayPlanNatureDate(), sysDate);
		intDelayDays = intDelayDays - 1;

		//2.2 根据用户获得的项目流水号、债权收款期数,更新【用户获得的债权】债权状态->4(逾期)，债权状态变化时间,逾期天数+1
		dealRepository.updateOverdueActClm(investmentSequence, peroidOfTime, intDelayDays);

		// 更新当期【债权收款计划】
		List<ClaimGatherPlan> claimGatherPlanList = dealRepository.findClaimGatherPlan(claimPayPlan.getInvestmentSequence(),
				claimPayPlan.getClaimPayPlanNumber());

		for (ClaimGatherPlan claimGatherPlan : claimGatherPlanList) {
			// 每期应还本息(本金+利息)
			double pmt = claimGatherPlan.getClaimGatherPlanPretendTotalAmount().doubleValue();
			//计划收罚息 pmt*逾期天数*0.0005
			BigDecimal claimGatherPlanPretendJusticeInterest = BigDecimal.valueOf(FinanceUtil.calcO2I_PenaltyInterest(pmt, rate, intDelayDays));
			claimGatherPlan.setClaimGatherPlanPretendJusticeInterest(claimGatherPlanPretendJusticeInterest);
			claimGatherPlan.setClaimGatherPlanActualJusticeInterest(claimGatherPlanPretendJusticeInterest);
			//claimGatherPlan.setClaimGatherPlanStatus(ClaimGatherPlanStatusEnum.OVERDUE.getCode());
			claimGatherPlan.setRecordEditDate(sysDate);
		}

		if (!CollectionUtil.isEmpty(claimGatherPlanList)) {
			claimGatherPlanRepository.save(claimGatherPlanList);
		}

		double pmt = claimPayPlan.getClaimPayPlanTotalAmount().doubleValue();
		// 罚息 pmt*逾期天数*0.0005
		BigDecimal justiceInterest = BigDecimal.valueOf(FinanceUtil.calcO2I_PenaltyInterest(pmt, rate, intDelayDays));
		claimPayPlan.setClaimPayPlanJusticeInterest(justiceInterest);
		claimPayPlan.setClaimPayPlanDelayDays(intDelayDays);
	}
}
