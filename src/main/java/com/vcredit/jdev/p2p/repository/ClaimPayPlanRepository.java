package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.ClaimPayPlan;

/**
 * 债权还款计划
 * 
 * @author zhuqiu
 */
public interface ClaimPayPlanRepository extends JpaRepository<ClaimPayPlan, Long> {

	/**
	 * 根据投资项目流水号和期次、用户、还款计划状态，查询投资项目的债权还款计划
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param peroidNo
	 *            期次
	 * @param payAccountSequence
	 *            贷款用户
	 * @return {@link ClaimPayPlan}
	 */
	public ClaimPayPlan findByInvestmentSequenceAndClaimPayPlanNumberAndPayAccountSequence(Long investmentSequence, Integer peroidNo,
			Long payAccountSequence);

	public ClaimPayPlan findByInvestmentSequenceAndClaimPayPlanNumber(Long investmentSequence, Integer peroidNo);

	/**
	 * 根据投资项目流水号和期次、用户、还款计划状态，查询投资项目的债权还款计划
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param peroidNo
	 *            期次
	 * @param payAccountSequence
	 *            贷款用户
	 * @param claimPayPlanStatus
	 *            还款计划状态
	 * @return {@link ClaimPayPlan}
	 */
	public ClaimPayPlan findByInvestmentSequenceAndClaimPayPlanNumberAndPayAccountSequenceAndClaimPayPlanStatus(Long investmentSequence,
			Integer peroidNo, Long payAccountSequence, Integer claimPayPlanStatus);

	/**
	 * 根据投资项目流水号和状态，查询项目的债权还款计划
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param claimPayPlanStatus
	 *            债权状态
	 * @return {@link ClaimPayPlan}的list
	 */
	public List<ClaimPayPlan> findByInvestmentSequenceAndClaimPayPlanStatus(Long investmentSequence, Integer claimPayPlanStatus);

	/**
	 * 查询逾期天数>=30,还款计划状态=【逾期】的债权还款计划
	 * 
	 * @param claimPayPlanDelayDays
	 *            逾期天数
	 * @param claimPayPlanStatus
	 *            还款计划状态
	 * @return {@link ClaimPayPlan}的List
	 */
	public List<ClaimPayPlan> findByClaimPayPlanDelayDaysIsGreaterThanEqualAndClaimPayPlanStatus(Integer claimPayPlanDelayDays,
			Integer claimPayPlanStatus);

	/**
	 * 根据投资项目流水号，查询项目的债权还款计划
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link ClaimPayPlan}的list
	 */
	public List<ClaimPayPlan> findByInvestmentSequence(Long investmentSequence);

	/**
	 * 查询所有今日到期的项目的债权还款计划
	 * 
	 * @param claimPayPlanStatus
	 *            债权状态
	 * @return {@link ClaimPayPlan}的list
	 */
	@Query(nativeQuery = true, value = "select * from t_clm_pplan a inner join t_inv b on b.t_inv_seq = a.t_inv_seq where DATE_FORMAT(a.clm_pp_ndate, '%Y%m%d') <= DATE_FORMAT(now(),'%Y%m%d') and clm_pp_stat in( ?1, ?2, ?3) and b.inv_stat =?4 ")
	public List<ClaimPayPlan> findTodayAllInvestmentNotPaid(Integer claimPayPlanStatus, Integer claimPayPlanStatus2, Integer claimPayPlanStatus3,
			Integer invStat);

	/**
	 * 查询所有可以垫付债权还款计划
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link ClaimPayPlan}的list
	 */
	@Query(nativeQuery = true, value = "select * from t_clm_pplan a where DATE_FORMAT(DATE_ADD(clm_pp_ndate, INTERVAL 7 DAY),'%Y%m%d') <= DATE_FORMAT(now(),'%Y%m%d') and clm_pp_stat= ?1 ")
	public List<ClaimPayPlan> findBondForTranfer(Integer claimPayPlanStatus);

	/**
	 * 查询下期债权还款计划的还款日
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param claimPayPlanStatus1
	 *            未还
	 * @param claimPayPlanStatus2
	 *            逾期未垫付
	 * @return {@link ClaimPayPlan}
	 */
	@Query(nativeQuery = true, value = "select * from t_clm_pplan a where  t_inv_seq =?1 and  clm_pp_stat in (?2)  order by clm_pp_num asc limit 1 ")
	public ClaimPayPlan findNatureDateByInvestmentSequenceAndPayAccountSequence(Long investmentSequence, Integer claimPayPlanStatus1);

	/**
	 * 查询T日还款清单
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param claimPayPlanStatus1
	 *            未还 /逾期未垫付 /逾期已垫付
	 * @return {@link ClaimPayPlan}
	 */
	@Query(nativeQuery = true, value = "select d.* from t_loan_cut a inner join t_loan_data b on a.buss_code = b.buss_code inner join t_inv_act_ref c on c.t_loan_data_seq = b.t_loan_data_seq inner join t_clm_pplan d on d.t_inv_seq = c.t_inv_seq  and d.clm_pp_num = a.loan_period where DATE_FORMAT(a.rec_idate, '%Y%m%d')=DATE_FORMAT(now(), '%Y%m%d') and d.clm_pp_stat = ?1 ")
	public List<ClaimPayPlan> findTDayPayList(Integer claimPayPlanStatus);

	/**
	 * 查询债权已还期数
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param claimPayPlanStatus1
	 *            未还
	 * @param claimPayPlanStatus2
	 *            逾期未垫付
	 * @return {@link ClaimPayPlan}
	 */
	@Query(nativeQuery = true, value = "select * from t_clm_pplan where t_inv_seq=?1 and  clm_pp_stat in (?2, ?3, ?4) order by clm_pp_num desc limit 1 ")
	public ClaimPayPlan findPayedPeriodByInvestmentSequenceAndStatus(Long investmentSequence, Integer claimPayPlanStatus1,
			Integer claimPayPlanStatus2, Integer claimPayPlanStatus3);

	/**
	 * 查询债权已还期数
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param claimPayPlanStatus1
	 *            未还
	 * @param claimPayPlanStatus2
	 *            逾期已垫付
	 * @return {@link ClaimPayPlan}
	 */
	@Query(nativeQuery = true, value = "select * from t_clm_pplan where t_inv_seq=?1 and  clm_pp_stat in (?2, ?3, ?4, ?5) order by clm_pp_num desc limit 1 ")
	public ClaimPayPlan findPayedPeriodByInvestmentSequenceAndStatus(Long investmentSequence, Integer claimPayPlanStatus1,
			Integer claimPayPlanStatus2, Integer claimPayPlanStatus3, Integer claimPayPlanStatus4);

	/**
	 * 查询债权已还期数(包含已垫付)
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @param claimPayPlanStatus1
	 *            未还
	 * @param claimPayPlanStatus2
	 *            逾期未垫付
	 * @return {@link ClaimPayPlan}
	 */
	@Query(nativeQuery = true, value = "select * from t_clm_pplan where t_inv_seq=?1 and  clm_pp_stat in (?2, ?3, ?4, ?5, ?6) order by clm_pp_num desc limit 1 ")
	public ClaimPayPlan findPayedPeriodByInvestmentSequenceAndStatus(Long investmentSequence, Integer claimPayPlanStatus1,
			Integer claimPayPlanStatus2, Integer claimPayPlanStatus3, Integer claimPayPlanStatus4, Integer claimPayPlanStatus5);
}
