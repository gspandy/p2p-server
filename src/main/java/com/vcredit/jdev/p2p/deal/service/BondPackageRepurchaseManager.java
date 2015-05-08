package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.enums.ClaimPayPlanStatusEnum;
import com.vcredit.jdev.p2p.repository.AccountClaimRepository;
import com.vcredit.jdev.p2p.repository.AccountInvestmentRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderHistoryRepository;
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.ClaimGatherPlanRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;

/**
 * TODO 逾期后回购
 * 
 * @author zhuqiu
 *
 */
@Component
public class BondPackageRepurchaseManager {

	/** 债权还款计划 */
	@Autowired
	private ClaimPayPlanRepository claimPayPlanRepository;

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;

	/** 用户订单状态历史 */
	@Autowired
	private AccountOrderHistoryRepository accountOrderHistoryRepository;

	/** 用户获得的项目 */
	@Autowired
	private AccountInvestmentRepository accountInvestmentRepository;

	/** 用户获得的债权 */
	@Autowired
	private AccountClaimRepository accountClaimRepository;

	@Autowired
	private ClaimGatherPlanRepository claimGatherPlanRepository;

	@Autowired
	private DealRepository dealRepository;

	/**
	 * 查询归集逾期回购金额及清单
	 * 
	 * @return
	 */
	@Transactional
	public boolean searchRepurchaseList() {
		//取得垫付三次以上项目
		List<Investment> list = dealRepository.findRepurchaseList();

		for (Investment investment : list) {
			//取得项目未还债务
			List<ClaimPayPlan> claimPayPlanList = claimPayPlanRepository.findByInvestmentSequenceAndClaimPayPlanStatus(
					investment.getInvestmentSequence(), ClaimPayPlanStatusEnum.NOT_PAID.getCode());

		}
		//TODO
		return true;
	}

	/**
	 * 债权回购处理
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param accountThirdPartyPaymentId
	 *            投资人第三方支付账号
	 * @param amount
	 *            投标金额
	 * @return
	 */
	@Transactional
	public boolean bondRepurchase(Long investmentSequence, String accountThirdPartyPaymentId, BigDecimal amount) {

		//

		//通知用户投资成功
		return true;
	}

}
