package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * loans
 * @author 周佩
 *
 */
public class ReqExtLoans extends ReqExt {
	@JsonProperty("LoansVocherAmt")
	private BigDecimal loansVocherAmt;

	public BigDecimal getLoansVocherAmt() {
		if(loansVocherAmt!=null){
			BigDecimal temp=loansVocherAmt.setScale(2,BigDecimal.ROUND_HALF_UP);
			return temp;
		}
		return loansVocherAmt;
	}

	public void setLoansVocherAmt(BigDecimal loansVocherAmt) {
		this.loansVocherAmt = loansVocherAmt;
	}
	
}
