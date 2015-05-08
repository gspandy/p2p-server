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



//小贷公司扣款信息
@Entity
@Table(name="t_loan_cut")
public class LoanCut {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_loan_cut_seq", unique = true, nullable = false)//流水号
	private Long loanCutSequence;//流水号
	@Column(name = "buss_code", nullable = false,length=60)
	private String investmentBusinessCode;//业务订单号
	@Column(name = "inv_src", nullable = false)
	private Integer investmentSource;//项目来源
	@Column(name = "cut_sdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date cutShouldDate;//应扣款时间
	@Column(name = "cut_adate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cutActualDate;//实际扣款时间
	@Column(name = "total_sget", nullable = false,precision = 12, scale = 2)
	private BigDecimal totalShouldGet;//应扣总金额
	@Column(name = "total_aget", nullable = false,precision = 12, scale = 2)
	private BigDecimal totalActulGet;//实扣总金额
	@Column(name = "loan_period", nullable = false)
	private int loanPeriod;//扣款期数
	@Column(name = "rec_stat",nullable = false)
	private Integer recordStatus;//记录处理状态
	@Column(name = "rec_idate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordInDate;//记录入库时间
	@Column(name = "rec_odate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordOperateDate;//记录处理时间
	@Column(name = "bill_type",nullable = false)	//账单类型	
	private Integer billType;
	@Column(name = "pro_stat",nullable = false)	//数据处理进度	
	private Integer progressStauts;
	
	public Long getLoanCutSequence() {
		return loanCutSequence;
	}
	public void setLoanCutSequence(Long loanCutSequence) {
		this.loanCutSequence = loanCutSequence;
	}
	public String getInvestmentBusinessCode() {
		return investmentBusinessCode;
	}
	public void setInvestmentBusinessCode(String investmentBusinessCode) {
		this.investmentBusinessCode = investmentBusinessCode;
	}
	public Integer getInvestmentSource() {
		return investmentSource;
	}
	public void setInvestmentSource(Integer investmentSource) {
		this.investmentSource = investmentSource;
	}
	public BigDecimal getTotalShouldGet() {
		return totalShouldGet;
	}
	public void setTotalShouldGet(BigDecimal totalShouldGet) {
		this.totalShouldGet = totalShouldGet;
	}
	public BigDecimal getTotalActulGet() {
		return totalActulGet;
	}
	public void setTotalActulGet(BigDecimal totalActulGet) {
		this.totalActulGet = totalActulGet;
	}
	public int getLoanPeriod() {
		return loanPeriod;
	}
	public void setLoanPeriod(int loanPeriod) {
		this.loanPeriod = loanPeriod;
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
	public Date getCutShouldDate() {
		return cutShouldDate;
	}
	public void setCutShouldDate(Date cutShouldDate) {
		this.cutShouldDate = cutShouldDate;
	}
	public Date getCutActualDate() {
		return cutActualDate;
	}
	public void setCutActualDate(Date cutActualDate) {
		this.cutActualDate = cutActualDate;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public Integer getProgressStauts() {
		return progressStauts;
	}
	public void setProgressStauts(Integer progressStauts) {
		this.progressStauts = progressStauts;
	}
	
}
