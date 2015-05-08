package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.AccountFeePaymentRecord;

/**
 * 用户缴费记录
 * 
 * @author zhuqiu
 *
 */
public interface AccountFeePaymentRecordRepository extends JpaRepository<AccountFeePaymentRecord, Long> {

	public List<AccountFeePaymentRecord> findByGatherStatus(Integer gatherStatus);

	public List<AccountFeePaymentRecord> findByGatherStatusAndPayAccountSequence(Integer gatherStatus, Long payAccountSequence);
}
