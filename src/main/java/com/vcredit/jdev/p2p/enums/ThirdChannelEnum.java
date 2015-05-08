package com.vcredit.jdev.p2p.enums;

/**
 *三方接口方法调用名称<汇付cmdId>
 *@author周佩
 *
 */
public enum ThirdChannelEnum{
	/**应答返回码*/
	RESPCODE("RespCode"),
	/**应答返回码：值*/
	RESPCODEVALUE("000"),
	/**应答返回码：值*/
	RESPCODEVALUE395("395"),
	/**开户注册*/
	USERREGISTER("UserRegister"),
	/**用户绑卡*/
	USERBINDCARD("UserBindCard"),
	/**用户登录*/
	USERLOGIN("UserLogin"),
	/**账户信息修改（页面)*/
	ACCTMODIFY("AcctModify"),
	/**企业开户 需申请开通*/
	CORPREGISTER("CorpRegister"),
	/**删除银行卡*/
	DELCARD("DelCard"),
	/**充值*/
	NETSAVE("NetSave"),
	/**主动投标*/
	INITIATIVETENDER("InitiativeTender"),
	/**自动扣款（放款）*/
	LOANS("Loans"),
	/**自动扣款（还款）*/
	REPAYMENT("Repayment"),
	/**自动扣款转账（商户用）*/
	TRANSFER("Transfer"),
	/**取现*/
	CASH("Cash"),
	/**债权转让*/
	CREDITASSIGN("CreditAssign"),
	/**用户账户支付*/
	USRACCTPAY("UsrAcctPay"),
	/**前台用户间转账*/
	USRTRANSFER("UsrTransfer"),
	/**商户代取现*/
	MERCASH("MerCash"),
	/**投标撤销*/
	TENDERCANCLE("TenderCancle"),
	/**资金（货款）冻结*/
	USRFREEZEBG("UsrFreezeBg"),
	/**资金（货款）解冻*/
	USRUNFREEZE("UsrUnFreeze"),
	/**自动投标   需申请开通*/
	AUTOTENDER("AutoTender"),
	/**自动投标计划  需申请开通*/
	AUTOTENDERPLAN("AutoTenderPlan"),
	/**自动投标关闭  需申请开通*/
	AUTOTENDERPLANCLOSE("AutoTenderPlanClose"),
	/**取现复核*/
	CASHAUDIT("CashAudit"),
	/**自动债权转让*/
	AUTOCREDITASSIGN("AutoCreditAssign"),
	/**标的信息录入*/
	ADDBIDINFO("AddBidInfo"),
	/**批量还款接口*/
	BATCHREPAYMENT("BatchRepayment"),

	
	//*********************
	//********查询类第三类接口 cmdId
	//*******************
	/**余额查询(后台)*/
	QUERYBALANCEBG("QueryBalanceBg"),
	/**商户子账户信息查询*/
	QUERYACCTS("QueryAccts"),
	/**交易状态查询*/
	QUERYTRANSSTAT("QueryTransStat"),
	/**商户扣款对账*/
	TRFRECONCILIATION("TrfReconciliation"),
	/**放还款对账*/
	RECONCILIATION("Reconciliation"),
	/**取现对账*/
	CASHRECONCILIATION("CashReconciliation"),
	/**充值对账*/
	SAVERECONCILIATION("SaveReconciliation"),
	/**自动投标计划状态查询*/
	QUERYTENDERPLAN("QueryTenderPlan"),
	/**垫资手续费返还查询*/
	QUERYRETURNDZFEE("QueryReturnDzFee"),
	/**银行卡查询*/
	QUERYCARDINFO("QueryCardInfo"),
	/**担保类型企业开户状态查询*/
	CORPREGISTERQUERY("CorpRegisterQuery"),
	/**债权转让查询*/
	CREDITASSIGNRECONCILIATION("CreditAssignReconciliation"),
	/**用户信息查询*/
	QUERYUSRINFO("QueryUsrInfo"),
	/**交易明细查询*/
	QUERYTRANSDETAIL("QueryTransDetail"),
	
	
	/**用户后台绑卡*/
	BGBINDCARD("BgBindCard");


   ThirdChannelEnum(String code){
		this.code=code;
	}

	private String code;

	public String getCode(){
		return code;
	}
}


