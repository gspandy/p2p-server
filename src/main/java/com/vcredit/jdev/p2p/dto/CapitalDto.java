package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

/**
 * 资金交易数据传输类
 * 
 * @author 周佩
 *
 */
public class CapitalDto {
	/** 项目ID */
	private Long projectId;
	/** 订单ID */
	private BigDecimal orderid;
	/** 贷款人第三方支付账号信息 */
	private String borrower;
	/** 投资人第三方支付账号信息 */
	private String investors;
	/** 交易金额ID */
	private BigDecimal price;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public BigDecimal getOrderid() {
		return orderid;
	}

	public void setOrderid(BigDecimal orderid) {
		this.orderid = orderid;
	}

	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}

	public String getInvestors() {
		return investors;
	}

	public void setInvestors(String investors) {
		this.investors = investors;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
