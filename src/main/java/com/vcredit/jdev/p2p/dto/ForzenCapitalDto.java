package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;

/**
 * 资金冻结Dto
 * 
 * @author 周佩
 *
 */
public class ForzenCapitalDto {
	/** 冻结资金账户 */
	private ThirdPaymentAccount orig;
	/** 冻结资金账户金额 */
	private BigDecimal price;
	/** 项目流水号 */
	private Long investmentSequence;
	/** 订单信息 */
	private BigDecimal accountOrderSequence;// 流水号

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

	public BigDecimal getAccountOrderSequence() {
		return accountOrderSequence;
	}

	public void setAccountOrderSequence(BigDecimal accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
	}


}
