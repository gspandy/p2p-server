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


//贷款人基础信息和贷款业务信息
@Entity
@Table(name="t_loan_data")
public class LoanData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_loan_data_seq", unique = true, nullable = false)//流水号
	private Long loanDataSequence;
	@Column(name = "tac_gender")
	private String gender;// 性别 gender
	@Column(name = "tac_mobile", length = 11)
	private String mobile;// 手机号 p2p_account_mobile
	@Column(name = "tac_email", length = 60)
	private String email;// 邮箱 p2p_account_email
	@Column(name = "tac_name", length = 30)
	private String realName;// 姓名 p2p_account_name
	@Column(name = "tac_pid", length = 18)
	private String pid;// 身份证 p2p_account_pid
	@Column(name = "tac_inds",length=30)
	private String accountIndustry;//职业
	@Column(name = "tac_edu_degree",length=30)
	private String educationDegree;// 最高学历 p2p_account_education_degree
	@Column(name = "tac_income",length=30)
	private String income;// 月收入 p2p_account_income
	@Column(name = "tac_wprov",length=30)
	private String workProvince;// 工作所在省份 p2p_account_work_province
	@Column(name = "tac_wcity",length=30)
	private String workCity;// 工作所在地级市 p2p_account_work_city
	@Column(name = "tac_wtown",length=30)
	private String workTown;// 工作所在区县 p2p_account_work_town
	@Column(name = "tac_mstat",length=30)
	private String marryStatus;// 婚姻状况 p2p_account_marry_status
	@Column(name = "tac_hprov",length=30)
	private String hometownProvince;// 户籍所在省份 p2p_account_hometown_province
	@Column(name = "tac_hcity",length=30)
	private String hometownCity;// 户籍所在地级市 p2p_account_hometown_city
	@Column(name = "tac_htown",length=30)
	private String hometownTown;// 户籍所在区县 p2p_account_hometown_town
	@Column(name = "tac_house",length=30)
	private String houseProperty;// 房产情况 p2p_account_house_property
	@Column(name = "tac_cinds",length=30)
	private String companyInustry;// 公司行业 p2p_account_company_inustry
	@Column(name = "tac_csize",length=30)
	private String companySize;// 公司规模 p2p_account_company_size
	@Column(name = "tac_cpt",length=30)
	private String companyProperty;// 企业性质 p2p_account_company_property
	@Column(name = "tac_wtime",length=30)
	private Integer workTime;// 工作年限 p2p_account_work_time
	@Column(name = "tac_depart",length=30)
	private String department;// 任职部门 p2p_account_department
	@Column(name = "tac_position",length=30)
	private String position;// 职位 p2p_account_position
	@Column(name = "tac_sdate", length = 30)
	private String salaryDate;// 每月发薪日 p2p_account_salary_date
	@Column(name = "tac_cscore",length=30)
	private Integer credibilityScore;// 信用评分 p2p_account_credibility_score
	@Column(name = "tac_tlv",length=30)
	private String credibilityLevel;// 信用等级 p2p_account_credibility_level
	@Column(name = "tac_ltype",length=30)
	private String loanType;// 贷款人类型 p2p_account_loan_type
	@Column(name = "tac_house_loan",length=30)
	private String accountHouseLoan;//房贷情况
	@Column(name = "tac_addr", length = 300)
	private String accountAddress;//联系地址
	@Column(name = "buss_code",length=60)
	private String investmentBusinessCode;// 业务订单号
	@Column(name = "inv_src")
	private Integer investmentSource;// 项目来源
	@Column(name = "inv_stype",length=30)
	private String investmentGuaranteedInterestType;// 保额保息情况
	@Column(name = "inv_irate",length=30)
	private String investmentAnnualInterestRate;// 年利率
	@Column(name = "inv_period")
	private Integer investmentPeriod;// 期数
	@Column(name = "inv_ptype",length=30)
	private String investmentPayType;// 还款方式
	@Column(name = "inv_target",length=30)
	private String investmentTarget;//项目用途
	@Column(name = "inv_cscore")
	private Integer investmentCredibilityScore;// 项目评分
	@Column(name = "bank_code",length=30)
	private String bankCode;// 所属银行 bank_code
	@Column(name = "abc_num",length=30)
	private String bankCardNumber;// 银行卡号码 bank_card_number
	@Column(name = "abc_prov",length=30)
	private String bankProvince;// 开户行所在省份 bank_province
	@Column(name = "abc_city",length=30)
	private String bankCity;// 开户行所在地级市 bank_city
	@Column(name = "abc_town",length=30)
	private String bankTown;// 开户行所在区县 bank_town
	@Column(name = "rec_stat")
	private Integer recordStatus;//记录处理状态
	@Column(name = "rec_idate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordInDate;//记录入库时间
	@Column(name = "rec_odate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordOperateDate;//记录处理时间
	@Column(name = "tac_laddr",length=300)	//常住地址	
	private String accountLiveAddress;
	@Column(name = "tac_haddr",length=300)	//户籍地址	
	private String accountHometownAddress;
	@Column(name = "tac_caddr",length=300)	//公司地址	
	private String accountCompanyAddress;
	@Column(name = "abc_name",length=90)	//开户行名称	
	private String bankName;
	@Column(name = "act_lmount",precision = 12, scale = 2)	//贷款金额	
	private BigDecimal accountLoanAmount;
	@Column(name = "p2p_sfrate",length=10)	//服务费率	
	private String p2p_platformServiceFreeRate;
	@Column(name = "em_lv",length=20)	//紧急级别	
	private String emergencyLevel;
	@Column(name = "p2p_epay_frate",nullable = false,precision = 20, scale = 10)//提前清贷补偿金费率
	private BigDecimal p2pEarlyPayFeeRate;
	@Column(name = "p2p_pay_frate",nullable = false,precision = 20, scale = 10)//平台管理费率
	private BigDecimal p2pPayFeeRate;
	@Column(name = "p2p_oper_frate",nullable = false,precision = 20, scale = 10)//手续费率
	private BigDecimal p2pOperateFeeRate;
	@Column(name = "sign_date",nullable = false)//签约时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date signDate;
	@Column(name = "pro_stat",nullable = false)	//数据处理进度	
	private Integer progressStauts;
	@Column(name = "cont_id",nullable = false)	//合同编号	
	private String contractId;
	@Column(name = "inv_type",nullable = false)//项目发布方式 常量-参考数据字典
	private Integer investmentType;
	@Column(name = "lc_ptype",nullable = false)	//小贷公司推送类型	
	private Integer loanCompanyPushType;
	@Column(name = "prd_type",nullable = false) //产品种类
	private String productionType;
	@Column(name = "ss_stat",nullable = false)//缴金情况
	private Integer socialSecurityStatus;
	@Column(name = "sync_desc")//同步说明 用于（VBS数据成功进入p2p后，在p2p处理流程时记录具体信息异常时用）
	private Integer synchronizeDescription;	
	
	public Integer getInvestmentType() {
		return investmentType;
	}
	public void setInvestmentType(Integer investmentType) {
		this.investmentType = investmentType;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getAccountIndustry() {
		return accountIndustry;
	}
	public void setAccountIndustry(String accountIndustry) {
		this.accountIndustry = accountIndustry;
	}
	public String getEducationDegree() {
		return educationDegree;
	}
	public void setEducationDegree(String educationDegree) {
		this.educationDegree = educationDegree;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getWorkProvince() {
		return workProvince;
	}
	public void setWorkProvince(String workProvince) {
		this.workProvince = workProvince;
	}
	public String getWorkCity() {
		return workCity;
	}
	public void setWorkCity(String workCity) {
		this.workCity = workCity;
	}
	public String getWorkTown() {
		return workTown;
	}
	public void setWorkTown(String workTown) {
		this.workTown = workTown;
	}
	public String getMarryStatus() {
		return marryStatus;
	}
	public void setMarryStatus(String marryStatus) {
		this.marryStatus = marryStatus;
	}
	public String getHometownProvince() {
		return hometownProvince;
	}
	public void setHometownProvince(String hometownProvince) {
		this.hometownProvince = hometownProvince;
	}
	public String getHometownCity() {
		return hometownCity;
	}
	public void setHometownCity(String hometownCity) {
		this.hometownCity = hometownCity;
	}
	public String getHometownTown() {
		return hometownTown;
	}
	public void setHometownTown(String hometownTown) {
		this.hometownTown = hometownTown;
	}
	public String getHouseProperty() {
		return houseProperty;
	}
	public void setHouseProperty(String houseProperty) {
		this.houseProperty = houseProperty;
	}
	public String getCompanyInustry() {
		return companyInustry;
	}
	public void setCompanyInustry(String companyInustry) {
		this.companyInustry = companyInustry;
	}
	public String getCompanySize() {
		return companySize;
	}
	public void setCompanySize(String companySize) {
		this.companySize = companySize;
	}
	public String getCompanyProperty() {
		return companyProperty;
	}
	public void setCompanyProperty(String companyProperty) {
		this.companyProperty = companyProperty;
	}
	public Integer getWorkTime() {
		return workTime;
	}
	public void setWorkTime(Integer workTime) {
		this.workTime = workTime;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getSalaryDate() {
		return salaryDate;
	}
	public void setSalaryDate(String salaryDate) {
		this.salaryDate = salaryDate;
	}
	public Integer getCredibilityScore() {
		return credibilityScore;
	}
	public void setCredibilityScore(Integer credibilityScore) {
		this.credibilityScore = credibilityScore;
	}
	public String getCredibilityLevel() {
		return credibilityLevel;
	}
	public void setCredibilityLevel(String credibilityLevel) {
		this.credibilityLevel = credibilityLevel;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getAccountHouseLoan() {
		return accountHouseLoan;
	}
	public void setAccountHouseLoan(String accountHouseLoan) {
		this.accountHouseLoan = accountHouseLoan;
	}
	public String getAccountAddress() {
		return accountAddress;
	}
	public void setAccountAddress(String accountAddress) {
		this.accountAddress = accountAddress;
	}
	public String getInvestmentBusinessCode() {
		return investmentBusinessCode;
	}
	public void setInvestmentBusinessCode(String investmentBusinessCode) {
		this.investmentBusinessCode = investmentBusinessCode;
	}
	public String getInvestmentGuaranteedInterestType() {
		return investmentGuaranteedInterestType;
	}
	public void setInvestmentGuaranteedInterestType(
			String investmentGuaranteedInterestType) {
		this.investmentGuaranteedInterestType = investmentGuaranteedInterestType;
	}
	public String getInvestmentAnnualInterestRate() {
		return investmentAnnualInterestRate;
	}
	public void setInvestmentAnnualInterestRate(String investmentAnnualInterestRate) {
		this.investmentAnnualInterestRate = investmentAnnualInterestRate;
	}
	public Integer getInvestmentPeriod() {
		return investmentPeriod;
	}
	public void setInvestmentPeriod(Integer investmentPeriod) {
		this.investmentPeriod = investmentPeriod;
	}
	public String getInvestmentPayType() {
		return investmentPayType;
	}
	public void setInvestmentPayType(String investmentPayType) {
		this.investmentPayType = investmentPayType;
	}
	public String getInvestmentTarget() {
		return investmentTarget;
	}
	public void setInvestmentTarget(String investmentTarget) {
		this.investmentTarget = investmentTarget;
	}
	public Integer getInvestmentCredibilityScore() {
		return investmentCredibilityScore;
	}
	public void setInvestmentCredibilityScore(Integer investmentCredibilityScore) {
		this.investmentCredibilityScore = investmentCredibilityScore;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankCardNumber() {
		return bankCardNumber;
	}
	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}
	public String getBankProvince() {
		return bankProvince;
	}
	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}
	public String getBankCity() {
		return bankCity;
	}
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}
	public String getBankTown() {
		return bankTown;
	}
	public void setBankTown(String bankTown) {
		this.bankTown = bankTown;
	}
	public Integer getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	public Date getRecordInDate() {
		return recordInDate;
	}
	public void setRecordInDate(Date recordInDate) {
		this.recordInDate = recordInDate;
	}
	public Date getRecordOperateDate() {
		return recordOperateDate;
	}
	public void setRecordOperateDate(Date recordOperateDate) {
		this.recordOperateDate = recordOperateDate;
	}
	public Long getLoanDataSequence() {
		return loanDataSequence;
	}
	public void setLoanDataSequence(Long loanDataSequence) {
		this.loanDataSequence = loanDataSequence;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAccountLiveAddress() {
		return accountLiveAddress;
	}
	public void setAccountLiveAddress(String accountLiveAddress) {
		this.accountLiveAddress = accountLiveAddress;
	}
	public String getAccountHometownAddress() {
		return accountHometownAddress;
	}
	public void setAccountHometownAddress(String accountHometownAddress) {
		this.accountHometownAddress = accountHometownAddress;
	}
	public String getAccountCompanyAddress() {
		return accountCompanyAddress;
	}
	public void setAccountCompanyAddress(String accountCompanyAddress) {
		this.accountCompanyAddress = accountCompanyAddress;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public BigDecimal getAccountLoanAmount() {
		return accountLoanAmount;
	}
	public void setAccountLoanAmount(BigDecimal accountLoanAmount) {
		this.accountLoanAmount = accountLoanAmount;
	}
	public String getP2p_platformServiceFreeRate() {
		return p2p_platformServiceFreeRate;
	}
	public void setP2p_platformServiceFreeRate(String p2p_platformServiceFreeRate) {
		this.p2p_platformServiceFreeRate = p2p_platformServiceFreeRate;
	}
	public String getEmergencyLevel() {
		return emergencyLevel;
	}
	public void setEmergencyLevel(String emergencyLevel) {
		this.emergencyLevel = emergencyLevel;
	}
	public BigDecimal getP2pEarlyPayFeeRate() {
		return p2pEarlyPayFeeRate;
	}
	public void setP2pEarlyPayFeeRate(BigDecimal p2pEarlyPayFeeRate) {
		this.p2pEarlyPayFeeRate = p2pEarlyPayFeeRate;
	}
	public BigDecimal getP2pPayFeeRate() {
		return p2pPayFeeRate;
	}
	public void setP2pPayFeeRate(BigDecimal p2pPayFeeRate) {
		this.p2pPayFeeRate = p2pPayFeeRate;
	}
	public BigDecimal getP2pOperateFeeRate() {
		return p2pOperateFeeRate;
	}
	public void setP2pOperateFeeRate(BigDecimal p2pOperateFeeRate) {
		this.p2pOperateFeeRate = p2pOperateFeeRate;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public Integer getProgressStauts() {
		return progressStauts;
	}
	public void setProgressStauts(Integer progressStauts) {
		this.progressStauts = progressStauts;
	}
	public Integer getInvestmentSource() {
		return investmentSource;
	}
	public void setInvestmentSource(Integer investmentSource) {
		this.investmentSource = investmentSource;
	}
	public Integer getLoanCompanyPushType() {
		return loanCompanyPushType;
	}
	public void setLoanCompanyPushType(Integer loanCompanyPushType) {
		this.loanCompanyPushType = loanCompanyPushType;
	}
	public String getProductionType() {
		return productionType;
	}
	public void setProductionType(String productionType) {
		this.productionType = productionType;
	}
	public Integer getSocialSecurityStatus() {
		return socialSecurityStatus;
	}
	public void setSocialSecurityStatus(Integer socialSecurityStatus) {
		this.socialSecurityStatus = socialSecurityStatus;
	}
	public Integer getSynchronizeDescription() {
		return synchronizeDescription;
	}
	public void setSynchronizeDescription(Integer synchronizeDescription) {
		this.synchronizeDescription = synchronizeDescription;
	}
	
}
