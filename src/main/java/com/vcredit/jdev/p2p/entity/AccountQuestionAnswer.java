package com.vcredit.jdev.p2p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



//用户密码问题答案
@Entity
@Table(name="t_sqsa")
public class AccountQuestionAnswer {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_sqsa_seq", unique = true, nullable = false)
	private Long accountQuestionAnswerSequence;// 流水号
	@Column(name = "t_sqs_seq",nullable = false)
	private Long systemQuestionSequence;// 系统提供的密码问题流水号
	@Column(name = "t_acct_seq",nullable = false)
	private Long accountSequence;// 用户表流水号
	@Column(name = "tac_asnw",nullable = false,length=60)
	private String accountQuestionAnswer;// 用户密码问题答案
	
	public Long getAccountQuestionAnswerSequence() {
		return accountQuestionAnswerSequence;
	}
	public void setAccountQuestionAnswerSequence(Long accountQuestionAnswerSequence) {
		this.accountQuestionAnswerSequence = accountQuestionAnswerSequence;
	}
	public Long getSystemQuestionSequence() {
		return systemQuestionSequence;
	}
	public void setSystemQuestionSequence(Long systemQuestionSequence) {
		this.systemQuestionSequence = systemQuestionSequence;
	}
	public Long getAccountSequence() {
		return accountSequence;
	}
	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}
	public String getAccountQuestionAnswer() {
		return accountQuestionAnswer;
	}
	public void setAccountQuestionAnswer(String accountQuestionAnswer) {
		this.accountQuestionAnswer = accountQuestionAnswer;
	}

}
