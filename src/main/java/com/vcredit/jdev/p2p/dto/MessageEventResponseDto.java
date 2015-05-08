package com.vcredit.jdev.p2p.dto;

/**
 * 发布消息返回类
 * 
 * 
 * @author zhuqiu
 *
 */
public class MessageEventResponseDto {
	/** 处理结果 */
	private boolean result;
	/** 信息 */
	private String msg;

	public boolean isResult() {
		return result;
	}

	public String getMsg() {
		return msg;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
