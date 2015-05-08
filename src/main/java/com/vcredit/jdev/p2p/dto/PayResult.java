package com.vcredit.jdev.p2p.dto;
/**
 * 支付结果
 * @author 周佩
 *
 */
public class PayResult {
	/** 操作结果true：成功、false：失败 */
	private boolean result;
	/**订单号*/
	private String ordId;
	/**冻结订单号*/
	private String freezeId;
	/**冻结订单号*/
	private String unFreezeOrdId;
	/**经汇付处理的冻结订单号*/
	private String freezeTrxId;
	/**返回结果信息*/
	private String message;
	/**标示*/
	private String strTrxId;
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getOrdId() {
		return ordId;
	}
	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}
	public String getFreezeId() {
		return freezeId;
	}
	public void setFreezeId(String freezeId) {
		this.freezeId = freezeId;
	}
	public String getFreezeTrxId() {
		return freezeTrxId;
	}
	public void setFreezeTrxId(String freezeTrxId) {
		this.freezeTrxId = freezeTrxId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStrTrxId() {
		return strTrxId;
	}
	public void setStrTrxId(String strTrxId) {
		this.strTrxId = strTrxId;
	}
	public String getUnFreezeOrdId() {
		return unFreezeOrdId;
	}
	public void setUnFreezeOrdId(String unFreezeOrdId) {
		this.unFreezeOrdId = unFreezeOrdId;
	}
}
