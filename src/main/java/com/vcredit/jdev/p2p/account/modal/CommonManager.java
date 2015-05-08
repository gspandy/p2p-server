package com.vcredit.jdev.p2p.account.modal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.Area;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.AreaRepository;

@Component
public class CommonManager {

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private AccountRepository accountRepository;

	public Integer getAreaIdByAreaName(String areaName) {
		if (StringUtils.isNotBlank(areaName)) {
			return areaRepository.getAreaIdByAreaName(areaName);
		}
		return null;
	}
	
	public Area findByAddressChineseName(String areaName){
		if (StringUtils.isNotBlank(areaName)) {
			return areaRepository.findByAddressChineseName(areaName);
		}
		return null;
	} 

	public Account getAcccontByUserNameAndMobile(String userName,String mobile) {
		return accountRepository.findByUserNameAndMobile(userName, mobile);
	}
	public Account getAcccontByUserName(String userName) {
		return accountRepository.findByUserName(userName);
	}

	public boolean isExistUserName(String userName) {
		return null != getAcccontByUserName(userName);
	}
	
	public boolean isUserNameMatchMobile(String userName,String mobile) {
		return null != getAcccontByUserNameAndMobile(userName, mobile);
	}

	public Account getAcccontByMobile(String mobile) {
		return accountRepository.findByMobile(mobile);
	}
	
	public Account getAcccontById(Long aid) {
		return accountRepository.findOne(aid);
	}
	
	public boolean checkMobile(Long aid,String mobile){
		Account account = accountRepository.findOne(aid);
		if(mobile.equals(account.getMobile())){
			return true;
		}else{
			return false;
		}
	}

	public boolean isExistMobile(String mobile) {
		return null != getAcccontByMobile(mobile);
	}

	public Account getAcccontByUserNameOrMobile(String userName) {
		return accountRepository.findByUserNameOrMobile(userName, userName);
	}

	public boolean isExistUserNameOrMobile(String userName) {
		return null != getAcccontByUserNameOrMobile(userName);
	}

}
