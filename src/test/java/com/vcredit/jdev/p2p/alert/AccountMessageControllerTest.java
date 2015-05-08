package com.vcredit.jdev.p2p.alert;

import java.util.ArrayList;
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

import static org.junit.Assert.*;

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
public class AccountMessageControllerTest {
	@Autowired
	TestUtils testUtils;

	static Client client = null;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
		client.register(JacksonJsonProvider.class);
	}

	@Test
	public void testDeleteAccountMessageAll() {
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountMessage/deleteAccountMessageAll");
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testDeleteAccountMessageAll");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testDeleteAccountMessages() {
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountMessage/deleteAccountMessages");

		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Long> accountMessages = new ArrayList<Long>();

		accountMessages.add(110050L);
		accountMessages.add(110051L);

		map.put("accountMessages", accountMessages);

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testDeleteAccountMessages");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testUpdateAccountMessagesRead() {
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountMessage/updateAccountMessagesRead");

		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Long> accountMessages = new ArrayList<Long>();

		accountMessages.add(110050L);
		accountMessages.add(110051L);

		map.put("accountMessages", accountMessages);

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testUpdateAccountMessagesRead");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testQueryAccountMessageCount4SiteMsg() {
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountMessage/queryAccountMessageCount4SiteMsg");
		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(""));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testQueryMessageOne");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testQueryMessageOne() {

		Long accountMessageId = 71L;
		Long userId = 22222L;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accountMessageId", accountMessageId);
		map.put("userId", userId);

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountMessage/queryAccountMessageOne");

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));

		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testQueryMessageOne");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testQueryAccountMessageAllByUserId() {
		Long userId = 22222L;

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountMessage/queryAccountMessageAllByUserId");

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(userId));

		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testQueryAccountMessageAllByUserId");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testQueryAccountMessageByUserIdDate() {

		Long userId = 22222L;
		String beginDate = "2015-01-13T00:17:09.425Z";
		String endDate = "2015-01-15T03:17:09.425Z";

		beginDate = "Tue Jan 12 2015 00:00:00 GMT+0800 (中国标准时间)";
		endDate = "Tue Jan 13 2015 13:20:21 GMT+0800 (中国标准时间)";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountMessage/queryAccountMessageAllByUserIdDate");

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));

		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testQueryAccountMessageByUserIdDate");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testQueryAccountMessageByUserIdDateNULL() {

		Map<String, Object> map = new HashMap<String, Object>();

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/accountMessage/queryAccountMessageAllByUserIdDate");

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));

		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testQueryAccountMessageByUserIdDate");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

}
