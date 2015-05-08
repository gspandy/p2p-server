package com.vcredit.jdev.p2p.chinapnr.deal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.chinapnr.util.ChinapnrConstans;
import com.vcredit.jdev.p2p.chinapnr.util.HttpClientUtil;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;

@Component
public class FundManager {
	
	private static Logger logger = Logger.getLogger(FundManager.class);

	@Autowired
	private SignUtils signUtils;

	private Map<String, String> getUsrFreezeBgParam(String ordId,String ordDate,String usrCustId,
			String transAmt){
		
		String version = signUtils.getVersion1();
		String cmdId = "UsrFreezeBg";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl =signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
        // 若为中文，请用Base64转码

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("UsrCustId", usrCustId);
        params.put("OrdId", ordId);
        params.put("OrdDate", ordDate);
        params.put("TransAmt", transAmt);
        params.put("BgRetUrl", bgRetUrl);

      
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(usrCustId))
            .append(StringUtils.trimToEmpty(ordId))
            .append(StringUtils.trimToEmpty(ordDate))
            .append(StringUtils.trimToEmpty(transAmt))
            .append(StringUtils.trimToEmpty(bgRetUrl));
        String plainStr = buffer.toString();
        System.out.println(plainStr);
        
        try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			logger.error("getUsrFreezeBgParam->encryptByRSA error",e);
		}
        signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
        return params;
	}


	private Map<String, String> getUsrUnFreezeParam(String ordId,String ordDate,String trxId){
		
		String version = signUtils.getVersion1();
		String cmdId = "UsrUnFreeze";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl = signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
        // 若为中文，请用Base64转码

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("OrdId", ordId);
        params.put("OrdDate", ordDate);
        params.put("TrxId", trxId);
        params.put("BgRetUrl", bgRetUrl);

      
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId))
            .append(StringUtils.trimToEmpty(ordId))
            .append(StringUtils.trimToEmpty(ordDate))
            .append(StringUtils.trimToEmpty(trxId))
            .append(StringUtils.trimToEmpty(bgRetUrl));
        String plainStr = buffer.toString();
        System.out.println(plainStr);
        
        try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			logger.error("getUsrUnFreezeParam->encryptByRSA error",e);
		}
        signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
        return params;
	}

	/**资金（货款）解冻*/
	public String getUsrUnFreeze(String ordId,String ordDate,String trxId){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(),
					getUsrUnFreezeParam(ordId, ordDate, trxId));
		} catch (Exception e) {
			logger.error("资金（货款）解冻  getUsrUnFreeze:",e);
			
		}
		return HttpClientUtil.getResult(response);
	}
	
	
	/** 
	* @Title: getUsrFreezeBg 
	* @Description: 资金（货款）冻结 
	* @param ordId
	* @param ordDate
	* @param usrCustId
	* @param transAmt
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getUsrFreezeBg(String ordId,String ordDate,String usrCustId,
			String transAmt){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(), getUsrFreezeBgParam(ordId, ordDate, usrCustId, transAmt));
		} catch (Exception e) {
			logger.error("资金（货款）冻结  getUsrFreezeBg:",e);
			
		}
		return HttpClientUtil.getResult(response);
	}
	

	/** 
	* @Title: getNetSaveParam 
	* @Description: 网银充值  
	*  @param usrCusId 用户客户号
	*  @param ordId 订单号
	*  @param ordDate 订单日期
	*  @param transAmt 交易金额
	*  @param openBankId 开户银行代号
	*   GateBusiId，OpenBankId，DcFlag 只有在同时都有值时才有作用
	*  @param merPriv
	*  @param type:1-->投资者        0--->商户代充值
	*  @return
	* @return Map<String,String>    返回类型 
	* @throws 
	*/
	public Map<String, String> getNetSaveParam(String usrCusId,String ordId,String ordDate,
			String transAmt,String openBankId,String merPriv,int type){
		logger.debug("getNetSaveParam-->NetSave-->begin--------------");
		String version = signUtils.getVersion1();
		String cmdId = "NetSave";
		String merCustId = signUtils.getMerCustId();
		String bgRetUrl =signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
		String retUrl = "";
		//if(type==1){
		       retUrl = signUtils.getRetUrl()+"/handler/callbackRetUrl";
		// }
		//type:1-->投资者        0--->商户代充值
		// 支付网关业务代号  GateBusiId 投资者-->B2C 商户--->B2B
		String gateBusiId =ChinapnrConstans.GateBusiId.B2B;
		if(type==1){
			gateBusiId =ChinapnrConstans.GateBusiId.B2C;
		}
		logger.debug("getNetSaveParam-->NetSave-->gateBusiId:"+gateBusiId);
		String dcFlag =  ChinapnrConstans.DCFLAG;
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
		params.put("BgRetUrl", bgRetUrl);
		params.put("MerPriv", merPriv);
		params.put("RetUrl", retUrl);


		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		//        Version +CmdId + MerCustId + UsrCustId + 
		//        OrdId+  OrdDate  +  GateBusiId+ 
		//        OpenBankId+  DcFlag  +TransAmt+  RetUrl+ 
		//        BgRetUrl+ MerPriv
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
		.append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(usrCusId))
		.append(StringUtils.trimToEmpty(ordId))
		.append(StringUtils.trimToEmpty(ordDate));

		//TODO  GateBusiId，OpenBankId，DcFlag 只有在同时都有值时才有作用
		//支付网关业务代号     
		if(StringUtils.isNotBlank(gateBusiId)){
			params.put("GateBusiId", gateBusiId);
			buffer.append(StringUtils.trimToEmpty(gateBusiId));
		}
		//开户银行代号
		if(StringUtils.isNotBlank(openBankId)){
			params.put("OpenBankId", openBankId);
			buffer.append(StringUtils.trimToEmpty(openBankId));
		}
		//借贷记标记
		if(StringUtils.isNotBlank(dcFlag)){
			params.put("DcFlag", dcFlag);
			buffer.append(StringUtils.trimToEmpty(dcFlag));
		}
		buffer.append(StringUtils.trimToEmpty(transAmt))
		.append(StringUtils.trimToEmpty(retUrl))
		.append(StringUtils.trimToEmpty(bgRetUrl))
		.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		try {
			params.put("ChkValue",signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			logger.error("getNetSaveParam->encryptByRSA error",e);
			
		}
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
		logger.debug("getNetSaveParam-->NetSave-->end--------------");
		return params;
	}
	
	/** 
	* @Title: getCashParam 
	* @Description: 页面取现  v20 
	* @param usrCustId
	* @param ordId
	* @param transAmt
	* @param servFee
	* @param servFeeAcctId
	* @param openAcctId 取现银行的账户号（银行卡号） 
	* @param reqExt [{"FeeObjFlag":"M/U","FeeAcctId":"MDT00001","CashChl":"FAST|GENERAL|IMMEDIATE"}]
	*   手续费收取对象  FeeObjFlag 用户商户指定取现手续费的收取对象，优先级别 于商户配置的收取对象 M：向商户收取 U：向用户收取;
		手续费收取子账户  FeeAcctId FeeObjFlag=M.必填 FeeObjFlag=U.忽略;
		取现渠道  CashChl FAST  快速取现 GENERAL  一般取现 IMMEDIATE  即时取现   (可传的取现通道范围小于等于商户配置的取现通道)
	* @param @return    设定文件 
	* @return Map<String,String>    返回类型 
	* @throws 
	*/
	public Map<String, String> getCashParam(String usrCustId,String ordId,String transAmt,
			String servFee,String servFeeAcctId,String openAcctId,String reqExt,String merPriv,int type){
		String version = signUtils.getVersion2();
        String cmdId = "Cash";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl =signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
        String retUrl = "";
       // if(type==1){
        	retUrl = signUtils.getRetUrl()+"/handler/callbackRetUrl";
        //}
        // 若为中文，请用Base64转码
        //String merPriv = HttpClientHandler.getBase64Encode("11");

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("UsrCustId", usrCustId);
        params.put("OrdId", ordId);
        params.put("TransAmt", transAmt);
        params.put("MerPriv", merPriv);
        params.put("BgRetUrl", bgRetUrl);
        params.put("RetUrl", retUrl);

      
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档

//        Version +CmdId + MerCustId+ OrdId + 
//        UsrCustId + TransAmt+ ServFee+ 
//        ServFeeAcctId +OpenAcctId + RetUrl + 
//        BgRetUrl+ Remark+ MerPriv + ReqExt 
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version))
             .append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId))
            .append(StringUtils.trimToEmpty(ordId))
            .append(StringUtils.trimToEmpty(usrCustId))
            .append(StringUtils.trimToEmpty(transAmt));
        
        if(StringUtils.isNotBlank(servFee)){
        	params.put("ServFee", servFee);
        	buffer.append(StringUtils.trimToEmpty(servFee));
        }
        if(StringUtils.isNotBlank(servFeeAcctId)){
        	params.put("ServFeeAcctId", servFeeAcctId);
        	buffer.append(StringUtils.trimToEmpty(servFeeAcctId));
        }
        if(StringUtils.isNotBlank(openAcctId)){
        	params.put("OpenAcctId", openAcctId);
        	buffer.append(StringUtils.trimToEmpty(openAcctId));
        }
        
        //retUrl&BgRetUrl&merPriv
        buffer.append(StringUtils.trimToEmpty(retUrl))
              .append(StringUtils.trimToEmpty(bgRetUrl))
              .append(StringUtils.trimToEmpty(merPriv));
       
        //reqExt
        if(StringUtils.isNotBlank(reqExt)){
        	 params.put("ReqExt", reqExt);
        	 buffer.append(StringUtils.trimToEmpty(reqExt));
        }
        String plainStr = buffer.toString();
        System.out.println(plainStr);
        
        try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			logger.error("getCashParam->encryptByRSA error",e);
		}
        signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
        return params;
	}
	
	/** 
	* @Title: getCashParam 
	* @Description: 商户代取现接口 
	* @param usrCusId
	* @param ordId
	* @param ordDate
	* @param transAmt
	* @param @return    设定文件 
	* @return Map<String,String>    返回类型 
	* @throws 
	*/
	private Map<String, String> getMerCashParam(String usrCustId,String ordId,String transAmt
			,String servFee,String servFeeAcctId){
		String version = signUtils.getVersion2();
        String cmdId = "MerCash";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl = signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
        //String RetUrl = "http://127.0.0.1/p2p_trade_demo-JAVA/cashFinish";
        // 若为中文，请用Base64转码
        //String merPriv = HttpClientHandler.getBase64Encode("11");

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("UsrCustId", usrCustId);
        params.put("OrdId", ordId);
        params.put("TransAmt", transAmt);
        params.put("ServFee", servFee);
        params.put("ServFeeAcctId", servFeeAcctId);
        params.put("BgRetUrl", bgRetUrl);
        //params.put("RetUrl", RetUrl);

      
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
//        Version + 
//        CmdId + MerCustId + UsrCustId + 
//        BgRetUrl+MerPriv
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(ordId))
            .append(StringUtils.trimToEmpty(usrCustId))
            .append(StringUtils.trimToEmpty(transAmt))
            .append(StringUtils.trimToEmpty(servFee))
            .append(StringUtils.trimToEmpty(servFeeAcctId))
            //.append(StringUtils.trimToEmpty(RetUrl))
            .append(StringUtils.trimToEmpty(bgRetUrl));
        String plainStr = buffer.toString();
        System.out.println(plainStr);
        
        try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			logger.error("getMerCashParam->encryptByRSA error",e);
			
		}
        signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
        return params;
	}
	
	/** 
	* @Title: getMerCash 
	* @Description: 商户代取现接口 v20 
	* @param ordId
	* @param usrCustId
	* @param transAmt
	* @param servFee
	* @param servFeeAcctId
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String getMerCash(String ordId,String usrCustId,
			String transAmt,String servFee,String servFeeAcctId){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(),
					getMerCashParam(usrCustId, ordId, transAmt, servFee, servFeeAcctId));
		} catch (Exception e) {
			logger.error("商户代取现接口 MerCash error:",e);
			
		}
		return HttpClientUtil.getResult(response);
	}

}
