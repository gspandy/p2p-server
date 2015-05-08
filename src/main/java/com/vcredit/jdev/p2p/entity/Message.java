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


//系统富文本内容
@Entity
@Table(name="t_msg")
public class Message {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_msg_seq", unique = true, nullable = false)
	private Long messageSequence;// 流水号
	@Column(name = "msg_type",nullable = false)
	private Integer messageType;// 富文本类型 常量-参考数据字典
	@Column(name = "msg_title",nullable = false,length=60)
	private String messageTitle;// 富文本标题
	@Column(name = "msg_content",nullable = false,length=2000)
	private String messageContent;// 富文本内容
	@Column(name = "msg_stats",nullable = false)
	private Integer messageStatus;// 富文本状态 常量-参考数据字典
	@Column(name = "msg_cdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date messageCreateDate;// 创建时间
	@Column(name = "msg_sdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date messageIssueDate;// 发布时间
	@Column(name = "msg_itype",nullable = false)
	private Integer messageIssueType;// 发布方式
	@Column(name = "t_res_seq")
	private Integer resourceSequence;//资源流水号
	@Column(name = "msg_idx")//富文本索引
	private Integer messageIndex;
	@Column(name = "username",nullable=false)	//后台操作人员
	private String userName;
	@Column(name = "msg_ddate")	//停用时间	
	private Date messageDropDate;
	@Column(name = "msg_src")	//信息来源	
	private String messageSource;	

	public String getMessageSource() {
		return messageSource;
	}
	public void setMessageSource(String messageSource) {
		this.messageSource = messageSource;
	}
	public Long getMessageSequence() {
		return messageSequence;
	}
	public void setMessageSequence(Long messageSequence) {
		this.messageSequence = messageSequence;
	}
	public Integer getMessageType() {
		return messageType;
	}
	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}
	public String getMessageTitle() {
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public Integer getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(Integer messageStatus) {
		this.messageStatus = messageStatus;
	}
	public Date getMessageCreateDate() {
		return messageCreateDate;
	}
	public void setMessageCreateDate(Date messageCreateDate) {
		this.messageCreateDate = messageCreateDate;
	}
	public Date getMessageIssueDate() {
		return messageIssueDate;
	}
	public void setMessageIssueDate(Date messageIssueDate) {
		this.messageIssueDate = messageIssueDate;
	}
	public Integer getMessageIssueType() {
		return messageIssueType;
	}
	public void setMessageIssueType(Integer messageIssueType) {
		this.messageIssueType = messageIssueType;
	}
	public Integer getResourceSequence() {
		return resourceSequence;
	}
	public void setResourceSequence(Integer resourceSequence) {
		this.resourceSequence = resourceSequence;
	}
	public Integer getMessageIndex() {
		return messageIndex;
	}
	public void setMessageIndex(Integer messageIndex) {
		this.messageIndex = messageIndex;
	}

	public Date getMessageDropDate() {
		return messageDropDate;
	}
	public void setMessageDropDate(Date messageDropDate) {
		this.messageDropDate = messageDropDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
