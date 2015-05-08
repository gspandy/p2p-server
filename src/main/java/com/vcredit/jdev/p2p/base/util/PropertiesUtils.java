package com.vcredit.jdev.p2p.base.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 获得配置文件的相关信息
 * @author 周佩
 *
 */
@Component
public class PropertiesUtils {

	/** 上线项目默认存活时间 **/
	@Value("${investment.DEFAULT_LIMIT_DAY}")
	int defaultLimitDay;
	/** 周五上线项目存活时间 **/
	@Value("${investment.FRIDAY_LIMIT_DAY}")
	int fridayLimitDay;
	/** 上线项目默认存活时间 **/
	@Value("${investment.DEFAULT_MAX_PROJECT}")
	int defaultMaxProject;
	
	@Value("${sudoor.application.url}")
	String appUrl;
	public int getDefaultLimitDay() {
		return defaultLimitDay;
	}
	public void setDefaultLimitDay(int defaultLimitDay) {
		this.defaultLimitDay = defaultLimitDay;
	}
	public int getFridayLimitDay() {
		return fridayLimitDay;
	}
	public void setFridayLimitDay(int fridayLimitDay) {
		this.fridayLimitDay = fridayLimitDay;
	}
	public int getDefaultMaxProject() {
		return defaultMaxProject;
	}
	public void setDefaultMaxProject(int defaultMaxProject) {
		this.defaultMaxProject = defaultMaxProject;
	}
	
	public String getAppUrl(){
		return appUrl;
	}
	
}
