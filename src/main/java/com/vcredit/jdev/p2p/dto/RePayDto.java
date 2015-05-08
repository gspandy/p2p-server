package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;

/**
 * 正常还款DTO
 * 
 * @author 周佩
 *
 */
public class RePayDto {
	/** 订单号 */
	private String ordId;
	/** 订单日期 */
	private Date ordDate;
	/** 出账客户号 */
	private String outCustId;
	/** 出账客户子账号 */
	private String outAcctId;
	/** TransAmt */
	private BigDecimal transAmt;
	/** 扣款手续费 */
	private BigDecimal fee;
	/** 订单号 */
	private String subOrdId;
	/** 订单日期 */
	private Date subOrdDate;
	/** 入账客户号 */
	private String inCustId;
	/** 分账商户号当fee不为0时必须有值 */
	private List<DivDetailDto> divDetails;
	/** 续费收取对象标志 默认入账用户收取*/
	private String feeObjFlag="I";
	/** reqExt {"ProId":"0000001"}  ProId 标的的唯一标识 若 4.3.5 主动投标/4.3.6 自动投标的借款人信息BorrowerDetails 字段的项目 ID ProId 有值，则必填，否则为可选*/
	private ReqExt reqExt;
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
	public String getOutCustId() {
		return outCustId;
	}
	public void setOutCustId(String outCustId) {
		this.outCustId = outCustId;
	}
	public BigDecimal getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public BigDecimal getFee2Formate() {
		return fee.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
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
	public String getInCustId() {
		return inCustId;
	}
	public void setInCustId(String inCustId) {
		this.inCustId = inCustId;
	}
	public List<DivDetailDto> getDivDetails() {
		return divDetails;
	}
	public String getDivDetails2Json() {
		if(divDetails==null){
			return null;
		}
		try {
			return P2pUtil.getBeanToJson(divDetails);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	public void setDivDetails(List<DivDetailDto> divDetails) {
		this.divDetails = divDetails;
	}
	public String getFeeObjFlag() {
		return feeObjFlag;
	}
	public void setFeeObjFlag(String feeObjFlag) {
		this.feeObjFlag = feeObjFlag;
	}
	public ReqExt getReqExt() {
		return reqExt;
	}
	public void setReqExt(ReqExt reqExt) {
		this.reqExt = reqExt;
	}
	public String getReqExt2Json() {
		if(reqExt==null){
			return null;
		}
		try {
			return P2pUtil.getBeanToJson(reqExt);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	public String getOutAcctId() {
		return outAcctId;
	}
	public void setOutAcctId(String outAcctId) {
		this.outAcctId = outAcctId;
	}
	
}
