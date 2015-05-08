package com.vcredit.jdev.p2p.account.modal;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.number.CurrencyFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.alert.service.AccountMessageChannelEnum;
import com.vcredit.jdev.p2p.alert.service.AccountMessageManager;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.PropertiesUtils;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.repository.AccountRepository;

@Component
public class EmailManager {
	
	private final static Logger logger = LoggerFactory.getLogger(EmailManager.class);
	
	@Autowired
	private PropertiesUtils propertiesUtils;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	private AccountMessageManager accountMessageManager;

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public int editAccountEmail(Long id, String email) {
			return accountRepository.updateAccountEamil(id, email,ConstantsUtil.EntityStatus.STATUS_DISABLE);
	}
	
	
	/** 
	* @Title: editEmailAndSend 
	* @Description:修改用户邮件并给用户发送邮件以确认绑定 
	*  @param id
	*  @param email
	* @return void    返回类型 
	* @throws 
	*/
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void editEmailAndSend(Long id, String email,String type) {
		//editAccountEmail(id, email);
		Account account  = accountRepository.findOne(id);
		String url =  propertiesUtils.getAppUrl()+"/p2p/data/ws/rest/accountget/bindEmail?email="+email+"&aid="+id+"&type="+type;
		AccountMessageTemplateData atdata = new AccountMessageTemplateData();
		atdata.setUsername(account.getUserName());
		atdata.setValidateURL(url);
			accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.EMAIL_ADDR_YAN_ZHENG,
					atdata, AccountMessageChannelEnum.EMAIL, email);
	}
	
	
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public int bindAccountEmail(Long id, String email) {
		//Account account  = accountRepository.findOne(id);
		try {
			accountRepository.updateAccountEamil(id, email,ConstantsUtil.EntityStatus.STATUS_ENABLE);
			return 1;
		} catch (Exception e) {
			logger.error("bindAccountEmail error:"+e.getMessage());
			return 0;
		}
	    //accountRepository.bindAccountEamil(id, ConstantsUtil.EntityStatus.STATUS_ENABLE);
			
	}

	/**
	 * 验证是否是邮箱地址
	 * 
	 * @param email
	 * @return
	 */
	public boolean checkEmail(String email) {
		boolean flag = false;
		try {
			//String check = "^[a-zA-Z0-9_-]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$";
			String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
}
