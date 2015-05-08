package com.vcredit.jdev.p2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.LoanTransfer;
/**
 * 小贷公司常量转换
 * @author 周佩
 *
 */
public interface LoanTransferRepository extends JpaRepository<LoanTransfer, Long>{

}
