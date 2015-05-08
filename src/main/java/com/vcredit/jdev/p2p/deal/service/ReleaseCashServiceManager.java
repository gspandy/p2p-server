package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gplatform.sudoor.server.integration.AsyncEventMessageGateway;
import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.alert.service.AccountMessageManager;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.capital.modal.CapitalPlatformManager;
import com.vcredit.jdev.p2p.dto.AlertDto;
import com.vcredit.jdev.p2p.dto.LoansHaveFeeDto;
import com.vcredit.jdev.p2p.dto.LoansNoFeeDto;
import com.vcredit.jdev.p2p.dto.PayResult;
import com.vcredit.jdev.p2p.dto.ReqExtLoans;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountClaim;
import com.vcredit.jdev.p2p.entity.AccountContract;
import com.vcredit.jdev.p2p.entity.AccountInvestment;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.AccountOrderHistory;
import com.vcredit.jdev.p2p.entity.ClaimGatherPlan;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.AccountInvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.ActClmStatusEnum;
import com.vcredit.jdev.p2p.enums.ActInvGformEnum;
import com.vcredit.jdev.p2p.enums.ClaimGatherPlanStatusEnum;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.FeeRateEnum;
import com.vcredit.jdev.p2p.enums.InvSrcEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.OrderIDRuleEnum;
import com.vcredit.jdev.p2p.enums.OrderStatusEnum;
import com.vcredit.jdev.p2p.enums.RecStatusEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountTypeEnum;
import com.vcredit.jdev.p2p.enums.TradeTypeEnum;
import com.vcredit.jdev.p2p.pdf.convert.PdfInfos;
import com.vcredit.jdev.p2p.repository.AccountBankCardRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountClaimRepository;
import com.vcredit.jdev.p2p.repository.AccountContractRepository;
import com.vcredit.jdev.p2p.repository.AccountInvestmentRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderHistoryRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;
import com.vcredit.jdev.p2p.util.CollectionUtil;
import com.vcredit.jdev.p2p.util.DateUtil;
import com.vcredit.jdev.p2p.util.FinanceUtil;

/**
 * p2p平台放款手续费 。
 * 
 * @author zhuqiu
 *
 */
@Component
public class ReleaseCashServiceManager {

	private final Logger logger = LoggerFactory.getLogger(ReleaseCashServiceManager.class);
	private final static String ACCOUNT_SEQ = "accountSeq";
	private final static String TRADE_AMT = "tradeAmt";
	private final static String GATHER_PER_MONTH = "gatherPerMonth";

	/** 投资项目 */
	@Autowired
	private InvestmentRepository investmentRepository;

	/** 投资项目与贷款人关系 */
	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;

	/** 用户获得的项目 */
	@Autowired
	private AccountInvestmentRepository accountInvestmentRepository;

	/** 交易模块Repository */
	@Autowired
	private DealRepository dealRepository;

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;
	/** 用户订单状态历史 */
	@Autowired
	private AccountOrderHistoryRepository accountOrderHistoryRepository;

	/** 用户获得债权 */
	@Autowired
	private AccountClaimRepository accountClaimRepository;

	/** 债权收款计划 */
	@Autowired
	private ClaimGatherPlanRepository claimGatherPlanRepository;

	@Autowired
	private ClaimPayPlanRepository claimPayPlanRepository;
	/** 发送消息 */
	@Autowired
	private AccountMessageManager accountMessageManager;

	@Autowired
	private AccountThirdRepository accountThirdRepository;

	/** 用户银行卡 */
	@Autowired
	private AccountBankCardRecordRepository accountBankCardRecordRepository;

	/** 用户银行卡 */
	@Autowired
	private CapitalPlatformManager capitalPlatformManager;

	@Autowired
	private CapitalWithdrawServiceManager capitalWithdrawServiceManager;

	@Autowired
	private LoanDataRepository loanDataRepository;

	@Autowired
	private DictionaryUtil dictionaryUtil;

	@Autowired
	private EventMessageGateway eventMessageGateway;

	@Autowired
	private AsyncEventMessageGateway asyncEventMessageGateway;

	@Autowired
	private AccountContractRepository accountContractRepository;

	@Autowired
	private AccountRepository accountRepository;

	/**
	 * 每日收款状态
	 * 
	 * @param accountSequence
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response dailyPaymentStatus(Long accountSequence, Date beginDate, Date endDate) {
		List<ClaimGatherPlan> myClaimGatherPlan = claimGatherPlanRepository.findByAccountSequence(accountSequence, beginDate, endDate);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();

		//	2015/04={expect=4041.0549200184, paid=4608.5694745365, 
		// date={18=[{claimGatherPlanActualTotalAmount=3803.3458070761, claimGatherPlanStatus=3, claimGatherPlanSequence=4361}, 
		// {claimGatherPlanActualTotalAmount=237.7091129423, claimGatherPlanStatus=3, claimGatherPlanSequence=4385}], 15=[{claimGatherPlanActualTotalAmount=3871.1983586107, claimGatherPlanStatus=4, claimGatherPlanSequence=4517}, {claimGatherPlanActualTotalAmount=430.1331509567, claimGatherPlanStatus=4, claimGatherPlanSequence=4535}, {claimGatherPlanActualTotalAmount=307.2379649691, claimGatherPlanStatus=4, claimGatherPlanSequence=4553}]}}

		BigDecimal expect = new BigDecimal(0);
		BigDecimal paid = new BigDecimal(0);
		BigDecimal hold = new BigDecimal(0);

		for (ClaimGatherPlan obj : myClaimGatherPlan) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("claimGatherPlanSequence", obj.getClaimGatherPlanSequence());
			map.put("claimGatherPlanActualTotalAmount", obj.getClaimGatherPlanActualTotalAmount());
			map.put("claimGatherPlanStatus", obj.getClaimGatherPlanStatus());

			String dmKey = DateUtil.format(obj.getClaimGatherPlanNatureDate(), DateUtil.DATE_FORMAT_YEAR_MONTH);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			int dayKey = obj.getClaimGatherPlanCreateDate().getDate() + 1;

			//月
			Map monthMap = new HashMap();
			if (map1.get(dmKey) != null) {
				monthMap = (Map) map1.get(dmKey);
			}

			//日
			Map dayMap = new HashMap();
			if (monthMap.get("date") != null) {
				dayMap = (Map) monthMap.get("date");
			}

			List dayList = new ArrayList();
			if (dayMap.get(dayKey) != null) {
				dayList = (List) dayMap.get(dayKey);
			}
			dayList.add(map);

			dayMap.put(dayKey, dayList);
			monthMap.put("date", dayMap);

			if (obj.getClaimGatherPlanStatus() == 3) {
				if (monthMap.get("expect") != null) {
					expect = (BigDecimal) monthMap.get("expect");
				} else {
					expect = new BigDecimal(0);
				}
				expect = expect.add(obj.getClaimGatherPlanActualTotalAmount());
			} else if (obj.getClaimGatherPlanStatus() == 4) {
				if (monthMap.get("paid") != null) {
					paid = (BigDecimal) monthMap.get("paid");
				} else {
					paid = new BigDecimal(0);
				}
				paid = paid.add(obj.getClaimGatherPlanActualTotalAmount());
			} else {
				if (monthMap.get("hold") != null) {
					hold = (BigDecimal) monthMap.get("hold");
				} else {
					hold = new BigDecimal(0);
				}
				hold = hold.add(obj.getClaimGatherPlanActualTotalAmount());
			}

			monthMap.put("expect", expect); //逾期
			monthMap.put("paid", paid);//已还款
			monthMap.put("hold", hold);//已持有

			map1.put(dmKey, monthMap);
		}

		return Response.successResponse(map1);
	}

	/**
	 * <p>
	 * 满标放款。
	 * </p>
	 * <p>
	 * 投资人->贷款人</br>
	 * </p>
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return
	 */
	public Response releaseCash(Long investmentSequence) {
		List<PayResult> payResultList = null;

		Investment investment = investmentRepository.findOne(investmentSequence);
		if (investment == null) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "标的不存在", "");
		}
		int investmentStatus = investment.getInvestmentStatus();
		List<AccountOrder> accountOrderList = new ArrayList<AccountOrder>();

		List<LoansHaveFeeDto> loansHaveFeeDtoList = new ArrayList<LoansHaveFeeDto>();

		//check
		Response response = releaseCashChk(investmentSequence, investment, accountOrderList);

		if (response != null) {
			return response;
		}

		if (investmentStatus == InvestmentStatusEnum.TENDER_FINISH.getCode().intValue()) {
			//取得投资人冻结成功订单信息
			accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(investmentSequence,
					OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());

			if (CollectionUtil.isEmpty(accountOrderList)) {
				return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "投资人订单不存在", "");
			}

			//1.生成订单
			//20150309 chenchang
			loansHaveFeeDtoList = releaseCashCreateOrderHaveFee(investmentSequence, investment, accountOrderList);

		} else {
			//1.取得投资人满标放款失败订单信息
			accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(investmentSequence,
					OrderStatusEnum.PAYMENT_FALIUE.getCode(), TradeTypeEnum.RELEASE_CASH.getCode());

			if (CollectionUtil.isEmpty(accountOrderList)) {
				return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "投资人订单不存在", "");
			}

			loansHaveFeeDtoList = releaseCashFaliueOrder(investmentSequence, investment, accountOrderList);

		}

		//2.调用第三方支付
		payResultList = releaseCashThirdPayHaveFee(investmentSequence, loansHaveFeeDtoList);

		//3.更新订单
		boolean isSuccess = true;
		for (PayResult payResult : payResultList) {
			if (!payResult.isResult()) {
				isSuccess = false;
			}
			releaseCashAfter(payResult.getOrdId(), payResult.getUnFreezeOrdId(), payResult.isResult(), payResult.getMessage());
		}

		if (isSuccess) {
			return Response.response(ResponseConstants.CommonCode.SUCCESS_CODE, "项目放款成功");
		} else {
			return Response.response(ResponseConstants.CommonCode.SUCCESS_CODE, "项目放款中");
		}
	}

	public Response releaseCashChk(Long investmentSequence, Investment investment, List<AccountOrder> accountOrderList) {

		//取得满标项目贷款人信息 
		List<InvestmentAccountReference> investmentAccountReferenceList = investmentAccountReferenceRepository
				.findByInvestmentSequence(investmentSequence);

		if (CollectionUtil.isEmpty(investmentAccountReferenceList)) {
			//贷款人不能为空
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "借款人不存在", "");
		}

		int investmentStatus = investment.getInvestmentStatus().intValue();
		if (investmentStatus != InvestmentStatusEnum.TENDER_FINISH.getCode().intValue()
				&& investmentStatus != InvestmentStatusEnum.RELEASE_CASH_FALIUE.getCode().intValue()) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "投资项目状态只能'已满标'或'满标放款失败'", "");
		}

		return null;

	}

	/**
	 * @param investment
	 * @param accountOrderList
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<LoansHaveFeeDto> releaseCashFaliueOrder(Long investmentSequence, Investment investment, List<AccountOrder> accountOrderList) {
		//取得投资人订单信息
		List<LoansHaveFeeDto> loansDtoList = new ArrayList<LoansHaveFeeDto>();

		Long p2pPayAccount = null;//平台还款资金账户
		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_RECHARGE_WITHDRAW_ACCOUNT.getCode());
		if (!CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			p2pPayAccount = thirdPaymentAccountList.get(0).getAccountSequence();
		} else {
			System.out.println("平台还款资金账户不存在..");
		}

		//p2p平台放款服务费率
		double r = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.P2P_RC_FRATE.getCode()
				+ FeeRateEnum.P2P_RC_FRATE.getCode()));

		for (AccountOrder dto : accountOrderList) {
			//生成解冻订单
			String freezeOrder = P2pUtil.generate20Random();
			BigDecimal tradeAmount = dto.getTradeAmount();
			BigDecimal p2pFee = BigDecimal.valueOf(FinanceUtil.calcO2P_MakeLoanFee(tradeAmount.doubleValue(), r));

			LoansHaveFeeDto loansDto = new LoansHaveFeeDto();
			ReqExtLoans reqExt = new ReqExtLoans();
			reqExt.setProId(investment.getInvestmentNumber());
			loansDto.setReqExt(reqExt);
			loansDto.setOrdId(String.valueOf(dto.getCashFlowId()));//放款订单号
			loansDto.setOrdDate(new Date());
			loansDto.setOutAccountId(dto.getPayAccountSequence());// 投资人客户号
			loansDto.setTransAmt(tradeAmount);
			loansDto.setSubOrdId(String.valueOf(dto.getCashFlowId()));//投标订单号
			loansDto.setSubOrdDate(dto.getTradeDate());
			loansDto.setInAccountId(dto.getGatherAccountSequence());// 借款人第三方支付账号
			loansDto.setUnFreezeOrdId(String.valueOf(freezeOrder));//解冻订单号 
			loansDto.setFreezeTrxId(String.valueOf(dto.getTrxId()));
			//手续费
			loansDto.setFeeObjFlag("I");//I代表入账账户,支付手续费
			loansDto.setFee(p2pFee);
			Map<Long, BigDecimal> routingAmount = new HashMap<Long, BigDecimal>();
			routingAmount.put(p2pPayAccount, p2pFee);
			loansDto.setRoutingAmount(routingAmount);
			loansDtoList.add(loansDto);
		}

		//更新项目状态——〉满标放款中
		investment.setInvestmentStatus(InvestmentStatusEnum.RELEASE_CASH.getCode());
		investmentRepository.save(investment);
		return loansDtoList;
	}

	/**
	 * 放款&有费用
	 * 
	 * add by chenchang 20150309
	 * 
	 * @param investment
	 * @param accountOrderList
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<LoansHaveFeeDto> releaseCashCreateOrderHaveFee(Long investmentSequence, Investment investment, List<AccountOrder> accountOrderList) {
		//取得投资人订单信息
		List<LoansHaveFeeDto> loansDtoList = new ArrayList<LoansHaveFeeDto>();
		for (AccountOrder dto : accountOrderList) {

			ThirdPaymentAccount toThirdPaymentAccount = accountThirdRepository.findByAccountSequence(dto.getGatherAccountSequence());//贷款人第三方
			ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(dto.getPayAccountSequence());

			Long p2pPayAccount = null;//平台还款资金账户
			List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
					.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_RECHARGE_WITHDRAW_ACCOUNT.getCode());
			if (!CollectionUtil.isEmpty(thirdPaymentAccountList)) {
				p2pPayAccount = thirdPaymentAccountList.get(0).getAccountSequence();
			} else {
				System.out.println("平台还款资金账户不存在..");
			}
			ThirdPaymentAccount p2pThirdPaymentAccount = accountThirdRepository.findByAccountSequence(p2pPayAccount);

			String tradeDescription = "";

			//生成放款订单
			Date tradeDate = new Date();//交易时间
			BigDecimal tradeAmount = dto.getTradeAmount();//交易金额

			//p2p平台放款服务费率
			double r = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.P2P_RC_FRATE.getCode()
					+ FeeRateEnum.P2P_RC_FRATE.getCode()));
			BigDecimal p2pFee = BigDecimal.valueOf(FinanceUtil.calcO2P_MakeLoanFee(tradeAmount.doubleValue(), r));
			String cashFlowId = P2pUtil.generate20Random(OrderIDRuleEnum.FREEZE);
			Date orderEditDate = tradeDate;
			tradeDescription = "放款";

			AccountOrder accountOrder = new AccountOrder();
			accountOrder.setTradeDate(tradeDate);
			accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
			accountOrder.setTradeType(TradeTypeEnum.RELEASE_CASH.getCode());//交易类型
			accountOrder.setTradeAmount(tradeAmount);
			accountOrder.setCommodityTablePrimaryKeyValue(dto.getCommodityTablePrimaryKeyValue());
			accountOrder.setPayAccountSequence(dto.getPayAccountSequence());//付费用户P2P平台账号流水号
			accountOrder.setGatherAccountSequence(dto.getGatherAccountSequence());//收费用户P2P平台账号流水号
			accountOrder.setTradeDescription(investment.getInvestmentName());
			accountOrder.setCashFlowId(cashFlowId);
			accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder.setGatherThirdPaymentIdBalance(toThirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder.setOrderEditDate(orderEditDate);
			accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder = accountOrderRepository.save(accountOrder);

			AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
			accountOrderHistory.setOrderStatusChangeDate(orderEditDate);
			accountOrderHistoryRepository.save(accountOrderHistory);

			//生成手续费 订单,历史记录 20150310
			tradeDescription = "放款手续费";
			AccountOrder accountOrder2 = new AccountOrder();
			accountOrder2.setTradeDate(tradeDate);
			accountOrder2.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
			accountOrder2.setTradeType(TradeTypeEnum.P2P_RELEASE_CASH_SERVICE.getCode());//交易类型
			accountOrder2.setTradeAmount(p2pFee);
			accountOrder2.setCommodityTablePrimaryKeyValue(dto.getCommodityTablePrimaryKeyValue());
			accountOrder2.setPayAccountSequence(dto.getPayAccountSequence());//付费用户P2P平台账号流水号
			accountOrder2.setGatherAccountSequence(p2pPayAccount);//收费用户P2P平台账号流水号
			accountOrder2.setTradeDescription(investment.getInvestmentName());
			accountOrder2.setCashFlowId(cashFlowId);
			accountOrder2.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder2.setGatherThirdPaymentIdBalance(p2pThirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder2.setOrderEditDate(orderEditDate);
			accountOrder2.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder2 = accountOrderRepository.save(accountOrder2);

			AccountOrderHistory accountOrderHistory2 = new AccountOrderHistory();
			accountOrderHistory2.setAccountOrderSequence(accountOrder2.getAccountOrderSequence());
			accountOrderHistory2.setOrderStatus(OrderStatusEnum.PAYING.getCode());
			accountOrderHistory2.setOrderStatusChangeDate(orderEditDate);
			accountOrderHistory2.setTradeDescription(tradeDescription);
			accountOrderHistoryRepository.save(accountOrderHistory2);

			//生成解冻订单号
			//生成解冻订单
			AccountOrder accountOrder3 = new AccountOrder();
			accountOrder3.setTradeDate(new Date());
			accountOrder3.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
			accountOrder3.setTradeType(TradeTypeEnum.UNFREEZE.getCode());//交易类型
			accountOrder3.setTradeAmount(tradeAmount);
			accountOrder3.setCommodityTablePrimaryKeyValue(dto.getCommodityTablePrimaryKeyValue());
			accountOrder3.setPayAccountSequence(dto.getPayAccountSequence());//付费用户P2P平台账号流水号
			accountOrder3.setGatherAccountSequence(dto.getGatherAccountSequence());//收费用户P2P平台账号流水号
			accountOrder3.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FREEZE));
			accountOrder3.setTradeDescription(investment.getInvestmentName());
			accountOrder3.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder3.setGatherThirdPaymentIdBalance(p2pThirdPaymentAccount.getThirdPaymentIdBalance());

			accountOrder3.setOrderEditDate(new Date());
			accountOrder3.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder3 = accountOrderRepository.save(accountOrder3);

			LoansHaveFeeDto loansDto = new LoansHaveFeeDto();
			ReqExtLoans reqExt = new ReqExtLoans();
			//reqExt.setLoansVocherAmt(new BigDecimal(0));
			reqExt.setProId(investment.getInvestmentNumber());
			loansDto.setReqExt(reqExt);
			loansDto.setOrdId(String.valueOf(accountOrder.getCashFlowId()));//放款订单号
			loansDto.setOrdDate(new Date());
			loansDto.setOutAccountId(dto.getPayAccountSequence());// 投资人客户号
			loansDto.setTransAmt(tradeAmount);
			loansDto.setSubOrdId(String.valueOf(dto.getCashFlowId()));//投标订单号
			loansDto.setSubOrdDate(dto.getTradeDate());
			loansDto.setInAccountId(dto.getGatherAccountSequence());// 借款人第三方支付账号
			loansDto.setUnFreezeOrdId(String.valueOf(accountOrder3.getCashFlowId()));//解冻订单号 
			loansDto.setFreezeTrxId(String.valueOf(dto.getTrxId()));
			//手续费
			loansDto.setFeeObjFlag("I");//I代表入账账户,支付手续费
			loansDto.setFee(p2pFee);
			Map<Long, BigDecimal> routingAmount = new HashMap<Long, BigDecimal>();
			routingAmount.put(p2pPayAccount, p2pFee);
			loansDto.setRoutingAmount(routingAmount);
			loansDtoList.add(loansDto);

		}

		//更新项目状态——〉满标放款中
		investment.setInvestmentStatus(InvestmentStatusEnum.RELEASE_CASH.getCode());
		investmentRepository.save(investment);
		return loansDtoList;
	}

	/**
	 * @param investment
	 * @param accountOrderList
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<LoansNoFeeDto> releaseCashCreateOrder(Long investmentSequence, Investment investment, List<AccountOrder> accountOrderList) {
		//取得投资人订单信息
		List<LoansNoFeeDto> loansDtoList = new ArrayList<LoansNoFeeDto>();
		for (AccountOrder dto : accountOrderList) {

			ThirdPaymentAccount toThirdPaymentAccount = accountThirdRepository.findByAccountSequence(dto.getGatherAccountSequence());//贷款人第三方
			ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(dto.getPayAccountSequence());

			//生成放款订单
			AccountOrder accountOrder = new AccountOrder();
			accountOrder.setTradeDate(new Date());
			accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
			accountOrder.setTradeType(TradeTypeEnum.RELEASE_CASH.getCode());//交易类型
			accountOrder.setTradeAmount(dto.getTradeAmount());
			accountOrder.setCommodityTablePrimaryKeyValue(dto.getCommodityTablePrimaryKeyValue());
			accountOrder.setPayAccountSequence(dto.getPayAccountSequence());//付费用户P2P平台账号流水号
			accountOrder.setGatherAccountSequence(dto.getGatherAccountSequence());//收费用户P2P平台账号流水号
			accountOrder.setTradeDescription(investment.getInvestmentName());
			accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FREEZE));
			accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder.setGatherThirdPaymentIdBalance(toThirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder = accountOrderRepository.save(accountOrder);

			AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

			//生成解冻订单
			//			AccountOrder freezeOrder = new AccountOrder();
			//			freezeOrder.setTradeDate(new Date());
			//			freezeOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
			//			freezeOrder.setTradeType(TradeTypeEnum.RELEASE_CASH.getCode());//交易类型
			//			freezeOrder.setTradeAmount(dto.getTradeAmount());
			//			freezeOrder.setCommodityTablePrimaryKeyValue(dto.getCommodityTablePrimaryKeyValue());
			//			freezeOrder.setPayAccountSequence(dto.getPayAccountSequence());//付费用户P2P平台账号流水号
			//			freezeOrder.setGatherAccountSequence(dto.getGatherAccountSequence());//收费用户P2P平台账号流水号
			//			freezeOrder.setTradeDescription("");
			String freezeOrder = P2pUtil.generate20Random();
			//			freezeOrder.setThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			//			freezeOrder = accountOrderRepository.save(freezeOrder);

			// 生成用户订单状态历史
			//			accountOrderHistory = new AccountOrderHistory();
			//			accountOrderHistory.setAccountOrderSequence(freezeOrder.getAccountOrderSequence());
			//			accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
			//			accountOrderHistory.setOrderStatusChangeDate(new Date());
			//			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

			LoansNoFeeDto loansDto = new LoansNoFeeDto();
			ReqExtLoans reqExt = new ReqExtLoans();
			//reqExt.setLoansVocherAmt(new BigDecimal(0));
			reqExt.setProId(investment.getInvestmentNumber());
			loansDto.setReqExt(reqExt);
			loansDto.setOrdId(String.valueOf(accountOrder.getCashFlowId()));//放款订单号
			loansDto.setOrdDate(new Date());
			loansDto.setOutAccountId(dto.getPayAccountSequence());// 投资人客户号
			loansDto.setTransAmt(dto.getTradeAmount());
			loansDto.setSubOrdId(String.valueOf(dto.getCashFlowId()));//投标订单号
			loansDto.setSubOrdDate(dto.getTradeDate());
			loansDto.setInAccountId(dto.getGatherAccountSequence());// 借款人第三方支付账号
			loansDto.setUnFreezeOrdId(String.valueOf(freezeOrder));//解冻订单号 
			loansDto.setFreezeTrxId(String.valueOf(dto.getTrxId()));
			loansDtoList.add(loansDto);
		}

		//更新项目状态——〉满标放款中
		investment.setInvestmentStatus(InvestmentStatusEnum.RELEASE_CASH.getCode());
		investmentRepository.save(investment);
		return loansDtoList;
	}

	/**
	 * @param investmentSequence
	 * @param loansDtoList
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<PayResult> releaseCashThirdPay(Long investmentSequence, List<LoansNoFeeDto> loansDtoList) {
		List<PayResult> payResultList = new ArrayList<PayResult>();
		//发布消息 调用第三方接口支付
		for (LoansNoFeeDto loansNoFeeDto : loansDtoList) {
			//
			PayResult payResult = capitalPlatformManager.normalLoansNoFee(loansNoFeeDto);

			payResultList.add(payResult);
		}

		return payResultList;

	}

	/**
	 * 放款&收取手续费 add by chenchang 20150309
	 * 
	 * @param investmentSequence
	 * @param loansDtoList
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<PayResult> releaseCashThirdPayHaveFee(Long investmentSequence, List<LoansHaveFeeDto> loansDtoList) {
		List<PayResult> payResultList = new ArrayList<PayResult>();
		for (LoansHaveFeeDto loansHaveFeeDto : loansDtoList) {
			PayResult payResult = null;
			try {
				//发布消息 调用第三方接口支付
				payResult = capitalPlatformManager.normalLoansHaveFee(loansHaveFeeDto);
			} catch (Exception e) {
				e.printStackTrace();
				payResult = new PayResult();
				payResult.setOrdId(loansHaveFeeDto.getOrdId());
				payResult.setUnFreezeOrdId(loansHaveFeeDto.getUnFreezeOrdId());
				payResult.setResult(false);
				payResult.setMessage(e.getMessage());
			}
			payResultList.add(payResult);
		}
		return payResultList;

	}

	/**
	 * <p>
	 * 满标放款。
	 * </p>
	 * <p>
	 * 投资人->贷款人</br>
	 * </p>
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return
	 */
	public synchronized void releaseCashAfter(String orderId, String unFreezeOrdId, boolean isSuccess, String msg) {
		//更新订单
		Long investmentSequence = releaseCashAfterUpdateOrder(orderId, unFreezeOrdId, isSuccess, msg);
		if (isSuccess) {
			//放款成功
			executeSuccess(investmentSequence);
		}
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Long releaseCashAfterUpdateOrder(String orderId, String unFreezeOrdId, boolean isSuccess, String tradeDescription) {
		//放款投资人-->贷款人转账完成后处理
		// 获取转账订单
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);
		Long investmentSequence = null;

		for (AccountOrder accountOrder : accountOrderList) {
			if (investmentSequence == null) {
				investmentSequence = accountOrder.getCommodityTablePrimaryKeyValue();
			}

			ThirdPaymentAccount gThirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountOrder.getGatherAccountSequence());//贷款人第三方
			ThirdPaymentAccount pThirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountOrder.getPayAccountSequence());

			BigDecimal gforzenAmt = accountOrderRepository.findFreezeApplySuccessAmt(accountOrder.getGatherAccountSequence(),
					OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
			BigDecimal gbalanceAmt = accountOrder.getGatherThirdPaymentIdBalance().subtract(gforzenAmt);

			BigDecimal pforzenAmt = accountOrderRepository.findFreezeApplySuccessAmt(accountOrder.getPayAccountSequence(),
					OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
			BigDecimal pbalanceAmt = accountOrder.getPayerThirdPaymentIdBalance().subtract(pforzenAmt);

			//			BigDecimal pbalanceAmt = pThirdPaymentAccount.getThirdPaymentValidBalance();
			logger.info("放款订单ID:" + orderId + ", 投资人：" + accountOrder.getPayAccountSequence() + ", 投资人总额:"
					+ pThirdPaymentAccount.getThirdPaymentIdBalance() + ", 投资人可用余额:" + pbalanceAmt);

			int orderStatus = accountOrder.getOrderStatus();
			if (orderStatus == OrderStatusEnum.PAYING.getCode().intValue()) {

				if (isSuccess) {
					//平台更新交易记录
					//更新用户订单的订单状态-〉成功
					accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
					accountOrder.setOrderEditDate(new Date());

					accountOrder.setPayerThirdPaymentIdBalance(pbalanceAmt);
					accountOrder.setGatherThirdPaymentIdBalance(gbalanceAmt);
					accountOrderRepository.save(accountOrder);
					//生成用户订单状态历史
					AccountOrderHistory entity = new AccountOrderHistory();
					entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
					entity.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
					entity.setOrderStatusChangeDate(new Date());
					accountOrderHistoryRepository.save(entity);

				} else {
					// 平台更新交易记录
					// 更新用户订单的订单状态-〉失败 
					accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
					accountOrder.setTradeComment(tradeDescription);
					accountOrder.setOrderEditDate(new Date());
					accountOrder.setPayerThirdPaymentIdBalance(pbalanceAmt);
					accountOrder.setGatherThirdPaymentIdBalance(gbalanceAmt);
					accountOrderRepository.save(accountOrder);
					// 生成用户订单状态历史
					AccountOrderHistory entity = new AccountOrderHistory();
					entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
					entity.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
					entity.setOrderStatusChangeDate(new Date());
					accountOrderHistoryRepository.save(entity);
				}
			} else if (orderStatus == OrderStatusEnum.PAYMENT_SUCCESS.getCode().intValue()) {
			} else if (orderStatus == OrderStatusEnum.PAYMENT_FALIUE.getCode().intValue()) {
				if (isSuccess) {
					//平台更新交易记录
					//更新用户订单的订单状态-〉成功
					accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
					accountOrder.setOrderEditDate(new Date());
					accountOrder.setPayerThirdPaymentIdBalance(pbalanceAmt);
					accountOrder.setGatherThirdPaymentIdBalance(gbalanceAmt);
					accountOrderRepository.save(accountOrder);
					//生成用户订单状态历史
					AccountOrderHistory entity = new AccountOrderHistory();
					entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
					entity.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
					entity.setOrderStatusChangeDate(new Date());
					accountOrderHistoryRepository.save(entity);
				}
			}
		}

		List<AccountOrder> unFreezeOrderList = accountOrderRepository.findByCashFlowId(unFreezeOrdId);

		for (AccountOrder accountOrder : unFreezeOrderList) {
			int orderStatus = accountOrder.getOrderStatus();
			ThirdPaymentAccount gThirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountOrder.getGatherAccountSequence());//贷款人第三方
			ThirdPaymentAccount pThirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountOrder.getPayAccountSequence());

			BigDecimal gforzenAmt = accountOrderRepository.findFreezeApplySuccessAmt(accountOrder.getGatherAccountSequence(),
					OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
			BigDecimal gbalanceAmt = gThirdPaymentAccount.getThirdPaymentIdBalance().subtract(gforzenAmt);

			BigDecimal pbalanceAmt = pThirdPaymentAccount.getThirdPaymentValidBalance();

			if (orderStatus == OrderStatusEnum.PAYING.getCode().intValue()) {
				if (isSuccess) {
					//平台更新交易记录
					//更新用户订单的订单状态-〉成功
					accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
					accountOrder.setOrderEditDate(new Date());
					accountOrder.setPayerThirdPaymentIdBalance(pbalanceAmt);
					accountOrder.setGatherThirdPaymentIdBalance(gbalanceAmt);
					accountOrderRepository.save(accountOrder);
					//生成用户订单状态历史
					AccountOrderHistory entity = new AccountOrderHistory();
					entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
					entity.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
					entity.setOrderStatusChangeDate(new Date());
					accountOrderHistoryRepository.save(entity);

				} else {
					// 平台更新交易记录
					// 更新用户订单的订单状态-〉失败 
					accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
					accountOrder.setTradeComment(tradeDescription);
					accountOrder.setOrderEditDate(new Date());
					accountOrder.setPayerThirdPaymentIdBalance(pbalanceAmt);
					accountOrder.setGatherThirdPaymentIdBalance(gbalanceAmt);
					accountOrderRepository.save(accountOrder);
					// 生成用户订单状态历史
					AccountOrderHistory entity = new AccountOrderHistory();
					entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
					entity.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
					entity.setOrderStatusChangeDate(new Date());
					accountOrderHistoryRepository.save(entity);
				}
			} else if (orderStatus == OrderStatusEnum.PAYMENT_SUCCESS.getCode().intValue()) {
			} else if (orderStatus == OrderStatusEnum.PAYMENT_FALIUE.getCode().intValue()) {
				if (isSuccess) {
					//平台更新交易记录
					//更新用户订单的订单状态-〉成功
					accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
					accountOrder.setOrderEditDate(new Date());
					accountOrder.setPayerThirdPaymentIdBalance(pbalanceAmt);
					accountOrder.setGatherThirdPaymentIdBalance(gbalanceAmt);
					accountOrderRepository.save(accountOrder);
					//生成用户订单状态历史
					AccountOrderHistory entity = new AccountOrderHistory();
					entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
					entity.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
					entity.setOrderStatusChangeDate(new Date());
					accountOrderHistoryRepository.save(entity);
				}
			}
		}

		return investmentSequence;
	}

	/**
	 * @param isSuccess
	 * @param investmentSequence
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void executeSuccess(Long investmentSequence) {
		boolean isSuccess = false;
		// 检查查询是否存在付款中的订单
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
				investmentSequence, OrderStatusEnum.PAYING.getCode(), TradeTypeEnum.RELEASE_CASH.getCode());

		Investment investment = null;
		if (CollectionUtil.isEmpty(accountOrderList)) {
			//没有未支付完成的订单
			//检查是否有付款失败订单
			List<AccountOrder> list = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(investmentSequence,
					OrderStatusEnum.PAYMENT_FALIUE.getCode(), TradeTypeEnum.RELEASE_CASH.getCode());

			investment = investmentRepository.findOne(investmentSequence);
			if (investment == null) {
				logger.debug("标的" + investmentSequence + "不存在");
				return;
			}
			if (CollectionUtil.isEmpty(list)) {
				if (investment.getInvestmentStatus().intValue() != InvestmentStatusEnum.RELEASE_CASH_SUCCESS.getCode().intValue()
						&& investment.getInvestmentStatus().intValue() != InvestmentStatusEnum.AUTO_WITHDRAW_SUCCESS.getCode().intValue()
						&& investment.getInvestmentStatus().intValue() != InvestmentStatusEnum.AUTO_WITHDRAW_FAIL.getCode().intValue()) {
					isSuccess = true;
					//项目放款成功
					investment.setInvestmentStatus(InvestmentStatusEnum.RELEASE_CASH_SUCCESS.getCode());
					investment.setInvestmentCreditDate(new Date());

					investmentRepository.save(investment);
				}
			} else {
				isSuccess = false;
				//项目放款失败
				investment.setInvestmentStatus(InvestmentStatusEnum.RELEASE_CASH_FALIUE.getCode());
				investment.setInvestmentCreditDate(new Date());
				investmentRepository.save(investment);
			}
		}

		//放款成功
		if (isSuccess) {

			investment = investmentRepository.findOne(investmentSequence);

			double monthRate = 0; // 月利率
			Double yearR = 0D;
			int investmentSource = 0;
			LoanData loanData = null;
			BigDecimal PF_FEE_RATE = BigDecimal.ZERO;
			//期数
			final Integer investmentPeriod = Integer.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode()
					+ DictionaryEnum.T_INV_INV_PERIOD.getCode() + investment.getInvestmentPeriod()));
			Long borrowerAccountSequence = null;

			List<InvestmentAccountReference> investmentAccountReferenceList = investmentAccountReferenceRepository
					.findByInvestmentSequence(investmentSequence);
			if (!CollectionUtil.isEmpty(investmentAccountReferenceList)) {
				//贷款人不能为空
				loanData = loanDataRepository.findOne(investmentAccountReferenceList.get(0).getLoanDataSequence());
				investmentSource = loanData.getInvestmentSource();//项目来源

				yearR = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_IRATE.getCode()
						+ loanData.getInvestmentAnnualInterestRate()));

				monthRate = yearR / 12;// 月利率
				borrowerAccountSequence = investmentAccountReferenceList.get(0).getAccountSequence();
				PF_FEE_RATE = loanData.getP2pPayFeeRate();
			}

			Date baseDate = null;
			Date payDate = null;
			//项目来源是维信
			if (InvSrcEnum.VBS.getCode().intValue() == investmentSource && Integer.valueOf(DateFormatUtil.getNowDay()).intValue() > 27) {
				StringBuffer source = new StringBuffer();
				source = source.append(DateFormatUtil.getNowYear()).append(DateFormatUtil.getNowMonth()).append(27);
				payDate = DateUtil.toDateType(source.toString());
			} else {
				payDate = new Date();
			}
			baseDate = DateUtil.addDay(payDate, 1);

			//更新债权还款计划 账单日
			updateClaimPayPlan(investmentSequence, payDate);

			// 生成投资人 用户获得的项目，获得债权，债权收款计划
			List<Map<String, String>> usersMap = insertCliamGatherPlan(investment, monthRate, investmentPeriod, baseDate, loanData.getContractId());

			// 生成自动提现 手续费订单
			capitalWithdrawServiceManager.createWithdrawOrder(investmentSequence);

			// 生成合同
			List<Long> toUsers = new ArrayList<Long>();
			try {

				saveAccountContract(investmentSequence, yearR, loanData, investmentPeriod, borrowerAccountSequence, payDate, usersMap, toUsers,
						PF_FEE_RATE);
				PdfInfos pdfInfos = new PdfInfos();
				pdfInfos.setContractNo("V".concat(loanData.getContractId()));
				pdfInfos.setInvestment(investment);
				logger.debug("step[vbsPdf] 放款后zhuqiu发布异步event,pdfInfos={}", pdfInfos);
				asyncEventMessageGateway.publishEvent(pdfInfos);

			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("项目流水号：" + investmentSequence + "，生成合同失败");
			}

			// 发送站内信  通知投资用户已放款 《借款协议》电子合同
			AccountMessageTemplateData data = new AccountMessageTemplateData();
			data.setProjectName(investment.getInvestmentName());
			data.setProjectIdURL("#/investitem/" + investment.getInvestmentSequence());

			AlertDto alertDto = new AlertDto();
			alertDto.setAccountMessageTemplateData(data);
			alertDto.setAccountMessageTemplateEnum(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI);
			alertDto.setToUser(toUsers);

			try {
				eventMessageGateway.publishEvent(alertDto);
			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("项目流水号：" + investmentSequence + "，放款发送站内信失败");
			}
		}
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void saveAccountContract(Long investmentSequence, Double yearR, LoanData loanData, final Integer investmentPeriod,
			Long borrowerAccountSequence, Date payDate, List<Map<String, String>> usersMap, List<Long> toUsers, BigDecimal PF_FEE_RATE) {

		final double INV_MANAGE_FEE_RATE = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode()
				+ DictionaryEnum.ACT_INV_PMFRATE.getCode() + FeeRateEnum.ACT_INV_PMFRATE.getCode()));
		final double JUSTIC_RATE = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.LC_JFRATE.getCode()
				+ FeeRateEnum.LC_JFRATE.getCode()));
		//		final double PF_FEE_RATE = new Double(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode()
		//				+ DictionaryEnum.T_DIC_P2P_PAY_FRATE.getCode() + FeeRateEnum.P2P_PAY_FRATE.getCode()));//p2p平台管理费率

		final double monthRate = yearR / 12;// 月利率

		Account account = accountRepository.findOne(borrowerAccountSequence);
		if (account == null) {
			return;
		}
		String borrowerAccountUserName = account.getUserName();

		//每月还款金额
		BigDecimal payMonthAmount = BigDecimal.valueOf(
				FinanceUtil.PMT(Double.valueOf(monthRate), investmentPeriod, loanData.getAccountLoanAmount().doubleValue())).setScale(
				ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);

		List<AccountContract> accountContractList = new ArrayList<AccountContract>();
		for (Map<String, String> map : usersMap) {
			Long investerAccountSeq = Long.valueOf(map.get(ACCOUNT_SEQ));
			BigDecimal investmentAmount = new BigDecimal(map.get(TRADE_AMT));
			BigDecimal gatherPerMonth = new BigDecimal(map.get(GATHER_PER_MONTH));

			toUsers.add(Long.valueOf(map.get(ACCOUNT_SEQ)));
			Account investAccount = accountRepository.findOne(investerAccountSeq);
			if (investAccount == null) {
				continue;
			}

			AccountContract accountContract = new AccountContract();
			//accountContract.setAccountContractSequence(accountContractSequence);
			accountContract.setBorrowerAccountEmail(loanData.getEmail()); // 借款人邮箱地址
			accountContract.setBorrowerAccountName(loanData.getRealName());// 借款人真实姓名
			accountContract.setBorrowerAccountPid(loanData.getPid());// 借款人身份证号码
			accountContract.setBorrowerAccountUserNname(borrowerAccountUserName);// 借款人平台用户名
			accountContract.setContractCreateDate(new Date());// 放款日期
			accountContract.setContractNumber("V".concat(loanData.getContractId()));// 合同编号
			accountContract.setEarilierCleanCondition("正常还款6个月");// 提前还款条件
			accountContract.setGatherAccountSequence(investerAccountSeq);// 投资用户P2P平台账号流水号	
			accountContract.setGatherMonthAmount(gatherPerMonth);// 每月收款金额
			accountContract.setInvesterAccountName(investAccount.getRealName());//投资人真实姓名
			accountContract.setInvesterAccountPid(investAccount.getPid());// 投资人身份证号码
			accountContract.setInvesterAccountUserName(investAccount.getUserName());//投资人用户名
			accountContract.setInvestmentAmount(investmentAmount);// 投资金额
			accountContract.setInvestmentAnnualInterestRate(BigDecimal.valueOf(yearR));// 预期年利率	
			accountContract.setInvestmentPeriod(investmentPeriod);// 借款期限	
			accountContract.setInvestmentProjectManagementFeeRate(BigDecimal.valueOf(INV_MANAGE_FEE_RATE));//投资管理费率
			accountContract.setJusticInterestRate(BigDecimal.valueOf(JUSTIC_RATE));// 罚息率
			accountContract.setLoanAmount(loanData.getAccountLoanAmount());// 贷款金额
			accountContract.setLoanTarget(loanData.getInvestmentTarget());// 贷款用途
			accountContract.setP2pPayFeeRate(PF_FEE_RATE);//平台管理费率
			// 平台管理费
			BigDecimal claimPayPlanPlatformManagementFee = BigDecimal.valueOf(
					FinanceUtil.calcO2P_PlatformManagementFee(loanData.getAccountLoanAmount().doubleValue(), PF_FEE_RATE.doubleValue())).setScale(2,
					BigDecimal.ROUND_HALF_UP);
			accountContract.setClaimPayPlanPlatformManagementFee(claimPayPlanPlatformManagementFee);
			accountContract.setPayMonthAmount(payMonthAmount);//	每月还款金额
			accountContract.setPayAccountSequence(borrowerAccountSequence);//	借款人用户P2P平台账号流水号
			accountContract.setPayDay(DateUtil.formatDefault(payDate));//还款日
			accountContract.setInvestmentSequence(investmentSequence);
			accountContractList.add(accountContract);
		}

		if (!CollectionUtil.isEmpty(accountContractList)) {
			accountContractRepository.save(accountContractList);
		}
	}

	/**
	 * 解冻订单。
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void unFreezOrderByInvestmentSequence(Long investmentSequence) {
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
				investmentSequence, OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());

		for (AccountOrder accountOrder : accountOrderList) {
			accountOrder.setOrderStatus(OrderStatusEnum.UNFREEZE_SUCCESS.getCode());
			accountOrder.setOrderEditDate(new Date());
			accountOrderRepository.save(accountOrder);
		}

	}

	/**
	 * 生成债权收款计划。
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * 
	 * @return 投资人用户流水号
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Map<String, String>> insertCliamGatherPlan(Investment investment, double monthRate, Integer investmentPeriod, Date baseDate,
			String contractId) {
		List<Map<String, String>> toUsers = new ArrayList<Map<String, String>>();
		List<ClaimGatherPlan> claimGatherPlanList = null;
		List<AccountClaim> accountClaimList = null;
		AccountClaim accountClaim = null;
		ClaimGatherPlan claimGatherPlan = null;
		Long investmentSequence = investment.getInvestmentSequence();

		Integer gatherServiceAccountSequence = 1000;//vmoney-p2p平台收费账号流水号

		// 获取所有放款转账订单
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
				investmentSequence, OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
		//取得费率
		final double r = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.ACT_INV_PMFRATE.getCode()
				+ FeeRateEnum.ACT_INV_PMFRATE.getCode()));
		int invNum = 0;
		for (AccountOrder accountOrder : accountOrderList) {
			claimGatherPlanList = new ArrayList<ClaimGatherPlan>();
			accountClaimList = new ArrayList<AccountClaim>();
			//投资总额
			BigDecimal tradeAmount = accountOrder.getTradeAmount();
			final BigDecimal PMT_NORMAL = BigDecimal.valueOf(FinanceUtil.PMT(monthRate, investmentPeriod, tradeAmount.doubleValue()));
			// 解冻订单
			accountOrder.setOrderStatus(OrderStatusEnum.UNFREEZE_SUCCESS.getCode());
			accountOrder.setOrderEditDate(new Date());

			Map<String, String> hashmap = new HashMap<String, String>();
			//获得项目的用户
			hashmap.put(ACCOUNT_SEQ, String.valueOf(accountOrder.getPayAccountSequence()));
			hashmap.put(TRADE_AMT, String.valueOf(tradeAmount));
			hashmap.put(GATHER_PER_MONTH, String.valueOf(PMT_NORMAL));
			toUsers.add(hashmap);

			// 新增用户获得的项目
			AccountInvestment accountInvestment = new AccountInvestment();
			if (accountOrder.getTradeType().intValue() == TradeTypeEnum.INVEST_MANUAL.getCode().intValue()) {
				accountInvestment.setAccountGetInvestmentForm(ActInvGformEnum.MANUAL_INVEST.getCode());////用户获得项目的方式 常量-参考数据字典				
			} else {
				accountInvestment.setAccountGetInvestmentForm(ActInvGformEnum.AUTO_INVEST.getCode());////用户获得项目的方式 常量-参考数据字典
			}
			accountInvestment.setAccountInvestmentIssueDate(new Date());
			accountInvestment.setAccountInvestmentNumber(investment.getInvestmentNumber() + String.format("%03d", ++invNum));// 用户获得的项目编号　规则　要需求确认
			accountInvestment.setAccountInvestmentPayedPeriod(0);// 已收款（还款）期数
			accountInvestment.setAccountInvestmentQuota(accountOrder.getTradeAmount());// 投资额度
			accountInvestment.setAccountInvestmentStatus(AccountInvestmentStatusEnum.REPAY_NORMAL.getCode());// 用户获得的项目状态 常量-参考数据字典
			accountInvestment.setAccountSequence(accountOrder.getPayAccountSequence());
			accountInvestment.setAccountFirstSequence(accountOrder.getPayAccountSequence());
			accountInvestment.setInvestmentSequence(investmentSequence);
			accountInvestment.setAccountInvestmentContractId("V".concat(contractId));
			accountInvestment = accountInvestmentRepository.save(accountInvestment);

			int i = 1;

			// 上期计划剩余本金
			BigDecimal tmpAmt = BigDecimal.ZERO;
			BigDecimal tmpAmtRound = BigDecimal.ZERO;
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

				// 新增用户获得的债权
				accountClaim = new AccountClaim();
				accountClaim.setAccountClaimActualQuota(actualQuota);//债权额度
				accountClaim.setAccountClaimPayedPeriod(i);
				accountClaim.setAccountClaimPretendQuota(actualQuotaRound);//债权额度（四舍五入）
				accountClaim.setAccountClaimStatus(ActClmStatusEnum.REPAYMENT.getCode());// 债权状态 常量-参考数据字典
				accountClaim.setAccountClaimStatusChangeDate(new Date());
				accountClaim.setAccountInvestmentSequence(accountInvestment.getAccountInvestmentSequence());
				accountClaimList.add(accountClaim);

				// 生成债权收款计划
				//p2p平台投资管理费
				BigDecimal manageFee = actualInterestRound.multiply(BigDecimal.valueOf(r));

				claimGatherPlan = new ClaimGatherPlan();
				claimGatherPlan.setAccountInvestmentSequence(accountInvestment.getAccountInvestmentSequence());
				claimGatherPlan.setClaim_gather_plan_period(DateUtil.format(DateUtil.addMonth(baseDate, i), DateUtil.YYYYMMDD));//收款期数

				claimGatherPlan.setClaimGatherPlanCreateDate(new Date());
				claimGatherPlan.setClaimGatherPlanNatureDate(DateUtil.addMonth(baseDate, i));// 账单日
				claimGatherPlan.setClaimGatherPlanNumber(i);// 收款期数序号

				claimGatherPlan.setClaimGatherPlanActualInterest(actualInterest);// 利息
				claimGatherPlan.setClaimGatherPlanPretendInterest(actualInterestRound);// 利息（四舍五入）
				claimGatherPlan.setClaimGatherPlanActualJusticeInterest(BigDecimal.ZERO);// 罚息 
				claimGatherPlan.setClaimGatherPlanPretendJusticeInterest(BigDecimal.ZERO);// 罚息（四舍五入）

				claimGatherPlan.setClaimGatherPlanActualSurplus(actualSurplus);// 剩余本金
				claimGatherPlan.setClaimGatherPlanPretendSurplus(actualSurplusRound);// 剩余本金（四舍五入）

				claimGatherPlan.setClaimGatherPlanActualPrincipal(actualQuota);// 计划收本金
				claimGatherPlan.setClaimGatherPlanPretendPrincipal(actualQuotaRound);// 计划收本金（四舍五入）

				claimGatherPlan.setClaimGatherPlanActualTotalAmount(actualTotalAmount);// 计划收总额
				claimGatherPlan.setClaimGatherPlanPretendTotalAmount(actualTotalAmountRound);// 计划收总额（四舍五入）

				claimGatherPlan.setClaimGatherPlanStatus(ClaimGatherPlanStatusEnum.NOT_PAID.getCode());//收款计划状态
				claimGatherPlan.setGatherAccountSequence(accountOrder.getPayAccountSequence());//收费用户P2P平台账号流水号	
				claimGatherPlan.setJusticeFeeAccountSequence(accountOrder.getGatherAccountSequence());//支付罚息人P2P平台账号流水号	
				claimGatherPlan.setPayAccountSequence(accountOrder.getGatherAccountSequence());//付费用户P2P平台账号流水号
				claimGatherPlan.setGatherServiceAccountSequence(gatherServiceAccountSequence);// vmoney-p2p平台收费账号流水号
				claimGatherPlan.setAccountClaimShouldManageFee(manageFee);//p2p平台投资管理费
				manageFee = manageFee.setScale(ConstantsUtil.FLOD_INDEX2, ConstantsUtil.ROUND_HALF_UP);
				claimGatherPlan.setAccountClaimActualManageFee(manageFee);////p2p平台投资管理费（四舍五入）
				claimGatherPlan.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
				claimGatherPlan.setRecordEditDate(new Date());

				claimGatherPlanList.add(claimGatherPlan);
				i++;

			}
			accountClaimRepository.save(accountClaimList);
			claimGatherPlanRepository.save(claimGatherPlanList);
		}

		accountOrderRepository.save(accountOrderList);

		return toUsers;
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void updateClaimPayPlan(Long investmentSequence, Date payDate) {
		// 更新债权还款计划 账单日
		List<ClaimPayPlan> cliamPayPlanList = claimPayPlanRepository.findByInvestmentSequence(investmentSequence);

		for (ClaimPayPlan claimPayPlan : cliamPayPlanList) {
			Integer claimPayPlanNumber = claimPayPlan.getClaimPayPlanNumber();
			Date tmpPayDate = DateUtil.addMonth(payDate, claimPayPlanNumber);

			claimPayPlan.setClaimPayPlanNatureDate(tmpPayDate);
			claimPayPlan.setClaimPayPlanPeriod(DateUtil.format(tmpPayDate, DateUtil.YYYYMMDD));
			claimPayPlan.setRecordEditDate(new Date());
		}

		claimPayPlanRepository.save(cliamPayPlanList);
	}
}
