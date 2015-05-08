package com.vcredit.jdev.p2p.merchant.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.merchant.modal.MerchantManager;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;

/**
 * 商品管理controller
 * @author 周佩
 * 创建时间：20141212
 */
@Path("/merchant")
public class MerchantController {
	@Autowired
	private MerchantManager merchantManager;
	@Autowired
	private InvestmentRepository investmentRepository;
	
	private Logger logger=LoggerFactory.getLogger(MerchantController.class);
	
	/**
	 * 检索在投资项目
	 * @param invest	投资项目
	 * @return
	 */
	@POST
	@Path("/queryInvestProjectInfo")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryInvestProjectInfo(Map<String,Object> parameter){
		List<Integer> status=(List<Integer>) parameter.get("investmentStatus");
		List<Integer> investmentPeriod=(List<Integer>)parameter.get("investmentPeriods");
		List<Integer> accountType=(List<Integer>)parameter.get("accountType");
		//默认检索当前上线的项目
		try{
			if(status==null||status.size()==0){
				status=new ArrayList<Integer>();
				status.add(InvestmentStatusEnum.ON_LINE.getCode());
				status.add(InvestmentStatusEnum.TENDER_FINISH.getCode());
			}
			List<Map<String,Object>> res=merchantManager.queryInvestmentInfo(status,investmentPeriod,accountType);
			return Response.successResponse(res);
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_599,ResponseConstants.CommonMessage.RESPONSE_MSG_599);
		}
	}
	
	/**
	 * 检索在投资项目
	 * @param invest	投资项目
	 * @return
	 */
	@POST
	@Path("/queryInvestmentInfoById")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryInvestmentInfoById(Map<String,Object> parameter){
		Long investmentId=Long.valueOf((String) parameter.get("investmentId"));
		try{
			List<Map<String,Object>> res=merchantManager.queryInvestmentInfoById(investmentId);
			return Response.successResponse(res);
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_599,ResponseConstants.CommonMessage.RESPONSE_MSG_599);
		}
	}

	/**
	 * 检索在投资项目
	 * @param invest	投资项目
	 * @return
	 */
	@GET
	@Path("/queryInvestProjectInfo")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response queryInvestProjectInfo(){
		Map<String,Object> parameter=new HashMap<String,Object>();
		return queryInvestProjectInfo(parameter);
	}
	
	/**
	 * 投资项目，选取loanData变成组合标的
	 * @param invest	投资项目
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Path("/componentProject")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response componentProject(Map<String, Object> paramMap) throws Exception{
		List<Integer> loanDataList=(List<Integer>) paramMap.get("loanDataList");
		List<Long> paramList=new ArrayList<Long>();
		for(Integer s:loanDataList){
			paramList.add(new Long(s));
		}
		String bool;
		try{
			bool=merchantManager.signleProjectById(paramList);
			return Response.successResponse(bool);
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_593,ResponseConstants.CommonMessage.RESPONSE_MSG_593); 
		}
	}
	
	/**
	 * loanData自动转化成投资项目
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Path("/autoSignleProject")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response autoSignleProject(Map<String, Object> paramMap) throws Exception{
		Integer size=Integer.valueOf( (String) paramMap.get("size"));
		try{
			String res = merchantManager.autoComponentProject(size);
			return Response.successResponse(res);
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_593,ResponseConstants.CommonMessage.RESPONSE_MSG_593); 
		}
	}
	
	/**
	 * 投资项目，选取loanData变成单个标的
	 * @param invest	投资项目
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Path("/signleProject")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signleProject(Map<String, Object> paramMap) throws Exception{
		List<Integer> loanDataList=(List<Integer>) paramMap.get("loanDataList");
		List<Long> paramList=new ArrayList<Long>();
		
		String res = "";
		try{
			for(Integer s:loanDataList){
				List<Long> i=new ArrayList<Long>();
				i.add(new Long(s));
				res=merchantManager.signleProjectById(i);
			}
			return Response.successResponse(res);
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_593,ResponseConstants.CommonMessage.RESPONSE_MSG_593); 
		}
	}
	
	/**
	 * 投资项目流标
	 * @param invest	投资项目
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Path("/investmentFlowBid")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response investmentFlowBid(Long investId) throws Exception{
		try{
			merchantManager.investmentFlowBid(investId);
		}catch(Exception e){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_597,ResponseConstants.CommonMessage.RESPONSE_MSG_597);
		}
		return Response.successResponse(true);
	}
	
	/**
	 * 投资项目常规上线
	 * @param invest	投资项目
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Path("/investmentOnline")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response investmentOnline(int onlineNum) throws Exception{
		try{
			merchantManager.investmentOnline(onlineNum);
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_596,ResponseConstants.CommonMessage.RESPONSE_MSG_596);
		}
		return Response.successResponse(true);
	}
	
	/**
	 * 投资项目定时上线
	 * @param invest	投资项目
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Path("/investmentTimeingOnline")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	public Response investmentTimeingOnlone() throws Exception{
		try{
			merchantManager.investmentTimeingOnline();
		}catch(Exception e){
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_595,ResponseConstants.CommonMessage.RESPONSE_MSG_595);
		}
		return Response.successResponse(true);
	}
	
	/**
	 * 投资项目常规上线	强制指定投资项目ID进行上线
	 * @param invest	投资项目
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Path("/investmentOnlineById")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response investmentOnlineById(Map<String, Object> paramMap) throws Exception{
		Response response =null;
		try{
			Long investmentId=Long.valueOf((String) paramMap.get("investmentId"));
			response = merchantManager.investmentOnlineById(investmentId);
			if(ResponseConstants.CommonCode.SUCCESS_CODE.equals(response.getStatus())){//如果状态是000那么表示成功，添加inv_type为null
				Investment i=investmentRepository.findOne(investmentId);
				i.setInvestmentType(null);
				investmentRepository.save(i);
			}
		}catch(Exception e){
			logger.error("investmentOnlineById is error",e);
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_596,ResponseConstants.CommonMessage.RESPONSE_MSG_596);
		}
		return Response.successResponse(response);
	}
	
	/**
	 * 将流标项目再次转化为待发布项目
	 * @return Response
	 */
	@POST
	@Path("/investmentFailing2online")
	@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response investmentFailing2online(Map<String, Object> paramMap){
		Long investmentId= Long.parseLong((String)paramMap.get("investmentId"));
		try {
			return merchantManager.copyInvestment(investmentId);
		} catch (Exception e) {
			logger.error("investmentFailing2online",e);
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_581,ResponseConstants.CommonMessage.RESPONSE_MSG_581,e);
		}
	}
}
