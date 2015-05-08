package com.vcredit.jdev.p2p.merchant.controller;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.dto.AccountWelfareDto;
import com.vcredit.jdev.p2p.entity.AccountWelfare;
import com.vcredit.jdev.p2p.enums.AccountWelfareEnum;
import com.vcredit.jdev.p2p.merchant.modal.AccountWelfareManager;

/**
 * 福利：红包的管理
 * 
 * @author 周佩
 *
 */
@Path("/accountWelfare")
public class AccountWelfareController {
	@Autowired
	private AccountWelfareManager accountWelfareManager;

	@Autowired
	private P2pSessionContext p2pSessionContext;
	/**
	 * 检索红包
	 * @param parameter	红包
	 * @return
	 */
	@POST
	@Path("/queryAccountWelfareByAccount")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryAccountWelfareByAccount(Map<String,Object> parameter){
		Long accountId = p2pSessionContext.getCurrentAid();
		try{
			int page=Integer.parseInt((String) (parameter.get("page")==null?0:parameter.get("page")));
			int size=Integer.parseInt((String) (parameter.get("size")==null?0:parameter.get("size")));
			Page<AccountWelfareDto> res=accountWelfareManager.queryAccountWelfareByAccount(accountId, page, size);
			return Response.successResponse(res);
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_592,ResponseConstants.CommonMessage.RESPONSE_MSG_592);
		}
	}
	
	/**
	 * 通过当前用户以及状态检索数据
	 * @param parameter	红包
	 * @return
	 */
	@POST
	@Path("/queryAccountWelfareByAccountAndStatus")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryAccountWelfareByAccountAndStatus(Map<String,Object> parameter){
		Long accountId = p2pSessionContext.getCurrentAid();
		try{
			AccountWelfare awf=new AccountWelfare();
			int welfareStatus=Integer.parseInt(String.valueOf(parameter.get("welfareStatus")==null?"0":parameter.get("welfareStatus")));
			awf.setAccountSequence(accountId);
			awf.setWelfareStatus(welfareStatus);
			int page=Integer.parseInt(String.valueOf( (parameter.get("page")==null?0:parameter.get("page"))));
			int size=Integer.parseInt(String.valueOf( (parameter.get("size")==null?0:parameter.get("size"))));
			Page<AccountWelfareDto> res=accountWelfareManager.queryAccountWelfareByAccountAndStatus(awf, page, size);
			return Response.successResponse(res);
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_592,ResponseConstants.CommonMessage.RESPONSE_MSG_592);
		}
	}
	
	/**
	 * 获得红包
	 * @param parameter	红包
	 * @return
	 */
	@POST
	@Path("/giveWelfare")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response giveWelfare(Map<String,Object> parameter){
		Long accountId = p2pSessionContext.getCurrentAid();
		try{
			Long welfareId=Long.valueOf( (String) parameter.get("welfareId"));
			Integer welfareSource=Integer.valueOf( (String) parameter.get("welfareSource"));
			accountWelfareManager.giveWelfare(accountId,welfareId,welfareSource);
			return Response.successResponse(true);
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_592,ResponseConstants.CommonMessage.RESPONSE_MSG_592);
		}
	}
	

	/**
	 * 红包兑换
	 * @param parameter	红包
	 * @return
	 */
	@POST
	@Path("/exchangeAccountWelfare")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response exchangeAccountWelfare(Map<String,Object> parameter){
		Long accountId = p2pSessionContext.getCurrentAid();
		try{
			boolean bool=true;
			Long accountWelfareId=Long.valueOf((String) (parameter.get("accountWelfareId")==null?0:parameter.get("accountWelfareId")));
			Integer welfareConsume=Integer.parseInt((String) (parameter.get("welfareConsume")==null?0:parameter.get("welfareConsume")));
			AccountWelfare awf = accountWelfareManager.exchangeAccountWelfare(accountId,accountWelfareId,welfareConsume);
			if(awf==null){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_591,ResponseConstants.CommonMessage.RESPONSE_MSG_591);
			}else if(AccountWelfareEnum.EXPIRED.getCode().equals(awf.getWelfareStatus())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_588,ResponseConstants.CommonMessage.RESPONSE_MSG_588);
			}else {
				return Response.successResponse(bool);
			}
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_592,ResponseConstants.CommonMessage.RESPONSE_MSG_592);
		}
	}
}
