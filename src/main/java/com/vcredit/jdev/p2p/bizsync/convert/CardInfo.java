package com.vcredit.jdev.p2p.bizsync.convert;

/**
 * 
 * @author zhaopeijun
 *
 */
public class CardInfo {

	private String bankCardNumber = "";
	private String bankCode = "";
	private String bankProvince = "";
	private String bankCity = "";
	
	public String getBankCardNumber() {
		return bankCardNumber;
	}
	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankProvince() {
		return bankProvince;
	}
	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}
	public String getBankCity() {
		return bankCity;
	}
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

}
