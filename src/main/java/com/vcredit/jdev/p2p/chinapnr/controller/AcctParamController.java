package com.vcredit.jdev.p2p.chinapnr.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.chinapnr.account.UserManager;


@Path("/acctparam")
@Consumes(MediaType.WILDCARD) 
public class AcctParamController {
	
	@Autowired
	private UserManager urManager;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/loginparam")
	public Response getLoginParam(Map<String, Object> paramMap){
		String usrCusId = (String) paramMap.get("usrCusId");
		Map<String,String> param = urManager.getUserLoginParam(usrCusId);
		return Response.successResponse(param);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registerparam")
	public Response getRegisterParam(@Context HttpServletRequest request){
		//TODO 生成 usrId规则;
		
//		String usrId = (String) paramMap.get("usrId");
//		String usrMp = (String) paramMap.get("usrMp");
		String usrId = "";
		String usrMp = "";
		String usrName = "";
		String idType = "";
		String idNo = "";
		String usrEmail = "";
		String merPri = "";
		Map<String,String> param = urManager.getUserRegisterParam(usrId, usrName, idType, idNo, usrMp, usrEmail,merPri,1);
		return Response.successResponse(param);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registersumbit")
	public Response sumbitRegisterParam(Map<String, Object> paramMap){
		//TODO 生成 usrId规则;
		String usrId = (String) paramMap.get("usrId");
		String usrMp = (String) paramMap.get("usrMp");
//		String usrId = "dk05408";
//		String usrMp = "13989578456";
		String usrName = "";
		String idType = "";
		String idNo = "";
		String usrEmail = "";
		String merPri = "";
		//urManager.userRegister(usrId, usrName, idType, idNo, usrMp, usrEmail);
		Map<String,String> param = urManager.getUserRegisterParam(usrId, usrName, idType, idNo, usrMp, usrEmail,merPri,1);
		return Response.successResponse(param);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/bindcardparam")
	public Response getBindCardParam(Map<String, Object> paramMap){
		String usrCusId = (String) paramMap.get("usrCusId");
		Map<String,String> param = urManager.getUserBindCardParam(usrCusId);
		return Response.successResponse(param);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/acctmodifyparam")
	public Response getAcctModifyParam(Map<String, Object> paramMap){
		String usrCusId = (String) paramMap.get("usrCusId");
		Map<String,String> param = urManager.getAcctModifyParam(usrCusId);
		return Response.successResponse(param);
	}
	
}
