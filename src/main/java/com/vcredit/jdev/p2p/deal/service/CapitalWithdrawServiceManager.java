package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.capital.modal.CapitalPlatformManager;
import com.vcredit.jdev.p2p.dto.CapitalWithdrawReturnDto;
import com.vcredit.jdev.p2p.dto.PayResult;
import com.vcredit.jdev.p2p.entity.AccountCashOperationRecord;
import com.vcredit.jdev.p2p.entity.AccountFeePaymentRecord;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.AccountOrderHistory;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.AprStatusEnum;
import com.vcredit.jdev.p2p.enums.AprTypeEnum;
import com.vcredit.jdev.p2p.enums.ArdStatusEnum;
import com.vcredit.jdev.p2p.enums.ArdTypeEnum;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.FeeRateEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.OrderIDRuleEnum;
import com.vcredit.jdev.p2p.enums.OrderStatusEnum;
import com.vcredit.jdev.p2p.enums.RecStatusEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountTypeEnum;
import com.vcredit.jdev.p2p.enums.TradeTypeEnum;
import com.vcredit.jdev.p2p.repository.AccountBankCardRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountCashOperationRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountFeePaymentRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderHistoryRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;
import com.vcredit.jdev.p2p.util.CollectionUtil;
import com.vcredit.jdev.p2p.util.FinanceUtil;

/**
 * p2p平台提现服务费 。
 * 
 * @author zhuqiu
 *
 */
@Component
public class CapitalWithdrawServiceManager {

	/** 用户充值/提现记录 */
	@Autowired
	private AccountCashOperationRecordRepository accountCashOperationRecordRepository;

	@Autowired
	private AccountFeePaymentRecordRepository accountFeePaymentRecordRepository;

	/** 第三方支付账号 */
	@Autowired
	private AccountThirdRepository accountThirdRepository;

	/** 用户银行卡 */
	@Autowired
	private AccountBankCardRecordRepository accountBankCardRecordRepository;

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;

	/** 用户订单状态历史 */
	@Autowired
	private AccountOrderHistoryRepository accountOrderHistoryRepository;

	@Autowired
	private EventMessageGateway eventMessageGateway;

	/** 投资项目 */
	@Autowired
	private InvestmentRepository investmentRepository;

	/** 投资项目与贷款人关系 */
	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;

	@Autowired
	private LoanDataRepository loanDataRepository;

	@Autowired
	private CapitalPlatformManager capitalPlatformManager;
	@Autowired
	private DictionaryUtil dictionaryUtil;

	/**
	 * 满标放款成功后生成自动提现,p2p平台手续费订单
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param accountSequence
	 *            贷款人p2p平台用户账号
	 * @param amount
	 *            贷款金额
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void createWithdrawOrder(Long investmentSequence) {

		List<InvestmentAccountReference> investmentAccountReferenceList = investmentAccountReferenceRepository
				.findByInvestmentSequence(investmentSequence);

		if (CollectionUtil.isEmpty(investmentAccountReferenceList)) {
			//贷款人不能为空
		}
		//p2p收费 SDT3
		Long p2pGatherAccount = null;

		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_ACCOUNT.getCode());
		if (CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			p2pGatherAccount = 1L;
		} else {
			p2pGatherAccount = thirdPaymentAccountList.get(0).getAccountSequence();
		}

		for (InvestmentAccountReference investmentAccountReference : investmentAccountReferenceList) {
			Long accountSequence = investmentAccountReference.getAccountSequence();

			LoanData loanData = loanDataRepository.findOne(investmentAccountReference.getLoanDataSequence());
			//贷款金额	
			BigDecimal amount = loanData.getAccountLoanAmount();

			ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountSequence);
			//金额计算
			//p2p平台放款服务费率
			double r = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.P2P_RC_FRATE.getCode()
					+ FeeRateEnum.P2P_RC_FRATE.getCode()));

			BigDecimal p2pFee = BigDecimal.valueOf(FinanceUtil.calcO2P_MakeLoanFee(amount.doubleValue(), r));
			BigDecimal withdrawAmt = amount.subtract(p2pFee);

			//生成提现订单
			AccountOrder accountOrder = new AccountOrder();
			accountOrder.setTradeDate(new Date());
			accountOrder.setOrderStatus(OrderStatusEnum.NON_PAYMENT.getCode());//订单状态
			accountOrder.setTradeType(TradeTypeEnum.WITHDRAW.getCode());//交易类型
			accountOrder.setTradeAmount(withdrawAmt);
			accountOrder.setCommodityTablePrimaryKeyValue(investmentSequence);
			accountOrder.setPayAccountSequence(accountSequence);//付费用户P2P平台账号流水号
			accountOrder.setGatherAccountSequence(accountSequence);//收费用户P2P平台账号流水号
			accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder.setGatherThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.CASH));
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder = accountOrderRepository.save(accountOrder);

			// 生成用户订单状态历史
			AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.NON_PAYMENT.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

		}
	}

	/**
	 * 提现处理
	 * 
	 * @param accountSequence
	 *            p2p平台用户账号
	 * @param amount
	 *            提现金额
	 * @param bankCardNumber
	 *            银行卡号
	 * @param bankCode
	 *            所属银行
	 * @param isPlatform
	 *            true:平台体现,false:投资人提现
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public CapitalWithdrawReturnDto withdraw(Long accountSequence, BigDecimal amount, String bankCardNumber, Integer bankCode, boolean isPlatform) {
		CapitalWithdrawReturnDto returnDto = new CapitalWithdrawReturnDto();
		Long vmoneyP2pAccount = 0L;

		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_ACCOUNT.getCode());

		if (!CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			vmoneyP2pAccount = thirdPaymentAccountList.get(0).getAccountSequence();
		}

		//取得费率
		double r = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.P2P_DRAW_FRATE.getCode()
				+ FeeRateEnum.P2P_DRAW_FRATE.getCode()));

		//提现手续费
		BigDecimal drawP2pFee = BigDecimal.ZERO;
		//平台提现
		if (isPlatform) {
			drawP2pFee = BigDecimal.ZERO;
		} else {
			//提现手续费（投资用户）
			drawP2pFee = BigDecimal.valueOf(FinanceUtil.calcI2P_WithdrawFee(amount.doubleValue(), r));
		}

		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountSequence);
		if (thirdPaymentAccount == null) {
			returnDto.setResult(false);
			returnDto.setMsg("第三方支付账号未绑定");
			returnDto.setStatus(ResponseConstants.CommonCode.NOT_DEFINE_CODE);
			return returnDto;
		}

		String cashFlowId = P2pUtil.generate20Random(OrderIDRuleEnum.CASH);
		//生成提现订单
		AccountOrder accountOrder = new AccountOrder();
		accountOrder.setTradeDate(new Date());
		accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
		accountOrder.setTradeType(TradeTypeEnum.WITHDRAW.getCode());//交易类型
		accountOrder.setTradeAmount(amount);
		accountOrder.setCommodityTablePrimaryKeyValue(0L);
		accountOrder.setPayAccountSequence(accountSequence);//付费用户P2P平台账号流水号
		accountOrder.setGatherAccountSequence(accountSequence);//收费用户P2P平台账号流水号
		accountOrder.setTradeDescription("");
		accountOrder.setCashFlowId(cashFlowId);
		accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
		accountOrder.setGatherThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
		accountOrder.setOrderEditDate(new Date());
		accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
		accountOrder = accountOrderRepository.save(accountOrder);

		// 生成用户订单状态历史
		AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
		accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
		accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
		accountOrderHistory.setOrderStatusChangeDate(new Date());
		accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

		//生成提现手续费订单
		if (drawP2pFee.compareTo(BigDecimal.ZERO) > 0) {
			AccountOrder feeOrder = new AccountOrder();
			feeOrder.setTradeDate(new Date());
			feeOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
			feeOrder.setTradeType(TradeTypeEnum.P2P_WITHDRAW_FEE.getCode());//交易类型
			feeOrder.setTradeAmount(drawP2pFee);
			feeOrder.setCommodityTablePrimaryKeyValue(0L);
			feeOrder.setPayAccountSequence(accountSequence);//付费用户P2P平台账号流水号
			feeOrder.setGatherAccountSequence(vmoneyP2pAccount);//收费用户P2P平台账号流水号
			feeOrder.setTradeDescription("");
			feeOrder.setCashFlowId(cashFlowId);
			feeOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			feeOrder.setGatherThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			feeOrder.setOrderEditDate(new Date());
			feeOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrderRepository.save(feeOrder);
		}

		returnDto.setServFee(drawP2pFee);
		returnDto.setServFeeAcctId(String.valueOf(vmoneyP2pAccount));
		returnDto.setOrderDate(accountOrder.getTradeDate());
		returnDto.setOrderId(cashFlowId);
		returnDto.setResult(true);
		returnDto.setMsg("订单生成成功");
		returnDto.setStatus(ResponseConstants.CommonCode.SUCCESS_CODE);
		return returnDto;
	}

	/**
	 * 提现返回后回调处理
	 * 
	 * @param accountSequence
	 *            p2p平台用户账号
	 * @param amount
	 *            提现金额
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public synchronized void withdrawAfter(String orderId, boolean isSuccess, String tradeDescription, String bankCardNumber, Integer bankCode,
			BigDecimal hfFee) {
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);

		//取得费率
		double r = Double.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.P2P_DRAW_FRATE.getCode()
				+ FeeRateEnum.P2P_DRAW_FRATE.getCode()));
		//提现手续费（投资用户）
		BigDecimal withdrawAmt = BigDecimal.ZERO;
		BigDecimal drawP2pFee = BigDecimal.ZERO;
		Long vmoneyP2pAccount = 0L;
		Long accountSequence = 0L;

		for (AccountOrder accountOrder : accountOrderList) {
			if (TradeTypeEnum.WITHDRAW.getCode().intValue() == accountOrder.getTradeType().intValue()) {
				withdrawAmt = accountOrder.getTradeAmount();
				accountSequence = accountOrder.getPayAccountSequence();
			} else if (TradeTypeEnum.P2P_WITHDRAW_FEE.getCode().intValue() == accountOrder.getTradeType().intValue()) {
				drawP2pFee = accountOrder.getTradeAmount();
				accountSequence = accountOrder.getPayAccountSequence();
				vmoneyP2pAccount = accountOrder.getGatherAccountSequence();
			}
		}

		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountSequence);
		BigDecimal forzenAmt = accountOrderRepository.findFreezeApplySuccessAmt(accountSequence, OrderStatusEnum.FREEZE_SUCCESS.getCode(),
				TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
		BigDecimal balanceAmt = thirdPaymentAccount.getThirdPaymentIdBalance().subtract(forzenAmt);
		for (AccountOrder accountOrder : accountOrderList) {
			int orderStatus = accountOrder.getOrderStatus();
			if (isSuccess
					&& (orderStatus == OrderStatusEnum.PAYING.getCode().intValue() || orderStatus == OrderStatusEnum.PAYMENT_FALIUE.getCode()
							.intValue())) {
				//平台更新交易记录
				//更新用户订单的订单状态-〉成功
				accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
				accountOrder.setOrderEditDate(new Date());
				accountOrder.setPayerThirdPaymentIdBalance(balanceAmt);
				accountOrder.setGatherThirdPaymentIdBalance(balanceAmt);
				accountOrderRepository.save(accountOrder);
				//生成用户订单状态历史
				AccountOrderHistory entity = new AccountOrderHistory();
				entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
				entity.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
				entity.setOrderStatusChangeDate(new Date());
				accountOrderHistoryRepository.save(entity);

				if (TradeTypeEnum.WITHDRAW.getCode().intValue() == accountOrder.getTradeType().intValue()) {
					//生成提现记录 
					AccountCashOperationRecord accountCashOperationRecord = null;
					List<AccountCashOperationRecord> accountCashOperationRecordList = accountCashOperationRecordRepository.findByCashFlowId(orderId);
					if (accountCashOperationRecordList.size() == 0) {
						accountCashOperationRecord = new AccountCashOperationRecord();
					} else {
						accountCashOperationRecord = accountCashOperationRecordList.get(0);
					}

					accountCashOperationRecord.setAccountSequence(accountSequence);//p2p平台用户流水号
					accountCashOperationRecord.setBankCardNumber(bankCardNumber);
					accountCashOperationRecord.setBankCode(bankCode);
					accountCashOperationRecord.setDrawP2pFee(drawP2pFee);//p2p平台取现手续费
					accountCashOperationRecord.setDrawServiceAccountSequence(vmoneyP2pAccount);//vmoney-提现服务p2p平台账号	
					accountCashOperationRecord.setOperateDate(new Date());//操作时间
					accountCashOperationRecord.setOperateDescription("");//操作描述
					accountCashOperationRecord.setOperateMoney(withdrawAmt);//充值/提现金额
					accountCashOperationRecord.setOperateThirdPaymentFee(BigDecimal.ZERO);//第三方支付平台手续费
					accountCashOperationRecord.setOperateType(ArdTypeEnum.WITHDRAW.getCode());//操作类型
					accountCashOperationRecord.setThirdPaymentAccountSequence(null);//第三方支付p2p平台账号

					accountCashOperationRecord.setOperateStats(ArdStatusEnum.SUCCESS.getCode());//操作状态
					accountCashOperationRecord.setCashFlowId(orderId);
					accountCashOperationRecordRepository.save(accountCashOperationRecord);

					if (hfFee.compareTo(BigDecimal.ZERO) > 0) {
						//第三方-汇付-取现服务
						Long P2P_MDT_ACCOUNT = null;
						List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
								.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_MDT_ACCOUNT.getCode());

						if (!CollectionUtil.isEmpty(thirdPaymentAccountList)) {
							P2P_MDT_ACCOUNT = thirdPaymentAccountList.get(0).getAccountSequence();
						}
						AccountOrder feeOrder = new AccountOrder();
						feeOrder.setTradeDate(new Date());
						feeOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());//订单状态
						feeOrder.setTradeType(TradeTypeEnum.THIRD_WITHDRAW_SERVICE.getCode());//交易类型
						feeOrder.setTradeAmount(hfFee);
						feeOrder.setCommodityTablePrimaryKeyValue(0L);
						feeOrder.setPayAccountSequence(P2P_MDT_ACCOUNT);//付费用户P2P平台账号流水号
						feeOrder.setGatherAccountSequence(accountSequence);//收费用户P2P平台账号流水号
						feeOrder.setTradeDescription("");
						feeOrder.setCashFlowId(accountOrder.getCashFlowId());
						feeOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
						feeOrder.setGatherThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
						feeOrder.setOrderEditDate(new Date());
						feeOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
						accountOrderRepository.save(feeOrder);
					}
				} else if (TradeTypeEnum.P2P_WITHDRAW_FEE.getCode().intValue() == accountOrder.getTradeType().intValue()) {
					// 缴费记录
					AccountFeePaymentRecord accountFeePaymentRecord = new AccountFeePaymentRecord();
					accountFeePaymentRecord.setActualPaymentDate(new Date());
					accountFeePaymentRecord.setGatherAccountSequence(vmoneyP2pAccount);
					accountFeePaymentRecord.setGatherDate(new Date());
					accountFeePaymentRecord.setPayAccountSequence(accountSequence);
					accountFeePaymentRecord.setPaymentAmount(drawP2pFee);
					accountFeePaymentRecord.setPaymentDescription("提现手续费");
					accountFeePaymentRecord.setPaymentName("提现手续费");
					accountFeePaymentRecord.setPaymentType(AprTypeEnum.P2P_WITHDRAW_FEE.getCode());
					accountFeePaymentRecord.setPreparePaymentDate(new Date());
					accountFeePaymentRecord.setGatherStatus(AprStatusEnum.PAY_SUCCESS.getCode());
					accountFeePaymentRecord.setCashFlowId(orderId);
					accountFeePaymentRecordRepository.save(accountFeePaymentRecord);
				}

			} else if (!isSuccess && orderStatus == OrderStatusEnum.PAYING.getCode().intValue()) {
				//失败 
				accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
				accountOrder.setTradeComment(tradeDescription);
				accountOrder.setOrderEditDate(new Date());
				accountOrder.setPayerThirdPaymentIdBalance(balanceAmt);
				accountOrder.setGatherThirdPaymentIdBalance(balanceAmt);
				accountOrderRepository.save(accountOrder);
				//生成用户订单状态历史
				AccountOrderHistory entity = new AccountOrderHistory();
				entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
				entity.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
				entity.setOrderStatusChangeDate(new Date());
				accountOrderHistoryRepository.save(entity);

				if (TradeTypeEnum.WITHDRAW.getCode().intValue() == accountOrder.getTradeType().intValue()) {
					//生成提现记录 
					AccountCashOperationRecord accountCashOperationRecord = null;
					List<AccountCashOperationRecord> accountCashOperationRecordList = accountCashOperationRecordRepository.findByCashFlowId(orderId);
					if (accountCashOperationRecordList.size() == 0) {
						accountCashOperationRecord = new AccountCashOperationRecord();
					} else {
						accountCashOperationRecord = accountCashOperationRecordList.get(0);
					}

					accountCashOperationRecord.setAccountSequence(accountSequence);//p2p平台用户流水号
					accountCashOperationRecord.setBankCardNumber(bankCardNumber);
					accountCashOperationRecord.setBankCode(bankCode);
					accountCashOperationRecord.setDrawP2pFee(drawP2pFee);//p2p平台取现手续费
					accountCashOperationRecord.setDrawServiceAccountSequence(vmoneyP2pAccount);//vmoney-提现服务p2p平台账号	
					accountCashOperationRecord.setOperateDate(new Date());//操作时间
					accountCashOperationRecord.setOperateDescription("");//操作描述
					accountCashOperationRecord.setOperateMoney(withdrawAmt);//充值/提现金额
					accountCashOperationRecord.setOperateThirdPaymentFee(BigDecimal.ZERO);//第三方支付平台手续费
					accountCashOperationRecord.setOperateType(ArdTypeEnum.WITHDRAW.getCode());//操作类型
					accountCashOperationRecord.setThirdPaymentAccountSequence(null);//第三方支付p2p平台账号

					accountCashOperationRecord.setOperateStats(ArdStatusEnum.FAILURE.getCode());//操作状态
					accountCashOperationRecord.setCashFlowId(orderId);
					accountCashOperationRecordRepository.save(accountCashOperationRecord);

				} else if (TradeTypeEnum.P2P_WITHDRAW_FEE.getCode().intValue() == accountOrder.getTradeType().intValue()) {

					// 缴费记录
					AccountFeePaymentRecord accountFeePaymentRecord = new AccountFeePaymentRecord();
					accountFeePaymentRecord.setActualPaymentDate(new Date());
					accountFeePaymentRecord.setGatherAccountSequence(vmoneyP2pAccount);
					accountFeePaymentRecord.setGatherDate(new Date());
					accountFeePaymentRecord.setPayAccountSequence(accountSequence);
					accountFeePaymentRecord.setPaymentAmount(drawP2pFee);
					accountFeePaymentRecord.setPaymentDescription("提现手续费");
					accountFeePaymentRecord.setPaymentName("提现手续费");
					accountFeePaymentRecord.setPaymentType(AprTypeEnum.P2P_WITHDRAW_FEE.getCode());
					accountFeePaymentRecord.setPreparePaymentDate(new Date());
					accountFeePaymentRecord.setGatherStatus(AprStatusEnum.PAY_FAILURE.getCode());
					accountFeePaymentRecord.setCashFlowId(orderId);
					accountFeePaymentRecordRepository.save(accountFeePaymentRecord);
				}
			}
		}
	}

	/**
	 * 自动提现，p2p平台扣取手续费
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param amount
	 *            贷款金额
	 * @return
	 */
	public Response autoWithdrawAndFee(Long investmentSequence) {
		Long p2pGatherAccount = null;

		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_ACCOUNT.getCode());
		if (CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "p2p平台收费账户不存在");
		}

		p2pGatherAccount = thirdPaymentAccountList.get(0).getAccountSequence();

		//取得 取现 未付款订单信息
		List<AccountOrder> withDrawOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
				investmentSequence, OrderStatusEnum.NON_PAYMENT.getCode(), TradeTypeEnum.WITHDRAW.getCode());

		for (int i = 0; i < withDrawOrderList.size(); i++) {
			AccountOrder withDrawOrder = withDrawOrderList.get(i);

			// 更新订单
			autoWithdrawAndFeeUpdateOrder(withDrawOrder);

			//汇付接口
			PayResult payResult = null;
			try {
				payResult = capitalPlatformManager.merCash(String.valueOf(withDrawOrder.getCashFlowId()), withDrawOrder.getPayAccountSequence(),
						withDrawOrder.getTradeAmount(), BigDecimal.ZERO, p2pGatherAccount);
			} catch (Exception e) {
				e.printStackTrace();
				payResult = new PayResult();
				payResult.setResult(false);
				payResult.setOrdId(String.valueOf(withDrawOrder.getCashFlowId()));
				payResult.setMessage(e.getMessage());
			}

			//回调处理
			autoWithdrawAndFeeBack(payResult.getOrdId(), payResult.isResult(), payResult.getMessage());

			if (payResult.isResult()) {
				return Response.response(ResponseConstants.CommonCode.SUCCESS_CODE, "自动取现成功");
			} else {
				return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "自动取现失败 " + payResult.getMessage());
			}
		}

		//取得 放款失败  订单信息
		withDrawOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(investmentSequence,
				OrderStatusEnum.PAYMENT_FALIUE.getCode(), TradeTypeEnum.WITHDRAW.getCode());

		//补单
		for (int i = 0; i < withDrawOrderList.size(); i++) {
			AccountOrder withDrawOrder = withDrawOrderList.get(i);
			//更新订单
			autoWithdrawAndFeeUpdateOrder(withDrawOrder);

			PayResult payResult = null;
			try {
				payResult = capitalPlatformManager.merCash(String.valueOf(withDrawOrder.getCashFlowId()), withDrawOrder.getPayAccountSequence(),
						withDrawOrder.getTradeAmount(), BigDecimal.ZERO, p2pGatherAccount);
			} catch (Exception e) {
				e.printStackTrace();
				payResult = new PayResult();
				payResult.setResult(false);
				payResult.setOrdId(String.valueOf(withDrawOrder.getCashFlowId()));
				payResult.setMessage(e.getMessage());
			}

			//支付后回调处理
			autoWithdrawAndFeeBack(payResult.getOrdId(), payResult.isResult(), payResult.getMessage());

			if (payResult.isResult()) {
				return Response.response(ResponseConstants.CommonCode.SUCCESS_CODE, "成功");
			} else {
				return Response.response(ResponseConstants.CommonCode.SUCCESS_CODE, "失败" + " " + payResult.getMessage());
			}
		}
		return Response.response(ResponseConstants.CommonCode.SUCCESS_CODE, "成功");
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void autoWithdrawAndFeeUpdateOrder(AccountOrder withDrawOrder) {
		//更新订单
		withDrawOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
		withDrawOrder.setOrderEditDate(new Date());
		withDrawOrder = accountOrderRepository.save(withDrawOrder);
		// 生成用户订单状态历史
		AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
		accountOrderHistory.setAccountOrderSequence(withDrawOrder.getAccountOrderSequence());
		accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
		accountOrderHistory.setOrderStatusChangeDate(new Date());

		accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

		//		//更新订单
		//		feeOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
		//		feeOrder.setOrderEditDate(new Date());
		//		feeOrder = accountOrderRepository.save(feeOrder);
		//		// 生成用户订单状态历史
		//		accountOrderHistory = new AccountOrderHistory();
		//		accountOrderHistory.setAccountOrderSequence(feeOrder.getAccountOrderSequence());
		//		accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
		//		accountOrderHistory.setOrderStatusChangeDate(new Date());
		//		accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);
	}

	/**
	 * 自动提现，p2p平台扣取手续费 回调处理
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param amount
	 *            贷款金额
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public synchronized void autoWithdrawAndFeeBack(String orderId, boolean isResult, String msg) {
		List<AccountOrder> withDrawOrderList = accountOrderRepository.findByCashFlowId(orderId);
		AccountOrder withDrawOrder = withDrawOrderList.get(0);
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(withDrawOrder.getPayAccountSequence());

		BigDecimal forzenAmt = accountOrderRepository.findFreezeApplySuccessAmt(withDrawOrder.getPayAccountSequence(),
				OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
		BigDecimal balanceAmt = thirdPaymentAccount.getThirdPaymentIdBalance().subtract(forzenAmt);

		int orderStatus = withDrawOrder.getOrderStatus();
		if (isResult
				&& (orderStatus == OrderStatusEnum.PAYING.getCode().intValue() || orderStatus == OrderStatusEnum.PAYMENT_FALIUE.getCode().intValue())) {
			// 更新订单
			withDrawOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());//订单状态
			withDrawOrder.setOrderEditDate(new Date());
			withDrawOrder.setPayerThirdPaymentIdBalance(balanceAmt);
			withDrawOrder.setGatherThirdPaymentIdBalance(balanceAmt);
			withDrawOrder = accountOrderRepository.save(withDrawOrder);
			// 生成用户订单状态历史
			AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(withDrawOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

			// 更新项目状态
			Investment investment = investmentRepository.findOne(withDrawOrder.getCommodityTablePrimaryKeyValue());
			investment.setInvestmentStatus(InvestmentStatusEnum.AUTO_WITHDRAW_SUCCESS.getCode());
			investmentRepository.save(investment);

		} else if (!isResult && orderStatus == OrderStatusEnum.PAYING.getCode().intValue()) {
			// 更新订单
			withDrawOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());//订单状态
			withDrawOrder.setTradeComment(msg);
			withDrawOrder.setOrderEditDate(new Date());
			withDrawOrder.setPayerThirdPaymentIdBalance(balanceAmt);
			withDrawOrder.setGatherThirdPaymentIdBalance(balanceAmt);
			withDrawOrder = accountOrderRepository.save(withDrawOrder);
			// 生成用户订单状态历史
			AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(withDrawOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

			// 更新项目状态
			Investment investment = investmentRepository.findOne(withDrawOrder.getCommodityTablePrimaryKeyValue());
			investment.setInvestmentStatus(InvestmentStatusEnum.AUTO_WITHDRAW_FAIL.getCode());
			investmentRepository.save(investment);
		}

	}
}
