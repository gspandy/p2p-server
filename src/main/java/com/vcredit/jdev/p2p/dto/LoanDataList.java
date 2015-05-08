package com.vcredit.jdev.p2p.dto;

import java.util.ArrayList;

import com.vcredit.jdev.p2p.entity.LoanData;

public class LoanDataList {
	private ArrayList<LoanData> loanDatas;

	public ArrayList<LoanData> getLoanDatas() {
		return loanDatas;
	}

	public void setLoanDatas(ArrayList<LoanData> loanDatas) {
		this.loanDatas = loanDatas;
	}
	
}
