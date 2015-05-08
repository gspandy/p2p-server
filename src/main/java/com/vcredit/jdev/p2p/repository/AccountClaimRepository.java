package com.vcredit.jdev.p2p.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.AccountClaim;
import com.vcredit.jdev.p2p.entity.AccountInvestment;

public interface AccountClaimRepository extends JpaRepository<AccountClaim, Long> {

	/**
	 * 根据用户获得的项目流水号，查询用户获得的债权
	 * 
	 * @param accountInvestmentSequence
	 *            用户Id
	 * @return {@link AccountInvestment}
	 */
	public List<AccountClaim> findByAccountInvestmentSequenceAndAccountClaimStatus(Long accountInvestmentSequence, Integer accountClaimStatus);

	/**
	 * 根据用户流水号，查询在投债权合计
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link Integer}
	 */
	@Query(nativeQuery = true, value = "select IFNULL(sum(b.act_clm_pqt),0) from t_act_inv a inner join t_act_clm b on a.t_act_inv_seq = b.t_act_inv_seq where a.t_acct_seq = ?1 and a.act_inv_stat =?2 ( act_clm_status=?3 or act_clm_status =?4) ")
	public BigDecimal findByAccountSequenceAndAccountClaimStatus(Long accountSequence, Integer accountInvestmentStatus, Integer accountClaimStatus1,
			Integer accountClaimStatus2);

}
