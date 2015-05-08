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



//系统提供的密码找回问题
@Entity
@Table(name="t_sqs")
public class SystemQuestion {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_sqs_seq", unique = true, nullable = false)
	private Long systemQuestionSequence;// 流水号
	@Column(name = "quest_text",nullable = false,length=60)
	private String questionText;// 密码提示问题文本
	@Column(name = "quest_stat",nullable = false)
	private Integer questionStatus;//密码问题状态 常量-参考数据字典
	@Column(name = "quest_cdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date questionCreateDate;//密码问题创建时间
	@Column(name = "quest_edate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date questionEditDate;//密码问题修改时间
	public Long getSystemQuestionSequence() {
		return systemQuestionSequence;
	}
	public void setSystemQuestionSequence(Long systemQuestionSequence) {
		this.systemQuestionSequence = systemQuestionSequence;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public Integer getQuestionStatus() {
		return questionStatus;
	}
	public void setQuestionStatus(Integer questionStatus) {
		this.questionStatus = questionStatus;
	}
	public Date getQuestionCreateDate() {
		return questionCreateDate;
	}
	public void setQuestionCreateDate(Date questionCreateDate) {
		this.questionCreateDate = questionCreateDate;
	}
	public Date getQuestionEditDate() {
		return questionEditDate;
	}
	public void setQuestionEditDate(Date questionEditDate) {
		this.questionEditDate = questionEditDate;
	}

}
