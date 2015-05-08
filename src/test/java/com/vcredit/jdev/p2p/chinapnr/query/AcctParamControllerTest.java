package com.vcredit.jdev.p2p.chinapnr.query;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

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
public class AcctParamControllerTest {
	@Autowired
	TestUtils testUtils;

	static Client client = null;

	@BeforeClass
	public static void init() {
		//register(JacksonFeatures.class).
		client = ClientBuilder.newBuilder().build();
		client.register(JacksonJsonProvider.class);
	}
	
	@Test
	public void testRegister() {

		WebTarget registersumbitTarget = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest/acctparam/registersumbit");
		Map<String, String> content = new HashMap<String, String>();
		content.put("usrId", "122");
		content.put("usrMp", "18621180023");
		Response registersumbitResponse = registersumbitTarget.request(MediaType.WILDCARD_TYPE).post(Entity.entity(content, MediaType.APPLICATION_JSON_TYPE));
		Map<String, NewCookie> registersumbitCookies = registersumbitResponse.getCookies();
		assert (registersumbitResponse.getStatus() == 200);

//		String pageCaptcha = "abc";
//		WebTarget validateTarget = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/sudoor/captcha/validate")
//				.queryParam("_captcha", pageCaptcha);
//		Builder validateBuilder = validateTarget.request(MediaType.WILDCARD_TYPE);
//		for (Iterator iterator = captchaImgCookies.values().iterator(); iterator.hasNext();) {
//			Cookie cookie = (Cookie) iterator.next();
//			validateBuilder.cookie(cookie);
//		}
//		Response validateResponse = validateBuilder.get();
//		assert (validateResponse.getStatus() == 200);
//
//		boolean res = validateResponse.readEntity(boolean.class);
//		assert (res == false);
	}
}

