package com.vcredit.jdev.p2p.capital.modal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vcredit.jdev.p2p.Application;
import com.vcredit.jdev.p2p.dto.DepositCapitalDto;
import com.vcredit.jdev.p2p.dto.SpringIntegrationMessageDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.SpringIntegrationMessageEnum;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CapitalPlatformControllerTest {
	@Autowired
    TestUtils testUtils;
	public String REST_SERVICE_URL;
	
	static Client client = null;

	@Autowired
	private CapitalAccountManager cAccount;
	@Autowired
	private CapitalPlatformManager cPlat;
	@Autowired
	private EventMessageGateway eventMessageGateway;

	@BeforeClass
	public static void init() {
		//register(JacksonFeatures.class).
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);
	}
	@Before
	public void befor(){
		REST_SERVICE_URL = testUtils.getEmbeddedServletContainerBaseURL()+ "/data/ws/rest";
	}
	
	/**
	 * 
	 */
	@Test
	public void testPublishEvent() {
//		SpringIntegrationMessageDto<List<Long>> dto=new SpringIntegrationMessageDto<List<Long>>();
//		dto.setMessage(SpringIntegrationMessageEnum.AMOUNTSPLIT);
//		List<Long> l=new ArrayList<Long>();
//		dto.setObj(l);
//		eventMessageGateway.publishEvent(dto);
	}

}
