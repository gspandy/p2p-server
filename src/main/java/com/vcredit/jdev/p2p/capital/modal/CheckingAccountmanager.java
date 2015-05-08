package com.vcredit.jdev.p2p.capital.modal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.chinapnr.query.QueryManager;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;
import com.vcredit.jdev.p2p.dto.ThirdPayUserResponseDto;
import com.vcredit.jdev.p2p.dto.UsrCardInfoDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.CapitalRecordRepository;
/**
 * 信息比对业务类
 * @author 周佩
 *
 */
@Component
@MessageEndpoint
public class CheckingAccountmanager {
	@Autowired
	private SignUtils signUtils;
	@Autowired
	private CapitalRecordRepository capitalRecordRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private QueryManager queryManager;
	@Autowired
	private CapitalPlatformManager capitalPlatformManager;
	@Autowired
	private AccountBankCardManager accountBankCardManager;

	/**
	 * 比对汇付的账户跟本地的账户信息
	 * @param userId
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public void synchronizeThirdAccount(Long userId) throws NumberFormatException, Exception {
		List<Account> acces= null;
		if(userId!=null){//指定账号同步
			acces= new ArrayList<Account>();
			Account acce=accountRepository.findOne(userId);
			acces.add(acce);
		}else{
			acces=accountRepository.findByPidNotNullAndAccountThirdPaymentIdIsNull();
		}
		for(Account a:acces){
			if(a.getPid()==null||"".equals(a.getPid().trim())){
				continue;
			}
			ThirdPaymentAccount tpa=capitalRecordRepository.findByAccountSequence(a.getAccountSequence());
			if(tpa==null){//不存在账户
				String jsonStr=queryManager.queryUsrInfo(a.getPid());
				ThirdPayUserResponseDto dto=ThirdPayUserResponseDto.formateJson(jsonStr);
				if(dto.getUsrCustId()!=null&&!"".equals(dto.getUsrCustId().trim())){
					capitalPlatformManager.openCreditCapitalAccount(a.getAccountSequence(), dto.getUsrId(), Long.valueOf(dto.getUsrCustId()),dto.getIdNo(),dto.getUsrEmail(),dto.getUsrName(),dto.getMobile());
				}
			}
		}
	}
	
	/**
	 * 比对汇付的银行卡跟本地的银行卡信息
	 * @param userId
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public void synchronizeAccountBankCard(Long userId) throws NumberFormatException, Exception {
		List<ThirdPaymentAccount> tpaes= null;
		if(userId!=null){//指定账号同步
			tpaes= new ArrayList<ThirdPaymentAccount>();
			ThirdPaymentAccount tpa=capitalRecordRepository.findByAccountSequence(userId);
			tpaes.add(tpa);
		}else{
			//根据时间为最近2天创建的三方账号当天创建的进行比对
			Calendar c=Calendar.getInstance();
			c.add(Calendar.DATE, -2);
			Date d=c.getTime();
			tpaes=capitalRecordRepository.findByThirdPaymentIdRegisterDateGreaterThan(d);
		}
		for(ThirdPaymentAccount a:tpaes){
			if(a.getThirdPaymentUniqueId()==null||"".equals(a.getThirdPaymentUniqueId().toString().trim())){
				continue;
			}
			String jsonStr=queryManager.queryCardInfo(a.getThirdPaymentUniqueId().toString(),null);
			ThirdPayUserResponseDto dto=ThirdPayUserResponseDto.formateJson(jsonStr);
			if(dto.getUsrCustId()!=null&&!"".equals(dto.getUsrCustId().trim())){
				for(UsrCardInfoDto d:dto.getUsrCardInfolist()){
					if(d!=null&&!"".equals(d.getCertId())){
						ThirdPayUserResponseDto tmp=new ThirdPayUserResponseDto();
						tmp.setUsrCustId(d.getUsrCustId());
						tmp.setOpenAcctId(d.getCardId());
						tmp.setOpenBankId(d.getBankId());
						accountBankCardManager.bindCardWeb(tmp);
					}
				}
			}
		}
	}
}
