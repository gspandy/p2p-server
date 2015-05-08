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


//操作记录
@Entity
@Table(name="t_oplog")
public class OperationRecord {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_oplog_seq", unique = true, nullable = false)
	private Long operationLogSequence;// 流水号
	@Column(name = "t_op_seq")
	private Long operaterSequence;// 后台人员编号
	@Column(name = "sql_text",length=2000)
	private String sqlText;// 后台人员执行的sql语句
	@Column(name = "op_date",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date operateDate;// 操作时间
	@Column(name = "op_type",nullable = false)
	private Integer operateType;//操作类型 常量-参考数据字典
	@Column(name = "t_acct_seq")
	private Long accountSequence;// 用户流水号
	
	public Long getOperationLogSequence() {
		return operationLogSequence;
	}
	public void setOperationLogSequence(Long operationLogSequence) {
		this.operationLogSequence = operationLogSequence;
	}
	public Long getOperaterSequence() {
		return operaterSequence;
	}
	public void setOperaterSequence(Long operaterSequence) {
		this.operaterSequence = operaterSequence;
	}
	public String getSqlText() {
		return sqlText;
	}
	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}
	public Date getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public Long getAccountSequence() {
		return accountSequence;
	}
	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}

}
