package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;

/**
 * 投资项目与贷款人关系
 */
public interface InvestmentAccountReferenceRepository extends JpaRepository<InvestmentAccountReference, Long> {

	/**
	 * 根据当前用户查找对应的小贷公司中间表
	 * 
	 * @param accountSequence
	 * @return
	 */
	public List<InvestmentAccountReference> findByAccountSequence(Long accountSequence);

	public List<InvestmentAccountReference> findByInvestmentSequence(Long investmentSequence);

	public List<InvestmentAccountReference> findByLoanDataSequence(Long loanDataSequence);

	/**
	 * 根据用户Id和项目编号，查询对应的小贷公司数据
	 * 
	 * @param investmentSequence
	 * @param accountId
	 * @return
	 */
	public List<InvestmentAccountReference> findByInvestmentSequenceAndAccountSequence(Long investmentSequence, Long accountId);

	/**
	 * 根据小贷公司业务编号，查询p2p项目流水号和贷款人的p2p用户
	 * 
	 * @param accountSequence
	 *            用户流水号
	 * @return 第三方支付账号
	 */
	@Query(nativeQuery = true, value = "select b.* from t_loan_data a inner join t_inv_act_ref b on a.t_loan_data_seq = b.t_loan_data_seq inner join t_inv c on c.t_inv_seq = b.t_inv_seq and c.inv_stat in( ?2, ?3) where buss_code=?1 ")
	public InvestmentAccountReference findInvestmentByBusinessCode(String businessCode, Integer invStat1, Integer invStat2);

}
