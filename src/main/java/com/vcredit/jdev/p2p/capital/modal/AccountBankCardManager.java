package com.vcredit.jdev.p2p.capital.modal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.chinapnr.account.UserManager;
import com.vcredit.jdev.p2p.dto.ThirdPayUserResponseDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountBankCard;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.AccountBankCardEnum;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.LoanDataStatusEnum;
import com.vcredit.jdev.p2p.enums.ThirdChannelEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountBindStatusEnum;
import com.vcredit.jdev.p2p.repository.AccountBankCardRecordRepository;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.CapitalRecordRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;

/**
 * 银行卡管理
 * 
 * @author 周佩 创建事件 20141218
 */
@Component
@MessageEndpoint
public class AccountBankCardManager {
	
	private Logger logger = LoggerFactory.getLogger(AccountBankCardManager.class);

	@Autowired
	private AccountBankCardRecordRepository accountBankCardRecordRepository;
	@Autowired
	private EventMessageGateway eventMessageGateway;
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private CapitalRecordRepository capitalRecordRepository;
	@Autowired
	private LoanDataRepository loanDataRepository;
	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;
	@Autowired
	private DictionaryUtil dictionaryUtil;
	@Autowired
	private InvestmentRepository investmentRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UserManager userManager;
	
	/**
	 * 设定默认银行卡
	 * 
	 * @param abc
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public AccountBankCard optionsDefaultDebit(String bankCard,Long aid) {
		List<AccountBankCard> res=accountBankCardRecordRepository.findByAccountSequnece(aid);
		AccountBankCard result = null;
		List<AccountBankCard> remove = new ArrayList<AccountBankCard>();
		List<AccountBankCard> update = new ArrayList<AccountBankCard>();
		for(AccountBankCard a:res){
			if(bankCard.equals(a.getBankCardNumber())){//需要修改的卡
				a.setBankCardDefaultDrawStatus(AccountBankCardEnum.DEFAULT_CARD.getCode());
				update.add(a);
				result = a;
			}else{//需要去掉默认的卡
				a.setBankCardDefaultDrawStatus(AccountBankCardEnum.NOT_DEFAULT_CARD.getCode());
				remove.add(a);
			}
		}
		if(update.size()>0){//只有存在设置的默认卡才能继续，不然不给修改，保证一个用户只有一个默认卡
				accountBankCardRecordRepository.save(update);
				accountBankCardRecordRepository.save(remove);
		}
		return result;
	}
	
	/**
	 * 后台运维平台绑卡入口
	 * @param userId
	 * @throws Exception
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void bindCardBg(Long userId) throws Exception{
		ThirdPaymentAccount tpa=capitalRecordRepository.findByAccountSequence(userId);
		bindCardBg(tpa.getThirdPaymentUniqueId().toString(), userId);
	}
	
	/**
	 * 根据用户注册的结果之后进行绑卡-后台-借款人
	 * @param dto
	 * @throws Exception
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void bindCardBg(String usrCustId,Long userId) throws Exception{
		//根据当前用户查询loanData对应的银行卡.
		//loanData
		List<InvestmentAccountReference> iafes=investmentAccountReferenceRepository.findByAccountSequence(userId);
		for(InvestmentAccountReference i:iafes){
			LoanData l=loanDataRepository.findOne(i.getLoanDataSequence());
//			String value=dictionaryUtil.getDicValue(DictionaryEnum.T_DIC.getCode()+DictionaryEnum.T_DIC_BANK_CODE.getCode()+l.getBankCode());//得到值
			String bankCode=dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode()+DictionaryEnum.T_DIC_BANK_ENAME.getCode()+l.getBankCode());
			String bankprovince=l.getBankProvince()==null?"0031":l.getBankProvince();
			String openAreaId=l.getBankCity()==null?"3100":l.getBankCity();
			if(bankprovince.length()==6){//因为汇付跟本地的国标地区编码不一致
				bankprovince="00"+bankprovince.substring(0,2);
			}
			if(openAreaId.length()==6){//因为汇付跟本地的国标地区编码不一致
				openAreaId=openAreaId.substring(0,4);
			}
			List<AccountBankCard> abc=accountBankCardRecordRepository.findBybankCardNumber(l.getBankCardNumber());
			if(abc==null||abc.size()==0){//不存在卡，则开始绑卡
				String jsonStr=userManager.bgBindCard(usrCustId, l.getBankCardNumber(), bankCode, bankprovince, openAreaId, "", "Y");
				logger.info("jsonStr:"+jsonStr+"@"+usrCustId+"@"+l.getBankCardNumber()+"@"+bankCode+"@"+bankprovince+"@"+openAreaId+"@"+""+"@"+"Y");
				ThirdPayUserResponseDto response=ThirdPayUserResponseDto.formateJson(jsonStr);
				if(ThirdChannelEnum.RESPCODEVALUE.getCode().equals(response.getRespCode())){//成功才绑卡
					//银行卡的开户地转化
					Integer province=Integer.valueOf(l.getBankProvince()==null?"0":l.getBankProvince());
					Integer bankCity=Integer.valueOf(l.getBankCity()==null?"0":l.getBankCity());
					Integer bankTown=Integer.valueOf(l.getBankTown()==null?"0":l.getBankTown());
					response.setProvince(province);
					response.setBankCity(bankCity);
					response.setBankTown(bankTown);
					bindCardWeb(response);
					//更新投资项目状态将激活中变成预发布：2->3,去掉待发布
					Investment inv=investmentRepository.findOne(i.getInvestmentSequence());
					if(InvestmentStatusEnum.ACTIVEING.getCode().equals(inv.getInvestmentStatus())){//如果是激活中的状态2将被修改为3
						inv.setInvestmentStatus(InvestmentStatusEnum.RELEASE_WAIT.getCode());
						investmentRepository.save(inv);
						//将loanData数据状态更新
						LoanData ld=loanDataRepository.findOne(i.getLoanDataSequence());
						ld.setProgressStauts(LoanDataStatusEnum.COMPLETE_BANKCARD_BIND.getCode());
						ld.setRecordOperateDate(new Date());
						loanDataRepository.save(ld);
					}
				}else{
					logger.info("bidcard is error:"+"jsonStr:"+jsonStr+"@"+usrCustId+"@"+l.getBankCardNumber()+"@"+bankCode+"@"+bankprovince+"@"+openAreaId+"@"+""+"@"+"Y");
				}
			}else{
				logger.info("银行卡已经存在！accountbankCardId:"+abc.get(0).getAccountSequnece());
			}
			
		}
	}
	
	/**
	 * 三方账号开好户之后前台绑卡回调入口
	 * @param accountId	账户Id
	 * @param investId	项目Id
	 * @throws Exception 
	 * 
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void bindCardWeb(ThirdPayUserResponseDto dto) throws Exception{
		ThirdPaymentAccount third=capitalRecordRepository.findByThirdPaymentUniqueId(Long.valueOf(dto.getUsrCustId()));
		Account account=accountRepository.findOne(third.getAccountSequence());
		List<AccountBankCard> abc=accountBankCardRecordRepository.findBybankCardNumber(dto.getOpenAcctId());
		if(abc==null||abc.size()==0){//不存在卡，则开始绑卡
			saveCard(third.getAccountSequence(),dto.getOpenBankId(),new BigDecimal(dto.getOpenAcctId())
			,account.getMobile(),third.getThirdPaymentId(),dto.getProvince(),dto.getBankCity(),dto.getBankTown());
			//更新三方账户
			if(ThirdPaymentAccountBindStatusEnum.IS_NO_BIND.getCode().equals(third.getThirdPaymentIdBindStatus())){
				third.setThirdPaymentIdBindStatus(ThirdPaymentAccountBindStatusEnum.IS_BIND.getCode());//绑卡
				third.setRecordOperateDate(new Date());//操作时间
				capitalRecordRepository.save(third);
			}
		}
	}
	
	
	/**
	 * 三方账号开好户前台绑卡
	 * @param third
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	private void saveCard(Long accountId,String bankCodePar,
			BigDecimal bankCardNum,String mobile,
			String thirdAccountId,Integer bankProvince,
			Integer bankCity,Integer bankTown){
		AccountBankCard abc=new AccountBankCard();
		
		//银行卡
		abc.setAccountSequnece(accountId);
		String bankCode=dictionaryUtil.getDicValue(DictionaryEnum.T_DIC.getCode()+
				DictionaryEnum.T_DIC_BANK_ENAME.getCode()+bankCodePar);
		String bankName=dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode()+
				DictionaryEnum.T_DIC_BANK_ENAME.getCode()+bankCode);
		abc.setBankCode(Integer.valueOf(bankCode));
		abc.setBankCardNumber(bankCardNum.toString());
		abc.setBankCardDefaultDrawStatus(AccountBankCardEnum.DEFAULT_CARD.getCode());
		abc.setBankProvince(bankProvince);//
		abc.setBankName(bankName);
		abc.setBankCity(bankCity);//
		abc.setBankTown(bankTown);//
		abc.setBankMobile(mobile);
		abc.setBankPrivateZone("");
		abc.setBankDataCharacterSet("");
		abc.setThirdPaymentId(thirdAccountId);
		accountBankCardRecordRepository.save(abc);
		//将其他的默认卡删除
		optionsDefaultDebit(bankCardNum.toString(),accountId);
	}
	
	/**
	 * 三方账号删除银行卡
	 * @param accountId	账户Id
	 * @param investId	项目Id
	 * @throws Exception 
	 * 
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void deleteCard(Map<String,String> parameter) throws Exception{
		String accountId = String.valueOf(parameter.get("accountId"));
		String bankNumber = String.valueOf(parameter.get("bankNumber"));
		//根据当前用户查询loanData对应的银行卡.
		List<AccountBankCard> result=accountBankCardRecordRepository.findBybankCardNumber(bankNumber);
		if(result!=null){
			accountBankCardRecordRepository.deleteInBatch(result);
		}
	}
	
	/**
	 * 当前用户所拥有的银行卡
	 * @param usrId	账户Id
	 * @throws Exception 
	 * 
	 */
	public List<AccountBankCard> queryAccountBankCard(Long usrId){
		return accountBankCardRecordRepository.findByAccountSequnece(usrId);
	}
}
