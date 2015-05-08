package com.vcredit.jdev.p2p.entity;

import java.math.BigDecimal;
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

import com.vcredit.jdev.p2p.bizsync.convert.VbsQueryDto;

//用户P2P平台账号
@SqlResultSetMappings({ @SqlResultSetMapping(name = "VbsQueryDtoMapping", classes = { @ConstructorResult(targetClass = VbsQueryDto.class, columns = {
		@ColumnResult(name = "investmentBusinessCode"),@ColumnResult(name = "investmentNumber"),@ColumnResult(name = "investmentStatus",type=Integer.class),@ColumnResult(name = "investmentStartDate",type=Date.class),@ColumnResult(name = "investmentFillDate",type=Date.class),@ColumnResult(name = "investmentLostDate",type=Date.class),@ColumnResult(name = "investmentCreditDate",type=Date.class),@ColumnResult(name = "investmentProgress",type=BigDecimal.class),@ColumnResult(name = "resourcePath"),@ColumnResult(name = "recordStatus",type=Integer.class) }) }) })
//投资项目
@Entity
@Table(name="t_inv")
public class Investment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_inv_seq", unique = true, nullable = false)
	private Long investmentSequence;// 流水号
	@Column(name = "inv_num",nullable = false,length=60)
	private String investmentNumber;// 项目编号
	@Column(name = "inv_stype",nullable = false)
	private Integer investmentGuaranteedInterestType;// 保额保息情况 费率-参考数据字典
	@Column(name = "t_res_sseq",nullable = false)
	private Integer investmentGuaranteedInterestDescriptionImage;// 保额保息说明图片
	@Column(name = "t_res_lseq",nullable = false)
	private Integer investmentGredibilityImage;// 项目信用评级说明图片
	@Column(name = "inv_grt",nullable = false)
	private Integer investmentGuarantee;// 保障机构
	@Column(name = "inv_irate",nullable = false)
	private Integer investmentAnnualInterestRate;// 年利率 费率-参考数据字典
	@Column(name = "inv_period",nullable = false)
	private Integer investmentPeriod;// 期数 常量-参考数据字典
	@Column(name = "inv_total",nullable = false,precision = 12, scale = 2)
	private BigDecimal investmentTotal;// 可投资总金额
	@Column(name = "inv_rem",nullable = false,precision = 12, scale = 2)
	private BigDecimal investmentSurplus;// 剩余可投资金额
	@Column(name = "inv_jcount",nullable = false)
	private Integer investmentJoinedCount;// 已参与投资人数
	@Column(name = "inv_process",nullable = false,precision = 20, scale = 10)
	private BigDecimal investmentProgress;// 投资进度
	@Column(name = "inv_sdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date investmentStartDate;// 投资开始时间/项目上线时间
	@Column(name = "inv_edate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date investmentEndDate;// 投资结束时间
	@Column(name = "inv_cdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date investmentCreateDate;// 项目创建时间
	@Column(name = "inv_ptype",nullable = false)
	private Integer investmentPayType;// 还款方式 费率-参考数据字典
	@Column(name = "inv_stat",nullable = false)
	private Integer investmentStatus;// 项目状态 费率-参考数据字典
	@Column(name = "t_res_seq",nullable = false)
	private Integer resourceSequence;// 项目投资协议编号
	@Column(name = "inv_smount",precision = 12, scale = 2)
	private BigDecimal investmentStartAmount;// 起投金额
	@Column(name = "inv_ldate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date investmentLostDate;// 流标时间
	@Column(name = "inv_fdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date investmentFillDate;// 满标时间
	@Column(name = "inv_cscore",nullable = false)
	private Integer investmentCredibilityScore;// investment：项目评分 针对 loanData：credibilityScore信用评分Modify：20150327
	@Column(name = "inv_type")//项目发布方式 费率-参考数据字典
	private Integer investmentType;
	@Column(name = "inv_target")
	private Integer investmentTarget;//项目用途 费率-参考数据字典
	@Column(name = "inv_odate")	//清贷时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date investmentOverDate; 
	@Column(name = "inv_name",nullable = false,length=90)
	private String investmentName;// 项目名称
	@Column(name = "inv_eform")	//项目结清方式	
	private Integer investmentEndForm;
	@Column(name = "inv_lv")	//项目等级	
	private Integer investmentLevel;
	@Column(name = "inv_cre_date")	//放款时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date investmentCreditDate;
	@Column(name = "inv_pcount")	//已还期数	
	private Integer investmentPayCount;
	@Column(name = "inv_gtype",nullable=false)//	保障方式	
	private Integer investmentGuaranteeType;
	@Column(name = "lc_ptype",nullable = false)	//小贷公司推送类型	
	private Integer loanCompanyPushType;
	@Column(name = "inv_idate")	//发布时间	
	private Integer investmentIssueDate;	

	public Integer getInvestmentTarget() {
		return investmentTarget;
	}
	public void setInvestmentTarget(Integer investmentTarget) {
		this.investmentTarget = investmentTarget;
	}
	public Long getInvestmentSequence() {
		return investmentSequence;
	}
	public void setInvestmentSequence(Long investmentSequence) {
		this.investmentSequence = investmentSequence;
	}
	public String getInvestmentNumber() {
		return investmentNumber;
	}
	public void setInvestmentNumber(String investmentNumber) {
		this.investmentNumber = investmentNumber;
	}
	public Integer getInvestmentGuaranteedInterestType() {
		return investmentGuaranteedInterestType;
	}
	public void setInvestmentGuaranteedInterestType(
			Integer investmentGuaranteedInterestType) {
		this.investmentGuaranteedInterestType = investmentGuaranteedInterestType;
	}
	public Integer getInvestmentGuaranteedInterestDescriptionImage() {
		return investmentGuaranteedInterestDescriptionImage;
	}
	public void setInvestmentGuaranteedInterestDescriptionImage(
			Integer investmentGuaranteedInterestDescriptionImage) {
		this.investmentGuaranteedInterestDescriptionImage = investmentGuaranteedInterestDescriptionImage;
	}
	public Integer getInvestmentGredibilityImage() {
		return investmentGredibilityImage;
	}
	public void setInvestmentGredibilityImage(Integer investmentGredibilityImage) {
		this.investmentGredibilityImage = investmentGredibilityImage;
	}
	public Integer getInvestmentGuarantee() {
		return investmentGuarantee;
	}
	public void setInvestmentGuarantee(Integer investmentGuarantee) {
		this.investmentGuarantee = investmentGuarantee;
	}
	public Integer getInvestmentAnnualInterestRate() {
		return investmentAnnualInterestRate;
	}
	public void setInvestmentAnnualInterestRate(Integer investmentAnnualInterestRate) {
		this.investmentAnnualInterestRate = investmentAnnualInterestRate;
	}
	public Integer getInvestmentPeriod() {
		return investmentPeriod;
	}
	public void setInvestmentPeriod(Integer investmentPeriod) {
		this.investmentPeriod = investmentPeriod;
	}
	public BigDecimal getInvestmentTotal() {
		return investmentTotal;
	}
	public void setInvestmentTotal(BigDecimal investmentTotal) {
		this.investmentTotal = investmentTotal;
	}
	public BigDecimal getInvestmentSurplus() {
		return investmentSurplus;
	}
	public void setInvestmentSurplus(BigDecimal investmentSurplus) {
		this.investmentSurplus = investmentSurplus;
	}
	public Integer getInvestmentJoinedCount() {
		return investmentJoinedCount;
	}
	public void setInvestmentJoinedCount(Integer investmentJoinedCount) {
		this.investmentJoinedCount = investmentJoinedCount;
	}
	public BigDecimal getInvestmentProgress() {
		return investmentProgress;
	}
	public void setInvestmentProgress(BigDecimal investmentProgress) {
		this.investmentProgress = investmentProgress;
	}
	public Date getInvestmentStartDate() {
		return investmentStartDate;
	}
	public void setInvestmentStartDate(Date investmentStartDate) {
		this.investmentStartDate = investmentStartDate;
	}
	public Date getInvestmentEndDate() {
		return investmentEndDate;
	}
	public void setInvestmentEndDate(Date investmentEndDate) {
		this.investmentEndDate = investmentEndDate;
	}
	public Date getInvestmentCreateDate() {
		return investmentCreateDate;
	}
	public void setInvestmentCreateDate(Date investmentCreateDate) {
		this.investmentCreateDate = investmentCreateDate;
	}
	public Integer getInvestmentPayType() {
		return investmentPayType;
	}
	public void setInvestmentPayType(Integer investmentPayType) {
		this.investmentPayType = investmentPayType;
	}
	public BigDecimal getInvestmentStartAmount() {
		return investmentStartAmount;
	}
	public void setInvestmentStartAmount(BigDecimal investmentStartAmount) {
		this.investmentStartAmount = investmentStartAmount;
	}
	public Date getInvestmentLostDate() {
		return investmentLostDate;
	}
	public void setInvestmentLostDate(Date investmentLostDate) {
		this.investmentLostDate = investmentLostDate;
	}
	public Date getInvestmentFillDate() {
		return investmentFillDate;
	}
	public void setInvestmentFillDate(Date investmentFillDate) {
		this.investmentFillDate = investmentFillDate;
	}
	public Integer getInvestmentCredibilityScore() {
		return investmentCredibilityScore;
	}
	public void setInvestmentCredibilityScore(Integer investmentCredibilityScore) {
		this.investmentCredibilityScore = investmentCredibilityScore;
	}
	public Integer getResourceSequence() {
		return resourceSequence;
	}
	public void setResourceSequence(Integer resourceSequence) {
		this.resourceSequence = resourceSequence;
	}
	public Integer getInvestmentStatus() {
		return investmentStatus;
	}
	public void setInvestmentStatus(Integer investmentStatus) {
		this.investmentStatus = investmentStatus;
	}
	public Integer getInvestmentType() {
		return investmentType;
	}
	public void setInvestmentType(Integer investmentType) {
		this.investmentType = investmentType;
	}
	public Date getInvestmentOverDate() {
		return investmentOverDate;
	}
	public void setInvestmentOverDate(Date investmentOverDate) {
		this.investmentOverDate = investmentOverDate;
	}
	public String getInvestmentName() {
		return investmentName;
	}
	public void setInvestmentName(String investmentName) {
		this.investmentName = investmentName;
	}
	public Integer getInvestmentEndForm() {
		return investmentEndForm;
	}
	public void setInvestmentEndForm(Integer investmentEndForm) {
		this.investmentEndForm = investmentEndForm;
	}
	public Integer getInvestmentLevel() {
		return investmentLevel;
	}
	public void setInvestmentLevel(Integer investmentLevel) {
		this.investmentLevel = investmentLevel;
	}
	public Date getInvestmentCreditDate() {
		return investmentCreditDate;
	}
	public void setInvestmentCreditDate(Date investmentCreditDate) {
		this.investmentCreditDate = investmentCreditDate;
	}
	public Integer getInvestmentPayCount() {
		return investmentPayCount;
	}
	public void setInvestmentPayCount(Integer investmentPayCount) {
		this.investmentPayCount = investmentPayCount;
	}
	public Integer getInvestmentGuaranteeType() {
		return investmentGuaranteeType;
	}
	public void setInvestmentGuaranteeType(Integer investmentGuaranteeType) {
		this.investmentGuaranteeType = investmentGuaranteeType;
	}
	public Integer getLoanCompanyPushType() {
		return loanCompanyPushType;
	}
	public void setLoanCompanyPushType(Integer loanCompanyPushType) {
		this.loanCompanyPushType = loanCompanyPushType;
	}
	public Integer getInvestmentIssueDate() {
		return investmentIssueDate;
	}
	public void setInvestmentIssueDate(Integer investmentIssueDate) {
		this.investmentIssueDate = investmentIssueDate;
	}
	
}
