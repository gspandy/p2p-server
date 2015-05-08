package com.vcredit.jdev.p2p.quratz;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.deal.service.BondPackageInvestManager;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;

@Component
public class TaskJobFactory {
	private Logger logger=LoggerFactory.getLogger(TaskJobFactory.class);
	@Autowired
	private InvestmentRepository investmentRepository;
	@Autowired
	private BondPackageInvestManager bondPackageInvestManager;
	
	//初始化定时计划
	@PostConstruct
	public void initJob() throws Exception{
		//从数据库获得应该流标的投资项目
		logger.info("initJob is start ...");
		//找出招标中的项目且结束时间已经小于当前时间满足的项目，然后将其上线
		List<Investment> investment = investmentRepository.findByInvestmentStatusAndInvestmentEndDateLessThan(InvestmentStatusEnum.ON_LINE.getCode(),
				new Date(), new Sort(Direction.DESC, "investmentCreateDate"));
		for (Investment i : investment) {
			//开启一个线程，在内存中执行定时流标120/119/118/117/93/92
	        Timer timer = new Timer();
	        Date d=new Date();
	        long delay=i.getInvestmentEndDate().getTime()-d.getTime();
	        delay = delay<0?1000l:delay;
	        timer.schedule(new TaskJob(i,bondPackageInvestManager), delay);
//			new Thread(new TaskJob(i,bondPackageInvestManager)).start();
		}
	}
	
	/**
	 * 添加流标项目
	 * @param inv
	 */
	public void addJob(Investment inv){
		//将应该流标的投资项目添加到定时任务中去
		logger.info("initJob is start ...");
        Timer timer = new Timer();
        Date d=new Date();
        long delay=inv.getInvestmentEndDate().getTime()-d.getTime();
        delay = delay<0?1000l:delay;
        timer.schedule(new TaskJob(inv,bondPackageInvestManager), delay);
//		new Thread(new TaskJob(inv,bondPackageInvestManager)).start();
	}
}
