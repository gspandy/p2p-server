package com.vcredit.jdev.p2p.account.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.account.modal.EmailManager;
import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.PropertiesUtils;

@Path("/accountget")
@Produces(MediaType.APPLICATION_JSON+ ";charset=UTF-8")
public class AccountGetController {
	
	@Autowired
	private P2pSessionContext p2pSessionContext;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountGetController.class);
	
	@Autowired
	private EmailManager emailManager;
	
	@Autowired
	private PropertiesUtils propertiesUtils;
	
	@GET
	@Path("/bindEmail")
	@Consumes(MediaType.WILDCARD) 
	public Response bidAccountEmail(@Context HttpServletRequest request) throws URISyntaxException{
//		Long aid = p2pSessionContext.getCurrentAid();
//		if(aid == null){
//			String loginPage = propertiesUtils.getAppUrl() + "/#/login";
//			return Response.seeOther(new URI(loginPage)).build();
//		}
		String aaid = (String) request.getParameter("aid");
		String email = (String) request.getParameter("email");
		String type = (String) request.getParameter("type");
		//绑定是1 修改为2
		String successDiv ="";
		String failDiv ="";
		String pageParam = "";
		if("1".equals(type)){
			successDiv ="emailBindSuccess";
			failDiv ="emailBindFail";
		}else if("2".equals(type)){
			successDiv ="emailSuccess";
			failDiv ="emailFail";
		}
		if(StringUtils.isBlank(aaid)){
			pageParam ="fail/"+failDiv+"/"+ResponseConstants.CommonCode.RESPONSE_CODE_231+"/"+ResponseConstants.CommonMessage.RESPONSE_MSG_231;;
		}else{
			try {
				int result = emailManager.bindAccountEmail(Long.valueOf(aaid),email);
				if(result==1){
					pageParam = "success/"+successDiv;
				}else{
					pageParam ="fail/"+failDiv+"/"+ResponseConstants.CommonCode.RESPONSE_CODE_242+"/"+ResponseConstants.CommonMessage.RESPONSE_MSG_242;
				}
			} catch (Exception e) {
				logger.error("bidAccountEmail error"+e.getMessage());
				pageParam ="fail/"+failDiv+"/"+ResponseConstants.CommonCode.RESPONSE_CODE_217+"/"+ResponseConstants.CommonMessage.RESPONSE_MSG_217;
			}
		}
		
		String redirectpage = propertiesUtils.getAppUrl() + "/#/"+pageParam;
		return Response.seeOther(new URI(redirectpage)).build();
	}

}
