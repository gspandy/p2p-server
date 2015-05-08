package com.vcredit.jdev.p2p.enums;

public enum SpringIntegrationMessageEnum {
	/** 开户 */
	OPENACCOUNT("openaccount"),
	/** 激活 */
	ACTIVE("active"),
	/** 绑卡 */
	BINDCARD("bindCard"),
	/** 充值 */
	RECHARGE("recharge"),
	/** 提现 */
	DEPOSITCAPITAL("depositCapital"),
	/** 冻结 */
	FORZENCAPITAL("forzenCapital"),
	/** 解冻 */
	UNFORZENCAPITAL("unforzenCapital"),
	/** 投资 */
	INVEST("invest"),
	/** 满标放款 */
	CREDIT("credit"),
	/** 手续费 */
	FEECAPITAL("feeCapital"),
	/** 归集还款金额分账 */
	AMOUNTSPLIT("amountSplit"),
	/** 批量正常还款 */
	NORMALREPAYMENTBAT("normalRepaymentBat"),
	/** 正常还款 */
	NORMALREPAYMENT("normalRepayment"),
	/** 逾期还款 */
	OVERDUEREPAYMENT("overdueRepayment"),
	/** 逾期垫付  */
	OVERDUEPAYFORBORROWER("overduePayForBorrower"),
	/** 逾期回购 */
	OVERDUEBUYBACK("overdueBuyBack"),
	/** 提前清贷  */
	AHEADREPAYMENT("aheadRepayment");
	
	SpringIntegrationMessageEnum(String code) {
		this.code = code;
	}

	private String code;

	public String getCode() {
		return code;
	}
}
