package com.vcredit.jdev.p2p.alert.controller;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.alert.repository.AccountRemindRepository;
import com.vcredit.jdev.p2p.alert.service.AccountMessageManager;
import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.entity.AccountRemind;

/**
 * @author ChenChang
 *
 */
@Path("/accountRemind")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON)
public class AccountRemindController {
	@Autowired
	private P2pSessionContext p2pSessionContext;
	@Autowired
	AccountRemindRepository accountRemindRepository;
	@Autowired
	AccountMessageManager accountMessageManager;

	// 查找用户消息设置
	@POST
	@Path("/queryAccountRemindByUserId")
	public Response queryAccountRemindByUserId() {
		try {
			Long aid = getSessionAid();

			if (null == aid) {
				return Response.noLoginResponse();
			}

			List<AccountRemind> list = accountRemindRepository
					.findAllByAid(aid);
			return Response.successResponse(list);
		} catch (Exception e) {
			e.printStackTrace();
			// 查找用户消息设置失败
			return Response.response(
					ResponseConstants.CommonCode.RESPONSE_CODE_794,
					ResponseConstants.CommonMessage.RESPONSE_MSG_794);
		}

	}

	// 更新一条用户消息设置
	@POST
	@Path("/updateAccountRemindOne")
	public Response updateAccountRemindOne(Map<String, Object> paramMap) {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			Long systemRemindOptionSequence = Long.valueOf(paramMap.get(
					"systemRemindOptionSequence").toString());
			Integer remindStatus = Integer.valueOf(paramMap.get("remindStatus")
					.toString());

			accountMessageManager.updateAccountRemindOne(remindStatus, aid,
					systemRemindOptionSequence);

			return Response.successResponse();
		} catch (Exception e) {
			e.printStackTrace();
			// 更新一条用户消息设置失败
			return Response.response(
					ResponseConstants.CommonCode.RESPONSE_CODE_793,
					ResponseConstants.CommonMessage.RESPONSE_MSG_793);
		}

	}

	private Long getSessionAid() {
		Long aid = p2pSessionContext.getCurrentAid();
//		// TODO DELETE TEST CODE
//		if (null == aid) {
//			aid = 25258L;
//		}
		return aid;
	}

}
