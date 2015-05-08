package com.vcredit.jdev.p2p.base.vbs;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tempuri.SendMessageServiceSoap;
import org.tempuri.SmsMessageData;

import com.vcredit.jdev.p2p.base.util.P2pUtil;

@Component
public class VcreditSmsServiceVBS {
	
	@Autowired
	private GetWebServiceFromVBS getWebServiceFromVBS;
	
	private static Logger logger = Logger.getLogger(VcreditSmsServiceVBS.class);
	
	public void mobileSendMessage(String mobile,String content ) throws Exception{
		logger.debug("mobileSendMessage begin ######################");
//		  SmsTypeName亿美短信发送
//        CustomerType1
//		  OperationUserID0
//		  SignStr13866
//        smsSourceP2P
		SmsMessageData smsMessageData = new SmsMessageData();
		smsMessageData.setSmsTypeName("亿美短信发送");
		smsMessageData.setCustomerType(Short.valueOf("1"));
		smsMessageData.setOperationUserID(0);
		smsMessageData.setSignStr("13866");
		smsMessageData.setSmsSource("P2P");
		smsMessageData.setMessageContent(content);
		smsMessageData.setMobile(mobile);
		SendMessageServiceSoap sendMessageServiceSoap = getWebServiceFromVBS.getSendMessageServiceSoap();
		boolean result  = sendMessageServiceSoap.sendSmsMessage(smsMessageData);
		if(result){
			logger.debug("mobileSendMessage result:success--->"+result);
		}else{
			logger.debug("mobileSendMessage result:failure--->"+result);
		}
		logger.debug("mobileSendMessage end ######################");
	}
	
	/** 
	* @Title: mobileYunSendMessage 
	* @Description: 云平台发送短信
	*  @param smsJsonDto
	*  @return
	*  @throws Exception
	* @return String    返回类型 
	* @throws 
	*/
	public void mobileYunSendMessage(String mobile,String code) throws Exception{
		logger.debug("mobileYunSendMessage--> begin ######################");
		SmsJsonDto smsJsonDto = new SmsJsonDto(0, "", mobile, "13866", "P2P", "维金荟-注册验证短信", 1, code+",120", "");
		logger.debug("mobileYunSendMessage-->message content:"+smsJsonDto.toString());
		SendMessageServiceSoap sendMessageServiceSoap = getWebServiceFromVBS.getSendMessageServiceSoap();
		String result  = sendMessageServiceSoap.sendSmsCloudCommunication(P2pUtil.getBeanToJson(smsJsonDto));
		logger.debug("mobileYunSendMessage--> result:"+result);
		logger.debug("mobileYunSendMessage--> end ######################");
		
//		SmsJsonDto smsJsonDto = new SmsJsonDto(0, "", "13916145186", "13866", "P2P", "维金荟-注册验证短信", 1, "123456,120", "");
//		vcreditSmsServiceVBS.mobileYunSendMessage(smsJsonDto);
	}

}
