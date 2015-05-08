package com.vcredit.jdev.p2p.account.modal;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;

import net.gplatform.sudoor.server.integration.EventMessageGateway;
import net.gplatform.sudoor.server.security.model.auth.SSAuth;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.bizsync.convert.Borrower;
import com.vcredit.jdev.p2p.dto.AlertDto;
import com.vcredit.jdev.p2p.dto.IpAdressDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.repository.AccountRepository;

@Component
public class RegisterManager {
	
	private static Logger logger = Logger.getLogger(RegisterManager.class);
	
	@Autowired
	private EventMessageGateway eventMessageGateway;
	
	@Autowired
	AccountRepository accountRepository;

	@Autowired
	private SSAuth auth;

	@Autowired
	private CommonManager commonManager;
	
	@Autowired
	private PasswordManager passwordManager;
	
	@Autowired
	private DictionaryUtil dictionaryUtil;

	//@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Account addAccount(String userName, int safety, String mobile, String registerIp) throws Exception {
		logger.info("addAccount begin......");
		if (commonManager.isExistUserName(userName)) {
			//此用户名已注册!
			logger.error("此用户名已注册! addAccount-->userName:"+userName+" is exist in table account!");
			throw new Exception("1");
		}
		if (commonManager.isExistMobile(mobile)) {
			//此手机号已注册!
			logger.error("此手机号已注册! addAccount-->mobile:"+mobile+" is exist in table account!");
			throw new Exception("2");
		}
		Account account = new Account();
		account.setUserName(userName);
		account.setMobile(mobile);
		account.setMobileBindStatus(ConstantsUtil.EntityStatus.STATUS_ENABLE);
		account.setSafety(safety);
		account.setAccountRole(ConstantsUtil.AccountRole.INVESTOR);
		account.setRegisterDate(new Date());
		account.setRegisterIp(registerIp);
		IpAdressDto ipdto = P2pUtil.getAddressByIP(registerIp);
		if (ipdto!=null&&!(ConstantsUtil.UNALLOCATED_OR_INTRANET_IP.equals(ipdto.getCountry()))) {
			account.setAccountRegisterIpProvice(commonManager.getAreaIdByAreaName(ipdto.getProvince()));
			account.setAccountRegisterIpCity(commonManager.getAreaIdByAreaName(ipdto.getCity()));
			account.setAccountRegisterIpTown(commonManager.getAreaIdByAreaName(ipdto.getDistrict()));
		}
		//account.setGender(1);
		account.setAccountStatus(0);
		Account savedAccount = accountRepository.saveAndFlush(account);
		logger.info("addAccount end......******** accountId:= "+savedAccount.getAccountSequence());
		System.out.println("---------=="+savedAccount.getAccountSequence());
		return savedAccount;
	}

	
	/** 
	* @Title: register 
	* @Description: 投资人注册平台用户  
	*  @param userName
	*  @param password
	*  @param safety
	*  @param mobile
	*  @param registerIp
	*  @return
	*  @throws Exception
	* @return Account    返回类型 
	* @throws 
	*/
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Account register(String userName, String password, int safety, String mobile, String registerIp) throws Exception {
		logger.info("register begin......********");
		Account account = addAccount(userName, safety, mobile, registerIp);
		String aid = account.getAccountSequence().toString();
		auth.register(aid, password);
		//站内信
		sendAccountRegisterMessage(aid);
		logger.info("register end......******** accountId:= "+account.getAccountSequence());
		return account;
	}
	
	public void sendAccountRegisterMessage(String aid){
		logger.debug("sendAccountRegisterMessage begin..........");
		AlertDto dto = new AlertDto();
		AccountMessageTemplateData accountMessageTemplateData = new AccountMessageTemplateData();
		dto.setAccountMessageTemplateData(accountMessageTemplateData);
		dto.setAccountMessageTemplateEnum(AccountMessageTemplateEnum.ZHU_CE_CHENG_GONG);
		//站内信_用户Id
		dto.setToUser(aid);
		eventMessageGateway.publishEvent(dto);
		logger.debug("sendAccountRegisterMessage end..........");
	}
	

	/** 
	* @Title: registerBorrowerFromVBS 
	* @Description: 借款人注册平台用户 
	*  @param borrower VBS传送借款人实体
	*  @return
	*  @throws Exception
	* @return Account    返回类型 
	* @throws 
	*/
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Account registerBorrowerFromVBS(Borrower borrower) throws Exception{
		String pid = borrower.getIdCard();
		if(StringUtils.isBlank(pid)){
			throw new Exception("身份证号不能为空!");
		}
		
		Account account = new Account();
		
		//用户名 生成规则：字母"LS"年份（YY）(2位)+月份（2位）+五位数（随机数）
		account.setUserName(getBorrowerUserName());
		
		//身份证号
		account.setPid(borrower.getIdCard());
		
		//客户类型 (注：若客户性质为空，传"其他")
		String customerType = borrower.getCustomerType();
		if(StringUtils.isBlank(customerType)){
			customerType ="其他";
		}
		String loanType = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_LTYPE.getCode()+customerType);
		if(StringUtils.isNotBlank(loanType)){
			account.setLoanType(Integer.valueOf(loanType));
		}
		
		//真实姓名
		account.setRealName(borrower.getName());
		
		//性别
		String gender = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_GENDER.getCode()+ borrower.getGender());
		if(StringUtils.isNotBlank(gender)){
			account.setGender(Integer.valueOf(gender));
		}
		
		//学历
		String educationDegree = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_EDU_DEGREE.getCode()+ borrower.getEducationDegree());
		if(StringUtils.isNotBlank(educationDegree)){
			account.setEducationDegree(Integer.valueOf(educationDegree));
		}
		
		//婚姻状况
		String marryStatus = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_MSTAT.getCode()+ borrower.getMarryStatus());
		if(StringUtils.isNotBlank(marryStatus)){
			account.setMarryStatus(Integer.valueOf(marryStatus));
		}
		
		//手机号码
		String mobile = borrower.getMobile();
		if(StringUtils.isNotBlank(mobile)){
			account.setMobile(mobile);
			account.setMobileBindStatus(ConstantsUtil.EntityStatus.STATUS_ENABLE);
		}
		
		//邮箱
		String email = borrower.getEmail();
		if(StringUtils.isNotBlank(email)){
			account.setEmail(borrower.getEmail());
			account.setEmailBindStatus(ConstantsUtil.EntityStatus.STATUS_ENABLE);
		}
		
		//户籍地 hometown  XX省 / XX直辖市
		String hometown = borrower.getHometown();
		if(StringUtils.isNotBlank(hometown)){
			account.setHometownProvince(commonManager.getAreaIdByAreaName(hometown));
		}
		
		//工作城市 workCity XX省XX市 / XX直辖市
		String workAddress = borrower.getWorkCity();
		if(StringUtils.isNotBlank(workAddress)){
			String workProvince = null;
			String workCity = null;
			//XX省(自治区)/XX市
			if(workAddress.contains("省")||workAddress.contains("自治区")){
				int pindex = workAddress.indexOf("省");
				int arindex = workAddress.indexOf("自治区");
				int cityindex = workAddress.indexOf("市");
				if(pindex>-1){
					workProvince = workAddress.substring(0, pindex+1);
					System.out.println(workProvince);
					if(cityindex>-1){
						workCity = workAddress.substring(pindex+1);
						System.out.println(workCity);
					}
				}else if(arindex>-1){
					workProvince = workAddress.substring(0, arindex+3);
					System.out.println(workProvince);
					if(cityindex>-1){
						workCity = workAddress.substring(pindex+1);
						System.out.println(workCity);
					}
				}
				
			}else{//XX直辖市
				workProvince = workAddress;
			}
			//工作所在省份
			if(StringUtils.isNotBlank(workProvince)){
				account.setWorkProvince(commonManager.getAreaIdByAreaName(workProvince));
			}
			//工作所在地级市
			if(StringUtils.isNotBlank(workCity)){
				account.setWorkCity(commonManager.getAreaIdByAreaName(workCity));
			}
		}
		
		//公司行业
		String companyInustry = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_CINDS.getCode()+ borrower.getCompanyInustry());
		if(StringUtils.isNotBlank(companyInustry)){
			account.setCompanyInustry(Integer.valueOf(companyInustry));
		}
		
		//公司规模
		String companySize = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_CSIZE.getCode()+ borrower.getCompanySize());
		if(StringUtils.isNotBlank(companySize)){
			account.setCompanySize(Integer.valueOf(companySize));
		}
		
		//工作年限
		String workTime = borrower.getWorkTime();
		if(StringUtils.isNotBlank(workTime)){
			account.setWorkTime(Integer.valueOf(workTime));
		}
		//任职部门
		account.setDepartment(borrower.getDepartment());
		//职位
		account.setPosition(borrower.getAccountIndustry());
		
		//企业性质
		String companyProperty = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_CPT.getCode()+ borrower.getCompanyProperty());
		if(StringUtils.isNotBlank(companyProperty)){
			account.setCompanyProperty(Integer.valueOf(companyProperty));;
		}
		
		//工资收入（注：若为空，传送值为0）
		String incomestr =  borrower.getIncome();
		if(StringUtils.isBlank(incomestr)){
			account.setIncome(0);
		}else{
			String income = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_INCOME.getCode()+incomestr);
			if(StringUtils.isNotBlank(income)){
				account.setIncome(Integer.valueOf(income));
			}
		}
		
		//每月授薪日
		account.setSalaryDate(borrower.getSalaryDate());
		//住宅状况
		//houseProperty
		
		//--------------
		account.setSafety(1);
		account.setAccountStatus(ConstantsUtil.EntityStatus.STATUS_ENABLE);
		account.setRegisterDate(new Date());
		account.setAccountRole(ConstantsUtil.AccountRole.BORROWER);
		account.setRegisterIp("VBS");
		return accountRepository.save(account);
	}
	
	/** 
	* @Title: registerBorrowerFromVBS 
	* @Description: 通过VBS推送的LoanData,借款人注册平台用户 
	*  @param loanData VBS推送的借款人信息
	*  @return
	*  @throws Exception
	* @return Account    返回类型 
	* @throws 
	*/
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Account registerBorrowerFromVBS(LoanData loanData) throws Exception{
		
		String pid = loanData.getPid();
		if(StringUtils.isBlank(pid)){
			logger.error("registerBorrowerFromVBS-->this pid is exist!");
			throw new Exception("身份证号不能为空! registerBorrowerFromVBS-->this pid is exist!");
		}
		Account account = accountRepository.findByPid(pid);
		Account mobileAccount = null; 
		String mobile = loanData.getMobile();
		if(StringUtils.isNotBlank(mobile)){
			mobileAccount = accountRepository.findByMobile(mobile);
		}
		if(account == null){
			//此身份证号没有注册过,创建新用户;当身份证号已注册过,修改原用户信息
			account = new Account();
			
			//--------------
			account.setSafety(1);
			account.setAccountStatus(ConstantsUtil.EntityStatus.STATUS_ENABLE);
			account.setRegisterDate(new Date());
			account.setAccountRole(ConstantsUtil.AccountRole.BORROWER);
			account.setRegisterIp("VBS");
			//手机号码
			if(mobileAccount == null&&(StringUtils.isNotBlank(mobile))){
				account.setMobile(mobile);
				account.setMobileBindStatus(ConstantsUtil.EntityStatus.STATUS_ENABLE);
			}else{
				logger.error("registerBorrowerFromVBS-->this mobile is exist!");
				throw new Exception("手机已被注册过!registerBorrowerFromVBS-->this mobile is exist!");
			}
			//用户名 生成规则：字母"LS"年份（YY）(2位)+月份（2位）+五位数（随机数）
			account.setUserName(getBorrowerUserName());
			//身份证号
			account.setPid(pid);
		}else{
			//手机号码
			if(mobileAccount == null&&(StringUtils.isNotBlank(mobile))){
				account.setMobile(mobile);
				account.setMobileBindStatus(ConstantsUtil.EntityStatus.STATUS_ENABLE);
			}else{
				if(!(account.getAccountSequence().equals(mobileAccount.getAccountSequence().longValue()))){
					logger.error("registerBorrowerFromVBS-->this mobile is exist!");
					throw new Exception("手机已被注册过!registerBorrowerFromVBS-->this mobile is exist!");
				}
			}
		}
		
	
		//客户类型 (注：若客户性质为空，传"其他")
		String loanType = loanData.getLoanType();
		if(StringUtils.isBlank(loanType)){
			loanType ="其他";
			loanType = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_LTYPE.getCode()+loanType);
		}
		
		if(StringUtils.isNotBlank(loanType)){
			account.setLoanType(Integer.valueOf(loanType));
		}
		
		//真实姓名
		account.setRealName(loanData.getRealName());
		
		//性别
		String gender = loanData.getGender();
		if(StringUtils.isNotBlank(gender)){
			account.setGender(Integer.valueOf(gender));
		}
		
		//学历
		String educationDegree = loanData.getEducationDegree();
		if(StringUtils.isNotBlank(educationDegree)){
			account.setEducationDegree(Integer.valueOf(educationDegree));
		}
		
		//婚姻状况
		String marryStatus = loanData.getMarryStatus();
		if(StringUtils.isNotBlank(marryStatus)){
			account.setMarryStatus(Integer.valueOf(marryStatus));
		}
		
	
		
		//邮箱
		String email = loanData.getEmail();
		if(StringUtils.isNotBlank(email)){
			account.setEmail(loanData.getEmail());
			account.setEmailBindStatus(ConstantsUtil.EntityStatus.STATUS_ENABLE);
		}
		
		//户籍地 hometown  XX省 / XX直辖市
		String homeTownProvince = loanData.getHometownProvince();
		if(StringUtils.isNotBlank(homeTownProvince)){
			account.setHometownProvince(Integer.valueOf(homeTownProvince));
		}
		
		String homeTownCity = loanData.getHometownCity();
		if(StringUtils.isNotBlank(homeTownCity)){
			account.setHometownCity(Integer.valueOf(homeTownCity));
		}
		
		String homeTownTown = loanData.getHometownTown();
		if(StringUtils.isNotBlank(homeTownTown)){
			account.setHometownTown(Integer.valueOf(homeTownTown));
		}
		
		//工作城市 workCity
		String workProvince = loanData.getWorkProvince();
		if(StringUtils.isNotBlank(workProvince)){
			account.setWorkProvince(Integer.valueOf(workProvince));
		}
		
		
		String workCity = loanData.getWorkCity();
		if(StringUtils.isNotBlank(workCity)){
			account.setWorkCity(Integer.valueOf(workCity));
		}
		
		String workTown = loanData.getWorkTown();
		if(StringUtils.isNotBlank(workTown)){
			account.setWorkCity(Integer.valueOf(workTown));
		}
		
		
		//公司行业
		String companyInustry = loanData.getCompanyInustry();
		if(StringUtils.isNotBlank(companyInustry)){
			account.setCompanyInustry(Integer.valueOf(companyInustry));
		}
		
		//公司规模
		String companySize =loanData.getCompanySize();
		if(StringUtils.isNotBlank(companySize)){
			account.setCompanySize(Integer.valueOf(companySize));
		}
		
		//工作年限
		account.setWorkTime(loanData.getWorkTime());
		//任职部门
		account.setDepartment(loanData.getDepartment());
		//职位
		account.setPosition(loanData.getAccountIndustry());
		
		//企业性质
		String companyProperty = loanData.getCompanyProperty();
		if(StringUtils.isNotBlank(companyProperty)){
			account.setCompanyProperty(Integer.valueOf(companyProperty));;
		}
		
		//工资收入（注：若为空，传送值为0）
		String income =  loanData.getIncome();
		if(StringUtils.isBlank(income)){
			account.setIncome(0);
		}else{
			account.setIncome(Integer.valueOf(income));
		}
		
		//住宅状况
		//houseProperty
		String houseProperty = loanData.getHouseProperty();
		if(StringUtils.isNotBlank(houseProperty)){
			account.setHouseProperty(Integer.valueOf(houseProperty));
		}
		
		//每月授薪日
		account.setSalaryDate(loanData.getSalaryDate());
		
		//信用评分
		Integer credibilityScore = loanData.getCredibilityScore();
		account.setCredibilityScore(credibilityScore);
		
		//信用等级
		String credibilityLevel = loanData.getCredibilityLevel();
		if(StringUtils.isNotBlank(credibilityLevel)){
			account.setCredibilityLevel(Integer.valueOf(credibilityLevel));
		}
		
		//房贷情况
		String accountHouseLoan=loanData.getAccountHouseLoan();
		if(StringUtils.isNotBlank(accountHouseLoan)){
			account.setAccountHouseLoan(Integer.valueOf(accountHouseLoan));
		}
	
		return accountRepository.save(account);
	}
	
	/**
	 * @throws UnsupportedEncodingException  
	* @Title: modifyAccount 
	* @Description: 投资人注册汇付后完善在P2P平台个人信息 
	* @param aid
	* @param IdNo
	* @param usrMp
	* @param usrEamil
	* @param usrName
	* @param @return    设定文件 
	* @return Account    返回类型 
	* @throws 
	*/
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Account modifyAccount(Long aid,String IdNo,String usrId,String usrEamil,String usrName) throws UnsupportedEncodingException{
		Account account = accountRepository.findOne(aid);
		//身份证号码
		if(StringUtils.isNotBlank(IdNo)){
			account.setPid(IdNo);
			
			//通过身份证号码判断出生年月日与性别
			//出生年月日
			String accountBirthday = IdNo.substring(6, 14);
			account.setAccountBirthday(accountBirthday);
			
			//性别
			int gender = Integer.valueOf(IdNo.substring(16, 17));
			if((gender&1)!=0){//奇数 为男性
				account.setGender(1);
			}else{//偶 数为女性
				account.setGender(0);
			}
			
		}
		//真实姓名
		if(StringUtils.isNotBlank(usrName)){
			logger.debug("************投资人注册汇付后完善在P2P平台个人信息  callback modifyAccount update realname:"+usrName);
			usrName = URLDecoder.decode(usrName, "UTF-8");
			logger.debug("**************投资人注册汇付后完善在P2P平台个人信息  callback modifyAccount update URLDecoder.decode realname:"+usrName);
			account.setRealName(usrName);
		}
		account.setAccountThirdPaymentId(usrId);
		account.setAccountThirdPaymentIdBindStatus(ConstantsUtil.EntityStatus.STATUS_ENABLE);
		//当在account中email为null，更新email,并设置email的绑定状态为未绑定
		if(StringUtils.isBlank(account.getEmail())&&StringUtils.isNotBlank(usrEamil)){
			account.setEmail(URLDecoder.decode(usrEamil, "UTF-8"));
			account.setEmailBindStatus(ConstantsUtil.EntityStatus.STATUS_DISABLE);
		}
		
		//修改安全状态
		Integer safety = account.getSafety();
		if(safety != null){
			if(safety == 1){
				account.setSafety(2);
			}else if((account.getSafety() == 2)&&(passwordManager.checkAccountIsSetQuestionByAid(aid))){
				account.setSafety(3);
			}
		}else{
			account.setSafety(1);
		}
		return accountRepository.save(account);
	}
	
	/** 
	* @Title: getBorrowerUserName 
	* @Description: 生成借款人用户名
	*  @return
	* @return String    返回类型 
	* @throws 
	*/
	private String getBorrowerUserName(){
		//生成规则：字母"LS"年份（YY）(2位)+月份（2位）+五位数（随机数）
		Calendar c = Calendar.getInstance();
		String year = (c.get(Calendar.YEAR)+"").substring(2, 4);
		int month = c.get(Calendar.MONTH)+1;
		String username = ConstantsUtil.BORROWER_UNAME_PREFIX+year+(month<10?"0"+month:month+"")+P2pUtil.generateRandom(5);
		return username;
	}
}
