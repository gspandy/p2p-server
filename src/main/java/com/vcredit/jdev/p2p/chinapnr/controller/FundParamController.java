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
import com.vcredit.jdev.p2p.chinapnr.deal.FundManager;


@Path("/fundparam")
@Consumes(MediaType.APPLICATION_JSON) 
public class FundParamController {
	
	@Autowired
	private FundManager fundManager;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/netsaveparam")
	public Response getNetSaveParam(Map<String, Object> paramMap){
		String usrCusId = (String) paramMap.get("usrCusId");
		String transAmt = (String) paramMap.get("transAmt");
		//TODO 生成订单ID与时间
		String ordId ="";
		String ordDate ="";
		//Map<String,String> param = fundManager.getNetSaveParam(usrCusId, ordId, ordDate, transAmt,"","");
		return Response.successResponse();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cashparam")
	public Response getCashParam(Map<String, Object> paramMap){
		//TODO 生成 usrId规则;
		String usrCustId ="";
		String ordId ="";
		String transAmt ="";
		String servFee ="";
		String servFeeAcctId ="";
		String reqExt ="";
		String openAcctId ="";
		//Map<String,String> param = fundManager.getCashParam(usrCustId, ordId, transAmt, servFee, servFeeAcctId, openAcctId, reqExt,"");
		return Response.successResponse();
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
