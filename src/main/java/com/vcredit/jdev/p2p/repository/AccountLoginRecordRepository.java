package com.vcredit.jdev.p2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.AccountLoginRecord;

public interface AccountLoginRecordRepository extends JpaRepository<AccountLoginRecord, Long> {
}
