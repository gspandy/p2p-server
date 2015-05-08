package com.vcredit.jdev.p2p.dto;

import java.math.BigInteger;
import java.util.Date;

public class AutoInvestDto {
	/** 流水号 */
	private Long tActInvRuleSeq;
	/** 单笔最大投资金额 */
	private Double airPmax;
	/** 账户保留余额 */
	private Double airRbal;
	/** 年利率 */
	private String airIrate;
	/** 期数 */
	private String airPeriod;
	/** 信用等级 */
	private String airClv;
	/** 投资规则状态 */
	private Integer airStat;
	/** 投资规则类别 */
	private Integer airType;
	/** 用户流水号 */
	private BigInteger tAcctSeq;

	private Double availableAmt;//可用余额 =投资人账户可用余额-冻结余额 

	private Date tacRdate;// 注册时间 p2p_account_register_date

	/** 用户第三方支付账号 */
	private String actThird;
	/** 今日还款资金 */
	private Double gptotal;

	public AutoInvestDto(Double airPmax, String airPeriod, String airClv, Integer airStat, Integer airType, BigInteger tAcctSeq, Double availableAmt,
			Date tacRdate, Double gptotal) {
		super();
		//		this.tActInvRuleSeq = tActInvRuleSeq;
		this.airPmax = airPmax;
		this.airPeriod = airPeriod;
		this.airClv = airClv;
		this.airStat = airStat;
		this.airType = airType;
		this.tAcctSeq = tAcctSeq;
		this.availableAmt = availableAmt;
		this.tacRdate = tacRdate;
		this.gptotal = gptotal;
	}

	public Long gettActInvRuleSeq() {
		return tActInvRuleSeq;
	}

	public void settActInvRuleSeq(Long tActInvRuleSeq) {
		this.tActInvRuleSeq = tActInvRuleSeq;
	}

	public Double getAirPmax() {
		return airPmax;
	}

	public void setAirPmax(Double airPmax) {
		this.airPmax = airPmax;
	}

	public Double getAirRbal() {
		return airRbal;
	}

	public void setAirRbal(Double airRbal) {
		this.airRbal = airRbal;
	}

	public String getAirIrate() {
		return airIrate;
	}

	public void setAirIrate(String airIrate) {
		this.airIrate = airIrate;
	}

	public String getAirPeriod() {
		return airPeriod;
	}

	public void setAirPeriod(String airPeriod) {
		this.airPeriod = airPeriod;
	}

	public String getAirClv() {
		return airClv;
	}

	public void setAirClv(String airClv) {
		this.airClv = airClv;
	}

	public Integer getAirStat() {
		return airStat;
	}

	public void setAirStat(Integer airStat) {
		this.airStat = airStat;
	}

	public Integer getAirType() {
		return airType;
	}

	public void setAirType(Integer airType) {
		this.airType = airType;
	}

	public BigInteger gettAcctSeq() {
		return tAcctSeq;
	}

	public void settAcctSeq(BigInteger tAcctSeq) {
		this.tAcctSeq = tAcctSeq;
	}

	public Double getAvailableAmt() {
		return availableAmt;
	}

	public void setAvailableAmt(Double availableAmt) {
		this.availableAmt = availableAmt;
	}

	public Date getTacRdate() {
		return tacRdate;
	}

	public void setTacRdate(Date tacRdate) {
		this.tacRdate = tacRdate;
	}

	public String getActThird() {
		return actThird;
	}

	public void setActThird(String actThird) {
		this.actThird = actThird;
	}

	public Double getGptotal() {
		return gptotal;
	}

	public void setGptotal(Double gptotal) {
		this.gptotal = gptotal;
	}

}
