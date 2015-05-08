package com.vcredit.jdev.p2p.dto;

/**
 * 用户Dto
 * 
 * @author zhuqiu
 *
 */
public class ClaimGatherAmtDto {
	public ClaimGatherAmtDto(Integer clmGnum, Double clmGptotal) {
		super();
		this.clmGnum = clmGnum;
		this.clmGptotal = clmGptotal;
	}

	/** 交易金额 */
	private Integer clmGnum;

	/** 可用余额 */
	private Double clmGptotal;

	public Integer getClmGnum() {
		return clmGnum;
	}

	public void setClmGnum(Integer clmGnum) {
		this.clmGnum = clmGnum;
	}

	public Double getClmGptotal() {
		return clmGptotal;
	}

	public void setClmGptotal(Double clmGptotal) {
		this.clmGptotal = clmGptotal;
	}

}
