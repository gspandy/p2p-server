package com.vcredit.jdev.p2p.merchant.modal;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.base.util.PropertiesUtils;
import com.vcredit.jdev.p2p.capital.modal.CapitalEngine;
import com.vcredit.jdev.p2p.chinapnr.deal.BidInfoManager;
import com.vcredit.jdev.p2p.deal.service.BondPackageInvestManager;
import com.vcredit.jdev.p2p.deal.service.DealServiceReferenceManager;
import com.vcredit.jdev.p2p.dto.LoanDataDto;
import com.vcredit.jdev.p2p.dto.LoanDataDtoList;
import com.vcredit.jdev.p2p.dto.ThirdPayUserResponseDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.ClaimPayPlanStatusEnum;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.InvestmentTypeEnum;
import com.vcredit.jdev.p2p.enums.LoanDataRecordStatusEnum;
import com.vcredit.jdev.p2p.enums.LoanDataStatusEnum;
import com.vcredit.jdev.p2p.enums.ThirdChannelEnum;
import com.vcredit.jdev.p2p.enums.ThirdPayRetTypeEnum;
import com.vcredit.jdev.p2p.enums.ThirdPaymentAccountTypeEnum;
import com.vcredit.jdev.p2p.quratz.TaskJobFactory;
import com.vcredit.jdev.p2p.repository.AccountClaimRepository;
import com.vcredit.jdev.p2p.repository.AccountInvestmentRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.CapitalRecordRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanRepository;
import com.vcredit.jdev.p2p.repository.DictionaryRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;
import com.vcredit.jdev.p2p.util.CollectionUtil;
import com.vcredit.jdev.p2p.util.DateUtil;
import com.vcredit.jdev.p2p.util.FinanceUtil;

/**
 * 商品管理 所有涉及到的商品：投资项目/红包福利/服务等等.
 * 
 * @author 周佩 创建时间 20141212
 */
@Component
@MessageEndpoint
public class MerchantManager {
	Logger logger = LoggerFactory.getLogger(MerchantManager.class);
	//默认投资项目失效时间
	private static int FRIDAY = 6;
	private static String DEFAULTNAME = "V";
	@Autowired
	private InvestmentRepository investmentRepository;
	@Autowired
	private EventMessageGateway eventMessageGateway;
	@Autowired
	private DictionaryRepository dictionaryRepository;
	@Autowired
	private LoanDataRepository loanDataRepository;
	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private DictionaryUtil dictionaryUtil;
	@Autowired
	private AccountOrderRepository accountOrderRepository;
	@Autowired
	private AccountInvestmentRepository accountInvestmentRepository;
	@Autowired
	private AccountClaimRepository accountClaimRepository;
	@Autowired
	private ClaimPayPlanRepository claimPayPlanRepository;
	@Autowired
	private BidInfoManager bidInfoManager;
	@Autowired
	private CapitalRecordRepository capitalRecordRepository;
	@Autowired
	private PropertiesUtils propertiesUtils;
	@Autowired
	private BondPackageInvestManager bondPackageInvestManager;
	@Autowired
	private TaskJobFactory taskJobFactory;
	@Autowired
	private P2pSessionContext p2pSessionContext;
	@Autowired
	private DealServiceReferenceManager dealServiceReferenceManager;

	@ServiceActivator(inputChannel = "asyncEventPublishChannel")
	public void handle(Object event) throws Exception {
		if (event instanceof LoanDataDtoList) {
			LoanDataDtoList list = (LoanDataDtoList) event;
			List<LoanDataDto> loanDataDtos = list.getLoanDataDtos();
			for(LoanDataDto dto:loanDataDtos){
				List<LoanData> loanList=new ArrayList<LoanData>();
				loanList.add(dto.getLoanData());
				String str=signleProjectByLoanDataBatch(loanList);
				logger.info("merchantManager handle:"+str);
			}
		}
	}

	/**
	 * 项目id生成规则 字母“V”+当前年份（YY）+字母（A~Z）+3位字符。 其中后三位字符每位均从0、1…9、A…Z依次计数 例：
	 * 今年为2014年，则本年第1笔投资项目ID为V14A000、 第2笔投资项目ID为V14A001、第11笔投资项目ID为V14A00A、
	 * 第36笔投资项目ID为V14A00Z、第37笔投资项目ID为V14A010, 第38笔投资项目ID为V14A011…依次类推
	 * 
	 * @return
	 */
	public String createProjectId() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
		Calendar c = Calendar.getInstance();
		String year = sdf.format(c.getTime()).substring(2);
		String month = sdf1.format(c.getTime());
		String previous = DEFAULTNAME + year + month;

		//当月最大值
		long maxLength = investmentRepository.findByinvestmentNumberLike(previous + "%").size();
		long num = 00000;
		num += maxLength;
		String paritcal = String.format("%05d", num);
		String defaultName = previous + paritcal;
		//计算10进制
		return defaultName;
	}

	//	/**
	//	 * 项目发布导入 激活时：将当前投资项目的id修改
	//	 * 
	//	 * @param invest
	//	 *            投资项目
	//	 * @return
	//	 * @throws Exception
	//	 */
	//
	//	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	//	public boolean activateInvestProject(Investment invest) throws Exception {
	//		boolean returnVal = false;
	//		//生成项目Id
	//		Investment investment = investmentRepository.findOne(invest.getInvestmentSequence());
	//		if (investment == null) {
	//			return false;
	//		}
	//		String investmentNumber = investment.getInvestmentNumber();
	//		if (investmentNumber.startsWith(DEFAULTNAME)) {
	//			return false;
	//		}
	//		investment.setInvestmentNumber(createProjectId());
	//		investmentRepository.save(investment);
	//		return returnVal;
	//	}

	/**
	 * 自动生成投资项目
	 * 
	 * @param LoanDataList
	 *            至少一个 投资项目
	 * @return
	 * @throws Exception
	 */

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public String autoComponentProject(int size) throws Exception {
		if (size == 0) {
			size = 10;//默认10
		}
		StringBuilder res = new StringBuilder();
		List<LoanData> loanDataList = loanDataRepository.findByProgressStauts(LoanDataStatusEnum.COMPLETE_P2P_ACCOUNT_CREATE.getCode(),
				new PageRequest(0, size));
		for (LoanData s : loanDataList) {
			System.out.println("loanData:" + s.getLoanDataSequence());
			List<LoanData> i = new ArrayList<LoanData>();
			i.add(s);
			if (!componentProject(i)) {
				res.append("id:" + s.getLoanDataSequence() + "\n");
			}
		}
		System.out.println("loanDataList记录数:" + loanDataList.size());
		return res.toString();
	}

	/**
	 * 生成单笔投资项目
	 * 
	 * @param LoanDataList
	 *            至少一个 投资项目
	 * @return
	 * @throws Exception
	 */

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public String signleProjectById(List<Long> loanDatas) throws Exception {
		String result = "";
		for (Long id : loanDatas) {
			List<LoanData> i = new ArrayList<LoanData>();
			LoanData s = loanDataRepository.findOne(id);
			i.add(s);
			result += signleProjectByLoanDataBatch(i);
		}
		return result;
	}

	/**
	 * 生成单笔投资项目
	 * 
	 * @param LoanDataList
	 *            至少一个 投资项目
	 * @return
	 * @throws Exception
	 */

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public String signleProject(Long loanDatas) throws Exception {
		List<LoanData> i = new ArrayList<LoanData>();
		LoanData s = loanDataRepository.findOne(loanDatas);
		i.add(s);
		return signleProjectByLoanDataBatch(i);
	}

	/**
	 * 生成单笔投资项目
	 * 
	 * @param LoanDataList
	 *            至少一个 投资项目
	 * @return
	 * @throws Exception
	 */

	public String signleProjectByLoanDataBatch(List<LoanData> loanDatas) throws Exception {
		StringBuilder res = new StringBuilder();
		for (LoanData s : loanDatas) {
			try{
				List<LoanData> i = new ArrayList<LoanData>();
				i.add(s);
				if (!componentProject(i)) {
					res.append("id:" + s.getLoanDataSequence() + "\n");
				} else {
					res.append("id:" + s.getLoanDataSequence() + " is error\n");
				}
			}catch(Exception e){
				logger.error("create investment is error:",e);
				//更新loanData的状态
				s.setProgressStauts(LoanDataStatusEnum.COMPLETE_P2P_ACCOUNT_CREATE.getCode()); //退回至上一成功状态
				s.setRecordStatus(LoanDataRecordStatusEnum.ERROR.getCode());
				s.setSynchronizeDescription(LoanDataStatusEnum.COMPLETE_INVESTMENT_CREATE.getCode()); //处理生成项目入库失败
				loanDataRepository.save(s);
			}
		}
		return res.toString();
	}

	/**
	 * 后台人员创建投资项目，同时跟loanData数据进行关联,组合标的，
	 * 
	 * @param LoanDataList
	 *            至少一个 投资项目
	 * @return
	 * @throws Exception
	 */

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	private boolean componentProject(List<LoanData> loanDataList) throws Exception {
		boolean result = true;
		if (loanDataList == null || loanDataList.size() == 0) {
			return false;
		}
		List<InvestmentAccountReference> fer = new ArrayList<InvestmentAccountReference>();
		Investment invest = new Investment();
		BigDecimal amount = new BigDecimal(0);//总金额
		for (LoanData o : loanDataList) {
			List<InvestmentAccountReference> ref = investmentAccountReferenceRepository.findByLoanDataSequence(o.getLoanDataSequence());
			if (ref != null) {//存在且项目是未流标的，那么返回，否则当做新项目重新生成
				for (InvestmentAccountReference i : ref) {
					Long oldInvestmentId = i.getInvestmentSequence();
					Investment old = investmentRepository.findByInvestmentSequence(oldInvestmentId);
					if (!old.getInvestmentStatus().equals(InvestmentStatusEnum.TENDER_FAIL.getCode())) {//存在未流标的返回
						return false;
					}else{
						if(!InvestmentTypeEnum.LASTBID2ONLINE.getCode().equals(old.getInvestmentType())){//不存在流标再发布状态的项目则将项目修改为流标再发布
							//modify20150401	将原本老的项目的类型修改为流标再发布的老项目
							old.setInvestmentType(InvestmentTypeEnum.LASTBID2ONLINE.getCode());
							//modify20150416	将新建项目的类型为改为流标再发布的新项目
							invest.setInvestmentType(InvestmentTypeEnum.NEWONLINE.getCode());
							investmentRepository.save(old);
						}
					}
				}
			}
			InvestmentAccountReference iar = new InvestmentAccountReference();
			iar.setLoanDataSequence(o.getLoanDataSequence());
			//获得loanData原始数据
			if (invest.getInvestmentSequence() == null) {//初始化投资项目
				saveInvestmentByLoanData(invest, o);
			}
			amount = amount.add(o.getAccountLoanAmount());//计算总金额
			//根据身份证获得账号id
			Account acc = accountRepository.findByPid(o.getPid());
			if (acc == null) {//账号为空则表示当前用户还没创建p2p账号不给创建投资项目
				logger.info("the account is null!");
				throw new Exception("the account is null!");
			} else {
				iar.setAccountSequence(acc.getAccountSequence());
			}
			iar.setInvestmentSequence(invest.getInvestmentSequence());
			fer.add(iar);
			//更新loanData的状态
			o.setProgressStauts(LoanDataStatusEnum.COMPLETE_INVESTMENT_CREATE.getCode());
			o.setRecordOperateDate(new Date());
			loanDataRepository.save(o);
		}
		if (fer.size() > 0) {
			investmentAccountReferenceRepository.save(fer);
			invest.setInvestmentSurplus(amount);
			invest.setInvestmentTotal(amount);
			investmentRepository.save(invest);
		}
		if (invest.getInvestmentSequence() != null) {
			//生成债券还款计划
			for (LoanData o : loanDataList) {
				createClaimPayPlan(
						invest.getInvestmentSequence(),
						Integer.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_PERIOD.getCode()
								+ invest.getInvestmentPeriod())),o);
			}
		}
		return result;
	}

	/**
	 * 生成债权还款计划。
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void createClaimPayPlan(Long investmentSequence, Integer investmentPeriod,LoanData l) throws Exception {
		List<InvestmentAccountReference> investmentAccountReferenceList = investmentAccountReferenceRepository
				.findByInvestmentSequence(investmentSequence);

		if (CollectionUtil.isEmpty(investmentAccountReferenceList)) {
			//贷款人不能为空
			throw new Exception("investmentAccountReferenceList is null");
		}

		// 生成债权还款计划
		Date baseDate = new Date();
		double serviceRate = new Double(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.T_DIC_P2P_PAY_FRATE.getCode()
				+ 1));//p2p平台管理费率
		Investment investment=investmentRepository.findOne(investmentSequence);
		//投资项目的项目等级对应着风险备用金的费率
		//modify 20150312	因为风险备用金汇率不用了，所以风险备用金汇率的计算方式是用loanData中的p2pEarlyPayFeeRate(平台管理费率)-p2p平台管理费率(固定为0.002)
		//小贷公司平台管理费率=p2p平台管理费率(固定为0.002)+风险备用金费率
		//所以数据字典的风险备用金费率不能用了
		//小贷公司平台管理费率(动态变化)=p2p平台管理费率(固定为0.002)+风险备用金费率
		BigDecimal risk = l.getP2pPayFeeRate().subtract(new BigDecimal(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_DIC.getCode() + DictionaryEnum.P2P_PAY_FRATE.getCode() + 1)));
		double riskRate = risk.doubleValue();//p2p风险备用金费率
		for (InvestmentAccountReference investmentAccountReference : investmentAccountReferenceList) {
			//贷款用户流水号
			Long accountSequence = investmentAccountReference.getAccountSequence();
			LoanData loanData = loanDataRepository.findOne(investmentAccountReference.getLoanDataSequence());
			BigDecimal loanAmount = loanData.getAccountLoanAmount();//贷款金额
			String yearRate = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_IRATE.getCode() + loanData.getInvestmentAnnualInterestRate());

			int i = 1;
			//计算用户的还款计划
			List<ClaimPayPlan> list = CapitalEngine.capitalMatchingService(loanAmount, investmentPeriod, new BigDecimal(yearRate));
			for (ClaimPayPlan claimPayPlan : list) {
				//平台管理费
				BigDecimal claimPayPlanPlatformManagementFee = BigDecimal.valueOf(FinanceUtil.calcO2P_PlatformManagementFee(loanAmount.doubleValue(),
						serviceRate)).setScale(2,BigDecimal.ROUND_HALF_UP);
				//风险备用金
				BigDecimal claimPayPlanRiskFee = BigDecimal.valueOf(FinanceUtil.calcO2P_RiskReserveFund(loanAmount.doubleValue(), riskRate));
				//提前清贷补贴
				BigDecimal claimPayPlanSubsidy = BigDecimal.ZERO;

				claimPayPlan.setClaimPayPlanCreateDate(new Date());
				claimPayPlan.setClaimPayPlanDelayDays(0);
				claimPayPlan.setClaimPayPlanJusticeInterest(BigDecimal.ZERO);// 应还罚息
//				claimPayPlan.setClaimPayPlanNatureDate(DateUtil.addMonth(baseDate, i));// 账单日
				claimPayPlan.setClaimPayPlanNumber(i);
				claimPayPlan.setClaimPayPlanPeriod(DateUtil.format(DateUtil.addMonth(baseDate, i), DateUtil.YYYYMMDD));
				claimPayPlan.setClaimPayPlanPlatformManagementFee(claimPayPlanPlatformManagementFee);//平台账号管理服务费
				claimPayPlan.setClaimPayPlanRiskFee(claimPayPlanRiskFee);//风险备用金
				claimPayPlan.setClaimPayPlanStatus(ClaimPayPlanStatusEnum.NOT_PAID.getCode());//还款计划状态
				claimPayPlan.setClaimPayPlanSubsidy(claimPayPlanSubsidy);// 提前清贷补贴
				claimPayPlan.setClaimPayPlanUnkownDays(0);//未决天数
				claimPayPlan.setInvestmentSequence(investmentSequence);
				claimPayPlan.setPayAccountSequence(accountSequence);//贷款用户P2P平台账号流水号	
				ThirdPaymentAccount tpa = capitalRecordRepository.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_RISK_ACCOUNT.getCode());
				claimPayPlan.setPayRiskAccountSequence(tpa.getAccountSequence());//vmoney-风险备用金p2p平台账号	
				ThirdPaymentAccount tpa2 = capitalRecordRepository
						.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_CAPITAL_ACCOUNT.getCode());
				claimPayPlan.setPayServiceAccountSequence(tpa2.getAccountSequence());//vmoney-还款服务p2p平台账号	
				claimPayPlan.setRecordCreateDate(new Date());
				claimPayPlanRepository.save(claimPayPlan);

				i++;
			}
		}
	}

	/**
	 * 将loanData的数据转化为投资项目Investment
	 * 
	 * @param loan
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	private void saveInvestmentByLoanData(Investment inv, LoanData loan) {
		String projectId = createProjectId();//项目编号
		inv.setInvestmentNumber(projectId);
		//		String guaranteedInterestType = dictionaryUtil.getDicValue(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_STYPE.getCode()
		//				+ loan.getInvestmentGuaranteedInterestType());
		String guaranteedInterestType = loan.getInvestmentGuaranteedInterestType();
		if (guaranteedInterestType != null && !"".trim().equals(guaranteedInterestType)) {
			inv.setInvestmentGuaranteedInterestType(Integer.valueOf(guaranteedInterestType));
		} else {
			inv.setInvestmentGuaranteedInterestType(1);//默认值
		}
		//		String investmentAnnualInterestRate = dictionaryUtil.getDicValue(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_IRATE.getCode()
		//				+ loan.getInvestmentAnnualInterestRate());
		String investmentAnnualInterestRate = loan.getInvestmentAnnualInterestRate();
		inv.setInvestmentAnnualInterestRate(Integer.valueOf(investmentAnnualInterestRate));
		inv.setInvestmentCreateDate(new Date());
		inv.setInvestmentCredibilityScore(Integer.valueOf(loan.getCredibilityLevel()));//loanData没有,modify20150327现在有了
		inv.setInvestmentEndDate(null);
		inv.setInvestmentFillDate(null);
		inv.setInvestmentGredibilityImage(0);//loanData数据库缺少
		inv.setInvestmentGuarantee(0);//保障机构loanData缺少
		inv.setInvestmentGuaranteedInterestDescriptionImage(0);//loanData数据库缺少
		String invTarget = loan.getInvestmentTarget();
		if (invTarget != null && !"".trim().equals(invTarget)) {
			inv.setInvestmentTarget(Integer.valueOf(invTarget));
			//贷款目的+项目编号:贷款目的：loanData中inv_target字段，表中的=projectId;
			String investpreiod = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_TARGET.getCode()
					+ invTarget);
			inv.setInvestmentName(investpreiod + projectId);
		} else {
			inv.setInvestmentTarget(1);
			String invTargetTemp = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_TARGET.getCode() + 1);
			inv.setInvestmentName(invTargetTemp + projectId);
		}
		inv.setInvestmentJoinedCount(0);
		inv.setInvestmentLostDate(null);
		inv.setInvestmentNumber(projectId);
		inv.setInvestmentOverDate(null);
		inv.setInvestmentPayType(1);//loanData数据库缺少
		//		String Period = dictionaryUtil.getDicValue(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_PERIOD.getCode()
		//				+ loan.getInvestmentPeriod());
		String Period = loan.getInvestmentPeriod().toString();
		if (Period != null && !"".trim().equals(Period)) {
			inv.setInvestmentPeriod(Integer.valueOf(Period));//翻译
		}
		inv.setInvestmentProgress(new BigDecimal(0));
		inv.setInvestmentStartAmount(new BigDecimal(50));//loanData数据库缺少
		inv.setInvestmentStartDate(null);
		inv.setInvestmentStatus(InvestmentStatusEnum.ACTIVE_WAIT.getCode());
		inv.setInvestmentSurplus(loan.getAccountLoanAmount());
		inv.setInvestmentTotal(loan.getAccountLoanAmount());
		if(inv.getInvestmentType()==null){
			inv.setInvestmentType(InvestmentTypeEnum.AUTO.getCode());
		}
		inv.setInvestmentGuaranteeType(1);//保障方式不能为空默认为1
		inv.setResourceSequence(0);
		inv.setInvestmentPayCount(0);
		//增加项目等级,不会为空,为空就是有问题
		inv.setInvestmentLevel(loan.getCredibilityLevel()==null?-1:Integer.valueOf(loan.getCredibilityLevel()));
		inv.setLoanCompanyPushType(loan.getLoanCompanyPushType());//新增小贷公司推送类型
		investmentRepository.save(inv);
	}

	/**
	 * 格式化投资项目的状态
	 * 
	 * @param status
	 * @return
	 */
	private List<Integer> formateStatus(List<Integer> status) {
		//格式化投标项目状态顺序招标中>已满标>还款中>已流标，12>6,13,14>15,19,20,21(自动提现成功(用户提现默认贷前交易成功，进入到了还款中))>5,16
		List<Integer> res = new ArrayList<Integer>();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Integer i : status) {
			map.put(i, i);
		}
		Integer online = map.get(InvestmentStatusEnum.ON_LINE.getCode());
		if (online != null) {
			res.add(online);
		}
		Integer tenderfinish = map.get(InvestmentStatusEnum.TENDER_FINISH.getCode());
		if (tenderfinish != null) {
			res.add(tenderfinish);
		}
		Integer releaseCash = map.get(InvestmentStatusEnum.RELEASE_CASH.getCode());
		if (releaseCash != null) {
			res.add(releaseCash);
		}
		Integer releaseCashFaliue = map.get(InvestmentStatusEnum.RELEASE_CASH_FALIUE.getCode());
		if (releaseCashFaliue != null) {
			res.add(releaseCashFaliue);
		}
		Integer releaseCashSuccess = map.get(InvestmentStatusEnum.RELEASE_CASH_SUCCESS.getCode());
		if (releaseCashSuccess != null) {
			res.add(releaseCashSuccess);
		}
		Integer autoWithdrawing = map.get(InvestmentStatusEnum.AUTO_WITHDRAWING.getCode());
		if (autoWithdrawing != null) {
			res.add(autoWithdrawing);
		}
		Integer autoWithdrawFail = map.get(InvestmentStatusEnum.AUTO_WITHDRAW_FAIL.getCode());
		if (autoWithdrawFail != null) {
			res.add(autoWithdrawFail);
		}
		Integer releasecashsuccess = map.get(InvestmentStatusEnum.AUTO_WITHDRAW_SUCCESS.getCode());
		if (releasecashsuccess != null) {
			res.add(releasecashsuccess);
		}
		Integer tendering = map.get(InvestmentStatusEnum.TENDERING.getCode());
		if (tendering != null) {
			res.add(tendering);
		}
		Integer tenderfail = map.get(InvestmentStatusEnum.TENDER_FAIL.getCode());
		if (tenderfail != null) {
			res.add(tenderfail);
		}
		return res;
	}

	/**
	 * 检索获得投资项目信息
	 * 
	 * @param status
	 *            状态
	 * @param investmentPeriod
	 *            期数
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Map<String, Object>> queryInvestmentInfo(List<Integer> status, List<Integer> investmentPeriod, List<Integer> accountType) {
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		//默认时间限制1个月内
		Calendar c = Calendar.getInstance();
		Date endDate = c.getTime();
		c.add(Calendar.DATE, -30);//当前时间30天
		Date startDate = c.getTime();
		//将状态符合的检索
		List<Integer> statusformate = formateStatus(status);
		for (Integer j : statusformate) {
			//暂时先屏蔽给测试进行测试使用
//			List<Investment> result = investmentRepository
//					.findByInvestmentStatusInAndInvestmentPeriodInAndInvestmentStartDateGreaterThanAndInvestmentStartDateLessThanAndInvestmentEndDateGreaterThan(j, investmentPeriod,
//							startDate, endDate, endDate, new Sort(Direction.DESC, "investmentStatus", "investmentStartDate"));
			List<Investment> result = investmentRepository
					.findByInvestmentStatusAndInvestmentPeriodIn(j, investmentPeriod,
							new Sort(Direction.DESC, "investmentStatus", "investmentStartDate"));
			//当前登陆用户是否已经投资
			Long id = p2pSessionContext.getCurrentAid();
			for (Investment i : result) {
				List<InvestmentAccountReference> tmp = investmentAccountReferenceRepository.findByInvestmentSequence(i.getInvestmentSequence());
				InvestmentAccountReference ref = tmp.get(0);
				//找寻符合的类型
				Account account = accountRepository.findByAccountSequenceAndLoanTypeIn(ref.getAccountSequence(), accountType);
				boolean isInvestment=false;
				if(id!=null){
					isInvestment = dealServiceReferenceManager.wytz0101ChkHasInvested(i.getInvestmentSequence(),id);
				}
				if (account != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("investment", i);
					map.put("account", account);
					map.put("isInvestment", isInvestment);
					res.add(map);
				}
			}
		}
		return res;
	}

	/**
	 * 检索获得投资项目信息
	 * 
	 * @param status
	 *            状态
	 * @param investmentPeriod
	 *            期数
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<Map<String, Object>> queryInvestmentInfoById(Long investmentId) {
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		Investment inv = investmentRepository.findOne(investmentId);
		List<InvestmentAccountReference> tmp = investmentAccountReferenceRepository.findByInvestmentSequence(inv.getInvestmentSequence());
		InvestmentAccountReference ref = tmp.get(0);
		//找寻符合的类型
		Account account = accountRepository.findOne(ref.getAccountSequence());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("investment", inv);
		map.put("account", account);
		//增加风险备用金
		ThirdPaymentAccount tpa=capitalRecordRepository.findByThirdPaymentIdType(ThirdPaymentAccountTypeEnum.P2P_RISK_ACCOUNT.getCode());
		if(tpa!=null){
			map.put("riskBackCapital", tpa.getThirdPaymentIdBalance().toString());
		}else{
			map.put("riskBackCapital", "0.00");
		}
		res.add(map);
		return res;
	}

	/**
	 * 强制指定投资项目流标
	 * 
	 * @param invest
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void investmentFlowBid(Long tInvSeq) {
		Investment investment = investmentRepository.findOne(tInvSeq);
		investment.setInvestmentStatus(InvestmentStatusEnum.TENDER_FAIL.getCode());//流标
		investment.setInvestmentLostDate(new Date());
		investmentRepository.save(investment);
	}

	/**
	 * 投资项目自动上线
	 * 
	 * @param onlineNum
	 *            待上线项目数目
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void investmentOnline(int onlineNum) {
		if (onlineNum <= 0) {
			return;
		}
		//找出待发布状态，类型是自动发布的项目的记录，然后将其上线
		Page<Investment> investment = investmentRepository.findByInvestmentStatusAndInvestmentType(InvestmentStatusEnum.IS_RELEASE.getCode(),
				InvestmentTypeEnum.AUTO.getCode(), new PageRequest(0, onlineNum, new Sort(Direction.DESC, "investmentCreateDate")));
		if (investment != null && investment.getSize() > 0) {
			for (Investment i : investment.getContent()) {
				investmentOnlineById(i.getInvestmentSequence(), true);
			}
		}
	}

	/**
	 * 获得日期时间
	 * 
	 * @return
	 */
	private int getLimitTime() {
		int limitDay = propertiesUtils.getDefaultLimitDay();
		Calendar c1 = Calendar.getInstance();
		int week = c1.get(Calendar.DAY_OF_WEEK);
		if (FRIDAY == week) {//是星期五，那么为3天
			limitDay = propertiesUtils.getFridayLimitDay();
		}
		return limitDay;
	}

	/**
	 * 立即上线以及自动上线 1.立即上线的功能:[待发布----》上线中]同时包括[预发布----》上线中]
	 * 2.自动上线的功能：[预发布----》上线中]
	 * 
	 * @param investmentId
	 *            项目Id
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Response investmentOnlineById(Long investmentId, boolean... isAuto) {
		Investment i = investmentRepository.findOne(investmentId);
		logger.info("开始上线操作......");
		try {
			if (isAuto != null && isAuto.length > 0 && isAuto[0]) {//自动上线,针对预发布4
				logger.info("自动上线......");
				if (InvestmentStatusEnum.IS_RELEASE.getCode() .equals( i.getInvestmentStatus())) {
					setLivingTime(i);
					investmentRepository.save(i);
					//添加标的录入
					logger.info("开始标的录入......");
					return addBidInfo(i.getInvestmentSequence(), new Date());
				} else {
					return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_586, ResponseConstants.CommonMessage.RESPONSE_MSG_586,
							"isAuto:" + isAuto[0]);
				}
			} else {//强制上线,针对预发布/待发布/上线失败的状态
				if (InvestmentStatusEnum.RELEASE_WAIT.getCode().equals(i.getInvestmentStatus())
						|| InvestmentStatusEnum.IS_RELEASE.getCode().equals(i.getInvestmentStatus())
						|| InvestmentStatusEnum.INVESTMENTONLINEFAILER.getCode().equals(i.getInvestmentStatus())) {//预发布和上线失败的才会改为上线状态
					setLivingTime(i);
					investmentRepository.save(i);
					//添加标的录入
					logger.info("开始标的录入......");
					return addBidInfo(i.getInvestmentSequence(), new Date());
				} else if (InvestmentStatusEnum.ON_LINE.getCode() .equals( i.getInvestmentStatus())) {
					return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_587, ResponseConstants.CommonMessage.RESPONSE_MSG_587, false);
				} else {
					return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_598, ResponseConstants.CommonMessage.RESPONSE_MSG_598, false);
				}
			}
		} catch (Exception e) {
			logger.error("investmentOnlineById", e);
			e.printStackTrace();
			i.setInvestmentStatus(InvestmentStatusEnum.INVESTMENTONLINEFAILER.getCode());
			investmentRepository.save(i);
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_584, ResponseConstants.CommonMessage.RESPONSE_MSG_584,
					"investmentOnlineById is error");
		}
	}

	/**
	 * 设置项目的开始和结束时间
	 * 
	 * @param i
	 * @return
	 */
	private void setLivingTime(Investment i) {
		int limitDay = getLimitTime();
		Calendar c = Calendar.getInstance();
		i.setInvestmentStartDate(c.getTime());//设置项目开始时间
		c.add(Calendar.DATE, limitDay);
		i.setInvestmentEndDate(c.getTime());//设置项目结束时间
	}

	/**
	 * 定时投资项目上线
	 * 
	 * @param invest
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void investmentTimeingOnline() {
		//找出上线时间条件已经满足且类型为定时项目，然后将其上线
		try {
			Thread.currentThread().sleep(10000l);
		} catch (InterruptedException e1) {
			logger.error("sleep is error",e1);
		}
		Date d=new Date();
		logger.info(d.toString());
		List<Investment> investment = investmentRepository.findByInvestmentStatusAndInvestmentTypeAndInvestmentStartDateLessThan(
				InvestmentStatusEnum.IS_RELEASE.getCode(), InvestmentTypeEnum.TIMEING.getCode(), d, new Sort(Direction.DESC,
						"investmentCreateDate"));
		for (Investment i : investment) {
			try {
				logger.info("开始定时上线："+i.getInvestmentName());
				//添加标的录入
				Response rep=addBidInfo(i.getInvestmentSequence(), new Date());
				logger.info("结束定时上线："+rep.getData());
			} catch (Exception e) {
				logger.error("investmentTimeingOnline", e);
				e.printStackTrace();
				i.setInvestmentStatus(InvestmentStatusEnum.INVESTMENTONLINEFAILER.getCode());
				investmentRepository.save(i);
			}
		}
	}
	
	/**
	 * 投资项目流标
	 * 
	 * @param invest
	 */
	public void investmentFailing() {
		//找出招标中的项目且结束时间已经小于当前时间满足的项目，然后将其流标
		List<Investment> investment = investmentRepository.findByInvestmentStatusAndInvestmentEndDateLessThan(InvestmentStatusEnum.ON_LINE.getCode(),
				new Date(), new Sort(Direction.DESC, "investmentCreateDate"));
		for (Investment i : investment) {
			//找出招标中的项目且结束时间已经小于当前时间满足的项目，然后将其流标
			bondPackageInvestManager.investFailure(i.getInvestmentSequence());
		}
	}

	/**
	 * 
	 * 投资项目的标的录入
	 * 
	 * @param investmentId
	 *            项目ID
	 * @param startDate
	 *            项目开始时间
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Response addBidInfo(Long investmentId, Date startDate) {
		if (investmentId == null) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_583, ResponseConstants.CommonMessage.RESPONSE_MSG_583,
					false);
		}
		ThirdPayUserResponseDto thirdPayUserResponseDto = null;
		Investment inv = investmentRepository.findOne(investmentId);
		setLivingTime(inv);
		String proId = inv.getInvestmentNumber();
		List<InvestmentAccountReference> res = investmentAccountReferenceRepository.findByInvestmentSequence(investmentId);
		for (InvestmentAccountReference i : res) {
			ThirdPaymentAccount tpa = capitalRecordRepository.findByAccountSequence(i.getAccountSequence());
			String borrCustId = tpa.getThirdPaymentUniqueId().toString();

			String borrTotAmt = inv.getInvestmentTotal().toString();
			String yearRate = dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_IRATE.getCode()
					+ inv.getInvestmentAnnualInterestRate());
			String retType = ThirdPayRetTypeEnum._01.getCode();
			String bidStartDate = DateFormatUtil.dateToString(inv.getInvestmentStartDate(), DateFormatUtil.YYYYMMDDHHMMSS);
			String bidEndDate = DateFormatUtil.dateToString(inv.getInvestmentEndDate(), DateFormatUtil.YYYYMMDDHHMMSS);
			Integer period = inv.getInvestmentPeriod();
			BigDecimal monthRate = new BigDecimal(yearRate).divide(new BigDecimal(12), ConstantsUtil.FLOD_INDEX, BigDecimal.ROUND_HALF_UP);
			double rate = 0;
			for (int j = period; j > 0; j--) {
				rate += FinanceUtil.IPMT(monthRate.doubleValue(), j, period, inv.getInvestmentTotal().doubleValue());
			}
			String retAmt = inv.getInvestmentTotal().add(new BigDecimal(rate)).setScale(ConstantsUtil.FLOD_INDEX2, BigDecimal.ROUND_HALF_UP)
					.toString();//本金+利息
			String retDate = DateFormatUtil.dateToString(startDate, DateFormatUtil.YYYYMMDD);
			String proArea = "1100";
			String result = bidInfoManager.addBidInfo(proId, borrCustId, borrTotAmt, yearRate, retType, bidStartDate, bidEndDate, retAmt, retDate,
					"", "", proArea);
			logger.info("后台标的录入返回结果："+result);
			try {
				Map<String,Object> parameter=P2pUtil.getBeanFromJson(result, HashMap.class);
				String rep=(String) parameter.get("RespCode");
				thirdPayUserResponseDto = ThirdPayUserResponseDto.formateJson(result);
				//如果成功
				if (ThirdChannelEnum.RESPCODEVALUE.getCode().equals(rep)) {
					//只有添加标的成功的才能修改
					logger.info("标的录入成功......");
					inv.setInvestmentStatus(InvestmentStatusEnum.ON_LINE.getCode());
					investmentRepository.save(inv);
					//添加定时流标的功能
					taskJobFactory.addJob(inv);
					return Response.successResponse(true);
				}else if(ThirdChannelEnum.RESPCODEVALUE395.getCode().equals(rep)){//标的已存在，检索数据是否重复，如果没重复，那么认为标的成功
					return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_578, ResponseConstants.CommonMessage.RESPONSE_MSG_578,
							false);
				} else {
					if(!InvestmentStatusEnum.ON_LINE.getCode().equals(inv.getInvestmentStatus())){//项目不是上线中
						logger.info("标的录入失败......"+inv.getInvestmentNumber());
						inv.setInvestmentStatus(InvestmentStatusEnum.INVESTMENTONLINEFAILER.getCode());
						investmentRepository.save(inv);
						return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_585, ResponseConstants.CommonMessage.RESPONSE_MSG_585,
								false);
					}else{
						return Response.successResponse(true);
					}
				}
			} catch (Exception e) {
				logger.info("后台标的录入出错：",e);
			}
			logger.info("$$$$$$$" + result);
		}
		logger.info("标的录入结束");
		return Response.successResponse(true);
	}

	/**
	 * 将流标的项目重新复制一份，然后直接将状态变为online
	 * 
	 * @param investmentId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Response copyInvestment(Long investmentId) throws Exception {
		Investment inv = investmentRepository.findOne(investmentId);
		if (inv == null) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_583, ResponseConstants.CommonMessage.RESPONSE_MSG_583, false);
		}
		if (!InvestmentStatusEnum.TENDER_FAIL.getCode().equals(inv.getInvestmentStatus())) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_582, ResponseConstants.CommonMessage.RESPONSE_MSG_582, false);
		}
		if (InvestmentTypeEnum.LASTBID2ONLINE.getCode().equals(inv.getInvestmentType())) {//当前状态已经流标再发布了则返回，不给流标再发布
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_580, ResponseConstants.CommonMessage.RESPONSE_MSG_580, false);
		}
		//将当前id的项目进行复制
		List<InvestmentAccountReference> refes = investmentAccountReferenceRepository.findByInvestmentSequence(investmentId);
		if (refes == null || refes.size() == 0) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_583, ResponseConstants.CommonMessage.RESPONSE_MSG_583, false);
		}
		InvestmentAccountReference i = refes.get(0);

		List<LoanData> loanDataList = new ArrayList<LoanData>();
		LoanData ld = loanDataRepository.findOne(i.getLoanDataSequence());
		loanDataList.add(ld);
		boolean res = componentProject(loanDataList);//将loanData重新生成对象
		//将复制的项目进行待发布状态
		if (res) {
			Response response = null;
			refes = investmentAccountReferenceRepository.findByLoanDataSequence(ld.getLoanDataSequence());
			for (InvestmentAccountReference ref : refes) {
				Investment investment = investmentRepository.findOne(ref.getInvestmentSequence());
				if (InvestmentStatusEnum.ACTIVE_WAIT.getCode() == investment.getInvestmentStatus()) {//新创建的项目
					//将新建的项目的状态更新为待发布，因为强制上线的功能限定了投资项目的状态必须为，待发布、预发布、上线错误这3个状态
//					response = investmentOnlineById(investment.getInvestmentSequence());
					investment.setInvestmentStatus(InvestmentStatusEnum.RELEASE_WAIT.getCode());
					investmentRepository.save(investment);
					//将旧的项目的状态更新为已废弃modify20150401去掉已废弃这个状态，
//					inv.setInvestmentStatus(InvestmentStatusEnum.CANCEL.getCode());
//					investmentRepository.save(inv);
				}
			}
			return Response.successResponse();//modify 20150414
		} else {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_593, ResponseConstants.CommonMessage.RESPONSE_MSG_593, false);
		}
	}
}
