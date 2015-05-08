package com.vcredit.jdev.p2p.account.modal;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.gplatform.sudoor.server.integration.EventMessageGateway;
import net.gplatform.sudoor.server.security.model.auth.SSAuth;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.dto.AlertDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountQuestionAnswer;
import com.vcredit.jdev.p2p.entity.SystemQuestion;
import com.vcredit.jdev.p2p.repository.AccountQuestionAnswerRepository;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.SystemQuestionRepository;

@Component
public class PasswordManager {

	private static Logger logger = Logger.getLogger(PasswordManager.class);
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountQuestionAnswerRepository aqaRepository;

	@Autowired
	private SSAuth auth;
	
	@Autowired
	private AccountInfoManager accountInfoManager;
	
	@Autowired
	private SystemQuestionRepository systemQuestionRepository;
	
	@Autowired
	private CommonManager commonManager;
	
	@Autowired
	private MobileManager mobileManager;
	
	@Autowired
	private EventMessageGateway eventMessageGateway;


	/** 
	 * @Title: findPassword 
	 * @Description: 找回密码  
	 * @param userName 用户名
	 * @param password 重置的密码
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void findPassword(String userName,String password) throws Exception{
		Account account =accountRepository.findByUserName(userName);
		if(account== null){
			//用户名不存在!
			 logger.error("findPassword--> by userName="+userName+" get account is not exist...");
			throw new Exception("1");
		}
		try {
			auth.updatePasswordByName(account.getAccountSequence().toString(), password);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("findPassword-->find password error:"+e.getMessage());
			//找回密码失败!
			throw new Exception("2");
		}
	}


	/** 
	* @Title: resetPassword 
	* @Description: 重置密码 
	* @param userName 用户名
	* @param oldpwd 老密码
	* @param newpwd 新密码
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void resetPassword(Long accountSequence, String oldpwd,String newpwd) throws Exception{
	     Account account = accountRepository.getOne(accountSequence);
	     if(account == null){
	    	 //用户不存在!
	    	 logger.error("resetPassword--> by accountSequence="+accountSequence+" get account is not exist...");
	    	 throw new Exception("1"); 
	     }
		try{
			auth.authenticateAndSignin(accountSequence.toString(), oldpwd);
		}catch(Exception e) {
			//您输入的当前密码有误，请重新输入!
			 logger.error("resetPassword-->this oldpassword is not right:"+e.getMessage());
			throw new Exception("2");
		}
		try {
			auth.updatePassword(oldpwd, newpwd);
		} catch (Exception e) {
			//重置密码失败!
			logger.error("resetPassword--> set the new password error:"+e.getMessage());
			throw new Exception("3");
		}
		//发送站内信
		try {
			sendAccountResetPasswordMessage(accountSequence.toString());
		} catch (Exception e) {
			//发送站内信失败
			logger.error("resetPassword-->send the message error:"+e.getMessage());
			throw new Exception("4");
		}
	}
	
	public void sendAccountResetPasswordMessage(String aid){
		logger.debug("sendAccountresetPasswordMessage--> begin..........");
		AlertDto dto = new AlertDto();
		AccountMessageTemplateData accountMessageTemplateData = new AccountMessageTemplateData();
		accountMessageTemplateData.setPwdModifyDateTime(DateFormatUtil.dateToString(new Date(), DateFormatUtil.YYYY_MM_DD_HH_MM_SS));;
		dto.setAccountMessageTemplateData(accountMessageTemplateData);
		dto.setAccountMessageTemplateEnum(AccountMessageTemplateEnum.XIU_GAI_MI_MA);
		//站内信_用户Id
		dto.setToUser(aid);
		eventMessageGateway.publishEvent(dto);
		logger.debug("sendAccountResetPasswordMessage--> end..........");
	}
	
	/** 
	* @Title: saveAccountQuestionAnswer 
	* @Description: 保存密码问题与答案
	* @param accountSequence  账号ID
	* @param systemQuestionSequence 密码问题ID
	* @param answer 密码问题答案
	* @param @return    设定文件 
	* @return AccountQuestionAnswer    返回类型 
	* @throws 
	*/
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public AccountQuestionAnswer saveAccountQuestionAnswer(Long accountSequence,Long systemQuestionSequence,String answer){
//		AccountQuestionAnswer accountQuestionAnswer = aqaRepository.findByAccountSequenceAndSystemQuestionSequence(accountSequence, systemQuestionSequence);
//		if(accountQuestionAnswer == null){
//			accountQuestionAnswer = new AccountQuestionAnswer();
//			accountQuestionAnswer.setAccountQuestionAnswer(answer);
//			accountQuestionAnswer.setSystemQuestionSequence(systemQuestionSequence);;
//			accountQuestionAnswer.setAccountSequence(accountSequence);
//			
//		}else{
//			accountQuestionAnswer.setAccountQuestionAnswer(answer);
//		}
		AccountQuestionAnswer accountQuestionAnswer = new AccountQuestionAnswer();
		accountQuestionAnswer.setAccountQuestionAnswer(answer);
		accountQuestionAnswer.setSystemQuestionSequence(systemQuestionSequence);;
		accountQuestionAnswer.setAccountSequence(accountSequence);
		accountQuestionAnswer = aqaRepository.save(accountQuestionAnswer);
		return accountQuestionAnswer ;
	}
	
	public Integer cleanAccountQuestionAnswer(long aid){
		return aqaRepository.delAccountQuestionAnswerByAccountId(aid);
	}
	
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void saveAccountQuestionAnswerList(List<Map<String, String>> list,Long accountSequence){
		//TODO bug 387 1.先删除此用户的安全问题设置
		cleanAccountQuestionAnswer(accountSequence);
		
		//2.保存密码问题与答案
		for (Map<String, String> dto : list) {
			saveAccountQuestionAnswer(accountSequence,Long.valueOf(dto.get("systemQuestionSequence")), dto.get("answer"));
		}
		
		//3.修改 用户的安全等级
		if(checkAccountIsSetQuestionByAid(accountSequence)){
			Account account = commonManager.getAcccontById(accountSequence);
			if(account.getSafety() == 3){
				return;
			}
			if(StringUtils.isBlank(account.getAccountThirdPaymentId())){
				accountInfoManager.updateAccountSafety(2, accountSequence);
			}else{
				accountInfoManager.updateAccountSafety(3, accountSequence);
			}
		}
	}
	
	
	
	/** 
	* @Title: checkAccountQuestionAnswer 
	* @Description: 检测密保问题答案是否正确--忽略大小写
	* @param accountSequence  账号ID
	* @param systemQuestionSequence 密码问题ID
	* @param answer 密码问题答案
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws 
	*/
	public Integer checkAccountQuestionAnswer(String userName,Long systemQuestionSequence,String answer){
		Account account =accountRepository.findByUserNameOrMobile(userName, userName);
		if(account== null){
			//用户名不存在!
			logger.error("checkAccountQuestionAnswer --> by userName="+userName+" get account is not exist...");
			return 1;
		}
		AccountQuestionAnswer accountQuestionAnswer= aqaRepository.findByAccountSequenceAndSystemQuestionSequence(account.getAccountSequence(), systemQuestionSequence);
		if(accountQuestionAnswer == null){
			return 3;
		}else{
			if(answer.equalsIgnoreCase(accountQuestionAnswer.getAccountQuestionAnswer())){
				return 2;
			}else{
				return 3;
			}
		}
		
	}
	
	/**
	 * @throws Exception  
	* @Title: checkAccountIsSetQuestion 
	* @Description: 检验账户是否设置了密码保护问题 
	* @param accountSequence
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws 
	*/
	public boolean checkAccountIsSetQuestion(String userName) throws Exception{
		Account account =accountRepository.findByUserNameOrMobile(userName, userName);
		if(account== null){
			//用户名不存在!
			logger.error("checkAccountIsSetQuestion--> by userName="+userName+" get account is not exist...");
			throw new Exception("1");
		}
		return checkAccountIsSetQuestionByAid(account.getAccountSequence());
	}
	
	public boolean checkAccountIsSetQuestionByAid(Long aid){
		return !aqaRepository.findByAccountSequence(aid).isEmpty();
	}
	
	public List<SystemQuestion> getSystemQuestionListByAid(String userName,int status) throws Exception{
		Account account =accountRepository.findByUserNameOrMobile(userName, userName);
		if(account== null){
			//用户名不存在!
			logger.error("getSystemQuestionListByAid --> by userName="+userName+" get account is not exist...");
			throw new Exception("1");
		}
		return systemQuestionRepository.findSystemQuestionListByAId(account.getAccountSequence(), status);
	}
	
	public List<SystemQuestion> getSystemQuestionList(int status){
		return systemQuestionRepository.findSystemQuestionListByQuestionStatus(status);
	}
	
	
	
	/** 
	* @Title: restPasswordByBg 
	* @Description: 后台触发修改产生随机密码，并发送短信 
	*  @param userName
	*  @throws Exception
	* @return void    返回类型 
	* @throws 
	*/
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void restPasswordByBg(String userName) throws Exception{
		logger.debug("restPasswordByBg --> 后台触发修改产生随机密码，并发送短信  begin......");
		Account account =accountRepository.findByUserName(userName);
		if(account== null){
			logger.error("restPasswordByBg --> by userName="+userName+" get account is not exist...");
			//用户名不存在!
			throw new Exception("1");
		}
		logger.debug("restPasswordByBg --> set new password ....");
		String password = MobileManager.createRandom(true, 6);
		try {
			auth.updatePasswordByName(account.getAccountSequence().toString(), password);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("restPasswordByBg --> set new password error:"+e.getMessage());
			//找回密码失败!
			throw new Exception("2");
		}
		//发送短信 
		mobileManager.sendBgRestPwdMobileMessage(account.getMobile(), password);
		logger.debug("restPasswordByBg ---> 后台触发修改产生随机密码，并发送短信  end......");
	}
	


}
