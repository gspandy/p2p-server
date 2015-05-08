package com.vcredit.jdev.p2p.enums;

/** 
* @ClassName: OrderIDRuleEnum 
* @Description: 订单ID生成规则枚举
* @author dk 
* @date 2015年2月13日 下午5:04:23 
*  
*/
public enum OrderIDRuleEnum {
	//网银充值
	NETSAVE("1"),
	//提现
	CASH("2"),
	//投资冻结,投资资金解冻,投资放款,债权转让投资,债权转让本金回收
	FREEZE("3"),
	//本息收入,罚息收入,垫付本息收入,回购收入,提前清贷本金收入,提前清贷补偿金收入
	INTEREST("4"),
	//提现手续费支出,投资管理费支出,债权转让手续费支出,提现手续费收入,维信手续费收入,平台管理费收入,风险备用金收入,投资管理费收入
	//维信垫付本息支出,维信垫付平台管理费支出,回购剩余本金支出,回购垫付金额支出,债权转让手续费收入
	FEE("5"),
	//红包奖励,红包赠送支出
	REDENVELOPE("6")
	;
	
	OrderIDRuleEnum(String code) {
		this.code = code;
	}

	private String code;

	public String getCode() {
		return code;
	}

	public String toString() {
		return String.valueOf(this.code);
	}
}
