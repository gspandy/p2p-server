package com.vcredit.jdev.p2p.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



//用户银行卡
@Entity
@Table(name="t_act_bkcard")
public class AccountBankCard {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_bkcard_seq", unique = true, nullable = false)
	private Long BankCardSequence;// 流水号 account_bank_card_sequence
	@Column(name="act_third",nullable=false,length=60)
	private String thirdPaymentId;// 第三方支付账号 account_third-party_payment_id
	@Column(name = "bank_code",nullable = false)
	private Integer bankCode;// 所属银行 bank_code 常量-参考数据字典
	@Column(name = "abc_num",nullable = false,length=30)
	private String bankCardNumber;// 银行卡号码 bank_card_number
	@Column(name = "abc_ddstat",nullable = false)
	private Integer bankCardDefaultDrawStatus;// 是否默认取现银行卡bank_card_bind_status 常量-参考数据字典
	@Column(name = "abc_prov")
	private Integer bankProvince;// 开户行所在省份 bank_province 邮编-参考区域表
	@Column(name = "abc_city")
	private Integer bankCity;// 开户行所在地级市 bank_city 邮编-参考区域表
	@Column(name = "abc_town")
	private Integer bankTown;// 开户行所在区县 bank_town 邮编-参考区域表
	@Column(name = "t_acct_seq", nullable = false)
	private Long accountSequnece;// 用户流水号
	@Column(name = "abc_name",length=90)	//开户行名称	
	private String bankName;
	@Column(name = "abc_mobile",length=11)	//银行预留手机号	
	private String bankMobile;
	@Column(name = "abc_pri_zone",length=255)	//私有域	
	private String bankPrivateZone;
	@Column(name = "abc_char_set",length=30)	//数据编码	
	private String bankDataCharacterSet;

	public Long getBankCardSequence() {	
		return BankCardSequence;
	}
	public void setBankCardSequence(Long bankCardSequence) {
		BankCardSequence = bankCardSequence;
	}
	public Integer getBankProvince() {
		return bankProvince;
	}
	public void setBankProvince(Integer bankProvince) {
		this.bankProvince = bankProvince;
	}
	public Integer getBankCity() {
		return bankCity;
	}
	public void setBankCity(Integer bankCity) {
		this.bankCity = bankCity;
	}
	public Integer getBankTown() {
		return bankTown;
	}
	public void setBankTown(Integer bankTown) {
		this.bankTown = bankTown;
	}
	public String getThirdPaymentId() {
		return thirdPaymentId;
	}
	public void setThirdPaymentId(String thirdPaymentId) {
		this.thirdPaymentId = thirdPaymentId;
	}
	public Integer getBankCardDefaultDrawStatus() {
		return bankCardDefaultDrawStatus;
	}
	public void setBankCardDefaultDrawStatus(Integer bankCardDefaultDrawStatus) {
		this.bankCardDefaultDrawStatus = bankCardDefaultDrawStatus;
	}
	public Integer getBankCode() {
		return bankCode;
	}
	public void setBankCode(Integer bankCode) {
		this.bankCode = bankCode;
	}
	public Long getAccountSequnece() {
		return accountSequnece;
	}
	public void setAccountSequnece(Long accountSequnece) {
		this.accountSequnece = accountSequnece;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankMobile() {
		return bankMobile;
	}
	public void setBankMobile(String bankMobile) {
		this.bankMobile = bankMobile;
	}
	public String getBankPrivateZone() {
		return bankPrivateZone;
	}
	public void setBankPrivateZone(String bankPrivateZone) {
		this.bankPrivateZone = bankPrivateZone;
	}
	public String getBankDataCharacterSet() {
		return bankDataCharacterSet;
	}
	public void setBankDataCharacterSet(String bankDataCharacterSet) {
		this.bankDataCharacterSet = bankDataCharacterSet;
	}
	public String getBankCardNumber() {
		return bankCardNumber;
	}
	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}

}
