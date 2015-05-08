package com.vcredit.jdev.p2p.merchant.modal;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
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
import com.vcredit.jdev.p2p.base.P2pUtilTest;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountWelfareControllerTest {
	@Autowired
    TestUtils testUtils;
	@Autowired
    P2pUtilTest p2pUtilTest;
	public String REST_SERVICE_URL;
	
	static Client client = null;
	@Autowired
	private MerchantManager merchantManager;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private InvestmentRepository investmentRepository;
	@Autowired
	private LoanDataRepository loanDataRepository;

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
	 * 检索红包
	 */
	@Test
	public void testQueryAccountWelfareByAccount() {
		Cookie cookies = p2pUtilTest.getCurrentCookie(client, testUtils, REST_SERVICE_URL,null,null);
		//具体业务
		String path = "/accountWelfare/queryAccountWelfareByAccountAndStatus";
		WebTarget target = client.target(REST_SERVICE_URL).path(path);
		Map body = new HashMap();
		body.put("page", "1");
		body.put("size", "2");
		Builder builder=target.request(MediaType.APPLICATION_JSON);
		builder.cookie(cookies);
		
		Response response = builder.post(Entity.json(body));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@" + content);
		System.out.println("retrieveFile1() statusCode:" + statusCode);
		assert (statusCode == 200);
	}
	
	/**
	 * 检索红包根据当前用户下的红包状态
	 */
	@Test
	public void testQueryAccountWelfareByAccountAndStatus() {
		Cookie cookies = p2pUtilTest.getCurrentCookie(client, testUtils, REST_SERVICE_URL,null,null);
		//具体业务
		String path = "/accountWelfare/queryAccountWelfareByAccountAndStatus";
		WebTarget target = client.target(REST_SERVICE_URL).path(path);
		Map body = new HashMap();
		body.put("welfareStatus", "2");
		body.put("page", "1");
		body.put("size", "2");
		Builder builder=target.request(MediaType.APPLICATION_JSON);
		builder.cookie(cookies);
		Response response = builder.post(Entity.json(body));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@" + content);
		System.out.println("retrieveFile1() statusCode:" + statusCode);
		assert (statusCode == 200);
	}
	
	/**
	 * 获得红包
	 */
	@Test
	public void testGiveWelfare() {
		Cookie cookies = p2pUtilTest.getCurrentCookie(client, testUtils, REST_SERVICE_URL,null,null);
		//具体业务
		String path = "/accountWelfare/giveWelfare";
		WebTarget target = client.target(REST_SERVICE_URL).path(path);
		Map body = new HashMap();
		body.put("welfareId", "4");//福利id
		body.put("welfareSource", "2");//福利来源
		Builder builder=target.request(MediaType.APPLICATION_JSON);
		builder.cookie(cookies);
		
		Response response = builder.post(Entity.json(body));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@" + content);
		System.out.println("retrieveFile1() statusCode:" + statusCode);
		assert (statusCode == 200);
	}
	
	/**
	 * 红包兑换
	 */
	@Test
	public void testExchangeAccountWelfare() {
//		String path = "/accountWelfare/exchangeAccountWelfare";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Map body = new HashMap();
//		body.put("accountWelfareId", "4");//兑付账号
//		body.put("welfareConsume", "2");//支付方式
//		
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(body));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		System.out.println("retrieveFile1() statusCode:" + statusCode);
//		assert (statusCode == 200);
	}
}
