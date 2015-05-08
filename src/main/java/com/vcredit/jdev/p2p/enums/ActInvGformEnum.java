package com.vcredit.jdev.p2p.enums;

/**
 * 用户获得债权方式
 * 
 * @author zhuqiu
 *
 */
public enum ActInvGformEnum {

	/** 自动投资 */
	AUTO_INVEST(2),
	/** 手动投资 */
	MANUAL_INVEST(1),
	/** 转让 */
	TRANSFERED(3),
	/** 回购 */
	REPURCHASED(4),
	/** 垫付 */
	PREPAID(5);

	ActInvGformEnum(Integer code) {
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
