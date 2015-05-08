package com.vcredit.jdev.p2p.alert;

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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountRemindControllerTest {
	@Autowired
	TestUtils testUtils;

	static Client client = null;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
		client.register(JacksonJsonProvider.class);
	}

	@Test
	public void testQueryAccountRemindByUserId() {
		WebTarget target = client.target(
				testUtils.getEmbeddedServletContainerBaseURL()
						+ "/data/ws/rest").path(
				"/accountRemind/queryAccountRemindByUserId");

		Response response = target.request(MediaType.APPLICATION_JSON).post(
				Entity.json(""));

		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testQueryAccountRemindByUserId");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}

	@Test
	public void testUpdateAccountRemindOne() {

		WebTarget target = client.target(
				testUtils.getEmbeddedServletContainerBaseURL()
						+ "/data/ws/rest").path(
				"/accountRemind/updateAccountRemindOne");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemRemindOptionSequence", 4L);
		map.put("remindStatus", 0);

		Response response = target.request(MediaType.APPLICATION_JSON).post(
				Entity.json(map));

		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testUpdateAccountRemindOne");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);

		assertTrue(statusCode == 200);
	}
}
