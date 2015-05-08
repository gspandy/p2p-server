package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.hsqldb.lib.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.alert.service.AccountMessageManager;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.capital.modal.CapitalAccountManager;
import com.vcredit.jdev.p2p.dto.AlertDto;
import com.vcredit.jdev.p2p.dto.AutoInvestDto;
import com.vcredit.jdev.p2p.dto.ManualInvestBackMessageDto;
import com.vcredit.jdev.p2p.dto.ManualInvestReturnDto;
import com.vcredit.jdev.p2p.dto.PayResult;
import com.vcredit.jdev.p2p.dto.ThirdPayUserRequestDto;
import com.vcredit.jdev.p2p.dto.UnForzenCapitalDto;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.AccountInvestRule;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.AccountOrderHistory;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;
import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;
import com.vcredit.jdev.p2p.enums.AccountInvestRuleStatusEnum;
import com.vcredit.jdev.p2p.enums.AccountInvestRuleTypeEnum;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.OrderIDRuleEnum;
import com.vcredit.jdev.p2p.enums.OrderStatusEnum;
import com.vcredit.jdev.p2p.enums.RecStatusEnum;
import com.vcredit.jdev.p2p.enums.TradeTypeEnum;
import com.vcredit.jdev.p2p.repository.AccountInvestRuleRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderHistoryRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.AccountThirdRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.util.CollectionUtil;
import com.vcredit.jdev.p2p.util.DateUtil;

/**
 * 订单模块的债权交易，主要包括自动投资，手动投资，循环投资，投资策略等。
 * 
 * @author zhuqiu
 *
 */
@Component
public class BondPackageInvestManager {
	private Logger logger = LoggerFactory.getLogger(BondPackageInvestManager.class);

	/** 投资项目 */
	@Autowired
	private InvestmentRepository investmentRepository;

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;

	/** 用户订单状态历史 */
	@Autowired
	private AccountOrderHistoryRepository accountOrderHistoryRepository;

	/** 用户第三方支付 */
	@Autowired
	private AccountThirdRepository accountThirdRepository;

	/** 用户定义的投资规则 */
	@Autowired
	private AccountInvestRuleRepository accountInvestRuleRepository;

	/** 自动投资Repository */
	@Autowired
	private DealRepository dealRepository;

	/** 用户p2p平台账号Repository */
	@Autowired
	private AccountRepository accountRepository;

	/** 投资项目与贷款人关系 */
	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;

	@Autowired
	private CapitalAccountManager capitalAccountManager;

	@Autowired
	private AccountMessageManager accountMessageManager;

	@Autowired
	private DictionaryUtil dictionaryUtil;

	@Autowired
	private EventMessageGateway eventMessageGateway;

	/**
	 * 手动投资
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param investAccountSequence
	 *            投资人用户账号
	 * @param amount
	 *            投标金额
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public ManualInvestReturnDto manualInvest(Long investmentSequence, Long investAccountSequence, BigDecimal amount) {

		ManualInvestReturnDto returnDto = new ManualInvestReturnDto();

		//取得项目的用户
		List<InvestmentAccountReference> investmentAccountReferenceList = investmentAccountReferenceRepository
				.findByInvestmentSequence(investmentSequence);
		if (CollectionUtil.isEmpty(investmentAccountReferenceList)) {
			returnDto.setResult(false);
			returnDto.setMsg("项目的贷款用户不存在");
			return returnDto;
		}

		Investment investment = investmentRepository.findOne(investmentSequence);

		if (investment.getInvestmentStatus().intValue() != InvestmentStatusEnum.ON_LINE.getCode().intValue()) {
			returnDto.setResult(false);
			returnDto.setMsg("选择的项目状态必须为招标中");
			return returnDto;
		}

		//取得投资人第三方支付账号
		ThirdPaymentAccount investThirdPaymentAccount = accountThirdRepository.findByAccountSequence(investAccountSequence);

		// 贷款人账户可能多个
		for (InvestmentAccountReference investmentAccountReference : investmentAccountReferenceList) {
			Long accountSequence = investmentAccountReference.getAccountSequence();
			//			ThirdPaymentAccount borrowThirdPayment = accountThirdRepository.findByAccountSequence(accountSequence);
			//2.交易开始
			//项目投标订单
			AccountOrder accountOrder = new AccountOrder();
			accountOrder.setTradeDate(new Date());
			accountOrder.setOrderStatus(OrderStatusEnum.FREEZE_APPLY.getCode());//订单状态
			accountOrder.setTradeType(TradeTypeEnum.INVEST_MANUAL.getCode());//交易类型
			accountOrder.setTradeAmount(amount);
			accountOrder.setPayAccountSequence(investAccountSequence);//付费用户P2P平台账号流水号
			accountOrder.setGatherAccountSequence(accountSequence);//收费用户P2P平台账号流水号
			accountOrder.setCommodityTablePrimaryKeyValue(investmentSequence);

			accountOrder.setTradeDescription(investment.getInvestmentName());
			accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FREEZE));
			accountOrder.setPayerThirdPaymentIdBalance(investThirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder.setGatherThirdPaymentIdBalance(BigDecimal.ZERO);
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder = accountOrderRepository.save(accountOrder);
			//生成用户订单状态历史
			AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.FREEZE_APPLY.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

			returnDto.setInvestOrderId(accountOrder.getCashFlowId());

			//生成冻结订单
			accountOrder = new AccountOrder();
			accountOrder.setTradeDate(new Date());
			accountOrder.setOrderStatus(OrderStatusEnum.NON_PAYMENT.getCode());//订单状态
			accountOrder.setTradeType(TradeTypeEnum.FREEZE.getCode());//交易类型
			accountOrder.setTradeAmount(amount);
			accountOrder.setPayAccountSequence(investAccountSequence);//付费用户P2P平台账号流水号
			accountOrder.setGatherAccountSequence(accountSequence);//收费用户P2P平台账号流水号
			accountOrder.setCommodityTablePrimaryKeyValue(investmentSequence);

			accountOrder.setTradeDescription(investment.getInvestmentName());
			accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FREEZE));
			accountOrder.setPayerThirdPaymentIdBalance(investThirdPaymentAccount.getThirdPaymentIdBalance());
			accountOrder.setGatherThirdPaymentIdBalance(BigDecimal.ZERO);
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder = accountOrderRepository.save(accountOrder);
			//生成用户订单状态历史
			accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.NON_PAYMENT.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

			returnDto.setFreezeOrderId(accountOrder.getCashFlowId());
			// 调用第三方支付接口，冻结投资人投资金额
		}

		returnDto.setResult(true);
		return returnDto;
	}

	/**
	 * 自助式自动投资策略
	 * 
	 * @param accountSequence
	 *            用户
	 * @param airPmaxAmt
	 *            单笔投资金额
	 * @param period
	 *            期次
	 * @param creditLevel
	 *            信用等级
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Response chkInvestStrategy(Long accountSequence, BigDecimal airPmaxAmt, String period, String creditLevel, AccountInvestRuleTypeEnum type) {
		//1.开通条件
		//取得第三方支付账号
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountSequence);

		if (thirdPaymentAccount == null) {
			//第三方支付账号未开通
			return new Response(ResponseConstants.CommonCode.RESPONSE_CODE_300, ResponseConstants.CommonMessage.RESPONSE_MSG_300, null);
		}

		//单笔投资金额必须为50整数倍，最低不少于50
		int intPerMaxAmount = airPmaxAmt.intValue();
		if (airPmaxAmt.compareTo(new BigDecimal(intPerMaxAmount)) != 0) {
			//不是整数
			return new Response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "单笔投资金额必须为50整数倍，最低不少于50", null);
		}

		if (intPerMaxAmount < 50 || intPerMaxAmount % 50 != 0) {
			//小于50或不是50的整数倍
			return new Response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "单笔投资金额必须为50整数倍，最低不少于50", null);
		}

		//2.开启自动投资 t_act_inv_rule
		AccountInvestRule accountInvestRule = accountInvestRuleRepository.findByAccountSequnece(accountSequence);
		if (accountInvestRule == null) {
			accountInvestRule = new AccountInvestRule();
		}

		accountInvestRule.setPeriod(period);
		accountInvestRule.setCreditLevel(creditLevel);
		accountInvestRule.setPerMaxAmount(airPmaxAmt);
		accountInvestRule.setStatus(AccountInvestRuleStatusEnum.OFF.getCode());
		accountInvestRule.setType(type.getCode());
		accountInvestRule.setAccountSequnece(accountSequence);
		accountInvestRule.setRecordCreateDate(new Date());// 入库时间	
		accountInvestRule.setAccountInvestmentRuleStartDate(new Date());//启动时间	
		accountInvestRuleRepository.save(accountInvestRule);

		return Response.successResponse();
	}

	/**
	 * 开启自动投资策略
	 * 
	 * @param accountSequence
	 *            用户流水号
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void openInvestStrategy(Long accountSequence) {
		//2.开启自动投资 t_act_inv_rule
		AccountInvestRule accountInvestRule = accountInvestRuleRepository.findByAccountSequnece(accountSequence);
		if (accountInvestRule == null) {
			accountInvestRule = new AccountInvestRule();
		}
		accountInvestRule.setStatus(AccountInvestRuleStatusEnum.ON.getCode());
		//accountInvestRule.setType(AccountInvestRuleTypeEnum.SELF_HELP.getCode());
		//accountInvestRule.setAccountSequnece(accountSequence);
		accountInvestRule.setAccountInvestmentRuleStartDate(new Date());//启动时间	
		accountInvestRuleRepository.save(accountInvestRule);
	}

	/**
	 * 关闭自动投资策略
	 * 
	 * @param accountSequence
	 *            用户
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public Response closeInvestStrategy(Long accountSequence) {

		AccountInvestRule accountInvestRule = accountInvestRuleRepository.findByAccountSequnece(accountSequence);

		if (accountInvestRule == null) {
			accountInvestRule = new AccountInvestRule();
		}
		accountInvestRule.setStatus(AccountInvestRuleStatusEnum.OFF.getCode());
		accountInvestRule.setAccountInvestmentRuleStartDate(new Date());//启动时间	
		accountInvestRuleRepository.save(accountInvestRule);

		//1.开通条件
		return Response.successResponse();
	}

	/**
	 * 后台进行自动投资
	 * 
	 * @param accountInvestRule
	 *            投资策略
	 * @return
	 */
	public boolean autoInvest() {
		//4.取得未满标的投资项目
		List<Investment> investmentList = investmentRepository.findAutoInvestmentOrderByInvestmentProgress(InvestmentStatusEnum.ON_LINE.getCode());

		if (CollectionUtil.isEmpty(investmentList)) {
			return false;
		}

		//6.自动投资开始
		for (Investment investment : investmentList) {
			long investmentSequence = investment.getInvestmentSequence();//投资项目流水号

			//取得项目的借款人
			List<InvestmentAccountReference> investmentAccountReferenceList = investmentAccountReferenceRepository
					.findByInvestmentSequence(investmentSequence);
			if (CollectionUtil.isEmpty(investmentAccountReferenceList)) {
				continue;
			}
			//TODO 以后贷款人账户可以多个
			Long borrowerAccountSequence = investmentAccountReferenceList.get(0).getAccountSequence();
			// 取得贷款人的信用等级
			Account account = accountRepository.findOne(borrowerAccountSequence);

			// 筛选自动投资用户
			List<AutoInvestDto> userlist = extractInvestUser(investment, account);

			for (AutoInvestDto userDto : userlist) {
				Investment realInvestment = investmentRepository.findOne(investmentSequence);

				if (realInvestment.getInvestmentStatus().intValue() != InvestmentStatusEnum.ON_LINE.getCode().intValue()) {
					break;
				}
				BigDecimal invSurplusAmt = realInvestment.getInvestmentSurplus();//项目剩余可投资金额
				BigDecimal userPerMaxAmt = BigDecimal.valueOf(userDto.getAirPmax());//单笔最大投资金额
				BigDecimal userSurplusAmt = BigDecimal.valueOf(userDto.getAvailableAmt());//用户可投资余额
				BigDecimal userInvAmt = BigDecimal.ZERO;//投资金额

				if (invSurplusAmt.compareTo(BigDecimal.ZERO) <= 0) {
					//项目余额0，投标结束
					break;
				}

				//计算用户的投资金额
				if (invSurplusAmt.compareTo(userPerMaxAmt) < 0) {
					//项目可投余额不足
					userInvAmt = invSurplusAmt;
				} else {
					if (userSurplusAmt.compareTo(userPerMaxAmt) < 0) {
						// 用户可用余额不足
						// 若用户可投资余额小于50
						if (userSurplusAmt.intValue() < 50) {
							continue;
						}
						// 50整数倍
						userInvAmt = BigDecimal.valueOf((userSurplusAmt.intValue() / 50) * 50);

					} else {
						userInvAmt = userPerMaxAmt;
					}
				}

				// 1.生成自动投资订单
				ThirdPayUserRequestDto requestDto = this.createAutoInvestOrder(investmentSequence, borrowerAccountSequence, userDto.gettAcctSeq()
						.longValue(), realInvestment, userInvAmt);

				// 2.调用第三方支付  自动投标接口
				PayResult payResult = null;
				try {
					payResult = capitalAccountManager.autoTender(investmentSequence, requestDto, userDto.gettAcctSeq().longValue());
				} catch (Exception e) {
					e.printStackTrace();
					payResult = new PayResult();
					payResult.setOrdId(requestDto.getOrderId());
					payResult.setFreezeId(requestDto.getFreezeOrdId());
					payResult.setResult(false);
					payResult.setStrTrxId("0");
					payResult.setFreezeTrxId("0");
					logger.error("自动投资汇付异常.项目流水号:" + investmentSequence + ",用户流水号:" + userDto.gettAcctSeq());
				}

				// 3.回调处理
				this.manualOrAutoInvestBack(payResult.getOrdId(), payResult.getFreezeId(), payResult.isResult(), payResult.getStrTrxId(),
						payResult.getFreezeTrxId(), payResult.getMessage());

				logger.info("自动投资处理结束，项目流水号: " + investmentSequence + ",用户流水号: " + userDto.gettAcctSeq() + ", 今日还款资金: " + userDto.getGptotal()
						+ " ,自动投资类型: " + (userDto.getAirType() == 0 ? "一键式" : "自助式") + ", 单笔投资金额: " + userDto.getAirPmax() + " ,可用余额: "
						+ userDto.getAvailableAmt() + " , 用户平台注册时间: " + DateUtil.formatDefault(userDto.getTacRdate()));
			}

		}

		return true;
	}

	/**
	 * 生成自动投资订单
	 * 
	 * @param investmentSequence
	 * @param borrowerAccountSequence
	 * @param userlist
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public ThirdPayUserRequestDto createAutoInvestOrder(long investmentSequence, Long borrowerAccountSequence, Long investAccountSequence,
			Investment investment, BigDecimal userInvAmt) {
		userInvAmt = userInvAmt.setScale(2);
		//自动投资用户p2p账户
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(investAccountSequence);
		BigDecimal thirdPaymentIdBalance = thirdPaymentAccount.getThirdPaymentIdBalance();

		AccountOrder accountOrder = new AccountOrder();
		accountOrder.setTradeDate(new Date());
		accountOrder.setOrderStatus(OrderStatusEnum.FREEZE_APPLY.getCode());//订单状态
		accountOrder.setTradeType(TradeTypeEnum.INVEST.getCode());//交易类型
		accountOrder.setTradeAmount(userInvAmt);
		accountOrder.setPayAccountSequence(investAccountSequence);//付费用户P2P平台账号流水号
		accountOrder.setGatherAccountSequence(borrowerAccountSequence);//收费用户P2P平台账号流水号
		accountOrder.setCommodityTablePrimaryKeyValue(investmentSequence);
		accountOrder.setTradeDescription(investment.getInvestmentName());
		accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FREEZE));
		accountOrder.setPayerThirdPaymentIdBalance(thirdPaymentIdBalance);
		accountOrder.setGatherThirdPaymentIdBalance(BigDecimal.ZERO);
		accountOrder.setOrderEditDate(new Date());
		accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
		accountOrder = accountOrderRepository.save(accountOrder);
		//生成用户订单状态历史
		AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
		accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
		accountOrderHistory.setOrderStatus(OrderStatusEnum.FREEZE_APPLY.getCode());
		accountOrderHistory.setOrderStatusChangeDate(new Date());
		accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

		//生成冻结订单
		AccountOrder freezeOrder = new AccountOrder();
		freezeOrder.setTradeDate(new Date());
		freezeOrder.setOrderStatus(OrderStatusEnum.NON_PAYMENT.getCode());//订单状态
		freezeOrder.setTradeType(TradeTypeEnum.FREEZE.getCode());//交易类型
		freezeOrder.setTradeAmount(userInvAmt);
		freezeOrder.setPayAccountSequence(investAccountSequence);//付费用户P2P平台账号流水号
		freezeOrder.setGatherAccountSequence(borrowerAccountSequence);//收费用户P2P平台账号流水号
		freezeOrder.setCommodityTablePrimaryKeyValue(investmentSequence);
		freezeOrder.setTradeDescription(investment.getInvestmentName());
		freezeOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FREEZE));
		freezeOrder.setPayerThirdPaymentIdBalance(thirdPaymentIdBalance);
		freezeOrder.setGatherThirdPaymentIdBalance(BigDecimal.ZERO);
		freezeOrder.setOrderEditDate(new Date());
		freezeOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
		freezeOrder = accountOrderRepository.save(freezeOrder);
		//生成用户订单状态历史
		accountOrderHistory = new AccountOrderHistory();
		accountOrderHistory.setAccountOrderSequence(freezeOrder.getAccountOrderSequence());
		accountOrderHistory.setOrderStatus(OrderStatusEnum.NON_PAYMENT.getCode());
		accountOrderHistory.setOrderStatusChangeDate(new Date());
		accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

		ThirdPayUserRequestDto requestDto = new ThirdPayUserRequestDto();

		//自动投资订单ID
		requestDto.setOrderId(String.valueOf(accountOrder.getCashFlowId()));
		//冻结订单ID
		requestDto.setFreezeOrdId(String.valueOf(freezeOrder.getCashFlowId()));
		requestDto.setOrdDate(DateUtil.getCurrentDate(DateUtil.DATE_FORMAT_SHORT));
		requestDto.setTransAmt(String.valueOf(userInvAmt));
		return requestDto;

	}

	/**
	 * 筛选投资人列表
	 * 
	 * @param investment
	 * @param tmpUserlist
	 * @param account
	 * @param userlist
	 */
	public List<AutoInvestDto> extractInvestUser(Investment investment, Account account) {
		List<AutoInvestDto> userlist = new ArrayList<AutoInvestDto>();

		//1.检测是否有用户开启了自动投资工具
		//2.对用户排序 单笔投资金额〉用户可用余额〉投资人p2p平台账户注册时间
		//3.账户可用余额-账户保留余额>=投资金额
		List<AutoInvestDto> tmpUserlist = dealRepository.getAutoInvestUserList();
		if (CollectionUtil.isEmpty(tmpUserlist)) {
			return userlist;
		}

		String investmentPeriod = String.valueOf(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode()
				+ DictionaryEnum.T_INV_INV_PERIOD.getCode() + investment.getInvestmentPeriod()));

		for (AutoInvestDto userDto : tmpUserlist) {

			//项目期限
			if (StringUtil.isEmpty(userDto.getAirPeriod()) || !userDto.getAirPeriod().contains(investmentPeriod)) {
				continue;
			}

			// 取得项目的等级
			Integer investmentLevel = investment.getInvestmentLevel();
			if (StringUtil.isEmpty(userDto.getAirClv()) || !userDto.getAirClv().contains(String.valueOf(investmentLevel))) {
				continue;
			}

			List<AccountOrder> accountOrderList = accountOrderRepository
					.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeTypeAndPayAccountSequence(investment.getInvestmentSequence(),
							OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), userDto.gettAcctSeq().longValue());
			if (!CollectionUtil.isEmpty(accountOrderList)) {
				// 投资人已对此项目进行投资
				continue;
			}

			userlist.add(userDto);
		}

		return userlist;
	}

	/**
	 * 手自动投资回调处理
	 * 
	 * @param accountInvestRule
	 *            投资策略
	 * @return
	 */
	public synchronized void manualOrAutoInvestBack(String investOrderId, String freezeOrderId, boolean isSuccess, String strTrxId,
			String strFreezeTrxId, String msg) {
		List<AccountOrder> investAccountOrderList = accountOrderRepository.findByCashFlowId(investOrderId);
		List<AccountOrder> freezeAccountOrderList = accountOrderRepository.findByCashFlowId(freezeOrderId);

		AccountOrder investAccountOrder = investAccountOrderList.get(0);
		AccountOrder freezeAccountOrder = freezeAccountOrderList.get(0);

		Integer orderStatus = investAccountOrder.getOrderStatus();
		if (orderStatus.intValue() == OrderStatusEnum.FREEZE_APPLY.getCode().intValue()) {
			// 申请冻结
			if (isSuccess) {
				exeSuccess(investAccountOrder, freezeAccountOrder, strTrxId, strFreezeTrxId);
			} else {
				// 更新用户订单的订单状态-〉失败
				exeFailure(msg, investAccountOrder, freezeAccountOrder);
			}
		} else if (orderStatus.intValue() == OrderStatusEnum.FREEZE_SUCCESS.getCode().intValue()) {
			//冻结成功
		} else if (orderStatus.intValue() == OrderStatusEnum.FREEZE_FALIUE.getCode().intValue()) {
			//冻结失败
			if (isSuccess) {
				exeSuccess(investAccountOrder, freezeAccountOrder, strTrxId, strFreezeTrxId);
			}
		}
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void exeFailure(String msg, AccountOrder investAccountOrder, AccountOrder freezeAccountOrder) {
		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(investAccountOrder.getPayAccountSequence());
		BigDecimal forzenAmt = accountOrderRepository.findFreezeApplySuccessAmt(investAccountOrder.getPayAccountSequence(),
				OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
		BigDecimal balanceAmt = thirdPaymentAccount.getThirdPaymentIdBalance().subtract(forzenAmt);

		investAccountOrder.setOrderStatus(OrderStatusEnum.FREEZE_FALIUE.getCode());
		investAccountOrder.setTradeComment(msg);
		investAccountOrder.setOrderEditDate(new Date());
		investAccountOrder.setPayerThirdPaymentIdBalance(balanceAmt);
		accountOrderRepository.save(investAccountOrder);
		// 生成用户订单状态历史
		AccountOrderHistory entity = new AccountOrderHistory();
		entity.setAccountOrderSequence(investAccountOrder.getAccountOrderSequence());
		entity.setOrderStatus(OrderStatusEnum.FREEZE_FALIUE.getCode());
		entity.setOrderStatusChangeDate(new Date());
		accountOrderHistoryRepository.save(entity);

		//冻结订单
		freezeAccountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
		freezeAccountOrder.setTradeComment(msg);
		freezeAccountOrder.setOrderEditDate(new Date());
		freezeAccountOrder.setPayerThirdPaymentIdBalance(balanceAmt);
		accountOrderRepository.save(freezeAccountOrder);
		//生成用户订单状态历史
		entity = new AccountOrderHistory();
		entity.setAccountOrderSequence(freezeAccountOrder.getAccountOrderSequence());
		entity.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
		entity.setOrderStatusChangeDate(new Date());
		accountOrderHistoryRepository.save(entity);
	}

	/**
	 * @param investAccountOrder
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public void exeSuccess(AccountOrder investAccountOrder, AccountOrder freezeAccountOrder, String strTrxId, String strFreezeTrxId) {
		// 投资项目 剩余可投资金额不足，汇付 是不是处理？ check 投资项目 剩余可投资金额 >=投资人投资金额
		Investment investment = investmentRepository.findOne(investAccountOrder.getCommodityTablePrimaryKeyValue());
		if (investAccountOrder.getTradeAmount().compareTo(investment.getInvestmentSurplus()) > 0) {
			//交易失败
			investAccountOrder.setOrderStatus(OrderStatusEnum.FREEZE_FALIUE.getCode());
			investAccountOrder.setOrderEditDate(new Date());
			investAccountOrder.setTrxId(new BigDecimal(strFreezeTrxId));
			investAccountOrder.setTradeComment("项目余额不足");
			accountOrderRepository.save(investAccountOrder);
			//生成用户订单状态历史
			AccountOrderHistory entity = new AccountOrderHistory();
			entity.setAccountOrderSequence(investAccountOrder.getAccountOrderSequence());
			entity.setOrderStatus(OrderStatusEnum.FREEZE_FALIUE.getCode());
			entity.setOrderStatusChangeDate(new Date());
			accountOrderHistoryRepository.save(entity);

			//冻结订单
			freezeAccountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
			freezeAccountOrder.setOrderEditDate(new Date());
			freezeAccountOrder.setTrxId(new BigDecimal(strTrxId));
			accountOrderRepository.save(freezeAccountOrder);
			//生成用户订单状态历史
			entity = new AccountOrderHistory();
			entity.setAccountOrderSequence(freezeAccountOrder.getAccountOrderSequence());
			entity.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
			entity.setOrderStatusChangeDate(new Date());
			accountOrderHistoryRepository.save(entity);

			// 解冻投资人投资金额
			//生成流标订单
			AccountOrder accountOrder = new AccountOrder();
			accountOrder.setTradeDate(new Date());
			accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
			accountOrder.setTradeType(TradeTypeEnum.UNFREEZE.getCode());//交易类型
			accountOrder.setTradeAmount(investAccountOrder.getTradeAmount());
			accountOrder.setPayAccountSequence(investAccountOrder.getPayAccountSequence());//付费用户P2P平台账号流水号
			accountOrder.setGatherAccountSequence(investAccountOrder.getGatherAccountSequence());//收费用户P2P平台账号流水号
			accountOrder.setCommodityTablePrimaryKeyValue(investAccountOrder.getCommodityTablePrimaryKeyValue());
			accountOrder.setTradeDescription(investAccountOrder.getTradeDescription());
			accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FREEZE));
			accountOrder.setPayerThirdPaymentIdBalance(investAccountOrder.getPayerThirdPaymentIdBalance());
			accountOrder.setGatherThirdPaymentIdBalance(BigDecimal.ZERO);
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder = accountOrderRepository.save(accountOrder);

			// 生成用户订单状态历史
			AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

			ThirdPaymentAccount fromThirdPaymentId = accountThirdRepository.findByAccountSequence(investAccountOrder.getPayAccountSequence());//投资人
			UnForzenCapitalDto forzenCapitalDto = new UnForzenCapitalDto();
			forzenCapitalDto.setOrig(fromThirdPaymentId);
			forzenCapitalDto.setPrice(investAccountOrder.getTradeAmount());
			forzenCapitalDto.setOrderId(accountOrder.getCashFlowId());
			forzenCapitalDto.setInvestmentSequence(investAccountOrder.getCommodityTablePrimaryKeyValue());
			forzenCapitalDto.setTrxId(strFreezeTrxId);//本平台交易唯一标识 冻结订单ID

			List<UnForzenCapitalDto> unForzenList = new ArrayList<UnForzenCapitalDto>();
			unForzenList.add(forzenCapitalDto);
			//调用第三方接口支付
			List<ManualInvestBackMessageDto> payResultList = unFreezeThirdPay(unForzenList);

			// 回调处理
			boolean isSuccess = true;
			for (ManualInvestBackMessageDto resultDto : payResultList) {
				if (!resultDto.isResult()) {
					isSuccess = false;
				}

				UnFreezeBack(resultDto.getOrderId(), resultDto.isResult(), resultDto.getMsg());
			}

			logger.info("项目余额不足投资失败>>>>项目流水号:" + investAccountOrder.getCommodityTablePrimaryKeyValue() + ",用户流水号:"
					+ investAccountOrder.getPayAccountSequence() + ",投资资金流水号:" + investAccountOrder.getCashFlowId() + ",冻结资金流水号:"
					+ freezeAccountOrder.getCashFlowId());

			return;
		}

		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(investAccountOrder.getPayAccountSequence());
		BigDecimal forzenAmt = accountOrderRepository.findFreezeApplySuccessAmt(investAccountOrder.getPayAccountSequence(),
				OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
		BigDecimal balanceAmt = thirdPaymentAccount.getThirdPaymentIdBalance().subtract(forzenAmt).subtract(investAccountOrder.getTradeAmount());
		//平台更新交易记录
		//更新用户订单的订单状态-〉成功
		investAccountOrder.setOrderStatus(OrderStatusEnum.FREEZE_SUCCESS.getCode());
		investAccountOrder.setOrderEditDate(new Date());
		investAccountOrder.setTrxId(new BigDecimal(strFreezeTrxId));
		investAccountOrder.setPayerThirdPaymentIdBalance(balanceAmt);
		//investAccountOrder.setGatherThirdPaymentIdBalance(thirdPaymentAccount.getThirdPaymentIdBalance());
		accountOrderRepository.save(investAccountOrder);
		//生成用户订单状态历史
		AccountOrderHistory entity = new AccountOrderHistory();
		entity.setAccountOrderSequence(investAccountOrder.getAccountOrderSequence());
		entity.setOrderStatus(OrderStatusEnum.FREEZE_SUCCESS.getCode());
		entity.setOrderStatusChangeDate(new Date());
		accountOrderHistoryRepository.save(entity);

		//冻结
		freezeAccountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
		freezeAccountOrder.setTrxId(new BigDecimal(strTrxId));
		freezeAccountOrder.setOrderEditDate(new Date());
		freezeAccountOrder.setPayerThirdPaymentIdBalance(balanceAmt);
		accountOrderRepository.save(freezeAccountOrder);

		AccountOrderHistory accountOrderHistorySuccess = new AccountOrderHistory();
		accountOrderHistorySuccess.setAccountOrderSequence(freezeAccountOrder.getAccountOrderSequence());
		accountOrderHistorySuccess.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
		accountOrderHistorySuccess.setOrderStatusChangeDate(new Date());
		accountOrderHistoryRepository.save(accountOrderHistorySuccess);

		//更新投资项目 （t_inv）
		// 判断投资是否满额 
		investment.setInvestmentSurplus(investment.getInvestmentSurplus().subtract(investAccountOrder.getTradeAmount())); //剩余可投资金额
		if (BigDecimal.ZERO.compareTo(investment.getInvestmentSurplus()) == 0) {
			investment.setInvestmentStatus(InvestmentStatusEnum.TENDER_FINISH.getCode());
			investment.setInvestmentFillDate(new Date());
		}
		investment.setInvestmentJoinedCount(investment.getInvestmentJoinedCount() + 1);//已参与投资人数
		BigDecimal tmp = investment.getInvestmentTotal().subtract(investment.getInvestmentSurplus());//已投资总金额
		investment.setInvestmentProgress(tmp.divide(investment.getInvestmentTotal(), 6, BigDecimal.ROUND_DOWN));//招标进度
		investmentRepository.save(investment);

		logger.info("项目投资成功>>>>项目流水号:" + investAccountOrder.getCommodityTablePrimaryKeyValue() + ",用户流水号:"
				+ investAccountOrder.getPayAccountSequence() + ",投资资金流水号:" + investAccountOrder.getCashFlowId() + ",冻结资金流水号:"
				+ freezeAccountOrder.getCashFlowId());
	}

	/**
	 * 流标处理
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return
	 */
	public boolean investFailure(Long investmentSequence) {

		//取得满标项目贷款人信息
		Investment investment = investmentRepository.findOne(investmentSequence);
		if (investment == null) {
			return false;
		}
		List<UnForzenCapitalDto> unForzenList = new ArrayList<UnForzenCapitalDto>();

		//检查是否已经有订单
		List<AccountOrder> existList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndTradeType(investmentSequence,
				TradeTypeEnum.FAILURE_TENDERS.getCode());
		if (!CollectionUtil.isEmpty(existList)) {
			for (AccountOrder dto : existList) {
				//查询是否有失败订单
				if (OrderStatusEnum.PAYMENT_FALIUE.getCode().compareTo(dto.getOrderStatus()) == 0) {
					ThirdPaymentAccount fromThirdPaymentId = accountThirdRepository.findByAccountSequence(dto.getPayAccountSequence());//投资人
					if (fromThirdPaymentId == null) {
						continue;
					}
					//失败场合,加入补单列表
					UnForzenCapitalDto forzenCapitalDto = new UnForzenCapitalDto();
					forzenCapitalDto.setOrig(fromThirdPaymentId);
					forzenCapitalDto.setPrice(dto.getTradeAmount());
					forzenCapitalDto.setOrderId(dto.getCashFlowId());
					forzenCapitalDto.setInvestmentSequence(investmentSequence);
					forzenCapitalDto.setTrxId(String.valueOf(dto.getTrxId()));//本平台交易唯一标识 冻结订单ID

					unForzenList.add(forzenCapitalDto);
				}
			}
		} else {
			//新生成订单
			//取得投资人订单信息
			List<AccountOrder> accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
					investmentSequence, OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(),
					TradeTypeEnum.INVEST_MANUAL.getCode());

			if (CollectionUtil.isEmpty(accountOrderList)) {
				//没有投标的项目直接流标
				if (InvestmentStatusEnum.ON_LINE.getCode().intValue() == investment.getInvestmentStatus().intValue()) {
					//项目流标成功
					investment.setInvestmentStatus(InvestmentStatusEnum.TENDER_FAIL.getCode());
					investment.setInvestmentLostDate(new Date());
					investmentRepository.save(investment);
				}
				return false;
			}

			//1.生成流标订单
			unForzenList = createUnFreezeOrder(investmentSequence, accountOrderList, investment);

		}

		if (CollectionUtil.isEmpty(unForzenList)) {
			return false;
		}

		//2.调用第三方支付 解冻订单
		List<ManualInvestBackMessageDto> payResultList = unFreezeThirdPay(unForzenList);

		//3.回调处理
		boolean isSuccess = true;
		for (ManualInvestBackMessageDto resultDto : payResultList) {
			if (!resultDto.isResult()) {
				isSuccess = false;
			}

			UnFreezeBack(resultDto.getOrderId(), resultDto.isResult(), resultDto.getMsg());
		}

		return isSuccess;
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<ManualInvestBackMessageDto> unFreezeThirdPay(List<UnForzenCapitalDto> unForzenList) {
		List<ManualInvestBackMessageDto> payResultList = new ArrayList<ManualInvestBackMessageDto>();
		ManualInvestBackMessageDto resultDto = null;
		for (UnForzenCapitalDto dto : unForzenList) {

			try {
				//调用第三方接口支付
				resultDto = capitalAccountManager.disForzenCapital(dto);
			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("订单号：" + dto.getOrderId() + "，调用汇付解冻出错");
				resultDto.setResult(false);
				resultDto.setMsg(e.getMessage());
				resultDto.setOrderId(dto.getOrderId());
				resultDto.setInvestmentSequence(dto.getInvestmentSequence());
			}

			payResultList.add(resultDto);
		}

		return payResultList;
	}

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public List<UnForzenCapitalDto> createUnFreezeOrder(Long investmentSequence, List<AccountOrder> accountOrderList, Investment investment) {
		List<UnForzenCapitalDto> unForzenList = new ArrayList<UnForzenCapitalDto>();
		for (AccountOrder dto : accountOrderList) {

			ThirdPaymentAccount fromThirdPaymentId = accountThirdRepository.findByAccountSequence(dto.getPayAccountSequence());//投资人

			if (fromThirdPaymentId == null) {
				return null;
			}
			//生成流标订单
			AccountOrder accountOrder = new AccountOrder();
			accountOrder.setTradeDate(new Date());
			accountOrder.setOrderStatus(OrderStatusEnum.PAYING.getCode());//订单状态
			accountOrder.setTradeType(TradeTypeEnum.FAILURE_TENDERS.getCode());//交易类型
			accountOrder.setTradeAmount(dto.getTradeAmount());
			accountOrder.setCommodityTablePrimaryKeyValue(dto.getCommodityTablePrimaryKeyValue());
			accountOrder.setPayAccountSequence(dto.getPayAccountSequence());//付费用户P2P平台账号流水号
			accountOrder.setGatherAccountSequence(dto.getGatherAccountSequence());//收费用户P2P平台账号流水号

			accountOrder.setCashFlowId(P2pUtil.generate20Random(OrderIDRuleEnum.FREEZE));
			accountOrder.setPayerThirdPaymentIdBalance(fromThirdPaymentId.getThirdPaymentIdBalance());
			accountOrder.setGatherThirdPaymentIdBalance(new BigDecimal(0));
			accountOrder.setTrxId(dto.getTrxId());
			accountOrder.setOrderEditDate(new Date());
			accountOrder.setTradeDescription(investment.getInvestmentName());
			accountOrder.setRecordStatus(RecStatusEnum.UNTREATED.getCode());
			accountOrder = accountOrderRepository.save(accountOrder);

			// 生成用户订单状态历史
			AccountOrderHistory accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.PAYING.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

			UnForzenCapitalDto forzenCapitalDto = new UnForzenCapitalDto();
			forzenCapitalDto.setOrig(fromThirdPaymentId);
			forzenCapitalDto.setPrice(dto.getTradeAmount());
			forzenCapitalDto.setOrderId(accountOrder.getCashFlowId());
			forzenCapitalDto.setInvestmentSequence(investmentSequence);
			forzenCapitalDto.setTrxId(String.valueOf(dto.getTrxId()));//本平台交易唯一标识 冻结订单ID

			unForzenList.add(forzenCapitalDto);
		}

		return unForzenList;
	}

	/**
	 * 解冻回调处理
	 * 
	 * @param orderId
	 *            订单流水号
	 * @return
	 */
	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	public synchronized void UnFreezeBack(String orderId, boolean isResult, String tradeDescription) {
		//流标解冻完成后处理
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCashFlowId(orderId);

		AccountOrder accountOrder = accountOrderList.get(0);
		Integer orderStatus = accountOrder.getOrderStatus();
		Integer tradeType = accountOrder.getTradeType();
		Long investmentSequence = accountOrder.getCommodityTablePrimaryKeyValue();

		ThirdPaymentAccount thirdPaymentAccount = accountThirdRepository.findByAccountSequence(accountOrder.getPayAccountSequence());
		BigDecimal forzenAmt = accountOrderRepository.findFreezeApplySuccessAmt(accountOrder.getPayAccountSequence(),
				OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
		BigDecimal balanceAmt = thirdPaymentAccount.getThirdPaymentIdBalance().subtract(forzenAmt);

		if (orderStatus.intValue() == OrderStatusEnum.PAYING.getCode().intValue()) {
			//申请解冻
			if (isResult) {
				//平台更新交易记录
				//更新用户订单的订单状态-〉成功
				accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
				accountOrder.setOrderEditDate(new Date());
				accountOrder.setPayerThirdPaymentIdBalance(balanceAmt);
				accountOrderRepository.save(accountOrder);
				//生成用户订单状态历史
				AccountOrderHistory entity = new AccountOrderHistory();
				entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
				entity.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
				entity.setOrderStatusChangeDate(new Date());
				accountOrderHistoryRepository.save(entity);

			} else {
				//更新用户订单的订单状态-〉失败
				accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
				accountOrder.setTradeComment(tradeDescription);
				accountOrder.setOrderEditDate(new Date());
				accountOrder.setPayerThirdPaymentIdBalance(balanceAmt);
				accountOrderRepository.save(accountOrder);
				//生成用户订单状态历史
				AccountOrderHistory entity = new AccountOrderHistory();
				entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
				entity.setOrderStatus(OrderStatusEnum.PAYMENT_FALIUE.getCode());
				entity.setOrderStatusChangeDate(new Date());
				accountOrderHistoryRepository.save(entity);
			}

		} else if (orderStatus.intValue() == OrderStatusEnum.PAYMENT_SUCCESS.getCode().intValue()) {
			//解冻成功
		} else if (orderStatus.intValue() == OrderStatusEnum.PAYMENT_FALIUE.getCode().intValue()) {
			//解冻失败
			if (isResult) {
				//平台更新交易记录
				//更新用户订单的订单状态-〉成功
				accountOrder.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
				accountOrder.setOrderEditDate(new Date());
				accountOrder.setPayerThirdPaymentIdBalance(balanceAmt);
				accountOrderRepository.save(accountOrder);
				//生成用户订单状态历史
				AccountOrderHistory entity = new AccountOrderHistory();
				entity.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
				entity.setOrderStatus(OrderStatusEnum.PAYMENT_SUCCESS.getCode());
				entity.setOrderStatusChangeDate(new Date());
				accountOrderHistoryRepository.save(entity);
			}
		}

		if (TradeTypeEnum.FAILURE_TENDERS.getCode().intValue() != tradeType.intValue()) {
			return;
		}

		// 检查查询是否存在申请解冻的订单
		accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(investmentSequence,
				OrderStatusEnum.PAYING.getCode(), TradeTypeEnum.FAILURE_TENDERS.getCode());

		if (CollectionUtil.isEmpty(accountOrderList)) {
			//检查是否有付款失败订单
			List<AccountOrder> list = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(investmentSequence,
					OrderStatusEnum.PAYMENT_FALIUE.getCode(), TradeTypeEnum.FAILURE_TENDERS.getCode());

			if (CollectionUtil.isEmpty(list)) {
				Investment investment = investmentRepository.findOne(investmentSequence);
				//如果项目流标中
				if (InvestmentStatusEnum.ON_LINE.getCode().intValue() == investment.getInvestmentStatus().intValue()) {
					//项目流标成功
					investment.setInvestmentStatus(InvestmentStatusEnum.TENDER_FAIL.getCode());
					investment.setInvestmentLostDate(new Date());
					investmentRepository.save(investment);
					// 解冻订单
					List<Long> toUsers = this.unFreezOrderByInvestmentSequence(investmentSequence);
					// 站内信
					AccountMessageTemplateData data = new AccountMessageTemplateData();
					data.setProjectName(investment.getInvestmentName());
					data.setProjectIdURL("#/investitem/" + investment.getInvestmentSequence());

					AlertDto alertDto = new AlertDto();
					alertDto.setAccountMessageTemplateData(data);
					alertDto.setAccountMessageTemplateEnum(AccountMessageTemplateEnum.LIU_BIAO_TONG_ZHI);
					alertDto.setToUser(toUsers);

					try {
						eventMessageGateway.publishEvent(alertDto);
					} catch (Exception e) {
						e.printStackTrace();
						logger.debug("项目流水号：" + investmentSequence + "，流标发送站内信失败");
					}

				}
			}
		}
	}

	/**
	 * 解冻订单。
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return
	 */
	private List<Long> unFreezOrderByInvestmentSequence(Long investmentSequence) {
		List<Long> toUsers = new ArrayList<Long>();
		List<AccountOrder> accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
				investmentSequence, OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());

		for (AccountOrder accountOrder : accountOrderList) {

			accountOrder.setOrderStatus(OrderStatusEnum.UNFREEZE_SUCCESS.getCode());
			accountOrder.setOrderEditDate(new Date());
			accountOrderRepository.save(accountOrder);

			//获得项目的用户
			toUsers.add(accountOrder.getPayAccountSequence());
		}

		return toUsers;

	}
}
