package com.vcredit.jdev.p2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.ResponseLog;


/** 
* @ClassName: ResponseLogRepository 
* @Description: 记录汇付响应Repository
* @author dk 
* @date 2015年3月5日 上午10:41:18 
*  
*/
public interface ResponseLogRepository extends JpaRepository<ResponseLog, Long> {

}
