package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

public class CapitalCalculateQueryDto {
	
	/** 手续费 */
	private BigDecimal operateFee;
	
	/** 实际发放贷款金额 */
	private BigDecimal acReleaseAmt;
	
	/** 放款失败原因 */
	private String tradeDesc;
	
	/** 剩余本金（元） */
	private BigDecimal invReleaseAmt;
	
	/** 剩余期限(个月) */
	private Integer releaseTerms;
	
	/** 还款日 */
	private Integer invRepayDate;
	
	/** 月还款额	 */
	private BigDecimal monthRepayAmt;
	
	/** 连续垫付次数 */
	private Integer ccm;
	
	/** 还款期数 */
	private Integer claimPayPlanPeriod;
	
	
	public CapitalCalculateQueryDto(BigDecimal operateFee,BigDecimal acReleaseAmt,String tradeDesc,BigDecimal invReleaseAmt,Integer releaseTerms,Integer invRepayDate,BigDecimal monthRepayAmt){
		super();
		this.operateFee = operateFee;
		this.acReleaseAmt = acReleaseAmt;
		this.tradeDesc = tradeDesc;
		this.invReleaseAmt = invReleaseAmt;
		this.releaseTerms = releaseTerms;
		this.invRepayDate = invRepayDate;
		this.monthRepayAmt = monthRepayAmt;
//		this.ccm = ccm;
	}


	public BigDecimal getOperateFee() {
		return operateFee;
	}


	public void setOperateFee(BigDecimal operateFee) {
		this.operateFee = operateFee;
	}


	public BigDecimal getAcReleaseAmt() {
		return acReleaseAmt;
	}


	public void setAcReleaseAmt(BigDecimal acReleaseAmt) {
		this.acReleaseAmt = acReleaseAmt;
	}


	public String getTradeDesc() {
		return tradeDesc;
	}


	public void setTradeDesc(String tradeDesc) {
		this.tradeDesc = tradeDesc;
	}


	public BigDecimal getInvReleaseAmt() {
		return invReleaseAmt;
	}


	public void setInvReleaseAmt(BigDecimal invReleaseAmt) {
		this.invReleaseAmt = invReleaseAmt;
	}


	public Integer getReleaseTerms() {
		return releaseTerms;
	}


	public void setReleaseTerms(Integer releaseTerms) {
		this.releaseTerms = releaseTerms;
	}


	public Integer getInvRepayDate() {
		return invRepayDate;
	}


	public void setInvRepayDate(Integer invRepayDate) {
		this.invRepayDate = invRepayDate;
	}


	public BigDecimal getMonthRepayAmt() {
		return monthRepayAmt;
	}


	public void setMonthRepayAmt(BigDecimal monthRepayAmt) {
		this.monthRepayAmt = monthRepayAmt;
	}


	public Integer getCcm() {
		return ccm;
	}


	public void setCcm(Integer ccm) {
		this.ccm = ccm;
	}


	public Integer getClaimPayPlanPeriod() {
		return claimPayPlanPeriod;
	}


	public void setClaimPayPlanPeriod(Integer claimPayPlanPeriod) {
		this.claimPayPlanPeriod = claimPayPlanPeriod;
	}

	

}
