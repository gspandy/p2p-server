package com.vcredit.jdev.p2p.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.AccountInvestment;

/**
 * 用户获得的项目
 * 
 *
 */
public interface AccountInvestmentRepository extends JpaRepository<AccountInvestment, Long> {

	/**
	 * 用户持有 项目数
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link AccountInvestment}
	 */
	//	@Query(nativeQuery = true, value = "SELECT count(t_inv_seq) from  (select t_inv_seq from t_act_inv where t_acct_seq=?1 and act_inv_stat =?2 group by t_inv_seq ) a")
	@Query(nativeQuery = true, value = "SELECT count( distinct t_inv_seq) from  t_act_inv where t_acct_seq=?1 and act_inv_stat in (1,2,3,4,15)")
	public Long countByAccountSequenceAndAccountInvestmentStatus(Long accountSequence);

	/**
	 * 查找用户持有债权/或持有过(转让,转让成功)
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link AccountInvestment}
	 */
	@Query(nativeQuery = true, value = "SELECT * from  t_act_inv where t_acct_seq=?1 and act_inv_stat in (1,2,3,4,15) ")
	public List<AccountInvestment> findAccountInvestmentHolder(Long accountSequence);

	/**
	 * 查找用户持有债权/或持有过(转让,转让成功)
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link AccountInvestment}
	 */
	@Query(nativeQuery = true, value = "SELECT * from  t_act_inv where t_acct_seq=?1 and t_inv_seq =?2 and act_inv_stat in (1,2,3,4,10,15) ")
	public List<AccountInvestment> findAccountInvestmentHolder(Long accountSequence, Long investmentSequence);

	/**
	 * 根据投资项目流水号，查询用户获得的项目
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link AccountInvestment}
	 */
	public List<AccountInvestment> findByInvestmentSequence(Long investmentSequence);

	/**
	 * 根据投资项目流水号，查询用户获得的项目
	 * 
	 * @param investmentSequence
	 *            投资项目流水号
	 * @return {@link AccountInvestment}
	 */
	public List<AccountInvestment> findByInvestmentSequenceAndAccountInvestmentStatus(Long investmentSequence, Integer accountInvestmentStatus);

	/**
	 * 根据投资项目流水号，查询用户获得的项目
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link AccountInvestment}
	 */
	public List<AccountInvestment> findByAccountSequenceAndAccountInvestmentStatus(Long accountSequence, Integer accountInvestmentStatus);

	/**
	 * 根据用户流水号，查询用户获得的项目个数
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link AccountInvestment}
	 */
	@Query(nativeQuery = true, value = "SELECT count(1) from t_act_inv where t_acct_seq = ?1 ")
	public Long getCountByAccountSequence(Long accountSequence);

	/**
	 * 根据用户流水号，查询结束，转让成功的项目
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link AccountInvestment}
	 */
	@Query(nativeQuery = true, value = "SELECT * from  t_act_inv where t_acct_seq=?1 and (act_inv_stat =?2 or act_inv_stat=?3) ")
	public List<AccountInvestment> findFinishInvestmentByAccountSequence(Long accountSequence, Integer accountInvestmentStatus1,
			Integer accountInvestmentStatus2);

	/**
	 * 根据投资项目流水号，查询用户获得的项目
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link AccountInvestment}
	 */
	public List<AccountInvestment> findByInvestmentSequenceAndAccountSequence(Long investmentSequence, Long accountSequence);

	/**
	 * 根据投资项目流水号，查询用户的投资金额
	 * 
	 * @param accountSequence
	 *            用户Id
	 * @return {@link AccountInvestment}
	 */
	@Query(nativeQuery = true, value = "SELECT IFNULL(sum(act_inv_qt), 0) from t_act_inv where t_inv_seq= ?1 and t_acct_seq = ?2 ")
	public BigDecimal sumByInvestmentSequenceAndAccountSequence(Long investmentSequence, Long accountSequence);

}
