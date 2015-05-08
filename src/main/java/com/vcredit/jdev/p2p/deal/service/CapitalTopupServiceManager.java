package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.dto.CapitalTopupReturnDto;
import com.vcredit.jdev.p2p.entity.AccountCashOperationRecord;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.AccountOrderHistory;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.ArdStatusEnum;
import com.vcredit.jdev.p2p.enums.ArdTypeEnum;
import com.vcredit.jdev.p2p.enums.OrderIDRuleEnum;
import com.vcredit.jdev.p2p.enums.OrderStatusEnum;
import com.vcredit.jdev.p2p.enums.RecStatusEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountTypeEnum;
import com.vcredit.jdev.p2p.enums.TradeTypeEnum;
import com.vcredit.jdev.p2p.repository.AccountBankCardRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountCashOperationRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderHistoryRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
import com.vcredit.jdev.p2p.util.CollectionUtil;

/**
 * p2p平台充值服务费 。
 * 
 * @author zhuqiu
 *
 */
@Component
public class CapitalTopupServiceManager {

	/** 用户充值/提现记录 */
	@Autowired
	private AccountCashOperationRecordRepository accountCashOperationRecordRepository;

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

	/**
	 * 充值处理
	 * 
	 * @param accountSequence
	 *            p2p平台用户账号
	 * @param amount
	 *            提现金额
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public CapitalTopupReturnDto capitalTopup(Long accountSequence, BigDecimal amt) {
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountSequence);
		CapitalTopupReturnDto returnDto = new CapitalTopupReturnDto();

		if (thirdPaymentAccount == null) {
			returnDto.setMsg("第三方支付账号未绑定");
			returnDto.setResult(false);
			returnDto.setStatus(ResponseConstants.CommonCode.NOT_DEFINE_CODE);
			return returnDto;
		}

		String cashFlowId = P2pUtil.generate20Random(OrderIDRuleEnum.NETSAVE);
		//生成充值订单
		AccountOrder accountOrder = new AccountOrder();
		accountOrder.setTradeDate(new Date());
		accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
		accountOrder.setTradeType(TradeTypeEnum.RECHARGE.getCode());//交易类型
		accountOrder.setTradeAmount(amt);
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

		returnDto.setResult(true);
		returnDto.setOrderId(accountOrder.getCashFlowId());
		returnDto.setOrderDate(accountOrder.getTradeDate());
		return returnDto;

	}

	/**
	 * 充值处理
	 * 
	 * @param accountSequence
	 *            p2p平台用户账号
	 * @param amount
	 *            提现金额
	 * @param bankCardNumber
	 *            银行卡号
	 * @param bankCode
	 *            银行类型
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public synchronized void capitalTopupAfter(String orderId, boolean isResult, String bankCardNumber, Integer bankCode, String msg, BigDecimal hffee) {
		Long P2P_MDT_ACCOUNT = null;
		List<ThirdPaymentAccount> thirdPaymentAccountList = accountThirdRepository
				.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_MDT_ACCOUNT.getCode());

		if (!CollectionUtil.isEmpty(thirdPaymentAccountList)) {
			P2P_MDT_ACCOUNT = thirdPaymentAccountList.get(0).getAccountSequence();
		}
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);
		AccountOrder accountOrder = accountOrderList.get(0);

		int orderStatus = accountOrder.getOrderStatus();
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountOrder.getPayAccountSequence());

		BigDecimal forzenAmt = accountOrderRepository.findFreezeApplySuccessAmt(accountOrder.getPayAccountSequence(),
				OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
		BigDecimal balanceAmt = thirdPaymentAccount.getThirdPaymentIdBalance().subtract(forzenAmt);
		if (isResult
				&& (orderStatus == OrderStatusEnum.PAYING.getCode().intValue() || orderStatus == OrderStatusEnum.PAYMENT_FALIUE.getCode().intValue())) {

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

			//生成充值记录 
			AccountCashOperationRecord accountCashOperationRecord = new AccountCashOperationRecord();
			accountCashOperationRecord.setAccountSequence(accountOrder.getPayAccountSequence());//p2p平台用户流水号
			accountCashOperationRecord.setBankCardNumber(bankCardNumber);
			accountCashOperationRecord.setBankCode(bankCode);
			accountCashOperationRecord.setDrawP2pFee(BigDecimal.ZERO);//p2p平台取现手续费
			accountCashOperationRecord.setDrawServiceAccountSequence(null);//vmoney-提现服务p2p平台账号	
			accountCashOperationRecord.setOperateDate(new Date());//操作时间
			accountCashOperationRecord.setOperateDescription("TODO 用户充值");//操作描述
			accountCashOperationRecord.setOperateMoney(accountOrder.getTradeAmount());//充值/提现金额
			accountCashOperationRecord.setOperateThirdPaymentFee(BigDecimal.ZERO);//第三方支付平台手续费
			accountCashOperationRecord.setOperateType(ArdTypeEnum.RECHARGE.getCode());//操作类型
			accountCashOperationRecord.setThirdPaymentAccountSequence(null);//第三方支付p2p平台账号
			//生成提现记录 
			accountCashOperationRecord.setOperateStats(ArdStatusEnum.SUCCESS.getCode());//操作状态
			accountCashOperationRecord.setCashFlowId(orderId);
			accountCashOperationRecordRepository.save(accountCashOperationRecord);

			// 第三方-汇付-充值服务 
			AccountOrder feeOrder = new AccountOrder();
			feeOrder.setTradeDate(new Date());
			feeOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());//订单状态
			feeOrder.setTradeType(TradeTypeEnum.THIRD_RECHARGE_SERVICE.getCode());//交易类型
			feeOrder.setTradeAmount(hffee);
			feeOrder.setCommodityTablePrimaryKeyValue(0L);
			feeOrder.setPayAccountSequence(P2P_MDT_ACCOUNT);//付费用户P2P平台账号流水号
			feeOrder.setGatherAccountSequence(P2P_MDT_ACCOUNT);//收费用户P2P平台账号流水号
			feeOrder.setTradeDescription("");
			feeOrder.setCashFlowId(accountOrder.getCashFlowId());
			feeOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			feeOrder.setGatherThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
			feeOrder.setOrderEditDate(new Date());
			feeOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrderRepository.save(feeOrder);

		} else if (!isResult && orderStatus == OrderStatusEnum.PAYING.getCode().intValue()) {
			//平台更新交易记录
			//失败 
			accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
			accountOrder.setTradeComment(msg);
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

			//生成充值记录 
			AccountCashOperationRecord accountCashOperationRecord = new AccountCashOperationRecord();
			accountCashOperationRecord.setAccountSequence(accountOrder.getPayAccountSequence());//p2p平台用户流水号
			accountCashOperationRecord.setBankCardNumber(bankCardNumber);
			accountCashOperationRecord.setBankCode(bankCode);
			accountCashOperationRecord.setDrawP2pFee(BigDecimal.ZERO);//p2p平台取现手续费
			accountCashOperationRecord.setDrawServiceAccountSequence(null);//vmoney-提现服务p2p平台账号	
			accountCashOperationRecord.setOperateDate(new Date());//操作时间
			accountCashOperationRecord.setOperateDescription("TODO 用户充值");//操作描述
			accountCashOperationRecord.setOperateMoney(accountOrder.getTradeAmount());//充值/提现金额
			accountCashOperationRecord.setOperateThirdPaymentFee(BigDecimal.ZERO);//第三方支付平台手续费
			accountCashOperationRecord.setOperateType(ArdTypeEnum.RECHARGE.getCode());//操作类型
			accountCashOperationRecord.setThirdPaymentAccountSequence(null);//第三方支付p2p平台账号
			//生成提现记录 
			accountCashOperationRecord.setOperateStats(ArdStatusEnum.FAILURE.getCode());//操作状态
			accountCashOperationRecord.setCashFlowId(orderId);
			accountCashOperationRecordRepository.save(accountCashOperationRecord);
		}
	}
}
