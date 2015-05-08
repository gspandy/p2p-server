package com.vcredit.jdev.p2p.pdf.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.vcredit.jdev.p2p.entity.Investment;

/**
 * 借款协议-合同数据
 * @author zhaopeijun
 *
 */
public class PdfInfos {
	
	/** 放款后传送的标的对象 */
	private Investment investment;
	
	/** 合同编号 */
	private String contractNo;
	/** 放款日期-yyyy-MM-dd */
	private String investReleaseYmd;
	/** 投资人列表 */
	private List<Lender> lenderList;
	/** 授权协议投资人列表（同一个投资人只显示一次） */
	private List<Lender> accreditLenderList;
	/** 借款人真实姓名 */
	private String borrowerRealName;
	/** 借款人身份证ID */
	private String borrowerPid;
	/** 借款人平台用户名 */
	private String borrowerUserName;
	/** 借款人邮箱地址 */
	private String borrowerEmail;
	/** 贷款用途 */
	private String loanTarget;
	/** 贷款金额 */
	private String loanAmount;
	/** 预期年利率 */
	private BigDecimal investmentAnnualInterestRate;
	/** 贷款期限 */
	private Integer investmentPeriod;
	/** 月还款金额 */     
	private BigDecimal montyPayAmount;
	/** 平台管理费率 */
	private BigDecimal p2pPayFeeRate;
	/** 平台管理费 */
	private BigDecimal p2pPayFee;
	/** 还款日 */
	private String payDay;
	/** 投资管理费率 */
	private BigDecimal investmentManageFeeRate;
	/** 罚息率 */
	private BigDecimal justicInterestRate;
	/** 可提前还款月 */
	private Integer earlierCleanMonth;
	/** 借款人是否为空 */
	private String ifborrower;
	
	//convert用
	/** 放款日期-yyyy */
	private String investReleaseYear;
	
	/** 放款日期-MM */
	private String investReleaseMonth;
	
	/** 放款日期-dd */
	private String investReleaseDay;
	
	public Investment getInvestment() {
		return investment;
	}
	public void setInvestment(Investment investment) {
		this.investment = investment;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getInvestReleaseYmd() {
		return investReleaseYmd;
	}
	public void setInvestReleaseYmd(String investReleaseYmd) {
		this.investReleaseYmd = investReleaseYmd;
	}
	
	public List<Lender> getLenderList() {
		return lenderList;
	}
	public void setLenderList(List<Lender> lenderList) {
		this.lenderList = lenderList;
	}
	public String getBorrowerRealName() {
		return borrowerRealName;
	}
	public void setBorrowerRealName(String borrowerRealName) {
		this.borrowerRealName = borrowerRealName;
	}
	public String getBorrowerPid() {
		return borrowerPid;
	}
	public void setBorrowerPid(String borrowerPid) {
		this.borrowerPid = borrowerPid;
	}
	public String getBorrowerUserName() {
		return borrowerUserName;
	}
	public void setBorrowerUserName(String borrowerUserName) {
		this.borrowerUserName = borrowerUserName;
	}
	public String getBorrowerEmail() {
		return borrowerEmail;
	}
	public void setBorrowerEmail(String borrowerEmail) {
		this.borrowerEmail = borrowerEmail;
	}
	public String getLoanTarget() {
		return loanTarget;
	}
	public void setLoanTarget(String loanTarget) {
		this.loanTarget = loanTarget;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	public BigDecimal getInvestmentAnnualInterestRate() {
		return investmentAnnualInterestRate;
	}
	public void setInvestmentAnnualInterestRate(BigDecimal investmentAnnualInterestRate) {
		this.investmentAnnualInterestRate = investmentAnnualInterestRate;
	}
	public Integer getInvestmentPeriod() {
		return investmentPeriod;
	}
	public void setInvestmentPeriod(Integer investmentPeriod) {
		this.investmentPeriod = investmentPeriod;
	}
	public BigDecimal getMontyPayAmount() {
		return montyPayAmount;
	}
	public void setMontyPayAmount(BigDecimal montyPayAmount) {
		this.montyPayAmount = montyPayAmount;
	}
	public BigDecimal getP2pPayFeeRate() {
		return p2pPayFeeRate;
	}
	public void setP2pPayFeeRate(BigDecimal p2pPayFeeRate) {
		this.p2pPayFeeRate = p2pPayFeeRate;
	}
	public BigDecimal getP2pPayFee() {
		return p2pPayFee;
	}
	public void setP2pPayFee(BigDecimal p2pPayFee) {
		this.p2pPayFee = p2pPayFee;
	}
	public String getPayDay() {
		return payDay;
	}
	public void setPayDay(String payDay) {
		this.payDay = payDay;
	}
	public BigDecimal getInvestmentManageFeeRate() {
		return investmentManageFeeRate;
	}
	public void setInvestmentManageFeeRate(BigDecimal investmentManageFeeRate) {
		this.investmentManageFeeRate = investmentManageFeeRate;
	}
	public BigDecimal getJusticInterestRate() {
		return justicInterestRate;
	}
	public void setJusticInterestRate(BigDecimal justicInterestRate) {
		this.justicInterestRate = justicInterestRate;
	}
	public Integer getEarlierCleanMonth() {
		return earlierCleanMonth;
	}
	public void setEarlierCleanMonth(Integer earlierCleanMonth) {
		this.earlierCleanMonth = earlierCleanMonth;
	}
	public String getInvestReleaseYear() {
		return investReleaseYear;
	}
	public void setInvestReleaseYear(String investReleaseYear) {
		this.investReleaseYear = investReleaseYear;
	}
	public String getInvestReleaseMonth() {
		return investReleaseMonth;
	}
	public void setInvestReleaseMonth(String investReleaseMonth) {
		this.investReleaseMonth = investReleaseMonth;
	}
	public String getInvestReleaseDay() {
		return investReleaseDay;
	}
	public void setInvestReleaseDay(String investReleaseDay) {
		this.investReleaseDay = investReleaseDay;
	}
	public List<Lender> getAccreditLenderList() {
		return accreditLenderList;
	}
	public void setAccreditLenderList(List<Lender> accreditLenderList) {
		this.accreditLenderList = accreditLenderList;
	}
	public String getIfborrower() {
		return ifborrower;
	}
	public void setIfborrower(String ifborrower) {
		this.ifborrower = ifborrower;
	}
}
