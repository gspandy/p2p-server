package com.vcredit.jdev.p2p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



//用户消息推送设置
@Entity
@Table(name="t_act_remind")
public class AccountRemind {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_remind_seq", unique = true, nullable = false)
	private Long accountRemindSequence;// 流水号
	@Column(name = "t_ropt_seq",nullable = false)
	private Long systemRemindOptionSequence;// 系统提供的消息推送选项流水号
	@Column(name = "t_acct_seq",nullable = false)
	private Long accountSequnece;// 用户流水号
	@Column(name = "remind_stat",nullable = false)	//设置状态	
	private Integer remindStatus;

	public Long getAccountRemindSequence() {
		return accountRemindSequence;
	}
	public void setAccountRemindSequence(Long accountRemindSequence) {
		this.accountRemindSequence = accountRemindSequence;
	}
	public Long getSystemRemindOptionSequence() {
		return systemRemindOptionSequence;
	}
	public void setSystemRemindOptionSequence(Long systemRemindOptionSequence) {
		this.systemRemindOptionSequence = systemRemindOptionSequence;
	}
	public Long getAccountSequnece() {
		return accountSequnece;
	}
	public void setAccountSequnece(Long accountSequnece) {
		this.accountSequnece = accountSequnece;
	}
	public Integer getRemindStatus() {
		return remindStatus;
	}
	public void setRemindStatus(Integer remindStatus) {
		this.remindStatus = remindStatus;
	}

}
