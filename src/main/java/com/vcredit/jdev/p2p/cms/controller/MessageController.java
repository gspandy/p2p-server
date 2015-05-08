package com.vcredit.jdev.p2p.cms.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.cms.repository.MessageRepository;
import com.vcredit.jdev.p2p.cms.service.MessageManager;
import com.vcredit.jdev.p2p.entity.Message;

/**
 * @ClassName: MessageController
 * @Description: 富文本CONTROLLER
 * 
 *               活动发布I,查看ListPager,失效U
 * @author ChenChang
 * @date 2014年12月24日 下午3:15:26
 */
@Path("/message")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON)
public class MessageController {

	@Autowired
	MessageManager messageManager;

	@Autowired
	MessageRepository messageRepository;

	/**
	 * 发布一个公告
	 * 
	 * @param message
	 *            公告ENTITY
	 * @return
	 */
	@POST
	@Path("/publishMessage")
	public Response publishMessage(Message message) {
		try {
			messageManager.publishMessage(message);
			return Response.successResponse();
		} catch (Exception e) {
			// 发布消息失败
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_696, ResponseConstants.CommonMessage.RESPONSE_MSG_696);
		}
	}

	/**
	 * 查询一个公告
	 * 
	 * @param msgId
	 *            公告Id
	 * @return
	 */
	@POST
	@Path("/queryMessageOne")
	public Response queryMessageOne(Long msgId) {
		try {
			//			Message message = messageManager.queryMessageOne(msgId);
			Message message = messageRepository.findOnePublishedAnnouncement(msgId);
			return Response.successResponse(message);
			
		} catch (Exception e) {
			e.printStackTrace();
			// 查找一条公告失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_699, ResponseConstants.CommonMessage.RESPONSE_MSG_699);
		}
	}

	/**
	 * 查询所有公告
	 * 
	 * @return
	 */
	@POST
	@Path("/queryMessageAll")
	public Response queryMessageAll() {
		try {
			//	List<Message> listMsg = messageManager.queryMessageAll();

			List<Message> listMsg = messageRepository.findAllPublishedAnnouncement();
			return Response.successResponse(listMsg);
		} catch (Exception e) {
			e.printStackTrace();
			// 查找所有公告失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_698, ResponseConstants.CommonMessage.RESPONSE_MSG_698);
		}
	}

	/**
	 * 删除一个公告
	 * 
	 * @param msgId
	 *            公告Id
	 * @return
	 */
	@POST
	@Path("/deleteMessageOne")
	public Response deleteMessageOne(Long msgId) {
		try {
			messageManager.deleteMessage(msgId);
			return Response.successResponse();
		} catch (Exception e) {
			e.printStackTrace();
			// 删除一条公告失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_697, ResponseConstants.CommonMessage.RESPONSE_MSG_697);
		}
	}
}
