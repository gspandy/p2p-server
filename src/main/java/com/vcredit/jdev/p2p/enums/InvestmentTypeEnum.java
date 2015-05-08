package com.vcredit.jdev.p2p.enums;
/**
 * 发布方式
 * @author 周佩
 *
 */
public enum InvestmentTypeEnum {
	/** 自动发布 */
	AUTO(1),
	/** 定时发布 */
	TIMEING(2),
	/** 临时发布 */
	TEMPORARY(3),
	/** 流标在发布的老项目 */
	LASTBID2ONLINE(4),
	/** 流标在发布的新项目 */
	NEWONLINE(5);
	

	InvestmentTypeEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}
}
