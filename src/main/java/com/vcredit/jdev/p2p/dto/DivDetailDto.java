package com.vcredit.jdev.p2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 分账账户串
 * @author 周佩
 *
 */
public class DivDetailDto {
	/**
	 * 商户账号
	 */
	@JsonProperty("DivCustId")
	private String divCustId;
	/**
	 * 商户子账号 
	 */
	@JsonProperty("DivAcctId")
	private String divAcctId;
	/**
	 * 金额
	 */
	@JsonProperty("DivAmt")
	private String divAmt;
	public String getDivCustId() {
		return divCustId;
	}
	public void setDivCustId(String divCustId) {
		this.divCustId = divCustId;
	}
	public String getDivAcctId() {
		return divAcctId;
	}
	public void setDivAcctId(String divAcctId) {
		this.divAcctId = divAcctId;
	}
	public String getDivAmt() {
		return divAmt;
	}
	public void setDivAmt(String divAmt) {
		this.divAmt = divAmt;
	}
}
