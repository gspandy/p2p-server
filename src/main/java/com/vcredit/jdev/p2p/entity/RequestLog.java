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

@Entity
@Table(name = "t_req_log")//与汇付通信记录
public class RequestLog {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_req_log_seq", unique = true, nullable = false)//与汇付通信记录流水号	
	private Long requestLogSequence;
	@Column(name = "session_id", length=90)	//会话id	
	private String sessionId;
	@Column(name = "req_date", nullable = false)	//请求时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestDate;	
	@Column(name = "req_type", length=60, nullable = false)	//请求类型	
	private String requestType;
	@Column(name = "req_url",length=255)	//请求url	
	private String requestUrl;
	
	public Long getRequestLogSequence() {
		return requestLogSequence;
	}
	public void setRequestLogSequence(Long requestLogSequence) {
		this.requestLogSequence = requestLogSequence;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
}
