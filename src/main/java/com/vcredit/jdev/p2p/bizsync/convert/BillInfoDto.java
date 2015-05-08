package com.vcredit.jdev.p2p.bizsync.convert;

/**
 * 
 * @author zhaopeijun
 *
 */
public class BillInfoDto {
	private String investmentSource;
	private Integer billType = 0;
	private String investmentBusinessCode;
	private Integer loanPeriod = 0;
	private String totalShouldGet;
	private String totalActulGet;
	private String cutShouldDate;
	
	public String getInvestmentSource() {
		return investmentSource;
	}
	public void setInvestmentSource(String investmentSource) {
		this.investmentSource = investmentSource;
	}
	public String getCutShouldDate() {
		return cutShouldDate;
	}
	public void setCutShouldDate(String cutShouldDate) {
		this.cutShouldDate = cutShouldDate;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public String getInvestmentBusinessCode() {
		return investmentBusinessCode;
	}
	public void setInvestmentBusinessCode(String investmentBusinessCode) {
		this.investmentBusinessCode = investmentBusinessCode;
	}
	public Integer getLoanPeriod() {
		return loanPeriod;
	}
	public void setLoanPeriod(Integer loanPeriod) {
		this.loanPeriod = loanPeriod;
	}
	public String getTotalShouldGet() {
		return totalShouldGet;
	}
	public void setTotalShouldGet(String totalShouldGet) {
		this.totalShouldGet = totalShouldGet;
	}
	public String getTotalActulGet() {
		return totalActulGet;
	}
	public void setTotalActulGet(String totalActulGet) {
		this.totalActulGet = totalActulGet;
	}
	
}
