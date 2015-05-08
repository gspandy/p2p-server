package com.vcredit.jdev.p2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 
 * @author 周佩
 *
 */
public class AcctDetail {
	@JsonProperty("AcctType")
	private String acctType;
	@JsonProperty("SubAcctId")
	private String subAcctId;
	@JsonProperty("AvlBal")
	private String avlBal;
	@JsonProperty("AcctBal")
	private String acctBal;
	@JsonProperty("FrzBal")
	private String frzBal;
	public String getAvlBal() {
		return avlBal;
	}
	public void setAvlBal(String avlBal) {
		this.avlBal = avlBal;
	}
	public String getAcctBal() {
		return acctBal;
	}
	public void setAcctBal(String acctBal) {
		this.acctBal = acctBal;
	}
	public String getFrzBal() {
		return frzBal;
	}
	public void setFrzBal(String frzBal) {
		this.frzBal = frzBal;
	}
	public String getAcctType() {
		return acctType;
	}
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}
	public String getSubAcctId() {
		return subAcctId;
	}
	public void setSubAcctId(String subAcctId) {
		this.subAcctId = subAcctId;
	}

}
