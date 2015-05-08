package com.vcredit.jdev.p2p.quratz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vcredit.jdev.p2p.deal.service.BondPackageInvestManager;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.util.DateUtil;

/**
 * 主要作用是创建定时任务，将应该流标的项目进行流标
 * 
 * @author 周佩 创建时间：20150313
 */
public class TaskJob extends java.util.TimerTask implements Job,Runnable {

	private Logger logger = LoggerFactory.getLogger(TaskJob.class);
	
	private Investment i;
	
	@Autowired
	private BondPackageInvestManager bondPackageInvestManager;
	// 静态变量
	public static final String INVESTMENTSEQUENCE = "InvestmentSequence";
	public static final String STARTDATE = "startDate";
	public static final String ENDDATE = "endDate";
	public static final String NAME = "name";
	public static final String BONDPACKAGEINVESTMANAGER = "bondPackageInvestManager";

	public TaskJob() {
	}
	
	public TaskJob(Investment i,BondPackageInvestManager bondPackageInvestManager) {
		this.i= i;
		this.bondPackageInvestManager = bondPackageInvestManager;
	}
	
	/**
	 * 创建一个定时任务
	 * @throws Exception 
	 */
	private void createScheduler() throws Exception{
		if(i==null||bondPackageInvestManager==null){
			return ;
		}
		logger.info("------- Initializing ------------");
		logger.info("create schedule factory.");
		StdSchedulerFactory sf = new StdSchedulerFactory();
		Properties props = new Properties();
		props.put("org.quartz.scheduler.instanceName", i.getInvestmentName());
		props.put("org.quartz.threadPool.threadCount", "100");
		sf.initialize(props);
		Scheduler s = sf.getScheduler();
		System.out.println(s);
		logger.info("------- Initializing complete----");
		Date runTime = i.getInvestmentEndDate();
		logger.info("------- create job----");
		JobDetail job = newJob(TaskJob.class).withIdentity("job"+i.getInvestmentName(), "group1").build();
		job.getJobDataMap().put(TaskJob.INVESTMENTSEQUENCE, ""+i.getInvestmentSequence());
		job.getJobDataMap().put(TaskJob.STARTDATE, DateUtil.format(i.getInvestmentStartDate(),DateUtil.DATE_FORMAT_RECORD_DATE));
		job.getJobDataMap().put(TaskJob.ENDDATE, DateUtil.format(i.getInvestmentEndDate(),DateUtil.DATE_FORMAT_RECORD_DATE));
		job.getJobDataMap().put(TaskJob.NAME, i.getInvestmentName());
		job.getJobDataMap().put(TaskJob.BONDPACKAGEINVESTMANAGER, bondPackageInvestManager);
		logger.info("------- create trigger----");
		Trigger trigger = newTrigger().withIdentity("trigger"+i.getInvestmentName(), "group1").startAt(runTime).build();
		// 注册并进行调度  
        s.scheduleJob(job, trigger);
        // 启动调度器  
        s.standby();
        s.start();
        logger.info("结束运行。。。。");
        s.shutdown(true);
	}
	
	/**
	 * 项目流标
	 * 
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("faildInvestment is start ...");
		// job 的名字
		String jobName = context.getJobDetail().getKey().getName();
		// 任务执行的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒");
		String jobRunTime = dateFormat.format(Calendar.getInstance().getTime());
		logger.info("jobName:" + jobName + "\n\texecuteTime:" + jobRunTime);
		// 获取 JobDataMap , 并从中取出参数
		JobDataMap data = context.getJobDetail().getJobDataMap();
		String investmentSequence = data.getString(INVESTMENTSEQUENCE);
		String startDate = data.getString(STARTDATE);
		String endDate = data.getString(ENDDATE);
		String name = data.getString(NAME);
		BondPackageInvestManager bondPackageInvestManager = (BondPackageInvestManager) data.get(BONDPACKAGEINVESTMANAGER);
		logger.info("开始执行流标操作！！！！项目ID："+investmentSequence+";项目名称："+name+";项目开始时间："+startDate+";项目结束时间："+endDate+";");
		bondPackageInvestManager.investFailure(Long.valueOf(investmentSequence));
		logger.info("faildInvestment is end.");
	}

	@Override
	public void run() {
		try {
			bondPackageInvestManager.investFailure(i.getInvestmentSequence());
		} catch (Exception e) {
			logger.error("create faildInvestment scheduler is error.",e);
		}
		
	}
}
