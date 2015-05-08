package com.vcredit.jdev.p2p.bizsync.convert;

import java.math.BigDecimal;
import java.util.Date;

public class VbsQueryDto {
	
	/** 业务订单号 */
	private String investmentBusinessCode;
	
	/** 项目编号 */
	private String investmentNumber;
	
	/** 项目状态 */
	private Integer investmentStatus;
	
	/** 投资开始时间/项目上线时间 */
	private Date investmentStartDate;
	
	/** 满标时间 */
	private Date investmentFillDate;
	
	/** 流标时间 */
	private Date investmentLostDate;
	
	/** 放款时间	 */
	private Date investmentCreditDate;
	
	/** 投资进度 */
	private BigDecimal investmentProgress;
	
	/** 资源存放路径 */
	private String resourcePath;
	
	/** 记录处理状态 */
	private Integer recordStatus;
	
	public VbsQueryDto(String investmentBusinessCode,String investmentNumber,Integer investmentStatus,Date investmentStartDate,Date investmentFillDate,Date investmentLostDate,Date investmentCreditDate,BigDecimal investmentProgress,String resourcePath,Integer recordStatus){
		super();
		this.investmentBusinessCode = investmentBusinessCode;
		this.investmentNumber = investmentNumber;
		this.investmentStatus = investmentStatus;
		this.investmentStartDate = investmentStartDate;
		this.investmentFillDate = investmentFillDate; 
		this.investmentLostDate = investmentLostDate;
		this.investmentCreditDate = investmentCreditDate;
		this.investmentProgress = investmentProgress;
		this.resourcePath = resourcePath;
		this.recordStatus = recordStatus;
	}

	public String getInvestmentBusinessCode() {
		return investmentBusinessCode;
	}

	public void setInvestmentBusinessCode(String investmentBusinessCode) {
		this.investmentBusinessCode = investmentBusinessCode;
	}

	public String getInvestmentNumber() {
		return investmentNumber;
	}

	public void setInvestmentNumber(String investmentNumber) {
		this.investmentNumber = investmentNumber;
	}

	public Integer getInvestmentStatus() {
		return investmentStatus;
	}

	public void setInvestmentStatus(Integer investmentStatus) {
		this.investmentStatus = investmentStatus;
	}

	public Date getInvestmentStartDate() {
		return investmentStartDate;
	}

	public void setInvestmentStartDate(Date investmentStartDate) {
		this.investmentStartDate = investmentStartDate;
	}

	public Date getInvestmentFillDate() {
		return investmentFillDate;
	}

	public void setInvestmentFillDate(Date investmentFillDate) {
		this.investmentFillDate = investmentFillDate;
	}

	public Date getInvestmentLostDate() {
		return investmentLostDate;
	}

	public void setInvestmentLostDate(Date investmentLostDate) {
		this.investmentLostDate = investmentLostDate;
	}

	public Date getInvestmentCreditDate() {
		return investmentCreditDate;
	}

	public void setInvestmentCreditDate(Date investmentCreditDate) {
		this.investmentCreditDate = investmentCreditDate;
	}

	public BigDecimal getInvestmentProgress() {
		return investmentProgress;
	}

	public void setInvestmentProgress(BigDecimal investmentProgress) {
		this.investmentProgress = investmentProgress;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

}
