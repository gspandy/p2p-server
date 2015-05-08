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


//用户消息
@Entity
@Table(name="t_act_msg")
public class AccountMessage {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_msg_seq", unique = true, nullable = false)
	private Long accountMessageSequence;// 流水号
	@Column(name = "t_acct_seq",nullable = false)
	private Long accountSequnece;// 用户流水号
	@Column(name = "amsg_title",nullable = false,length=90)
	private String MessageTitle;// 消息标题
	@Column(name = "amsg_rdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date MessageReceiveDate;// 接收时间
	@Column(name = "amsg_rstat",nullable = false)
	private Integer MessageReadStatus;// 是否已读 常量-参考数据字典
	@Column(name = "amsg_cont",nullable = false,length=2000)
	private String MessageContent;// 消息内容
	@Column(name = "t_act_remind_seq",nullable = false)//用户消息推送设置流水号
	private Long accountRemindSequence;
	@Column(name = "amsg_sender",nullable = false,length=60)	//发件人	
	private String accountMessageSender;
	@Column(name = "ropt_form",nullable = false)//	推送形式	
	private Integer remindOptionForm;

	public String getAccountMessageSender() {
		return accountMessageSender;
	}
	public void setAccountMessageSender(String accountMessageSender) {
		this.accountMessageSender = accountMessageSender;
	}
	public Long getAccountMessageSequence() {
		return accountMessageSequence;
	}
	public void setAccountMessageSequence(Long accountMessageSequence) {
		this.accountMessageSequence = accountMessageSequence;
	}
	public Long getAccountSequnece() {
		return accountSequnece;
	}
	public void setAccountSequnece(Long accountSequnece) {
		this.accountSequnece = accountSequnece;
	}
	public String getMessageTitle() {
		return MessageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		MessageTitle = messageTitle;
	}
	public Date getMessageReceiveDate() {
		return MessageReceiveDate;
	}
	public void setMessageReceiveDate(Date messageReceiveDate) {
		MessageReceiveDate = messageReceiveDate;
	}
	public Integer getMessageReadStatus() {
		return MessageReadStatus;
	}
	public void setMessageReadStatus(Integer messageReadStatus) {
		MessageReadStatus = messageReadStatus;
	}
	public String getMessageContent() {
		return MessageContent;
	}
	public void setMessageContent(String messageContent) {
		MessageContent = messageContent;
	}
	public Long getAccountRemindSequence() {
		return accountRemindSequence;
	}
	public void setAccountRemindSequence(Long accountRemindSequence) {
		this.accountRemindSequence = accountRemindSequence;
	}
	public Integer getRemindOptionForm() {
		return remindOptionForm;
	}
	public void setRemindOptionForm(Integer remindOptionForm) {
		this.remindOptionForm = remindOptionForm;
	}

}
