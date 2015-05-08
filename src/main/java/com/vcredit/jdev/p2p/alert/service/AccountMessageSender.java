package com.vcredit.jdev.p2p.alert.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.alert.repository.AccountMessageRepository;
import com.vcredit.jdev.p2p.alert.repository.AccountRemindRepository;
import com.vcredit.jdev.p2p.base.vbs.VcreditSmsServiceVBS;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountMessage;
import com.vcredit.jdev.p2p.entity.AccountRemind;
import com.vcredit.jdev.p2p.enums.MessageReadStatusEnum;

/**
 * @ClassName: AccountMessageSender
 * @Description:消息发送者
 * @author ChenChang
 * @date 2014年12月31日 下午12:57:10
 */

// TODO 20150119_155811 站内信,由SENDER 决定发不发送,
// 取 T_ACT_REMIND中状态 by 用户id,roptId
// 2. 提供<T> Account,String 两种发送选项

@Component
public class AccountMessageSender {
	
	private final static Logger logger = LoggerFactory.getLogger(AccountMessageSender.class);

	@Value("${spring.mail.username}")
	String from;

	@Autowired
	JavaMailSender mailSender;
	@Autowired
	private AccountMessageRepository accountMessageRepository;
	@Autowired
	private AccountRemindRepository accountRemindRepository;
	@Autowired
	private VcreditSmsServiceVBS vcreditSmsServiceVBS;

	/**
	 * 发送给一个用户/多个用户
	 * 
	 * @param accountMessageType
	 *            消息类型
	 * @param toUser
	 *            用户 Account,String,ID..
	 */
	public void sendMsg(AccountMessageType accountMessageType, Object toUser) {
		AccountMessageChannelEnum channel = accountMessageType.getChannel();

		String sendSubject = accountMessageType.getSendSubject();
		String sendContent = accountMessageType.getSendContent();
		String attachPath = accountMessageType.getTemplateData().getAttachPath();

		String toUserStr = "";

		if (toUser instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> toUsers = (List<Object>) (toUser);
			if (toUsers.size() == 1) {
				sendMsg(accountMessageType, toUsers.get(0));
			} else if (toUsers.size() > 1) {
				Iterator<Object> it = toUsers.iterator();
				Object toUserOne = null;
				while (it.hasNext()) {
					toUserOne = it.next();
					if (null != toUserOne) {
						sendMsg(accountMessageType, toUserOne);
					}
				}
			} else {

			}
		} else {
			// 发送邮件
			if (channel == AccountMessageChannelEnum.EMAIL) {
				if (toUser instanceof Account) {
					toUserStr = ((Account) toUser).getEmail();
				} else {
					toUserStr = toUser.toString();
				}
				if (!"".equals((toUserStr))) {
					sendEmail(toUserStr, sendSubject, sendContent, attachPath);
				}
			}

			// 发送短信
			else if (channel == AccountMessageChannelEnum.MOBILE) {
				System.out.println("sendMobileMsg BEGIN");

				if (toUser instanceof Account) {
					toUserStr = ((Account) toUser).getMobile();
				} else {
					toUserStr = toUser.toString();
				}
				if (!"".equals((toUserStr))) {
					sendMobileMsg(toUserStr, sendContent);
				}

				System.out.println("sendMobileMsg END");
			}

			// 发送站内信
			else if (channel == AccountMessageChannelEnum.SITE) {
				System.out.println("sendSiteMsg BEGIN");

				if (toUser instanceof Account) {
					toUserStr = ((Account) toUser).getAccountSequence().toString();
				} else {
					toUserStr = toUser.toString();
				}

				if (!"".equals((toUserStr))) {
					sendSiteMsg(accountMessageType, toUserStr, sendSubject, sendContent);
				}

				System.out.println("sendSiteMsg END");
			} else {
				System.out.println("NOT SUPPORT CHANNEL!");
			}
		}

	}

	/**
	 * @param accountMessageType
	 * @param toUser
	 * @param sendSubject
	 * @param sendContent
	 * @param accountRemindSequence
	 */
	private void sendSiteMsg(AccountMessageType accountMessageType, String toUser, String sendSubject, String sendContent) {

		long accountSequence;
		int remindOptionForm;
		long accountRemindSequence = 1L;

		try {
			accountSequence = Long.valueOf(toUser);
			remindOptionForm = accountMessageType.getTemplate().getCode();

			// check send or not @t_act_remind: userId & roptId & stat =1
			// 1未启用,不发送
			List<AccountRemind> list = accountRemindRepository.findAllByAid_RoptId_Stat(accountSequence, remindOptionForm, 1);

			if (list.size() == 0) {
				// INSERT DB
				AccountMessage entity = new AccountMessage();
				entity.setAccountSequnece(accountSequence);// 传入用户sequence
				entity.setMessageTitle(sendSubject);
				entity.setMessageContent(sendContent);
				entity.setMessageReadStatus(MessageReadStatusEnum.UNREAD.getCode());
				entity.setMessageReceiveDate(new Date());
				entity.setAccountRemindSequence(accountRemindSequence);
				entity.setAccountMessageSender("SYSTEM");
				entity.setRemindOptionForm(remindOptionForm);
				accountMessageRepository.save(entity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param toUser
	 * @param sendContent
	 */
	private void sendMobileMsg(String toUser, String sendContent) {
		try {
			vcreditSmsServiceVBS.mobileSendMessage(toUser, sendContent);
		} catch (Exception e) {
			logger.error("sendMobileMsg error:"+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @param toUser
	 * @param sendSubject
	 * @param sendContent
	 */
	public void sendEmail(String toUser, String sendSubject, String sendContent, String attachPath) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			mimeMessageHelper.setSubject(sendSubject);
			mimeMessageHelper.setTo(toUser);
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setText(sendContent, true);

			//	String path = this.getClass().getClassLoader().getResource("").getPath().replace("test-", "") + "templates/mail/" + attachPath;
			if (StringUtils.isNotBlank(attachPath)&&!"".equals(attachPath)) {
				FileSystemResource file = new FileSystemResource(attachPath);
				if (file.exists()) {
					mimeMessageHelper.addAttachment(file.getFilename(), file);
				} else {
					System.out.println(attachPath + "  file not exists !");
				}
			}

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
