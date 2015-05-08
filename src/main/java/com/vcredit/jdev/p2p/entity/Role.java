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


//角色
@Entity
@Table(name="t_role")
public class Role {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_role_seq", unique = true, nullable = false)
	private Long roleSequence;// 流水号
	@Column(name = "role_name",nullable = false,length=30)
	private String roleName;// 角色名
	@Column(name = "role_desc",nullable = false,length=60)
	private String roleDescription;// 角色说明
	@Column(name = "role_cdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date roleCreateDate;// 创建时间
	
	public Long getRoleSequence() {
		return roleSequence;
	}
	public void setRoleSequence(Long roleSequence) {
		this.roleSequence = roleSequence;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	public Date getRoleCreateDate() {
		return roleCreateDate;
	}
	public void setRoleCreateDate(Date roleCreateDate) {
		this.roleCreateDate = roleCreateDate;
	}

}
