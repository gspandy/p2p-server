package com.vcredit.jdev.p2p.dto;


/**
 * 三方支付的用户传输类
 * 可选字段
 * @author 周佩
 *
 */
public class ThirdPayUserRequestDto extends ThirdPayBaseRequestDto{
	/** 用户号 */
	private String usrId;
	/** 用户客户号 */
	private String usrCustId;
	/** 订单号 */
	private String orderId;
	/** 真实姓名 */
	private String usrName;
	/** 身份类型 默认身份证：00 */
	private String idType = "00";
	/** 证件号码 */
	private String idNo;
	/** 手机号 */
	private String usrMp;
	/** 用户Email */
	private String usrEmail;
	/** 编码集 */
	private String charSet;
	/** 签名 */
	private String chkValue;
	/** 订单日期 */
	private String ordDate;
	/** 金额 */
	private String transAmt;
	/**银行卡代码*/
	private String openBankId;
	/**银行卡名*/
	private String openBankName;
	private String servFee;
	private String servFeeAcctId;
	private String openAcctId;
	private String reqExt;
	private String borrowerDetails;
	/**冻结订单号*/
	private String freezeOrdId;
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getUsrName() {
		return usrName;
	}
	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getUsrMp() {
		return usrMp;
	}
	public void setUsrMp(String usrMp) {
		this.usrMp = usrMp;
	}
	public String getUsrEmail() {
		return usrEmail;
	}
	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}
	public String getCharSet() {
		return charSet;
	}
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	public String getChkValue() {
		return chkValue;
	}
	public void setChkValue(String chkValue) {
		this.chkValue = chkValue;
	}
	public String getUsrCustId() {
		return usrCustId;
	}
	public void setUsrCustId(String usrCustId) {
		this.usrCustId = usrCustId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrdDate() {
		return ordDate;
	}
	public void setOrdDate(String ordDate) {
		this.ordDate = ordDate;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getServFee() {
		return servFee;
	}
	public void setServFee(String servFee) {
		this.servFee = servFee;
	}
	public String getServFeeAcctId() {
		return servFeeAcctId;
	}
	public void setServFeeAcctId(String servFeeAcctId) {
		this.servFeeAcctId = servFeeAcctId;
	}
	public String getOpenAcctId() {
		return openAcctId;
	}
	public void setOpenAcctId(String openAcctId) {
		this.openAcctId = openAcctId;
	}
	public String getReqExt() {
		return reqExt;
	}
	public void setReqExt(String reqExt) {
		this.reqExt = reqExt;
	}
	public String getOpenBankId() {
		return openBankId;
	}
	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}
	public String getOpenBankName() {
		return openBankName;
	}
	public void setOpenBankName(String openBankName) {
		this.openBankName = openBankName;
	}
	public String getBorrowerDetails() {
		return borrowerDetails;
	}
	public void setBorrowerDetails(String borrowerDetails) {
		this.borrowerDetails = borrowerDetails;
	}
	public String getFreezeOrdId() {
		return freezeOrdId;
	}
	public void setFreezeOrdId(String freezeOrdId) {
		this.freezeOrdId = freezeOrdId;
	}

}
