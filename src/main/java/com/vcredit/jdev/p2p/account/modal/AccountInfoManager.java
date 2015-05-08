package com.vcredit.jdev.p2p.account.modal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.capital.modal.CapitalManager;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.repository.AccountRepository;

@Component
public class AccountInfoManager {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private CommonManager commonManager;
	
	@Autowired
	private PasswordManager passwordManager;
	
//	@Autowired
//	private CapitalManager capitalManager;

	/**
	 * @throws Exception
	 * @Title: modifyAccountInfo
	 * @Description: 修改个人账户信息
	 * @param attr 账户属性
	 * @param value 账户属性值
	 * @param id 账户ID
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public int modifyAccountInfo(String attr, String value, Long id) throws Exception {
		Integer val = null;
		if (!("1".equals(attr)) || !("4".equals(attr))) {
			if (StringUtils.isNotBlank(value)) {
				//throw new Exception("修改的值不能为空!");
				val = Integer.valueOf(value);
			}
		}
		if ("1".equals(attr)) {//修改账户联系地址
			return accountRepository.updateAccountAddress(id, value);
		} else if ("2".equals(attr)) {//修改账户最高学历
			return accountRepository.updateAccountEducationDegree(id, val);
		} else if ("3".equals(attr)) {//修改账户月收入
			return accountRepository.updateAccountIncome(id, val);
		} else if ("4".equals(attr)) {//修改账户职业
			return accountRepository.updateAccountIndustry(id, value);
		} else if ("5".equals(attr)) {//修改账户安全等级
			return accountRepository.updateAccountSafety(id, val);
		}
		return 0;
	}
	
	//修改账户联系地址
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public int updateAccountAddress(String value, Long id){
		return accountRepository.updateAccountAddress(id, value);
	}
	
	//修改账户最高学历
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public int updateAccountEducationDegree(String value, Long id){
		return accountRepository.updateAccountEducationDegree(id, getAtrrVaule(value));
	}
	
	//修改账户月收入
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public int updateAccountIncome(String value, Long id){
		return accountRepository.updateAccountIncome(id, getAtrrVaule(value));
	}
	
	//修改账户职业
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public int updateAccountIndustry(String value, Long id){
		return accountRepository.updateAccountIndustry(id, value);
	}
	
	//修改账户安全等级
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public int updateAccountSafety(int safety, Long id){
		return accountRepository.updateAccountSafety(id,safety);
	}
	
	//修改联系地址所在城市
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public int updateAccountProvinceAndCity(Long aid,String provinceCode,String cityCode){
		//Integer cityId = commonManager.getAreaIdByAreaName(value);
		return accountRepository.updateAccountTouchProvinceAndCity(aid, getAtrrVaule(provinceCode), getAtrrVaule(cityCode));
	}
	
	public Integer getAtrrVaule(String value){
		Integer val = null;
		if (StringUtils.isNotBlank(value)) {
			val = Integer.valueOf(value);
		}
		return val;
	}
}
