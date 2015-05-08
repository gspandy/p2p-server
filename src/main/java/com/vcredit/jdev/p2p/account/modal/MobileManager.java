package com.vcredit.jdev.p2p.account.modal;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.alert.service.AccountMessageChannelEnum;
import com.vcredit.jdev.p2p.alert.service.AccountMessageManager;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;
import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.vbs.SmsJsonDto;
import com.vcredit.jdev.p2p.base.vbs.VcreditSmsServiceVBS;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.pdf.exception.CommonDefinedException;
import com.vcredit.jdev.p2p.pdf.exception.ErrorCodeException.CommonException;
import com.vcredit.jdev.p2p.repository.AccountRepository;

@Component
@ConfigurationProperties(prefix = "mobile.msg")
public class MobileManager {
	
	private static Logger logger = LoggerFactory.getLogger(MobileManager.class);
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private P2pSessionContext p2pSessionContext;

	@Autowired
	private VcreditSmsServiceVBS smsServiceVBS;
	
	@Autowired
	private AccountMessageManager accountMessageManager;
	
	@Autowired
	private VcreditSmsServiceVBS vcreditSmsServiceVBS;
	
	@Autowired
	private CommonManager commonManager;
	
	private String masterKey;
	
	public String getMasterKey() {
		return masterKey;
	}

	public void setMasterKey(String masterKey) {
		this.masterKey = masterKey;
	}

	@Transactional
	public int editAccountMobile(Long id, String mobile) {
		Account account = accountRepository.findByMobile(mobile);
		if(account!=null){
			if(account.getAccountSequence() != id){
				return -1;
			}
		}else{
			accountRepository.updateAccountMobile(id, mobile);
		}
		
		return 1;
	}
	


	/**
	 * 创建指定数量的随机字符串
	 * 
	 * @param numberFlag
	 *            是否是数字
	 * @param length
	 * @return
	 */
	public static String createRandom(boolean numberFlag, int length) {
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);

		return retStr;
	}

	/**
	 * @Title: mobileCodeValidate
	 * @Description: 手机验证码验证
	 * @param request
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public int mobileCodeValidate(String mobile, String mobileVerificationCode) {
		Long createDate = (Long) p2pSessionContext.getAttribute(mobile+"_time");
		Long validateDate = new Date().getTime();
		System.out.println("mobileCodeValidate:sessionId="+p2pSessionContext.getSessionId());
		logger.debug("############################mobileCodeValidate:sessionId="+p2pSessionContext.getSessionId());
		if (StringUtils.isNotBlank(masterKey)) {
			logger.warn("Master key configed for mobile message!!!, you can comment out: mobile.msg.masterKey={} in application.properties", masterKey);
			if (StringUtils.equalsIgnoreCase(masterKey, mobileVerificationCode)) {
				//p2pSessionContext.removeAttribute(mobile);
				return 1;
			}
		}
		if(validateDate-createDate>60*2*1000L){
			logger.debug("mobileCodeValidate Validate timeout 120s ......");
			p2pSessionContext.removeAttribute(mobile);
			return 2;
		}
		String codeFromSession = (String) p2pSessionContext.getAttribute(mobile);
		if (StringUtils.equalsIgnoreCase(codeFromSession, mobileVerificationCode)) {
			//p2pSessionContext.removeAttribute(mobile);
			return 1;
		}
		return 0;
	}

	/**
	 * @throws Exception
	 * @Title: sendMobileVerificationCode
	 * @Description: 发送手机验证码
	 * @param mobile
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String sendMobileVerificationCode(String mobile,String sendKind) throws Exception {
		logger.debug("sendMobileVerificationCode begin ################");
		//验证码
		String code = createRandom(true, 6);
		//将验证码保存到Session中
		p2pSessionContext.setAttribute(mobile, code);
		logger.debug("sendMobileVerificationCode code:"+code);
		System.out.println("手机验证码：" + code);
		//content
		System.out.println("sendMobileVerificationCode:sessionId="+p2pSessionContext.getSessionId());
		logger.debug("############################  sendMobileVerificationCode:sessionId="+p2pSessionContext.getSessionId());

		
		
		//TODO ----2015/3/13 begin
//		AccountMessageTemplateData atdata = new AccountMessageTemplateData();
//		atdata.setMobileVerificationCode(code);
//		if("1".equals(sendKind)){//注册动态码 短信
//			accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHU_CE_YAN_ZHENG,
//					atdata, AccountMessageChannelEnum.MOBILE, mobile);
//		}else if("2".equals(sendKind)){//找回密码动态码 短信
//			accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHAO_HUI_MI_MA,
//					atdata, AccountMessageChannelEnum.MOBILE, mobile);
//		}
//		else if("3".equals(sendKind)){//提现动态码  短信
//			accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.TI_XIAN_YAN_ZHENG,
//					atdata, AccountMessageChannelEnum.MOBILE, mobile);
//		}
//		else if("4".equals(sendKind)){//修改手机号动态码 短信
//			accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.XIU_GAI_SHOU_JI,
//					atdata, AccountMessageChannelEnum.MOBILE, mobile);
//		}
//		else if("5".equals(sendKind)){//找回安全保护问题动态码 短信
//			accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.ZHAO_HUI_AN_QUAN_WENTI,
//					atdata, AccountMessageChannelEnum.MOBILE, mobile);
//		}
//		else if("6".equals(sendKind)){//后台重置密码 短信
//			accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.HOU_TAI_CHONG_SHE_MIMA,
//					atdata, AccountMessageChannelEnum.MOBILE, mobile);
//		}
		//TODO ----2015/3/13 end 
		
		//TODO change send message method and only use a templet dk 2015/3/13 
		//SmsJsonDto smsJsonDto = new SmsJsonDto(0, "", mobile, "13866", "P2P", "维金荟-注册验证短信", 1, code+",120", "");
		//logger.debug("sendMobileVerificationCode entity:"+smsJsonDto.toString());
		vcreditSmsServiceVBS.mobileYunSendMessage(mobile,code);
		p2pSessionContext.setAttribute(mobile+"_time", new Date().getTime());
		logger.debug("sendMobileVerificationCode end ################");
		return null;
	}
	
	
	
	public void sendMoblieMessageToBorrower(String userName,String mail) throws CommonException{
		logger.debug("step[vbsPdf] sendMoblieMessageToBorrower--> begin....");
		try {
			Account account = commonManager.getAcccontByUserName(userName);
			if(account == null||StringUtils.isBlank(mail)){
				logger.error("step[vbsPdf] sendMoblieMessageToBorrower-->userName:"+userName+" is not exist or email:"+mail +" is null");
				CommonException ce = new CommonDefinedException().MSG_SEND_FAIL_DETAIL1;
				throw ce;
			}
			String mobile = account.getMobile();
			if(StringUtils.isBlank(mobile)){
				logger.error("step[vbsPdf] sendMoblieMessageToBorrower--> this mobile of user:username="+userName+" is null");
				CommonException ce = new CommonDefinedException().MSG_SEND_FAIL_DETAIL2;
				throw ce;
			}
			sendBorrowerAfterLoanFinish(mobile, mail);
		} catch (Exception e) {
			CommonException ce = new CommonDefinedException().MSG_SEND_FAIL;
			ce.setDetail(e.getMessage());
			throw ce;
		}
		logger.debug("step[vbsPdf] sendMoblieMessageToBorrower-->sendMoblieMessageToBorrower end....");
	}
	
	/** 
	* @Title: sendBorrowerAfterLoanFinish 
	* @Description: 放款短信通知给借款人
	*  @param mobile
	*  @param mail
	*  @throws Exception
	* @return void    返回类型 
	* @throws 
	*/
	public void sendBorrowerAfterLoanFinish(String mobile,String mail){
		logger.debug("step[vbsPdf] sendBorrowerAfterLoanFinish--->(放款短信通知给借款人) begin  ....");
		AccountMessageTemplateData atdata = new AccountMessageTemplateData();
		atdata.setMail(mail);;
		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.FANG_KUAN_DUAN_XING,
				atdata, AccountMessageChannelEnum.MOBILE, mobile);
		logger.debug("step[vbsPdf] sendBorrowerAfterLoanFinish-->(放款短信通知给借款人) end  ....");
	}
	
	/** 
	* @Title: sendBgRestPwd 
	* @Description: 后台重置密码发送短信
	*  @param mobile 手机号
	*  @param pwd    密码
	* @return void    返回类型 
	* @throws 
	*/
	public void sendBgRestPwdMobileMessage(String mobile,String pwd){
		logger.debug("sendBgRestPwdMobileMessage---> 后台重置密码发送短信  begin......");
		AccountMessageTemplateData atdata = new AccountMessageTemplateData();
		atdata.setMobileVerificationCode(pwd);;
		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.HOU_TAI_CHONG_SHE_MIMA,
				atdata, AccountMessageChannelEnum.MOBILE, mobile);
		logger.debug("sendBgRestPwdMobileMessage--> 后台重置密码发送短信  end......");
	}

}
