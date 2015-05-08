package com.vcredit.jdev.p2p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



//系统按钮
@Entity
@Table(name="t_button")
public class Button {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_button_seq", unique = true, nullable = false)
	private Long buttonSequence;// 流水号
	@Column(name = "button_name",nullable = false,length=30)
	private String buttonChineseName;// 按钮中文名
	@Column(name = "button_url",nullable = false,length=255)
	private String buttonForwardUrl;// 按钮跳转的url
	@Column(name = "button_code",nullable = false,length=30)
	private String buttonCode;// 按钮code
	@Column(name = "t_res_seq")
	private Integer buttonImage;// 按钮图片
	@Column(name = "t_menu_seq",nullable = false)
	private Long menuSequence;// 菜单流水号
	
	public Long getButtonSequence() {
		return buttonSequence;
	}
	public void setButtonSequence(Long buttonSequence) {
		this.buttonSequence = buttonSequence;
	}
	public String getButtonChineseName() {
		return buttonChineseName;
	}
	public void setButtonChineseName(String buttonChineseName) {
		this.buttonChineseName = buttonChineseName;
	}
	public String getButtonForwardUrl() {
		return buttonForwardUrl;
	}
	public void setButtonForwardUrl(String buttonForwardUrl) {
		this.buttonForwardUrl = buttonForwardUrl;
	}
	public String getButtonCode() {
		return buttonCode;
	}
	public void setButtonCode(String buttonCode) {
		this.buttonCode = buttonCode;
	}
	public Integer getButtonImage() {
		return buttonImage;
	}
	public void setButtonImage(Integer buttonImage) {
		this.buttonImage = buttonImage;
	}
	public Long getMenuSequence() {
		return menuSequence;
	}
	public void setMenuSequence(Long menuSequence) {
		this.menuSequence = menuSequence;
	}

}
