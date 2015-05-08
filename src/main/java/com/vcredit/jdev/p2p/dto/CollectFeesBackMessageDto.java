package com.vcredit.jdev.p2p.dto;

/**
 * 贷款人还款扣手续费数据传输类
 * 
 * @author zhuqiu
 *
 */
public class CollectFeesBackMessageDto {

	public boolean isResult() {
		return result;
	}

	public String getMsg() {
		return msg;
	}

	public Long getAccountPaymentRecordSequence() {
		return accountPaymentRecordSequence;
	}

	public Long getAccountOrderSequence() {
		return accountOrderSequence;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setAccountPaymentRecordSequence(Long accountPaymentRecordSequence) {
		this.accountPaymentRecordSequence = accountPaymentRecordSequence;
	}

	public void setAccountOrderSequence(Long accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
	}

	/** 处理结果 */
	private boolean result;
	/** 信息 */
	private String msg;
	/** 缴费记录流水号 */
	private Long accountPaymentRecordSequence;
	/** 流水号 */
	private Long accountOrderSequence;

}
