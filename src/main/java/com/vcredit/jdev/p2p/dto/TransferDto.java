package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

/**
 * 商户扣款DTO
 * @author 周佩
 *
 */
public class TransferDto {
	/** 订单号 */
	private String ordId;
	/** 出账客户号 */
	private String outCustId;
	/** 出账子账户 */
	private String outAcctId;
	/** 金额 */
	private BigDecimal transAmt;
	/** 入账客户号 */
	private String inCustId;
	/** 入账子账户 */
	private String inAcctId;
	/** 私有域 */
	private String merPriv;
	public String getOrdId() {
		return ordId;
	}
	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}
	public String getOutCustId() {
		return outCustId;
	}
	public void setOutCustId(String outCustId) {
		this.outCustId = outCustId;
	}
	public String getOutAcctId() {
		return outAcctId;
	}
	public void setOutAcctId(String outAcctId) {
		this.outAcctId = outAcctId;
	}
	public BigDecimal getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}
	public String getInCustId() {
		return inCustId;
	}
	public void setInCustId(String inCustId) {
		this.inCustId = inCustId;
	}
	public String getInAcctId() {
		return inAcctId;
	}
	public void setInAcctId(String inAcctId) {
		this.inAcctId = inAcctId;
	}
	public String getMerPriv() {
		return merPriv;
	}
	public void setMerPriv(String merPriv) {
		this.merPriv = merPriv;
	}
	
}