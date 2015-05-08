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


//用户得到福利，暂时为的红包
@Entity
@Table(name="t_act_wf")
public class AccountWelfare {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_wf_seq", unique = true, nullable = false)
	private Long accountWelfareSequence;// 流水号
	@Column(name = "t_wf_seq",nullable = false)
	private Long welfareSequence;// 福利流水号--暂为红包
	@Column(name = "t_acct_seq",nullable = false)
	private Long accountSequence;// 用户流水号
	@Column(name = "taw_stat",nullable = false)
	private Integer welfareStatus;// 福利状态--暂为红包 常量-参考数据字典
	@Column(name = "taw_res",nullable = false)
	private Integer welfareSource;// 福利来源--暂为红包 常量-参考数据字典
	@Column(name = "taw_csu")
	private Integer welfareConsume;// 福利消费行为--暂为红包
	@Column(name = "taw_edate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date welfareEarnDate;// 获得福利时间--暂为红包 
	@Column(name = "taw_cdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date welfareConsumDate;// 消费福利时间--暂为红包
	
	public Long getAccountWelfareSequence() {
		return accountWelfareSequence;
	}
	public void setAccountWelfareSequence(Long accountWelfareSequence) {
		this.accountWelfareSequence = accountWelfareSequence;
	}
	public Long getWelfareSequence() {
		return welfareSequence;
	}
	public void setWelfareSequence(Long welfareSequence) {
		this.welfareSequence = welfareSequence;
	}
	public Long getAccountSequence() {
		return accountSequence;
	}
	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}
	public Integer getWelfareStatus() {
		return welfareStatus;
	}
	public void setWelfareStatus(Integer welfareStatus) {
		this.welfareStatus = welfareStatus;
	}
	public Integer getWelfareSource() {
		return welfareSource;
	}
	public void setWelfareSource(Integer welfareSource) {
		this.welfareSource = welfareSource;
	}
	public Integer getWelfareConsume() {
		return welfareConsume;
	}
	public void setWelfareConsume(Integer welfareConsume) {
		this.welfareConsume = welfareConsume;
	}
	public Date getWelfareEarnDate() {
		return welfareEarnDate;
	}
	public void setWelfareEarnDate(Date welfareEarnDate) {
		this.welfareEarnDate = welfareEarnDate;
	}
	public Date getWelfareConsumDate() {
		return welfareConsumDate;
	}
	public void setWelfareConsumDate(Date welfareConsumDate) {
		this.welfareConsumDate = welfareConsumDate;
	}

}
