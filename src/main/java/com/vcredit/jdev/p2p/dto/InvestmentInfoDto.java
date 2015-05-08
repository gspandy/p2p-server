package com.vcredit.jdev.p2p.dto;

/**
 * 给后台系统使用的数据对象
 * 
 * @author demoklis
 *
 */
public class InvestmentInfoDto {
	// 初始本金--用户项目 0 double
	private String initialCapital;
	// 剩余本金--用户债权 0 double
	private String surplusCapital;
	// 剩余应收本息--收款计划 0 double
	private String surplusPrincipalAndInterest;
	// 月还款额
	private String monthRepayCapital;
	//已还期数
	private String payedPeriod;

	public String getInitialCapital() {
		return initialCapital;
	}

	public void setInitialCapital(String initialCapital) {
		this.initialCapital = initialCapital;
	}

	public String getSurplusCapital() {
		return surplusCapital;
	}

	public void setSurplusCapital(String surplusCapital) {
		this.surplusCapital = surplusCapital;
	}

	public String getSurplusPrincipalAndInterest() {
		return surplusPrincipalAndInterest;
	}

	public void setSurplusPrincipalAndInterest(String surplusPrincipalAndInterest) {
		this.surplusPrincipalAndInterest = surplusPrincipalAndInterest;
	}

	public String getMonthRepayCapital() {
		return monthRepayCapital;
	}

	public void setMonthRepayCapital(String monthRepayCapital) {
		this.monthRepayCapital = monthRepayCapital;
	}

	public String getPayedPeriod() {
		return payedPeriod;
	}

	public void setPayedPeriod(String payedPeriod) {
		this.payedPeriod = payedPeriod;
	}
}
