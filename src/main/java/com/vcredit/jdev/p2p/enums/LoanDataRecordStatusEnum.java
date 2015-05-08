package com.vcredit.jdev.p2p.enums;

/**
 * VBS记录推送到p2p的数据状态
 * @author zhaopeijun
 *
 */
public enum LoanDataRecordStatusEnum {

	/** p2p处理正常 */
	RIGHT(1),
	/** p2p处理异常 */
	ERROR(2);

	LoanDataRecordStatusEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}

	public String toString() {
		return String.valueOf(this.code);
	}
}
