package com.vcredit.jdev.p2p.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.ClaimGatherRecord;

public interface ClaimGatherRecordRepository extends JpaRepository<ClaimGatherRecord, Long> {

	public List<ClaimGatherRecord> findByGatherAccountSequence(Long thirdPaymentId);

	/**
	 * 根据用户获得的项目流水号,查询该项目债权收款记录的实收本息(元)总额
	 * 
	 * @param accountInvestmentSequence
	 *            用户获得项目流水号
	 * @return {@link BigDecimal}
	 */
	@Query(nativeQuery = true, value = "select IFNULL(sum(b.clm_gptotal),0) from t_act_clm a inner join t_clm_grecord b on a.t_act_clm_seq = b.t_act_clm_seq where t_act_inv_seq = ?1 ")
	public BigDecimal findSumGatherByAccountInvestmentSequence(Long accountInvestmentSequence);

}
