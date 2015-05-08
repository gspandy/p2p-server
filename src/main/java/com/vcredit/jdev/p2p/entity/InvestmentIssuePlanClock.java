package com.vcredit.jdev.p2p.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


//投资项目定时发布时间点
@Entity
@Table(name="t_isp_clock")
public class InvestmentIssuePlanClock {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_isp_clock_seq", unique = true, nullable = false)
	private Long investmentIssuePlanClockSequence;// 流水号
	@Column(name = "isp_clock",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date investmentIssuePlanClock;// 定时发布时间点
	@Column(name = "isp_clock_desc",nullable = false,length=60)
	private String investmentIssuePlanClockDescription;// 时间点说明
	
	public Long getInvestmentIssuePlanClockSequence() {
		return investmentIssuePlanClockSequence;
	}
	public void setInvestmentIssuePlanClockSequence(
			Long investmentIssuePlanClockSequence) {
		this.investmentIssuePlanClockSequence = investmentIssuePlanClockSequence;
	}
	public Date getInvestmentIssuePlanClock() {
		return investmentIssuePlanClock;
	}
	public void setInvestmentIssuePlanClock(Date investmentIssuePlanClock) {
		this.investmentIssuePlanClock = investmentIssuePlanClock;
	}
	public String getInvestmentIssuePlanClockDescription() {
		return investmentIssuePlanClockDescription;
	}
	public void setInvestmentIssuePlanClockDescription(
			String investmentIssuePlanClockDescription) {
		this.investmentIssuePlanClockDescription = investmentIssuePlanClockDescription;
	}

}
