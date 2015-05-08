package com.vcredit.jdev.p2p.dto;

import com.vcredit.jdev.p2p.entity.LoanData;

public class LoanDataDto{
	private Long aid;
	private LoanData loanData;

	public Long getAid() {
		return aid;
	}

	public void setAid(Long aid) {
		this.aid = aid;
	}

	public LoanData getLoanData() {
		return loanData;
	}

	public void setLoanData(LoanData loanData) {
		this.loanData = loanData;
	}

	public LoanDataDto(Long aid, LoanData loanData) {
		this.aid = aid;
		this.loanData = loanData;
	}
	
	

}
