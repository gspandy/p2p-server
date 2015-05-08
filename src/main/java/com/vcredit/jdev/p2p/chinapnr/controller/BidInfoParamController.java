package com.vcredit.jdev.p2p.chinapnr.controller;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.chinapnr.account.UserManager;
import com.vcredit.jdev.p2p.chinapnr.deal.BidInfoManager;


@Path("/bidinfoparam")
@Consumes(MediaType.APPLICATION_JSON) 
public class BidInfoParamController {
	
	@Autowired
	private BidInfoManager bidInfoManager;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/initiativeTenderParam")
	public Response getInitiativeTenderParam(Map<String, Object> paramMap) throws Exception{
		String usrCusId = (String) paramMap.get("usrCusId");
		String borrowerDetails = "";
		String ordId = "";
		String ordDate = "";
		String transAmt = "";
		String maxTenderRate = "";
		String isFreeze = "";
		String freezeOrdId = "";
		Map<String,String> param = bidInfoManager.getInitiativeTenderParams(borrowerDetails, 
				ordId, ordDate,transAmt, usrCusId, maxTenderRate, isFreeze, freezeOrdId);
		return Response.successResponse(param);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAutoTenderPlanBackParam")
	public Response getAutoTenderPlanBackParam(Map<String, Object> paramMap) throws Exception{
		//TODO 生成 usrId规则;
		String usrCusId = "";
		String tenderPlanType = "";
		Map<String,String> param = bidInfoManager.getAutoTenderPlanParams(usrCusId, tenderPlanType,"");
		return Response.successResponse(param);
	}
	
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/bindcardparam")
//	public Response getBindCardParam(Map<String, Object> paramMap){
//		String usrCusId = (String) paramMap.get("usrCusId");
//		Map<String,String> param = urManager.getUserBindCardParam(usrCusId);
//		return Response.successResponse(param);
//	}
//	
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/acctmodifyparam")
//	public Response getAcctModifyParam(Map<String, Object> paramMap){
//		String usrCusId = (String) paramMap.get("usrCusId");
//		Map<String,String> param = urManager.getAcctModifyParam(usrCusId);
//		return Response.successResponse(param);
//	}
	
}
