package com.vcredit.jdev.p2p.account.controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

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
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountControllerTest {
	
	@Autowired
    TestUtils testUtils;
	
	public String REST_SERVICE_URL;

	
    private static final String TYPE_XML = "application/xml";  
    private static final String TYPE_JSON = "application/json";  
	
//    @Test
//	public void testLogin() {
//    	//(String userName,String password,String verificationCode)
//		WebClient client = WebClient.create(REST_SERVICE_URL); 
//        client.path("/account/login","123456").accept(  
//        		TYPE_JSON).type(TYPE_JSON).query("userName", "test").query("password", "12345678").query("verificationCode", "654286315");    
//        String response = client.get(String.class);  
//        System.out.println(response);  
//	}

	static Client client = null;


	@BeforeClass
	public static void init() {
		//register(JacksonFeatures.class).
		client = ClientBuilder.newBuilder().build();
		client.register(JacksonJsonProvider.class);
	}
	
	
	@Before
	public void setUp() throws Exception {
		REST_SERVICE_URL = testUtils.getEmbeddedServletContainerBaseURL()+ "/data/ws/rest";
	}
	
	@Test
	public void testLogin(){
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/login");
		Map body = new HashMap();
		body.put("userName", "13916143511");
		body.put("password", "123456");
		body.put("verificationCode", "test");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}

	@Test
	public void testAccountIsExist() {
		
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/accountisexist");
				//.queryParam ("userName", "testfsd");
		Map body = new HashMap();
		body.put("userName", "testfsd");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}

	@Test
	public void testRegister() {
		//for (int i = 100; i < 100; i++) {
			Map body = new HashMap();
			body.put("userName", "adusers"+1112);
			body.put("password", "123456");
			body.put("mobile", "1391611"+1112);
			body.put("safety", "1");
			WebTarget target = client.target(REST_SERVICE_URL).path("/account/register");
			String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
			System.out.println(response);
		//}
	}

	@Test
	public void testSendMobileVerificationCode() {
//		Map body = new HashMap();
//		body.put("mobile", "13916145186");
//		WebTarget target = client.target(REST_SERVICE_URL).path("/account/sendMobileVerificationCode");
//		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
//		System.out.println(response);
	}

	@Test
	public void testModifyAccountMobile() {
		Map body = new HashMap();
		body.put("mobile", "13916145111");
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/modifyAccountMobile");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}

	@Test
	public void testModifyAccountEmail() {
		Map body = new HashMap();
		body.put("email", "13@qq.com");
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/modifyAccountEmail");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}

	@Test
	public void testFindPassword() {
		//findPassword
		Map body = new HashMap();
		body.put("userName", "adusers1111");
		body.put("password", "654321");
		body.put("safety", "2");
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/findPassword");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}

	@Test
	public void testResetPassword() {
		Map body = new HashMap();
		body.put("oldpwd", "123456789");
		body.put("newpwd", "987654321");
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/resetPassword");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}

	@Test
	public void testSaveAccountQuestionAnswer() {
//		List list = new ArrayList();
//		for (int i = 1; i < 4; i++) {
//			Map body = new HashMap();
//			body.put("qid", i);
//			body.put("answer", "080540833");
//			list.add(body);
//		}
//		Map dy = new HashMap();
//		dy.put("data", list);
//		WebTarget target = client.target(REST_SERVICE_URL).path("/account/saveAccountQuestionAnswer");
//		String response = target.request(TYPE_JSON).post(Entity.json(dy),String.class);
//		System.out.println(response);
	}
	
	@Test
	public void testGetQuestionList() {
		Map body = new HashMap();
		body.put("userName", "adminn");
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/getQuestionList");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}

	@Test
	public void testCheckAccountIsSetQuestion() {
		Map body = new HashMap();
		body.put("userName", "adminn");
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/checkAccountIsSetQuestion");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}

	@Test
	public void testCheckAccountQuestionAnswer() {
		Map body = new HashMap();
		body.put("qid", "3");
		body.put("answer", "080540833");
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/checkAccountQuestionAnswer");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}

	public void testModifyAccountInfo() {
		Map body = new HashMap();
		body.put("attr", "3");
		body.put("value","1");
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/info");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}
	
	@Test
	public void testmodifyProvinceAndCity() {
		Map body = new HashMap();
//		String provinceCode  = (String) paramMap.get("provinceCode");
//		String cityCode  = (String) paramMap.get("cityCode");
		body.put("provinceCode","120000");
		body.put("cityCode","");
		WebTarget target = client.target(REST_SERVICE_URL).path("/account/modifyProvinceAndCity");
		String response = target.request(TYPE_JSON).post(Entity.json(body),String.class);
		System.out.println(response);
	}

	public void testLogin3() {
	}
	
	@Test
	public void testLogin4() {
//		WebClient client = WebClient.create(REST_SERVICE_URL); 
//        client.path("/account/login4").accept(  
//        		MediaType.APPLICATION_JSON);    
//        String response = client.get(String.class);  
//        System.out.println(response);  
	}
}
