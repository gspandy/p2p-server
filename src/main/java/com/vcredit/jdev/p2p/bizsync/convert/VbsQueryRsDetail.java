package com.vcredit.jdev.p2p.bizsync.convert;

import java.math.BigDecimal;

public class VbsQueryRsDetail {
	
	/** 业务订单号 */
	private String investmentBusinessCode;
	
	/** 项目编号 */
	private String investmentNumber;
	
	/** 项目状态 */
	private Integer investmentStatus;
	
	/** 投资开始时间/项目上线时间 */
	private String investmentStartDateStr;
	
	/** 满标时间 */
	private String investmentFillDateStr;
	
	/** 流标时间 */
	private String investmentLostDateStr;
	
	/** 放款时间	 */
	private String investmentCreditDateStr;
	
	/** 投资进度 */
	private BigDecimal investmentProgress;
	
	/** 资源存放路径 */
	private String resourcePath;
	
	/** 记录处理状态 */
	private Integer recordStatus;
	
	/** 解析错误编码 */
	private String parseErrCode;
	
	/** 解析错误信息 */
	private String parseErrMsg;
	
	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getParseErrCode() {
		return parseErrCode;
	}

	public void setParseErrCode(String parseErrCode) {
		this.parseErrCode = parseErrCode;
	}

	public String getParseErrMsg() {
		return parseErrMsg;
	}

	public void setParseErrMsg(String parseErrMsg) {
		this.parseErrMsg = parseErrMsg;
	}

	public String getInvestmentFillDateStr() {
		return investmentFillDateStr;
	}

	public void setInvestmentFillDateStr(String investmentFillDateStr) {
		this.investmentFillDateStr = investmentFillDateStr;
	}

	public String getInvestmentLostDateStr() {
		return investmentLostDateStr;
	}

	public void setInvestmentLostDateStr(String investmentLostDateStr) {
		this.investmentLostDateStr = investmentLostDateStr;
	}

	public String getInvestmentCreditDateStr() {
		return investmentCreditDateStr;
	}

	public void setInvestmentCreditDateStr(String investmentCreditDateStr) {
		this.investmentCreditDateStr = investmentCreditDateStr;
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

	public String getInvestmentStartDateStr() {
		return investmentStartDateStr;
	}

	public void setInvestmentStartDateStr(String investmentStartDateStr) {
		this.investmentStartDateStr = investmentStartDateStr;
	}
	
	

}
