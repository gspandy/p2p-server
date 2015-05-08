package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 处理结果返回类
 * 
 * 
 * @author zhuqiu
 *
 */
public class CapitalWithdrawReturnDto {
	/** 处理结果 */
	private boolean result;
	/** 信息 */
	private String msg;
	/** 代码 */
	private String status;
	/** 订单ID */
	private String orderId;
	/** 订单日 */
	private Date orderDate;
	/** 手续费 */
	private BigDecimal servFee;
	/** 手续费收取账户 */
	private String servFeeAcctId;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public BigDecimal getServFee() {
		return servFee;
	}

	public String getServFeeAcctId() {
		return servFeeAcctId;
	}

	public void setServFee(BigDecimal servFee) {
		this.servFee = servFee;
	}

	public void setServFeeAcctId(String servFeeAcctId) {
		this.servFeeAcctId = servFeeAcctId;
	}

}
