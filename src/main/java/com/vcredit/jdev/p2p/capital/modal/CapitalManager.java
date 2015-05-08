package com.vcredit.jdev.p2p.capital.modal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vcredit.jdev.p2p.account.modal.RegisterManager;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.chinapnr.account.UserManager;
import com.vcredit.jdev.p2p.chinapnr.deal.FundManager;
import com.vcredit.jdev.p2p.chinapnr.deal.LoansManager;
import com.vcredit.jdev.p2p.chinapnr.query.QueryManager;
import com.vcredit.jdev.p2p.dto.AcctDetail;
import com.vcredit.jdev.p2p.dto.ThirdPayUserResponseDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.Phones;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.ThirdChannelEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountActiveStatusEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountBindStatusEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountTypeEnum;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.CapitalRecordRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;
import com.vcredit.jdev.p2p.repository.PhonesRepository;
import com.vcredit.jdev.p2p.util.NumberUtil;

/**
 * 资金账户管理业务类
 * 
 * @author 周佩 创建时间：20141211
 */
public abstract class CapitalManager {
	private Logger logger = LoggerFactory.getLogger(CapitalManager.class);

	private static String ACCOUNT = "LS";

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	protected EventMessageGateway eventMessageGateway;
	@Autowired
	private CapitalRecordRepository capitalRecordRepository;
	@Autowired
	private InvestmentRepository investmentRepository;
	@Autowired
	private LoanDataRepository loanDataRepository;
	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;
	@Autowired
	private UserManager urManager;
	@Autowired
	private FundManager fundManager;
	@Autowired
	private QueryManager queryManager;
	@Autowired
	private LoansManager loansManager;
	@Autowired
	private RegisterManager registerManager;
	@Autowired
	private AccountBankCardManager accountBankCardManager;
	@Autowired
	private PhonesRepository phonesrepository;
	
	public FundManager getFundManager() {
		return fundManager;
	}

	public void setFundManager(FundManager fundManager) {
		this.fundManager = fundManager;
	}

	public LoansManager getLoansManager() {
		return loansManager;
	}

	public void setLoansManager(LoansManager loansManager) {
		this.loansManager = loansManager;
	}

	/**
	 * 通过p2p平台账户 查询用户的第三方资金账户同时跟本地的第三方资金账户进行同步
	 * 
	 * @param id
	 * @param email
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public ThirdPaymentAccount queryCapitalInfo(Account acc) {
		logger.info("start queryCapitalInfo......");
		//通过p2p平台账户检索第三方的资金账户
		logger.info("query p2p captitalInfo");
		ThirdPaymentAccount localResult = capitalRecordRepository.findByAccountSequence(acc.getAccountSequence());
		if(localResult==null){
			return null;
		}
		return localResult;
	}
	
	protected ThirdPaymentAccount synchronizeCapitalAccount(ThirdPaymentAccount localResult) {
		//TODO 将p2p的资金账户得到的第三方支付账户 通过第三方支付接口获得
		logger.info("query third capitalInfo");
		String usrCustId=null;
		String balanceBg;
		usrCustId=localResult.getThirdPaymentUniqueId().toString();
		balanceBg=queryManager.queryBalanceBg(usrCustId);
		//******************************************
		//返回类型：{"CmdId":"QueryBalanceBg","RespCode":"000","RespDesc":"成功","ChkValue":"B349139352D0009B1F92A4DBB836E89E8301F646AD3FB128F26F46D1DDFA2A8CE6E010261ACE78656E596103D39F069B205E2C7AB391ADBCB4A1F028116B1F98115D62D80C3CF68F254B01EE3787758A15B28E5D6C906226179522B529FC53BEB071EE612C72CD2F7A97DFFB5687E7A149BE9902AA762ED5A66DB671DD2A67C6","Version":null,"AvlBal":"200.00","AcctBal":"200.00","FrzBal":"0.00","MerCustId":"6000060000622535","UsrCustId":"6000060000754517","PlainStr":"QueryBalanceBg00060000600006225356000060000754517200.00200.000.00"}
		logger.info("资金账号信息：####"+balanceBg+"###");
		ThirdPayUserResponseDto dto = null;
		try {
			dto = ThirdPayUserResponseDto.formateJson(balanceBg);
		} catch (IOException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		if(dto==null||!ThirdChannelEnum.RESPCODEVALUE.getCode().equals(dto.getRespCode())){
			return localResult;
		}
		//******************************************
		//同步第三方的支付账户,如不一致将本地进行更新
		logger.info("synchronized p2p and third capitalInfo");
		if(balanceBg!=null&&!"".equals(balanceBg.trim())){
			//资金不同，则同步
			String acctBal = NumberUtil.removeComma(dto.getAcctBal());
			String avlBal = NumberUtil.removeComma(dto.getAvlBal());
			BigDecimal acctBalCapital = new BigDecimal(acctBal);
			BigDecimal avlBalCapital = new BigDecimal(avlBal);
			if (localResult.getThirdPaymentIdBalance().compareTo(acctBalCapital)!=0) {
				localResult.setThirdPaymentIdBalance(acctBalCapital);
				localResult.setThirdPaymentValidBalance(avlBalCapital);//可用余额
				localResult.setRecordOperateDate(new Date());//操作时间
				capitalRecordRepository.save(localResult);
			}
		}
		logger.info("end queryCapitalInfo.");
		return localResult;
	}
	
	/**
	 * 同步商户账户
	 * @param localResult
	 * @return
	 */
	protected void synchronizeMerAccount() {
		//TODO 将p2p的资金账户得到的第三方支付账户 通过第三方支付接口获得
		logger.info("synchronizeMerAccount。。。。。。。。。。。。。");
		String balanceBg;
		balanceBg=queryManager.queryAccts();
		//******************************************
		logger.info("资金账号信息：####"+balanceBg+"###");
		ThirdPayUserResponseDto dto = null;
		try {
			dto = ThirdPayUserResponseDto.formateJson(balanceBg);
		} catch (IOException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		//******************************************
		//同步第三方的支付账户,如不一致将本地进行更新
		if(balanceBg!=null&&!"".equals(balanceBg.trim())){
			for(AcctDetail a:dto.getAcctDetails()){
				ThirdPaymentAccount tmp=capitalRecordRepository.findByThirdPaymentId(a.getSubAcctId());
				String acctBal = NumberUtil.removeComma(a.getAcctBal());
				String avlBal = NumberUtil.removeComma(a.getAvlBal());
				//资金不同，则同步
				if (tmp!=null&&tmp.getThirdPaymentIdBalance().compareTo(new BigDecimal(acctBal))!=0) {
					tmp.setThirdPaymentIdBalance(new BigDecimal(acctBal));
					BigDecimal avlBalCapital = new BigDecimal(avlBal);
					tmp.setThirdPaymentValidBalance(avlBalCapital);//可用余额
					tmp.setRecordOperateDate(new Date());//操作时间
					capitalRecordRepository.save(tmp);
				}
			}
		}
		logger.info("end synchronizeMerAccount。。。。。。。。。。。。.");
	}
	
	public ThirdPaymentAccount queryCapitalInfo(Long aid) {
		//通过p2p平台账户检索第三方的资金账户
		logger.info("query p2p captitalInfo");
		ThirdPaymentAccount localResult = capitalRecordRepository.findByAccountSequence(aid);
		//TODO 将p2p的资金账户得到的第三方支付账户 通过第三方支付接口获得
		logger.info("query third capitalInfo");
		return localResult;
	}
	
	/**
	 * 客户开户：开通贷款/投资客户p2p平台的资金账户之前
	 * 调用三方接口之前组装数据
	 * @param investment
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Map<String,String> regeditCreditCapitalAccountBefore(Long accId,String privParameter) throws Exception {
		Account account=accountRepository.findOne(accId);
		if(account==null){
			return null;
		}
		String usrId = account.getUserName();
		String usrName = "";
		String idType = "";
		String idNo = "";
		String usrEmail = "";
		String merPri = ""+privParameter;
		//根据用户accountRole来判断用户是否是借款人
		String usrMp = account.getMobile();
		if(ConstantsUtil.AccountRole.BORROWER==account.getAccountRole()){//认为是贷款用户
			usrName = account.getRealName();
			idType = "00";
			idNo = account.getPid();
			usrEmail = account.getEmail();
			usrMp = createMobile(usrMp);
		}
		Integer tacRole = account.getAccountRole();
		return urManager.getUserRegisterParam(usrId, usrName, idType, idNo, usrMp, usrEmail,merPri,tacRole);
	}
	
	/**
	 * 根据原始手机号码生成新的手机号码
	 * @param oldMobile
	 * @return
	 */
	private String createMobile(String oldMobile){
		String newMobile=oldMobile.substring(0,6);//取前六位
		//随机后五位
		String next5number=P2pUtil.generateRandom(5);
		newMobile += next5number;
		return newMobile;
	}

	/**
	 * 客户开户：开通p2p平台的资金账户之后
	 * 调用三方接口创建资金账户返回之后回调此方法
	 * @param investment
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public ThirdPaymentAccount openCreditCapitalAccount(Long accId,String thirdAccountName,Long thirdPaymentUniqueId,String idNo,String usrEmail,String usrName,String mobile) throws Exception {
		//查询当前id所对应的的账户名
		Account account = accountRepository.findOne(accId);
		if (account == null) {
			throw new Exception("用户不存在");
		}
		//获取三方支付账号
		ThirdPaymentAccount thirdAccount = capitalRecordRepository.findByAccountSequence(account.getAccountSequence());
		if (thirdAccount == null){
			//如果账户为空则认为贷款客户未开户
			thirdAccount = openInvestAccount(accId,thirdAccountName,thirdPaymentUniqueId);
			//如果是借款人
			if (ConstantsUtil.AccountRole.BORROWER == account.getAccountRole()) {
				//添加真假手机号码对照
				Phones phone=new Phones();
				phone.setRealPhones(account.getMobile());
				phone.setFakePhones(mobile);
				phonesrepository.save(phone);
			}
			
			registerManager.modifyAccount(accId, idNo, thirdAccountName, usrEmail,usrName);//如果当前账户是借款人
		}
		return thirdAccount;
	}

	/**
	 * 开通除贷款用户以外的资金账户，通过web端回调的方式
	 * @param thirdPaymentAccount
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public ThirdPaymentAccount openInvestAccount(Long accountId,String thirdPaymentName,Long thirdPaymentUniqueId)throws Exception{
		//生成资金账户
		ThirdPaymentAccount thirdAccount=new ThirdPaymentAccount();
		thirdAccount.setAccountSequence(accountId);
		thirdAccount.setThirdPaymentId(thirdPaymentName);
		thirdAccount.setThirdPaymentIdActiveStatus(ThirdPaymentAccountActiveStatusEnum.IS_ACTIVE.getCode());
		thirdAccount.setThirdPaymentIdBalance(new BigDecimal(0));//默认可用余额为0
		thirdAccount.setThirdPaymentIdBindStatus(ThirdPaymentAccountBindStatusEnum.IS_NO_BIND.getCode());
		thirdAccount.setThirdPaymentIdRegisterDate(new Date());
		thirdAccount.setThirdPaymentIdType(ThirdPaymentAccountTypeEnum.CUSTOMER_ACCOUNT.getCode());
		thirdAccount.setThirdPaymentUniqueId(thirdPaymentUniqueId);
		thirdAccount.setThirdPaymentValidBalance(new BigDecimal(0));//可用余额
		thirdAccount.setRecordOperateDate(new Date());//操作时间
		thirdAccount = capitalRecordRepository.save(thirdAccount);
		return thirdAccount;
	}
	
}
