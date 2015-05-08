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



//用户定义的投资规则
@Entity
@Table(name="t_act_inv_rule")
public class AccountInvestRule {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_inv_rule_seq", unique = true, nullable = false)
	private Long accountInvestmentRuleSequence;// 流水号
	@Column(name = "air_pmax",nullable = false,precision = 12, scale = 2)
	private BigDecimal perMaxAmount;// 单笔投资金额
	@Column(name = "air_period",length=30)
	private String period;// 期数
	@Column(name = "air_clv",length=30)
	private String creditLevel;// 信用等级
	@Column(name = "air_stat",nullable = false)
	private Integer status;// 投资规则状态 常量-参考数据字典
	@Column(name = "air_type",nullable = false)
	private Integer type;// 投资规则类别 常量-参考数据字典
	@Column(name = "t_acct_seq",nullable = false)
	private Long accountSequnece;// 用户流水号
	@Column(name = "rec_cdate",nullable = false)	//入库时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordCreateDate;
	@Column(name = "air_sdate")	//启动时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountInvestmentRuleStartDate;

	
	public Long getAccountInvestmentRuleSequence() {
		return accountInvestmentRuleSequence;
	}
	public void setAccountInvestmentRuleSequence(Long accountInvestmentRuleSequence) {
		this.accountInvestmentRuleSequence = accountInvestmentRuleSequence;
	}
	public BigDecimal getPerMaxAmount() {
		return perMaxAmount;
	}
	public void setPerMaxAmount(BigDecimal perMaxAmount) {
		this.perMaxAmount = perMaxAmount;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getCreditLevel() {
		return creditLevel;
	}
	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getAccountSequnece() {
		return accountSequnece;
	}
	public void setAccountSequnece(Long accountSequnece) {
		this.accountSequnece = accountSequnece;
	}
	public Date getRecordCreateDate() {
		return recordCreateDate;
	}
	public void setRecordCreateDate(Date recordCreateDate) {
		this.recordCreateDate = recordCreateDate;
	}
	public Date getAccountInvestmentRuleStartDate() {
		return accountInvestmentRuleStartDate;
	}
	public void setAccountInvestmentRuleStartDate(
			Date accountInvestmentRuleStartDate) {
		this.accountInvestmentRuleStartDate = accountInvestmentRuleStartDate;
	}
	
}
