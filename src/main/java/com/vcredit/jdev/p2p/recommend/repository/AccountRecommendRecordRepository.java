package com.vcredit.jdev.p2p.recommend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.AccountRecommendRecord;

/**
 * @author ChenChang
 *
 */
public interface AccountRecommendRecordRepository extends JpaRepository<AccountRecommendRecord, Long> {
	@Query(nativeQuery = true, value = "SELECT distinct * from t_rec_record where t_acct_seq =?1")
	public List<AccountRecommendRecord> findAccountRecommendRecordAllByUserId(Long userId);

	@Query(nativeQuery = true, value = "SELECT count(distinct rec_frd_id) from t_rec_record where t_acct_seq =?1")
	public Long findAccountRecommendRecordCountByUserId(Long userId);

}
