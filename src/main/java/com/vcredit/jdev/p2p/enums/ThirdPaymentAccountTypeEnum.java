package com.vcredit.jdev.p2p.enums;

public enum ThirdPaymentAccountTypeEnum {
	/** 用户账号*/
	CUSTOMER_ACCOUNT(1),
	/** 平台还款账户 */
//	P2P_REPAY_ACCOUNT(2),
	/** Vcredit3P2P平台收费账户SDT000003*/
	P2P_CAPITAL_ACCOUNT(5),
	/** Vcredit5p2p平台垫付资金账号 SDT000001*/
	P2P_CAPITAL_PAYFORSOMEBODY_ACCOUNT(4),
	/** Vcredit1（还款资金账户）SDT000004*/
	P2P_RECHARGE_WITHDRAW_ACCOUNT(2),
	/** Vcredit2风险备用金资金账户 SDT000002*/
	P2P_RISK_ACCOUNT(6),
	/** Vcredit4MDT账户 */
	P2P_MDT_ACCOUNT(7);

	ThirdPaymentAccountTypeEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}
}
