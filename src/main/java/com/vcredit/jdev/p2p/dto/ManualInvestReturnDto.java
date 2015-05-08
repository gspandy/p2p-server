package com.vcredit.jdev.p2p.dto;


/**
 * 处理结果返回类
 * 
 * 
 * @author zhuqiu
 *
 */
public class ManualInvestReturnDto {
	/** 处理结果 */
	private boolean result;
	/** 信息 */
	private String msg;
	/** 代码 */
	private String status;
	/** 项目投标订单ID */
	private String investOrderId;
	/** 冻结订单ID */
	private String freezeOrderId;

	public boolean isResult() {
		return result;
	}

	public String getMsg() {
		return msg;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setInvestOrderId(String investOrderId) {
		this.investOrderId = investOrderId;
	}

	public void setFreezeOrderId(String freezeOrderId) {
		this.freezeOrderId = freezeOrderId;
	}

	public String getInvestOrderId() {
		return investOrderId;
	}

	public String getFreezeOrderId() {
		return freezeOrderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
