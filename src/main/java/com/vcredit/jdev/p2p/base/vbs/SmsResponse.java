package com.vcredit.jdev.p2p.base.vbs;

public class SmsResponse {
	private int	State;
	private String	Context;
	private String	Result;
	
	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
	}
	public String getContext() {
		return Context;
	}
	public void setContext(String context) {
		Context = context;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}

}
