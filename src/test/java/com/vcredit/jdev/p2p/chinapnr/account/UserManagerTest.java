package com.vcredit.jdev.p2p.chinapnr.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.vcredit.jdev.p2p.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class UserManagerTest {
	@Autowired
	UserManager userManager;

	@Test
	public void testBgBindCard() {
		System.out.println(userManager.bgBindCard("6000060000810920", "6217001210031548222", "CCB", "0031", "3100", "", "Y"));
	}
	
	@Test
	public void testGetUserRegisterParam() {
		System.out.println(userManager.getUserRegisterParam("123456", "测试", "00", "434582194129485692", "12345678901", "dd@163.com", "123", 1));
	}
	
	@Test
	public void testGetUserBindCardParam() {
		System.out.println(userManager.getUserBindCardParam("6000060000810920"));
	}
	
	@Test
	public void testGetUserLoginParam() {
		System.out.println(userManager.getUserLoginParam("6000060000810920"));
	}
	
	

}
