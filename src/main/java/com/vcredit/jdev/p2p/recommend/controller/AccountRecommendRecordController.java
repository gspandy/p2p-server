package com.vcredit.jdev.p2p.recommend.controller;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.entity.AccountRecommendRecord;
import com.vcredit.jdev.p2p.recommend.repository.AccountRecommendRecordRepository;
import com.vcredit.jdev.p2p.recommend.service.AccountRecommendRecordManager;

@Path("/accountRecommendRecord")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON)
public class AccountRecommendRecordController {

	@Autowired
	AccountRecommendRecordRepository accountRecommendRecordRepository;
	@Autowired
	private P2pSessionContext p2pSessionContext;
	@Autowired
	AccountRecommendRecordManager accountRecommendRecordManager;

	/**
	 * 发送邮件推荐
	 * 
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/sendMailRecommend")
	public Response sendMailRecommend(Map<String, Object> paramMap) {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			Object a = paramMap.get("toUser");
			Object b = paramMap.get("sendSubject");
			Object c = paramMap.get("sendContent");

			if (null != a && null != b && null != c) {
				String toUser = (String) a;
				String sendSubject = (String) b;
				String sendContent = (String) c;

				accountRecommendRecordManager.sendMailRecommend(toUser, sendSubject, sendContent);

				return Response.successResponse();
			} else {
				// 用户推荐信息输入不完整
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_998, ResponseConstants.CommonMessage.RESPONSE_MSG_998);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 发送邮件推荐失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_997, ResponseConstants.CommonMessage.RESPONSE_MSG_997);
		}
	}

	/**
	 * 查找用户推荐记录笔数
	 * 
	 * @return
	 */
	@POST
	@Path("/queryAccountRecommendRecordCount")
	public Response queryAccountRecommendRecordCount() {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			Long count = accountRecommendRecordRepository.findAccountRecommendRecordCountByUserId(aid);

			return Response.successResponse(count);
		} catch (Exception e) {
			e.printStackTrace();
			//查找用户推荐记录笔数失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_996, ResponseConstants.CommonMessage.RESPONSE_MSG_996);
		}
	}

	/**
	 * 查找用户推荐记录笔数_管理员
	 * 
	 * @return
	 */
	@POST
	@Path("/queryAccountRecommendRecordCount4Admin")
	public Response queryAccountRecommendRecordCount4Admin(Map<String, Object> pMap) {
		try {
			Long sAid = getSessionAid();
			if (null == sAid) {
				return Response.noLoginResponse();
			}

			Long aid = Long.valueOf(pMap.get("aid").toString());
			Long count = accountRecommendRecordRepository.findAccountRecommendRecordCountByUserId(aid);

			return Response.successResponse(count);
		} catch (Exception e) {
			e.printStackTrace();
			//查找用户推荐记录笔数失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_995, ResponseConstants.CommonMessage.RESPONSE_MSG_995);
		}
	}

	/**
	 * 查找用户推荐记录
	 * 
	 * @return
	 */
	@POST
	@Path("/queryAccountRecommendRecord")
	public Response queryAccountRecommendRecord() {
		try {
			Long aid = getSessionAid();
			if (null == aid) {
				return Response.noLoginResponse();
			}

			List<AccountRecommendRecord> list = accountRecommendRecordRepository.findAccountRecommendRecordAllByUserId(aid);

			return Response.successResponse(list);
		} catch (Exception e) {
			e.printStackTrace();
			// 查找用户推荐记录失败
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_999, ResponseConstants.CommonMessage.RESPONSE_MSG_999);
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
