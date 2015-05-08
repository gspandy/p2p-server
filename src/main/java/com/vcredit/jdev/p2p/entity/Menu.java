package com.vcredit.jdev.p2p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



//系统菜单
@Entity
@Table(name="t_menu")
public class Menu {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_menu_seq", unique = true, nullable = false)
	private Long menuSequence;// 流水号
	@Column(name = "menu_name",nullable = false,length=60)
	private String menuChineseName;// 菜单中文名
	@Column(name = "menu_url",nullable = false,length=255)
	private String menuForwardUrl;// 菜单跳转的url
	@Column(name = "menu_code",nullable = false,length=30)
	private String menuCode;// 菜单code
	@Column(name = "t_res_seq")
	private Long resourceSequence;//菜单引用的图片
	@Column(name = "t_res_pseq")	
	private Long parentMenuSequence;//上级菜单流水号
	@Column(name = "menu_type",nullable = false,length=30)
	private String menuType;// 菜单类型
	
	public Long getMenuSequence() {
		return menuSequence;
	}
	public void setMenuSequence(Long menuSequence) {
		this.menuSequence = menuSequence;
	}
	public String getMenuChineseName() {
		return menuChineseName;
	}
	public void setMenuChineseName(String menuChineseName) {
		this.menuChineseName = menuChineseName;
	}
	public String getMenuForwardUrl() {
		return menuForwardUrl;
	}
	public void setMenuForwardUrl(String menuForwardUrl) {
		this.menuForwardUrl = menuForwardUrl;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public Long getParentMenuSequence() {
		return parentMenuSequence;
	}
	public void setParentMenuSequence(Long parentMenuSequence) {
		this.parentMenuSequence = parentMenuSequence;
	}
	public Long getResourceSequence() {
		return resourceSequence;
	}
	public void setResourceSequence(Long resourceSequence) {
		this.resourceSequence = resourceSequence;
	}

}
