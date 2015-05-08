package com.vcredit.jdev.p2p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



//系统提供的通知提醒选项
@Entity
@Table(name="t_ropt")
public class RemindOption {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_ropt_seq", unique = true, nullable = false)
	private Long remindOptionSequence;// 流水号
	@Column(name = "ropt_type",nullable = false)
	private Integer remindOptionOption;// 通知类型 常量-参考数据字典
	@Column(name = "ropt_form",nullable = false)
	private Integer remindOptionForm;// 通知形式 常量-参考数据字典
	
	public Long getRemindOptionSequence() {
		return remindOptionSequence;
	}
	public void setRemindOptionSequence(Long remindOptionSequence) {
		this.remindOptionSequence = remindOptionSequence;
	}
	public Integer getRemindOptionOption() {
		return remindOptionOption;
	}
	public void setRemindOptionOption(Integer remindOptionOption) {
		this.remindOptionOption = remindOptionOption;
	}
	public Integer getRemindOptionForm() {
		return remindOptionForm;
	}
	public void setRemindOptionForm(Integer remindOptionForm) {
		this.remindOptionForm = remindOptionForm;
	}

}
