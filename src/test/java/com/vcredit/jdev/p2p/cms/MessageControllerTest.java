package com.vcredit.jdev.p2p.cms;

import java.util.Date;
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

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vcredit.jdev.p2p.Application;
import com.vcredit.jdev.p2p.entity.Message;
import com.vcredit.jdev.p2p.enums.MessageIssueTypeEnum;
import com.vcredit.jdev.p2p.enums.MessageStatusEnum;
import com.vcredit.jdev.p2p.enums.MessageTypeEnum;
import com.vcredit.jdev.p2p.util.TestUtils;

/**
 * @ClassName: MessageManagerTest
 * @Description:
 * @author ChenChang
 * @date 2014年12月24日 下午4:45:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MessageControllerTest {

	@Autowired
	TestUtils testUtils;

	static Client client = null;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
		client.register(JacksonJsonProvider.class);
	}

	@Test
	public void testPublishMessage() {

		System.out.println("@BEGIN testPublishMessage");
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/message/publishMessage");

		//		Map<String, Object> msg = new HashMap<String, Object>();
		//		msg.put("messageType", MessageTypeEnum.Advertisement.getCode());
		//		msg.put("messageTitle", new Date().toString());
		//		msg.put("messageContent", "messageContent");
		//		msg.put("messageStatus", MessageStatusEnum.PUBLISHED.getCode());
		//		msg.put("messageCreateDate", new Date());
		//		msg.put("messageIssueDate", new Date());
		//		msg.put("messageIssueType", MessageIssueTypeEnum.MANUAL.getCode());
		//		msg.put("resourceSequence", 1);
		//		msg.put("messageIndex", 1);
		//		msg.put("messageSource", "CCN");

		Message msg = new Message();
		msg.setMessageType(MessageTypeEnum.Advertisement.getCode());
		msg.setMessageTitle(new Date().toString());
		msg.setMessageContent("messageContent");
		msg.setMessageStatus(MessageStatusEnum.PUBLISHED.getCode());
		msg.setMessageCreateDate(new Date());
		msg.setMessageIssueDate(new Date());
		msg.setMessageIssueType(MessageIssueTypeEnum.MANUAL.getCode());
		msg.setResourceSequence(1);
		msg.setMessageIndex(1);
		msg.setMessageSource("CCN");
		msg.setUserName("ADMIN");

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(msg));

		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
		System.out.println("@END testPublishMessage");
	}

	@Test
	public void testQueryMessageOne() {
		System.out.println("@BEGIN testQueryMessageOne");
		Long msgId = 9L;

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/message/queryMessageOne");

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(msgId));

		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
		System.out.println("@END testQueryMessageOne");
	}

	@Test
	public void testQueryMessageAll() {
		System.out.println("@BEGIN testQueryMessageAll");
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/message/queryMessageAll");

		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(""));

		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@content" + content);
		System.out.println("@statusCode:" + statusCode);
		assertTrue(statusCode == 200);
		System.out.println("@END testQueryMessageAll");
	}

	// @Test
	// public void testDeleteMessageOne() {
	// Long msgId = 1L;
	// WebTarget target = client
	// .target(testUtils.getEmbeddedServletContainerBaseURL()
	// + "/data/ws/rest")
	// .path("/message/deleteMessageOne/{msgId}")
	// .resolveTemplate("msgId", msgId);
	// ;
	//
	// Response response = target.request(MediaType.APPLICATION_JSON).get();
	// int statusCode = response.getStatus();
	// String content = response.readEntity(String.class);
	// System.out.println("@content" + content);
	// System.out.println("@statusCode:" + statusCode);
	// assert (statusCode == 200);
	//
	// }
}
