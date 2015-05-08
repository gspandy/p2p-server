package com.vcredit.jdev.p2p.base.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ConstantsUtil {
    //实体状态
    public abstract class EntityStatus {
    	public static final int STATUS_ENABLE = 0;//实体可用状态
        public static final int STATUS_DISABLE = 1; //实体不可用状态

    }
    
    public abstract class AccountRole {
    	public static final int INVESTOR = 1;//投资人
    	public static final int BORROWER = 2; //借款人
    	public static final int INVESTORANDBORROWER = 3; //投资人&&借款人
    	
    }
    
    //vbs推送数据到p2p，导入参数
    public abstract class SyncParam {
    	public static final String SYNC_TYPE = "sync_type"; //同步方式
    	public static final String SYNC_DATA = "sync_data"; //同步数据
    	public static final String LOAN_LIST = "loan_list"; //查询标的参数
    }
    
    //vbs 同步类型
    public abstract class VbsSyncType {
    	public static final String BEFORE_LOAN = "before_loan"; //贷前推送
    	public static final String AFTER_LOAN = "after_loan"; //贷后推送
    }
    
    public abstract class BeforeLoanData {
    	public static final String EMAIL = "Email"; 
    	public static final String COMPANY_SIZE = "CompanySize";
    	public static final String Hometown = "Hometown";
    	public static final String Work_City = "WorkCity";
    	public static final String Work_Province = "WorkProvince"; //loan_data设值时用，由 workCity解析而来
    	public static final String SALARY_DATE = "SalaryDate";
    	public static final String House_Loan_Property = "HouseLoanProperty";
    }
    
    public abstract class AfterLoanData {
    	public static final String LOAN_PERIOD = "LoanPeriod";
//    	public static final String ADVANCE_BILLTYPE = "2"; //提前清贷账单 
//    	public static final String USUAL_BILLTYPE = "1"; //普通账单
    }
    
    public final static Map SALARY_TYPEMAP = new HashMap<String, String>();
    static{
    	SALARY_TYPEMAP.put("工薪", "1");
    	SALARY_TYPEMAP.put("非法人且大股东", "2");
    	SALARY_TYPEMAP.put("非法人且小股东", "3");
    	SALARY_TYPEMAP.put("其他", ""); //
    }
    public static final String SALARY_STR = "工薪族";
    
    public final static Map LAWYER_TYPEMAP = new HashMap<String, String>();
    static{
    	LAWYER_TYPEMAP.put("法人", "1");
    }
    public static final String LAWYER_STR = "私营业主";
    
    public final static Map MARRY_TYPEMAP = new HashMap<String, String>();
    static{
    	MARRY_TYPEMAP.put("未说明婚姻状况", "1");
    }
    public static final String OTHER_STR = "其他";
    
    public final static Map HOUSE_HAS_TYPEMAP = new HashMap<String, String>();
    static{
    	HOUSE_HAS_TYPEMAP.put("自置", "1");
    	HOUSE_HAS_TYPEMAP.put("按揭", "2");
    	HOUSE_HAS_TYPEMAP.put("共有住宅", "3");
    }
    public static final String HOUSE_HAS = "有";
    public final static Map HOUSE_NONE_TYPEMAP = new HashMap<String, String>();
    static{
    	HOUSE_NONE_TYPEMAP.put("亲属楼宇", "1");
    	HOUSE_NONE_TYPEMAP.put("集体宿舍", "2");
    	HOUSE_NONE_TYPEMAP.put("租赁", "3");
    	HOUSE_NONE_TYPEMAP.put("公房", "4");
    	HOUSE_NONE_TYPEMAP.put("其他", "5");
    }
    public static final String HOUSE_NONE = "无";
    
    public final static Map HOUSE_LOAN_TYPEMAP = new HashMap<String, String>();
    static{
    	HOUSE_LOAN_TYPEMAP.put("按揭", "1");
    }
    
    // p2p数据库状态 对应 转换给VBS的状态 待发布
    public final static Map INV2VBS_STATUS1_MAP = new HashMap<String, String>();
    static{
    	INV2VBS_STATUS1_MAP.put("1", "");
    	INV2VBS_STATUS1_MAP.put("2", "");
    	INV2VBS_STATUS1_MAP.put("3", "");
    	INV2VBS_STATUS1_MAP.put("4", "");
    	INV2VBS_STATUS1_MAP.put("22", "");
    }
    
    // p2p数据库状态 对应 转换给VBS的状态 招标中
    public final static Map INV2VBS_STATUS2_MAP = new HashMap<String, String>();
    static{
    	INV2VBS_STATUS2_MAP.put("12", "");
    }
    
    // p2p数据库状态 对应 转换给VBS的状态 已流标
    public final static Map INV2VBS_STATUS3_MAP = new HashMap<String, String>();
    static{
    	INV2VBS_STATUS3_MAP.put("16", "");
    }
    
    //  p2p数据库状态 对应 转换给VBS的状态 已满标
    public final static Map INV2VBS_STATUS4_MAP = new HashMap<String, String>();
    static{
    	INV2VBS_STATUS4_MAP.put("5", "");
    	INV2VBS_STATUS4_MAP.put("6", "");
    	INV2VBS_STATUS4_MAP.put("13", "");
    	INV2VBS_STATUS4_MAP.put("14", "");
    }
    
    //  p2p数据库状态 对应 转换给VBS的状态 已放款
    public final static Map INV2VBS_STATUS5_MAP = new HashMap<String, String>();
    static{
    	INV2VBS_STATUS5_MAP.put("7", "");
    	INV2VBS_STATUS5_MAP.put("8", "");
    	INV2VBS_STATUS5_MAP.put("9", "");
    	INV2VBS_STATUS5_MAP.put("10", "");
    	INV2VBS_STATUS5_MAP.put("11", "");
    	INV2VBS_STATUS5_MAP.put("17", "");
    	INV2VBS_STATUS5_MAP.put("15", "");
    	INV2VBS_STATUS5_MAP.put("19", "");
    	INV2VBS_STATUS5_MAP.put("20", "");
    	INV2VBS_STATUS5_MAP.put("21", "");
    }
    
    /** 省级别和市级别分离 */
    public final static String SPLIT_AREA = "_";
    
    
    public static final int FLOD_INDEX = 8; //8位小数
    public static final int FLOD_INDEX2 = 2; //2位小数
    public static final int ROUND_HALF_UP = BigDecimal.ROUND_HALF_UP; //四舍五入
    public static final String UNALLOCATED_OR_INTRANET_IP= "未分配或者内网IP";//未分配或者内网IP
    
    public static final String BORROWER_UNAME_PREFIX="LS";

}
