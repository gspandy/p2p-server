package com.vcredit.jdev.p2p.chinapnr.query;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.chinapnr.base.QueryVerifyByRSAManager;
import com.vcredit.jdev.p2p.chinapnr.util.ChinapnrConstans;
import com.vcredit.jdev.p2p.chinapnr.util.HttpClientUtil;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;

@Component
public class QueryManager {

	private final static Logger logger = LoggerFactory.getLogger(QueryManager.class);

	@Autowired
	private SignUtils signUtils;
	
	@Autowired
	private QueryVerifyByRSAManager queryVerifyByRSAManager;
	

	private Map<String, String> getQueryBalanceBgParam(String usrCustId) {

		String version = signUtils.getVersion1();
		String cmdId = "QueryBalanceBg";
		String MerCustId = signUtils.getMerCustId();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", MerCustId);
		params.put("UsrCustId", usrCustId);

		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(MerCustId))
				.append(StringUtils.trimToEmpty(usrCustId));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		try {
			params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		} catch (Exception e) {
			logger.error("getQueryBalanceBgParam--> encryptByRSA error",e);
		}

		signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrCustId_"+usrCustId);
		return params;
	}

	private Map<String, String> getQueryTransStatParams(String ordId,
			String ordDate, String queryTransType) throws Exception {

		String version = signUtils.getVersion1();
		String cmdId = "QueryTransStat";
		String merCustId = signUtils.getMerCustId();
		// 若为中文，请用Base64转码
		// String merPriv = HttpClientHandler.getBase64Encode("11");

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("OrdId", ordId);
		params.put("OrdDate", ordDate);

		params.put("QueryTransType", queryTransType);
		// Version+CmdId+MerCustId+OrdId+
		// OrdDate+ QueryTransType
		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))
				.append(StringUtils.trimToEmpty(queryTransType));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
		return params;
	}

	/**
	 * @Title: getReconciliationParams
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param beginDate
	 * @param endDate
	 * @param pageNum
	 * @param pageSize
	 * @param queryTransType
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return Map<String,String> 返回类型
	 * @throws
	 */
	private Map<String, String> getReconciliationParams(String beginDate,
			String endDate, String pageNum, String pageSize,
			String queryTransType) throws Exception {

		String version = signUtils.getVersion1();
		String merCustId = signUtils.getMerCustId();
		String cmdId = "Reconciliation";

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("BeginDate", beginDate);
		params.put("EndDate", endDate);
		params.put("PageNum", pageNum);
		params.put("PageSize", pageSize);
		params.put("QueryTransType", queryTransType);

		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(beginDate))
				.append(StringUtils.trimToEmpty(endDate))
				.append(StringUtils.trimToEmpty(pageNum))
				.append(StringUtils.trimToEmpty(pageSize));
		buffer.append(StringUtils.trimToEmpty(queryTransType));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"QueryTransType+_"+queryTransType);
		return params;
	}

	private Map<String, String> getXReconciliationParams(String cmdId,
			String beginDate, String endDate, String pageNum, String pageSize)
			throws Exception {

		String version = signUtils.getVersion1();
		String merCustId = signUtils.getMerCustId();
		if (ChinapnrConstans.ReconciliationType.CASHRE_CONCILIATION
				.equals(cmdId)) {
			version = signUtils.getVersion2();
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("BeginDate", beginDate);
		params.put("EndDate", endDate);
		params.put("PageNum", pageNum);
		params.put("PageSize", pageSize);
		// cmdId=Reconciliatio,将queryTransType添加到参数中

		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(beginDate))
				.append(StringUtils.trimToEmpty(endDate))
				.append(StringUtils.trimToEmpty(pageNum))
				.append(StringUtils.trimToEmpty(pageSize));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"BeginDate_"+beginDate+" EndDate_"+endDate);
		return params;
	}

	/**
	 * 垫资手续费返还查询接口请求参数
	 * 
	 * @throws Exception
	 * 
	 */
	private Map<String, String> getQueryReturnDzFeeParams(String beginDate,
			String endDate) throws Exception {

		String version = signUtils.getVersion1();
		String cmdId = "QueryReturnDzFee";
		String merCustId = signUtils.getMerCustId();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("BeginDate", beginDate);
		params.put("EndDate", endDate);

		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(beginDate))
				.append(StringUtils.trimToEmpty(endDate));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"BeginDate_"+beginDate+" EndDate_"+endDate);
		return params;
	}

	/**
	 * 商户子账户信息查询接口请求参数
	 * 
	 * @throws Exception
	 * 
	 */
	private Map<String, String> getQueryAcctsParams() throws Exception {

		String version = signUtils.getVersion1();
		String cmdId = "QueryAccts";
		String merCustId = signUtils.getMerCustId();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);

		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,null);
		return params;
	}

	/**
	 * 自动投标计划状态查询接口请求参数
	 * 
	 * @throws Exception
	 * 
	 */
	private Map<String, String> getQueryTenderPlanParams(String usrCustId)
			throws Exception {

		String version = signUtils.getVersion1();
		String cmdId = "QueryTenderPlan";
		String merCustId = signUtils.getMerCustId();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("UsrCustId", usrCustId);

		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrCustId_"+usrCustId);
		return params;
	}

	/**
	 * @Title: getQueryAcctDetailsParams
	 * @Description: 账户明细查询(页面)接口
	 * @param usrCustId
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return Map<String,String> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unused")
	private Map<String, String> getQueryAcctDetailsParams(String usrCustId)
			throws Exception {
		String version = signUtils.getVersion1();
		String cmdId = "QueryAcctDetails";
		String merCustId = signUtils.getMerCustId();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("UsrCustId", usrCustId);

		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrCustId_"+usrCustId);
		return params;
	}

	/**
	 * 银行卡查询接口请求参数
	 * 
	 * @throws Exception
	 * 
	 */
	private Map<String, String> getQueryCardInfoParams(String usrCustId,
			String cardId) throws Exception {
		String version = signUtils.getVersion1();
		String cmdId = "QueryCardInfo";
		String merCustId = signUtils.getMerCustId();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("UsrCustId", usrCustId);
		params.put("CardId", cardId);

		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(cardId));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"UsrCustId_"+usrCustId+" CardId_"+cardId);
		return params;
	}

	/**
	 * 担保类型企业开户状态查询接口请求参数
	 * 
	 * @param busiCode
	 *            营业执照编号
	 * @throws Exception
	 * 
	 */
	private Map<String, String> getCorpRegisterQueryParams(String busiCode)
			throws Exception {
		String version = signUtils.getVersion1();
		String cmdId = "CorpRegisterQuery";
		String merCustId = signUtils.getMerCustId();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("BusiCode", busiCode);// 营业执照编号

		// 组装加签字符串原文
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(busiCode));
		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"BusiCode_"+busiCode);
		return params;
	}

	/**
	 * 债权查询接口请求参数
	 * 
	 * @param busiCode
	 *            营业执照编号
	 * @throws Exception
	 * 
	 */
	private Map<String, String> getCreditAssignReconciliationParams(
			String ordId, String beginDate, String endDate, String pageNum,
			String pageSize, String sellCustId, String buyCustId)
			throws Exception {

		String version = signUtils.getVersion1();
		String cmdId = "CreditAssignReconciliation";
		String merCustId = signUtils.getMerCustId();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("BeginDate", beginDate);
		params.put("EndDate", endDate);
		params.put("PageNum", pageNum);
		params.put("PageSize", pageSize);

		params.put("OrdId", ordId);
		params.put("SellCustId", sellCustId);
		params.put("BuyCustId", buyCustId);

		// 组装加签字符串原文
		// Version+CmdId+MerCustId+
		// OrdId+BeginDate+EndDate+SellCustId+BuyC
		// ustId+PageNum+PageSize
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(beginDate))
				.append(StringUtils.trimToEmpty(endDate))
				.append(StringUtils.trimToEmpty(sellCustId))
				.append(StringUtils.trimToEmpty(buyCustId))
				.append(StringUtils.trimToEmpty(pageNum))
				.append(StringUtils.trimToEmpty(pageSize));

		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId);
		return params;
	}

	/**
	 * @Title: getQueryUsrInfoParams
	 * @Description: 用户信息查询参数
	 * @param certId
	 *            用户身份证号
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return Map<String,String> 返回类型
	 * @throws
	 */
	private Map<String, String> getQueryUsrInfoParams(String certId)
			throws Exception {

		String version = signUtils.getVersion1();
		String cmdId = "QueryUsrInfo";
		String merCustId = signUtils.getMerCustId();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("CertId", certId);

		// 组装加签字符串原文
		// version+CmdId+MerCustId+CertId+ ReqExt
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(certId));

		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"CertId_"+certId);
		return params;
	}

	/**
	 * @Title: getQueryTransDetailParams
	 * @Description: 用于查询交易明细，现只有充值交易明细查询
	 * @param ordId
	 *            订单号
	 * @param queryTransType
	 *            交易查询类型 SAVE：充值交易明细查询
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return Map<String,String> 返回类型
	 * @throws
	 */
	private Map<String, String> getQueryTransDetailParams(String ordId,
			String queryTransType) throws Exception {

		String version = signUtils.getVersion1();
		String cmdId = "QueryTransDetail";
		String merCustId = signUtils.getMerCustId();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Version", version);
		params.put("CmdId", cmdId);
		params.put("MerCustId", merCustId);
		params.put("OrdId", ordId);
		params.put("QueryTransType", queryTransType);

		// 组装加签字符串原文
		// Version+CmdId+MerCustId+OrdId+ QueryTransType
		// 注意加签字符串的组装顺序参 请照接口文档
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(version))
				.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(queryTransType));

		String plainStr = buffer.toString();
		System.out.println(plainStr);

		params.put("ChkValue", signUtils.encryptByRSA(plainStr));
		signUtils.addRequestChinapnrLogEventPublish(cmdId,"OrdId_"+ordId+" QueryTransType_"+queryTransType);
		return params;
	}

	/**
	 * @Title: queryBalanceBg
	 * @Description: 余额查询（后台）
	 * @param usrCustId
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryBalanceBg(String usrCustId) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getQueryBalanceBgParam(usrCustId));
		} catch (Exception e) {
			logger.error("余额查询（后台）    queryBalanceBg error" ,e);
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("QueryBalanceBg",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: queryTransStat
	 * @Description: 交易状态查询
	 * @param ordId
	 *            订单号 由商户的系统生成，必须保证唯一，请使用纯数字
	 * @param ordDate
	 *            订单日期 格式为 YYYYMMDD
	 * @param queryTransType
	 *            LOANS(放款交易查询)REPAYMENT(还款交易查询) TENDER(投标交易查询)CASH(取现交易查询)
	 *            FREEZE(冻结解冻交易查询) --ChinapnrConstans
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryTransStat(String ordId, String ordDate,
			String queryTransType) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getQueryTransStatParams(ordId, ordDate, queryTransType));
		} catch (Exception e) {
			logger.error("交易状态查询    queryTransStat error" ,e);
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("QueryTransStat",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: queryTrfReconciliation
	 * @Description: 商户扣款对账 10
	 * @param beginDate
	 *            开始时间 YYYYMMDD 格式 BeginDate 和 EndDate 日期跨度不能大于 90 天
	 * @param endDate
	 *            结束时间 YYYYMMDD 格式 BeginDate 和 EndDate 日期跨度不能大于 90 天
	 * @param pageNum
	 *            页数 查询数据的所在页号，>0 的整数
	 * @param pageSize每页记录数
	 *            查询数据的所在页号，>0 且<=1000 的整数
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryTrfReconciliation(String beginDate, String endDate,
			String pageNum, String pageSize) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getXReconciliationParams("TrfReconciliation", beginDate,
							endDate, pageNum, pageSize));
		} catch (Exception e) {
			logger.error("商户扣款对账    queryTrfReconciliation error"
					,e);
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("TrfReconciliation",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: queryCashReconciliation
	 * @Description: 取现对账 (V20)
	 * @param beginDate
	 *            开始时间 YYYYMMDD 格式 BeginDate 和 EndDate 日期跨度不能大于 90 天
	 * @param endDate
	 *            结束时间 YYYYMMDD 格式 BeginDate 和 EndDate 日期跨度不能大于 90 天
	 * @param pageNum
	 *            页数 查询数据的所在页号，>0 的整数
	 * @param pageSize每页记录数
	 *            查询数据的所在页号，>0 且<=1000 的整数
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryCashReconciliation(String beginDate, String endDate,
			String pageNum, String pageSize) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getXReconciliationParams("CashReconciliation", beginDate,
							endDate, pageNum, pageSize));
		} catch (Exception e) {
			logger.error("取现对账    queryCashReconciliation error"
					,e);
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("CashReconciliation",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: querySaveReconciliation
	 * @Description: 充值对账10
	 * @param beginDate
	 *            开始时间 YYYYMMDD 格式 BeginDate 和 EndDate 日期跨度不能大于 90 天
	 * @param endDate
	 *            结束时间 YYYYMMDD 格式 BeginDate 和 EndDate 日期跨度不能大于 90 天
	 * @param pageNum
	 *            页数 查询数据的所在页号，>0 的整数
	 * @param pageSize每页记录数
	 *            查询数据的所在页号，>0 且<=1000 的整数
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String querySaveReconciliation(String beginDate, String endDate,
			String pageNum, String pageSize) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getXReconciliationParams("SaveReconciliation", beginDate,
							endDate, pageNum, pageSize));
		} catch (Exception e) {
			logger.error("充值对账    querySaveReconciliation error"
					,e);
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("SaveReconciliation",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: queryReconciliation
	 * @Description: 放还款对账
	 * @param beginDate
	 *            开始时间 YYYYMMDD 格式 BeginDate 和 EndDate 日期跨度不能大于 90 天
	 * @param endDate
	 *            结束时间 YYYYMMDD 格式 BeginDate 和 EndDate 日期跨度不能大于 90 天
	 * @param pageNum
	 *            页数 查询数据的所在页号，>0 的整数
	 * @param pageSize每页记录数
	 *            查询数据的所在页号，>0 且<=1000 的整数
	 * @param queryTransType
	 *            LOANS：放款交易查询 REPAYMENT：还款交易
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryReconciliation(String beginDate, String endDate,
			String pageNum, String pageSize, String queryTransType) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getReconciliationParams(beginDate, endDate, pageNum,
							pageSize, queryTransType));
		} catch (Exception e) {
			logger.error("放还款对账    queryReconciliation error" ,e);
			
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("Reconciliation",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: queryAccts
	 * @Description: 商户子账户信息查询
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryAccts() {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(), getQueryAcctsParams());
		} catch (Exception e) {
			logger.error("商户子账户信息查询   queryAccts error" ,e);
			
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("QueryAccts",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: queryTenderPlan
	 * @Description: 自动投标计划状态查询
	 * @param usrCustId
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryTenderPlan(String usrCustId) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getQueryTenderPlanParams(usrCustId));
		} catch (Exception e) {
			logger.error("自动投标计划状态查询   queryTenderPlan error" ,e);
			
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("QueryTenderPlan",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: queryReturnDzFee
	 * @Description: 垫资手续费返还查询
	 * @param beginDate
	 * @param endDate
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryReturnDzFee(String beginDate, String endDate) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getQueryReturnDzFeeParams(beginDate, endDate));
		} catch (Exception e) {
			logger.error("垫资手续费返还查询   queryReturnDzFee  error" ,e);
			
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("QueryBalanceBg",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: queryCardInfo
	 * @Description: 银行卡查询接口
	 * @param usrCustId
	 * @param cardId
	 *            取现银行的账户号（银行卡号）
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryCardInfo(String usrCustId, String cardId) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getQueryCardInfoParams(usrCustId, cardId));
		} catch (Exception e) {
			logger.error("银行卡查询接口  queryCardInfo  error" ,e);
			
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("QueryCardInfo",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: corpRegisterQuery
	 * @Description: 担保类型企业开户状态查询接口
	 * @param busiCode
	 *            营业执照编号
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String corpRegisterQuery(String busiCode) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getCorpRegisterQueryParams(busiCode));
		} catch (Exception e) {
			logger.error("担保类型企业开户状态查询接口  corpRegisterQuery  error"
					,e);
			
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("CorpRegisterQuery",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: queryUsrInfo
	 * @Description: 用户信息查询
	 * @param certId
	 *            用户身份证号
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryUsrInfo(String certId) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(), getQueryUsrInfoParams(certId));
		} catch (Exception e) {
			logger.error("用户信息查询  queryUsrInfo  error" ,e);
			
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("QueryUsrInfo",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: queryTransDetail
	 * @Description: 用于查询交易明细，现只有充值交易明细查询
	 * @param ordId
	 *            订单号
	 * @param queryTransType
	 *            交易查询类型 SAVE：充值交易明细查询
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryTransDetail(String ordId, String queryTransType) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getQueryTransDetailParams(ordId, queryTransType));
		} catch (Exception e) {
			logger.error("查询交易明细  queryTransDetail error",e);
			
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("QueryTransDetail",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}

	/**
	 * @Title: creditAssignReconciliation
	 * @Description: 债权查询接口
	 * @param ordId
	 *            订单号
	 * @param beginDate
	 * @param endDate
	 * @param pageNum
	 * @param pageSize
	 * @param sellCustId
	 *            转让人客户号
	 * @param buyCustId
	 *            承接人客户号
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String creditAssignReconciliation(String ordId, String beginDate,
			String endDate, String pageNum, String pageSize, String sellCustId,
			String buyCustId) {
		HttpResponse response = null;
		try {
			response = HttpClientUtil.sendPostRequest(
					signUtils.getServerPath(),
					getCreditAssignReconciliationParams(ordId, beginDate,
							endDate, pageNum, pageSize, sellCustId, buyCustId));
		} catch (Exception e) {
			logger.error("调用债权查询接口 creditAssignReconciliation  error",e);
			logger.error("调用债权查询接口 creditAssignReconciliation  error"+e.getMessage());
		}
		String result = HttpClientUtil.getResult(response);
		// 验签
		// try {
		// queryVerifyByRSAManager.queryVerifyByRSARout("CreditAssignReconciliation",
		// result);
		// } catch (JsonProcessingException e) {
		// 
		// } catch (IOException e) {
		// 
		// }
		return result;
	}
	
	


}
