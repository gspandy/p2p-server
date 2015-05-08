package com.vcredit.jdev.p2p.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.Investment;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

	@Query(nativeQuery = true, value = "SELECT * from t_inv where inv_stat = ?1 order by inv_process desc ")
	public List<Investment> findAutoInvestmentOrderByInvestmentProgress(Integer status);

	/** 根据项目编号模糊检索数据 */
	List<Investment> findByinvestmentNumberLike(String investmentNumber);
	/** 根据项目编号检索数据 */
	List<Investment> findByinvestmentNumber(String investmentNumber);

	/** 根据项目状态检索数据 */
	List<Investment> findByInvestmentStatus(Integer i);

	/** 根据项目状态和项目上线时间检索数据 */
	List<Investment> findByInvestmentStatusAndInvestmentTypeAndInvestmentStartDateLessThan(Integer i, Integer investmentType, Date date, Sort sort);

	/** 根据项目状态和发布方式检索数据 */
	Page<Investment> findByInvestmentStatusAndInvestmentType(int investmentStatus, int investmentType, Pageable pageable);

	/** 根据项目状态和项目流标时间检索应该数据 */
	List<Investment> findByInvestmentStatusAndInvestmentEndDateLessThan(Integer i, Date date, Sort sort);

	/** 根据项目状态和贷款期限检索数据 */
	List<Investment> findByInvestmentStatusInAndInvestmentPeriodInAndInvestmentStartDateGreaterThanAndInvestmentStartDateLessThanAndInvestmentEndDateGreaterThan(Integer i,
			List<Integer> InvestmentPeriod, Date lessdate, Date greateDate, Date endDate, Sort sort);

	/** 根据项目状态和发布方式检索数据 */
	List<Investment> findByInvestmentStatusAndInvestmentType(int investmentStatus, int investmentType, Sort sort);

	/** 根据项目状态检索数据 */
	List<Investment> findByInvestmentStatusAndInvestmentTypeNot(int investmentStatus, int investmentType, Sort sort);

	@Query(nativeQuery = true, value = "select a.* from t_inv a inner join t_act_inv b on a.t_inv_seq =b.t_inv_seq where t_act_inv_seq = ?1 ")
	public Investment findInvestmentByAccountInvestmentSequence(Long accountInvestmentSequence);
	
	@Query(nativeQuery = true, value = "select t.* from t_inv t INNER JOIN t_act_cont ac on t.t_inv_seq=ac.t_inv_seq where t.t_inv_seq = ?1 ")
	public List<Investment> findInvestmentByAccountContractInvestmentSequence(Long accountInvestmentSequence);

	/** 根据项目状态和期数 */
	List<Investment> findByInvestmentStatusAndInvestmentPeriodIn(Integer i,
			List<Integer> InvestmentPeriod, Sort sort);
	/** 根据项目id检索数据 */
	Investment findByInvestmentSequence(Long investmentSequence);
}
