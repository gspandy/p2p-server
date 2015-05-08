package com.vcredit.jdev.p2p.enums;

/**
 * 投资项目状态枚举类
 * 
 * @author zhuqiu
 *
 */
public enum InvestmentStatusEnum {

	/** 待激活 */
	ACTIVE_WAIT(1),
	/** 激活中 */
	ACTIVEING(2),
	/** 待发布 */
	RELEASE_WAIT(3),
	/** 预发布 */
	IS_RELEASE(4),
	/** 流标中 */
	TENDERING(5),
	/** 已满标 */
	TENDER_FINISH(6),
	/** 已结清 */
	REPAY_FINISH(7),
	/** 提前清贷 */
	ADVANCE_LOAN_CLEAR(8),
	/** 回购中 */
	BUY_BACKING(9),
	/** 已回购 */
	BUY_BACKED(10),
	/** 待回购 */
	BUY_BACK_WAIT(11),
	/** 招标中 */
	ON_LINE(12),
	/** 满标放款中 */
	RELEASE_CASH(13),
	/** 满标放款失败 */
	RELEASE_CASH_FALIUE(14),
	/** 满标放款成功 */
	RELEASE_CASH_SUCCESS(15),
	/** 已流标 */
	TENDER_FAIL(16),

	/** 逾期 */
	OVERDUE(17),
	/** 还款中 */
	REPAYMENT(18),
	/** 自动提现中 */
	AUTO_WITHDRAWING(19),
	/** 自动提现失败 */
	AUTO_WITHDRAW_FAIL(20),
	/** 自动提现成功 */
	AUTO_WITHDRAW_SUCCESS(21),
	/** 项目状态上线失败 */
	INVESTMENTONLINEFAILER(22),

	;

	InvestmentStatusEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}

}
