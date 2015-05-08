package com.vcredit.jdev.p2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vcredit.jdev.p2p.chinapnr.util.ChinapnrConstans;
/** 
 * 取现收取手续费时需要设置取现参数
 */
public class ReqExtCashDto {
	
	
	public enum CashChlEnum{
		/** 正常盈利 */
		FAST("FAST"),
		/** 转让中 */
		GENERAL("GENERAL"),
		/** 转让中 */
		IMMEDIATE("IMMEDIATE");
		
		CashChlEnum(String code) {
			this.code = code;
		}

		private String code;

		public String getCode() {
			return code;
		}

	}
	
	/**手续费收取对象M商户，U用户,默认商户*/
	@JsonProperty("FeeObjFlag")
	private String feeObjFlag=ChinapnrConstans.FeeObjFlag.M;
	/**手续费收取子账户M必填，U忽略*/
	@JsonProperty("FeeAcctId")
	private String feeAcctId="";
	/**取现渠道 默认快速取现*/
	@JsonProperty("CashChl")
	private String cashChl=ChinapnrConstans.CashChl.FAST;
	public String getFeeObjFlag() {
		return feeObjFlag;
	}
	public void setFeeObjFlag(String feeObjFlag) {
		this.feeObjFlag = feeObjFlag;
	}
	public String getFeeAcctId() {
		return feeAcctId;
	}
	public void setFeeAcctId(String feeAcctId) {
		this.feeAcctId = feeAcctId;
	}
	public String getCashChl() {
		return cashChl;
	}
	public void setCashChl(String cashChl) {
		this.cashChl = cashChl;
	}

}
