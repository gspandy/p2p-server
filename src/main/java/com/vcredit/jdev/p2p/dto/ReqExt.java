package com.vcredit.jdev.p2p.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
/** 
 * reqExt {"ProId":"0000001"} 
 * ProId 标的的唯一标识 若 4.3.5 主动投标/4.3.6 自动投标的借款人信息
 * BorrowerDetails 字段的项目 ID ProId 有值，则必填，否则为可选
 */
public class ReqExt {
	@JsonProperty("ProId")
	private String proId;

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}
	
}
