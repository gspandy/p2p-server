package com.vcredit.jdev.p2p.chinapnr.util;

public class ChinapnrConstans {
	 
	//交易查询类型
    public abstract class QueryTransType {
    	public static final String LOANS = "LOANS";//放款交易查询
        public static final String REPAYMENT = "REPAYMENT"; //还款交易查询
        public static final String TENDER = "TENDER"; //投标交易查询 
        public static final String CASH = "CASH"; //取现交易查询 
        public static final String FREEZE = "FREEZE"; //冻结解冻交易查询
    }
    
	//对账类型
    public abstract class ReconciliationType {
    	public static final String TRFRE_CONCILIATION = "TrfReconciliation";//放款交易查询
        public static final String CASHRE_CONCILIATION = "CashReconciliation"; //还款交易查询
        public static final String SAVERE_CONCILIATION = "SaveReconciliation"; //投标交易查询 
        public static final String RECONCILIATION = "Reconciliation"; //放还款对账 
    }
    
    //支付网关业务代号
    public abstract class GateBusiId {
    	public static final String B2C = "B2C";//网银支付
    	public static final String B2B = "B2B"; //网银支付
    	public static final String FPAY = "FPAY"; //快捷支付 
    	public static final String WH = "WH"; //代扣
    }
    
    //是否冻结
    public abstract class IsFreeze {
    	public static final String YES = "Y";//是
    	public static final String NO = "N"; //否
    }
    
    //是否解冻
    public abstract class IsUnFreeze {
    	public static final String YES = "Y";//是
    	public static final String NO = "N"; //否
    }
    
    //投标计划类型
    public abstract class TenderPlayType {
    	public static final String W = "W";//全部授权
    	public static final String P = "P";//部分授权
    }
    
    //是否到银行卡
    public abstract class IsDefaultCard {
      	public static final String YES = "Y";//是
    	public static final String NO = "N"; //否
    }
    
    //取现渠道
    public abstract class CashChl {
    	public static final String FAST = "FAST";//快速取现
    	public static final String GENERAL = "GENERAL"; //一般取现
    	public static final String IMMEDIATE = "IMMEDIATE"; //即时取现
    }
    
    //手续费收取对象
    public abstract class FeeObjFlag {
    	public static final String M = "M";//向商户收取MERCHANT
    	public static final String U = "U"; //向用户收取USER
    }
    
    public static final String DCFLAG ="D";
    
    //cmdId
//    UserRegister //用户开户
//    UserBindCard //用户绑卡接口
//    UserLogin //用户登录接口
    

}
