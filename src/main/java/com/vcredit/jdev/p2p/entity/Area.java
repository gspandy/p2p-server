package com.vcredit.jdev.p2p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



//全国行政区
@Entity
@Table(name="t_area")
public class Area {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_area_seq", unique = true, nullable = false)
	private Long areaSequence;// 流水号
	@Column(name = "ta_acode",nullable = false)
	private Integer areaCode;// 所在大区编号
	@Column(name = "ta_aname",nullable = false,length=20)
	private String areaChineseName;// 所在大区中文名
	@Column(name = "ta_ccode")
	private Integer cityCode;// 所在地级市编号
	@Column(name = "ta_city",length=60)
	private String cityChineseName;// 所在地级市中文名
	@Column(name = "ta_id",nullable = false)
	private Integer addressId;// 所在地编号
	@Column(name = "ta_level",nullable = false)
	private Integer addressLevel;// 所在地等级编号
	@Column(name = "ta_lname",nullable = false,length=30)
	private String addressLevelChineseMean;// 所在地等级中文含义
	@Column(name = "ta_name",nullable = false,length=60)
	private String addressChineseName;// 所在地中文名
	@Column(name = "ta_prcode")
	private Integer addressProvinceCode;// 所在地的省份编码
	@Column(name = "ta_prov",length=30)
	private String addressProvinceChineseName;// 所在地的省份中文名
	
	public Long getAreaSequence() {
		return areaSequence;
	}
	public void setAreaSequence(Long areaSequence) {
		this.areaSequence = areaSequence;
	}
	public Integer getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(Integer areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaChineseName() {
		return areaChineseName;
	}
	public void setAreaChineseName(String areaChineseName) {
		this.areaChineseName = areaChineseName;
	}
	public Integer getCityCode() {
		return cityCode;
	}
	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityChineseName() {
		return cityChineseName;
	}
	public void setCityChineseName(String cityChineseName) {
		this.cityChineseName = cityChineseName;
	}
	public Integer getAddressId() {
		return addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	public Integer getAddressLevel() {
		return addressLevel;
	}
	public void setAddressLevel(Integer addressLevel) {
		this.addressLevel = addressLevel;
	}
	public String getAddressLevelChineseMean() {
		return addressLevelChineseMean;
	}
	public void setAddressLevelChineseMean(String addressLevelChineseMean) {
		this.addressLevelChineseMean = addressLevelChineseMean;
	}
	public String getAddressChineseName() {
		return addressChineseName;
	}
	public void setAddressChineseName(String addressChineseName) {
		this.addressChineseName = addressChineseName;
	}
	public Integer getAddressProvinceCode() {
		return addressProvinceCode;
	}
	public void setAddressProvinceCode(Integer addressProvinceCode) {
		this.addressProvinceCode = addressProvinceCode;
	}
	public String getAddressProvinceChineseName() {
		return addressProvinceChineseName;
	}
	public void setAddressProvinceChineseName(String addressProvinceChineseName) {
		this.addressProvinceChineseName = addressProvinceChineseName;
	}

}
