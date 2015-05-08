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


//系统资源
@Entity
@Table(name="t_res")
public class Resource {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_res_seq", unique = true, nullable = false)
	private Long resourceSequence;// 流水号
	@Column(name = "res_name",nullable = false,length=60)
	private String resourceName;// 资源名称
	@Column(name = "res_path",nullable = false,length=255)
	private String resourcePath;// 资源存放路径
	@Column(name = "res_type",nullable = false)
	private Integer resourceType;// 资源类型 常量-参考数据字典
	@Column(name = "res_desc",length=60)
	private String resourceDescription;// 资源描述
	@Column(name = "res_cdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date resourceCreateDate;// 资源创建时间
	public Long getResourceSequence() {
		return resourceSequence;
	}
	public void setResourceSequence(Long resourceSequence) {
		this.resourceSequence = resourceSequence;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public Integer getResourceType() {
		return resourceType;
	}
	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}
	public String getResourceDescription() {
		return resourceDescription;
	}
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}
	public Date getResourceCreateDate() {
		return resourceCreateDate;
	}
	public void setResourceCreateDate(Date resourceCreateDate) {
		this.resourceCreateDate = resourceCreateDate;
	}
}
