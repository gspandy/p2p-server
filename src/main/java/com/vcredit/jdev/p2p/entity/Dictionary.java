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



//数据字典
@Entity
@Table(name="t_dic")
public class Dictionary {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_dic_seq", unique = true, nullable = false)
	private Long dataDictionarySequence;// 流水号
	@Column(name = "tab_name",nullable = false,length=30)
	private String tableName;// 表名code
	@Column(name = "col_name",nullable = false,length=30)
	private String columnName;// 字段名code
	@Column(name = "col_value")
	private Integer columnValue;// 字段值
	@Column(name = "tab_cn",nullable = false,length=60)
	private String tableChineseName;// 表的中文含义
	@Column(name = "col_cn")
	private String columnChineseName;// 字段的中文含义
	@Column(name = "col_cvcn",length=20)
	private String columnValueChineseMean;// 字段值的中文含义
	@Column(name = "rec_cdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordCreateDate;//记录创建时间
	@Column(name = "rec_status",nullable = false)
	private Integer recordStatus;//记录状态 常量-参考数据字典
	@Column(name = "rec_edate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordEditDate;//记录修改时间
	@Column(name = "col_bcvcn",length=20,nullable=false)	
	private String backgroundColumnChineseName;// 后台管理系统显示的中文含义
	@Column(name = "col_fcvcn",length=20)	
	private String frontColumnChineseName;// 前台显示的中文含义	
	
	public Long getDataDictionarySequence() {
		return dataDictionarySequence;
	}
	public void setDataDictionarySequence(Long dataDictionarySequence) {
		this.dataDictionarySequence = dataDictionarySequence;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Integer getColumnValue() {
		return columnValue;
	}
	public void setColumnValue(Integer columnValue) {
		this.columnValue = columnValue;
	}
	public String getTableChineseName() {
		return tableChineseName;
	}
	public void setTableChineseName(String tableChineseName) {
		this.tableChineseName = tableChineseName;
	}
	public String getColumnChineseName() {
		return columnChineseName;
	}
	public void setColumnChineseName(String columnChineseName) {
		this.columnChineseName = columnChineseName;
	}
	public String getColumnValueChineseMean() {
		return columnValueChineseMean;
	}
	public void setColumnValueChineseMean(String columnValueChineseMean) {
		this.columnValueChineseMean = columnValueChineseMean;
	}
	public Date getRecordCreateDate() {
		return recordCreateDate;
	}
	public void setRecordCreateDate(Date recordCreateDate) {
		this.recordCreateDate = recordCreateDate;
	}
	public Integer getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	public Date getRecordEditDate() {
		return recordEditDate;
	}
	public void setRecordEditDate(Date recordEditDate) {
		this.recordEditDate = recordEditDate;
	}
	public String getBackgroundColumnChineseName() {
		return backgroundColumnChineseName;
	}
	public void setBackgroundColumnChineseName(String backgroundColumnChineseName) {
		this.backgroundColumnChineseName = backgroundColumnChineseName;
	}
	public String getFrontColumnChineseName() {
		return frontColumnChineseName;
	}
	public void setFrontColumnChineseName(String frontColumnChineseName) {
		this.frontColumnChineseName = frontColumnChineseName;
	}
	
}
