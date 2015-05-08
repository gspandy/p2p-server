package com.vcredit.jdev.p2p.alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.vcredit.jdev.p2p.Application;
import com.vcredit.jdev.p2p.alert.repository.AccountRemindRepository;
import com.vcredit.jdev.p2p.alert.service.AccountMessageChannelEnum;
import com.vcredit.jdev.p2p.alert.service.AccountMessageManager;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;
import com.vcredit.jdev.p2p.alert.service.AccountMessageType;
import com.vcredit.jdev.p2p.base.vbs.VcreditSmsServiceVBS;
import com.vcredit.jdev.p2p.dto.AlertDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountMessage;
import com.vcredit.jdev.p2p.entity.AccountRemind;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.util.DateUtil;

/**
 * @ClassName: AccountMessageManagerTest
 * @Description:
 * @author ChenChang
 * @date 2014年12月26日 下午4:30:50
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountMessageManagerTest {

	@Autowired
	private VcreditSmsServiceVBS smsServiceVBS;

	@Autowired
	private AccountMessageManager accountMessageManager;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountRemindRepository accountRemindRepository;

	@Autowired
	EventMessageGateway eventMessageGateway;

	@Before
	public void initialize() {
		Account a = accountRepository.findByMobile("13814878856");
		if (null == a) {
			a = new Account();
			a.setUserName("chenchang");
			a.setAccountStatus(0);
			a.setAccountStatus(0);
			a.setGender(0);
			a.setMobile("13814878856");
			a.setRegisterDate(new Date());
			a.setRegisterIp(" ");
			a.setEmail("chenchang@vcredit.com");
			a.setAccountRole(1);
			a = accountRepository.save(a);
		}

		if (!a.getEmail().equals("chenchang@vcredit.com")) {
			a.setEmail("chenchang@vcredit.com");
			a = accountRepository.save(a);
		}

		Long count = accountRemindRepository.getCountByAid_RoptId(a.getAccountSequence(),
				Long.valueOf(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI.getCode()));

		if (count == 0) {
			AccountRemind ar = new AccountRemind();
			ar.setAccountSequnece(a.getAccountSequence());
			ar.setRemindStatus(1);
			ar.setSystemRemindOptionSequence(Long.valueOf(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI.getCode()));
			accountRemindRepository.save(ar);

			ar = new AccountRemind();
			ar.setAccountSequnece(a.getAccountSequence());
			ar.setRemindStatus(0);
			ar.setSystemRemindOptionSequence(Long.valueOf(AccountMessageTemplateEnum.LIU_BIAO_TONG_ZHI.getCode()));
			accountRemindRepository.save(ar);

			ar = new AccountRemind();
			ar.setAccountSequnece(a.getAccountSequence());
			ar.setRemindStatus(0);
			ar.setSystemRemindOptionSequence(Long.valueOf(AccountMessageTemplateEnum.ZHENG_CHANG_HUAN_KUAN.getCode()));
			accountRemindRepository.save(ar);

			ar = new AccountRemind();
			ar.setAccountSequnece(a.getAccountSequence());
			ar.setRemindStatus(0);
			ar.setSystemRemindOptionSequence(Long.valueOf(AccountMessageTemplateEnum.YU_QI_HUAN_KUAN.getCode()));
			accountRemindRepository.save(ar);

			ar = new AccountRemind();
			ar.setAccountSequnece(a.getAccountSequence());
			ar.setRemindStatus(0);
			ar.setSystemRemindOptionSequence(Long.valueOf(AccountMessageTemplateEnum.YU_QI_TONG_ZHI.getCode()));
			accountRemindRepository.save(ar);

			ar = new AccountRemind();
			ar.setAccountSequnece(a.getAccountSequence());
			ar.setRemindStatus(0);
			ar.setSystemRemindOptionSequence(Long.valueOf(AccountMessageTemplateEnum.DIAN_FU_TONG_ZHI.getCode()));
			accountRemindRepository.save(ar);

			ar = new AccountRemind();
			ar.setAccountSequnece(a.getAccountSequence());
			ar.setRemindStatus(0);
			ar.setSystemRemindOptionSequence(Long.valueOf(AccountMessageTemplateEnum.HUI_GOU_TONG_ZHI.getCode()));
			accountRemindRepository.save(ar);

			ar = new AccountRemind();
			ar.setAccountSequnece(a.getAccountSequence());
			ar.setRemindStatus(0);
			ar.setSystemRemindOptionSequence(Long.valueOf(AccountMessageTemplateEnum.JIE_QING_TONG_ZHI.getCode()));
			accountRemindRepository.save(ar);

			ar = new AccountRemind();
			ar.setAccountSequnece(a.getAccountSequence());
			ar.setRemindStatus(0);
			ar.setSystemRemindOptionSequence(Long.valueOf(AccountMessageTemplateEnum.ZHAI_QUAN_ZHUANG_RANG.getCode()));
			accountRemindRepository.save(ar);

			ar = new AccountRemind();
			ar.setAccountSequnece(a.getAccountSequence());
			ar.setRemindStatus(0);
			ar.setSystemRemindOptionSequence(Long.valueOf(AccountMessageTemplateEnum.HONG_BAO_TONG_ZHI.getCode()));
			accountRemindRepository.save(ar);
		}

	}

	/**
	 * 发送给一个用户
	 *
	 * 1.指定发送人 toUser: 邮件/用户ID/手机号/用户Account 对象
	 *
	 * 2.查看要发送的模板AccountMessageTemplateEnum, 在 resource/mail 文件夹,确定要绑定的变量,
	 * 如:放款通知
	 * 
	 * 内容模板:FANG_KUAN_TONG_ZHI 标题模板:FANG_KUAN_TONG_ZHI_HEAD
	 *
	 * 3.绑定模板变量 AccountMessageTemplateData
	 *
	 * 4.AccountMessageManager 发送给一个用户
	 * 
	 * AccountMessageChannelEnum 设置为null
	 * 时,按系统默认通道发送(参见:AccountMessageTemplateEnum 注释)
	 * 
	 */
	@Test
	public void sendAccMessageOne() {

		String toUser = "chenchang@vcredit.com";// 发邮件
		AccountMessageTemplateData data = new AccountMessageTemplateData();
		data.setHelpCenterURL("www.baidu.com"); //默认 #/help

		data.setAttachPath("d:\\test.docx");
		// 发邮件
		// accountMessageManager.sendAccMessage(
		// AccountMessageTemplateEnum.XM_TOU_ZI_CHENG_GONG, data,
		// AccountMessageChannelEnum.EMAIL, toUser);

		// 发站内信
		toUser = "chenchang@vcredit.com"; // 用户ID,发站内信
		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI, data, AccountMessageChannelEnum.EMAIL, toUser);
	}

	@Test
	public void sendAccMessageMany() {
		List<String> toUsers = new ArrayList<String>();
		// 发送邮件
		toUsers.add("chenchang@vcredit.com");

		AccountMessageTemplateData data = new AccountMessageTemplateData();
		data.setProjectId("projectId_20141231");

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI, data, AccountMessageChannelEnum.EMAIL, toUsers);

		// 发送站内信
		toUsers.clear();
		toUsers.add("11111");// 用户ID,发站内信
		toUsers.add("22222");
		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI, data, AccountMessageChannelEnum.SITE, toUsers);
	}

	/**
	 * 测试默认通道发送
	 */
	@Test
	public void testSendAccMessage4DefaultChannel() {
		Account a = accountRepository.findByMobile("13814878856");
		AccountMessageTemplateData data = new AccountMessageTemplateData();
		data.setProjectId("projectId_20150120");

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.JIE_KUAN_XIE_YI, data, null, a);
	}

	/**
	 * 测试接收者为: Account
	 * 
	 * 测试 用户定义不发送的选项
	 */
	@Test
	public void testSendAccMessage4Account() {
		Account a = accountRepository.findByMobile("13814878856");
		AccountMessageTemplateData data = new AccountMessageTemplateData();
		data.setProjectId("projectId_20150120");

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHU_CE_YAN_ZHENG, data, AccountMessageChannelEnum.EMAIL, a);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI, data, AccountMessageChannelEnum.SITE, a);
	}

	/**
	 * 测试接收者为: String
	 */
	@Test
	public void testSendAccMessage() {

		String toUser = "chenchang@vcredit.com";// 发邮件
		AccountMessageTemplateData data = new AccountMessageTemplateData();
		data.setHelpCenterURL("www.baidu.com");
		data.setProjectId("projectId_20150120");

		// 发短信,暂以邮件测试
		data.setMobileVerificationCode("189564");

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHU_CE_YAN_ZHENG, data, AccountMessageChannelEnum.EMAIL, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHAO_HUI_MI_MA, data, AccountMessageChannelEnum.EMAIL, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.TI_XIAN_YAN_ZHENG, data, AccountMessageChannelEnum.EMAIL, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.XIU_GAI_SHOU_JI, data, AccountMessageChannelEnum.EMAIL, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHAO_HUI_AN_QUAN_WENTI, data, AccountMessageChannelEnum.EMAIL, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.FANG_KUAN_DUAN_XING, data, AccountMessageChannelEnum.EMAIL, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.HOU_TAI_CHONG_SHE_MIMA, data, AccountMessageChannelEnum.EMAIL, toUser);

		// 发邮件
		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.JIE_KUAN_XIE_YI, data, AccountMessageChannelEnum.EMAIL, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHAI_QUAN_ZHUANG_RANG_YOUJIAN, data, AccountMessageChannelEnum.EMAIL, toUser);

		// 发站内信
		toUser = "22222"; // 用户ID,发站内信
		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.LIU_BIAO_TONG_ZHI, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHENG_CHANG_HUAN_KUAN, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.YU_QI_HUAN_KUAN, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.YU_QI_TONG_ZHI, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.DIAN_FU_TONG_ZHI, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.HUI_GOU_TONG_ZHI, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.JIE_QING_TONG_ZHI, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHAI_QUAN_ZHUANG_RANG, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.HONG_BAO_TONG_ZHI, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHU_CE_CHENG_GONG, data, AccountMessageChannelEnum.SITE, toUser);

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.XIU_GAI_MI_MA, data, AccountMessageChannelEnum.SITE, toUser);

	}

	@Test
	public void testQueryAccountMessageByUserIdDate() {
		Long userId = 22222L;
		Date beginDate = DateUtil.getDate(2015, 0, 10);
		Date endDate = DateUtil.getDate(2015, 0, 15);

		List<AccountMessage> list = accountMessageManager.queryAccountMessageAllByUserIdDate(userId, beginDate, endDate);
		System.out.println(list.size());
		System.out.println(userId);
		System.out.println(beginDate);
		System.out.println(endDate);

	}

	@Test
	public void testSendMobileMessage() {
		String toUser = "13916145186";// 手机号
		AccountMessageTemplateData data = new AccountMessageTemplateData();

		// 绑定变量:验证码
		data.setMobileVerificationCode("189564");

		//		 accountMessageManager.sendAccMessage(
		//		 AccountMessageTemplateEnum.ZHU_CE_YAN_ZHENG, data,
		//		 AccountMessageChannelEnum.MOBILE, toUser);
	}

	@Test
	public void testSendEmailAddr() {
		String toUser = "chenchang@vcredit.com";// 手机号
		AccountMessageTemplateData data = new AccountMessageTemplateData();

		// 绑定变量:验证码
		data.setUsername("chenchang");
		data.setValidateURL("http://localhost");

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.EMAIL_ADDR_YAN_ZHENG, data, AccountMessageChannelEnum.EMAIL, toUser);
	}

	/**
	 * 20150330
	 * 
	 * FANG_KUAN_TONG_ZHI_JKR.html
	 */
	@Test
	public void testFANG_KUAN_TONG_ZHI_JKR() {
		String toUser = "chenchang@vcredit.com";// 手机号
		AccountMessageTemplateData data = new AccountMessageTemplateData();

		data.setP1("EDEN"); //[借款人用户名]
		data.setP2("10000");//[总金额]
		data.setP3("36");//[期限]
		data.setP4("15");//[还款日]

		data.setAttachPath("d:\\test.docx"); //协议附件

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI_JKR, data, AccountMessageChannelEnum.EMAIL, toUser);
	}

	/**
	 * 发布事件,触发消息,去重复
	 */
	@Test
	public void testPublishAlertEventNoDuplicate() {
		AlertDto dto = new AlertDto();

		AccountMessageTemplateData accountMessageTemplateData = new AccountMessageTemplateData();
		accountMessageTemplateData.setProjectId("ProjectId_20150203");

		dto.setAccountMessageTemplateData(accountMessageTemplateData);
		dto.setAccountMessageTemplateEnum(AccountMessageTemplateEnum.JIE_KUAN_XIE_YI);

		List<String> toUsers = new ArrayList<String>();
		toUsers.add("chenchang@vcredit.com");
		toUsers.add("chenchang@vcredit.com");
		dto.setToUser(toUsers);
		eventMessageGateway.publishEvent(dto);

		//允许重复收件人		
		//		dto.setToUser(toUsers,true);
		//		eventMessageGateway.publishEvent(dto);

	}

	/**
	 * 发布事件,触发消息
	 */
	@Test
	public void testPublishAlertEvent() {
		try {
			AlertDto dto = new AlertDto();

			AccountMessageTemplateData accountMessageTemplateData = new AccountMessageTemplateData();
			accountMessageTemplateData.setProjectId("ProjectId_20150203");

			dto.setAccountMessageTemplateData(accountMessageTemplateData);
			dto.setAccountMessageTemplateEnum(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI);

			//站内信_用户Id
			dto.setToUser("22222");
			eventMessageGateway.publishEvent(dto);

			//站内信_用户Ids
			List<String> toUsers = new ArrayList<String>();
			toUsers.add("11111");
			toUsers.add("33333");
			dto.setToUser(toUsers);
			eventMessageGateway.publishEvent(dto);

			//站内信_用户Ids
			List<Long> toUsersLong = new ArrayList<Long>();
			toUsersLong.add(55555L);
			toUsersLong.add(55556L);
			dto.setToUser(toUsersLong);
			eventMessageGateway.publishEvent(dto);

			//站内信_用户 Account
			Account a = accountRepository.findByMobile("13814878856");
			dto.setToUser(a);
			eventMessageGateway.publishEvent(dto);

			//站内信_用户 Accounts
			List<Account> toAccounts = new ArrayList<Account>();
			Account b = accountRepository.findByMobile("13814878857");
			Account c = accountRepository.findByMobile("13814878856");
			toAccounts.add(b);
			toAccounts.add(c);
			dto.setToUser(toAccounts);
			eventMessageGateway.publishEvent(dto);

			//邮件
			dto.setToUser("chenchang@vcredit.com");
			dto.setAccountMessageTemplateEnum(AccountMessageTemplateEnum.JIE_KUAN_XIE_YI);
			eventMessageGateway.publishEvent(dto);

		} catch (Exception e) {
			System.out.println(e.getCause());
			System.out.println(e.getMessage());
		}
	}
}
