package com.vcredit.jdev.p2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 主动投标中的借款人信息
 * @author 周佩
 *
 */
public class BorrowerDetailsDto {
	@JsonProperty("BorrowerCustId")  
	private String borrowerCustId;
	@JsonProperty("BorrowerAmt")  
	private String borrowerAmt;
	@JsonProperty("BorrowerRate")  
	private String borrowerRate;
	@JsonProperty("ProId")  
	private String proId;
	public String getBorrowerCustId() {
		return borrowerCustId;
	}
	public void setBorrowerCustId(String borrowerCustId) {
		this.borrowerCustId = borrowerCustId;
	}
	public String getBorrowerAmt() {
		return borrowerAmt;
	}
	public void setBorrowerAmt(String borrowerAmt) {
		this.borrowerAmt = borrowerAmt;
	}
	public String getBorrowerRate() {
		//TODO
		/**注意:修改时间20150311,强制将借款汇率写成默认的1.00，王学彦说的！**/
		return "1.00";
//		return borrowerRate;
	}
	public void setBorrowerRate(String borrowerRate) {
		this.borrowerRate = borrowerRate;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}
}
