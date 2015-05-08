package com.vcredit.jdev.p2p.alert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.entity.AccountRemind;

public interface AccountRemindRepository extends
		JpaRepository<AccountRemind, Long> {

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update t_act_remind set remind_stat =?1 where t_acct_seq =?2 and t_ropt_seq=?3")
	public int updateAccountRemindOne(Integer remindStatus,
			Long accountSequnece, Long systemRemindOptionSequence);

	@Query(nativeQuery = true, value = "SELECT distinct * from t_act_remind where t_acct_seq =?1")
	public List<AccountRemind> findAllByAid(Long aid);

	@Query(nativeQuery = true, value = "SELECT distinct * from t_act_remind where t_acct_seq =?1 and t_ropt_seq=?2")
	public List<AccountRemind> findAllByAid_RoptId(long aid, long roptId);

	@Query(nativeQuery = true, value = "SELECT distinct * from t_act_remind where t_acct_seq =?1 and t_ropt_seq=?2 and remind_stat=?3")
	public List<AccountRemind> findAllByAid_RoptId_Stat(long aid, long roptId,
			int stat);

	@Query(nativeQuery = true, value = "SELECT count(1) from t_act_remind where t_acct_seq =?1 and t_ropt_seq=?2")
	public Long getCountByAid_RoptId(long aid, long roptId);

}
