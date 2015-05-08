package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.entity.AccountInvestment;
import com.vcredit.jdev.p2p.entity.AccountOrder;
import com.vcredit.jdev.p2p.entity.AccountOrderHistory;
import com.vcredit.jdev.p2p.enums.AccountInvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.OrderStatusEnum;
import com.vcredit.jdev.p2p.enums.TradeTypeEnum;
import com.vcredit.jdev.p2p.repository.AccountClaimRepository;
import com.vcredit.jdev.p2p.repository.AccountInvestmentRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderHistoryRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherRecordRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.util.DateUtil;

/**
 * TODO 用户多期债权转让 。
 * 
 * @author zhuqiu
 *
 */
@Component
public class BondPackageTransferManager {

	/** 用户获得的项目 */
	@Autowired
	private AccountInvestmentRepository accountInvestmentRepository;

	/** 手动投资 */
	@Autowired
	private BondPackageInvestManager bondPackageInvestManager;

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;

	/** 用户订单状态历史 */
	@Autowired
	private AccountOrderHistoryRepository accountOrderHistoryRepository;

	@Autowired
	private ClaimGatherRecordRepository claimGatherRecordRepository;

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private InvestmentRepository investmentRepository;

	/** 用户获得债权 */
	@Autowired
	private AccountClaimRepository accountClaimRepository;

	/**
	 * 出让债权
	 * 
	 * @param accountInvestmentSequence
	 *            用户获得的项目流水号
	 * @return
	 */
	@Transactional
	public boolean BondTransfer(Long accountInvestmentSequence) {

		//1.取得用户获得的项目
		AccountInvestment entity = accountInvestmentRepository.getOne(accountInvestmentSequence);

		//2.出让条件
		//债权必须处于还款中
		if (AccountInvestmentStatusEnum.REPAY_NORMAL.getCode().compareTo(entity.getAccountInvestmentStatus()) != 0) {
			return true;
		}

		//债权持有天数必须大于等于60
		if (DateUtil.diff(entity.getAccountInvestmentIssueDate(), new Date()) / (1000 * 60 * 60 * 24) < 60) {
			return true;
		}

		//TODO 规则引擎？债权转让日与下个计划应还款日之间〉=8
		//		if (){
		//			
		//		}

		//3.更新用户债权状态
		entity.setAccountInvestmentStatus(AccountInvestmentStatusEnum.TANSFERING.getCode());
		entity.setAccountInvestmentEndDate(DateUtil.add(new Date(), 7 * 24 * 60 * 60));
		accountInvestmentRepository.save(entity);
		//4.转让成功
		return true;
	}

	/**
	 * 购买出让债权
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param accountInvestmentSequence
	 *            获得投资项目流水号
	 * @param accountThirdPartyPaymentId
	 *            投资人用户账号
	 * @param toThirdPartyPaymentId
	 *            转让人账号
	 * @param amount
	 *            投标金额
	 * @return
	 */
	@Transactional
	public boolean BondPurchase(Long investmentSequence, Long accountInvestmentSequence, Long accountThirdPartyPaymentId, Long toThirdPartyPaymentId,
			BigDecimal amount) {
		AccountOrderHistory accountOrderHistory = null;

		//1.转让人获得项目
		AccountInvestment entity = accountInvestmentRepository.getOne(accountInvestmentSequence);
		//转让项目状态必须为“转让中”
		if (AccountInvestmentStatusEnum.TANSFERING.getCode().compareTo(entity.getAccountInvestmentStatus()) != 0) {
			//项目已经转让，转让失败
			return false;
		}

		//实际收本金
		BigDecimal sumActualPrincipal = dealRepository.getSumActualPrincipal(accountInvestmentSequence);
		//转让项目的剩余本金=获得项目的投资额-收款记录的实际收本金
		BigDecimal remainPrincipal = entity.getAccountInvestmentQuota().subtract(sumActualPrincipal);
		//TODO 债权转让手续费
		//转让金额=剩余本金-债权转让手续费

		//2.交易开始
		//平台新增交易记录 （t_act_order t_act_oh）
		AccountOrder accountOrder = new AccountOrder();
		accountOrder.setTradeDate(new Date());
		accountOrder.setOrderStatus(OrderStatusEnum.FREEZE_APPLY.getCode());//订单状态
		accountOrder.setTradeType(TradeTypeEnum.INVEST.getCode());//交易类型
		accountOrder.setTradeAmount(amount);

		accountOrder.setPayAccountSequence(accountThirdPartyPaymentId);//付费用户P2P平台账号流水号	
		accountOrder.setGatherAccountSequence(toThirdPartyPaymentId);
		accountOrder.setCommodityTablePrimaryKeyValue(investmentSequence);
		accountOrder = accountOrderRepository.save(accountOrder);
		//生成用户订单状态历史
		accountOrderHistory = new AccountOrderHistory();
		accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
		accountOrderHistory.setOrderStatus(OrderStatusEnum.FREEZE_APPLY.getCode());
		accountOrderHistory.setOrderStatusChangeDate(new Date());
		accountOrderHistory = accountOrderHistoryRepository.save(accountOrderHistory);

		boolean isTradeSucc = true;
		//TODO 调用第三方支付接口，冻结投资人投资金额
		//如果余额不足或第三方支付反馈失败
		if (!isTradeSucc) {
			//平台更新交易记录（t_act_order t_act_oh）
			//更新用户订单的订单状态-〉冻结失败 （t_act_order ）
			accountOrder.setOrderStatus(OrderStatusEnum.FREEZE_FALIUE.getCode());
			accountOrderRepository.save(accountOrder);
			//生成用户订单状态历史
			accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.FREEZE_FALIUE.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistoryRepository.save(accountOrderHistory);
			return false;
		}

		//冻结成功
		//check 转让项目的状态
		entity = accountInvestmentRepository.getOne(accountInvestmentSequence);
		if (AccountInvestmentStatusEnum.TANSFERING.getCode().compareTo(entity.getAccountInvestmentStatus()) != 0) {
			//项目已经转让，转让失败
			//交易失败，平台更新交易记录（t_act_order t_act_oh）
			accountOrder.setOrderStatus(OrderStatusEnum.FREEZE_FALIUE.getCode());
			accountOrderRepository.save(accountOrder);
			//生成用户订单状态历史
			accountOrderHistory = new AccountOrderHistory();
			accountOrderHistory.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
			accountOrderHistory.setOrderStatus(OrderStatusEnum.FREEZE_FALIUE.getCode());
			accountOrderHistory.setOrderStatusChangeDate(new Date());
			accountOrderHistoryRepository.save(accountOrderHistory);
			//TODO 调用第三方支付接口，解冻投资人投资金额
			return false;
		}

		//3.交易成功
		// 更新交易记录（t_act_order t_act_oh）
		accountOrder.setOrderStatus(OrderStatusEnum.FREEZE_SUCCESS.getCode());
		accountOrderRepository.save(accountOrder);
		// 新增交易历史
		AccountOrderHistory accountOrderHistorySuccess = new AccountOrderHistory();
		accountOrderHistorySuccess.setAccountOrderSequence(accountOrder.getAccountOrderSequence());
		accountOrderHistorySuccess.setOrderStatus(OrderStatusEnum.FREEZE_SUCCESS.getCode());
		accountOrderHistorySuccess.setOrderStatusChangeDate(new Date());
		accountOrderHistoryRepository.save(accountOrderHistorySuccess);

		//更新用户获得的项目 （转让人）
		entity.setAccountInvestmentIssueDate(new Date());
		entity.setAccountInvestmentStatus(AccountInvestmentStatusEnum.TANSFER_SUCCESS.getCode());
		accountInvestmentRepository.save(entity);

		//新增用户获得的项目 （投资人）
		AccountInvestment entityInvestment = new AccountInvestment();
		entityInvestment.setInvestmentSequence(investmentSequence);
		entityInvestment.setAccountInvestmentPayedPeriod(entity.getAccountInvestmentPayedPeriod());
		entityInvestment.setAccountInvestmentQuota(entity.getAccountInvestmentQuota());
		entityInvestment.setAccountInvestmentIssueDate(new Date());
		entityInvestment.setAccountInvestmentStatus(AccountInvestmentStatusEnum.REPAY_NORMAL.getCode());
		entityInvestment.setAccountSequence(accountThirdPartyPaymentId);
		accountInvestmentRepository.save(entityInvestment);

		//新增投资人的获得的债权
		dealRepository.insertSelectActClm(entityInvestment.getAccountInvestmentSequence(), entity.getAccountInvestmentSequence());
		//更新转让人的债权：还款中->已转让
		dealRepository.updateTransferActClm(entity.getAccountInvestmentSequence());

		//通知用户投资成功
		return true;
	}
}
