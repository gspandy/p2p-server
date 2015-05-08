package com.vcredit.jdev.p2p.enums;

/**
 * 订单状态枚举类
 * 
 * @author zhuqiu
 *
 */
public enum OrderStatusEnum {

	/** 未付款 */
	NON_PAYMENT(1),
	/** 付款中 */
	PAYING(2),
	/** 付款成功 */
	PAYMENT_SUCCESS(3),
	/** 付款失败 */
	PAYMENT_FALIUE(4),
	/** 未放款 */
	NOT_RELEASE_CASH(5),
	/** 申请冻结 */
	FREEZE_APPLY(6),
	/** 冻结成功 */
	FREEZE_SUCCESS(7),
	/** 冻结失败 */
	FREEZE_FALIUE(8),
	/** 放款成功 */
	RELEASE_CASH_SUCCESS(9),
	/** 放款失败 */
	RELEASE_CASH_FALIUE(10),
	/** 申请解冻 */
	UNFREEZE_APPLY(11),
	/** 解冻成功 */
	UNFREEZE_SUCCESS(12),
	/** 解冻失败 */
	UNFREEZE_FALIUE(13);

	OrderStatusEnum(Integer code) {
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
