package com.vcredit.jdev.p2p.enums;

/**
 * 字典表中各种费率
 * 
 * @author zhuqiu
 *
 */
public enum FeeRateEnum {

	/** 罚息费率 */
	LC_JFRATE(1),
	/** 第三方支付平台-汇付-放款服务费率 */
	THIRD_RC_FRATE(1),
	/** 第三方-汇付-充值-建设银行手续费率 */
	THIRD_RCRATE_JIANSHE(131),
	/** 第三方-汇付-充值-工商银行手续费率 */
	THIRD_RCRATE_GONGSHANG(101),
	/** 第三方-汇付-充值-华夏银行手续费率 */
	THIRD_RCRATE_HUAXIA(331),
	/** 第三方-汇付-充值-农业银行手续费率 */
	THIRD_RCRATE_NOONGYE(111),
	/** 第三方-汇付-充值-其它银行手续费率 */
	THIRD_RCRATE_OTHER(901),
	/** 第三方-汇付-充值-中信银行手续费率 */
	THIRD_RCRATE_ZHONGXIN(151),
	/** 正常还款-P2P风险备用金费率 */
	CLAIM_PAY_RRATE(1),
	/** 正常还款-P2P平台管理费率 */
	P2P_PAY_FRATE(1),
	/** 提现-投资用户P2P平台手续费率 */
	P2P_DRAW_FRATE(1),
	/** 提前清贷-P2P平台补偿金费率 */
	P2P_EPAY_FRATE(1),
	/** 投资管理费率 */
	ACT_INV_PMFRATE(1),
	/** 债权转让P2P手续费率-持有债权9个月 */
	P2P_CT_FRATE(3),
	/** 债权转让P2P手续费率-持有债权6个月 */
	P2P_CT_FRATE_FOR_SIX(2),
	/** 债权转让P2P手续费率-持有债权3个月 */
	P2P_CT_FRATE_FOR_THREE(1),
	/** P2P平台放款服务费率 */
	P2P_RC_FRATE(1), ;

	FeeRateEnum(Integer code) {
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
