package com.vcredit.jdev.p2p.bizsync.convert;

public class VbsDetailRs {

	/** 贷款业务编号 */
	private String loanBusinessId;
	
	/** 解析错误编码 */
	private String parseErrCode;
	
	/** 解析错误信息 */
	private String parseErrMsg;
	
	/** 期数 */
	private Integer loanPeriod;
	
	

	public Integer getLoanPeriod() {
		return loanPeriod;
	}

	public void setLoanPeriod(Integer loanPeriod) {
		this.loanPeriod = loanPeriod;
	}

	public String getLoanBusinessId() {
		return loanBusinessId;
	}

	public void setLoanBusinessId(String loanBusinessId) {
		this.loanBusinessId = loanBusinessId;
	}

	public String getParseErrCode() {
		return parseErrCode;
	}

	public void setParseErrCode(String parseErrCode) {
		this.parseErrCode = parseErrCode;
	}

	public String getParseErrMsg() {
		return parseErrMsg;
	}

	public void setParseErrMsg(String parseErrMsg) {
		this.parseErrMsg = parseErrMsg;
	}
	
	
}
