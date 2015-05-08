package com.vcredit.jdev.p2p.dto;

import java.util.List;

public class LoanDataDtoList {
	private List<LoanDataDto> loanDataDtos;

	public List<LoanDataDto> getLoanDataDtos() {
		return loanDataDtos;
	}

	public void setLoanDataDtos(List<LoanDataDto> loanDataDtos) {
		this.loanDataDtos = loanDataDtos;
	}

	public LoanDataDtoList(List<LoanDataDto> loanDataDtos) {
		super();
		this.loanDataDtos = loanDataDtos;
	}

}
