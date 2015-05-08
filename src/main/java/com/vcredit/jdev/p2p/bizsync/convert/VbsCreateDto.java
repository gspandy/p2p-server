package com.vcredit.jdev.p2p.bizsync.convert;

/**
 * 
 * @author zhaopeijun
 *
 */
public class VbsCreateDto {

	private Borrower borrower;
	
	private CardInfo cardInfo;
	
	private BorrowInfo borrowInfo;

	public Borrower getBorrower() {
		return borrower;
	}

	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}

	public CardInfo getCardInfo() {
		return cardInfo;
	}

	public void setCardInfo(CardInfo cardInfo) {
		this.cardInfo = cardInfo;
	}

	public BorrowInfo getBorrowInfo() {
		return borrowInfo;
	}

	public void setBorrowInfo(BorrowInfo borrowInfo) {
		this.borrowInfo = borrowInfo;
	}
	
	
	
}
