package com.vcredit.jdev.p2p.chinapnr.deal;

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
public class LoansManager {
	
	private static Logger logger = LoggerFactory.getLogger(LoansManager.class);

	@Autowired
	private SignUtils signUtils;

	private Map<String, String> getLoansParam(String ordId,String ordDate,String outCustId,
			String transAmt,String fee,String subOrdId,String subOrdDate,String inCustId,
			String divDetails,String isDefault,String isUnFreeze,
			String unFreezeOrdId,String freezeTrxId,String feeObjFlag,String reqExt){
		
		String version = signUtils.getVersion2();
		String cmdId = "Loans";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl = signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
        // 若为中文，请用Base64转码

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("OrdId", ordId);
        params.put("OrdDate", ordDate);
        params.put("OutCustId", outCustId);
        params.put("TransAmt", transAmt);
        params.put("Fee", fee);
        params.put("SubOrdId", subOrdId);
        params.put("SubOrdDate", subOrdDate);
        params.put("InCustId", inCustId);
        params.put("DivDetails", divDetails);
        params.put("IsDefault", isDefault);
        params.put("IsUnFreeze", isUnFreeze);
        params.put("UnFreezeOrdId", unFreezeOrdId);
        params.put("FreezeTrxId", freezeTrxId);
        params.put("FeeObjFlag", feeObjFlag);
        //params.put("ProId", proId);
        params.put("BgRetUrl", bgRetUrl);
        params.put("ReqExt", reqExt);

//        Version 
//        +CmdId + MerCustId + OrdId + OrdDate + 
//        OutCustId+ TransAmt+ Fee+ SubOrdId+ 
//        SubOrdDate+ InCustId+ DivDetails+ 
//        FeeObjFlag+ IsDefault + IsUnFreeze+ 
//        UnFreezeOrdId+ FreezeTrxId + BgRetUrl+ 
//        MerPriv+ ReqExt
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version)).append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId)).append(StringUtils.trimToEmpty(ordId))
            .append(StringUtils.trimToEmpty(ordDate)).append(StringUtils.trimToEmpty(outCustId))
            .append(StringUtils.trimToEmpty(transAmt)).append(StringUtils.trimToEmpty(fee))
            .append(StringUtils.trimToEmpty(subOrdId)).append(StringUtils.trimToEmpty(subOrdDate))
            .append(StringUtils.trimToEmpty(inCustId)).append(StringUtils.trimToEmpty(divDetails))
            .append(StringUtils.trimToEmpty(feeObjFlag))
            .append(StringUtils.trimToEmpty(isDefault))
            .append(StringUtils.trimToEmpty(isUnFreeze))
            .append(StringUtils.trimToEmpty(unFreezeOrdId))
            .append(StringUtils.trimToEmpty(freezeTrxId))
            .append(StringUtils.trimToEmpty(bgRetUrl))
            .append(StringUtils.trimToEmpty(reqExt));
        String plainStr = buffer.toString();
        System.out.println(plainStr);
        
        try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			logger.error("getLoansParam->encryptByRSA error",e);
		}
        signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
        return params;
	}


    /**
     * 还款接口请求参数
     * @throws Exception 
     * 
     */
    private Map<String, String> getRepayParams(String ordId,String ordDate,String outCustId,String outAcctId,
			String transAmt,String fee,String subOrdId,String subOrdDate,String inCustId,
			String divDetails,String feeObjFlag,String reqExt) throws Exception {
    	String version = signUtils.getVersion2();
		String cmdId = "Repayment";
        String merCustId = signUtils.getMerCustId();
        String bgRetUrl = signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
    	
        // 若为中文，请用Base64转码

        Map<String, String> params = new HashMap<String, String>();
        params.put("Version", version);
        params.put("CmdId", cmdId);
        params.put("MerCustId", merCustId);
        params.put("OrdId", ordId);
        params.put("OrdDate", ordDate);
        
        params.put("OutCustId", outCustId);
        params.put("OutAcctId", outAcctId);
        params.put("TransAmt", transAmt);
        params.put("Fee", fee);
        params.put("SubOrdId", subOrdId);
        params.put("SubOrdDate", subOrdDate);
        
        params.put("DivDetails", divDetails); 
        params.put("FeeObjFlag", feeObjFlag); 
        params.put("InCustId", inCustId);
        params.put("BgRetUrl", bgRetUrl);
        params.put("ReqExt", reqExt);
        
//        Version +CmdId + MerCustId + OrdId + 
//        OrdDate + OutCustId + SubOrdId+ 
//        SubOrdDate+ OutAcctId + TransAmt+ Fee 
//        + InCustId+ InAcctId+ DivDetails+ 
//        FeeObjFlag+ BgRetUrl+ MerPriv + ReqExt
      
        // 组装加签字符串原文
        // 注意加签字符串的组装顺序参 请照接口文档
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.trimToEmpty(version))
            .append(StringUtils.trimToEmpty(cmdId))
            .append(StringUtils.trimToEmpty(merCustId))
            .append(StringUtils.trimToEmpty(ordId))
            .append(StringUtils.trimToEmpty(ordDate))
            .append(StringUtils.trimToEmpty(outCustId))
            .append(StringUtils.trimToEmpty(subOrdId))
            .append(StringUtils.trimToEmpty(subOrdDate))
            .append(StringUtils.trimToEmpty(outAcctId))
            .append(StringUtils.trimToEmpty(transAmt))
            .append(StringUtils.trimToEmpty(fee))
            .append(StringUtils.trimToEmpty(inCustId))
            .append(StringUtils.trimToEmpty(divDetails))
            .append(StringUtils.trimToEmpty(feeObjFlag))
            .append(StringUtils.trimToEmpty(bgRetUrl))
            .append(StringUtils.trimToEmpty(reqExt));
        String plainStr = buffer.toString();
        System.out.println(plainStr);
        
        params.put("ChkValue", signUtils.encryptByRSA(plainStr));
        
        signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
        return params;
    }
    
    /**
     * 批量还款接口 
     */
	/**
	{
	    "InDetails": [ //还款账户串
	        {
	            "DivDetails": [  //Fee 分账账户串
	                {
	                    "DivAcctId": "MDT000001", //分账账户号 
	                    "DivAmt": "1.00",         // 分账金额
	                    "DivCustId": "xxx"        //分账商户号 
	                }
	            ],
	            "Fee": "1.00",          // 扣款手续费 此字段为空时 FeeObjFlag，DivDetails 可不填
	            "FeeObjFlag": "I",      // 手续费收取对象标志  I/O ， I--向入款客户号 InCustId 收取O--向出款客户号 OutCustId 收取
	            "InAcctId": "xxx",      //入账子账号 
	            "InCustId": "xxx",      //入账客户号 
	            "OrdId": "201412102144",//还款订单号
	            "SubOrdId": "xxx",      //原投标订单号
	            "TransAmt": "5.00"      // 交易金额
	        },
	        {
	            "DivDetails": [
	                {
	                    "DivAcctId": "MDT000001",
	                    "DivAmt": "1.00",
	                    "DivCustId": "xxx"
	                }
	            ],
	            "Fee": "1.00",
	            "FeeObjFlag": "O",
	            "InAcctId": "xxx",
	            "InCustId": "xxx",
	            "OrdId": "xxx",
	            "SubOrdId": "xxx",
	            "TransAmt": "5.00"
	        }
	    ]
	}
	**/
    private Map<String, String> getBatchRepaymentParams(String outCustId,
    		String outAcctId,String batchId,String merOrdDate,
    		String inDetails,String proId) throws Exception {
    	String version = signUtils.getVersion2();
    	String cmdId = "BatchRepayment";
    	String merCustId = signUtils.getMerCustId();
    	String bgRetUrl = signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";
    	
    	// 若为中文，请用Base64转码
    	
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("Version", version);
    	params.put("CmdId", cmdId);
    	params.put("MerCustId", merCustId);
    	params.put("OutCustId", outCustId);
    	params.put("OutAcctId", outAcctId);
    	params.put("BatchId", batchId);
    	
    	params.put("MerOrdDate", merOrdDate);
    	params.put("InDetails", inDetails);
    	params.put("ProId", proId);
    	params.put("BgRetUrl", bgRetUrl);
    	
//    	Version 
//    	+CmdId + MerCustId + OutCustId + 
//    	OutAcctId+ BatchId+ MerOrdDate + InDetails
//    	+ BgRetUrl+ MerPriv + ReqExt+ ProId
    	
    	// 组装加签字符串原文
    	// 注意加签字符串的组装顺序参 请照接口文档
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(StringUtils.trimToEmpty(version))
    	.append(StringUtils.trimToEmpty(cmdId))
    	.append(StringUtils.trimToEmpty(merCustId))
    	.append(StringUtils.trimToEmpty(outCustId))
    	.append(StringUtils.trimToEmpty(outAcctId))
    	.append(StringUtils.trimToEmpty(batchId))
    	.append(StringUtils.trimToEmpty(merOrdDate))
    	.append(StringUtils.trimToEmpty(inDetails))
    	.append(StringUtils.trimToEmpty(proId))
    	.append(StringUtils.trimToEmpty(bgRetUrl));
    	String plainStr = buffer.toString();
    	System.out.println(plainStr);
    	
    	params.put("ChkValue", signUtils.encryptByRSA(plainStr));
    	signUtils.addRequestChinapnrLogEventPublish(cmdId,"BatchId_"+batchId+" ProId_"+proId);
    	return params;
    }
    
    
	private Map<String, String> getTransferParam(String ordId,String outCustId,
			String outAcctId,String transAmt,String inCustId,String inAcctId,String merPriv){
		String version = signUtils.getVersion1();
		String cmdId = "Transfer";
		String bgRetUrl = signUtils.getBgRetUrl()+"/handler/callbackBgRetUrl";

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("OrdId", ordId);
		params.put("OutCustId", outCustId);
		params.put("OutAcctId", outAcctId);

		params.put("TransAmt", transAmt);
		params.put("InCustId", inCustId);
	
		params.put("BgRetUrl", bgRetUrl);
		params.put("MerPriv", merPriv);
		//    	Version +CmdId + OrdId + OutCustId + 
		//    	OutAcctId + TransAmt+ InCustId+ InAcctId+ 
		//    	RetUrl + BgRetUrl+ MerPriv

		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
		.append(StringUtils.trimToEmpty(cmdId))
		.append(StringUtils.trimToEmpty(ordId))
		.append(StringUtils.trimToEmpty(outCustId))
		.append(StringUtils.trimToEmpty(outAcctId))
		.append(StringUtils.trimToEmpty(transAmt))
		.append(StringUtils.trimToEmpty(inCustId));
		if(StringUtils.isNotBlank(inAcctId)){
			params.put("InAcctId", inAcctId);
			buffer.append(StringUtils.trimToEmpty(inAcctId));
		}
		buffer.append(StringUtils.trimToEmpty(bgRetUrl)).
		       append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getTransferParam->encryptByRSA error",e);
		}
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
		return params;
	}

	/** 
	* @Title: transfer 
	* @Description: 自动扣款转账（商户用) 
	*               出账请指定 OutAcctId，入账请指定 InAcctId
	*               只能扣商户的账户
	* @param ordId 订单号
	* @param outCustId 出账客户号 
	* @param outAcctId 出账子账户 
	* @param transAmt  交易金额
	* @param inCustId  入账客户号
	* @param inAcctId  入账子账户
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String transfer(String ordId,String outCustId,String outAcctId,String transAmt,
			String inCustId,String inAcctId,String merPriv){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(), 
					getTransferParam(ordId, outCustId, outAcctId, transAmt, inCustId, inAcctId,merPriv));
		} catch (Exception e) {
			logger.error("自动扣款转账（商户用) transfer error",e);
		}
		return HttpClientUtil.getResult(response);
	}
	
	/**  分账账户串
	[
	    {
	        "DivAcctId": "MDT000001",      //分账账户号 
	        "DivAmt": "1.00",              //分账金额
	        "DivCustId": "6000060000009547"//分账商户号
	    },
	    {
	        "DivAcctId": "MDT000001",
	        "DivAmt": "2.00",
	        "DivCustId": "6000060000002526"
	    }
	]
    **/
	/** 
	* @Title: repay 
	* @Description: 自动扣款（还款）(v20) 
	* @param ordId 订单号
	* @param ordDate 订单日期 YYYYMMDD
	* @param outCustId 出账客户号  
	* @param outAcctId 出账子账户 
	* @param transAmt 交易金额
	* @param fee 放款或扣款的手续费
	* @param subOrdId 订单号 如果本次交易从属于另一个交易流水，则需要通过填写该流水号来进行关联例如：本次放款：商户流水号是 OrdId，
	*                        日期是 OrdDate，关联投标订单流水是 SubOrdId，日期是 SubOrdDate
	* @param subOrdDate 订单日期 YYYYMMDD
	* @param inCustId   入账客户号 
	* @param divDetails 分账账户串 （当 Fee！=0 时是必填项）
	* @param feeObjFlag I--向入款客户号 InCustId 收取O--向出款客户号 OutCustId 收取 若 fee 大于 0.00，FeeObjFlag 为必填参数
	* @param reqExt {"ProId":"0000001"}  ProId 标的的唯一标识 若 4.3.5 主动投标/4.3.6 自动投标的借款人信息BorrowerDetails 字段的项目 ID ProId 有值，则必填，否则为可选
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String repay(String ordId,String ordDate,String outCustId,String outAcctId,
			String transAmt,String fee,String subOrdId,String subOrdDate,String inCustId,
			String divDetails,String feeObjFlag,String reqExt){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(),
					getRepayParams(ordId, ordDate, outCustId,outAcctId, transAmt, fee,
							subOrdId, subOrdDate, inCustId, 
							divDetails, feeObjFlag,reqExt));
		} catch (Exception e) {
			logger.error("自动扣款（还款） repay error",e);
		}
		return HttpClientUtil.getResult(response);
	}
	
	
	/**  分账账户串
	[
	    {
	        "DivAcctId": "MDT000001",      //分账账户号 
	        "DivAmt": "1.00",              //分账金额 ，保留两位小数
	        "DivCustId": "6000060000009547"//分账商户号
	    },
	    {
	        "DivAcctId": "MDT000001",
	        "DivAmt": "2.00",
	        "DivCustId": "6000060000002526"
	    }
	]
    **/
	/** 
	* @Title: loans 
	* @Description: 自动扣款（放款） (v20)
	* @param ordId 订单号
	* @param ordDate 订单日期 YYYYMMDD
	* @param outCustId 出账客户号  
	* @param transAmt 交易金额  金额格式必须是###.00
	* @param fee 扣款手续费
	* @param subOrdId 订单号 如果本次交易从属于另一个交易流水，则需要通过填写该流水号来进行关联例如：本次放款：商户流水号是 OrdId，
	*                      日期是 OrdDate，关联投标订单流水是 SubOrdId，日期是 SubOrdDate
	* @param subOrdDate 订单日期 YYYYMMDD
	* @param inCustId 入账客户号
	* @param divDetails 分账账户串  （当 Fee！=0 时是必填项）
	* @param isDefault 是否默认 Y--默认添加资金池:这部分资金需要商户调用商户代取现接口，帮助用户做后台取现动作
                             N--不默认不添加资金池:这部分资金用户可以自己取现
	* @param isUnFreeze 是否解冻 Y--解冻 N--不解冻
	* @param unFreezeOrdId 解冻订单号 如果 IsUnFreeze 参数传 Y，那么该参数不能为空
	* @param freezeTrxId   冻结标识
	* @param feeObjFlag 续费收取对象标志I/O I--向入款客户号 InCustId 收取 O--向出款客户号 OutCustId 收取
                                                                       若 fee 大于 0.00，FeeObjFlag 为必填参数
	* @param reqExt    入参扩展域 {"LoansVocherAmt":"50.00", "ProId":"000001"} 本次放款使用的代金券金额
	*                   LoansVocherAmt 本次放款使用的代金券金额;ProId 项目 ID 4.3.5 主动投标/4.3.6 自动投标的借款人信息BorrowerDetails 字段的项目 ID ProId 有值
	* @return String    返回类型 
	* @throws 
	*/
	public String loans(String ordId,String ordDate,String outCustId,
			String transAmt,String fee,String subOrdId,String subOrdDate,String inCustId,
			String divDetails,String isDefault,String isUnFreeze,
			String unFreezeOrdId,String freezeTrxId,String feeObjFlag,String reqExt){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(), 
			getLoansParam(ordId, ordDate, outCustId, transAmt, fee, subOrdId, 
					subOrdDate, inCustId, divDetails, isDefault, isUnFreeze, 
					unFreezeOrdId, freezeTrxId,feeObjFlag,reqExt));
		} catch (Exception e) {
			logger.error("自动扣款（放款） loans error",e);
			
		}
		return HttpClientUtil.getResult(response);
	}
	
	
	
	
	/**
	{
	    "InDetails": [ //还款账户串
	        {
	            "DivDetails": [  //Fee 分账账户串
	                {
	                    "DivAcctId": "MDT000001", //分账账户号 
	                    "DivAmt": "1.00",         // 分账金额
	                    "DivCustId": "xxx"        //分账商户号 
	                }
	            ],
	            "Fee": "1.00",          // 扣款手续费 此字段为空时 FeeObjFlag，DivDetails 可不填
	            "FeeObjFlag": "I",      // 手续费收取对象标志  I/O ， I--向入款客户号 InCustId 收取O--向出款客户号 OutCustId 收取
	            "InAcctId": "xxx",      //入账子账号 
	            "InCustId": "xxx",      //入账客户号 
	            "OrdId": "201412102144",//还款订单号
	            "SubOrdId": "xxx",      //原投标订单号
	            "TransAmt": "5.00"      // 交易金额
	        },
	        {
	            "DivDetails": [
	                {
	                    "DivAcctId": "MDT000001",
	                    "DivAmt": "1.00",
	                    "DivCustId": "xxx"
	                }
	            ],
	            "Fee": "1.00",
	            "FeeObjFlag": "O",
	            "InAcctId": "xxx",
	            "InCustId": "xxx",
	            "OrdId": "xxx",
	            "SubOrdId": "xxx",
	            "TransAmt": "5.00"
	        }
	    ]
	}
	**/
	/** 
	* @Title: batchRepayment 
	* @Description: 批量还款接口 
	* 1.批量还款时汇付处理时间相对会变长，如同步结果获得超时，请等待后台结果推送 
	* 2.此批量接口的出账客户号 OutCustId 是唯一的，即一个批量还款的请求仅支持同一个出账客户号的批量操作
	* 3.当出账客户号为借款人时，即为正常还款交易；当出账客户号为商户客户号或者担保客户号，则为垫资还款交易；当出账客户号位投资人时，即还垫资交易
	* @param outCustId 出账客户号
	* @param outAcctId 出账子账户 出款客户为商户时此字段必填
	* @param batchId 还款批次号 由商户生成，在商户下唯一，打印此字段用来结束异步消息接收
	* @param merOrdDate 商户还款订单日期
	* @param inDetails  还款账户串 见上面详解
	* @param proId 项目 ID  若 4.3.5 主动投标/4.3.6 自动投标的借款人信息BorrowerDetails 字段的项目 ID ProId 有值，则必填，否则为可选
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	public String batchRepayment(String outCustId,
    		String outAcctId,String batchId,String merOrdDate,
    		String inDetails,String proId){
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(signUtils.getServerPath(), 
					getBatchRepaymentParams(outCustId, outAcctId, batchId,
							merOrdDate, inDetails, proId));
		} catch (Exception e) {
			logger.error("批量还款接口  batchRepayment error",e);
		}
		return HttpClientUtil.getResult(response);
	}

}
