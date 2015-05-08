package com.vcredit.jdev.p2p.enums;
/**
 * loanData状态
 * @author 周佩
 *
 */
public enum LoanDataStatusEnum {

	/** 已处理 */
	IS_OPERATE(0),
	/** 未处理 */
	IS_NO_OPERATE(1),
	/** 完成p2p平台账号入库 */
	COMPLETE_P2P_ACCOUNT_CREATE(2),
	/** 完成投资项目入库 */
	COMPLETE_INVESTMENT_CREATE(3),
	/** 完成第三方支付平台账户开户 */
	COMPLETE_THIRDACCOUNT_CREATE(4),
	/** 完成银行卡绑定 */
	COMPLETE_BANKCARD_BIND(5);

	LoanDataStatusEnum(Integer code) {
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
