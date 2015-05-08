package com.vcredit.jdev.p2p.finance;

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
public class FinanceControllerTest {

	@Autowired
	TestUtils testUtils;

	static Client client = null;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
		client.register(JacksonJsonProvider.class);
	}

	@Test
	public void testQueryYieldComparison() {
		System.out.println("@BEGIN testQueryYieldComparison");
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/finance/queryYieldComparison");

		Map<String, Object> d = new HashMap<String, Object>();
		d.put("pv", 10000);
		d.put("nper", 12);
		d.put("isRoll", true);
		d.put("rate", 12);

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(d));

		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);

		d.clear();
		d.put("pv", 10000);
		d.put("nper", 12);
		d.put("rate", 0.13);
		//d.put("isRoll", false);
		response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(d));
		content = response.readEntity(String.class);
		System.out.println("@content2" + content);

		System.out.println("@END testQueryYieldComparison");
	}
}
