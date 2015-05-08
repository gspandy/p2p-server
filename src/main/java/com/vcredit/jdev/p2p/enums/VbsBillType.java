package com.vcredit.jdev.p2p.enums;

public enum VbsBillType {
	/** 普通账单 */
	usual_bill(1),
	
	/** 提前清贷账单 */
	advanced_bill(2);
	
	VbsBillType(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}
}
