package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;

/**
 * 放款DTO
 * 
 * @author 周佩
 *
 */
public class LoansDto {
	/** 订单号 */
	private String ordId;
	/** 订单日期 */
	private Date ordDate;
	/** 出账客户号 */
	private String outCustId;
	/** 交易金额 */
	private BigDecimal transAmt;
	/** 扣款手续费 0 */
	private BigDecimal fee;
	/** 订单号 投标的orderid */
	private String subOrdId;
	/** 订单日期 投标的时间 */
	private Date subOrdDate;
	/** 入账客户号 借款人第三方支付账号 */
	private String inCustId;
	/** 分账账户 手续费不为0要设 */
	private List<DivDetailDto> divDetails;
	/** 是否默认 Y/N ：Y */
	private String isDefault = "Y";
	/** 是否解冻 */
	private String isUnFreeze = "Y";
	/** 解冻订单号 */
	private String unFreezeOrdId;
	/** 冻结标识 */
	private String freezeTrxId;
	/** 续费收取对象标志I/O 手续费不为0要设 */
	private String feeObjFlag = "I";
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
		BigDecimal temp = transAmt.setScale(2,BigDecimal.ROUND_HALF_UP);
		this.transAmt = temp;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		BigDecimal temp = fee.setScale(2,BigDecimal.ROUND_HALF_UP);
		this.fee = temp;
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

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getIsUnFreeze() {
		return isUnFreeze;
	}

	public void setIsUnFreeze(String isUnFreeze) {
		this.isUnFreeze = isUnFreeze;
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

	public String getFeeObjFlag() {
		return feeObjFlag;
	}

	public void setFeeObjFlag(String feeObjFlag) {
		this.feeObjFlag = feeObjFlag;
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

}
