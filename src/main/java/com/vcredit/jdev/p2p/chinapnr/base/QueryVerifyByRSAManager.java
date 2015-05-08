package com.vcredit.jdev.p2p.chinapnr.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;
import com.vcredit.jdev.p2p.enums.ThirdChannelEnum;

/** 
* @ClassName: QueryVerifyByRSAManager 
* @Description: 查询类验证签名 管理类 
* @author dk 
* @date 2015年2月3日 下午3:46:53 
*  
*/
@Component
public class QueryVerifyByRSAManager {

	private static Logger logger = Logger.getLogger(QueryVerifyByRSAManager.class);

	@Autowired
	SignUtils signUtils;

	/**
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 * @Title: verifyByRSARout
	 * @Description: 对汇付的应答响应 验证签名 分发
	 * @param cmdId
	 *            各个方法调用的标识
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void queryVerifyByRSARout(String cmdId, String parameter) throws JsonProcessingException, IOException {
		if (StringUtils.isBlank(cmdId)) {// 命令为空
			return;
		}
		String respCode = P2pUtil.getStringByKey(parameter, ThirdChannelEnum.RESPCODE.getCode());
		if (respCode == null|| !(ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode))) {// 返回码为空或为失败
			return;
		}
		
		if (ThirdChannelEnum.QUERYBALANCEBG.getCode().equals(cmdId)) {// 余额查询(后台)
			verifyByRSAQueryBalanceBg(parameter);
		} else if (ThirdChannelEnum.QUERYACCTS.getCode().equals(cmdId)) {// 商户子账户信息查询
			verifyByRSAQueryAccts(parameter);
		} else if (ThirdChannelEnum.QUERYTRANSSTAT.getCode().equals(cmdId)) {// 交易状态查询
			verifyByRSAQueryTransStat(parameter);
		} else if (ThirdChannelEnum.TRFRECONCILIATION.getCode().equals(cmdId)) {// 商户扣款对账
			verifyByRSATrfReconciliation(parameter);
		} else if (ThirdChannelEnum.RECONCILIATION.getCode().equals(cmdId)) {// 放还款对账
			verifyByRSAReconciliation(parameter);
		} else if (ThirdChannelEnum.CASHRECONCILIATION.getCode().equals(cmdId)) {// 取现对账
			verifyByRSACashReconciliation(parameter);
		} else if (ThirdChannelEnum.SAVERECONCILIATION.getCode().equals(cmdId)) {// 充值对账
			verifyByRSASaveReconciliation(parameter);
		} else if (ThirdChannelEnum.QUERYTENDERPLAN.getCode().equals(cmdId)) {// 自动投标计划状态查询
			verifyByRSAQueryTenderPlan(parameter);
		} else if (ThirdChannelEnum.QUERYRETURNDZFEE.getCode().equals(cmdId)) {//垫资手续费返还查询
			verifyByRSAQueryReturnDzFee(parameter);
		} else if (ThirdChannelEnum.QUERYCARDINFO.getCode().equals(cmdId)) {//银行卡查询
			verifyByRSAQueryCardInfo(parameter);
		} else if (ThirdChannelEnum.CORPREGISTERQUERY.getCode().equals(cmdId)) {//担保类型企业开户状态查询
			verifyByRSACorpRegisterQuery(parameter);
		} else if (ThirdChannelEnum.CREDITASSIGNRECONCILIATION.getCode().equals(cmdId)) {// 债权转让查询
			verifyByRSACreditAssignReconciliation(parameter);
		} else if (ThirdChannelEnum.QUERYUSRINFO.getCode().equals(cmdId)) {// 用户信息查询
			verifyByRSAQueryUsrInfo(parameter);
		} else if (ThirdChannelEnum.QUERYTRANSDETAIL.getCode().equals(cmdId)) {// 交易明细查询
			verifyByRSAQueryTransDetail(parameter);
		} else if (ThirdChannelEnum.BGBINDCARD.getCode().equals(cmdId)) {// 用户后台绑卡
			verifyByRSABgBindCard(parameter);
		} 
		


	}

	/**
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 * @Title: verifyByRSAUserRegister
	 * @Description: 余额查询(后台) 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAQueryBalanceBg(String parameter) throws JsonProcessingException, IOException {
//		CmdId+RespCode+MerCustId+ UsrCustId+ 
//		AvlBal+ AcctBal+ FrzBal

		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String usrCustId =P2pUtil.getStringByKey(parameter,"UsrCustId");
		String avlBal =P2pUtil.getStringByKey(parameter,"AvlBal");
		String acctBal =P2pUtil.getStringByKey(parameter,"AcctBal");
		String frzBal =P2pUtil.getStringByKey(parameter,"FrzBal");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(avlBal))
				.append(StringUtils.trimToEmpty(acctBal))
				.append(StringUtils.trimToEmpty(frzBal));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("余额查询(后台)验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("余额查询(后台)验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug("余额查询(后台)验证签名成功........");
	}

	
	/**
	 * @throws IOException 
	 * @throws JsonProcessingException  
	* @Title: verifyByRSAQueryAccts 
	* @Description: 商户子账户信息查询 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSAQueryAccts(String parameter) throws JsonProcessingException, IOException {
//		CmdId  + RespCode+ MerCustId
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("商户子账户信息查询验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("商户子账户信息查询验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug("商户子账户信息查询验证签名成功........");
	}
	/** 
	* @Title: verifyByRSAQueryTransStat 
	* @Description: 交易状态查询 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	//TODO ---
	private void verifyByRSAQueryTransStat(String parameter) throws JsonProcessingException, IOException {
		
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String usrCustId =P2pUtil.getStringByKey(parameter,"UsrCustId");
		String avlBal =P2pUtil.getStringByKey(parameter,"AvlBal");
		String acctBal =P2pUtil.getStringByKey(parameter,"AcctBal");
		String frzBal =P2pUtil.getStringByKey(parameter,"FrzBal");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(avlBal))
				.append(StringUtils.trimToEmpty(acctBal))
				.append(StringUtils.trimToEmpty(frzBal));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug(" 交易状态查询验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 交易状态查询 验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug(" 交易状态查询验证签名成功........");
	}
	
	/** 
	* @Title: verifyByRSATrfReconciliation 
	* @Description: 商户扣款对账  验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSATrfReconciliation(String parameter) throws JsonProcessingException, IOException  {
//		CmdId+RespCode+MerCustId+ 
//		BeginDate+EndDate+PageNum+PageSize+TotalItems
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String beginDate =P2pUtil.getStringByKey(parameter,"BeginDate");
		String endDate =P2pUtil.getStringByKey(parameter,"EndDate");
		String pageNum =P2pUtil.getStringByKey(parameter,"PageNum");
		String pageSize =P2pUtil.getStringByKey(parameter,"PageSize");
		String totalItems =P2pUtil.getStringByKey(parameter,"TotalItems");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(beginDate))
				.append(StringUtils.trimToEmpty(endDate))
				.append(StringUtils.trimToEmpty(pageNum))
				.append(StringUtils.trimToEmpty(pageSize))
				.append(StringUtils.trimToEmpty(totalItems));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug(" 放还款对账 验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 放还款对账 验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug(" 放还款对账 验证签名成功........");
	}
	
	/** 
	* @Title: verifyByRSAReconciliation 
	* @Description: 放还款对账 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSAReconciliation(String parameter) throws JsonProcessingException, IOException  {
//		CmdId+RespCode+MerCustId+ 
//		BeginDate+EndDate+PageNum+PageSize+TotalItems+ QueryTransType
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String beginDate =P2pUtil.getStringByKey(parameter,"BeginDate");
		String endDate =P2pUtil.getStringByKey(parameter,"EndDate");
		String pageNum =P2pUtil.getStringByKey(parameter,"PageNum");
		String pageSize =P2pUtil.getStringByKey(parameter,"PageSize");
		String totalItems =P2pUtil.getStringByKey(parameter,"TotalItems");
		String queryTransType =P2pUtil.getStringByKey(parameter,"QueryTransType");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(beginDate))
				.append(StringUtils.trimToEmpty(endDate))
				.append(StringUtils.trimToEmpty(pageNum))
				.append(StringUtils.trimToEmpty(pageSize))
				.append(StringUtils.trimToEmpty(totalItems))
				.append(StringUtils.trimToEmpty(queryTransType));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug(" 商户扣款对账 验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 商户扣款对账 验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug(" 商户扣款对账  验证签名成功........");
	}
	/** 
	* @Title: verifyByRSACashReconciliation 
	* @Description: 取现对账 验证签名 v20
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSACashReconciliation(String parameter) throws JsonProcessingException, IOException  {
//		CmdId+RespCode+MerCustId+ 
//		BeginDate+EndDate+PageNum+PageSize+TotalItems+ FeeObj
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String beginDate =P2pUtil.getStringByKey(parameter,"BeginDate");
		String endDate =P2pUtil.getStringByKey(parameter,"EndDate");
		String pageNum =P2pUtil.getStringByKey(parameter,"PageNum");
		String pageSize =P2pUtil.getStringByKey(parameter,"PageSize");
		String totalItems =P2pUtil.getStringByKey(parameter,"TotalItems");
		String feeObj =P2pUtil.getStringByKey(parameter,"FeeObj");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(beginDate))
				.append(StringUtils.trimToEmpty(endDate))
				.append(StringUtils.trimToEmpty(pageNum))
				.append(StringUtils.trimToEmpty(pageSize))
				.append(StringUtils.trimToEmpty(totalItems))
				.append(StringUtils.trimToEmpty(feeObj));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug(" 取现对账 验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 取现对账 验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug(" 取现对账  验证签名成功........");
	}
	/** 
	* @Title: verifyByRSASaveReconciliation 
	* @Description: 充值对账 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSASaveReconciliation(String parameter) throws JsonProcessingException, IOException {
//		CmdId+RespCode+MerCustId+ 
//		BeginDate+EndDate+PageNum+PageSize+TotalItems
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String beginDate =P2pUtil.getStringByKey(parameter,"BeginDate");
		String endDate =P2pUtil.getStringByKey(parameter,"EndDate");
		String pageNum =P2pUtil.getStringByKey(parameter,"PageNum");
		String pageSize =P2pUtil.getStringByKey(parameter,"PageSize");
		String totalItems =P2pUtil.getStringByKey(parameter,"TotalItems");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(beginDate))
				.append(StringUtils.trimToEmpty(endDate))
				.append(StringUtils.trimToEmpty(pageNum))
				.append(StringUtils.trimToEmpty(pageSize))
				.append(StringUtils.trimToEmpty(totalItems));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug(" 充值对账  验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 充值对账  验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug(" 充值对账  验证签名成功........");
	}
	/** 
	* @Title: verifyByRSAQueryTenderPlan 
	* @Description: 自动投标计划状态查询 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSAQueryTenderPlan(String parameter) throws JsonProcessingException, IOException {
		
		//CmdId+RespCode+MerCustId+ UsrCustId+TransStat
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String usrCustId =P2pUtil.getStringByKey(parameter,"UsrCustId");
		String transStat =P2pUtil.getStringByKey(parameter,"TransStat");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(transStat));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug(" 自动投标计划状态查询 验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 自动投标计划状态查询 验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug(" 自动投标计划状态查询验证签名成功........");
	}
	
	/** 
	* @Title: verifyByRSAQueryReturnDzFee 
	* @Description: 垫资手续费返还查询 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSAQueryReturnDzFee(String parameter) throws JsonProcessingException, IOException {
//		CmdId+RespCode+MerCustId+ 
//		BeginDate+EndDate
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String beginDate =P2pUtil.getStringByKey(parameter,"BeginDate");
		String endDate =P2pUtil.getStringByKey(parameter,"EndDate");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(beginDate))
				.append(StringUtils.trimToEmpty(endDate));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug(" 垫资手续费返还查询  验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 垫资手续费返还查询  验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug("垫资手续费返还查询  验证签名成功........");
	}
	
	/** 
	* @Title: verifyByRSAQueryCardInfo 
	* @Description: 银行卡查询 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSAQueryCardInfo(String parameter) throws JsonProcessingException, IOException {
		//CmdId+RespCode+MerCustId+UsrCustId + CardId
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String usrCustId =P2pUtil.getStringByKey(parameter,"UsrCustId");
		String cardId =P2pUtil.getStringByKey(parameter,"CardId");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(cardId));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug(" 银行卡查询  验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 银行卡查询  验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug("银行卡查询  验证签名成功........");
	}
	/** 
	* @Title: verifyByRSACorpRegisterQuery 
	* @Description:  企业开户状态查询 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSACorpRegisterQuery(String parameter) throws JsonProcessingException, IOException {
//		CmdId+RespCode+MerCustId+UsrCustId 
//		+UsrId + AuditStat+BusiCode+RespExt
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String usrCustId =P2pUtil.getStringByKey(parameter,"UsrCustId");
		String usrId =P2pUtil.getStringByKey(parameter,"UsrId");
		String auditStat =P2pUtil.getStringByKey(parameter,"AuditStat");
		String busiCode =P2pUtil.getStringByKey(parameter,"BusiCode");
		String respExt =P2pUtil.getStringByKey(parameter,"RespExt");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(usrId))
				.append(StringUtils.trimToEmpty(auditStat))
				.append(StringUtils.trimToEmpty(busiCode))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("  企业开户状态查询  验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 企业开户状态查询  验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug(" 企业开户状态查询  验证签名成功........");
	}
	
	/** 
	* @Title: verifyByRSACreditAssignReconciliation 
	* @Description: 债权转让查询 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSACreditAssignReconciliation(String parameter) throws JsonProcessingException, IOException {
//		CmdId+RespCode+MerCustId+OrdId 
//		+BeginDate+EndDate+SellCustId+BuyCustId+
//		PageNum+PageSize+TotalItems+RespExt
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String ordId =P2pUtil.getStringByKey(parameter,"OrdId");
		String beginDate =P2pUtil.getStringByKey(parameter,"BeginDate");
		String endDate =P2pUtil.getStringByKey(parameter,"EndDate");
		String sellCustId =P2pUtil.getStringByKey(parameter,"SellCustId");
		String buyCustId =P2pUtil.getStringByKey(parameter,"BuyCustId");
		String pageNum =P2pUtil.getStringByKey(parameter,"PageNum");
		String pageSize =P2pUtil.getStringByKey(parameter,"PageSize");
		String totalItems =P2pUtil.getStringByKey(parameter,"TotalItems");
		String respExt =P2pUtil.getStringByKey(parameter,"RespExt");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(beginDate))
				.append(StringUtils.trimToEmpty(endDate))
				.append(StringUtils.trimToEmpty(sellCustId))
				.append(StringUtils.trimToEmpty(buyCustId))
				.append(StringUtils.trimToEmpty(pageNum))
				.append(StringUtils.trimToEmpty(pageSize))
				.append(StringUtils.trimToEmpty(totalItems))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("  债权转让查询  验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 债权转让查询  验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug(" 债权转让查询  验证签名成功........");
	}
	/** 
	* @Title: verifyByRSAQueryUsrInfo 
	* @Description: 用户信息查询 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSAQueryUsrInfo(String parameter) throws JsonProcessingException, IOException {
//		CmdId+RespCode+MerCustId 
//		+UsrCustId+UsrId+CertId+UsrStat+RespExt
		
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String usrCustId =P2pUtil.getStringByKey(parameter,"UsrCustId");
		String usrId =P2pUtil.getStringByKey(parameter,"UsrId");
		String certId =P2pUtil.getStringByKey(parameter,"CertId");
		String usrStat =P2pUtil.getStringByKey(parameter,"UsrStat");
		String respExt =P2pUtil.getStringByKey(parameter,"RespExt");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(usrId))
				.append(StringUtils.trimToEmpty(certId))
				.append(StringUtils.trimToEmpty(usrStat))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("  用户信息查询  验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 用户信息查询  验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug("用户信息查询  验证签名成功........");
	}
	/** 
	* @Title: verifyByRSAQueryTransDetail 
	* @Description: 交易明细查询 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSAQueryTransDetail(String parameter) throws JsonProcessingException, IOException {
//		CmdId+ 
//		RespCode+ MerCustId+ UsrCustId+ OrdId 
//		+OrdDate + QueryTransType + TransAmt + 
//		TransStat + FeeAmt + FeeCustId + FeeAcctId + 
//		GateBusiId + RespExt
		
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String usrCustId =P2pUtil.getStringByKey(parameter,"UsrCustId");
		String ordId =P2pUtil.getStringByKey(parameter,"OrdId");
		String ordDate =P2pUtil.getStringByKey(parameter,"OrdDate");
		String queryTransType =P2pUtil.getStringByKey(parameter,"QueryTransType");
		String transAmt =P2pUtil.getStringByKey(parameter,"TransAmt");
		String transStat =P2pUtil.getStringByKey(parameter,"TransStat");
		String feeAmt =P2pUtil.getStringByKey(parameter,"FeeAmt");
		String feeCustId =P2pUtil.getStringByKey(parameter,"FeeCustId");
		String feeAcctId =P2pUtil.getStringByKey(parameter,"FeeAcctId");
		String gateBusiId =P2pUtil.getStringByKey(parameter,"GateBusiId");
		String respExt =P2pUtil.getStringByKey(parameter,"RespExt");
		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))
				.append(StringUtils.trimToEmpty(queryTransType))
				.append(StringUtils.trimToEmpty(transAmt))
				.append(StringUtils.trimToEmpty(transStat))
				.append(StringUtils.trimToEmpty(feeAmt))
				.append(StringUtils.trimToEmpty(feeCustId))
				.append(StringUtils.trimToEmpty(feeAcctId))
				.append(StringUtils.trimToEmpty(gateBusiId))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("  交易明细查询 验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 交易明细查询  验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug("交易明细查询  验证签名成功........");
	}
	/** 
	* @Title: verifyByRSABgBindCard 
	* @Description: 用户后台绑卡 验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSABgBindCard(String parameter) throws JsonProcessingException, IOException {
		//CmdId + RespCode+ MerCustId + UsrCustId + OpenAcctId+ OpenBankId + MerPriv
		String cmdId = P2pUtil.getStringByKey(parameter,"CmdId");
		String respCode = P2pUtil.getStringByKey(parameter,"RespCode");
		String merCustId = P2pUtil.getStringByKey(parameter,"MerCustId");
		String usrCustId =P2pUtil.getStringByKey(parameter,"UsrCustId");
		String openAcctId =P2pUtil.getStringByKey(parameter,"OpenAcctId");
		String openBankId =P2pUtil.getStringByKey(parameter,"OpenBankId");
		String merPriv =P2pUtil.getStringByKey(parameter,"MerPriv");

		
		String chkValue =P2pUtil.getStringByKey(parameter,"ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(openAcctId))
				.append(StringUtils.trimToEmpty(openBankId))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("  用户后台绑卡 验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug(" 用户后台绑卡  验证签名失败........");
			System.out.println("验证签名失败...");
		}
		logger.debug("用户后台绑卡  验证签名成功........");
	}
}