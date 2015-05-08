package com.vcredit.jdev.p2p.deal.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO 提前清贷。
 * 
 * @author zhuqiu
 *
 */
@Component
public class BondPackagePayOffManager {

	/**
	 * 查询归集提前清贷金额及清单（管理平台）
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return
	 */
	@Transactional
	public boolean findBondPackagePayOffList(Long investmentSequence, String accountThirdPartyPaymentId, BigDecimal amount) {

		//通知用户投资成功
		return true;
	}

	/**
	 * 提前清贷处理
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return
	 */
	@Transactional
	public boolean bondPackagePayOff(Long investmentSequence, String accountThirdPartyPaymentId, BigDecimal amount) {

		//通知用户投资成功
		return true;
	}

}
