package com.vcredit.jdev.p2p.chinapnr.account;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.chinapnr.util.HttpClientUtil;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;

@Component
public class UserManager {
	private static Logger logger = Logger.getLogger(UserManager.class);

	@Autowired
	private SignUtils signUtils;

	/** 
	* @Title: getUserRegisterParam 
	* @Description: 取注册参数 
	* @param usrId 用户Id
	* @param usrName 用户真实姓名
	* @param idType  证件类型
	* @param idNo    证件号码
	* @param usrMp   手机
	* @param usrEmail 邮箱
	* @param @return    设定文件 
	* @return Map<String,String>    返回类型 
	* @throws 
	*/
	public Map<String, String> getUserRegisterParam(String usrId,String usrName,String idType,
			String idNo,String usrMp,String usrEmail,String merPriv,Integer accountRole){
		logger.debug("getUserRegisterParam begin...................");
		String version = signUtils.getVersion1();
		String cmdId = "UserRegister";
		String merCustId = signUtils.getMerCustId();
		String bgRetUrl = signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
		String retUrl = null;
		if(ConstantsUtil.AccountRole.INVESTOR==accountRole){
			retUrl =  signUtils.getRetUrl()+"/handler/callbackRetUrl";
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("BgRetUrl", bgRetUrl);
		
		params.put("RetUrl", retUrl);
		


		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
//		Version  +  CmdId  +  MerCustId  +  BgRetUrl  + 
//		RetUrl  + UsrId + UsrName + IdType + IdNo + 
//		UsrMp + UsrEmail+ MerPri
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
		.append(StringUtils.trimToEmpty(cmdId))
		.append(StringUtils.trimToEmpty(merCustId))
		.append(StringUtils.trimToEmpty(bgRetUrl))
		.append(StringUtils.trimToEmpty(retUrl));
		
		if(StringUtils.isNotBlank(usrId)){
			params.put("UsrId", usrId);
			buffer.append(StringUtils.trimToEmpty(usrId));
		}
		if(StringUtils.isNotBlank(usrName)){
			params.put("UsrName", usrName);
			buffer.append(StringUtils.trimToEmpty(usrName));
		}
		if(StringUtils.isNotBlank(idType)){
			params.put("IdType", idType);
			buffer.append(StringUtils.trimToEmpty(idType));
		}
		if(StringUtils.isNotBlank(idNo)){
			params.put("IdNo", idNo);
			buffer.append(StringUtils.trimToEmpty(idNo));
		}
		if(StringUtils.isNotBlank(usrMp)){
			 params.put("UsrMp", usrMp);
			 buffer.append(StringUtils.trimToEmpty(usrMp));
		}
		if(StringUtils.isNotBlank(usrEmail)){
			params.put("UsrEmail", usrEmail);
			buffer.append(StringUtils.trimToEmpty(usrEmail));
		}
		if(StringUtils.isNotBlank(merPriv)){
			params.put("MerPriv", merPriv);
			buffer.append(StringUtils.trimToEmpty(merPriv));
		}
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		 try {
				params.put("ChkValue", signUtils.encryptByRSA(plainStr));
			} catch (Exception e) {
				logger.error("getUserRegisterParam--> encryptByRSA error",e);
				logger.error("用户开户加签失败!");
			}
		 signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrId_"+usrId);
		 logger.debug("getUserRegisterParam end...................");
		 return params;

	}
	
	/** 
	* @Title: getUserLoginParam 
	* @Description: 用户登录 
	* @param usrCusId 用户客户号
	* @param @return    设定文件 
	* @return Map<String,String>    返回类型 
	* @throws 
	*/
	public Map<String, String> getUserLoginParam(String usrCusId){
		logger.debug("getUserLoginParam begin...................");
		String version = signUtils.getVersion1();
        String cmdId = "UserLogin";
        String merCustId = signUtils.getMerCustId();

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("UsrCustId", usrCusId);
        logger.debug("getUserLoginParam end...................");
		return params;
	}
	
	/** 
	* @Title: getUserBindCardParam 
	* @Description: 用户绑卡接口 
	* @param usrCusId
	* @param @return    设定文件 
	* @return Map<String,String>    返回类型 
	* @throws 
	*/
	public Map<String, String> getUserBindCardParam(String usrCusId){
		logger.debug("getUserBindCardParam begin...................");
		String version = signUtils.getVersion1();
        String cmdId = "UserBindCard";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl =  signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("UsrCustId", usrCusId);
        params.put("BgRetUrl", bgRetUrl);
        //params.put("MerPriv", merPriv);

      
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
//        Version + 
//        CmdId + MerCustId + UsrCustId + 
//        BgRetUrl+MerPriv
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(usrCusId))
            .append(StringUtils.trimToEmpty(bgRetUrl));
        String plainStr = buffer.toString();
        System.out.println(plainStr);
        
        try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getQueryBalanceBgParam--> encryptByRSA error",e);
		}
        signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrCustId_"+usrCusId);
        logger.debug("getUserBindCardParam end...................");
        return params;
	}
	
	
	/** 
	* @Title: getBgBindCardParam 
	* @Description: 后台绑卡参数
	*  @param usrCusId   用户客户号
	*  @param openAcctId 开户银行账号(取现银行的账户号（银行卡号）)
	*  @param openBankId 开户银行代号 (如:农行-ABC)
	*  @param openProvId 开户银行省份
	*  @param openAreaId 开户银行地区
	*  @param openBranchName 开户银行支行名称(选填)-取现银行支行名称，具体参见“附件一：开户银行代号”
	*  @param isDefault  Y：设置为默认取现卡   N：不设置为默认取现卡
	*  @return
	* @return Map<String,String>    返回类型 
	* @throws 
	*/
	private Map<String, String> getBgBindCardParam(String usrCusId,
			String openAcctId,String openBankId,String openProvId,String openAreaId,
			String openBranchName,String isDefault){
		String version = signUtils.getVersion1();
		String cmdId = "BgBindCard";
		String merCustId = signUtils.getMerCustId();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("UsrCustId", usrCusId);
		params.put("OpenAcctId", openAcctId);
		params.put("OpenBankId", openBankId);
		params.put("OpenProvId", openProvId);
		params.put("OpenAreaId", openAreaId);
		params.put("OpenBranchName", openBranchName);
		params.put("IsDefault", isDefault);
		
		
		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
//		Version +CmdId + MerCustId + UsrCustId + OpenAcctId+ 
//		OpenBankId + OpenProvId + OpenAreaId + 
//		OpenBranchName + IsDefault + MerPriv
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
		.append(StringUtils.trimToEmpty(cmdId))
		.append(StringUtils.trimToEmpty(merCustId))
		.append(StringUtils.trimToEmpty(usrCusId))
		.append(StringUtils.trimToEmpty(openAcctId))
		.append(StringUtils.trimToEmpty(openBankId))
		.append(StringUtils.trimToEmpty(openProvId))
		.append(StringUtils.trimToEmpty(openAreaId))
		.append(StringUtils.trimToEmpty(openBranchName))
		.append(StringUtils.trimToEmpty(isDefault));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		
		try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getBgBindCardParam--> encryptByRSA error",e);
		}
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrCustId_"+usrCusId+" OpenAcctId_"+openAcctId);
		return params;
	}
	
	/** 
	* @Title: getDelCardParam 
	* @Description: 删除银行卡接口参数 
	* @param usrCusId
	* @param cardId
	* @param @return    设定文件 
	* @return Map<String,String>    返回类型 
	* @throws 
	*/
	private Map<String, String> getDelCardParam(String usrCusId,String cardId){
		String version = signUtils.getVersion1();
		String cmdId = "UserLogin";
		String merCustId = signUtils.getMerCustId();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("UsrCustId", usrCusId);
		params.put("BgRetUrl", cardId);
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
		.append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(usrCusId))
		.append(StringUtils.trimToEmpty(cardId));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		
		try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getDelCardParam--> encryptByRSA error",e);
		}
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrCustId_"+usrCusId);
		return params;
	}
	
	/** 
	* @Title: getAcctModifyParam 
	* @Description: 账户信息修改（页面）接口参数 
	* @param usrCusId
	* @param @return    设定文件 
	* @return Map<String,String>    返回类型 
	* @throws 
	*/
	public Map<String, String> getAcctModifyParam(String usrCusId){
		String version = signUtils.getVersion1();
		String cmdId = "AcctModify";
		String merCustId = signUtils.getMerCustId();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("UsrCustId", usrCusId);
		//params.put("MerPriv", merPriv);
		
		
		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
//        Version + 
//        CmdId + MerCustId + UsrCustId + 
//        BgRetUrl+MerPriv
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
		.append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(usrCusId));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		
		try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getAcctModifyParam--> encryptByRSA error",e);
		}
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrCustId_"+usrCusId);
		return params;
	}
	
	/** 
	* @Title: delCard 
	* @Description: 删除银行卡接口 
	* @param usrCusId
	* @param cardId
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String delCard(String usrCusId,String cardId){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(), 
					getDelCardParam(usrCusId, cardId));
		} catch (Exception e) {
			logger.error("delCard--> sendPostRequest error",e);
			logger.error("删除银行卡接口 delCard error",e);
		}
		return HttpClientUtil.getResult(response);
	}
	
	/** 
	* @Title: bgBindCard 
	* @Description: 后台绑卡 
	*  @param usrCusId   用户客户号
	*  @param openAcctId 开户银行账号(取现银行的账户号（银行卡号）)
	*  @param openBankId 开户银行代号 (如:农行-ABC)
	*  @param openProvId 开户银行省份 省份代码为两位，传输时加00补足4位，如北京11 openProvId=0011
	*  @param openAreaId 开户银行地区
	*  @param openBranchName 开户银行支行名称(选填)-取现银行支行名称，具体参见“附件一：开户银行代号”
	*  @param isDefault  Y：设置为默认取现卡   N：不设置为默认取现卡
	*  @return
	* @return String    返回类型 
	* @throws 
	*/
	public String bgBindCard(String usrCusId,
			String openAcctId,String openBankId,String openProvId,String openAreaId,
			String openBranchName,String isDefault){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(), 
					getBgBindCardParam(usrCusId, openAcctId, openBankId, openProvId, openAreaId, openBranchName, isDefault));
			
		} catch (Exception e) {
			logger.error("bgBindCard--> sendPostRequest error",e);
			logger.error("后台绑卡接口 bgBindCard error",e);
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("BgBindCard",
		// result);
		// } catch (JsonProcessingException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		return result;
	}

}
