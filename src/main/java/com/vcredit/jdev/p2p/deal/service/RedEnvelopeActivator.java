package com.vcredit.jdev.p2p.deal.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.dto.RedEnvelopeBackMessageDto;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.AccountOrderHistory;
import com.vcredit.jdev.p2p.enums.OrderStatusEnum;
import com.vcredit.jdev.p2p.enums.RecStatusEnum;
import com.vcredit.jdev.p2p.enums.TradeTypeEnum;
import com.vcredit.jdev.p2p.repository.AccountOrderHistoryRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;

/**
 * 红包交易消息处理类。
 * 
 * @author zhuqiu
 *
 */
@Component
public class RedEnvelopeActivator {

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;

	/** 用户订单状态历史 */
	@Autowired
	private AccountOrderHistoryRepository accountOrderHistoryRepository;

	/**
	 * 红包
	 * 
	 * @return
	 */
	@ServiceActivator(inputChannel = "eventPublishChannel")
	public void handle(Object event) throws Exception {
		if (!(event instanceof RedEnvelopeBackMessageDto)) {
			return;
		}

		RedEnvelopeBackMessageDto dto = (RedEnvelopeBackMessageDto) event;

		if (dto.isResult()) {
			//生成提现订单
			AccountOrder accountOrder = new AccountOrder();
			accountOrder.setTradeDate(new Date());
			accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());//订单状态
			accountOrder.setTradeType(TradeTypeEnum.RED_ENVELOPE_PURCHASE.getCode());//交易类型
			accountOrder.setTradeAmount(dto.getTradeAmount());
			accountOrder.setCommodityTablePrimaryKeyValue(dto.getCommodityTablePrimaryKeyValue());
			accountOrder.setPayAccountSequence(dto.getPayAccountSequence());//付费用户P2P平台账号流水号
			accountOrder.setGatherAccountSequence(dto.getGatherAccountSequence());//收费用户P2P平台账号流水号
			accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder = accountOrderRepository.save(accountOrder);

			// 生成用户订单状态历史
			AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

		} else {
			//生成提现订单
			AccountOrder accountOrder = new AccountOrder();
			accountOrder.setTradeDate(new Date());
			accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());//订单状态
			accountOrder.setTradeType(TradeTypeEnum.RED_ENVELOPE_PURCHASE.getCode());//交易类型
			accountOrder.setTradeAmount(dto.getTradeAmount());
			accountOrder.setCommodityTablePrimaryKeyValue(dto.getCommodityTablePrimaryKeyValue());
			accountOrder.setPayAccountSequence(dto.getPayAccountSequence());//付费用户P2P平台账号流水号
			accountOrder.setGatherAccountSequence(dto.getGatherAccountSequence());//收费用户P2P平台账号流水号
			accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder = accountOrderRepository.save(accountOrder);

			// 生成用户订单状态历史
			AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);
		}

	}

}
