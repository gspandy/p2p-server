package com.vcredit.jdev.p2p.account.modal;

import java.util.Date;

import net.gplatform.sudoor.server.security.model.auth.SSAuth;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.dto.IpAdressDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountLoginRecord;
import com.vcredit.jdev.p2p.repository.AccountLoginRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountRepository;

@Component
public class LoginManager {

	private static Logger logger = Logger.getLogger(LoginManager.class);
	
	@Autowired
	private SSAuth auth;

	@Autowired
	private P2pSessionContext p2pSessionContext;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountLoginRecordRepository alrRepository;

	@Autowired
	private CommonManager commonManager;

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public int login(String userName, String password, String ip){
		Account accont = commonManager.getAcccontByUserNameOrMobile(userName);
		//验证用户是否存在
		if (accont == null) {
			//此用户名不存在!
			logger.debug("此用户名不存在!");
			return 1;
		}
		
		//验证密码是否准确
		try {
			auth.authenticateAndSignin(accont.getAccountSequence().toString(), password);
			p2pSessionContext.setAttribute("aid", accont.getAccountSequence().toString());
			System.out.println(p2pSessionContext.getSessionId());
		} catch (Exception e) {
			//输入的密码不准确!
			logger.debug("输入的密码不准确!"+e.getLocalizedMessage());
			return 2;
		}
		//添加登录痕迹
		try {
			addAccountLoginRecord(accont.getAccountSequence(), ip);
		} catch (Exception e) {
			//添加登录记录失败!
			logger.debug("添加登录记录失败!"+e.getLocalizedMessage());
			return 3;
		}
		return 4;
	}
	
	public int  checkMobileAndPwd(String mobile,String password){
		Account account  = accountRepository.findByMobile(mobile);
		//验证用户是否存在
		if (account == null) {
			//此用户名不存在!
			logger.debug("此用户名不存在!");
			return 1;
		}
		//验证密码是否准确
		try {
			auth.authenticate(account.getAccountSequence().toString(), password);
		} catch (Exception e) {
			logger.debug("输入的密码不准确!"+e.getLocalizedMessage());
			//输入的密码不准确!
			return 2;
		}
		return 4;
		
	}
	public int checkPwd(Long aid,String password){
		//验证密码是否准确
		try {
			auth.authenticate(aid.toString(), password);
		} catch (Exception e) {
			logger.debug("输入的密码不准确!"+e.getLocalizedMessage());
			return 0;
			//输入的密码不准确!
		}
		return 1;
		
	}

	/**
	 * @Title: addAccountLoginRecord
	 * @Description: 添加登录日志
	 * @param accountSequence 登录账户ID
	 * @param ip 登录账户IP
	 * @param @return 设定文件
	 * @return AccountLoginRecord 返回类型
	 * @throws
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public AccountLoginRecord addAccountLoginRecord(Long accountSequence, String ip) {
		//*ip:10.100.12.34 *country:未分配或者内网IP *area: *province: *city: *district: *carrier:
		//*ip:117.89.35.58 *country:中国 *area:华东 *province:江苏省 *city:南京市 *district:白下区 *carrier:电信
		AccountLoginRecord accountLoginRecord = new AccountLoginRecord();
		accountLoginRecord.setAccountLoginDate(new Date());
		accountLoginRecord.setAccountSequence(accountSequence);
		accountLoginRecord.setLoginIp(ip);
		IpAdressDto ipdto = P2pUtil.getAddressByIP(ip);
		if (ipdto!=null&&!(ConstantsUtil.UNALLOCATED_OR_INTRANET_IP.equals(ipdto.getCountry()))) {
			accountLoginRecord.setIpAddressProvince(commonManager.getAreaIdByAreaName(ipdto.getProvince()));
			accountLoginRecord.setIpAddressCity(commonManager.getAreaIdByAreaName(ipdto.getCity()));
			accountLoginRecord.setIpAddressTown(commonManager.getAreaIdByAreaName(ipdto.getDistrict()));
		}
		accountLoginRecord.setSessionId(p2pSessionContext.getSessionId());
		return alrRepository.save(accountLoginRecord);
	}

}
