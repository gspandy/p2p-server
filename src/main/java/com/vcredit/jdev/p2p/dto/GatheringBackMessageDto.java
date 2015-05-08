package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

/**
 * 收款数据传输类
 * 
 * @author zhuqiu
 *
 */
public class GatheringBackMessageDto {

	public boolean isResult() {
		return result;
	}

	public String getMsg() {
		return msg;
	}

	public Long getAccountOrderSequence() {
		return accountOrderSequence;
	}

	public Long getClaimGatherPlanSequence() {
		return claimGatherPlanSequence;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setAccountOrderSequence(Long accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
	}

	public void setClaimGatherPlanSequence(Long claimGatherPlanSequence) {
		this.claimGatherPlanSequence = claimGatherPlanSequence;
	}

	/** 处理结果 */
	private boolean result;
	/** 信息 */
	private String msg;
	/** 流水号 */
	private Long accountOrderSequence;
	/** 收款计划流水号 */
	private Long claimGatherPlanSequence;

	public BigDecimal getManageFee() {
		return manageFee;
	}

	public void setManageFee(BigDecimal manageFee) {
		this.manageFee = manageFee;
	}

	/** p2p平台投资管理费（四舍五入） */
	private BigDecimal manageFee;

}
