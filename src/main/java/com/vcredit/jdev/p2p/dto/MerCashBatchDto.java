package com.vcredit.jdev.p2p.dto;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
/**
 * 批量还款
 * @author zhoupei
 *
 */
public class MerCashBatchDto {
	/** 订单号 */
	private String batchId;
	/** 用户交易号 */
	private String outCustId;
	/** 交易资金 */
	private String outAcctId;
	/** 服务费 */
	private String merOrdDate;
	/** 商户收取服务费的子账号 */
	private String proId;
	
	private List<InDetails> inDetails;

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getOutCustId() {
		return outCustId;
	}

	public void setOutCustId(String outCustId) {
		this.outCustId = outCustId;
	}

	public String getOutAcctId() {
		return outAcctId;
	}

	public void setOutAcctId(String outAcctId) {
		this.outAcctId = outAcctId;
	}

	public String getMerOrdDate() {
		return merOrdDate;
	}

	public void setMerOrdDate(String merOrdDate) {
		this.merOrdDate = merOrdDate;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public List<InDetails> getInDetails() {
		return inDetails;
	}

	public void setInDetails(List<InDetails> inDetails) {
		this.inDetails = inDetails;
	}
	
	public String getInDetails2Json() {
		try {
			return P2pUtil.getBeanToJson(inDetails);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
