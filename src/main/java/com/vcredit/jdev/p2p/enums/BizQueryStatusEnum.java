package com.vcredit.jdev.p2p.enums;

/**
 * VBS查询 贷前数据处理状态
 * 
 * @author zhaopeijun
 *
 */
public enum BizQueryStatusEnum {
	
	/** 待发布 */
	RELEASE_WAIT(1),
	
	/** 招标中 */
	ON_LINE(2),
	
	/** 已流标 */
	TENDER_FAIL(3),
	
	/** 已满标 */
	TENDER_FINISH(4),
	
	/** 已放款 */
	RELEASE_CASH_FINISH(5),
	
	/** p2p已入库但未处理 */
	PROCESS_WAIT(0),
	
	/** 处理失败 */
	PROCESS_FAIL(99);

	BizQueryStatusEnum(int code) {
		this.code = code;
	}

	private int code;

	public int getCode() {
		return code;
	}
}
