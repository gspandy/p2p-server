package com.vcredit.jdev.p2p.dto;

import java.util.ArrayList;
import java.util.List;

import com.vcredit.jdev.p2p.entity.LoanCut;

/**
 * 还款消息数据传输类
 * 
 * @author zhuqiu
 *
 */
public class BondPayOffManagerMessageDto {

	/** 处理结果 */
	private boolean result;

	/** 信息 */
	private String msg;

	/** 信息 */
	private List<LoanCut> loanCutList = new ArrayList<LoanCut>();

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<LoanCut> getLoanCutList() {
		return loanCutList;
	}

	public void setLoanCutList(List<LoanCut> loanCutList) {
		this.loanCutList = loanCutList;
	}

	public void addLoanCut(LoanCut loanCut) {
		loanCutList.add(loanCut);
	}
}
