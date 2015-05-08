package com.vcredit.jdev.p2p.chinapnr.controller;

import net.gplatform.sudoor.server.integration.AsyncEventMessageGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.entity.RequestLog;
import com.vcredit.jdev.p2p.entity.ResponseLog;
import com.vcredit.jdev.p2p.repository.RequestLogRepository;
import com.vcredit.jdev.p2p.repository.ResponseLogRepository;

/** 
* @ClassName: RequestLogActivator 
* @Description: 记录访问汇付日志接收类 
* @author dk 
* @date 2015年3月3日 下午3:24:14 
*  
*/
@MessageEndpoint
public class ChinapnrLogActivator {
	
	private static Logger logger = LoggerFactory.getLogger(ChinapnrLogActivator.class);
	
//	@Autowired
//	private AsyncEventMessageGateway asyncEventMessageGateway;//异步
	
	@Autowired
	private RequestLogRepository requestLogRepository;
	
	@Autowired
	private ResponseLogRepository responseLogRepository;
	
	/** 
	* @Title: handle 
	* @Description:
	*  @param event
	*  @return
	*  @throws Exception
	* @return Object    返回类型 
	* @throws 
	*/
	@ServiceActivator(inputChannel = "asyncEventPublishChannel")
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void handle(Object event) throws Exception {
		if(event instanceof RequestLog){//保存访问日志
			logger.debug("add call chinapnr interface RequestLog begin..........");
			logger.debug("add requestLog ..........");
			RequestLog requestLog = (RequestLog) event;
			requestLogRepository.save(requestLog);
			logger.debug("add call chinapnr interface RequestLog end..........");
		}else if(event instanceof ResponseLog){//保存响应日志
			logger.debug("add call chinapnr interface ResponseLog begin..........");
			logger.debug("add ResponseLog ..........");
			ResponseLog responseLog = (ResponseLog) event;
			responseLogRepository.save(responseLog);
			logger.debug("add call chinapnr interface ResponseLog end..........");
		}
	}

}
