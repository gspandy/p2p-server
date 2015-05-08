package com.vcredit.jdev.p2p.chinapnr.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import net.gplatform.sudoor.server.integration.AsyncEventMessageGateway;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import chinapnr.SecureLink;

import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.entity.RequestLog;
import com.vcredit.jdev.p2p.entity.ResponseLog;

@Component
public class SignUtils implements Serializable {
	
	
	private static final long serialVersionUID = 7109211036394126484L;
	
	private static Logger logger = Logger.getLogger(SignUtils.class);
	
	/** MD5签名类型 **/
	public static final String SIGN_TYPE_MD5           = "M";

	/** RSA签名类型 **/
	public static final String SIGN_TYPE_RSA           = "R";

	/** RSA验证签名成功结果 **/
	public static final int    RAS_VERIFY_SIGN_SUCCESS = 0;

	/** URL三方地址 **/
	public static final String URL           = "URL";
	
	@Autowired
	ApplicationContext ctx;
	
	
	@Autowired
	private AsyncEventMessageGateway asyncEventMessageGateway;//异步
	
	@Autowired
	private P2pSessionContext p2pSessionContext;


	/** 商户号 **/
	@Value("${chinapnr.recvMerId}")
	String recvMerId;
	
	/**客户号 **/
	@Value("${chinapnr.merCustId}")
	String merCustId;

	/** 商户公钥文件名称 **/
	@Value("${chinapnr.merPubKeyName}")
	String merPubKeyName;

	/** 商户私钥文件名称 **/
	@Value("${chinapnr.merPriKeyName}")
	String merPriKeyName;
	
	/**汇付服务访问路径**/
	@Value("${chinapnr.url}")
	String chinapnrUrl;
	
	/**汇付版本1**/
	@Value("${chinapnr.version1}")
	String version1;
	
	/**汇付版本2**/
	@Value("${chinapnr.version2}")
	String version2;
	
	/**商户后台应答地址**/
	@Value("${chinapnr.bgRetUrl}")
	String bgRetUrl;
	
	/**汇付页面返回路径Url**/
	@Value("${chinapnr.retUrl}")
	String retUrl;

//	public static String test ="";
//	@Value("${my.name}")
//	public void setPrivateName(String privateName) {
//		SignUtils.test = privateName;
//	}
	
	
	/** 商户公钥文件地址 **/
	public String getMerPubKeyNamePath(){
		return getRealPath(merPubKeyName);
	}

	/** 商户私钥文件地址 **/
	public String getMerPriKeyNamePath(){
		return getRealPath(merPriKeyName);
	}
	
	/**
	 * 取key所在路径
	 * **/
	public String getRealPath(String path){
		String relealpath = null;
		try {
			relealpath = ctx.getResource(path).getFile().getPath();;
		} catch (IOException e) {
			logger.info("取keys所在路径报错:=====get keys path error....");
			e.printStackTrace();
		}
		logger.info("spring context:"+relealpath+"####################");
		return relealpath;
	}
	
	/** 
	* @Title: getServerPath 
	* @Description: 获取第三方服务访问路径
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getServerPath(){
		return chinapnrUrl;
	}
	
	
	/** 
	* @Title: getRecvMerId 
	* @Description:     获取商户号 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getRecvMerId(){
		return recvMerId;
	}
	
	/** 
	* @Title: getMerCustId 
	* @Description:     获取客户号 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getMerCustId(){
		return merCustId;
	}
	
	
	/** 
	* @Title: getVersion1 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getVersion1(){
		return version1;
	}
	
	
	/** 
	* @Title: getVersion2 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getVersion2(){
		return version2;
	}
	
	
	/** 
	* @Title: getBgRetUrl 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getBgRetUrl(){
		return bgRetUrl;
	}
	
	/** 
	* @Title: getRetUrl 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getRetUrl(){
		return retUrl;
	}


	/**
	 * RSA方式加签
	 * 
	 * @param custId
	 * @param forEncryptionStr
	 * @param charset
	 * @return
	 * @throws Exception 
	 */
	public  String encryptByRSA(String forEncryptionStr) throws Exception {
		SecureLink sl = new SecureLink();
		int result = sl.SignMsg(recvMerId, getMerPriKeyNamePath(), forEncryptionStr);
		System.out.println(recvMerId+" "+getMerPriKeyNamePath());
		if (result < 0) {
			// 打印日志 
			logger.error("RSA方式加签失败!");
			throw new Exception();
		}
		return sl.getChkValue();
	}

	/** 
	* @Title: verifyByRSA 
	* @Description: RSA方式验签
	* @param forEncryptionStr
	* @param chkValue
	* @param @return
	* @param @throws Exception    设定文件 
	* @return boolean    返回类型 
	* @throws 
	*/
	public boolean verifyByRSA(String forEncryptionStr, String chkValue)
			throws Exception {
		try {
			int verifySignResult = new SecureLink().VeriSignMsg(getMerPubKeyNamePath(), forEncryptionStr, chkValue);
			return verifySignResult == RAS_VERIFY_SIGN_SUCCESS;
		} catch (Exception e) {
			logger.error("RSA方式验签失败!");
			// 打印日志
			throw new Exception();
		}
	}
	
	
	/** 
	* @Title: addRequestChinapnrLogEventPublish 
	* @Description: 添加访问汇付日志
	*  @param requestType
	* @return void    返回类型 
	* @throws 
	*/
	public void addRequestChinapnrLogEventPublish(String requestType,String requestKey){
//		String sessionId = null;
//		RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
//		if (attribs instanceof NativeWebRequest) {
//			//HttpServletRequest request = (HttpServletRequest) ((NativeWebRequest) attribs).getNativeRequest();
//			sessionId = p2pSessionContext.getSessionId();
//		}
		RequestLog requestLog = new RequestLog();
		requestLog.setRequestDate(new Date());
		requestLog.setRequestUrl(getServerPath());
		requestLog.setRequestType(requestType);
		requestLog.setSessionId(requestKey);
		asyncEventMessageGateway.publishEvent(requestLog);
	}
	
	
	/** 
	* @Title: addResponseChinapnrLogEventPublish 
	* @Description: 添加应答汇付日志 
	*  @param responseType
	*  @param responseCode
	*  @param responseKey
	*  @param responseDescription
	* @return void    返回类型 
	* @throws 
	*/
	public void addResponseChinapnrLogEventPublish(String responseType,String responseCode,String responseKey
			,String responseDescription){
		ResponseLog responseLog = new ResponseLog();
		responseLog.setResponseType(responseType);
		responseLog.setResponseCode(responseCode);
		responseLog.setResponseKey(responseKey);
		responseLog.setResponseDescription(responseDescription);
		responseLog.setResponseDate(new Date());
		asyncEventMessageGateway.publishEvent(responseLog);
	}

}
