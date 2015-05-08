package com.vcredit.jdev.p2p.dto;

import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountBankCard;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;

/**
 * vbs推送数据对象
 * 
 * @author 周佩
 *
 */
public class BorrowerProject {

	private Investment investment;

	private ThirdPaymentAccount thirdPaymentAccount;

	private Account account;
	
	private AccountBankCard accountBankCard;

	public Investment getInvestment() {
		return investment;
	}

	public void setInvestment(Investment investment) {
		this.investment = investment;
	}

	public ThirdPaymentAccount getThirdPaymentAccount() {
		return thirdPaymentAccount;
	}

	public void setThirdPaymentAccount(ThirdPaymentAccount thirdPaymentAccount) {
		this.thirdPaymentAccount = thirdPaymentAccount;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public AccountBankCard getAccountBankCard() {
		return accountBankCard;
	}

	public void setAccountBankCard(AccountBankCard accountBankCard) {
		this.accountBankCard = accountBankCard;
	}

}
