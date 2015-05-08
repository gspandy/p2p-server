package com.vcredit.jdev.p2p.chinapnr.controller;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.util.PropertiesUtils;
import com.vcredit.jdev.p2p.capital.modal.BaseManager;
import com.vcredit.jdev.p2p.chinapnr.base.ChinapnrLogManager;
import com.vcredit.jdev.p2p.chinapnr.base.VerifyByRSAManager;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;
import com.vcredit.jdev.p2p.enums.ThirdChannelEnum;


/** 
* @ClassName: CallBackController 
* @Description: 汇付通过BgRetUrl与RetUrl回调控制类
* @author dk 
* @date 2015年1月21日 上午11:47:19 
*  
*/
@Path("/handler")
@Consumes(MediaType.WILDCARD) 
public class CallBackController {
	
	//slf4j.log
	private static final Logger logger  =  LoggerFactory.getLogger(CallBackController.class );
	
	@Autowired
	private BaseManager baseManager;
	
	@Autowired
	private VerifyByRSAManager verifyByRSAManager;
	
	@Autowired
	private ChinapnrLogManager chinapnrLogManager;

	@Autowired
	SignUtils signUtils;
	
	@Autowired
	PropertiesUtils propertiesUtils;

	
	/** 
	* @Title: callback 
	* @Description: 汇付通过BgRetUrl,并通过 baseManager.invokeMethod分发处理.并给汇付响应（trxId与ordId）
	* @param request 回调请求,里面包括所有返回参数
	* @param @return
	* @param @throws UnsupportedEncodingException    设定文件 
	* @return Response    返回类型 
	* @throws 
	*/
	@SuppressWarnings("rawtypes")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/callbackBgRetUrl")
	public Response callbackBgRetUrlRoute(@Context HttpServletRequest request) throws UnsupportedEncodingException{
		
		logger.debug("-------------汇付回调开始-----callbackBgRetUrlRoute begin------------");
		String respCode = request.getParameter("RespCode");
		String respDesc = request.getParameter("RespDesc");
		//区分各个回调callbackBgRetUrlRoute begin
		
		String cmdId = request.getParameter("CmdId");
		//取现 异步对账返回 没有cmdId,只有RespType = Cash
		String respType = request.getParameter("RespType");
		if(StringUtils.isNotBlank(respType)){
			cmdId = respType;
		}
		
		//这些字段需要返回到汇付===bigin
		String trxId = request.getParameter("TrxId");
		String ordId = request.getParameter("OrdId");
		String proId = request.getParameter("ProId");
		String batchId = request.getParameter("BatchId");
		//这些字段需要返回到汇付======end
		
		if(StringUtils.isNotBlank(respDesc)){
			respDesc = URLDecoder.decode(respDesc, "UTF-8");
		}
		

		logger.debug("callbackBgRetUrlRoute-------------汇付回调:"+"   消息类型(cmdId):"+cmdId+"  返回码(respCode):"+respCode+"   描述(respDesc):"+respDesc);
		//1.当所有回调参数放到Map
		Map<String,String> requestmap= new ConcurrentHashMap<String, String>();
		Enumeration enu=request.getParameterNames();
		while(enu.hasMoreElements()){
			String paraName=(String)enu.nextElement();
			//对所有返回参数进行UTF-8转义
			String value = request.getParameter(paraName);
			if(StringUtils.isNotBlank(value)){
				value = URLDecoder.decode(value, "UTF-8");
			}
			requestmap.put(paraName, value);
		}
		
		if(StringUtils.isNotBlank(respType)){
			requestmap.put("cmdId",cmdId);
		}
		System.out.println(requestmap);
		logger.debug("callbackBgRetUrlRoute requestmap:"+requestmap);
		//先验签 再处理 回复回调
		//2.先验签
		try {
			//verifyByRSAManager.verifyByRSARout(cmdId, requestmap);
		} catch (Exception e) {
			logger.error("调用 response callbackHandler callbackBgRetUrlRoute 中的验签 error.-------------------------"+e.getMessage());
		}
		
		//3.当respCode !=000,添加response log
		try {
			logger.debug("callbackRoute callbackBgRetUrlRoute call chinapnrLogManager---> addResponseLogRote .......");
			chinapnrLogManager.addResponseLogRote(cmdId, requestmap, respCode, respDesc);
		} catch (Exception e) {
			logger.error(" response callbackHandler callbackBgRetUrlRoute add response log error.-------------------------"+e.getMessage());
		}
		
		//4.处理回调参数(周佩)
		try {
			logger.debug("callbackRoute callbackBgRetUrlRoute call baseManager invokeMethod .......");
			//if(StringUtils.isNotBlank(cmdId)){
				baseManager.invokeMethod(cmdId, requestmap);
//			}else if(StringUtils.isNotBlank(respType)){//取现有两次回调，标注方法一次是cmdId,一次是respType
//				baseManager.invokeMethod(respType, requestmap);
//			}
		} catch (Exception e) {
			logger.error("调用 response callbackHandler callbackBgRetUrlRoute invokeMethod  error.-------------------------",e);
		}
		
		//5.将成功的回调的trxId,ordId,batchId,proId返回，以确认回调成功
		Map<String, String> map = null;
		if (respCode.equals("000")) {
			map = new HashMap<String, String>();
			if (StringUtils.isNotBlank(trxId)) {
				map.put("trxId", "RECV_ORD_ID_".concat(trxId));
			}
			if (StringUtils.isNotBlank(ordId)) {
				map.put("ordId", "RECV_ORD_ID_".concat(ordId));
			}
			if (StringUtils.isNotBlank(batchId)) {// 批量还款
				map.put("batchId", "RECV_ORD_ID_".concat(batchId));
			}
			if (StringUtils.isNotBlank(proId)) {// 标的录入
				map.put("proId", "RECV_ORD_ID_".concat(proId));
			}
			logger.debug("-------------打印回调-----------------Map:###"+map+"-----");
		}
        logger.debug("-------------汇付回调结束-----callbackBgRetUrlRoute end------------");
		return Response.successResponse(map);
	}
	
	/** 
	* @Title: callbackRetUrlRoute 
	* @Description: 汇付通过RetUrl回调进行302redirect
	*  @param request
	*  @return
	*  @throws UnsupportedEncodingException
	*  @throws URISyntaxException
	* @return javax.ws.rs.core.Response    返回类型 
	* @throws 
	*/
	@SuppressWarnings("rawtypes")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/callbackRetUrl")
	public javax.ws.rs.core.Response callbackRetUrlRoute(@Context HttpServletRequest request) throws UnsupportedEncodingException, URISyntaxException{
		
		logger.debug("callbackRoute callbackRetUrlRoute begin.......");
		String cmdId = request.getParameter("CmdId");
		// 取现 异步对账返回 没有cmdId,只有RespType = Cash
		String respType = request.getParameter("RespType");
		if(StringUtils.isNotBlank(respType)){
			cmdId = respType;
		}
		String respCode = request.getParameter("RespCode");
		String respDesc = request.getParameter("RespDesc");
		if (StringUtils.isNotBlank(respDesc)) {
			respDesc = URLDecoder.decode(respDesc, "UTF-8");
		}
		
		logger.debug("callbackRetUrlRoute-------------汇付回调:"+"   消息类型(cmdId):"+cmdId+"  返回码(respCode):"+respCode+"   描述(respDesc):"+respDesc);
		
		//1.处理参数
		Map<String, String> requestmap = new ConcurrentHashMap<String, String>();
		Enumeration enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			String value = request.getParameter(paraName);
			//对所有返回参数进行UTF-8转义
			if(StringUtils.isNotBlank(value)){
				value = URLDecoder.decode(value, "UTF-8");
			}
			requestmap.put(paraName, value);
		}
		if(StringUtils.isNotBlank(respType)){
			requestmap.put("cmdId",cmdId);
		}
		logger.debug("callbackRetUrlRoute requestmap:"+requestmap);
		
		//2.处理回调参数(周佩)
		try {
			logger.debug("callbackRoute callbackRetUrlRoute call baseManager invokeMethod .......");
			if(StringUtils.isNotBlank(cmdId)){
				baseManager.invokeMethod(cmdId, requestmap);
			}else if(StringUtils.isNotBlank(respType)){//取现有两次回调，标注方法一次是cmdId,一次是respType
				baseManager.invokeMethod(respType, requestmap);
			}
		} catch (Exception e) {
			logger.error("调用 自动投资计划开启 response callbackRetUrlRoute 处理回调 error.-------------------------"+e.getMessage());
		}
		
		//3.页面的跳转
		String pageParam = "";
		String resparam =  "";
		if ((respCode.equals("000"))) {
			logger.debug("callbackRoute callbackRetUrlRoute success page .......");
			//传给success page的参数
			// 自动投标计划的开启与关闭，这两个接口只有RetUrl接收回调，而没有BgRetUrl回调

			if (ThirdChannelEnum.INITIATIVETENDER.getCode().equals(cmdId)) {// 主动投标
				pageParam = "success/investSuccess";
			} else if (ThirdChannelEnum.USERREGISTER.getCode().equals(cmdId)) {// 开户注册
				pageParam = "success/thirdAccount";
			} else if (ThirdChannelEnum.NETSAVE.getCode().equals(cmdId)) {// 充值
				pageParam = "success/topupSuccess";
			} else if (ThirdChannelEnum.CASH.getCode().equals(cmdId)) {//TODO 提现  有两次返回
				pageParam = "success/applySuccess";
			} else if (ThirdChannelEnum.AUTOTENDERPLAN.getCode().equals(cmdId)) {// 自动投资计划开启
				pageParam = "onetouch";
			} else if (ThirdChannelEnum.AUTOTENDERPLANCLOSE.getCode().equals(cmdId)) {// 自动投资计划关闭
				pageParam = "invest/auto";
			}
		}else{
			logger.debug("调用 response callbackRetUrlRoute fail page.------------------------- respCode:"
					+ respCode + " respDesc:" + respDesc);
			resparam ="/"+respCode+"/"+respDesc;
			//传给error page的参数
			if (ThirdChannelEnum.INITIATIVETENDER.getCode().equals(cmdId)) {// 主动投标
				pageParam = "fail/investSuccessFail";
			} else if (ThirdChannelEnum.USERREGISTER.getCode().equals(cmdId)) {// 开户注册
				pageParam = "fail/thirdAccountFail";
			} else if (ThirdChannelEnum.NETSAVE.getCode().equals(cmdId)) {// 充值
				pageParam = "fail/topupSuccessFail";
			} else if (ThirdChannelEnum.CASH.getCode().equals(cmdId)) {//TODO 提现  有两次返回
				pageParam = "fail/applySuccessFail";
			}
		}
		//logger end
		logger.debug("callbackRoute callbackRetUrlRoute end.......");
		
		//TODO 通过successPageParam的不同，来在前台显示不同的div,来展示不同的success page
		String redirectpage = propertiesUtils.getAppUrl() + "/#/"+pageParam;
		
		
		// 303 重定向
		return javax.ws.rs.core.Response.seeOther(new URI(redirectpage+resparam)).build();

		// 307
		// return javax.ws.rs.core.Response.temporaryRedirect(new
		// URI("http://localhost/#/account")).build();
		// 302
		// return javax.ws.rs.core.Response.status(Status.FOUND).location(new
		// URI("http://localhost/#/account")).build();
		
	}

}
