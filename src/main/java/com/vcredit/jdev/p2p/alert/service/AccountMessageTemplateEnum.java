package com.vcredit.jdev.p2p.alert.service;

/**
 * @ClassName: AccountMessageTemplate
 * @Description:
 * @author ChenChang
 * @date 2014年12月26日 下午3:34:45
 */
public enum AccountMessageTemplateEnum {

	/**
	 * 放款通知 站内信
	 */
	FANG_KUAN_TONG_ZHI(1),
	/**
	 * 流标通知 站内信
	 */
	LIU_BIAO_TONG_ZHI(2),
	/**
	 * 正常还款通知 站内信
	 */
	ZHENG_CHANG_HUAN_KUAN(3),
	/**
	 * 逾期还款通知 站内信
	 */
	YU_QI_HUAN_KUAN(4),
	/**
	 * 逾期通知 站内信
	 */
	YU_QI_TONG_ZHI(5),
	/**
	 * 垫付通知 站内信
	 */
	DIAN_FU_TONG_ZHI(6),
	/**
	 * 回购通知 站内信
	 */
	HUI_GOU_TONG_ZHI(7),

	/**
	 * 结清通知 站内信
	 */
	JIE_QING_TONG_ZHI(8),
	/**
	 * 债权转让通知 站内信
	 */
	ZHAI_QUAN_ZHUANG_RANG(9),
	/**
	 * 红包通知 站内信
	 */
	HONG_BAO_TONG_ZHI(10),
	/**
	 * 注册成功通知 站内信
	 */
	ZHU_CE_CHENG_GONG(11),
	/**
	 * 修改登录密码通知 站内信
	 */
	XIU_GAI_MI_MA(12),
	/**
	 * 注册动态码 短信
	 */
	ZHU_CE_YAN_ZHENG(13),
	/**
	 * 找回密码动态码 短信
	 */
	ZHAO_HUI_MI_MA(14),
	/**
	 * 提现动态码 短信
	 */
	TI_XIAN_YAN_ZHENG(15),
	/**
	 * 修改手机号动态码 短信
	 */
	XIU_GAI_SHOU_JI(16),
	/**
	 * 找回安全保护问题动态码 短信
	 */
	ZHAO_HUI_AN_QUAN_WENTI(17),
	/**
	 * 放款短信通知 短信
	 */
	FANG_KUAN_DUAN_XING(18),
	/**
	 * 借款协议邮件 邮件
	 */
	JIE_KUAN_XIE_YI(19),
	/**
	 * 债权转让通知函 邮件
	 */
	ZHAI_QUAN_ZHUANG_RANG_YOUJIAN(20),
	/**
	 * 后台重置密码 短信
	 */
	HOU_TAI_CHONG_SHE_MIMA(21),
	/**
	 * 维金荟Email地址验证
	 */
	EMAIL_ADDR_YAN_ZHENG(22),

	/**
	 * 放款通知_借款人
	 */
	FANG_KUAN_TONG_ZHI_JKR(23);

	private int code;

	public int getCode() {
		return code;
	}

	private AccountMessageTemplateEnum(int _code) {
		this.code = _code;
	}
}
