package com.vcredit.jdev.p2p.quratz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.base.util.PropertiesUtils;
import com.vcredit.jdev.p2p.capital.modal.CheckingAccountmanager;
import com.vcredit.jdev.p2p.deal.service.BondPackageInvestManager;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.InvestmentTypeEnum;
import com.vcredit.jdev.p2p.merchant.modal.MerchantManager;
import com.vcredit.jdev.p2p.repository.DictionaryRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;

/**
 * 定时守护线程
 * 
 * @author 周佩
 *
 */
@Component
@EnableScheduling
@ConditionalOnExpression("'${application.quratz}' == 'true'")
public class CommonalityQuratz {

	private Logger logger = LoggerFactory.getLogger(CommonalityQuratz.class);

	@Autowired
	private InvestmentRepository investmentRepository;
	@Autowired
	private MerchantManager merchantManager;

	@Autowired
	private BondPackageInvestManager bondPackageInvestManager;
	@Autowired
	private DictionaryRepository dictionaryRepository;
	@Autowired
	private PropertiesUtils propertiesUtils;
	@Autowired
	private CheckingAccountmanager checkingAccountmanager;

	/**
	 * 项目流标
	 */
	@Scheduled(cron = "0 01 01 * * ?")
	public void onLineProject() {
//		logger.info("开始流标工作......");
//		//执行流标
//		merchantManager.investmentFailing();
//		logger.info("结束流标工作......");
		logger.info("开始项目上线工作......");
		//查询当前的上线项目是否达到一个最大值
		//所有上线项目
		List<Investment> result1 = investmentRepository.findByInvestmentStatus(InvestmentStatusEnum.ON_LINE.getCode());
		//定时上线的项目
		List<Investment> result2 = investmentRepository.findByInvestmentStatusAndInvestmentType(InvestmentStatusEnum.ON_LINE.getCode(),
				InvestmentTypeEnum.TIMEING.getCode(), new Sort(Direction.DESC, "investmentCreateDate"));
		//需要上线的项目数目
		//增加从数据字典中获取项目上线数据，然后替换下面的上线数目
		int onlineProjectNum = propertiesUtils.getDefaultMaxProject() - (result1.size() - result2.size());
		if (onlineProjectNum > 0) {
			//执行项目上线
			merchantManager.investmentOnline(onlineProjectNum);
		}
		logger.info("结束项目上线工作......");

		//用户账户信息比对********************************begin
		try {
			logger.info("开始本地账户跟汇付账户进行比对工作......");
			checkingAccountmanager.synchronizeThirdAccount(null);
			logger.info("结束本地账户跟汇付账户进行比对工作......");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//用户账户信息比对********************************end

		//用户银行卡信息比对********************************begin
		try {
			logger.info("开始银行卡跟汇付账户进行比对工作......");
			checkingAccountmanager.synchronizeAccountBankCard(null);
			logger.info("结束银行卡跟汇付账户进行比对工作......");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//用户银行卡信息比对********************************end
	}

	/**
	 * 定时上线
	 */
	@Scheduled(cron = "0 30 10 * * ?")
	public void time1() {
		//执行定时上线
		merchantManager.investmentTimeingOnline();
	}

	/**
	 * 定时上线
	 */
	@Scheduled(cron = "0 30 13 * * ?")
	public void time2() {
		//执行定时上线
		merchantManager.investmentTimeingOnline();
	}

	/**
	 * 定时上线
	 */
	@Scheduled(cron = "0 30 17 * * ?")
	public void time3() {
		//执行定时上线
		merchantManager.investmentTimeingOnline();
	}

	/**
	 * 定时上线
	 */
	@Scheduled(cron = "0 30 20 * * ?")
	public void time4() {
		//执行定时上线
		merchantManager.investmentTimeingOnline();
	}
}
