package com.vcredit.jdev.p2p.deal.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

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
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class BondPackageRepurchaseManagerTest {

	private static final String TYPE_JSON = "application/json";
	private final String REST_SERVICE_URL = "http://localhost:8080/p2p/data/ws/rest";

	static Client client = null;

	@Autowired
	TestUtils testUtils;

	@Autowired
	private BondPackageRepurchaseManager bondPackageRepurchaseManager;

	@BeforeClass
	public static void init() {
		//register(JacksonFeatures.class).
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);

	}

	@Test
	public void testPayoffList() {

		//TODO
		//bondPackageRepurchaseManager.searchRepurchaseList();

	}

}
