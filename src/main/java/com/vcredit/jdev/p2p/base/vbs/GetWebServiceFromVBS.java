package com.vcredit.jdev.p2p.base.vbs;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tempuri.SendMessageService;
import org.tempuri.SendMessageServiceSoap;
import org.tempuri.VcreditSmsService;
import org.tempuri.VcreditSmsServiceSoap;

@Component
public class GetWebServiceFromVBS {
	
	private static Logger logger = Logger.getLogger(GetWebServiceFromVBS.class);
//	@Value("${spring.soap.smsServiceUrl}")
//	String smsServiceUrl;
	
	@Value("${spring.soap.yunSmsServiceUrl}")
	String yunSmsServiceUrl;
	
	private static VcreditSmsServiceSoap smsServiceSoap = null;
	private static SendMessageServiceSoap sendMessageServiceSoap = null;
	
//	public VcreditSmsServiceSoap getSmsServiceSoap() throws Exception{
//		if (smsServiceSoap == null) {
//			VcreditSmsService vcreditSmsService = null;
//			try {
//				vcreditSmsService = new VcreditSmsService(new URL(smsServiceUrl));
//				GetWebServiceFromVBS.smsServiceSoap = vcreditSmsService.getVcreditSmsServiceSoap();
//			} catch (MalformedURLException e) {
//				logger.error(e);
//			} catch (Exception e) {
//				logger.error(e);
//				throw new Exception("Error when load VBS web service--VcreditSmsService");
//			}
//		}
//		return smsServiceSoap;
//	}
	
	public SendMessageServiceSoap getSendMessageServiceSoap() throws Exception{
		if (smsServiceSoap == null) {
			SendMessageService sendMessageService = null;
			try {
				sendMessageService = new SendMessageService(new URL(yunSmsServiceUrl));
				GetWebServiceFromVBS.sendMessageServiceSoap = sendMessageService.getSendMessageServiceSoap();
			} catch (MalformedURLException e) {
				logger.error(e);
				throw new Exception("Error when load VBS web service--sendMessageService sendMobileMessage1----");
			} catch (Exception e) {
				logger.error(e);
				throw new Exception("Error when load VBS web service--sendMessageService sendMobileMessage2----");
			}
		}
		return sendMessageServiceSoap;
	}
}
