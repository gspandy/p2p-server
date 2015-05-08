package com.vcredit.jdev.p2p.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.NoRepositoryBean;

import com.vcredit.jdev.p2p.dto.AutoInvestDto;
import com.vcredit.jdev.p2p.dto.ClaimGatherAmtDto;
import com.vcredit.jdev.p2p.dto.DealDetailListDto;
import com.vcredit.jdev.p2p.entity.ClaimGatherPlan;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.enums.AccountInvestmentStatusEnum;

/**
 * 交易模块Repository
 * 
 */
@NoRepositoryBean
public interface DealRepository {

	/**
	 * 检索转让人债权，并把检索结果作为受让人获得的债权插入债权表
	 * <p>
	 * 用户开启了自动投资工具 <br>
	 * 对用户排序 单笔投资金额〉用户可用余额〉投资人p2p平台账户注册时间 <br>
	 * 账户可用余额-账户保留余额>=投资金额<br>
	 * 
	 * @return 满足自动投资条件的用户List
	 */
	public List<AutoInvestDto> getAutoInvestUserList();

	/**
	 * 收款记录的实际收本金
	 * 
	 * @return 收本金
	 */
	public BigDecimal getSumActualPrincipal(Long actInvSeq);

	/**
	 * 检索转让人债权，并把检索结果作为受让人获得的债权插入债权表
	 * 
	 * @param investmentSequence
	 *            投资人获得的项目流水号
	 * @param transferSequence
	 *            转让人获得的项目流水号
	 * @return 件数
	 */
	public int insertSelectActClm(Long investmentSequence, Long transferSequence);

	/**
	 * 更新转让人获得的债权 - 债权状态-〉已转让
	 * 
	 * @param transferSequence
	 *            转让人获得的项目流水号
	 * @return 件数
	 */
	public int updateTransferActClm(Long transferSequence);

	/**
	 * 逾期还款时，根据项目流水号，更新【用户获得的债权】- 债权状态->4(逾期)，债权状态变化时间,逾期天数+1
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param periodOfTime
	 *            项目期次
	 * @return 件数
	 */
	@Transactional
	public int updateOverdueActClm(Long investmentSequence, Integer periodOfTime, int delayDays);

	@Transactional
	public int updateOverdueActInv(Long investmentSequence, AccountInvestmentStatusEnum accountInvestmentStatusEnum);

	@Transactional
	public int updateActInv(Long investmentSequence, Integer periodOfTime);

	/**
	 * 根据项目流水号，期次，查询该项目期次的投资用户债权收款计划
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param periodOfTime
	 *            项目期次
	 * @return {@link ClaimGatherPlan}件数
	 */
	public List<ClaimGatherPlan> findClaimGatherPlan(Long investmentSequence, Integer periodOfTime);

	/**
	 * 查询归集逾期回购的项目
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param periodOfTime
	 *            项目期次
	 * @return {@link ClaimGatherPlan}件数
	 */
	public List<Investment> findRepurchaseList();

	/**
	 * 根据用户流水号查询用户第三方支付账号
	 * 
	 * @param accountSequence
	 *            用户流水号
	 * @return 第三方支付账号
	 */
	public String findAccountThirdBySequence(Long accountSequence);

	public List<Investment> findDealList(Long accountSeq, Date startDate, Date endDate, Integer orderStat, Integer tradeType, Integer dateType);

	public List<DealDetailListDto> findDealDetialList(Long accountSeq, Date startDate, Date endDate, Integer orderStat, Integer tradeType,
			Integer dateType);

	/**
	 * 查询投资人获得的利息罚息总和
	 * 
	 * @param accountSequence
	 *            用户流水号
	 * @return {@link BigDecimal}
	 */
	public List<ClaimGatherAmtDto> findSumByGatherAccountSequence(Long accountSequence);

	public Date getSystemTime();
}