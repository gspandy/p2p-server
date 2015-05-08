package com.vcredit.jdev.p2p.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.ClaimGatherPlan;

public interface ClaimGatherPlanRepository extends JpaRepository<ClaimGatherPlan, Long> {

	//@Query(nativeQuery = true, value = "SELECT  a.t_clm_gplan_seq,A.clm_gp_stat,a.clm_gpptotal clm_gpptotal,DATE_FORMAT(A.clm_gp_ndate,'%Y-%m-%d') clm_gp_ndate FROM t_clm_gplan A, t_act_inv  B WHERE  a.T_GACCT_SEQ = ?1 AND A.clm_gp_ndate BETWEEN ?2 AND   ?3  ")
	@Query(nativeQuery = true, value = "SELECT  a.* FROM t_clm_gplan A WHERE  a.T_GACCT_SEQ = ?1 AND A.clm_gp_ndate BETWEEN ?2 AND   ?3  ")
	public List<ClaimGatherPlan> findByAccountSequence(Long accountSequence, Date beginDate, Date endDate);

	public List<ClaimGatherPlan> findByAccountInvestmentSequenceAndClaimGatherPlanStatus(Long accountInvestmentSequence, Integer claimGatherPlanStatus);

	public List<ClaimGatherPlan> findByGatherAccountSequenceAndClaimGatherPlanStatus(Long accountId, Integer claimGatherPlanStatus);

	public List<ClaimGatherPlan> findByGatherAccountSequenceAndClaimGatherPlanStatusAndClaimGatherPlanNatureDateLessThanAndClaimGatherPlanNatureDateGreaterThan(
			Long gatherAccountSequence, Integer claimGatherPlanStatus, Date maxDate, Date minDate);

	/**
	 * 根据用户流水号，查询未还，逾期收款计划
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link ClaimGatherPlan}
	 */
	@Query(nativeQuery = true, value = "select b.* from t_act_inv a inner join t_clm_gplan b on a.t_act_inv_seq = b.t_act_inv_seq where a.t_acct_seq =?1 and a.act_inv_stat =?2 and ( b.clm_gp_stat=?3 or b.clm_gp_stat =?4)")
	public List<ClaimGatherPlan> findByAccountSequenceAndAccountClaimStatus(Long accountSequence, Integer accountInvestmentStatus,
			Integer accountClaimStatus1, Integer accountClaimStatus2);

	/**
	 * 根据用户流水号，查询当天的未还，逾期收款计划
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link ClaimGatherPlan}
	 */
	@Query(nativeQuery = true, value = "select b.* from t_act_inv a inner join t_clm_gplan b on a.t_act_inv_seq = b.t_act_inv_seq where a.t_acct_seq =?1 and a.act_inv_stat =?2 and DATE_FORMAT(b.clm_gp_ndate,'%Y%m%d') = DATE_FORMAT(now(),'%Y%m%d') ")
	public List<ClaimGatherPlan> findTodayGatherClaim(Long accountSequence, Integer accountInvestmentStatus);

	public ClaimGatherPlan findByAccountInvestmentSequenceAndClaimGatherPlanNumber(Long accountInvestmentSequence, Integer claimGatherPlanNumber);

	/**
	 * 根据用户获得的项目流水号，查询未还收款计划
	 * 
	 * @param claimGatherPlanStatus
	 *            用户Id
	 * @return {@link ClaimGatherPlan}
	 */
	public List<ClaimGatherPlan> findByAccountInvestmentSequenceAndClaimGatherPlanStatusNot(Long accountInvestmentSequence,
			Integer claimGatherPlanStatus);

	/**
	 * 还未收回的本金金额合计
	 * 
	 * @param claimGatherPlanStatus
	 *            用户Id
	 * @return {@link ClaimGatherPlan}
	 */
	@Query(nativeQuery = true, value = "select IFNULL(sum(clm_gpppri),0) from t_clm_gplan a where a.t_gacct_seq =?1 and a.clm_gp_stat <> ?2 ")
	public BigDecimal sumByGatherAccountSequence(Long accountSequence, Integer claimGatherPlanStatus);

	/**
	 * 还未收回的收款计划
	 * 
	 * @param claimGatherPlanStatus
	 *            用户Id
	 * @return {@link ClaimGatherPlan}
	 */
	@Query(nativeQuery = true, value = "select * from t_clm_gplan a where a.t_gacct_seq =?1 and a.clm_gp_stat <> ?2 ")
	public List<ClaimGatherPlan> findByGatherAccountSequenceNotPaid(Long accountSequence, Integer claimGatherPlanStatus);

	/**
	 * 根据用户流水号，查询今日总收益
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link ClaimGatherPlan}
	 */
	@Query(nativeQuery = true, value = "select * from t_clm_gplan a where a.t_gacct_seq =?1 and a.clm_gp_stat =?2 and DATE_FORMAT(a.rec_edate,'%Y%m%d') = DATE_FORMAT(now(),'%Y%m%d') ")
	public List<ClaimGatherPlan> findTodayGatherAmt(Long accountSequence, Integer claimGatherPlanStatus);

	/**
	 * 查询某期次投资人的本息和罚息总和
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param claimGatherPlanNumber
	 *            期次
	 * 
	 * @return {@link ClaimGatherPlan}
	 */
	@Query(nativeQuery = true, value = "select IFNULL(sum(c.clm_gppint),0) + IFNULL(sum(c.clm_gppjint),0) +IFNULL(sum(c.clm_gpppri),0) from t_inv a inner join t_act_inv b ON b.t_inv_seq = a.t_inv_seq inner join t_clm_gplan c ON c.t_act_inv_seq = b.t_act_inv_seq where a.t_inv_seq = ?1 and c.clm_gp_num = ?2 ")
	public BigDecimal sumInterestByInvSeqAndPeriod(Long investmentSequence, Integer claimGatherPlanNumber);

	@Query(nativeQuery = true, value = "select IFNULL(sum(c.clm_gpptotal),0)  from t_clm_gplan c where c.t_act_inv_seq =  ?1 and c.clm_gp_num > ?2 ")
	public BigDecimal sumNotGatherGpptotal(Long accountInvestmentSequence, Integer claimGatherPlanNumber);

	/**
	 * 查询某投资人已收的罚息+利息-投资管理费
	 * 
	 * @param investmentSequence
	 *            项目流水号
	 * @param claimGatherPlanNumber
	 *            期次
	 * 
	 * @return {@link ClaimGatherPlan}
	 */
	@Query(nativeQuery = true, value = "select IFNULL(sum(c.clm_gppint),0) + IFNULL(sum(c.clm_gppjint),0) - IFNULL(sum(c.act_clm_amfee),0) from  t_clm_gplan c where c.t_gacct_seq = ?1 and c.clm_gp_stat = ?2 ")
	public BigDecimal sumBenifitByAcctSeqAndStatus(Long accoutSequence, Integer claimGatherPlanStatus);

}
