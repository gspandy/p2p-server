package com.vcredit.jdev.p2p.chinapnr.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;
import com.vcredit.jdev.p2p.enums.ThirdChannelEnum;

/**
 * @ClassName: verifyByRSAManager
 * @Description:验证签名 管理类
 * @author dk
 * @date 2015年1月26日 上午10:24:47
 * 
 */
@Component
public class VerifyByRSAManager {

	private static Logger logger = Logger.getLogger(VerifyByRSAManager.class);

	@Autowired
	SignUtils signUtils;

	/**
	 * @Title: verifyByRSARout
	 * @Description: 对汇付的应答响应 验证签名 分发
	 * @param cmdId
	 *            各个方法调用的标识
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void verifyByRSARout(String cmdId, Map<String, String> parameter) {
		if (StringUtils.isBlank(cmdId)) {// 命令为空
			return;
		}
		String respCode = parameter.get(ThirdChannelEnum.RESPCODE.getCode());
		if (respCode == null
				|| !ThirdChannelEnum.RESPCODEVALUE.getCode().equals(respCode)) {// 返回码为空或为失败
			// return;
			return;
		}

		if (ThirdChannelEnum.ADDBIDINFO.getCode().equals(cmdId)) {// 标的录入
			verifyByRSAAddBidInfo(parameter);
		} else if (ThirdChannelEnum.CASH.getCode().equals(cmdId)) {// 取现
			verifyByRSACash(parameter);
		} else if (ThirdChannelEnum.DELCARD.getCode().equals(cmdId)) {// 删除银行卡
			verifyByRSADelCard(parameter);
		} else if (ThirdChannelEnum.INITIATIVETENDER.getCode().equals(cmdId)) {// 主动投标
			verifyByRSAInitiativeTender(parameter);
		} else if (ThirdChannelEnum.AUTOTENDERPLAN.getCode().equals(cmdId)) {// 自动投标计划
			verifyByRSAAutoTenderPlan(parameter);
		} else if (ThirdChannelEnum.AUTOTENDER.getCode().equals(cmdId)) {// 自动投标
			verifyByRSAAutoTender(parameter);
		} else if (ThirdChannelEnum.AUTOTENDERPLANCLOSE.getCode().equals(cmdId)) {// 自动投标计划关闭
			verifyByRSAAutoTenderPlanClose(parameter);
		} else if (ThirdChannelEnum.LOANS.getCode().equals(cmdId)) {// 自动扣款（放款）
			verifyByRSALoans(parameter);
		} else if (ThirdChannelEnum.MERCASH.getCode().equals(cmdId)) {// 商户代取现
			verifyByRSAMerCash(parameter);
		} else if (ThirdChannelEnum.NETSAVE.getCode().equals(cmdId)) {// 充值
			verifyByRSANetSave(parameter);
		} else if (ThirdChannelEnum.REPAYMENT.getCode().equals(cmdId)) {// 自动扣款（还款）
			verifyByRSARepayment(parameter);
		} else if (ThirdChannelEnum.TRANSFER.getCode().equals(cmdId)) {// 自动扣款转账（商户用）
			verifyByRSATransfer(parameter);
		} else if (ThirdChannelEnum.USERBINDCARD.getCode().equals(cmdId)) {// 绑卡
			verifyByRSAUserBindCard(parameter);
		} else if (ThirdChannelEnum.USERLOGIN.getCode().equals(cmdId)) {// 登陆
			// verifyByRSAUserLogin(parameter);//没有参数返回
		} else if (ThirdChannelEnum.USERREGISTER.getCode().equals(cmdId)) {// 注册
			verifyByRSAUserRegister(parameter);
		} else if (ThirdChannelEnum.USRFREEZEBG.getCode().equals(cmdId)) {// 资金冻结
			verifyByRSAUsrFreezeBg(parameter);
		} else if (ThirdChannelEnum.USRUNFREEZE.getCode().equals(cmdId)) {// 资金解冻
			verifyByRSAUsrUnFreeze(parameter);
		}else if(ThirdChannelEnum.TENDERCANCLE.getCode().equals(cmdId)) {// 投标撤销
			verifyByRSATenderCancle(parameter);
		}else if(ThirdChannelEnum.CREDITASSIGN.getCode().equals(cmdId)) {// 债权转让
			verifyByRSACreditAssign(parameter);
		} else if (ThirdChannelEnum.AUTOCREDITASSIGN.getCode().equals(cmdId)) {// 自动债权转让
			verifyByRSAAutoCreditAssign(parameter);
		} else if (ThirdChannelEnum.BATCHREPAYMENT.getCode().equals(cmdId)) {// 批量还款接口
			verifyByRSABatchRepayment(parameter);
		}
	}

	/**
	 * @Title: verifyByRSAUserRegister
	 * @Description: 开户注册验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAUserRegister(Map<String, String> parameter) {
		// CmdId + RespCode+ MerCustId + UsrId+
		// UsrCustId +BgRetUrl + TrxId +RetUrl + MerPriv

		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String usrId = parameter.get("UsrId");
		String usrCustId = parameter.get("UsrCustId");
		String bgRetUrl = parameter.get("BgRetUrl");
		String trxId = parameter.get("TrxId");
		String retUrl = parameter.get("RetUrl");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		try {
			retUrl = URLDecoder.decode(retUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}

		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("开户注册验证签名失败........" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("开户注册验证签名失败........");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAUserBindCard
	 * @Description: 用户绑卡验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAUserBindCard(Map<String, String> parameter) {
		// CmdId+RespCode+MerCustId+OpenAcctId+
		// OpenBankId + UsrCustId+ TrxId + BgRetUrl +
		// MerPriv
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String openAcctId = parameter.get("OpenAcctId");
		String openBankId = parameter.get("OpenBankId");
		String UsrCustId = parameter.get("UsrCustId");
		String trxId = parameter.get("TrxId");

		String bgRetUrl = parameter.get("BgRetUrl");
		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}

		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(openAcctId))
				.append(StringUtils.trimToEmpty(openBankId))
				.append(StringUtils.trimToEmpty(UsrCustId))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("用户绑卡验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("用户绑卡验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAUserLogin
	 * @Description: 用户登录 验证签名 没有返回
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	// private void verifyByRSAUserLogin(Map<String, String> parameter){
	// String cmdId = parameter.get("CmdId");
	// String respCode = parameter.get("RespCode");
	// String respDesc = parameter.get("RespDesc");
	// String merCustId = parameter.get("MerCustId");
	// String usrId = parameter.get("UsrId");
	// String usrCustId = parameter.get("UsrCustId");
	// String bgRetUrl = parameter.get("BgRetUrl");
	// String trxId = parameter.get("TrxId");
	// String retUrl = parameter.get("RetUrl");
	//
	// String idType = parameter.get("IdType");
	// String idNo = parameter.get("IdNo");
	// String usrMp = parameter.get("UsrMp");
	//
	// String usrEmail = parameter.get("UsrEmail");
	// String usrName = parameter.get("UsrName");
	// try {
	// bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// try {
	// retUrl = URLDecoder.decode(retUrl, "UTF-8");
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// String merPriv = parameter.get("MerPriv");
	// if(StringUtils.isNotBlank(merPriv)){
	// try {
	// merPriv = URLDecoder.decode(merPriv, "UTF-8");
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// }
	// String chkValue = parameter.get("ChkValue");
	//
	// StringBuffer buffer = new StringBuffer();
	// buffer.append(StringUtils.trimToEmpty(cmdId))
	// .append(StringUtils.trimToEmpty(respCode))
	// .append(StringUtils.trimToEmpty(merCustId))
	// .append(StringUtils.trimToEmpty(usrId))
	// .append(StringUtils.trimToEmpty(usrCustId))
	// .append(StringUtils.trimToEmpty(bgRetUrl))
	// .append(StringUtils.trimToEmpty(trxId))
	// .append(StringUtils.trimToEmpty(retUrl))
	// .append(StringUtils.trimToEmpty(merPriv));
	// String plainStr = buffer.toString();
	// System.out.println(plainStr);
	// boolean flag = false;
	// try {
	// flag = signUtils.verifyByRSA(plainStr, chkValue);
	// } catch (Exception e) {
	// logger.debug("用户绑卡验证签名失败"+e.getMessage());
	// e.printStackTrace();
	// }
	// if (!flag) {
	// logger.debug("用户绑卡验证签名失败");
	// System.out.println("验证签名失败...");
	// }
	// }

	/**
	 * @Title: verifyByRSAAcctModify 没有返回参数，无需验证签名
	 * @Description: 账户信息修改（页面) 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws //
	 */
	// private void verifyByRSAAcctModify(Map<String, String> parameter){
	// String cmdId = parameter.get("CmdId");
	// String respCode = parameter.get("RespCode");
	// String respDesc = parameter.get("RespDesc");
	// String merCustId = parameter.get("MerCustId");
	// String usrId = parameter.get("UsrId");
	// String usrCustId = parameter.get("UsrCustId");
	// String bgRetUrl = parameter.get("BgRetUrl");
	// String trxId = parameter.get("TrxId");
	// String retUrl = parameter.get("RetUrl");
	//
	// String idType = parameter.get("IdType");
	// String idNo = parameter.get("IdNo");
	// String usrMp = parameter.get("UsrMp");
	//
	// String usrEmail = parameter.get("UsrEmail");
	// String usrName = parameter.get("UsrName");
	// try {
	// bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// try {
	// retUrl = URLDecoder.decode(retUrl, "UTF-8");
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// String merPriv = parameter.get("MerPriv");
	// if(StringUtils.isNotBlank(merPriv)){
	// try {
	// merPriv = URLDecoder.decode(merPriv, "UTF-8");
	// } catch (UnsupportedEncodingException e1) {
	// e1.printStackTrace();
	// }
	// }
	// String chkValue = parameter.get("ChkValue");
	//
	// StringBuffer buffer = new StringBuffer();
	// buffer.append(StringUtils.trimToEmpty(cmdId))
	// .append(StringUtils.trimToEmpty(respCode))
	// .append(StringUtils.trimToEmpty(merCustId))
	// .append(StringUtils.trimToEmpty(usrId))
	// .append(StringUtils.trimToEmpty(usrCustId))
	// .append(StringUtils.trimToEmpty(bgRetUrl))
	// .append(StringUtils.trimToEmpty(trxId))
	// .append(StringUtils.trimToEmpty(retUrl))
	// .append(StringUtils.trimToEmpty(merPriv));
	// String plainStr = buffer.toString();
	// System.out.println(plainStr);
	// boolean flag = false;
	// try {
	// flag = signUtils.verifyByRSA(plainStr, chkValue);
	// } catch (Exception e) {
	// logger.debug("用户绑卡验证签名失败"+e.getMessage());
	// e.printStackTrace();
	// }
	// if (!flag) {
	// logger.debug("用户绑卡验证签名失败");
	// System.out.println("验证签名失败...");
	// }
	// }

	/**
	 * @Title: verifyByRSACorpRegister(一期没做)
	 * @Description: 企业开户验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void verifyByRSACorpRegister(Map<String, String> parameter) {

		// CmdId +
		// RespCode +MerCustId+ UsrId +UsrName+
		// UsrCustId + AuditStat + TrxId + OpenBankId +
		// CardId+ BgRetUrl+ RespExt
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String usrId = parameter.get("UsrId");
		String usrName = parameter.get("UsrName");
		String usrCustId = parameter.get("UsrCustId");
		String auditStat = parameter.get("AuditStat");

		String trxId = parameter.get("TrxId");
		String openBankId = parameter.get("OpenBankId");
		String cardId = parameter.get("CardId");
		String bgRetUrl = parameter.get("BgRetUrl");

		String respExt = parameter.get("RespExt");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrId))
				.append(StringUtils.trimToEmpty(usrName))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(auditStat))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(openBankId))
				.append(StringUtils.trimToEmpty(cardId))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("企业开户验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("企业开户验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSADelCard
	 * @Description: 删除银行卡 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSADelCard(Map<String, String> parameter) {
		// CmdId +
		// RespCode +MerCustId+ UsrCustId + CardId
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");

		String usrCustId = parameter.get("UsrCustId");
		String cardId = parameter.get("CardId");

		String chkValue = parameter.get("ChkValue");

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
			logger.debug("删除银行卡验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("删除银行卡验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSANetSave
	 * @Description: 网银充值 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSANetSave(Map<String, String> parameter) {

		// CmdId +
		// RespCode + MerCustId + UsrCustId + OrdId+
		// OrdDate + TransAmt + TrxId + RetUrl+
		// BgRetUrl+ MerPriv
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String usrCustId = parameter.get("UsrCustId");
		String ordId = parameter.get("OrdId");
		String ordDate = parameter.get("OrdDate");
		String transAmt = parameter.get("TransAmt");
		String trxId = parameter.get("TrxId");

		String retUrl = parameter.get("RetUrl");
		String bgRetUrl = parameter.get("BgRetUrl");
		String merPriv = parameter.get("MerPriv");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (StringUtils.isNotBlank(retUrl)) {
			try {
				retUrl = URLDecoder.decode(retUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))
				.append(StringUtils.trimToEmpty(transAmt))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("网银充值验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("网银充值验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAInitiativeTender
	 * @Description: 主动投标 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAInitiativeTender(Map<String, String> parameter) {
		// CmdId+ RespCode + MerCustId + OrdId +
		// OrdDate + TransAmt + UsrCustId + TrxId +
		// IsFreeze+ FreezeOrdId+FreezeTrxId +RetUrl +
		// BgRetUrl + MerPriv+ RespExt

		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String ordId = parameter.get("OrdId");
		String ordDate = parameter.get("OrdDate");
		String transAmt = parameter.get("TransAmt");
		String usrCustId = parameter.get("UsrCustId");
		String trxId = parameter.get("TrxId");
		String isFreeze = parameter.get("IsFreeze");
		String freezeOrdId = parameter.get("FreezeOrdId");
		String freezeTrxId = parameter.get("FreezeTrxId");
		String retUrl = parameter.get("RetUrl");
		String bgRetUrl = parameter.get("BgRetUrl");
		String respExt = parameter.get("RespExt");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			retUrl = URLDecoder.decode(retUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))
				.append(StringUtils.trimToEmpty(transAmt))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(isFreeze))
				.append(StringUtils.trimToEmpty(freezeOrdId))
				.append(StringUtils.trimToEmpty(freezeTrxId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(merPriv))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("主动投标 验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("主动投标 验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSALoans
	 * @Description: 自动扣款（放款） 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSALoans(Map<String, String> parameter) {

		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String ordId = parameter.get("OrdId");
		String ordDate = parameter.get("OrdDate");

		String outCustId = parameter.get("OutCustId");
		String outAcctId = parameter.get("OutAcctId");
		String transAmt = parameter.get("TransAmt");
		String fee = parameter.get("Fee");
		String inCustId = parameter.get("InCustId");

		String inAcctId = parameter.get("InAcctId");
		String subOrdId = parameter.get("SubOrdId");
		String subOrdDate = parameter.get("SubOrdDate");
		String feeObjFlag = parameter.get("FeeObjFlag");
		String isDefault = parameter.get("IsDefault");

		String isUnFreeze = parameter.get("IsUnFreeze");
		String unFreezeOrdId = parameter.get("UnFreezeOrdId");
		String freezeTrxId = parameter.get("FreezeTrxId");
		String bgRetUrl = parameter.get("BgRetUrl");
		String respExt = parameter.get("RespExt");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();

		// CmdId +
		// RespCode + MerCustId + OrdId + OrdDate + 5
		// OutCustId + OutAcctId +TransAmt+ Fee+ 9
		// InCustId + InAcctId +SubOrdId+ SubOrdDate+ 13
		// FeeObjFlag+ IsDefault + IsUnFreeze + 16
		// UnFreezeOrdId + FreezeTrxId + BgRetUrl +
		// MerPriv + RespExt 21
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))
				//
				.append(StringUtils.trimToEmpty(outCustId))
				.append(StringUtils.trimToEmpty(outAcctId))
				.append(StringUtils.trimToEmpty(transAmt))
				.append(StringUtils.trimToEmpty(fee))
				.append(StringUtils.trimToEmpty(inCustId))
				//
				.append(StringUtils.trimToEmpty(inAcctId))
				.append(StringUtils.trimToEmpty(subOrdId))
				.append(StringUtils.trimToEmpty(subOrdDate))
				.append(StringUtils.trimToEmpty(feeObjFlag))
				.append(StringUtils.trimToEmpty(isDefault))
				//
				.append(StringUtils.trimToEmpty(isUnFreeze))
				.append(StringUtils.trimToEmpty(unFreezeOrdId))
				.append(StringUtils.trimToEmpty(freezeTrxId))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(merPriv))//
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("自动扣款（放款）验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("自动扣款（放款）验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSARepayment
	 * @Description: 自动扣款（还款） 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSARepayment(Map<String, String> parameter) {
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String ordId = parameter.get("OrdId");
		String ordDate = parameter.get("OrdDate");

		String outCustId = parameter.get("OutCustId");
		String subOrdId = parameter.get("SubOrdId");
		String subOrdDate = parameter.get("SubOrdDate");
		String outAcctId = parameter.get("OutAcctId");
		String transAmt = parameter.get("TransAmt");

		String fee = parameter.get("Fee");
		String inCustId = parameter.get("InCustId");
		String inAcctId = parameter.get("InAcctId");
		String feeObjFlag = parameter.get("FeeObjFlag");
		String bgRetUrl = parameter.get("BgRetUrl");
		String respExt = parameter.get("RespExt");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		// CmdId + RespCode+ MerCustId + OrdId + 4
		// OrdDate + OutCustId + SubOrdId+ 7
		// SubOrdDate+ OutAcctId + TransAmt+ Fee 11
		// + InCustId+ InAcctId+ FeeObjFlag + 14
		// BgRetUrl+ MerPriv + RespExt 17
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))
				//
				.append(StringUtils.trimToEmpty(outCustId))
				.append(StringUtils.trimToEmpty(subOrdId))
				.append(StringUtils.trimToEmpty(subOrdDate))
				.append(StringUtils.trimToEmpty(outAcctId))
				.append(StringUtils.trimToEmpty(transAmt))
				//
				.append(StringUtils.trimToEmpty(fee))
				.append(StringUtils.trimToEmpty(inCustId))
				.append(StringUtils.trimToEmpty(inAcctId))
				.append(StringUtils.trimToEmpty(feeObjFlag))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				//
				.append(StringUtils.trimToEmpty(merPriv))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("自动扣款（还款）验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("自动扣款（还款）验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSATransfer
	 * @Description: 自动扣款转账（商户用 )验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSATransfer(Map<String, String> parameter) {

		// CmdId + RespCode +OrdId + OutCustId +
		// OutAcctId + TransAmt+ InCustId+ InAcctId+
		// RetUrl + BgRetUrl+ MerPriv
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String ordId = parameter.get("OrdId");
		String outCustId = parameter.get("OutCustId");
		String outAcctId = parameter.get("OutAcctId");

		String transAmt = parameter.get("TransAmt");
		String inCustId = parameter.get("InCustId");
		String inAcctId = parameter.get("InAcctId");
		String retUrl = parameter.get("RetUrl");
		String bgRetUrl = parameter.get("BgRetUrl");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		if (StringUtils.isNotBlank(retUrl)) {
			try {
				retUrl = URLDecoder.decode(retUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(outCustId))
				.append(StringUtils.trimToEmpty(outAcctId))//
				.append(StringUtils.trimToEmpty(transAmt))
				.append(StringUtils.trimToEmpty(inCustId))
				.append(StringUtils.trimToEmpty(inAcctId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(bgRetUrl))//
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("自动扣款转账（商户用 )验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("自动扣款转账（商户用 )验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSACash
	 * @Description: 取现 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	//TODO 取现 两次  验证签名
	private void verifyByRSACash(Map<String, String> parameter) {
		//CmdId + RespCode + MerCustId+ OrdId + 
//		UsrCustId + TransAmt+ OpenAcctId + 
//		OpenBankId + FeeAmt + FeeCustId + 
//		FeeAcctId + ServFee + ServFeeAcctId +RetUrl
//		+ BgRetUrl+ MerPriv+ RespExt
		
//		RespType+ RespCode + MerCustId+ OrdId + 
//		UsrCustId + TransAmt+ OpenAcctId + 
//		OpenBankId + RetUrl + BgRetUrl+ MerPriv+ 
//		RespExt
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String respDesc = parameter.get("RespDesc");
		String merCustId = parameter.get("MerCustId");
		String usrId = parameter.get("UsrId");
		String usrCustId = parameter.get("UsrCustId");
		String bgRetUrl = parameter.get("BgRetUrl");
		String trxId = parameter.get("TrxId");
		String retUrl = parameter.get("RetUrl");

		String idType = parameter.get("IdType");
		String idNo = parameter.get("IdNo");
		String usrMp = parameter.get("UsrMp");

		String usrEmail = parameter.get("UsrEmail");
		String usrName = parameter.get("UsrName");
		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			retUrl = URLDecoder.decode(retUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("取现 验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("取现 验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSACreditAssign()
	 * @Description: 债权转让 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * 
	 *
	 */
	private void verifyByRSACreditAssign(Map<String, String> parameter) {
		
//		CmdId  +  RespCode  +  MerCustId  + 
//		SellCustId + CreditAmt   + CreditDealAmt + 
//		Fee  +  BuyCustId  +  OrdId  +  OrdDate  + 
//		RetUrl + BgRetUrl + MerPriv+ RespExt
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String sellCustId = parameter.get("SellCustId");
		String creditAmt = parameter.get("CreditAmt");
		
		String creditDealAmt = parameter.get("CreditDealAmt");
		String fee = parameter.get("Fee");
		String buyCustId = parameter.get("BuyCustId");
		String ordId = parameter.get("OrdId");
		String ordDate = parameter.get("OrdDate");
		
		String retUrl = parameter.get("RetUrl");  
		String bgRetUrl = parameter.get("BgRetUrl");
		String respExt = parameter.get("RespExt");


		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			retUrl = URLDecoder.decode(retUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(sellCustId))
				.append(StringUtils.trimToEmpty(creditAmt))//
				.append(StringUtils.trimToEmpty(creditDealAmt))
				.append(StringUtils.trimToEmpty(fee))
				.append(StringUtils.trimToEmpty(buyCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))//
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(merPriv))
		        .append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("债权转让验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("债权转让验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAUsrAcctPay
	 * @Description: 用户账户支付 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAUsrAcctPay(Map<String, String> parameter) {
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String respDesc = parameter.get("RespDesc");
		String merCustId = parameter.get("MerCustId");
		String usrId = parameter.get("UsrId");
		String usrCustId = parameter.get("UsrCustId");
		String bgRetUrl = parameter.get("BgRetUrl");
		String trxId = parameter.get("TrxId");
		String retUrl = parameter.get("RetUrl");

		String idType = parameter.get("IdType");
		String idNo = parameter.get("IdNo");
		String usrMp = parameter.get("UsrMp");

		String usrEmail = parameter.get("UsrEmail");
		String usrName = parameter.get("UsrName");
		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			retUrl = URLDecoder.decode(retUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("用户绑卡验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("用户绑卡验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAUsrTransfer
	 * @Description: 前台用户间转账 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAUsrTransfer(Map<String, String> parameter) {
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String respDesc = parameter.get("RespDesc");
		String merCustId = parameter.get("MerCustId");
		String usrId = parameter.get("UsrId");
		String usrCustId = parameter.get("UsrCustId");
		String bgRetUrl = parameter.get("BgRetUrl");
		String trxId = parameter.get("TrxId");
		String retUrl = parameter.get("RetUrl");

		String idType = parameter.get("IdType");
		String idNo = parameter.get("IdNo");
		String usrMp = parameter.get("UsrMp");

		String usrEmail = parameter.get("UsrEmail");
		String usrName = parameter.get("UsrName");
		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			retUrl = URLDecoder.decode(retUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("用户绑卡验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("用户绑卡验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAMerCash
	 * @Description: 商户代取现 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAMerCash(Map<String, String> parameter) {
		
//		CmdId  + 
//		RespCode + MerCustId+ OrdId + UsrCustId + 
//		TransAmt + OpenAcctId+ OpenBankId +
//		ServFee  +ServFeeAcctId  +RetUrl  +  BgRetUrl+ 
//		MerPriv+ RespExt
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String ordId = parameter.get("OrdId");
		String usrCustId = parameter.get("UsrCustId");
		
		String transAmt = parameter.get("TransAmt");
		String openAcctId = parameter.get("OpenAcctId");
		String openBankId = parameter.get("OpenBankId");
		String servFee = parameter.get("ServFee");
		String servFeeAcctId = parameter.get("ServFeeAcctId");
		
		String retUrl = parameter.get("RetUrl");
		String bgRetUrl = parameter.get("BgRetUrl");
		String respExt = parameter.get("RespExt");
		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (StringUtils.isNotBlank(retUrl)) {
			try {
				retUrl = URLDecoder.decode(retUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(usrCustId))//
				.append(StringUtils.trimToEmpty(transAmt))
				.append(StringUtils.trimToEmpty(openAcctId))
				.append(StringUtils.trimToEmpty(openBankId))
				.append(StringUtils.trimToEmpty(servFee))
				.append(StringUtils.trimToEmpty(servFeeAcctId))//
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(merPriv))
		        .append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("商户代取现签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("商户代取现验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSATenderCancle
	 * @Description: 投标撤销 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSATenderCancle(Map<String, String> parameter) {

		// CmdId + RespCode + MerCustId + OrdId +
		// OrdDate + TransAmt + UsrCustId +
		// IsUnFreeze+ UnFreezeOrdId + FreezeTrxId+
		// RetUrl + BgRetUrl + MerPriv+ RespExt
		
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String ordId = parameter.get("OrdId");
		String ordDate = parameter.get("OrdDate");
		
		String transAmt = parameter.get("TransAmt");
		String usrCustId = parameter.get("UsrCustId");
		String isUnFreeze = parameter.get("IsUnFreeze");
		String unFreezeOrdId = parameter.get("UnFreezeOrdId");
		String freezeTrxId = parameter.get("FreezeTrxId");
		
		String retUrl = parameter.get("RetUrl");
		String bgRetUrl = parameter.get("BgRetUrl");
		String respExt = parameter.get("RespExt");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (StringUtils.isNotBlank(retUrl)) {
			try {
				retUrl = URLDecoder.decode(retUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))//
				.append(StringUtils.trimToEmpty(transAmt))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(isUnFreeze))
				.append(StringUtils.trimToEmpty(unFreezeOrdId))
				.append(StringUtils.trimToEmpty(freezeTrxId))//
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(merPriv))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("投标撤销验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("投标撤销验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAUsrFreezeBg
	 * @Description: 资金（货款）冻结 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAUsrFreezeBg(Map<String, String> parameter) {

		// CmdId +
		// RespCode + MerCustId + UsrCustId+
		// SubAcctType + SubAcctId + OrdId + OrdDate
		// +TransAmt+ RetUrl+ BgRetUrl+ TrxId +MerPriv
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String usrCustId = parameter.get("UsrCustId");

		String subAcctType = parameter.get("SubAcctType");
		String subAcctId = parameter.get("SubAcctId");
		String ordId = parameter.get("OrdId");
		String ordDate = parameter.get("OrdDate");
		String transAmt = parameter.get("TransAmt");
		String retUrl = parameter.get("RetUrl");
		String bgRetUrl = parameter.get("BgRetUrl");
		String trxId = parameter.get("TrxId");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		if (StringUtils.isNotBlank(retUrl)) {
			try {
				retUrl = URLDecoder.decode(retUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}

		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(subAcctType))
				.append(StringUtils.trimToEmpty(subAcctId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))
				.append(StringUtils.trimToEmpty(transAmt))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("资金（货款）冻结 验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("资金（货款）冻结 验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAUsrUnFreeze
	 * @Description: 资金（货款）解冻 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAUsrUnFreeze(Map<String, String> parameter) {

		// CmdId
		// +RespCode + MerCustId + OrdId + OrdDate +
		// TrxId + RetUrl+ BgRetUrl+ MerPriv

		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String ordId = parameter.get("OrdId");
		String ordDate = parameter.get("OrdDate");

		String bgRetUrl = parameter.get("BgRetUrl");
		String trxId = parameter.get("TrxId");
		String retUrl = parameter.get("RetUrl");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		if (StringUtils.isNotBlank(retUrl)) {
			try {
				retUrl = URLDecoder.decode(retUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}

		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("资金（货款）解冻验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("资金（货款）解冻验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAAutoTender
	 * @Description:自动投标 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAAutoTender(Map<String, String> parameter) {

		// CmdId+ RespCode + MerCustId + OrdId +
		// OrdDate + TransAmt + UsrCustId + TrxId +
		// IsFreeze+ FreezeOrdId+FreezeTrxId +RetUrl +
		// BgRetUrl + MerPriv+ RespExt

		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String ordId = parameter.get("OrdId");
		String ordDate = parameter.get("OrdDate");

		String transAmt = parameter.get("TransAmt");
		String usrCustId = parameter.get("UsrCustId");
		String trxId = parameter.get("TrxId");
		String isFreeze = parameter.get("IsFreeze");
		String freezeOrdId = parameter.get("FreezeOrdId");

		String freezeTrxId = parameter.get("FreezeTrxId");
		String retUrl = parameter.get("RetUrl");
		String bgRetUrl = parameter.get("BgRetUrl");

		String respExt = parameter.get("RespExt");

		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		if (StringUtils.isNotBlank(retUrl)) {
			try {
				retUrl = URLDecoder.decode(retUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))
				.append(StringUtils.trimToEmpty(transAmt))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(isFreeze))
				.append(StringUtils.trimToEmpty(freezeOrdId))
				.append(StringUtils.trimToEmpty(freezeTrxId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(merPriv))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("自动投标验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("自动投标验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAAutoTenderPlan
	 * @Description: 自动投标计划 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAAutoTenderPlan(Map<String, String> parameter) {
		
//		 CmdId + 
//		 RespCode + MerCustId + UsrCustId + 
//		 TenderPlanType + TransAmt + RetUrl + MerPriv
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String usrCustId = parameter.get("UsrCustId");
		String tenderPlanType = parameter.get("TenderPlanType");
		
		String transAmt = parameter.get("TransAmt");
		String retUrl = parameter.get("RetUrl");
		if (StringUtils.isNotBlank(retUrl)) {
			try {
				retUrl = URLDecoder.decode(retUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))  
				.append(StringUtils.trimToEmpty(tenderPlanType))
				.append(StringUtils.trimToEmpty(transAmt))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("自动投标计划 验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("自动投标计划 验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAAutoTenderPlanClose
	 * @Description: 自动投标关闭 验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAAutoTenderPlanClose(Map<String, String> parameter) {
		
//		CmdId + RespCode + MerCustId + UsrCustId + 
//		RetUrl + MerPriv
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String usrCustId = parameter.get("UsrCustId");

		String retUrl = parameter.get("RetUrl");
		if (StringUtils.isNotBlank(retUrl)) {
			try {
				retUrl = URLDecoder.decode(retUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("自动投标关闭 验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("自动投标关闭 验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSACashAudit
	 * @Description: 取现复核验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSACashAudit(Map<String, String> parameter) {
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String respDesc = parameter.get("RespDesc");
		String merCustId = parameter.get("MerCustId");
		String usrId = parameter.get("UsrId");
		String usrCustId = parameter.get("UsrCustId");
		String bgRetUrl = parameter.get("BgRetUrl");
		String trxId = parameter.get("TrxId");
		String retUrl = parameter.get("RetUrl");

		String idType = parameter.get("IdType");
		String idNo = parameter.get("IdNo");
		String usrMp = parameter.get("UsrMp");

		String usrEmail = parameter.get("UsrEmail");
		String usrName = parameter.get("UsrName");
		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			retUrl = URLDecoder.decode(retUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(usrId))
				.append(StringUtils.trimToEmpty(usrCustId))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(trxId))
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(merPriv));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("用户绑卡验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("用户绑卡验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAAutoCreditAssign
	 * @Description: 自动债权转让验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAAutoCreditAssign(Map<String, String> parameter) {
//		 CmdId 
//		 +  RespCode  +  MerCustId  +  SellCustId  + 
//		 CreditAmt  +  CreditDealAmt  +  Fee  + 
//		 BuyCustId  +  OrdId  +  OrdDate  +  RetUrl  + 
//		 BgRetUrl + MerPriv+ RespExt
		
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String sellCustId = parameter.get("SellCustId");
		String creditAmt = parameter.get("CreditAmt");
		
		String creditDealAmt = parameter.get("CreditDealAmt");
		String fee = parameter.get("Fee");
		String buyCustId = parameter.get("BuyCustId");
		String ordId = parameter.get("OrdId");
		String ordDate = parameter.get("OrdDate");
		
		String retUrl = parameter.get("RetUrl");  
		String bgRetUrl = parameter.get("BgRetUrl");
		String respExt = parameter.get("RespExt");
		
		
		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (StringUtils.isNotBlank(retUrl)) {
			try {
				retUrl = URLDecoder.decode(retUrl, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");

		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(sellCustId))
				.append(StringUtils.trimToEmpty(creditAmt))//
				.append(StringUtils.trimToEmpty(creditDealAmt))
				.append(StringUtils.trimToEmpty(fee))
				.append(StringUtils.trimToEmpty(buyCustId))
				.append(StringUtils.trimToEmpty(ordId))
				.append(StringUtils.trimToEmpty(ordDate))//
				.append(StringUtils.trimToEmpty(retUrl))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(merPriv))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("自动债权转让 验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("自动债权转让 验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

	/**
	 * @Title: verifyByRSAAddBidInfo
	 * @Description: 标的信息录入验证签名
	 * @param parameter
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void verifyByRSAAddBidInfo(Map<String, String> parameter) {
//		CmdId+RespCode+MerCustId+ProId+BorrCustId+
//		BorrTotAmt+GuarCompId+GuarAmt+ProArea
//		+BgRetUrl+MerPriv+RespExt
		
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String proId = parameter.get("ProId");
		String borrCustId = parameter.get("BorrCustId");
		
		String borrTotAmt = parameter.get("BorrTotAmt");
		String guarCompId = parameter.get("GuarCompId");
		String guarAmt = parameter.get("GuarAmt");
		String proArea = parameter.get("ProArea");
		String bgRetUrl = parameter.get("BgRetUrl");
		
		String respExt = parameter.get("RespExt");
		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		String merPriv = parameter.get("MerPriv");
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");
		StringBuffer buffer = new StringBuffer();
		buffer.append(StringUtils.trimToEmpty(cmdId))
				.append(StringUtils.trimToEmpty(respCode))
				.append(StringUtils.trimToEmpty(merCustId))
				.append(StringUtils.trimToEmpty(proId))
				.append(StringUtils.trimToEmpty(borrCustId))
				.append(StringUtils.trimToEmpty(borrTotAmt))
				.append(StringUtils.trimToEmpty(guarCompId))
				.append(StringUtils.trimToEmpty(guarAmt))
				.append(StringUtils.trimToEmpty(proArea))
				.append(StringUtils.trimToEmpty(bgRetUrl))
				.append(StringUtils.trimToEmpty(merPriv))
				.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("标的信息录入 验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("标的信息录入 验证签名失败");
			System.out.println("验证签名失败...");
		}
	}
	
	/** 
	* @Title: verifyByRSABatchRepayment 
	* @Description:   批量还款接口   验证签名
	*  @param parameter
	* @return void    返回类型 
	* @throws 
	*/
	private void verifyByRSABatchRepayment(Map<String, String> parameter) {
//		CmdId + RespCode + MerCustId + OutCustId + 
//		OutAcctId +  BatchId + MerOrdDate + 
//		BgRetUrl+ MerPriv + SucNum + FailNum +
//		ErrMsg + ProId+ ReqExt
		
		String cmdId = parameter.get("CmdId");
		String respCode = parameter.get("RespCode");
		String merCustId = parameter.get("MerCustId");
		String outCustId = parameter.get("OutCustId");
		String outAcctId = parameter.get("OutAcctId");
		
		String batchId = parameter.get("BatchId");
		String merOrdDate = parameter.get("MerOrdDate");
		String bgRetUrl = parameter.get("BgRetUrl");
		String merPriv = parameter.get("MerPriv");
		String sucNum = parameter.get("SucNum");
		
		String failNum = parameter.get("FailNum");
		String errMsg = parameter.get("ErrMsg");
		String proId = parameter.get("ProId");
		String respExt = parameter.get("RespExt");
		try {
			bgRetUrl = URLDecoder.decode(bgRetUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		if (StringUtils.isNotBlank(merPriv)) {
			try {
				merPriv = URLDecoder.decode(merPriv, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String chkValue = parameter.get("ChkValue");
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(StringUtils.trimToEmpty(cmdId))
		.append(StringUtils.trimToEmpty(respCode))
		.append(StringUtils.trimToEmpty(merCustId))
		.append(StringUtils.trimToEmpty(outCustId))
		.append(StringUtils.trimToEmpty(outAcctId))//
		.append(StringUtils.trimToEmpty(batchId))
		.append(StringUtils.trimToEmpty(merOrdDate))
		.append(StringUtils.trimToEmpty(bgRetUrl))
		.append(StringUtils.trimToEmpty(merPriv))
		.append(StringUtils.trimToEmpty(sucNum))//
		.append(StringUtils.trimToEmpty(failNum))
		.append(StringUtils.trimToEmpty(errMsg))
		.append(StringUtils.trimToEmpty(proId))
		.append(StringUtils.trimToEmpty(respExt));
		String plainStr = buffer.toString();
		System.out.println(plainStr);
		boolean flag = false;
		try {
			flag = signUtils.verifyByRSA(plainStr, chkValue);
		} catch (Exception e) {
			logger.debug("批量还款接口 验证签名失败" + e.getMessage());
			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("批量还款接口 验证签名失败");
			System.out.println("验证签名失败...");
		}
	}

}