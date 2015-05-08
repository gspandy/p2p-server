package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;
import java.util.Date;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.dto.DepositCapitalDto;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.AccountOrderHistory;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.OrderIDRuleEnum;
import com.vcredit.jdev.p2p.enums.OrderStatusEnum;
import com.vcredit.jdev.p2p.enums.RecStatusEnum;
import com.vcredit.jdev.p2p.enums.TradeTypeEnum;
import com.vcredit.jdev.p2p.repository.AccountInvestRuleRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderHistoryRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;

/**
 * 红包交易。
 * 
 * @author zhuqiu
 *
 */
@Component
public class RedEnvelopeManager {
	/** 投资项目 */
	@Autowired
	private InvestmentRepository investmentRepository;

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;

	/** 用户订单状态历史 */
	@Autowired
	private AccountOrderHistoryRepository accountOrderHistoryRepository;

	/** 用户第三方支付 */
	@Autowired
	private AccountThirdRepository accountThirdRepository;

	/** 用户定义的投资规则 */
	@Autowired
	private AccountInvestRuleRepository accountInvestRuleRepository;

	/** 用户p2p平台账号Repository */
	@Autowired
	private AccountRepository accountRepository;

	/** 投资项目与贷款人关系 */
	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;

	@Autowired
	private EventMessageGateway eventMessageGateway;

	/**
	 * 红包
	 * 
	 * @param accountSequence
	 *            用户
	 * @param tradeAmount
	 *            红包金额
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Response useRedEnvelope(Long accountSequence, BigDecimal tradeAmount) {
		//TODO 
		Long payAccountSequence = 9999L;

		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountSequence);

		if (thirdPaymentAccount == null) {
			return new Response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "第三方支付账号未开通!", null);
		}

		//生成提现订单
		AccountOrder accountOrder = new AccountOrder();
		accountOrder.setTradeDate(new Date());
		accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
		accountOrder.setTradeType(TradeTypeEnum.RED_ENVELOPE_PURCHASE.getCode());//交易类型
		accountOrder.setTradeAmount(tradeAmount);
		accountOrder.setCommodityTablePrimaryKeyValue(null);
		accountOrder.setPayAccountSequence(payAccountSequence);//付费用户P2P平台账号流水号
		accountOrder.setGatherAccountSequence(accountSequence);//收费用户P2P平台账号流水号
		accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.REDENVELOPE));
		accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
		//TODO: Logic need to be implemented
		accountOrder.setGatherThirdPaymentIdBalance(new BigDecimal(0));
		accountOrder.setOrderEditDate(new Date());
		accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
		accountOrder = accountOrderRepository.save(accountOrder);

		// 生成用户订单状态历史
		AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
		accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
		accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
		accountOrderHistory.setOrderStatusChangeDate(new Date());
		accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

		//TODO 1.调用第三方支付接口
		DepositCapitalDto obj = new DepositCapitalDto();
		return Response.successResponse();
	}

}
