package com.vcredit.jdev.p2p.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



//投资项目放款情况
@Entity
@Table(name="t_loan_trans")
public class LoanTransfer {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_loan_trans_seq", unique = true, nullable = false)	//流水号	
	private Long loanTransferSequence;
	@Column(name = "tab_name",nullable = false,length=30)	//表名	
	private String tableName;
	@Column(name = "col_name",nullable = false,length=30)	//字段名	
	private String columnName;
	@Column(name = "col_value",nullable = false)	//字段值	
	private String columnValue;
	@Column(name = "src_data",nullable = false,length=60)	//源数据	
	private String sourceData;
	public Long getLoanTransferSequence() {
		return loanTransferSequence;
	}
	public void setLoanTransferSequence(Long loanTransferSequence) {
		this.loanTransferSequence = loanTransferSequence;
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
	public String getColumnValue() {
		return columnValue;
	}
	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}
	public String getSourceData() {
		return sourceData;
	}
	public void setSourceData(String sourceData) {
		this.sourceData = sourceData;
	}

}
