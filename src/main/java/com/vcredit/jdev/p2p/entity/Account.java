package com.vcredit.jdev.p2p.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vcredit.jdev.p2p.dto.AutoInvestDto;

//用户P2P平台账号
@SqlResultSetMappings({ @SqlResultSetMapping(name = "AutoInvestDtoMapping", classes = { @ConstructorResult(targetClass = AutoInvestDto.class, columns = {
		@ColumnResult(name = "airPmax"), @ColumnResult(name = "airPeriod"), @ColumnResult(name = "airClv"), @ColumnResult(name = "airStat"),
		@ColumnResult(name = "airType"), @ColumnResult(name = "tAcctSeq"), @ColumnResult(name = "amt", type = Double.class),
		@ColumnResult(name = "tacRdate", type = Date.class), @ColumnResult(name = "gptotal") }) }) })
@Entity
@Table(name = "t_acct")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_acct_seq", unique = true, nullable = false)
	private Long accountSequence;// 流水号 account_sequence
	@Column(name = "tac_stat", nullable = false)
	private Integer accountStatus;// 账号状态 accounts_status 常量-参考数据字典
	@Column(name = "tac_gender")
	private Integer gender;// 性别 gender 常量-参考数据字典
	@Column(name = "tac_uname", length = 30, nullable = false)
	private String userName;// 用户名 userName
	@Column(name = "tac_mobile", length = 11, nullable = false)
	private String mobile;// 手机号 p2p_account_mobile
	@Column(name = "tac_mbstat")
	private Integer mobileBindStatus;// 是否已绑定手机号 p2p_account_mobile_bind_status 常量-参考数据字典
	@Column(name = "tac_email", length = 60)
	private String email;// 邮箱 p2p_account_email
	@Column(name = "tac_emstat")
	private Integer emailBindStatus;// 是否已绑定邮箱 p2p_account_email_bind_status 常量-参考数据字典
	@Column(name = "tac_name", length = 30)
	private String realName;// 姓名 p2p_account_name
	@Column(name = "tac_slv")
	private Integer safety;// 账户安全等级 p2p_account_safety 常量-参考数据字典
	@Column(name = "tac_pid", length = 18)
	private String pid;// 身份证 p2p_account_pid
	@Column(name = "tac_inds", length = 60)
	private String accountIndustry;//职业 
	@Column(name = "tac_edu_degree")
	private Integer educationDegree;// 最高学历 p2p_account_education_degree 常量-参考数据字典
	@Column(name = "tac_income")
	private Integer income;// 月收入 p2p_account_income 常量-参考数据字典
	@Column(name = "tac_wprov")
	private Integer workProvince;// 所在省份 p2p_account_work_province 邮编-参考区域表
	@Column(name = "tac_wcity")
	private Integer workCity;// 所在地级市 p2p_account_work_city 邮编-参考区域表
	@Column(name = "tac_wtown")
	private Integer workTown;// 所在区县 p2p_account_work_town 邮编-参考区域表
	@Column(name = "tac_rdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date registerDate;// 注册时间 p2p_account_register_date
	@Column(name = "tac_rip", length = 20, nullable = false)
	private String registerIp;// 注册IP p2p_account_register_ip
	@Column(name = "tac_rip_prove")
	//注册IP所在省 邮编-参考区域表
	private Integer accountRegisterIpProvice;
	@Column(name = "tac_rip_city")
	//注册IP所在地级市 邮编-参考区域表
	private Integer accountRegisterIpCity;
	@Column(name = "tac_rip_town")
	//注册IP所在区县 邮编-参考区域表
	private Integer accountRegisterIpTown;
	@Column(name = "tac_mstat")
	private Integer marryStatus;// 婚姻状况 p2p_account_marry_status 常量-参考数据字典
	@Column(name = "tac_hprov")
	private Integer hometownProvince;// 户籍所在省份 p2p_account_hometown_province 邮编-参考区域表
	@Column(name = "tac_hcity")
	private Integer hometownCity;// 户籍所在地级市 p2p_account_hometown_city 邮编-参考区域表
	@Column(name = "tac_htown")
	private Integer hometownTown;// 户籍所在区县 p2p_account_hometown_town 邮编-参考区域表
	@Column(name = "tac_house")
	private Integer houseProperty;// 房产情况 p2p_account_house_property 常量-参考数据字典
	@Column(name = "tac_car")
	private Integer carProperty;// 车产情况 p2p_account_car_property 常量-参考数据字典
	@Column(name = "tac_cinds")
	private Integer companyInustry;// 公司行业 p2p_account_company_inustry 常量-参考数据字典
	@Column(name = "tac_csize")
	private Integer companySize;// 公司规模 p2p_account_company_size 常量-参考数据字典
	@Column(name = "tac_cpt")
	private Integer companyProperty;// 企业性质 p2p_account_company_property 常量-参考数据字典
	@Column(name = "tac_wtime")
	private Integer workTime;// 工作年限 p2p_account_work_time
	@Column(name = "tac_depart", length = 60)
	private String department;// 任职部门 p2p_account_department
	@Column(name = "tac_position", length = 60)
	private String position;// 职位 p2p_account_position
	@Column(name = "tac_sdate", length = 20)
	private String salaryDate;// 每月发薪日 p2p_account_salary_date
	@Column(name = "tac_cscore")
	private Integer credibilityScore;// 信用评分 p2p_account_credibility_score
	@Column(name = "tac_tlv")
	private Integer credibilityLevel;// 信用等级 p2p_account_credibility_level 常量-参考数据字典
	@Column(name = "t_res_seq")
	private Integer credibilityImage;// 信用引用图片 p2p_account_credibility_image
	@Column(name = "tac_idcs")
	private Integer pidCredibilityStatus;// 身份证明状态p2p_account_pid_credibility_status 常量-参考数据字典
	@Column(name = "tac_iccs")
	private Integer incomeCredibilityStatus;// 收入证明状态p2p_account_income_credibility_status 常量-参考数据字典
	@Column(name = "tac_jcs")
	private Integer jobCredibilityStatus;// 工作证明状态p2p_account_job_credibility_status 常量-参考数据字典
	@Column(name = "tac_addcs")
	private Integer addressCredibilityStatus;// 住址证明状态p2p_account_address_credibility_status 常量-参考数据字典
	@Column(name = "tac_idcs_pdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pidCredibilityPassDate;// 身份证明审核通过时间p2p_account_pid_credibility_pass_date
	@Column(name = "tac_iccs_pdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date incomeCredibilityPassDate;// 收入证明审核通过时间p2p_account_income_credibility_pass_date
	@Column(name = "tac_jts_pdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date jobCredibilityPassDate;// 工作证明审核通过时间p2p_account_job_credibility_pass_date
	@Column(name = "tac_addjs_pdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date addressCredibilityPassDate;// 住址证明审核通过时间p2p_account_address_credibility_pass_date
	@Column(name = "tac_ltype")
	private Integer loanType;// 贷款人类型 p2p_account_loan_type 常量-参考数据字典
	@Column(name = "tac_house_loan")
	private Integer accountHouseLoan;//房贷情况 常量-参考数据字典
	@Column(name = "tac_addr", length = 300)
	private String accountAddress;//联系地址
	@Column(name = "tac_birth", length = 8)
	//出生年月日	
	private String accountBirthday;
	@Column(name = "act_third", length = 60)
	//第三方支付账号	
	private String accountThirdPaymentId;
	@Column(name = "act_thbstat")
	//账号绑定状态	
	private Integer accountThirdPaymentIdBindStatus;
	@Column(name = "tac_tcity")
	//联系地址所在城市	
	private Integer accountTouchCity;
	@Column(name = "tac_tprov")
	//联系地址所在省份	
	private Integer accountTouchProvince;
	@Column(name = "tac_role", nullable = false)
	//用户角色	
	private Integer accountRole;

	public String getAccountBirthday() {
		return accountBirthday;
	}

	public void setAccountBirthday(String accountBirthday) {
		this.accountBirthday = accountBirthday;
	}

	public String getAccountAddress() {
		return accountAddress;
	}

	public void setAccountAddress(String accountAddress) {
		this.accountAddress = accountAddress;
	}

	public Long getAccountSequence() {
		return accountSequence;
	}

	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getMobileBindStatus() {
		return mobileBindStatus;
	}

	public void setMobileBindStatus(Integer mobileBindStatus) {
		this.mobileBindStatus = mobileBindStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEmailBindStatus() {
		return emailBindStatus;
	}

	public void setEmailBindStatus(Integer emailBindStatus) {
		this.emailBindStatus = emailBindStatus;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getSafety() {
		return safety;
	}

	public void setSafety(Integer safety) {
		this.safety = safety;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getEducationDegree() {
		return educationDegree;
	}

	public void setEducationDegree(Integer educationDegree) {
		this.educationDegree = educationDegree;
	}

	public Integer getIncome() {
		return income;
	}

	public void setIncome(Integer income) {
		this.income = income;
	}

	public Integer getWorkProvince() {
		return workProvince;
	}

	public void setWorkProvince(Integer workProvince) {
		this.workProvince = workProvince;
	}

	public Integer getWorkCity() {
		return workCity;
	}

	public void setWorkCity(Integer workCity) {
		this.workCity = workCity;
	}

	public Integer getWorkTown() {
		return workTown;
	}

	public void setWorkTown(Integer workTown) {
		this.workTown = workTown;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public Integer getMarryStatus() {
		return marryStatus;
	}

	public void setMarryStatus(Integer marryStatus) {
		this.marryStatus = marryStatus;
	}

	public Integer getHometownProvince() {
		return hometownProvince;
	}

	public void setHometownProvince(Integer hometownProvince) {
		this.hometownProvince = hometownProvince;
	}

	public Integer getHometownCity() {
		return hometownCity;
	}

	public void setHometownCity(Integer hometownCity) {
		this.hometownCity = hometownCity;
	}

	public Integer getHometownTown() {
		return hometownTown;
	}

	public void setHometownTown(Integer hometownTown) {
		this.hometownTown = hometownTown;
	}

	public Integer getHouseProperty() {
		return houseProperty;
	}

	public void setHouseProperty(Integer houseProperty) {
		this.houseProperty = houseProperty;
	}

	public Integer getCarProperty() {
		return carProperty;
	}

	public void setCarProperty(Integer carProperty) {
		this.carProperty = carProperty;
	}

	public Integer getCompanyInustry() {
		return companyInustry;
	}

	public void setCompanyInustry(Integer companyInustry) {
		this.companyInustry = companyInustry;
	}

	public Integer getCompanySize() {
		return companySize;
	}

	public void setCompanySize(Integer companySize) {
		this.companySize = companySize;
	}

	public Integer getCompanyProperty() {
		return companyProperty;
	}

	public void setCompanyProperty(Integer companyProperty) {
		this.companyProperty = companyProperty;
	}

	public Integer getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Integer workTime) {
		this.workTime = workTime;
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

	public Integer getCredibilityLevel() {
		return credibilityLevel;
	}

	public void setCredibilityLevel(Integer credibilityLevel) {
		this.credibilityLevel = credibilityLevel;
	}

	public Integer getCredibilityImage() {
		return credibilityImage;
	}

	public void setCredibilityImage(Integer credibilityImage) {
		this.credibilityImage = credibilityImage;
	}

	public Integer getPidCredibilityStatus() {
		return pidCredibilityStatus;
	}

	public void setPidCredibilityStatus(Integer pidCredibilityStatus) {
		this.pidCredibilityStatus = pidCredibilityStatus;
	}

	public Integer getIncomeCredibilityStatus() {
		return incomeCredibilityStatus;
	}

	public void setIncomeCredibilityStatus(Integer incomeCredibilityStatus) {
		this.incomeCredibilityStatus = incomeCredibilityStatus;
	}

	public Integer getJobCredibilityStatus() {
		return jobCredibilityStatus;
	}

	public void setJobCredibilityStatus(Integer jobCredibilityStatus) {
		this.jobCredibilityStatus = jobCredibilityStatus;
	}

	public Integer getAddressCredibilityStatus() {
		return addressCredibilityStatus;
	}

	public void setAddressCredibilityStatus(Integer addressCredibilityStatus) {
		this.addressCredibilityStatus = addressCredibilityStatus;
	}

	public Date getPidCredibilityPassDate() {
		return pidCredibilityPassDate;
	}

	public void setPidCredibilityPassDate(Date pidCredibilityPassDate) {
		this.pidCredibilityPassDate = pidCredibilityPassDate;
	}

	public Date getIncomeCredibilityPassDate() {
		return incomeCredibilityPassDate;
	}

	public void setIncomeCredibilityPassDate(Date incomeCredibilityPassDate) {
		this.incomeCredibilityPassDate = incomeCredibilityPassDate;
	}

	public Date getJobCredibilityPassDate() {
		return jobCredibilityPassDate;
	}

	public void setJobCredibilityPassDate(Date jobCredibilityPassDate) {
		this.jobCredibilityPassDate = jobCredibilityPassDate;
	}

	public Date getAddressCredibilityPassDate() {
		return addressCredibilityPassDate;
	}

	public void setAddressCredibilityPassDate(Date addressCredibilityPassDate) {
		this.addressCredibilityPassDate = addressCredibilityPassDate;
	}

	public Integer getLoanType() {
		return loanType;
	}

	public void setLoanType(Integer loanType) {
		this.loanType = loanType;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
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

	public Integer getAccountHouseLoan() {
		return accountHouseLoan;
	}

	public void setAccountHouseLoan(Integer accountHouseLoan) {
		this.accountHouseLoan = accountHouseLoan;
	}

	public Integer getAccountRegisterIpProvice() {
		return accountRegisterIpProvice;
	}

	public void setAccountRegisterIpProvice(Integer accountRegisterIpProvice) {
		this.accountRegisterIpProvice = accountRegisterIpProvice;
	}

	public Integer getAccountRegisterIpCity() {
		return accountRegisterIpCity;
	}

	public void setAccountRegisterIpCity(Integer accountRegisterIpCity) {
		this.accountRegisterIpCity = accountRegisterIpCity;
	}

	public Integer getAccountRegisterIpTown() {
		return accountRegisterIpTown;
	}

	public void setAccountRegisterIpTown(Integer accountRegisterIpTown) {
		this.accountRegisterIpTown = accountRegisterIpTown;
	}

	public String getAccountIndustry() {
		return accountIndustry;
	}

	public void setAccountIndustry(String accountIndustry) {
		this.accountIndustry = accountIndustry;
	}

	public String getAccountThirdPaymentId() {
		return accountThirdPaymentId;
	}

	public void setAccountThirdPaymentId(String accountThirdPaymentId) {
		this.accountThirdPaymentId = accountThirdPaymentId;
	}

	public Integer getAccountThirdPaymentIdBindStatus() {
		return accountThirdPaymentIdBindStatus;
	}

	public void setAccountThirdPaymentIdBindStatus(Integer accountThirdPaymentIdBindStatus) {
		this.accountThirdPaymentIdBindStatus = accountThirdPaymentIdBindStatus;
	}

	public Integer getAccountTouchCity() {
		return accountTouchCity;
	}

	public void setAccountTouchCity(Integer accountTouchCity) {
		this.accountTouchCity = accountTouchCity;
	}

	public Integer getAccountTouchProvince() {
		return accountTouchProvince;
	}

	public void setAccountTouchProvince(Integer accountTouchProvince) {
		this.accountTouchProvince = accountTouchProvince;
	}

	public Integer getAccountRole() {
		return accountRole;
	}

	public void setAccountRole(Integer accountRole) {
		this.accountRole = accountRole;
	}

}
