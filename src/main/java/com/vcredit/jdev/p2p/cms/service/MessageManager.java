package com.vcredit.jdev.p2p.cms.service;

import java.util.List;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.cms.repository.MessageRepository;
import com.vcredit.jdev.p2p.entity.Message;

/**
 * @ClassName: MessageManager
 * @Description: 消息类业务逻辑管理 1.公告,活动,广告,通知 发布 C,R,U,D,RL
 * @author ChenChang
 * @date 2014年12月24日 下午3:21:46
 */
@Component
public class MessageManager {

	@Autowired
	private MessageRepository messageRepository;

	// 1.消息:发送,媒体(短信,邮件,站内)
	// 2.公告:发布,获取LIST,失效,
	// ,活动,广告,
	// 5.通知
	// FUNC:发布公告/查找公告一个/查找公告列表分页/

	/**
	 * 发布公告
	 * 
	 * @param message
	 */
	public void publishMessage(Message message) {
		messageRepository.save(message);
	}

	/**
	 * 查找一条公告
	 * 
	 * @param messageSequence
	 *            标识ID
	 * @return
	 */
	public Message queryMessageOne(Long messageSequence) {
		return messageRepository.findOne(messageSequence);
	}

	/**
	 * 查找公告列表
	 * 
	 * @return
	 */
	public List<Message> queryMessageAll() {
		return messageRepository.findAll();
	}

	/**
	 * 删除一条公告
	 * 
	 * @param messageSequence
	 *            标识ID
	 */
	public void deleteMessage(Long messageSequence) {
		messageRepository.delete(messageSequence);
	}
}
