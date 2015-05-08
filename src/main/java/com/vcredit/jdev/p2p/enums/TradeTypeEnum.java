package com.vcredit.jdev.p2p.enums;

/**
 * 订单的交易类型枚举类
 * 
 * @author luoxun
 *
 */
public enum TradeTypeEnum {

	/** 项目投资-自动 */
	INVEST(1),
	/** 满标放款 */
	RELEASE_CASH(2),
	/** 流标 */
	FAILURE_TENDERS(3),
	/** 正常收款 */
	BOND_GATHERING(4),
	/** 债权还款 */
	BOND_PAY_BACK(5),
	/** P2P平台放款服务 */
	P2P_RELEASE_CASH_SERVICE(6),
	/** P2P平台账户管理服务 */
	P2P_ACCOUNT_SERVICE(7),
	/** P2P平台债权转让服务 */
	P2P_BOND_TRANSFER_SERVICE(8),
	/** 债权转让 */
	BOND_TRANSFER(9),
	/** 债权垫付 */
	BOND_PREPAID(10),
	/** 债权回购 */
	BOND_REPURCHASE(11),
	/** 提前清贷 */
	BOND_PAY_OFF(12),
	/** 红包奖励 */
	RED_ENVELOPE_PURCHASE(13),
	//	/** 第三方-汇付-放款服务 */
	//	THIRD_PARTY_RELEASE_CASH(14),

	/** 充值 */
	RECHARGE(14),
	/** 取现 */
	WITHDRAW(15),
	/** 第三方-汇付-取现服务 */
	THIRD_WITHDRAW_SERVICE(18),
	/** 第三方-汇付-充值服务 */
	THIRD_RECHARGE_SERVICE(19),
	/** 项目投资-手动 */
	INVEST_MANUAL(20),
	/** p2p平台-取现服务 */
	P2P_WITHDRAW_FEE(21),
	/** 投资管理费 */
	P2P_INVEST(22),
	/** 冻结资金 */
	FREEZE(23),
	/** 解冻资金 */
	UNFREEZE(24),
	/** 贷款放款 */
	LOAN_RELEASE_CASH(25),
	/** p2p平台账户还款风险备用金 */
	P2P_ACCOUNT_RISK_RESERVE_FUND(26),
	/** 垫付收款 */
	PREPAID_GATHERING(27),
	/** 回购收款 */
	REPURCHASE_GATHERING(28),
	/** 罚息 */
	DEFAULT_INTEREST(29),
	/** 利息罚息尾差 */
	DEFAULT_INTEREST_BALANCE(30), ;

	TradeTypeEnum(Integer code) {
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
