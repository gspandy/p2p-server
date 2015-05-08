package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.AccountCashOperationRecord;

public interface AccountCashOperationRecordRepository extends JpaRepository<AccountCashOperationRecord, Long> {
	public List<AccountCashOperationRecord> findByCashFlowId(String orderId);
}
