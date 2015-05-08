package com.vcredit.jdev.p2p.chinapnr.deal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.chinapnr.util.HttpClientUtil;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;

@Component
public class CreditManager {
	
	private static Logger logger = Logger.getLogger(CreditManager.class);

	@Autowired
	private SignUtils signUtils;
	
	 /**
     * 自动债权转让接口请求参数
     * @throws Exception 
     * 
     */
    private Map<String, String> getAutoCreditAssignParams(String sellCustId,String creditAmt,
    		String creditDealAmt,String bidDetails,String fee,String buyCustId,
    		String ordId,String ordDate) throws Exception {
    	
    	String version =  signUtils.getVersion2();
    	String cmdId = "AutoTender";
    	String merCustId = signUtils.getMerCustId();
    	String bgRetUrl = "http://127.0.0.1/p2p_trade_demo-JAVA/tbfinish";
    	String retUrl = "http://127.0.0.1/p2p_trade_demo-JAVA/tbfinish";
    	// 若为中文，请用Base64转码
    	//String merPriv = HttpClientHandler.getBase64Encode("11");
    	
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("Version", version);
    	params.put("CmdId", cmdId);
    	params.put("MerCustId", merCustId);
    	params.put("SellCustId", sellCustId);
    	params.put("CreditAmt", creditAmt);
    	params.put("CreditDealAmt", creditDealAmt);
    	params.put("BidDetails", bidDetails);
    	params.put("Fee", fee);
    	params.put("BuyCustId", buyCustId);
    	params.put("OrdId", ordId);
    	params.put("OrdDate", ordDate);
    	params.put("BgRetUrl", bgRetUrl);
    	params.put("RetUrl", retUrl);

//    	Version + CmdId + MerCustId + SellCustId + 
//    	CreditAmt + CreditDealAmt + BidDetails + 
//    	Fee + DivDetails + BuyCustId + OrdId + 
//    	OrdDate + RetUrl + BgRetUrl + MerPriv+ 
//    	ReqExt
    	// 组装加签字符串原文
    	// 注意加签字符串的组装顺序参 请照接口文档
//        Version + 
//        CmdId + MerCustId + UsrCustId + 
//        BgRetUrl+MerPriv
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
    	.append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(sellCustId))
    	.append(StringUtils.trimToEmpty(creditAmt)).append(StringUtils.trimToEmpty(creditDealAmt))
    	.append(StringUtils.trimToEmpty(bidDetails)).append(StringUtils.trimToEmpty(fee))
    	.append(StringUtils.trimToEmpty(buyCustId))
    	.append(StringUtils.trimToEmpty(ordId))
    	.append(StringUtils.trimToEmpty(ordDate))
    	.append(StringUtils.trimToEmpty(retUrl))
    	.append(StringUtils.trimToEmpty(bgRetUrl));
    	String plainStr = buffer.toString();
    	System.out.println(plainStr);
    	
    	try {
    		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
    	} catch (Exception e) {
    		logger.error("getAutoCreditAssignParams->encryptByRSA error",e);
    	}
    	signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
    	return params;
    }
    
	
    /** 
    * @Title: getCreditAssignParams 
    * @Description:  债权转让接口 
    * @param sellCustId
    * @param creditAmt
    * @param creditDealAmt
    * @param bidDetails
    * @param buyCustId
    * @param ordId
    * @param ordDate
    * @param @return
    * @param @throws Exception    设定文件 
    * @return Map<String,String>    返回类型 
    * @throws 
    */
    private Map<String, String> getCreditAssignParams(String sellCustId,String creditAmt,
    		String creditDealAmt,String bidDetails,String buyCustId,String ordId,
    		String ordDate) throws Exception {


        String version = signUtils.getVersion1();
        String cmdId = "CreditAssign";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl = "http://127.0.0.1/p2p_trade_demo-JAVA/loans";

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("SellCustId", sellCustId);
        params.put("CreditAmt", creditAmt);
        
        params.put("CreditDealAmt", creditDealAmt);
        params.put("BidDetails", bidDetails);
        params.put("BuyCustId", buyCustId);
        params.put("OrdId", ordId);
        params.put("OrdDate", ordDate);
       
        params.put("BgRetUrl", bgRetUrl);
        //params.put("MerPriv", merPriv);

//    	Version + CmdId + MerCustId + SellCustId + 
//    	CreditAmt  +  CreditDealAmt  +  BidDetails  + 
//    	Fee  +  DivDetails  +  BuyCustId  +  OrdId  + 
//    	OrdDate  +  RetUrl  +  BgRetUrl  +  MerPriv+ 
//    	ReqExt 
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(sellCustId))
            .append(StringUtils.trimToEmpty(creditAmt)).append(StringUtils.trimToEmpty(creditDealAmt))
            .append(StringUtils.trimToEmpty(bidDetails)).append(StringUtils.trimToEmpty(buyCustId))
            .append(StringUtils.trimToEmpty(ordId)).append(StringUtils.trimToEmpty(ordDate))
            .append(StringUtils.trimToEmpty(bgRetUrl));
        String plainStr = buffer.toString();
        System.out.println(plainStr);
        
        params.put("ChkValue", signUtils.encryptByRSA(plainStr));
        signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
        return params;
    }
    
	/** 
	* @Title: getCreditAssign 
	* @Description:  债权转让接口  
	* @param sellCustId
	* @param creditAmt
	* @param creditDealAmt
	* @param bidDetails
	* @param buyCustId
	* @param ordId
	* @param ordDate
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getCreditAssign(String sellCustId,String creditAmt,
    		String creditDealAmt,String bidDetails,String buyCustId,String ordId,
    		String ordDate){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(), 
					getCreditAssignParams(sellCustId, creditAmt, creditDealAmt, 
							bidDetails, buyCustId, ordId, ordDate));
		} catch (Exception e) {
			logger.error("getCreditAssign error",e);
		}
		return HttpClientUtil.getResult(response);
	}
	
	/** 
	* @Title: autoCreditAssign 
	* @Description: 自动债权转让接口 
	* @param sellCustId
	* @param creditAmt
	* @param creditDealAmt
	* @param bidDetails
	* @param fee
	* @param buyCustId
	* @param ordId
	* @param ordDate
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String autoCreditAssign(String sellCustId,String creditAmt,
    		String creditDealAmt,String bidDetails,String fee,String buyCustId,
    		String ordId,String ordDate){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(), 
					getAutoCreditAssignParams(sellCustId, creditAmt, creditDealAmt,
							bidDetails, fee, buyCustId, ordId, ordDate));
		} catch (Exception e) {
			logger.error("autoCreditAssign error",e);
		}
		return HttpClientUtil.getResult(response);
	}

}
