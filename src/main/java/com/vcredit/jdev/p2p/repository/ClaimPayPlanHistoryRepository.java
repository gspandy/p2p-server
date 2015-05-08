package com.vcredit.jdev.p2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.ClaimPayPlanHistory;

public interface ClaimPayPlanHistoryRepository extends JpaRepository<ClaimPayPlanHistory, Long> {

	@Query(nativeQuery = true, value = "select * from t_clm_pp_his where t_clm_pplan_seq = ?1 and clm_pp_ddays =?2 ")
	public ClaimPayPlanHistory findOverduePaidPlanHistory(Long claimPayPlanSequence, Integer claimPayPlanDelayDays);

	@Query(nativeQuery = true, value = "select * from t_clm_pp_his where t_clm_pplan_seq = ?1 and clm_pp_ddays =?2 and DATE_FORMAT(rec_cdate,'%Y%m%d') = DATE_FORMAT(now(),'%Y%m%d') ")
	public ClaimPayPlanHistory findOverduePaidPlanHistoryByNowDate(Long claimPayPlanSequence, Integer claimPayPlanDelayDays);

	@Query(nativeQuery = true, value = "select * from t_clm_pp_his where t_clm_pplan_seq = ?1 and clm_pp_ddays =?2 and clm_pp_stat =?3 ")
	public ClaimPayPlanHistory findOverduePaidPlanHistoryByStatus(Long claimPayPlanSequence, Integer claimPayPlanDelayDays, Integer claimPayPlanStatus);

}
