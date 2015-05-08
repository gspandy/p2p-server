package com.vcredit.jdev.p2p.capital.modal;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.P2pUtilTest;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountBankCardControllerTest {

	@Autowired
    TestUtils testUtils;
	@Autowired
    P2pUtilTest p2pUtilTest;
	public String REST_SERVICE_URL;
	static Client client = null;
	@Autowired
	private AccountBankCardManager accountBankCardManager;
	@Autowired
	private P2pSessionContext p2pSessionContext;
	
	@BeforeClass
	public static void init() {
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);
	}
	
	@Before
	public void befor(){
		REST_SERVICE_URL = testUtils.getEmbeddedServletContainerBaseURL()+ "/data/ws/rest";
	}

	/**
	 * 设置默认卡
	 */
	@Test
	public void testQueryCapitalInfo() {
		Cookie cookies = p2pUtilTest.getCurrentCookie(client, testUtils, REST_SERVICE_URL,null,null);
		//具体业务
		String path = "/accountBankCard/optionsDefaultDebit";
		WebTarget target = client.target(REST_SERVICE_URL).path(path);
		Map<String,Object> c=new HashMap<String,Object>();
		c.put("bankCard", "6217001212354568546");
		Invocation.Builder build = target.request(MediaType.APPLICATION_JSON);
		build.cookie(cookies);
		Response response = build.post(Entity.json(c));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@" + content);
		System.out.println("retrieveFile1() statusCode:" + statusCode);
		assert (statusCode == 200);
	}
}
