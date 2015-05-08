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


//后台操作人员
@Entity
@Table(name="t_op")
public class Operator {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_op_seq", unique = true, nullable = false)
	private Long operaterSequence;// 流水号
	@Column(name = "buss_id",nullable = false,length=60)
	private String businessId;// 后台人员编号
	@Column(name = "op_src",nullable = false)
	private Integer operaterSource;// 来源
	@Column(name = "op_name",nullable = false,length=30)
	private String operaterName;// 用户名
	@Column(name = "op_passwd",nullable = false,length=30)
	private String operaterPassword;// 登录密码
	@Column(name = "op_cdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date operaterCreateDate;// 创建时间
	@Column(name = "op_status",nullable = false)
	private Integer operaterStatus;//账号状态 常量-参考数据字典
	
	public Long getOperaterSequence() {
		return operaterSequence;
	}
	public void setOperaterSequence(Long operaterSequence) {
		this.operaterSequence = operaterSequence;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public Integer getOperaterSource() {
		return operaterSource;
	}
	public void setOperaterSource(Integer operaterSource) {
		this.operaterSource = operaterSource;
	}
	public String getOperaterName() {
		return operaterName;
	}
	public void setOperaterName(String operaterName) {
		this.operaterName = operaterName;
	}
	public String getOperaterPassword() {
		return operaterPassword;
	}
	public void setOperaterPassword(String operaterPassword) {
		this.operaterPassword = operaterPassword;
	}
	public Date getOperaterCreateDate() {
		return operaterCreateDate;
	}
	public void setOperaterCreateDate(Date operaterCreateDate) {
		this.operaterCreateDate = operaterCreateDate;
	}
	public Integer getOperaterStatus() {
		return operaterStatus;
	}
	public void setOperaterStatus(Integer operaterStatus) {
		this.operaterStatus = operaterStatus;
	}

}
