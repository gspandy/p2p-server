package com.vcredit.jdev.p2p.enums;
/**
 * 地区 类型
 * @author zhaopeijun
 *
 */
public enum AreaLevelEnum {
	
	/** 地区-省 */
	AREA_PROVINCE(1,"省"),
	
	/** 地区-直辖市 */
	AERA_SELF_GOVERNED_CITY(2,"直辖市"),
	
	/** 地区-直辖市 */
	AERA_CITY(3,"地级市"),
	
	/** 地区-区县 */
	AERA_DISTRICT(4,"区县");
	
	AreaLevelEnum(Integer level,String levelName) {
		this.level = level;
		this.levelName = levelName;
	}

	private Integer level;
	private String levelName;

	public Integer getLevel() {
		return level;
	}
	
	public String getLevelName() {
		return levelName;
	}
}
