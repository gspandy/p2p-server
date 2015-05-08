package com.vcredit.jdev.p2p.enums;
/**
 * 投标计划类型
 * @author 周佩
 *
 */
public enum TransAmt {
	/**P--部分授权*/
	PART("P"),
	/**W--完全授权*/
	FULL("W");
	TransAmt(String code) {
		this.code = code;
	}

	private String code;

	public String getCode() {
		return code;
	}
}
