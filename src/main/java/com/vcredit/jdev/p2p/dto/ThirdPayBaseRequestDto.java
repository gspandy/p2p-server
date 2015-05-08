package com.vcredit.jdev.p2p.dto;
/**
 * 三方接口参数存必须字段
 * @author 周佩
 *
 */
public class ThirdPayBaseRequestDto {
	/** 版本号 */
	private String version;
	/** 消息类型 */
	private String cmdId;
	/** 商户客户号 */
	private String merCustId;
	/** 商户应答地址 */
	private String bgRetUrl;
	/** 页面返回URL */
	private String retUrl;
	/** 商户私有域 */
	private String merPriv;
	/** 签名 */
	private String chkValue;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCmdId() {
		return cmdId;
	}
	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}
	public String getMerCustId() {
		return merCustId;
	}
	public void setMerCustId(String merCustId) {
		this.merCustId = merCustId;
	}
	public String getBgRetUrl() {
		return bgRetUrl;
	}
	public void setBgRetUrl(String bgRetUrl) {
		this.bgRetUrl = bgRetUrl;
	}
	public String getMerPriv() {
		return merPriv;
	}
	public void setMerPriv(String merPriv) {
		this.merPriv = merPriv;
	}
	public String getChkValue() {
		return chkValue;
	}
	public void setChkValue(String chkValue) {
		this.chkValue = chkValue;
	}
	public String getRetUrl() {
		return retUrl;
	}
	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}
	
}
