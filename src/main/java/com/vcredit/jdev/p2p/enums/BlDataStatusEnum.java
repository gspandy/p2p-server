package com.vcredit.jdev.p2p.enums;

/**
 * VBS推送 贷前数据处理状态
 * 
 * @author zhaopeijun
 *
 */
public enum BlDataStatusEnum {
	
	/** pro_stat未处理，rec_stat贷前、贷后数据已入库 */
	importHandelCompleted(1),
	
	/** 完成p2p平台账号入库 */
	accountHandleCompleted(2),
	
	/** 完成投资项目入库 */
	investmentHandleCompleted(3),
	
	/** 完成第三方支付平台账户开户 */
	thirdAccountHandleCompleted(4),
	
	/** 完成银行卡绑定 */
	bankCardHandleCompleted(5);

	BlDataStatusEnum(int code) {
		this.code = code;
	}

	private int code;

	public int getCode() {
		return code;
	}
}
