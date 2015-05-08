package com.vcredit.jdev.p2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.RequestLog;

/** 
* @ClassName: RequestLogRepository 
* @Description: 记录第三方接口访问日志 
* @author dk 
* @date 2015年3月3日 下午3:34:52 
*  
*/
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

}
