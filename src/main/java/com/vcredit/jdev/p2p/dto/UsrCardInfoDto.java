package com.vcredit.jdev.p2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 银行卡信息
 * @author 周佩
 *
 */
public class UsrCardInfoDto {
	/**商户号*/
	@JsonProperty("MerCustId")
	private String merCustId;
	/**用户汇付账号*/
	@JsonProperty("UsrCustId")
	private String usrCustId;
	/**用户名*/
	@JsonProperty("UsrName")
	private String usrName;
	/**身份证*/
	@JsonProperty("CertId")
	private String certId;
	/**银行简称*/
	@JsonProperty("BankId")
	private String bankId;
	/**银行卡号*/
	@JsonProperty("CardId")
	private String cardId;
	/**标示*/
	@JsonProperty("RealFlag")
	private String realFlag;
	/**更新时间*/
	@JsonProperty("UpdDateTime")
	private String updDateTime;
	/**省码*/
	@JsonProperty("ProvId")
	private String provId;
	/**地区码*/
	@JsonProperty("AreaId")
	private String areaId;
	/**是否默认卡号*/
	@JsonProperty("IsDefault")
	private String isDefault;
	/***/
	@JsonProperty("ExpressFlag")
	private String expressFlag;
	public String getMerCustId() {
		return merCustId;
	}
	public void setMerCustId(String merCustId) {
		this.merCustId = merCustId;
	}
	public String getUsrCustId() {
		return usrCustId;
	}
	public void setUsrCustId(String usrCustId) {
		this.usrCustId = usrCustId;
	}
	public String getUsrName() {
		return usrName;
	}
	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}
	public String getCertId() {
		return certId;
	}
	public void setCertId(String certId) {
		this.certId = certId;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getRealFlag() {
		return realFlag;
	}
	public void setRealFlag(String realFlag) {
		this.realFlag = realFlag;
	}
	public String getUpdDateTime() {
		return updDateTime;
	}
	public void setUpdDateTime(String updDateTime) {
		this.updDateTime = updDateTime;
	}
	public String getProvId() {
		return provId;
	}
	public void setProvId(String provId) {
		this.provId = provId;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getExpressFlag() {
		return expressFlag;
	}
	public void setExpressFlag(String expressFlag) {
		this.expressFlag = expressFlag;
	}
	
}
