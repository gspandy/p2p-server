package com.vcredit.jdev.p2p.dto;

import java.util.List;


/**
 * 三方支付的用户传输类
 * 可选字段
 * @author 周佩
 *
 */
public class ThirdPayCashRequestDto extends ThirdPayBaseRequestDto{
	/** 用户客户号 */
	private String usrCustId;
	/** 订单号 */
	private String ordId;
	/** 编码集 */
	private String charSet;
	/** 金额 */
	private String transAmt;
	/**银行卡名*/
	private String openBankName;
	private String servFee;
	private String servFeeAcctId;
	private String openAcctId;
	private List<ReqExtCashDto> reqExt;
	public String getUsrCustId() {
		return usrCustId;
	}
	public void setUsrCustId(String usrCustId) {
		this.usrCustId = usrCustId;
	}
	public String getOrdId() {
		return ordId;
	}
	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}
	public String getCharSet() {
		return charSet;
	}
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getOpenBankName() {
		return openBankName;
	}
	public void setOpenBankName(String openBankName) {
		this.openBankName = openBankName;
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
	public List<ReqExtCashDto> getReqExt() {
		return reqExt;
	}
	public void setReqExt(List<ReqExtCashDto> reqExt) {
		this.reqExt = reqExt;
	}
}
