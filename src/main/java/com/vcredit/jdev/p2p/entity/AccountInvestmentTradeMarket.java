package com.vcredit.jdev.p2p.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



//债权转让市场
@Entity
@Table(name = "t_act_inv_tmk")
public class AccountInvestmentTradeMarket {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_inv_tmk_seq", unique = true, nullable = false)
	//流水号
	private Long accountInvestmentTradeMarketSequence;		
	@Column(name = "t_act_inv_seq", nullable = false)
	private Long accountInvestmentSequence;// 用户获得的项目流水号
	@Column(name = "t_pacct_seq")	//	付费用户P2P平台账号流水号	
	private Long payAccountSequence;
	@Column(name = "t_gacct_seq", nullable = false)	//	收费用户P2P平台账号流水号	
	private Long gatherAccountSequence;
	@Column(name = "trade_idate", nullable = false)	//转让发起时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date tradeIssueDate;
	@Column(name = "trade_ddate")	//转让成交时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date tradeDealDate;
	@Column(name = "trade_odate", nullable = false)	//转让过期时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date tradeOutDate;
	@Column(name = "trade_stat", nullable = false)	//转让状态 常量-参考数据字典
	private Integer tradeStatus;
	public Long getAccountInvestmentTradeMarketSequence() {
		return accountInvestmentTradeMarketSequence;
	}
	public void setAccountInvestmentTradeMarketSequence(
			Long accountInvestmentTradeMarketSequence) {
		this.accountInvestmentTradeMarketSequence = accountInvestmentTradeMarketSequence;
	}
	public Long getPayAccountSequence() {
		return payAccountSequence;
	}
	public void setPayAccountSequence(Long payAccountSequence) {
		this.payAccountSequence = payAccountSequence;
	}
	public Long getGatherAccountSequence() {
		return gatherAccountSequence;
	}
	public void setGatherAccountSequence(Long gatherAccountSequence) {
		this.gatherAccountSequence = gatherAccountSequence;
	}
	public Date getTradeIssueDate() {
		return tradeIssueDate;
	}
	public void setTradeIssueDate(Date tradeIssueDate) {
		this.tradeIssueDate = tradeIssueDate;
	}
	public Date getTradeDealDate() {
		return tradeDealDate;
	}
	public void setTradeDealDate(Date tradeDealDate) {
		this.tradeDealDate = tradeDealDate;
	}
	public Date getTradeOutDate() {
		return tradeOutDate;
	}
	public void setTradeOutDate(Date tradeOutDate) {
		this.tradeOutDate = tradeOutDate;
	}
	public Integer getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(Integer tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public Long getAccountInvestmentSequence() {
		return accountInvestmentSequence;
	}
	public void setAccountInvestmentSequence(Long accountInvestmentSequence) {
		this.accountInvestmentSequence = accountInvestmentSequence;
	}

}
