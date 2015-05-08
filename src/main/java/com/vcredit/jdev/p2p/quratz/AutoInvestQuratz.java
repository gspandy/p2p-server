package com.vcredit.jdev.p2p.quratz;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.deal.service.BondPackageInvestManager;
import com.vcredit.jdev.p2p.entity.OperationRecord;
import com.vcredit.jdev.p2p.enums.OpTypeEnum;
import com.vcredit.jdev.p2p.repository.OperationRecordRepository;

/**
 * 自动投资定时任务
 * 
 * @author 朱球
 *
 */
@Component
@EnableScheduling
@ConditionalOnExpression("'${application.quratz}' == 'true'")
public class AutoInvestQuratz {

	private Logger logger = LoggerFactory.getLogger(AutoInvestQuratz.class);

	@Autowired
	private BondPackageInvestManager bondPackageInvestManager;

	@Autowired
	private OperationRecordRepository operationRecordRepository;

	/**
	 * 项目流标
	 */
	@Scheduled(cron = "0 */3 * * * ?")
	public void onLineProject() {
		logger.info("开始自动投资......");
		long start = System.currentTimeMillis();
		OperationRecord operationRecord = new OperationRecord();
		operationRecord.setOperateDate(new Date(System.currentTimeMillis()));
		operationRecord.setOperateType(OpTypeEnum.AUTO_INVEST_BEGIN.getCode());
		operationRecordRepository.save(operationRecord);

		bondPackageInvestManager.autoInvest();

		operationRecord = new OperationRecord();
		operationRecord.setOperateDate(new Date(System.currentTimeMillis()));
		operationRecord.setOperateType(OpTypeEnum.AUTO_INVEST_END.getCode());
		operationRecordRepository.save(operationRecord);

		long end = System.currentTimeMillis();
		logger.info("结束自动投资......cost:" + (end - start) / 1000 + "S");

	}
}
