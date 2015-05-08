package com.vcredit.jdev.p2p.deal.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vcredit.jdev.p2p.Application;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class DealServiceTest {

	private static final String TYPE_JSON = "application/json";
	//	private final String REST_SERVICE_URL = "http://localhost:8080/p2p/data/ws/rest";

	static Client client = null;

	@Autowired
	private TestUtils testUtils;

	@Autowired
	private DealRepository autoInvestRepository;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);

	}

	//@Test
	public void manualInvest() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("investmentSequence", "8");
		body.put("amount", "100");
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/manualInvest");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	/*
	 * @Test public void PayoffNormal() { WebTarget target =
	 * client.target(testUtils.getEmbeddedServletContainerBaseURL() +
	 * "/data/ws/rest").path("/deal/payoffNormal/1/1"); String response =
	 * target.request(TYPE_JSON).get(String.class);
	 * System.out.println(response); }
	 */
	@Test
	public void releaseCash() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("investmentSequence", "7");
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/releaseCash");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void autoWithdraw() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("investmentSequence", "7");
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/autoWithdraw");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void withdraw() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("amount", "100");
		body.put("bankCardNumber", "620000000000");
		body.put("bankCode", "7");

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/withdraw");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void topup() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("amount", "100");
		body.put("bankCardNumber", "620000000000");
		body.put("bankCode", "7");

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/topup");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void selfHelpInvest() {
		Map<String, Object> body = new HashMap<String, Object>();

		body.put("perMaxAmount", "100");
		body.put("period", "6,12,24,36");
		body.put("creditLevel", "1,2,3,4,5");

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/selfHelpInvest");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void oneKeyInvest() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("amount", "100");
		body.put("bankCardNumber", "620000000000");
		body.put("bankCode", "7");

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/oneKeyInvest");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void colseAutoInvest() {
		Map<String, Object> body = new HashMap<String, Object>();

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/colseAutoInvest");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void useRedEnvelope() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("amount", "100");
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/consumeRedEnvelope");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void wdzh02MyInvest() {
		Map<String, Object> body = new HashMap<String, Object>();

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/wdzh02MyInvest");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void wdzh02MyInvestList() {
		Map<String, Object> body = new HashMap<String, Object>();

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/wdzh02MyInvestList");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void wytz0101List() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("investmentSequence", "8");
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/wytz0101List");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void manualInvestChk() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("investmentSequence", "8");

		body.put("amount", "100");
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/manualInvestChk");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void calcBenifitMonth() {
		Map<String, Object> body = new HashMap<String, Object>();

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/calcProfitMonth");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void isCurrentAccountInvested() {
		Map<String, Object> body = new HashMap<String, Object>();

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/isCurrentAccountInvested");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}
}
