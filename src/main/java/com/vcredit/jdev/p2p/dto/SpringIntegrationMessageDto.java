package com.vcredit.jdev.p2p.dto;

import com.vcredit.jdev.p2p.enums.SpringIntegrationMessageEnum;

/**
 * 模块间的消息发送
 * 
 * @author 周佩
 *
 */
public class SpringIntegrationMessageDto<T> {

	private SpringIntegrationMessageEnum message;
	
	private T obj;
	
	public SpringIntegrationMessageEnum getMessage() {
		return message;
	}

	public void setMessage(SpringIntegrationMessageEnum message) {
		this.message = message;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

}
