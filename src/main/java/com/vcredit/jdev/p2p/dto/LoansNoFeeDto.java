package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;

public class LoansNoFeeDto {
	/** 订单号 */
	private String ordId;
	/** 订单日期 */
	private Date ordDate;
	/** 出账客户号 */
	private Long outAccountId;
	/** 交易金额 */
	private BigDecimal transAmt;
	/** 订单号 投标的orderid */
	private String subOrdId;
	/** 订单日期 投标的时间 */
	private Date subOrdDate;
	/** 入账客户号 借款人第三方支付账号 */
	private Long inAccountId;
	/** 解冻订单号 */
	private String unFreezeOrdId;
	/** 冻结标识 */
	private String freezeTrxId;
	/**
	 * reqExt 入参扩展域 {"LoansVocherAmt":"50.00", "ProId":"000001"} 本次放款使用的代金券金额
	 * LoansVocherAmt 本次放款使用的代金券金额;ProId 项目 ID 4.3.5 主动投标/4.3.6
	 * 自动投标的借款人信息BorrowerDetails 字段的项目 ID ProId 有值
	 */
	private ReqExtLoans reqExt;
	
	public String getOrdId() {
		return ordId;
	}

	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}

	public Date getOrdDate() {
		return ordDate;
	}

	public String getOrdDateStr() {
		return DateFormatUtil.dateToString(ordDate, DateFormatUtil.YYYYMMDD);
	}

	public void setOrdDate(Date ordDate) {
		this.ordDate = ordDate;
	}
	public BigDecimal getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(BigDecimal transAmt) {
		BigDecimal temp=transAmt.setScale(2,BigDecimal.ROUND_HALF_UP);
		this.transAmt = temp;
	}

	public String getSubOrdId() {
		return subOrdId;
	}

	public void setSubOrdId(String subOrdId) {
		this.subOrdId = subOrdId;
	}

	public Date getSubOrdDate() {
		return subOrdDate;
	}

	public String getSubOrdDateStr() {
		return DateFormatUtil.dateToString(subOrdDate, DateFormatUtil.YYYYMMDD);
	}

	public void setSubOrdDate(Date subOrdDate) {
		this.subOrdDate = subOrdDate;
	}


	public String getUnFreezeOrdId() {
		return unFreezeOrdId;
	}

	public void setUnFreezeOrdId(String unFreezeOrdId) {
		this.unFreezeOrdId = unFreezeOrdId;
	}

	public String getFreezeTrxId() {
		return freezeTrxId;
	}

	public void setFreezeTrxId(String freezeTrxId) {
		this.freezeTrxId = freezeTrxId;
	}

	public ReqExtLoans getReqExt() {
		return reqExt;
	}

	public void setReqExt(ReqExtLoans reqExt) {
		this.reqExt = reqExt;
	}
	public String getReqExt2Json() {
		String result = null;
		try {
			result = P2pUtil.getBeanToJson(reqExt);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Long getOutAccountId() {
		return outAccountId;
	}

	public void setOutAccountId(Long outAccountId) {
		this.outAccountId = outAccountId;
	}

	public Long getInAccountId() {
		return inAccountId;
	}

	public void setInAccountId(Long inAccountId) {
		this.inAccountId = inAccountId;
	}
}
