package com.vcredit.jdev.p2p.chinapnr.util;

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
public class SignUtilTest {
	@Autowired
	SignUtils signUtils;

	@Test
	public void testGetMerPriKeyNamePath() {
		String merPriKeyNamePath = signUtils.getMerPriKeyNamePath();
	}
	
	@Test
	public void testGetMerPubKeyNamePath() {
		String merPubKeyNamePath = signUtils.getMerPubKeyNamePath();
	}

}
