package com.vcredit.jdev.p2p.bizsync.convert;

/**
 * 
 * @author zhaopeijun
 *
 */
public class BorrowInfo {

	private String investmentSource;
	private String investmentBusinessCode = "";
	private String productionType =""; //产品种类
	private String investmentTarget = "";
	private String investmentPeriod = "";
	private String accountLoanAmount = "";
	private String contractNumber = "";
	private String credibilityLevel = "";
	private Integer credibilityScore; //客户评分
	private String investmentAnnualInterestRate = "";
	private String p2pEarlyPayFeeRate = "";
	private String p2pPayFeeRate = "";
	private String p2pOperateFeeRate = "";
	private String signDate = "";
	
	public String getInvestmentSource() {
		return investmentSource;
	}
	public void setInvestmentSource(String investmentSource) {
		this.investmentSource = investmentSource;
	}
	public String getInvestmentBusinessCode() {
		return investmentBusinessCode;
	}
	public void setInvestmentBusinessCode(String investmentBusinessCode) {
		this.investmentBusinessCode = investmentBusinessCode;
	}
	public String getProductionType() {
		return productionType;
	}
	public void setProductionType(String productionType) {
		this.productionType = productionType;
	}
	public String getInvestmentTarget() {
		return investmentTarget;
	}
	public void setInvestmentTarget(String investmentTarget) {
		this.investmentTarget = investmentTarget;
	}
	public String getInvestmentPeriod() {
		return investmentPeriod;
	}
	public void setInvestmentPeriod(String investmentPeriod) {
		this.investmentPeriod = investmentPeriod;
	}
	public String getAccountLoanAmount() {
		return accountLoanAmount;
	}
	public void setAccountLoanAmount(String accountLoanAmount) {
		this.accountLoanAmount = accountLoanAmount;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getCredibilityLevel() {
		return credibilityLevel;
	}
	public void setCredibilityLevel(String credibilityLevel) {
		this.credibilityLevel = credibilityLevel;
	}
	public Integer getCredibilityScore() {
		return credibilityScore;
	}
	public void setCredibilityScore(Integer credibilityScore) {
		this.credibilityScore = credibilityScore;
	}
	public String getInvestmentAnnualInterestRate() {
		return investmentAnnualInterestRate;
	}
	public void setInvestmentAnnualInterestRate(String investmentAnnualInterestRate) {
		this.investmentAnnualInterestRate = investmentAnnualInterestRate;
	}
	public String getP2pEarlyPayFeeRate() {
		return p2pEarlyPayFeeRate;
	}
	public void setP2pEarlyPayFeeRate(String p2pEarlyPayFeeRate) {
		this.p2pEarlyPayFeeRate = p2pEarlyPayFeeRate;
	}
	public String getP2pPayFeeRate() {
		return p2pPayFeeRate;
	}
	public void setP2pPayFeeRate(String p2pPayFeeRate) {
		this.p2pPayFeeRate = p2pPayFeeRate;
	}
	public String getP2pOperateFeeRate() {
		return p2pOperateFeeRate;
	}
	public void setP2pOperateFeeRate(String p2pOperateFeeRate) {
		this.p2pOperateFeeRate = p2pOperateFeeRate;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

}
