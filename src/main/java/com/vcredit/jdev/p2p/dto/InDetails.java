package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 还款
 * @author 周佩
 *
 */
public class InDetails {
	/**入账客户号*/
	@JsonProperty("InCustId")
	private String inCustId;
	/**入账子账号 非必须*/
	@JsonProperty("InAcctId")
	private String inAcctId;
	/**还款订单号*/
	@JsonProperty("OrdId")
	private String ordId;
	/**原投标订单号*/
	@JsonProperty("SubOrdId")
	private String subOrdId;
	/**手续费收取对象标志 I/O*/
	@JsonProperty("FeeObjFlag")
	private String feeObjFlag;
	/**交易金额*/
	@JsonProperty("TransAmt")
	private BigDecimal transAmt;
	/**扣款手续费*/
	@JsonProperty("Fee")
	private BigDecimal fee;
	/**分账账户串*/
	@JsonProperty("DivDetails")
	private List<DivDetailDto> divDetails;
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
	public String getOrdId() {
		return ordId;
	}
	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}
	public String getSubOrdId() {
		return subOrdId;
	}
	public void setSubOrdId(String subOrdId) {
		this.subOrdId = subOrdId;
	}
	public String getFeeObjFlag() {
		return feeObjFlag;
	}
	public void setFeeObjFlag(String feeObjFlag) {
		this.feeObjFlag = feeObjFlag;
	}
	public BigDecimal getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public List<DivDetailDto> getDivDetails() {
		return divDetails;
	}
	public void setDivDetails(List<DivDetailDto> divDetails) {
		this.divDetails = divDetails;
	}
	
}
