package com.vcredit.jdev.p2p.enums;

/**
 * 缴费类别
 * 
 * @author zhuqiu
 *
 */
public enum AprTypeEnum {

	/** P2P平台放款服务费 */
	P2P_RELEASECASH_FEE(1),
	/** 正常还款-P2P平台管理费 */
	P2P_NORMALPAID_FEE(2),
	/** 提前清贷-P2P平台补偿金费 */
	P2P_PACKAGE_PAID_OFF_FEE(3),
	/** 债权转让P2P手续费 */
	P2P_BOND_TRANSFER_FEE(4),
	/** 提现-投资用户P2P平台手续费 */
	P2P_WITHDRAW_FEE(5),
	/** 投资管理费 */
	P2P_INVEST_FEE(6),
	/** 第三方支付平台-汇付-放款服务费 */
	HF_INVEST_FEE(7),
	/** 正常还款-P2P风险备用金费 */
	P2P_RISK_RESERVE_FUND_FEE(8),
	/** 第三方-汇付-充值-工商银行手续费 */
	/** 第三方-汇付-充值-农业银行手续费 */
	/** 第三方-汇付-充值-建设银行手续费 */
	/** 第三方-汇付-充值-中信银行手续费 */
	/** 第三方-汇付-充值-华夏银行手续费 */
	/** 第三方-汇付-充值-其它银行手续费 */
	;

	AprTypeEnum(Integer code) {
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
