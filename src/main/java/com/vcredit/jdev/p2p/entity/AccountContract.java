package com.vcredit.jdev.p2p.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



//用户借款合同
@Entity
@Table(name="t_act_cont")
public class AccountContract {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_cont_seq", unique = true, nullable = false)
	private Long accountContractSequence;// 流水号
	@Column(name = "cont_num", nullable = false,length=90)	//合同编号	
	private String contractNumber;
	@Column(name = "cont_cdate", nullable = false)	//放款日期	
	@Temporal(TemporalType.TIMESTAMP)
	private Date contractCreateDate;
	@Column(name = "inv_tac_uname", nullable = false,length=30)	//投资人用户名	
	private String investerAccountUserName;
	@Column(name = "inv_tac_name", nullable = false,length=30) //投资人真实姓名	
	private String investerAccountName;
	@Column(name = "inv_tac_pid", nullable = false,length=18)//	投资人身份证号码	
	private String investerAccountPid	;
	@Column(name = "inv_amount", nullable = false, precision = 20, scale = 10)	//	投资金额	
	private BigDecimal investmentAmount;
	@Column(name = "inv_period", nullable = false)	//	借款期限	
	private Integer investmentPeriod;
	@Column(name = "gmon_amount", nullable = false, precision = 20, scale = 10)	//	每月收款金额	
	private BigDecimal gatherMonthAmount;
	@Column(name = "pmon_amount", nullable = false, precision = 20, scale = 10)	//	每月还款金额	
	private BigDecimal payMonthAmount	;
	@Column(name = "jin_rate", nullable = false, precision = 20, scale = 10)	//	罚息率	
	private BigDecimal justicInterestRate;
	@Column(name = "bow_tac_name", nullable = false,length=30) //	借款人真实姓名	
	private String borrowerAccountName;
	@Column(name = "bow_tac_pid", nullable = false,length=18) //	借款人身份证号码	
	private String borrowerAccountPid;
	@Column(name = "bow_tac_uname", nullable = false,length=30) //	借款人平台用户名	
	private String borrowerAccountUserNname;
	@Column(name = "bow_tac_email", nullable = false,length=60) //	借款人邮箱地址	
	private String borrowerAccountEmail;
	@Column(name = "loan_target", nullable = false,length=30) //	贷款用途	
	private String loanTarget;
	@Column(name = "loan_amount", nullable = false, precision = 20, scale = 10)	//	贷款金额	
	private BigDecimal loanAmount;
	@Column(name = "inv_irate", nullable = false, precision = 20, scale = 10)	//	预期年利率	
	private BigDecimal investmentAnnualInterestRate;
	@Column(name = "pm_pay_frate", nullable = false, precision = 20, scale = 10)	//		平台管理费率	
	private BigDecimal p2pPayFeeRate;
	@Column(name = "pay_day", nullable = false,length=16) //	还款日	
	private String payDay;
	@Column(name = "pm_inv_frate", nullable = false, precision = 20, scale = 10)	//			投资管理费率	
	private BigDecimal investmentProjectManagementFeeRate;
	@Column(name = "ear_cln_con", nullable = false,length=255) //		提前还款条件	
	private String earilierCleanCondition;
	@Column(name = "t_pacct_seq", nullable = false)	//	付费用户P2P平台账号流水号	
	private Long payAccountSequence;
	@Column(name = "t_gacct_seq", nullable = false)	//	收费用户P2P平台账号流水号	
	private Long gatherAccountSequence;
	@Column(name = "t_inv_seq", nullable = false)
	private Long investmentSequence;// 投资项目流水号
	@Column(name = "clm_pp_pmfee",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayPlanPlatformManagementFee;// 平台账号管理服务费 费率-参考数据字典
	
	
	public Long getAccountContractSequence() {
		return accountContractSequence;
	}
	public void setAccountContractSequence(Long accountContractSequence) {
		this.accountContractSequence = accountContractSequence;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public Date getContractCreateDate() {
		return contractCreateDate;
	}
	public void setContractCreateDate(Date contractCreateDate) {
		this.contractCreateDate = contractCreateDate;
	}
	public String getInvesterAccountUserName() {
		return investerAccountUserName;
	}
	public void setInvesterAccountUserName(String investerAccountUserName) {
		this.investerAccountUserName = investerAccountUserName;
	}
	public String getInvesterAccountName() {
		return investerAccountName;
	}
	public void setInvesterAccountName(String investerAccountName) {
		this.investerAccountName = investerAccountName;
	}
	public String getInvesterAccountPid() {
		return investerAccountPid;
	}
	public void setInvesterAccountPid(String investerAccountPid) {
		this.investerAccountPid = investerAccountPid;
	}
	public BigDecimal getInvestmentAmount() {
		return investmentAmount;
	}
	public void setInvestmentAmount(BigDecimal investmentAmount) {
		this.investmentAmount = investmentAmount;
	}
	public Integer getInvestmentPeriod() {
		return investmentPeriod;
	}
	public void setInvestmentPeriod(Integer investmentPeriod) {
		this.investmentPeriod = investmentPeriod;
	}
	public BigDecimal getGatherMonthAmount() {
		return gatherMonthAmount;
	}
	public void setGatherMonthAmount(BigDecimal gatherMonthAmount) {
		this.gatherMonthAmount = gatherMonthAmount;
	}
	public BigDecimal getPayMonthAmount() {
		return payMonthAmount;
	}
	public void setPayMonthAmount(BigDecimal payMonthAmount) {
		this.payMonthAmount = payMonthAmount;
	}
	public BigDecimal getJusticInterestRate() {
		return justicInterestRate;
	}
	public void setJusticInterestRate(BigDecimal justicInterestRate) {
		this.justicInterestRate = justicInterestRate;
	}
	public String getBorrowerAccountName() {
		return borrowerAccountName;
	}
	public void setBorrowerAccountName(String borrowerAccountName) {
		this.borrowerAccountName = borrowerAccountName;
	}
	public String getBorrowerAccountPid() {
		return borrowerAccountPid;
	}
	public void setBorrowerAccountPid(String borrowerAccountPid) {
		this.borrowerAccountPid = borrowerAccountPid;
	}
	public String getBorrowerAccountUserNname() {
		return borrowerAccountUserNname;
	}
	public void setBorrowerAccountUserNname(String borrowerAccountUserNname) {
		this.borrowerAccountUserNname = borrowerAccountUserNname;
	}
	public String getBorrowerAccountEmail() {
		return borrowerAccountEmail;
	}
	public void setBorrowerAccountEmail(String borrowerAccountEmail) {
		this.borrowerAccountEmail = borrowerAccountEmail;
	}
	public String getLoanTarget() {
		return loanTarget;
	}
	public void setLoanTarget(String loanTarget) {
		this.loanTarget = loanTarget;
	}
	public BigDecimal getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}
	public BigDecimal getInvestmentAnnualInterestRate() {
		return investmentAnnualInterestRate;
	}
	public void setInvestmentAnnualInterestRate(
			BigDecimal investmentAnnualInterestRate) {
		this.investmentAnnualInterestRate = investmentAnnualInterestRate;
	}
	public BigDecimal getP2pPayFeeRate() {
		return p2pPayFeeRate;
	}
	public void setP2pPayFeeRate(BigDecimal p2pPayFeeRate) {
		this.p2pPayFeeRate = p2pPayFeeRate;
	}
	public String getPayDay() {
		return payDay;
	}
	public void setPayDay(String payDay) {
		this.payDay = payDay;
	}
	public BigDecimal getInvestmentProjectManagementFeeRate() {
		return investmentProjectManagementFeeRate;
	}
	public void setInvestmentProjectManagementFeeRate(
			BigDecimal investmentProjectManagementFeeRate) {
		this.investmentProjectManagementFeeRate = investmentProjectManagementFeeRate;
	}
	public String getEarilierCleanCondition() {
		return earilierCleanCondition;
	}
	public void setEarilierCleanCondition(String earilierCleanCondition) {
		this.earilierCleanCondition = earilierCleanCondition;
	}
	public Long getPayAccountSequence() {
		return payAccountSequence;
	}
	public void setPayAccountSequence(Long payAccountSequence) {
		this.payAccountSequence = payAccountSequence;
	}
	public Long getGatherAccountSequence() {
		return gatherAccountSequence;
	}
	public void setGatherAccountSequence(Long gatherAccountSequence) {
		this.gatherAccountSequence = gatherAccountSequence;
	}
	public Long getInvestmentSequence() {
		return investmentSequence;
	}
	public void setInvestmentSequence(Long investmentSequence) {
		this.investmentSequence = investmentSequence;
	}
	public BigDecimal getClaimPayPlanPlatformManagementFee() {
		return claimPayPlanPlatformManagementFee;
	}
	public void setClaimPayPlanPlatformManagementFee(
			BigDecimal claimPayPlanPlatformManagementFee) {
		this.claimPayPlanPlatformManagementFee = claimPayPlanPlatformManagementFee;
	}
	
}
