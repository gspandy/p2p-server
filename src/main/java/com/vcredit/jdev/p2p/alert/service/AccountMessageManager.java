/**
 * 
 */
package com.vcredit.jdev.p2p.alert.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.alert.repository.AccountMessageRepository;
import com.vcredit.jdev.p2p.alert.repository.AccountRemindRepository;
import com.vcredit.jdev.p2p.dto.AlertDto;
import com.vcredit.jdev.p2p.dto.SpringIntegrationMessageDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountMessage;
import com.vcredit.jdev.p2p.entity.AccountRemind;
import com.vcredit.jdev.p2p.enums.SpringIntegrationMessageEnum;

/**
 * @ClassName: AccountMessageManager
 * @Description:发送消息,保留记录,使用参见 AccountMessageManagerTest
 * @author ChenChang
 * @date 2014年12月26日 下午2:42:35
 */
@Component
@MessageEndpoint
public class AccountMessageManager {

	@Autowired
	private AccountMessageSender sender;
	@Autowired
	private AccountMessageType accountMessageType;
	@Autowired
	private AccountMessageRepository accountMessageRepository;
	@Autowired
	private AccountRemindRepository accountRemindRepository;

	/**
	 * 接收事件,发送消息
	 * 
	 * @param event
	 * @throws Exception
	 */
	@ServiceActivator(inputChannel = "eventPublishChannel")
	public void handle(Object event) throws Exception {
		if (event instanceof AlertDto) {
			AlertDto alertDto = (AlertDto) event;
			AccountMessageTemplateEnum template = alertDto.getAccountMessageTemplateEnum();
			AccountMessageTemplateData templateData = alertDto.getAccountMessageTemplateData();
			Object toUser = alertDto.getToUser();
			if (null != toUser) {
				this.sendAccMessage(template, templateData, null, toUser);
			}

		}
	}

	/**
	 * 更新用户消息设置
	 * 
	 * @param remindStatus
	 * @param accountSequnece
	 * @param systemRemindOptionSequence
	 */
	public void updateAccountRemindOne(Integer remindStatus, Long accountSequnece, Long systemRemindOptionSequence) {

		List<AccountRemind> list = accountRemindRepository.findAllByAid_RoptId(accountSequnece, systemRemindOptionSequence);
		if (list.size() > 0) {
			accountRemindRepository.updateAccountRemindOne(remindStatus, accountSequnece, systemRemindOptionSequence);
		} else {
			AccountRemind entity = new AccountRemind();
			entity.setAccountSequnece(accountSequnece);
			entity.setRemindStatus(remindStatus);
			entity.setSystemRemindOptionSequence(systemRemindOptionSequence);
			accountRemindRepository.save(entity);
		}
	}

	public void deleteAccountMessage(Long accountMessageId) {
		accountMessageRepository.delete(accountMessageId);
	}

	public Long queryAccountMessageCount4SiteMsg(Long aid) {
		return accountMessageRepository.queryAccountMessageCount4SiteMsg(aid);
	}

	public List<AccountRemind> saveAccountRemind(List<AccountRemind> list) {
		return accountRemindRepository.save(list);
	}

	/**
	 * 查找一条用户消息
	 * 
	 * @param accountMessageId
	 *            用户消息Id
	 * @return
	 */
	public AccountMessage queryAccountMessageOne(Long accountMessageId) {
		return accountMessageRepository.findOne(accountMessageId);
	}

	/**
	 * 保存
	 * 
	 * @param accountMessage
	 * @return
	 */
	public AccountMessage saveAccountMessageOne(AccountMessage accountMessage) {
		return accountMessageRepository.save(accountMessage);
	}

	/**
	 * 查找所有用户消息
	 * 
	 * @return
	 */
	public List<AccountMessage> queryAccountMessageAll() {
		return accountMessageRepository.findAll();
	}

	/**
	 * 查询一个用户所有消息
	 * 
	 * @return
	 */
	public List<AccountMessage> queryAccountMessageAllByUserId(Long userId) {
		return accountMessageRepository.findAccountMessageAllByUserId(userId);
	}

	public List<AccountMessage> queryAccountMessageAllByUserIdDate(Long userId, Date beginDate, Date endDate) {
		return accountMessageRepository.findAccountMessageAllByUserIdDate(userId, beginDate, endDate);
	}

	/**
	 * 发送给一个用户/多个用户
	 * 
	 * @param template
	 *            模板选项
	 * @param templateData
	 *            模板数据
	 * @param channel
	 *            发送通道
	 * @param toUser
	 *            用户<T> String,Account
	 */
	public void sendAccMessage(AccountMessageTemplateEnum template, AccountMessageTemplateData templateData, AccountMessageChannelEnum channel,
			Object toUser) {
		buildAccountType(template, templateData, channel);
		sendAccMessage(accountMessageType, toUser);
	}

	public void sendAccMessage(AccountMessageType accountMessageType, Object toUser) {
		sender.sendMsg(accountMessageType, toUser);
	}

	private void buildAccountType(AccountMessageTemplateEnum template, AccountMessageTemplateData templateData, AccountMessageChannelEnum channel) {
		accountMessageType.setTemplate(template);
		accountMessageType.setTemplateData(templateData);
		accountMessageType.setChannel(channel);
		accountMessageType.buildAccountMessageType();
	}

}
