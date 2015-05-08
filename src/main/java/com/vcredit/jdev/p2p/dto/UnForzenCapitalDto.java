package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;

/**
 * 资金解冻
 * 
 * @author 周佩
 *
 */
public class UnForzenCapitalDto {
	/** 解冻资金账户 */
	private ThirdPaymentAccount orig;
	/** 解冻资金账户金额 */
	private BigDecimal price;
	/** 项目流水号 */
	private Long investmentSequence;
	/** 订单信息 */
	private String orderId;
	/** 本平台交易唯一标识frezeOrdId */
	private String trxId;

	public ThirdPaymentAccount getOrig() {
		return orig;
	}

	public void setOrig(ThirdPaymentAccount orig) {
		this.orig = orig;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getInvestmentSequence() {
		return investmentSequence;
	}

	public void setInvestmentSequence(Long investmentSequence) {
		this.investmentSequence = investmentSequence;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
