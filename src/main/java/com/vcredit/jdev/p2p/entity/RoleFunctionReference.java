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


//角色功能关系
@Entity
@Table(name="t_role_func_ref")
public class RoleFunctionReference {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_role_func_ref_seq", unique = true, nullable = false)
	private Long functionRoleSequence;// 流水号
	@Column(name = "t_role_seq",nullable = false)
	private Long roleSequence;// 角色流水号
	@Column(name = "t_button_seq",nullable = false)
	private Long buttonSequence;// 按钮流水号
	@Column(name = "rbr_cdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date functionRoleReferenceCreateDate;// 创建时间
	@Column(name = "t_menu_seq",nullable = false)
	private Long menuSequence;// 菜单流水号
	
	public Long getRoleSequence() {
		return roleSequence;
	}
	public void setRoleSequence(Long roleSequence) {
		this.roleSequence = roleSequence;
	}
	public Long getButtonSequence() {
		return buttonSequence;
	}
	public void setButtonSequence(Long buttonSequence) {
		this.buttonSequence = buttonSequence;
	}
	public Long getFunctionRoleSequence() {
		return functionRoleSequence;
	}
	public void setFunctionRoleSequence(Long functionRoleSequence) {
		this.functionRoleSequence = functionRoleSequence;
	}
	public Date getFunctionRoleReferenceCreateDate() {
		return functionRoleReferenceCreateDate;
	}
	public void setFunctionRoleReferenceCreateDate(
			Date functionRoleReferenceCreateDate) {
		this.functionRoleReferenceCreateDate = functionRoleReferenceCreateDate;
	}
	public Long getMenuSequence() {
		return menuSequence;
	}
	public void setMenuSequence(Long menuSequence) {
		this.menuSequence = menuSequence;
	}
	
}
