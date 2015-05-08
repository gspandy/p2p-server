package com.vcredit.jdev.p2p.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.util.TestUtils;
@Component
public class P2pUtilTest {

	public Cookie getCurrentCookie(Client client,TestUtils testUtils,String REST_SERVICE_URL,String userName,String password) {
		//获得session
		WebTarget captchaImgTarget = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/app/sudoor/captcha-image.html");
		Response captchaImgResponse = captchaImgTarget.request(MediaType.WILDCARD_TYPE).get();
		Map<String, NewCookie> captchaImgCookies = captchaImgResponse.getCookies();
		Cookie cookies = null;//cookies
		for (Iterator iterator = captchaImgCookies.values().iterator(); iterator.hasNext();) {
			cookies = (Cookie) iterator.next();
		}

		//登陆
		String path1 = "/account/login";
		WebTarget target1 = client.target(REST_SERVICE_URL).path(path1);
		Map<String, Object> c1 = new HashMap<String, Object>();
		if(userName==null){
			userName="adusers1";
		}
		if(password==null){
			password="111111";
		}
		c1.put("userName", userName);
		c1.put("password", password);
		c1.put("verificationCode", "1");
		Invocation.Builder build1 = target1.request(MediaType.APPLICATION_JSON);
		build1.cookie(cookies);
		build1.post(Entity.json(c1));
		return cookies;
	}

	@Test
	public void testGetAddressByIP() {
		//		System.out.println(P2pUtil.getAddressByIP("10.100.12.34").toString());
		//		System.out.println(P2pUtil.getAddressByIP("117.89.35.58").toString());
		//		System.out.println(P2pUtil.getAddressByIP("114.80.68.23").toString());
		//		System.out.println(P2pUtil.getAddressByIP("127.0.0.1").toString());
	}

}
