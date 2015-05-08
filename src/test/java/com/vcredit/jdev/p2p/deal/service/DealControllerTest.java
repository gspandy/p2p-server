package com.vcredit.jdev.p2p.deal.service;

import static org.junit.Assert.assertTrue;

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
public class DealControllerTest {

	@Autowired
	TestUtils testUtils;

	static Client client = null;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
		client.register(JacksonJsonProvider.class);
	}

	@Test
	public void testFindAccountOrderList() {
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/findAccountOrderList");
		Map<String, Object> map = new HashMap<String, Object>();

		List<Integer> status = new ArrayList<Integer>();
		status.add(new Integer(3));
		status.add(new Integer(4));

		List<Integer> types = new ArrayList<Integer>();
		types.add(new Integer(2));
		types.add(new Integer(3));
		types.add(new Integer(4));
		types.add(new Integer(13));
		types.add(new Integer(14));
		types.add(new Integer(15));
		types.add(new Integer(21));
		types.add(new Integer(22));
		types.add(new Integer(23));
		types.add(new Integer(27));
		types.add(new Integer(29));

		map.put("dateFrom", "2015-03-01T00:00:00.000Z");
		map.put("dateTo", "2015-04-15T00:00:00.000Z");
		map.put("currentPage", 0);
		map.put("pageNumber", 10);
		map.put("status", status);
		map.put("types", types);

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@testFindAccountOrderList");
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
	}
}
