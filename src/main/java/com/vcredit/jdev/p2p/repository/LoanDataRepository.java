package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.LoanCut;
import com.vcredit.jdev.p2p.entity.LoanData;

/**
 * dto接口
 * 
 * @author 周佩
 * 创建时间20141211
 */
public interface LoanDataRepository extends JpaRepository<LoanData, Long> {
//	/**
//	 * 查询小贷公司信息
//	 * 
//	 * @return {@link LoanCut}的List
//	 */
//	public List<LoanData> findByRecordStatus(Integer recordStatus, Pageable pageable);
	/**
	 * 查询小贷公司信息
	 * 
	 * @return {@link LoanCut}的List
	 */
	public List<LoanData> findByProgressStauts(Integer recordStatus, Pageable pageable);

}
