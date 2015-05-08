package com.vcredit.jdev.p2p.dto;

/**
 * 给页面数据
 * 
 * @author 周佩
 *
 */
public class ThirdAccountView {
	/** 三方账号 */
	private String thirdAccount;
	/** 账户余额 */
	private String accountBalance;
	/** 可用余额 */
	private String availableBalance;
	/** 冻结资金 */
	private String forzenCapital;
	/** 总计被冻结成功的投资项目数量 */
	private String investmentCount;
	/** 持有项目 */
	private String holdProject;
	/** 预期总收益 */
	private String investCapital;
	/** 今日总收益 */
	private String yield;
	/** 累计总收益 */
	private String totalIncome;
	/** 累计净收益 */
	private String accumulatedIncome;
	/** 今日投资项目 */
	private String investProjectToday;
	/** 今日投资金额 */
	private String investCapitalToday;
	/** 今日逾期投资项目 */
	private String investProjectOverdue;
	/** 今日逾期投资金额 */
	private String investCapitalOverdue;
	/** 预计本月待收 */
	private String totalIncomeMonth;
	/** 本月已收 */
	private String haveIncomeMonth;
	/** 在投本金 */
	private String currentInvestCapital;
	/** 在投本金占比% */
	private String capitalPercent;
	/** 预期总收益占比% */
	private String futureEarningsPercent;
	/** 累计总收益占比% */
	private String allEarningsPercent;
	/** 累计推荐好友数 */
	private String recommendFriendRecord;

	public String getThirdAccount() {
		return thirdAccount;
	}

	public void setThirdAccount(String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getForzenCapital() {
		return forzenCapital;
	}

	public void setForzenCapital(String forzenCapital) {
		this.forzenCapital = forzenCapital;
	}

	public String getHoldProject() {
		return holdProject;
	}

	public void setHoldProject(String holdProject) {
		this.holdProject = holdProject;
	}

	public String getInvestCapital() {
		return investCapital;
	}

	public void setInvestCapital(String investCapital) {
		this.investCapital = investCapital;
	}

	public String getYield() {
		return yield;
	}

	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(String totalIncome) {
		this.totalIncome = totalIncome;
	}

	public String getInvestProjectToday() {
		return investProjectToday;
	}

	public void setInvestProjectToday(String investProjectToday) {
		this.investProjectToday = investProjectToday;
	}

	public String getInvestCapitalToday() {
		return investCapitalToday;
	}

	public void setInvestCapitalToday(String investCapitalToday) {
		this.investCapitalToday = investCapitalToday;
	}

	public String getInvestProjectOverdue() {
		return investProjectOverdue;
	}

	public void setInvestProjectOverdue(String investProjectOverdue) {
		this.investProjectOverdue = investProjectOverdue;
	}

	public String getInvestCapitalOverdue() {
		return investCapitalOverdue;
	}

	public void setInvestCapitalOverdue(String investCapitalOverdue) {
		this.investCapitalOverdue = investCapitalOverdue;
	}

	public String getTotalIncomeMonth() {
		return totalIncomeMonth;
	}

	public void setTotalIncomeMonth(String totalIncomeMonth) {
		this.totalIncomeMonth = totalIncomeMonth;
	}

	public String getHaveIncomeMonth() {
		return haveIncomeMonth;
	}

	public void setHaveIncomeMonth(String haveIncomeMonth) {
		this.haveIncomeMonth = haveIncomeMonth;
	}

	public String getCapitalPercent() {
		return capitalPercent;
	}

	public void setCapitalPercent(String capitalPercent) {
		this.capitalPercent = capitalPercent;
	}

	public String getFutureEarningsPercent() {
		return futureEarningsPercent;
	}

	public void setFutureEarningsPercent(String futureEarningsPercent) {
		this.futureEarningsPercent = futureEarningsPercent;
	}

	public String getAllEarningsPercent() {
		return allEarningsPercent;
	}

	public void setAllEarningsPercent(String allEarningsPercent) {
		this.allEarningsPercent = allEarningsPercent;
	}

	public String getRecommendFriendRecord() {
		return recommendFriendRecord;
	}

	public void setRecommendFriendRecord(String recommendFriendRecord) {
		this.recommendFriendRecord = recommendFriendRecord;
	}

	public String getCurrentInvestCapital() {
		return currentInvestCapital;
	}

	public void setCurrentInvestCapital(String currentInvestCapital) {
		this.currentInvestCapital = currentInvestCapital;
	}

	public String getInvestmentCount() {
		return investmentCount;
	}

	public void setInvestmentCount(String investmentCount) {
		this.investmentCount = investmentCount;
	}

	public String getAccumulatedIncome() {
		return accumulatedIncome;
	}

	public void setAccumulatedIncome(String accumulatedIncome) {
		this.accumulatedIncome = accumulatedIncome;
	}
}
