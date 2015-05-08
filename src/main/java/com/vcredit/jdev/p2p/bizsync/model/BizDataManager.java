package com.vcredit.jdev.p2p.bizsync.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.account.modal.CommonManager;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil.AfterLoanData;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil.BeforeLoanData;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil.VbsSyncType;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.bizsync.convert.BillInfoDto;
import com.vcredit.jdev.p2p.bizsync.convert.BorrowInfo;
import com.vcredit.jdev.p2p.bizsync.convert.Borrower;
import com.vcredit.jdev.p2p.bizsync.convert.CardInfo;
import com.vcredit.jdev.p2p.bizsync.convert.CheckFieldDto;
import com.vcredit.jdev.p2p.bizsync.convert.VbsCreateDto;
import com.vcredit.jdev.p2p.bizsync.convert.VbsDetailRs;
import com.vcredit.jdev.p2p.bizsync.convert.VbsQueryDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.Area;
import com.vcredit.jdev.p2p.entity.LoanCut;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.enums.AreaLevelEnum;
import com.vcredit.jdev.p2p.enums.BizSyncEnum;
import com.vcredit.jdev.p2p.enums.BlDataStatusEnum;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.InvPublishTypeEnum;
import com.vcredit.jdev.p2p.enums.InvestSourceEnum;
import com.vcredit.jdev.p2p.enums.LoanDataRecordStatusEnum;
import com.vcredit.jdev.p2p.enums.VbsBillType;
import com.vcredit.jdev.p2p.merchant.modal.MerchantManager;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.BizDataLoanCutRepository;
import com.vcredit.jdev.p2p.repository.BizDataQueryRepository;
import com.vcredit.jdev.p2p.repository.BizDataRepository;

@Component
public class BizDataManager {

	@Autowired
	BizDataRepository bizDataRepository;

	@Autowired
	BizDataLoanCutRepository bizDataLoanCutRepository;
	
	@Autowired
	AccountRepository accountRepository;

	@Autowired
	private DictionaryUtil dictionaryUtil;

	@Autowired
	private BizDataQueryRepository bizDataQueryRepository;

	@Autowired
	private CommonManager commonManager;
	
	Logger logger = LoggerFactory.getLogger(BizDataManager.class);

	/**
	 * 单条插入LoanData
	 * 
	 * @param dto
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public LoanData dataImport(LoanData data) {
		LoanData rsData = bizDataRepository.save(data);
		return rsData;
	}

	/**
	 * 单条插入LoanCut
	 * 
	 * @param data
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public LoanCut dataImportAfterLoan(LoanCut data) {
		LoanCut rsData = bizDataLoanCutRepository.save(data);
		return rsData;
	}

	/**
	 * dto转换实体
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public LoanData changeDto2bean(VbsCreateDto dto,String syncType) throws Exception {
		LoanData data = new LoanData();
		//借款人信息
		Borrower borrower = dto.getBorrower();
		data.setPid(borrower.getIdCard()); //身份证
		data.setLoanType(borrower.getCustomerType()); //客户类型
		data.setRealName(borrower.getName()); //姓名
		data.setGender(borrower.getGender()); //性别
		data.setEducationDegree(borrower.getEducationDegree()); //学历
		data.setMarryStatus(borrower.getMarryStatus()); //婚姻状况
		data.setMobile(borrower.getMobile());
		data.setEmail(borrower.getEmail());
//		data.setHometownCity(borrower.getHometown()); //户籍地所在省/直辖市
		data.setHometownProvince(borrower.getHometown());
		data.setWorkCity(borrower.getWorkCity());
		data.setCompanyInustry(borrower.getCompanyInustry());
		data.setCompanySize(borrower.getCompanySize());
		data.setWorkTime(Integer.parseInt(borrower.getWorkTime()));
		data.setDepartment(borrower.getDepartment());
		data.setAccountIndustry(borrower.getAccountIndustry());
		data.setCompanyProperty(borrower.getCompanyProperty());
		data.setIncome(borrower.getIncome());
		data.setSalaryDate(borrower.getSalaryDate());
		data.setHouseProperty(borrower.getHouseProperty());
		data.setSocialSecurityStatus(Integer.parseInt(borrower.getSocialSecurityStatus()));
		data.setAccountHouseLoan(borrower.getHouseLoanProperty()); //房贷情况

		//银行卡信息
		data.setBankCardNumber(dto.getCardInfo().getBankCardNumber());
		data.setBankCode(dto.getCardInfo().getBankCode());
		data.setBankProvince(dto.getCardInfo().getBankProvince());
		data.setBankCity(dto.getCardInfo().getBankCity());

		//借款项目信息
		BorrowInfo info = dto.getBorrowInfo();
//		data.setInvestmentSource(info.getInvestmentSource());
		data.setInvestmentSource(InvestSourceEnum.SOURCE_WEIXIN.getCode()); //目前只有维信 1
		data.setInvestmentBusinessCode(info.getInvestmentBusinessCode());
		data.setProductionType(info.getProductionType());
		data.setInvestmentTarget(info.getInvestmentTarget());
		data.setInvestmentPeriod(Integer.parseInt(info.getInvestmentPeriod()));
		data.setAccountLoanAmount(new BigDecimal(info.getAccountLoanAmount()));
		data.setContractId(info.getContractNumber());
		data.setCredibilityLevel(info.getCredibilityLevel());
		data.setCredibilityScore(info.getCredibilityScore());
		data.setInvestmentAnnualInterestRate(info.getInvestmentAnnualInterestRate());
		data.setP2pEarlyPayFeeRate(new BigDecimal(info.getP2pEarlyPayFeeRate()));
		data.setP2pPayFeeRate(new BigDecimal(info.getP2pPayFeeRate()));
		data.setP2pOperateFeeRate(new BigDecimal(info.getP2pOperateFeeRate()));
		if (null == DateFormatUtil.string2date(info.getSignDate(), DateFormatUtil.VBS_DATE_FMT)) {
			throw new NullPointerException(ResponseConstants.CommonMessage.RESPONSE_MSG_831);
		}
		data.setSignDate(DateFormatUtil.string2date(info.getSignDate(), DateFormatUtil.VBS_DATE_FMT)); //合同签约时间 

		data.setProgressStauts(BlDataStatusEnum.importHandelCompleted.getCode()); //进度状态 初始值1 pro_stat未处理
		data.setRecordInDate(new Date()); //记录入库时间 
		data.setRecordStatus(LoanDataRecordStatusEnum.RIGHT.getCode()); //项目状态 1 rec_stat贷前数据已入库, 改字段作为VBS导入 预留字段处理
		
		data.setInvestmentType(InvPublishTypeEnum.publish_auto.getCode()); //自动发布 p2p发布方式 自动 1 定时2
		data.setLoanCompanyPushType(Integer.parseInt(syncType)); //小贷公司推送类型：实时1（临时） 批量2（常规）
		
		return data;
	}

	/**
	 * 贷后dto转换entity
	 * 
	 * @param dto
	 * @return
	 */
	public LoanCut changeDto2beanAfterLoan(BillInfoDto dto) {
		LoanCut data = new LoanCut();
		data.setBillType(dto.getBillType());
		data.setInvestmentBusinessCode(dto.getInvestmentBusinessCode()); //业务编码,和期号，项目来源为复合唯一主键
		// 提前还贷类型 设置 期数0
		if (VbsBillType.advanced_bill.getCode().equals(dto.getBillType())) {
			data.setLoanPeriod(0);
		} else {
			data.setLoanPeriod(dto.getLoanPeriod());
		}
		data.setTotalShouldGet(new BigDecimal(dto.getTotalShouldGet()));
		data.setTotalActulGet(new BigDecimal(dto.getTotalActulGet()));
		if (null == DateFormatUtil.string2date(dto.getCutShouldDate(), DateFormatUtil.VBS_DATE_FMT)) {
			throw new NullPointerException(ResponseConstants.CommonMessage.RESPONSE_MSG_832);
		}
		data.setCutShouldDate(DateFormatUtil.string2date(dto.getCutShouldDate(), DateFormatUtil.VBS_DATE_FMT));

		data.setProgressStauts(BlDataStatusEnum.importHandelCompleted.getCode());
//		data.setInvestmentSource(1); //项目来源 暂定维信
		data.setInvestmentSource(InvestSourceEnum.SOURCE_WEIXIN.getCode()); //目前只有维信 1
		data.setRecordInDate(new Date()); //记录入库时间 
		data.setRecordStatus(LoanDataRecordStatusEnum.RIGHT.getCode());
		
		return data;
	}

	/**
	 * 检查导入字段是否为空
	 * 
	 * @param obj
	 * @param detailRs
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public boolean checkFieldIsNull(Object obj, String vbsSyncType, CheckFieldDto fDto) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		boolean isError = false;
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String name = fields[i].getName();
			fDto.setEmptyField(name);
			// 将属性的首字符大写，方便构造get，set方法
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			
			// 获取属性的类型
			//			String type = fields[i].getGenericType().toString();

			//获取属性的值
			Method m = obj.getClass().getMethod("get" + name);
			if (null == m.invoke(obj) || "".equals(m.invoke(obj))) {
				if (VbsSyncType.BEFORE_LOAN.equals(vbsSyncType)) {
					if (BeforeLoanData.EMAIL.equals(name) || BeforeLoanData.COMPANY_SIZE.equals(name) || BeforeLoanData.Work_Province.equals(name)) {
						continue; //贷前数据导入，这个3个字段 不做必须输入check, workProvince是用于 从 workCity解析出省份来
					}
					if (BeforeLoanData.SALARY_DATE.equals(name) || BeforeLoanData.House_Loan_Property.equals(name)){
						continue; //每月授薪日 可以为空，需求变更 3/12日;  房贷情况是用于 从住宅情况解析而来
					}
					/*if (BeforeLoanData.Hometown.equals(name) || BeforeLoanData.Work_City.equals(name)) {
						continue; //贷前数据导入，hometown 和 workcity,如果VBS推送过来匹配不上时 传空过来，故不做必须check
					}*/
					isError = true;
					break;
				} else if (VbsSyncType.AFTER_LOAN.equals(vbsSyncType)) {
					// 贷后记录的期数可以为空，但仅限 提前还贷类型，在贷后的check中继续拦截
					if (AfterLoanData.LOAN_PERIOD.equals(name)) {
						continue;
					}
					isError = true;
					break;
				}
			}

		}
		return isError;
	}

	/**
	 * 贷前数据做 业务效验
	 * 
	 * @param dto
	 * @param list
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean checkBeforeLoanData(VbsCreateDto dto, ArrayList<VbsDetailRs> list) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		boolean isError = false;

		//借款人信息
		Borrower borrower = dto.getBorrower();
		//银行卡信息
		CardInfo cardInfo = dto.getCardInfo();
		//借款项目信息
		BorrowInfo borrowInfo = dto.getBorrowInfo();

		//非空效验 begin
		CheckFieldDto fDto = new CheckFieldDto();
		if (!checkFieldIsNull(borrower, VbsSyncType.BEFORE_LOAN, fDto)) {
			if (!checkFieldIsNull(cardInfo, VbsSyncType.BEFORE_LOAN, fDto)) {
				isError = checkFieldIsNull(borrowInfo, VbsSyncType.BEFORE_LOAN, fDto);
			} else {
				isError = true;
			}
		} else {
			isError = true;
		}
		if (isError) {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_813, ResponseConstants.CommonMessage.RESPONSE_MSG_813+fDto.getEmptyField(), list,
					borrowInfo);
		}
		
		//非空效验 end		
		//判断身份证号码与手机号是否匹配 begin
		if(!checkPidAndMobileInAccount(borrower.getIdCard(), borrower.getMobile())){
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_208, ResponseConstants.CommonMessage.RESPONSE_MSG_208, list,
					borrowInfo);
		}
		//判断身份证号码与手机号是否匹配 end

		//业务编码唯一性 校验
		String businessCode = borrowInfo.getInvestmentBusinessCode();
//		Integer investmentSource = borrowInfo.getInvestmentSource();
		Integer investmentSource = InvestSourceEnum.SOURCE_WEIXIN.getCode();
		if (isExistBusinessCodeByCodeAndSource(businessCode, investmentSource)) {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_812, ResponseConstants.CommonMessage.RESPONSE_MSG_812, list,
					borrowInfo);
		}

		//字典值匹配 校验
		if (hasErrMapping(dto, list)) {
			isError = true;
		} else {
			isError = false;
		}

		return isError;
	}

	/**
	 * 
	 * @param errCode
	 * @param errMsg
	 * @param list
	 * @param detailRs
	 * @param borrowInfo
	 * @return
	 */
	private boolean setErrMsgBeforeLoan(String errCode, String errMsg, ArrayList<VbsDetailRs> list, BorrowInfo borrowInfo) {
		VbsDetailRs detailRs = new VbsDetailRs();
		detailRs.setParseErrCode(errCode);
		detailRs.setParseErrMsg(errMsg);
		detailRs.setLoanBusinessId(borrowInfo.getInvestmentBusinessCode());
		list.add(detailRs);
		return true;
	}
	
	private boolean checkPidAndMobileInAccount(String pid,String mobile){
		Account account = accountRepository.findByPid(pid);
		Account mobileAccount = null; 
		if(StringUtils.isNotBlank(mobile)){
			mobileAccount = accountRepository.findByMobile(mobile);
		}
		if(account == null){
			// pid不存在Account，而mobile存在Account
			if(mobileAccount != null){
				logger.error("registerBorrowerFromVBS-->this mobile is exist!");
				return false;
			}
		}else{
			//pid,mobile都存在Account,但两者Account的AccountSequence不相等
			//Long封装类型的比较用equals,long类型的比较用==
			if((mobileAccount != null)&&!(account.getAccountSequence().equals(mobileAccount.getAccountSequence()))){
				logger.error("registerBorrowerFromVBS-->this mobile is exist!");
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param errCode
	 * @param errMsg
	 * @param list
	 * @param billInfoDto
	 * @return
	 */
	private boolean setErrMsgAfterLoan(String errCode, String errMsg, ArrayList<VbsDetailRs> list, BillInfoDto billInfoDto) {
		VbsDetailRs detailRs = new VbsDetailRs();
		detailRs.setParseErrCode(errCode);
		detailRs.setParseErrMsg(errMsg);
		detailRs.setLoanBusinessId(billInfoDto.getInvestmentBusinessCode());
		detailRs.setLoanPeriod(billInfoDto.getLoanPeriod());
		list.add(detailRs);
		return true;
	}

	/**
	 * 
	 * @param dto
	 * @param list
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public boolean checkAfterLoanData(BillInfoDto dto, ArrayList<VbsDetailRs> list) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		boolean isError = false;

		//效验非空begin
		CheckFieldDto fDto = new CheckFieldDto();
		if (checkFieldIsNull(dto, VbsSyncType.AFTER_LOAN, fDto)) {
			return setErrMsgAfterLoan(ResponseConstants.CommonCode.RESPONSE_CODE_813, ResponseConstants.CommonMessage.RESPONSE_MSG_813 + fDto.getEmptyField(), list, dto);
		}
		// 贷后记录的期数可以为空，但仅限 提前还贷类型; 
		// 非提前还贷的，期数不能为空
		if (!VbsBillType.advanced_bill.getCode().equals(dto.getBillType())) {
			if (null == dto.getLoanPeriod() || 0 == dto.getLoanPeriod()) {
				return setErrMsgAfterLoan(ResponseConstants.CommonCode.RESPONSE_CODE_815, ResponseConstants.CommonMessage.RESPONSE_MSG_815, list, dto);
			}
		} else {
			dto.setLoanPeriod(0);
		}
		//效验非空end

		//业务编码唯一性 校验
		/*
		 * String businessCode = dto.getInvestmentBusinessCode(); Integer
		 * loanPeriod; if(null == dto.getLoanPeriod()){ loanPeriod = 0; }else{
		 * loanPeriod = dto.getLoanPeriod(); } //维信来源暂定为1 刘伟确认 // 记录类型 为提前还贷，则期数
		 * vbs不传至p2p if (isExistBusinessCodeByCodeAndLoanperiod(businessCode,
		 * loanPeriod)) { return
		 * setErrMsgAfterLoan(ResponseConstants.CommonCode.RESPONSE_CODE_812,
		 * ResponseConstants.CommonMessage.RESPONSE_MSG_812, list, dto); }
		 */

		return isError;
	}

	/**
	 * 转换vbs字典字典（ value值 to key）
	 * 
	 * @param d
	 * @param list
	 * @return
	 */
	public boolean hasErrMapping(VbsCreateDto d, ArrayList<VbsDetailRs> list) {

		//董坤 注册用户用
		//贷款人类型
		String loanType = d.getBorrower().getCustomerType();
		//工薪/非法人且大股东/非法人且小股东/其他（临时变更）  转换
		if(ConstantsUtil.SALARY_TYPEMAP.containsKey(loanType)){
//		if("工薪".equals(loanType) || "非法人且大股东".equals(loanType) || "非法人且小股东".equals(loanType)){
			loanType = ConstantsUtil.SALARY_STR; //工薪族
		}else if(ConstantsUtil.LAWYER_TYPEMAP.containsKey(loanType)){  //法人 转换
			loanType = ConstantsUtil.LAWYER_STR; //私营业主
		}//工薪族 工薪族
		loanType = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_LTYPE.getCode()
				+ loanType);
		if (StringUtils.isNotBlank(loanType)) {
			d.getBorrower().setCustomerType(loanType);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_833, ResponseConstants.CommonMessage.RESPONSE_MSG_833, list,
					d.getBorrowInfo());
		}

		//性别
		String gender = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_GENDER.getCode()
				+ d.getBorrower().getGender());
		if (StringUtils.isNotBlank(gender)) {
			d.getBorrower().setGender(gender);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_834, ResponseConstants.CommonMessage.RESPONSE_MSG_834, list,
					d.getBorrowInfo());
		}

		//学历
		String educationDegree = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_EDU_DEGREE.getCode()
				+ d.getBorrower().getEducationDegree());
		if (StringUtils.isNotBlank(educationDegree)) {
			d.getBorrower().setEducationDegree(educationDegree);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_835, ResponseConstants.CommonMessage.RESPONSE_MSG_835, list,
					d.getBorrowInfo());
		}

		//婚姻状况
		String marryStatus = d.getBorrower().getMarryStatus();
		if(ConstantsUtil.MARRY_TYPEMAP.containsKey(marryStatus)){ //未说明婚姻状况
			marryStatus = ConstantsUtil.OTHER_STR; //其他
		}
		marryStatus = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_MSTAT.getCode()
				+ marryStatus);
		if (StringUtils.isNotBlank(marryStatus)) {
			d.getBorrower().setMarryStatus(marryStatus);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_836, ResponseConstants.CommonMessage.RESPONSE_MSG_836, list,
					d.getBorrowInfo());
		}

		//户籍所在地级省/直辖市  tac_hcity
		if(!StringUtils.isEmpty(d.getBorrower().getHometown())){
			Integer hcityInt = commonManager.getAreaIdByAreaName(d.getBorrower().getHometown());
			if (null != hcityInt) {
				d.getBorrower().setHometown(hcityInt.toString());
			} else {
				return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_837, ResponseConstants.CommonMessage.RESPONSE_MSG_837, list,
						d.getBorrowInfo());
			}
		}

		//工作所在地级市 （XX市:地级市或直辖市）
		if(!StringUtils.isEmpty(d.getBorrower().getWorkCity())){
			String[] workProCity = d.getBorrower().getWorkCity().split(ConstantsUtil.SPLIT_AREA);
			if(workProCity.length == 1){
				//直辖市  XX市
				Area area = commonManager.findByAddressChineseName(workProCity[0]);
				if(null != area){
					if(area.getAddressLevel().intValue() == AreaLevelEnum.AERA_SELF_GOVERNED_CITY.getLevel().intValue()){
						d.getBorrower().setWorkCity(area.getAddressId().toString());
						d.getBorrower().setWorkProvince(area.getAddressId().toString());
					}else{
						return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_838, ResponseConstants.CommonMessage.RESPONSE_MSG_838, list,
								d.getBorrowInfo());
					}
				}else{
					return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_838, ResponseConstants.CommonMessage.RESPONSE_MSG_838, list,
							d.getBorrowInfo());
				}
			}else{
				//地级市 XX省XX市 
				//确保不会存在 同个省出现重复市的情况
				Area areaProvince = commonManager.findByAddressChineseName(workProCity[0]);
				Area areaCity = commonManager.findByAddressChineseName(workProCity[1]);
				if(null != areaProvince && null != areaCity){
					d.getBorrower().setWorkProvince(areaProvince.getAddressId().toString());
					d.getBorrower().setWorkCity(areaCity.getAddressId().toString());
				}else{
					return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_838, ResponseConstants.CommonMessage.RESPONSE_MSG_838, list,
							d.getBorrowInfo());
				}
				
			}
			/*Area area = commonManager.findByAddressChineseName(d.getBorrower().getWorkCity());
			if(null != area){
				//直辖市
				if(area.getAddressLevel() == AreaLevelEnum.AERA_SELF_GOVERNED_CITY.getLevel()){
					d.getBorrower().setWorkCity(area.getAddressId().toString());
					d.getBorrower().setWorkProvince(area.getAddressId().toString());
				}
				//地级市
				if(area.getAddressLevel() == AreaLevelEnum.AERA_CITY.getLevel()){
					d.getBorrower().setWorkCity(area.getAddressId().toString());
					//获取地级市所在省的 地区编号
					Area areaProvince = commonManager.findByAddressChineseName(area.getAddressProvinceChineseName());
					d.getBorrower().setWorkProvince(areaProvince.getAddressId().toString());
				}
			}else{
				return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_838, ResponseConstants.CommonMessage.RESPONSE_MSG_838, list,
						d.getBorrowInfo());
			}*/
		}

		String housePropertyStr = d.getBorrower().getHouseProperty();
		String p2pHouseProperty = ""; //p2p 有 / 无
		String houseProperty = ""; //字典key

		//房产情况
		if(ConstantsUtil.HOUSE_HAS_TYPEMAP.containsKey(housePropertyStr)){
//		if("自置".equals(houseProperty) || "按揭".equals(houseProperty) || "共有住宅".equals(houseProperty)){
			p2pHouseProperty = ConstantsUtil.HOUSE_HAS; //有
		}else if(ConstantsUtil.HOUSE_NONE_TYPEMAP.containsKey(housePropertyStr)){
//			}else if("亲属楼宇".equals(houseProperty) || "集体宿舍".equals(houseProperty) || "租赁".equals(houseProperty) || "公房".equals(houseProperty) || "其他".equals(houseProperty)){
			p2pHouseProperty = ConstantsUtil.HOUSE_NONE; //无
		}
		houseProperty = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_HOUSE.getCode()
				+ p2pHouseProperty);
		if (StringUtils.isNotBlank(houseProperty)) {
			d.getBorrower().setHouseProperty(houseProperty);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_839, ResponseConstants.CommonMessage.RESPONSE_MSG_839, list,
					d.getBorrowInfo());
		}
		
		//房贷情况
		if(ConstantsUtil.HOUSE_LOAN_TYPEMAP.containsKey(housePropertyStr)){
			p2pHouseProperty = ConstantsUtil.HOUSE_HAS; //有
		}else{
			p2pHouseProperty = ConstantsUtil.HOUSE_NONE; //无
		}
		houseProperty = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_HOUSE_LOAN.getCode()
				+ p2pHouseProperty);
		if (StringUtils.isNotBlank(houseProperty)) {
			d.getBorrower().setHouseLoanProperty(houseProperty);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_854, ResponseConstants.CommonMessage.RESPONSE_MSG_854, list,
					d.getBorrowInfo());
		}
		
		//缴金情况
		String ssStatus = dictionaryUtil.getDicValue(DictionaryEnum.T_LOAN_DATA.getCode() + DictionaryEnum.SS_STAT.getCode()
				+ d.getBorrower().getSocialSecurityStatus());
		if (StringUtils.isNotBlank(ssStatus)) {
			d.getBorrower().setSocialSecurityStatus((ssStatus));
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_851, ResponseConstants.CommonMessage.RESPONSE_MSG_851, list,
					d.getBorrowInfo());
		}
		
		//公司行业
		String companyInustry = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_CINDS.getCode()
				+ d.getBorrower().getCompanyInustry());
		if (StringUtils.isNotBlank(companyInustry)) {
			d.getBorrower().setCompanyInustry(companyInustry);;
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_840, ResponseConstants.CommonMessage.RESPONSE_MSG_840, list,
					d.getBorrowInfo());
		}
		
		//公司规模
		String companySize = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_CSIZE.getCode()
				+ d.getBorrower().getCompanySize());
		if (StringUtils.isNotBlank(companySize)) {
			d.getBorrower().setCompanySize(companySize);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_841, ResponseConstants.CommonMessage.RESPONSE_MSG_841, list,
					d.getBorrowInfo());
		}
		
		//企业性质
		String companyProperty = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_CPT.getCode()
				+ d.getBorrower().getCompanyProperty());
		if (StringUtils.isNotBlank(companyProperty)) {
			d.getBorrower().setCompanyProperty(companyProperty);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_842, ResponseConstants.CommonMessage.RESPONSE_MSG_842, list,
					d.getBorrowInfo());
		}
		
		//收入
		String income = dictionaryUtil.getDicValue(DictionaryEnum.T_ACCT.getCode() + DictionaryEnum.TAC_INCOME.getCode()
				+ d.getBorrower().getIncome());
		if (StringUtils.isNotBlank(income)) {
			d.getBorrower().setIncome(income);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_843, ResponseConstants.CommonMessage.RESPONSE_MSG_843, list,
					d.getBorrowInfo());
		}
		
		//周佩 发布标的用
		//年利率
		logger.debug("---vbsIn---: "+d.getBorrowInfo().getInvestmentAnnualInterestRate());
		String investmentAnnualInterestRate = dictionaryUtil.getDicValue(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_IRATE.getCode()
				+ d.getBorrowInfo().getInvestmentAnnualInterestRate());
		if (StringUtils.isNotBlank(investmentAnnualInterestRate)) {
			d.getBorrowInfo().setInvestmentAnnualInterestRate(investmentAnnualInterestRate);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_844, ResponseConstants.CommonMessage.RESPONSE_MSG_844, list,
					d.getBorrowInfo());
		}
		
		//项目用途 inv_target
		String investmentTarget = dictionaryUtil.getDicValue(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_TARGET.getCode()
				+ d.getBorrowInfo().getInvestmentTarget());
		if (StringUtils.isNotBlank(investmentTarget)) {
			d.getBorrowInfo().setInvestmentTarget(investmentTarget);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_845, ResponseConstants.CommonMessage.RESPONSE_MSG_845, list,
					d.getBorrowInfo());
		}
		
		//期数 
		String investmentPeriod = dictionaryUtil.getDicValue(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_PERIOD.getCode()
				+ d.getBorrowInfo().getInvestmentPeriod());
		if (StringUtils.isNotBlank(investmentPeriod)) {
			d.getBorrowInfo().setInvestmentPeriod(investmentPeriod);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_846, ResponseConstants.CommonMessage.RESPONSE_MSG_846, list,
					d.getBorrowInfo());
		}
		
		//项目来源
		String investSource = d.getBorrowInfo().getInvestmentSource();
		if(!investSource.equals(InvestSourceEnum.SOURCE_WEIXIN.getValue())){
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_847, ResponseConstants.CommonMessage.RESPONSE_MSG_847, list,
					d.getBorrowInfo());
		}
		
		//开户行所在省
//		String bankProvince = dictionaryUtil.getDicValue(DictionaryEnum.T_ACT_BKCARD.getCode() + DictionaryEnum.T_ACT_BKCARD_ABC_PROV.getCode()
//				+ d.getCardInfo().getBankProvince());
		
		Integer bankProvince = commonManager.getAreaIdByAreaName(d.getCardInfo().getBankProvince());
		
		if (null != bankProvince) {
			d.getCardInfo().setBankProvince(bankProvince.toString());
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_848, ResponseConstants.CommonMessage.RESPONSE_MSG_848, list,
					d.getBorrowInfo());
		}
		
		//开户行所在市 
//		String bankCity = dictionaryUtil.getDicValue(DictionaryEnum.T_ACT_BKCARD.getCode() + DictionaryEnum.T_ACT_BKCARD_ABC_CITY.getCode()
//				+ d.getCardInfo().getBankCity());
		Integer bankCity = commonManager.getAreaIdByAreaName(d.getCardInfo().getBankCity());
		if (null != bankCity) {
			d.getCardInfo().setBankCity(bankCity.toString());
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_849, ResponseConstants.CommonMessage.RESPONSE_MSG_849, list,
					d.getBorrowInfo());
		}
		
		//项目信用等级 
		String credibilityLevel = dictionaryUtil.getDicValue(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_LEVEL.getCode()
				+ d.getBorrowInfo().getCredibilityLevel());
		if (StringUtils.isNotBlank(credibilityLevel)) {
			d.getBorrowInfo().setCredibilityLevel(credibilityLevel);
		} else {
			return setErrMsgBeforeLoan(ResponseConstants.CommonCode.RESPONSE_CODE_850, ResponseConstants.CommonMessage.RESPONSE_MSG_850, list,
					d.getBorrowInfo());
		}
		
		return false;

	}

	/**
	 * 通过业务码 查询LoanData
	 * 
	 * @param investmentBusinessCode
	 * @return
	 */
	public LoanData getLoanDataByBusinessCode(String investmentBusinessCode) {
		return bizDataRepository.findByInvestmentBusinessCode(investmentBusinessCode);
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public List<VbsQueryDto> getVbsQueryList(String[] s) {
		return bizDataQueryRepository.getVbsQueryList(s);
	}

	/**
	 * 通过业务码、维信来源 查询LoanData
	 * 
	 * @param investmentBusinessCode
	 * @param investmentSource
	 * @return
	 */
	public LoanData getLoanDataByBusinessCodeanLoanData(String investmentBusinessCode, Integer investmentSource) {
		return bizDataRepository.findByInvestmentBusinessCodeAndInvestmentSource(investmentBusinessCode, investmentSource);
	}
	
//	public LoanData saveData(){
//		
//	}

	/**
	 * LoanData中是否已存在业务码
	 * 
	 * @param businessCode
	 * @return
	 */
	public boolean isExistBusinessCode(String businessCode) {
		return null != getLoanDataByBusinessCode(businessCode);
	}

	public boolean isExistBusinessCodeByCodeAndSource(String businessCode, Integer investmentSource) {
		return null != getLoanDataByBusinessCodeanLoanData(businessCode, investmentSource);
	}

	public LoanCut getLoanCutByBusinessCodeAndLoanPeriod(String investmentBusinessCode, Integer loanPeriod) {
		return bizDataLoanCutRepository.findByInvestmentBusinessCodeAndLoanPeriod(investmentBusinessCode, loanPeriod);
	}

	public boolean isExistBusinessCodeByCodeAndLoanperiod(String businessCode, Integer loanPeriod) {
		return null != getLoanCutByBusinessCodeAndLoanPeriod(businessCode, loanPeriod);
	}

}
