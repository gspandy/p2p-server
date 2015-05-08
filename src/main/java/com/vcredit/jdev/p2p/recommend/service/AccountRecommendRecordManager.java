package com.vcredit.jdev.p2p.recommend.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.alert.service.AccountMessageSender;
import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.entity.AccountRecommendRecord;
import com.vcredit.jdev.p2p.recommend.repository.AccountRecommendRecordRepository;

import org.apache.commons.lang3.StringUtils;

@Component
public class AccountRecommendRecordManager {
	@Autowired
	private P2pSessionContext p2pSessionContext;
	@Autowired
	AccountRecommendRecordRepository accountRecommendRecordRepository;
	@Autowired
	AccountMessageSender accountMessageSender;

	/**
	 * 发送邮件推荐好友
	 * 
	 * @param toUser
	 * @param sendSubject
	 * @param sendContent
	 */
	public void sendMailRecommend(String toUser, String sendSubject,
			String sendContent) {
		accountMessageSender.sendEmail(toUser, sendSubject, sendContent,"");
	}

	// 用户注册成功,有推荐人时,传递注册url,注册的 aid,保存
	// http://localhost/#/register?refid=22222&reftype=1
	/**
	 * @param url
	 *            推荐url
	 * @param accountSequence
	 *            推荐注册成功用户id
	 */
	public void saveAccountRecommendRecord(String url, Long accountSequence) {
		try {
			String refid = "";
			String reftype = "";

			try {
				refid = url.substring(url.indexOf("refid"), url.indexOf("&"))
						.replace("refid=", "");
				reftype = url.substring(url.indexOf("reftype")).replace(
						"reftype=", "");
			} catch (Exception e) {
				refid = "";
				reftype = "";
			}

			if (!StringUtils.isBlank(refid) && !StringUtils.isBlank(reftype)) {
				AccountRecommendRecord entity = new AccountRecommendRecord();

				entity.setAccountSequence(accountSequence);
				entity.setFriendId(refid);
				entity.setFriendIdType(Integer.valueOf(reftype));
				entity.setRecommendDate(new Date());
				entity.setRecommendStatus(0);
				entity.setRecommendUrl(url);

				accountRecommendRecordRepository.save(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
