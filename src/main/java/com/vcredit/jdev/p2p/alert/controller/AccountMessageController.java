package com.vcredit.jdev.p2p.alert.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.alert.repository.AccountMessageRepository;
import com.vcredit.jdev.p2p.alert.service.AccountMessageManager;
import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.entity.AccountMessage;
import com.vcredit.jdev.p2p.enums.MessageReadStatusEnum;
import com.vcredit.jdev.p2p.util.DateUtil;

/**
 * @ClassName: AccountMessageController
 * @Description:
 * @author ChenChang
 * @date 2014年12月26日 下午2:43:23
 */
@Path("/accountMessage")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON)
public class AccountMessageController {
	@Autowired
	AccountMessageManager accountMessageManager;
	@Autowired
	AccountMessageRepository accountMessageRepository;
	@Autowired
	private P2pSessionContext p2pSessionContext;

	/**
	 * 删除用户的所有消息
	 * 
	 * @param paramMap
	 * @return
	 */
	@GET
	@Path("/deleteAccountMessageAll")
	public Response deleteAccountMessageAll() {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}
			accountMessageRepository.deleteAccountMessageAll(aid);
			return Response.successResponse();
		} catch (Exception e) {
			e.printStackTrace();
			// 删除用户的所有消息
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_789, ResponseConstants.CommonMessage.RESPONSE_MSG_789);
		}
	}

	/**
	 * 删除用户的多个消息
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/deleteAccountMessages")
	public Response deleteAccountMessages(Map<String, Object> paramMap) {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			@SuppressWarnings("rawtypes")
			List msgs = (ArrayList) (paramMap.get("accountMessages"));

			Long accountMessageId = null;
			AccountMessage accountMessage = null;

			for (int i = 0; i < msgs.size(); i++) {
				accountMessageId = Long.valueOf(msgs.get(i).toString());
				accountMessage = accountMessageManager.queryAccountMessageOne(accountMessageId);

				if (null != accountMessage && aid.equals(accountMessage.getAccountSequnece())) {
					accountMessageManager.deleteAccountMessage(accountMessageId);
				}
			}
			return Response.successResponse();
		} catch (Exception e) {
			e.printStackTrace();
			// 删除用户的多个消息失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_793, ResponseConstants.CommonMessage.RESPONSE_MSG_793);
		}

	}

	/**
	 * 更新用户的多个消息为已读
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/updateAccountMessagesRead")
	public Response updateAccountMessagesRead(Map<String, Object> paramMap) {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			@SuppressWarnings("rawtypes")
			List msgs = (ArrayList) (paramMap.get("accountMessages"));

			Long accountMessageId = null;
			AccountMessage accountMessage = null;

			for (int i = 0; i < msgs.size(); i++) {
				accountMessageId = Long.valueOf(msgs.get(i).toString());
				accountMessage = accountMessageManager.queryAccountMessageOne(accountMessageId);

				// 如果是消息所有者读取,更新为已读
				if (null != accountMessage && aid.equals(accountMessage.getAccountSequnece())) {
					accountMessage.setMessageReadStatus(MessageReadStatusEnum.READ.getCode());
					accountMessageManager.saveAccountMessageOne(accountMessage);
				}
			}
			return Response.successResponse();
		} catch (Exception e) {
			e.printStackTrace();
			// 更新用户的多个消息为已读失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_793, ResponseConstants.CommonMessage.RESPONSE_MSG_793);
		}

	}

	/**
	 * 查询用户站内信消息总数
	 * 
	 * @return
	 */
	@POST
	@Path("/queryAccountMessageCount4SiteMsg")
	public Response queryAccountMessageCount4SiteMsg() {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			Long siteMsgCount = 0L;
			siteMsgCount = accountMessageManager.queryAccountMessageCount4SiteMsg(aid);
			return Response.successResponse(siteMsgCount);
		} catch (Exception e) {
			e.printStackTrace();
			// 查询用户站内信消息总数失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_792, ResponseConstants.CommonMessage.RESPONSE_MSG_792);
		}

	}

	/**
	 * 查询一条用户消息
	 * 
	 * @param msgId
	 *            公告Id
	 * @return
	 */
	@POST
	@Path("/queryAccountMessageOne")
	public Response queryAccountMessageOne(Map<String, Object> paramMap) {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			Long accountMessageId = null;
			Object m = paramMap.get("accountMessageId");

			if (null == m) {
				// 用户消息ID为空
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_795, ResponseConstants.CommonMessage.RESPONSE_MSG_795);
			}

			accountMessageId = Long.parseLong(paramMap.get("accountMessageId").toString());

			AccountMessage accountMessage = null;

			accountMessage = accountMessageManager.queryAccountMessageOne(accountMessageId);
			// 如果是消息所有者读取,更新为已读
			if (null != accountMessage && aid.equals(accountMessage.getAccountSequnece())) {
				accountMessage.setMessageReadStatus(MessageReadStatusEnum.READ.getCode());
				accountMessageManager.saveAccountMessageOne(accountMessage);
			}

			return Response.successResponse(accountMessage);
		} catch (Exception e) {
			e.printStackTrace();
			// 查询一条用户消息失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_799, ResponseConstants.CommonMessage.RESPONSE_MSG_799);
		}
	}

	/**
	 * 查询所有用户消息
	 * 
	 * @return
	 */
	@POST
	@Path("/queryAccountMessageAll")
	public Response queryMessageAll() {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			List<AccountMessage> listMsg = accountMessageManager.queryAccountMessageAll();
			return Response.successResponse(listMsg);
		} catch (Exception e) {
			e.printStackTrace();
			// 查询所有用户消息失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_798, ResponseConstants.CommonMessage.RESPONSE_MSG_798);
		}
	}

	/**
	 * 查询一个用户所有消息
	 * 
	 * @return
	 */
	@POST
	@Path("/queryAccountMessageAllByUserId")
	public Response queryAccountMessageAllByUserId(Long userId) {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			List<AccountMessage> listAccountMessage = null;
			listAccountMessage = accountMessageManager.queryAccountMessageAllByUserId(aid);
			return Response.successResponse(listAccountMessage);

		} catch (Exception e) {
			e.printStackTrace();
			// 查询一个用户所有消息失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_797, ResponseConstants.CommonMessage.RESPONSE_MSG_797);
		}
	}

	/**
	 * 查询一个用户所有消息_用户ID&日期
	 * 
	 * @param userId
	 *            用户ID
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@POST
	@Path("/queryAccountMessageAllByUserIdDate")
	public Response queryAccountMessageAllByUserIdDate(Map<String, Object> paramMap) {
		try {

			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			Object begin = null, end = null;

			try {
				begin = paramMap.get("beginDate");
				end = paramMap.get("endDate");
			} catch (Exception e) {

			}

			Date beginDate = null;
			if (begin != null && !begin.equals("Invalid Date")) {
				beginDate = new Date(begin.toString());
			} else {
				beginDate = DateUtil.getDate(1999, 0, 1);
			}

			Date endDate = null;
			if (end != null && !end.equals("Invalid Date")) {
				endDate = new Date(end.toString());
			} else {
				endDate = DateUtil.getDate(9999, 0, 1);
			}

			System.out.println("@beginDate" + beginDate);
			System.out.println("@endDate" + endDate);

			List<AccountMessage> listAccountMessage = null;
			listAccountMessage = accountMessageManager.queryAccountMessageAllByUserIdDate(aid, beginDate, endDate);

			return Response.successResponse(listAccountMessage);

		} catch (Exception e) {
			e.printStackTrace();
			// 查询一个用户所有消息_按用户ID&日期失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_796, ResponseConstants.CommonMessage.RESPONSE_MSG_796);
		}
	}

	private Long getSessionAid() {
		Long aid = p2pSessionContext.getCurrentAid();
		// TODO DELETE TEST CODE
		//		if (null == aid) {
		//			aid = 22222L;
		//		}
		return aid;
	}
}
