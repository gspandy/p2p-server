package com.vcredit.jdev.p2p.dto;

import java.util.ArrayList;
import java.util.List;

import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;

/**
 * @author chenchang
 *
 * 
 *         toUser: 一个或多个 String,Account 发送站内信,邮件,手机短信
 */
public class AlertDto {

	private Object toUser;

	private AccountMessageTemplateEnum accountMessageTemplateEnum;
	private AccountMessageTemplateData accountMessageTemplateData;

	public AccountMessageTemplateEnum getAccountMessageTemplateEnum() {
		return accountMessageTemplateEnum;
	}

	public void setAccountMessageTemplateEnum(AccountMessageTemplateEnum accountMessageTemplateEnum) {
		this.accountMessageTemplateEnum = accountMessageTemplateEnum;
	}

	public AccountMessageTemplateData getAccountMessageTemplateData() {
		return accountMessageTemplateData;
	}

	public void setAccountMessageTemplateData(AccountMessageTemplateData accountMessageTemplateData) {
		this.accountMessageTemplateData = accountMessageTemplateData;
	}

	public Object getToUser() {
		return toUser;
	}

	/**
	 * @param toUser
	 *            : 一个 或 多个 接收用户,去重复
	 */
	public void setToUser(Object toUser) {
		this.setToUser(toUser, false);
	}

	/**
	 * @param toUser
	 * @param isAllowDulplicate
	 *            是否允许重复收件人
	 */
	public void setToUser(Object toUser, boolean isAllowDulplicate) {
		if (isAllowDulplicate || !(toUser instanceof List)) {
			this.toUser = toUser;
		} else {
			@SuppressWarnings("unchecked")
			List<Object> toUsers = (List<Object>) (toUser);
			List<Object> noDulplicateToUsers = new ArrayList<Object>();

			for (Object o : toUsers) {
				if (!noDulplicateToUsers.contains(o)) {
					noDulplicateToUsers.add(o);
				}
			}
			this.toUser = noDulplicateToUsers;
		}

	}

}
