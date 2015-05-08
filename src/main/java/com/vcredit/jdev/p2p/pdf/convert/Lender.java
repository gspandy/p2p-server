package com.vcredit.jdev.p2p.pdf.convert;

import java.math.BigDecimal;

/**
 * 借款协议-投资人信息
 *
 * @author zhaopeijun
 */
public class Lender {
	
	/** 投资用户名 */
	private String investerName;
	/** 投资金额 */
	private BigDecimal investmentAmount;
	/** 借款期限 */
	private Integer lendPeriod;
	/** 每月还款金额 */
	private BigDecimal montyPayAmount;
	
	/** 投资用户真实姓名 */
	private String investerRealName;
	/** 投资用户身份证 */
	private String investerPid;
	
	//convert用
	/** 维金会用户名（动态） */
	private String showName;
	
	public String getInvesterName() {
		return investerName;
	}
	public void setInvesterName(String investerName) {
		this.investerName = investerName;
	}
	public BigDecimal getInvestmentAmount() {
		return investmentAmount;
	}
	public void setInvestmentAmount(BigDecimal investmentAmount) {
		this.investmentAmount = investmentAmount;
	}
	public Integer getLendPeriod() {
		return lendPeriod;
	}
	public void setLendPeriod(Integer lendPeriod) {
		this.lendPeriod = lendPeriod;
	}
	public BigDecimal getMontyPayAmount() {
		return montyPayAmount;
	}
	public void setMontyPayAmount(BigDecimal montyPayAmount) {
		this.montyPayAmount = montyPayAmount;
	}
	public String getInvesterRealName() {
		return investerRealName;
	}
	public void setInvesterRealName(String investerRealName) {
		this.investerRealName = investerRealName;
	}
	public String getInvesterPid() {
		return investerPid;
	}
	public void setInvesterPid(String investerPid) {
		this.investerPid = investerPid;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	
}
