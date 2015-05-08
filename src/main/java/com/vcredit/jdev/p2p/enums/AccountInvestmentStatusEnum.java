package com.vcredit.jdev.p2p.enums;

/**
 * 用户获得的投资项目状态
 * 
 * @author zhuqiu
 *
 */
public enum AccountInvestmentStatusEnum {

	/** 正常盈利 */
	REPAY_NORMAL(1),
	/** 转让中 */
	TANSFERING(2),
	/** 转让成功 */
	TANSFER_SUCCESS(3),
	/** 转让失败 */
	TANSFER_FAIL(4),
	/** 流标 */
	TENDER_FAIL(5),
	/** 满标未放款 */
	TENDER_FINISH_RELEASE_CASH(1),
	/** 满标放款中 */
	TENDER_FINISH_RELEASING(7),
	/** 满标放款成功 */
	TENDER_FINISH_RELEASE_CASH_SUCCESS(8),
	/** 满标放款失败 */
	TENDER_FINISH_RELEASE_CASH_FAIL(9),
	/** 项目结束 */
	INVESTMENT_END(10),
	/** 流标退款中 */
	TENDER_FAIL_DRAWBACKING(11),
	/** 流标退款成功 */
	TENDER_FAIL_DRAWBACK_SUCCESS(12),
	/** 流标退款失败 */
	TENDER_FAIL_DRAWBACK_FAIL(13),
	/** 流标未退款 */
	TENDER_FAIL_NOT_DRAWBACK(14),
	/** 逾期 */
	OVERDUE(15);

	AccountInvestmentStatusEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}

}
