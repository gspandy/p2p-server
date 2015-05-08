package com.vcredit.jdev.p2p.capital.controller;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.capital.modal.CapitalCalculateManager;
import com.vcredit.jdev.p2p.dto.CapitalCalculateQueryDto;

/**
 * 项目资金相关
 * @author zhaopeijun
 *
 */
@Path("/capitalCalculate")
public class CapitalCalculateController {
	
	@Autowired
	private CapitalCalculateManager capitalCalculateManager;
	
	@POST
	@Path("/queryInvestment")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryInvestment(Map<String, Object> paramMap) throws JsonProcessingException {
		Long id = Long.valueOf((paramMap.get("invId").toString()));
		List<CapitalCalculateQueryDto> list = capitalCalculateManager.queryInvestment(id);
		if(null == list || list.isEmpty()){
			return null;
		}else{
			return Response.successResponse((CapitalCalculateQueryDto)list.get(0));
		}
	}

}