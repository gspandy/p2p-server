package com.vcredit.jdev.p2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 三方接口参数存必须字段
 * @author 周佩
 *
 */
public class ThirdPayBaseResponseDto {
	/** 消息类型 */
	@JsonProperty("CmdId")
	private String cmdId;
	/** 消息类型 */
	@JsonProperty("Version")
	private String version;
	/** 应答返回码 */
	@JsonProperty("RespCode")
	private String respCode;
	/** 应答描述 */
	@JsonProperty("RespDesc")
	private String respDesc;
	/** 商户客户号 */
	@JsonProperty("MerCustId")
	private String merCustId;
	/** 用户客户号 */
	@JsonProperty("UsrCustId")
	private String usrCustId;
	/** 商户应答地址 */
	@JsonProperty("BgRetUrl")
	private String bgRetUrl;
	/** 签名 */
	@JsonProperty("ChkValue")
	private String chkValue;
	/** 商户私有域 */
	@JsonProperty("MerPriv")
	private String merPriv;
	public String getCmdId() {
		return cmdId;
	}
	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
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
	public String getBgRetUrl() {
		return bgRetUrl;
	}
	public void setBgRetUrl(String bgRetUrl) {
		this.bgRetUrl = bgRetUrl;
	}
	public String getChkValue() {
		return chkValue;
	}
	public void setChkValue(String chkValue) {
		this.chkValue = chkValue;
	}
	public String getMerPriv() {
		return merPriv;
	}
	public void setMerPriv(String merPriv) {
		this.merPriv = merPriv;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

}
