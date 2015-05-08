package com.vcredit.jdev.p2p.chinapnr.deal;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.chinapnr.util.HttpClientUtil;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;

@Component
public class BidInfoManager {
	
	private static Logger logger = LoggerFactory.getLogger(BidInfoManager.class);

	@Autowired
	private SignUtils signUtils;
	
	/**
     * 标的录入请求参数
     * @throws Exception 
     * 
     */
    private Map<String, String> getAddBidInfoParams(String proId,String borrCustId,
    		String borrTotAmt,String yearRate,String retType,String bidStartDate,
    		String bidEndDate,String retAmt,String retDate,String guarCompId,
    		String guarAmt,String proArea) throws Exception {
    	

        String version = signUtils.getVersion1();
        String cmdId = "AddBidInfo";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl = signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("ProId", proId);
        params.put("BorrCustId", borrCustId);
        
        params.put("BorrTotAmt", borrTotAmt);
       
        //TODO 2015/3/11 学彦要求 13.00% =0.13 -->13.00 begin
        logger.debug("#########yearRate format before:yearRate="+yearRate);
        BigDecimal decimal = new BigDecimal(yearRate).multiply(new BigDecimal(100));
        yearRate = decimal.setScale(2, BigDecimal.ROUND_DOWN).toString();
        logger.debug("#########yearRate format after:yearRate="+yearRate);
        //end
        params.put("YearRate", yearRate);
        
        params.put("RetType", retType);
        params.put("BidStartDate", bidStartDate);
        params.put("BidEndDate", bidEndDate);
        
       
        params.put("RetAmt", retAmt);
        params.put("RetDate", retDate);
        params.put("ProArea", proArea);
        params.put("BgRetUrl", bgRetUrl);
        //params.put("MerPriv", merPriv);

//    	Version+CmdId+MerCustId+ProId+BorrCustId
//    	+BorrTotAmt+YearRate+RetType+BidStartDate
//    	+BidEndDate+RetAmt+RetDate+GuarCompId
//    	+GuarAmt+ProArea+BgRetUrl+MerPriv+
//    	ReqExt
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(proId))
            .append(StringUtils.trimToEmpty(borrCustId)).append(StringUtils.trimToEmpty(borrTotAmt))
            .append(StringUtils.trimToEmpty(yearRate)).append(StringUtils.trimToEmpty(retType))
            .append(StringUtils.trimToEmpty(bidStartDate)).append(StringUtils.trimToEmpty(bidEndDate))
            .append(StringUtils.trimToEmpty(retAmt)).append(StringUtils.trimToEmpty(retDate))
            .append(StringUtils.trimToEmpty(proArea)).append(StringUtils.trimToEmpty(bgRetUrl));
        String plainStr = buffer.toString();
        System.out.println(plainStr);
        
        params.put("ChkValue", signUtils.encryptByRSA(plainStr));
        signUtils.addRequestChinapnrLogEventPublish(cmdId,"ProId_"+proId);
        return params;
    }
    
	/** 
	* @Title: addBidInfo 
	* @Description: 标的录入 
	* @param proId
	* @param borrCustId
	* @param borrTotAmt
	* @param yearRate
	* @param retType
	* @param bidStartDate
	* @param bidEndDate
	* @param retAmt
	* @param retDate
	* @param guarCompId 担保公司 ID
	* @param guarAmt 担保金额 
	* @param proArea
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String addBidInfo(String proId,String borrCustId,
    		String borrTotAmt,String yearRate,String retType,String bidStartDate,
    		String bidEndDate,String retAmt,String retDate,String guarCompId,
    		String guarAmt,String proArea){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(), 
					getAddBidInfoParams(proId, borrCustId, borrTotAmt, yearRate,
							retType, bidStartDate, bidEndDate, retAmt,
							retDate, guarCompId, guarAmt, proArea));
		} catch (Exception e) {
			logger.error(" 标的录入 addBidInfo error",e);
		}
		return HttpClientUtil.getResult(response);
	}
	
	
    /** 
    * @Title: getInitiativeTenderParams 
    * @Description: 主动投标(v20)
    * @param borrowerDetails
    * @param ordId
    * @param ordDate
    * @param transAmt
    * @param usrCusId
    * @param maxTenderRate
    * @param isFreeze
    * @param @return
    * @param @throws Exception    设定文件 
    * @return Map<String,String>    返回类型 
    * @throws 
    */
    public Map<String, String> getInitiativeTenderParams(String borrowerDetails,String ordId,
    		String ordDate,String transAmt,String usrCusId,String maxTenderRate,
    		String isFreeze,String freezeOrdId) throws Exception {
    	
    	String version =  signUtils.getVersion2();
        String cmdId = "InitiativeTender";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl = signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
        String retUrl = signUtils.getRetUrl()+"/handler/callbackRetUrl";
        // 若为中文，请用Base64转码
        //String merPriv = HttpClientHandler.getBase64Encode("11");

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("UsrCustId", usrCusId);
        params.put("OrdId", ordId);
        params.put("OrdDate", ordDate);
        params.put("TransAmt", transAmt);
       //TODO 2015/3/11 学彦要求 maxTenderRate=0.20 begin
        maxTenderRate="0.20";
        params.put("MaxTenderRate", maxTenderRate);
        //end
        params.put("BorrowerDetails", borrowerDetails);
        params.put("IsFreeze", isFreeze);
        params.put("BgRetUrl", bgRetUrl);
        params.put("RetUrl", retUrl);
//        Version +CmdId + MerCustId + OrdId + 
//        OrdDate + TransAmt + UsrCustId + 
//        MaxTenderRate + BorrowerDetails + IsFreeze+ 
//        FreezeOrdId+ RetUrl +BgRetUrl + MerPriv+ 
//        ReqExt
      
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
//        Version + 
//        CmdId + MerCustId + UsrCustId + 
//        BgRetUrl+MerPriv
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(ordId))
            .append(StringUtils.trimToEmpty(ordDate)).append(StringUtils.trimToEmpty(transAmt))
            .append(StringUtils.trimToEmpty(usrCusId)).append(StringUtils.trimToEmpty(maxTenderRate))
            .append(StringUtils.trimToEmpty(borrowerDetails))
            .append(StringUtils.trimToEmpty(isFreeze));
	        if(StringUtils.isNotBlank(freezeOrdId)){
	            params.put("FreezeOrdId", freezeOrdId);
	        	buffer.append(StringUtils.trimToEmpty(freezeOrdId));
	        }
            buffer.append(StringUtils.trimToEmpty(retUrl));
            buffer.append(StringUtils.trimToEmpty(bgRetUrl));
        String plainStr = buffer.toString();
        System.out.println(plainStr);
		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
        return params;
    }
    
    
    
    private Map<String, String> getAutoTenderParams(String borrowerDetails,String ordId,
    		String ordDate,String transAmt,String usrCusId,String maxTenderRate,
    		String isFreeze,String freezeOrdId) throws Exception {
    	
    	String version =  signUtils.getVersion2();
        String cmdId = "AutoTender";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl = signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
       // String retUrl = "http://127.0.0.1/p2p_trade_demo-JAVA/tbfinish";
        // 若为中文，请用Base64转码
        //String merPriv = HttpClientHandler.getBase64Encode("11");

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("UsrCustId", usrCusId);
        params.put("OrdId", ordId);
        params.put("OrdDate", ordDate);
        params.put("TransAmt", transAmt);
        //TODO 2015/3/11 学彦要求 maxTenderRate=0.20 begin
        maxTenderRate="0.20";
        params.put("MaxTenderRate", maxTenderRate);
        //end
        params.put("BorrowerDetails", borrowerDetails);
        params.put("IsFreeze", isFreeze);
        params.put("FreezeOrdId", freezeOrdId);
        params.put("BgRetUrl", bgRetUrl);
       // params.put("RetUrl", retUrl);
//        Version +CmdId + MerCustId + OrdId + 
//        OrdDate + TransAmt + UsrCustId + 
//        MaxTenderRate + BorrowerDetails + IsFreeze+ 
//        FreezeOrdId+ RetUrl +BgRetUrl + MerPriv+ 
//        ReqExt
      
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
//        Version + 
//        CmdId + MerCustId + UsrCustId + 
//        BgRetUrl+MerPriv
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(ordId))
            .append(StringUtils.trimToEmpty(ordDate)).append(StringUtils.trimToEmpty(transAmt))
            .append(StringUtils.trimToEmpty(usrCusId)).append(StringUtils.trimToEmpty(maxTenderRate))
            .append(StringUtils.trimToEmpty(borrowerDetails))
            .append(StringUtils.trimToEmpty(isFreeze))
            .append(StringUtils.trimToEmpty(freezeOrdId))
//            .append(StringUtils.trimToEmpty(retUrl))
            .append(StringUtils.trimToEmpty(bgRetUrl));
        String plainStr = buffer.toString();
        System.out.println(plainStr);
        
        try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			logger.error("getAutoTenderParams-->encryptByRSA error",e);
		}
        signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
        return params;
    }
    
    
    /** 
    * @Title: getAutoTenderPlanParams 
    * @Description: 自动投标计划 
    * @param usrCusId
    * @param tenderPlanType
    * @param @return
    * @param @throws Exception    设定文件 
    * @return Map<String,String>    返回类型 
    * @throws 
    */
    public Map<String, String> getAutoTenderPlanParams(String usrCusId,String tenderPlanType,String merPriv) throws Exception {
    	
    	String version =  signUtils.getVersion1();
    	String cmdId = "AutoTenderPlan";
    	String merCustId = signUtils.getMerCustId();
    	//TODO 是returl 不是BgRetUrl
    	String retUrl = signUtils.getRetUrl()+"/handler/callbackRetUrl";
    	// 若为中文，请用Base64转码
    	//String merPriv = HttpClientHandler.getBase64Encode("11");
    	
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("Version", version);
    	params.put("CmdId", cmdId);
    	params.put("MerCustId", merCustId);
    	params.put("UsrCustId", usrCusId);
    	params.put("TenderPlanType", tenderPlanType);
    	params.put("RetUrl", retUrl);
    	params.put("MerPriv", merPriv);
    	
    	// 组装加签字符串原文
    	// 注意加签字符串的组装顺序参 请照接口文档
//    	Version 
//    	+CmdId + MerCustId + UsrCustId + 
//    	TenderPlanType + TransAmt + RetUrl + MerPriv
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
    	.append(StringUtils.trimToEmpty(merCustId))
    	.append(StringUtils.trimToEmpty(usrCusId))
    	.append(StringUtils.trimToEmpty(tenderPlanType))
    	.append(StringUtils.trimToEmpty(retUrl))
    	.append(StringUtils.trimToEmpty(merPriv));
    	String plainStr = buffer.toString();
    	System.out.println(plainStr);
    	
    	try {
    		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
    	} catch (Exception e) {
    		logger.error("getAutoTenderPlanParams-->encryptByRSA error",e);
    	}
    	signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrCustId_"+usrCusId);
    	return params;
    }
    
    
    /** 
    * @Title: getAutoTenderPlanCloseParams 
    * @Description: 自动投标计划关闭 
    * @param usrCusId
    * @param @return
    * @param @throws Exception    设定文件 
    * @return Map<String,String>    返回类型 
    * @throws 
    */
    public Map<String, String> getAutoTenderPlanCloseParams(String usrCusId,String merPriv) throws Exception {
    	
    	String version =  signUtils.getVersion1();
    	String cmdId = "AutoTenderPlanClose";
    	String merCustId = signUtils.getMerCustId();
    	//TODO 是returl 不是BgRetUrl
    	String retUrl = signUtils.getRetUrl()+"/handler/callbackRetUrl";
    	// 若为中文，请用Base64转码
    	//String merPriv = HttpClientHandler.getBase64Encode("11");
    	
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("Version", version);
    	params.put("CmdId", cmdId);
    	params.put("MerCustId", merCustId);
    	params.put("UsrCustId", usrCusId);
    	params.put("RetUrl", retUrl);
    	params.put("MerPriv", merPriv);
    	
    	// 组装加签字符串原文
    	// 注意加签字符串的组装顺序参 请照接口文档
//    	Version +CmdId + MerCustId + UsrCustId + 
//    	RetUrl + MerPriv
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
    	.append(StringUtils.trimToEmpty(merCustId))
    	.append(StringUtils.trimToEmpty(usrCusId))
    	.append(StringUtils.trimToEmpty(retUrl))
    	.append(StringUtils.trimToEmpty(merPriv));
    	String plainStr = buffer.toString();
    	System.out.println(plainStr);
    	
    	try {
    		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
    	} catch (Exception e) {
    		logger.error("getAutoTenderPlanCloseParams-->encryptByRSA error",e);
    	}
    	signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrCustId_"+usrCusId);
    	return params;
    }
    
    
   
	/** 
	* @Title: getAutoTender 
	* @Description: 自动投标 （v20）
	* @param borrowerDetails
	* @param ordId
	* @param ordDate
	* @param transAmt
	* @param usrCusId
	* @param maxTenderRate
	* @param isFreeze
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String autoTender(String borrowerDetails,String ordId,
    		String ordDate,String transAmt,String usrCusId,String maxTenderRate,
    		String isFreeze,String freezeOrdId){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(), 
					getAutoTenderParams(borrowerDetails, ordId, ordDate, transAmt, usrCusId, maxTenderRate, isFreeze,freezeOrdId));
		} catch (Exception e) {
			logger.error("自动投标  autoTender error",e);
			
		}
		return HttpClientUtil.getResult(response);
	}
	

	

}
