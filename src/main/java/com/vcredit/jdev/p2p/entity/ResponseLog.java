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
@Table(name = "t_rep_log")//汇付应答记录
public class ResponseLog {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_rep_log_seq", unique = true, nullable = false)//汇付应答记录流水号	
	private Long requestLogSequence;
	@Column(name = "rep_date", nullable = false)	//应答时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date responseDate;	
	@Column(name = "rep_type", nullable = false, length=60)//应答类型
	private String responseType;
	@Column(name = "rep_code", nullable = false, length=60)//应答码
	private String responseCode;
	@Column(name = "rep_desc", length=255)//应答描述
	private String responseDescription;
	@Column(name = "rep_key", length=255) //应答关键字
	private String responseKey;
	public Long getRequestLogSequence() {
		return requestLogSequence;
	}
	public void setRequestLogSequence(Long requestLogSequence) {
		this.requestLogSequence = requestLogSequence;
	}
	public Date getResponseDate() {
		return responseDate;
	}
	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	public String getResponseKey() {
		return responseKey;
	}
	public void setResponseKey(String responseKey) {
		this.responseKey = responseKey;
	}
	
}
