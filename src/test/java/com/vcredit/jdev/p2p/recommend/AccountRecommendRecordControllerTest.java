package com.vcredit.jdev.p2p.recommend;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountRecommendRecordControllerTest {
	@Autowired
	TestUtils testUtils;

	static Client client = null;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
		client.register(JacksonJsonProvider.class);
	}

	@Test
	public void testQueryAccountRecommendRecord() {
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountRecommendRecord/queryAccountRecommendRecord");
		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(""));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testQueryAccountRecommendRecord");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testQueryAccountRecommendRecordCount() {
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountRecommendRecord/queryAccountRecommendRecordCount");
		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(""));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testQueryAccountRecommendRecordCount");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testQueryAccountRecommendRecordCount4Admin() {
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountRecommendRecord/queryAccountRecommendRecordCount4Admin");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aid", "22222");

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testQueryAccountRecommendRecordCount4Admin");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testSendMailRecommend() {
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountRecommendRecord/sendMailRecommend");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("toUser", "chenchang@vcredit.com");
		map.put("sendSubject", "sendSubject");
		map.put("sendContent", "sendContent");

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testSendMailRecommend");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}
}
