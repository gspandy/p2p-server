package com.vcredit.jdev.p2p.capital.controller;

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
import com.vcredit.jdev.p2p.capital.modal.AccountBankCardManager;
import com.vcredit.jdev.p2p.entity.AccountBankCard;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
/**
 * 银行卡管理controller
 * @author 周佩
 * 创建时间：20141211
 */
@Path("/accountBankCard")
public class AccountBankCardController {
	@Autowired
	private AccountBankCardManager accountBankCardManager;
	@Autowired
	private AccountThirdRepository accountThirdRepository;
	@Autowired
	private P2pSessionContext p2pSessionContext;
	/**
	 * 设置默认银行卡
	 * 
	 * @param userid
	 * @return
	 */
	@POST
	@Path("/optionsDefaultDebit")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response optionsDefaultDebit(Map<String,Object> parameter) {
		AccountBankCard result;
		try{
			Long aid = p2pSessionContext.getCurrentAid();
			String bankCard=String.valueOf( parameter.get("bankCard"));
			if(bankCard==null){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_494,ResponseConstants.CommonMessage.RESPONSE_MSG_494);
			}
			result = accountBankCardManager.optionsDefaultDebit(bankCard,aid);
		}catch(Exception e){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_494,ResponseConstants.CommonMessage.RESPONSE_MSG_494);
		}
		return Response.successResponse(result);
	}
	
	/**
	 * 检索当前用户所拥有的银行卡
	 * 
	 * @param userid
	 * @return
	 */
	@POST
	@Path("/queryAccountBankCard")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryAccountBankCard(Map<String,Object> parameter) {
		List<AccountBankCard> result;
		try{
			Long aid = p2pSessionContext.getCurrentAid();
			result = accountBankCardManager.queryAccountBankCard(aid);
		}catch(Exception e){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_494,ResponseConstants.CommonMessage.RESPONSE_MSG_494);
		}
		return Response.successResponse(result);
	}
}
