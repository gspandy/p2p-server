package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

/**
 * 红包消息发送类
 * 
 * @author zhuqiu
 *
 */
public class RedEnvelopeBackMessageDto {

	/** 处理结果 */
	private boolean result;
	/** 订单金额 */
	private BigDecimal tradeAmount;
	/** 付费用户P2P平台账号流水号 */
	private Long payAccountSequence;
	/** 收费用户P2P平台账号流水号 */
	private Long gatherAccountSequence;
	/** 交易商品表主键值 */
	private Long commodityTablePrimaryKeyValue;

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public Long getCommodityTablePrimaryKeyValue() {
		return commodityTablePrimaryKeyValue;
	}

	public void setCommodityTablePrimaryKeyValue(Long commodityTablePrimaryKeyValue) {
		this.commodityTablePrimaryKeyValue = commodityTablePrimaryKeyValue;
	}

	public Long getPayAccountSequence() {
		return payAccountSequence;
	}

	public Long getGatherAccountSequence() {
		return gatherAccountSequence;
	}

	public void setPayAccountSequence(Long payAccountSequence) {
		this.payAccountSequence = payAccountSequence;
	}

	public void setGatherAccountSequence(Long gatherAccountSequence) {
		this.gatherAccountSequence = gatherAccountSequence;
	}

}
