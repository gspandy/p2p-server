package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

public class DepositCapitalDto {
	/** 三方资金账号 */
	private String ThirdPaymentAccount;
	/** 银行卡 */
	private String bankCard;
	/** 金额 */
	private BigDecimal price;
	/** 项目流水号 */
	private Long investmentSequence;
	/** 用户充值/提现记录 流水号 */
	private Long cashOperationRecordSequence;//	流水号
	/** 流水号 */
	private Long accountOrderSequence;
	/** 商户收取服务金额 */
	private String servFee;
	/** 商户子账号 */
	private String servFeeAcctId;
	/** 商户银行账户 */
	private String openAcctId;
	/** 入参扩展域 */
	private String reqExt;

	public String getThirdPaymentAccount() {
		return ThirdPaymentAccount;
	}

	public void setThirdPaymentAccount(String thirdPaymentAccount) {
		ThirdPaymentAccount = thirdPaymentAccount;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getInvestmentSequence() {
		return investmentSequence;
	}

	public Long getCashOperationRecordSequence() {
		return cashOperationRecordSequence;
	}

	public Long getAccountOrderSequence() {
		return accountOrderSequence;
	}

	public void setInvestmentSequence(Long investmentSequence) {
		this.investmentSequence = investmentSequence;
	}

	public void setCashOperationRecordSequence(Long cashOperationRecordSequence) {
		this.cashOperationRecordSequence = cashOperationRecordSequence;
	}

	public void setAccountOrderSequence(Long accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
	}

	public String getServFee() {
		return servFee;
	}

	public void setServFee(String servFee) {
		this.servFee = servFee;
	}

	public String getServFeeAcctId() {
		return servFeeAcctId;
	}

	public void setServFeeAcctId(String servFeeAcctId) {
		this.servFeeAcctId = servFeeAcctId;
	}

	public String getOpenAcctId() {
		return openAcctId;
	}

	public void setOpenAcctId(String openAcctId) {
		this.openAcctId = openAcctId;
	}

	public String getReqExt() {
		return reqExt;
	}

	public void setReqExt(String reqExt) {
		this.reqExt = reqExt;
	}

}
