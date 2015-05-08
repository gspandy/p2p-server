package com.vcredit.jdev.p2p.recommend;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.vcredit.jdev.p2p.Application;
import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.entity.AccountRecommendRecord;
import com.vcredit.jdev.p2p.recommend.service.AccountRecommendRecordManager;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountRecommendRecordManagerTest {

	@Autowired
	AccountRecommendRecordManager accountRecommendRecordManager;
	@Autowired
	private P2pSessionContext p2pSessionContext;

	@Test
	public void testSaveAccountRecommendRecord() {
		// 推荐人:用户进入网站时记录
		// // 当前注册用户:注册成功后记录
		// // 链接来源

		Long friendId = 33333L;// 推荐的用户
		// refid 推荐人id
		// reftype 推荐类型
		String recommendUrl = "http://localhost/p2p/refid=22222&reftype=1";

		accountRecommendRecordManager.saveAccountRecommendRecord(recommendUrl,
				friendId);

	}
}
