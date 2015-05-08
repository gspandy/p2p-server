package com.vcredit.jdev.p2p.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.AccountOrder;

public interface AccountOrderRepository extends JpaRepository<AccountOrder, Long> {

	/**
	 * 根据投资项目流水号，查询交易类型：2[放款]，订单状态：4[付款失败] 的订单
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link AccountOrder}
	 */
	public List<AccountOrder> findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(Long investmentSequence, Integer orderStatus,
			Integer tradeType);

	public List<AccountOrder> findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeTypeAndPayAccountSequence(Long investmentSequence,
			Integer orderStatus, Integer tradeType1, Long accountSequence);

	public List<AccountOrder> findByCommodityTablePrimaryKeyValueAndTradeType(Long investmentSequence, Integer tradeType);

	@Query(nativeQuery = true, value = "SELECT * from t_act_order where trade_ctpvalue =?1 and order_stat =?2 and (trade_type =?3 or trade_type =?4) order by trade_date desc ")
	public List<AccountOrder> findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(Long investmentSequence, Integer orderStatus,
			Integer tradeType1, Integer tradeType2);

	@Query(nativeQuery = true, value = "SELECT IFNULL(sum(trade_amount),0) from t_act_order where trade_ctpvalue =?1 and order_stat =?2 and (trade_type =?3 or trade_type =?4) and t_pacct_seq = ?5 ")
	public BigDecimal sumHasInvestedByInvestmentSequenceAndAccountSequence(Long investmentSequence, Integer orderStatus, Integer tradeType1,
			Integer tradeType2, Long accountSeq);

	/**
	 * 查询某用户是否投资过某项目
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link AccountOrder}
	 */
	@Query(nativeQuery = true, value = "SELECT * from t_act_order where trade_ctpvalue =?1 and order_stat =?2 and (trade_type =?3 or trade_type =?4) and t_pacct_seq = ?5 ")
	public List<AccountOrder> findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeTypeAndAccount(Long investmentSequence, Integer orderStatus,
			Integer tradeType1, Integer tradeType2, Long accountSequence);

	/**
	 * 根据用户流水号，用户申请中的订单（冻结成功的自动或手动投资）
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link AccountOrder}
	 */
	@Query(nativeQuery = true, value = "SELECT * from t_act_order where t_pacct_seq =?1 and order_stat in (?2,?3) and (trade_type =?4 or trade_type =?5) ")
	public List<AccountOrder> findFreezeApply(Long payAccountSequence, Integer orderStatus1, Integer orderStatus2, Integer tradeType1,
			Integer tradeType2);

	/**
	 * 根据用户流水号，查询用户冻结成功的金额（冻结成功的自动或手动投资）
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link AccountOrder}
	 */
	@Query(nativeQuery = true, value = "SELECT IFNULL(sum(trade_amount), 0) from t_act_order where t_pacct_seq =?1 and order_stat =?2 and (trade_type =?3 or trade_type =?4) ")
	public BigDecimal findFreezeApplySuccessAmt(Long payAccountSequence, Integer orderStatus, Integer tradeType1, Integer tradeType2);

	/**
	 * 根据用户流水号，查询用户冻结成功的项目数
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link Long}
	 */
	@Query(nativeQuery = true, value = "SELECT count(1) from t_act_order where t_pacct_seq =?1 and order_stat =?2 and (trade_type =?3 or trade_type =?4) ")
	public Long getCountFreezeApplySuccess(Long payAccountSequence, Integer orderStatus, Integer tradeType1, Integer tradeType2);

	/**
	 * 根据用户流水号，查询用户冻结成功的项目数
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link Long}
	 */
	@Query(nativeQuery = true, value = "SELECT count(1) from t_act_order where t_pacct_seq =?1 and (order_stat =?2 or order_stat =?3) and (trade_type =?4 or trade_type =?5) ")
	public Long getCountInvested(Long payAccountSequence, Integer orderStatus1, Integer orderStatus2, Integer tradeType1, Integer tradeType2);

	/**
	 * 根据订单号，查询订单
	 * 
	 * @param orderId
	 *            订单号
	 * @return {@link AccountOrder}
	 */
	public List<AccountOrder> findByCashFlowId(String orderId);

	/**
	 * 根据某期还款计划，查询是否有付款中或付款失败，的债权还款 、P2P平台账户管理服务、 风险备用金的订单
	 * 
	 * @param orderId
	 *            订单号
	 * @return 没有成功付款的订单
	 */
	@Query(nativeQuery = true, value = "SELECT count(1) from t_act_order where trade_ctpvalue =?1 and order_stat <> 3 and (trade_type =5 or trade_type =7 or trade_type =26) ")
	public Long getCountPayPlanNotSuccess(Long claimPayPlanSequence);

	/**
	 * 查询指定标的的投标记录订单。（包括冻结成功和解冻成功）
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @return 订单
	 */
	@Query(nativeQuery = true, value = "SELECT * from t_act_order where trade_ctpvalue =?1 and order_stat in(?2,?3) and (trade_type =?4 or trade_type =?5) order by trade_date desc ")
	public List<AccountOrder> findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(Long investmentSequence, Integer orderStatus1,
			Integer orderStatus2, Integer tradeType1, Integer tradeType2);

	/**
	 * 查询指定用户的提现手续费。
	 * 
	 * @param accountSequence
	 *            用户流水号
	 * @param tradeType
	 *            订单类型
	 * @param orderStatus
	 *            订单状态
	 * @return 订单
	 */
	@Query(nativeQuery = true, value = "select IFNULL(sum(trade_amount), 0) from t_act_order where (t_pacct_seq = ?1 or t_gacct_seq =?1 )and trade_type = ?2 and order_stat= ?3 ")
	public BigDecimal sumWithdrawFeeByAccountSequence(Long accountSequence, Integer tradeType, Integer orderStatus);

	/**
	 * 查询指定用户的资金明细。
	 * 
	 * @param accountSequence
	 *            用户流水号
	 * @param startDate
	 *            开始日
	 * @param endDate
	 *            终了日
	 * @param tradeType
	 *            订单类型
	 * @param orderStatus
	 *            订单状态
	 * @return 订单
	 */
	Page<AccountOrder> findByPayAccountSequenceAndTradeDateBetweenAndOrderStatusInAndTradeTypeInOrGatherAccountSequenceAndTradeDateBetweenAndOrderStatusInAndTradeTypeIn(
			Long payAccountSequence, Date startDate, Date endDate, Collection<Integer> orderStatus, Collection<Integer> tradeType,
			Long gatherAccountSequence, Date startDate1, Date endDate1, Collection<Integer> orderStatus1, Collection<Integer> tradeType1,
			Pageable pageable);

	/**
	 * 根据用户流水号，查询用户冻结成功的金额（冻结成功的自动或手动投资）
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link AccountOrder}
	 */
	@Query(nativeQuery = true, value = "SELECT IFNULL(sum(trade_amount), 0) from t_act_order where t_pacct_seq =?1 and order_stat =?2 and (trade_type =?3 or trade_type =?4) and trade_ctpvalue <> ?5 ")
	public BigDecimal findFreezeApplySuccessAmtForReleaseCash(Long payAccountSequence, Integer orderStatus, Integer tradeType1, Integer tradeType2,
			Long tradeCtpvalue);

}
