package com.vcredit.jdev.p2p.dto;

public class MerCashDto {
	/** 订单号 */
	private String ordId;
	/** 用户交易号 */
	private String usrCustId;
	/** 交易资金 */
	private String transAmt;
	/** 服务费 */
	private String servFee;
	/** 商户收取服务费的子账号 */
	private String servFeeAcctId;
	public String getOrdId() {
		return ordId;
	}
	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}
	public String getUsrCustId() {
		return usrCustId;
	}
	public void setUsrCustId(String usrCustId) {
		this.usrCustId = usrCustId;
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
	
}
