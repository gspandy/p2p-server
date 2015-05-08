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


//后台人员角色关系
@Entity
@Table(name="t_role_opt_ref")
public class OperatorRoleReference {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_role_opt_ref_seq", unique = true, nullable = false)
	private Long operaterRoleReferenceSequence;// 流水号
	@Column(name = "t_role_seq",nullable = false)
	private Long roleSequence;// 角色流水号
	@Column(name = "t_op_seq",nullable = false)
	private Long operaterSequence;// 后台操作人员流水号
	@Column(name = "ror_cdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date operaterRoleReferenceCreateDate;// 创建时间
	
	public Long getOperaterRoleReferenceSequence() {
		return operaterRoleReferenceSequence;
	}
	public void setOperaterRoleReferenceSequence(Long operaterRoleReferenceSequence) {
		this.operaterRoleReferenceSequence = operaterRoleReferenceSequence;
	}
	public Long getRoleSequence() {
		return roleSequence;
	}
	public void setRoleSequence(Long roleSequence) {
		this.roleSequence = roleSequence;
	}
	public Long getOperaterSequence() {
		return operaterSequence;
	}
	public void setOperaterSequence(Long operaterSequence) {
		this.operaterSequence = operaterSequence;
	}
	public Date getOperaterRoleReferenceCreateDate() {
		return operaterRoleReferenceCreateDate;
	}
	public void setOperaterRoleReferenceCreateDate(
			Date operaterRoleReferenceCreateDate) {
		this.operaterRoleReferenceCreateDate = operaterRoleReferenceCreateDate;
	}

}
