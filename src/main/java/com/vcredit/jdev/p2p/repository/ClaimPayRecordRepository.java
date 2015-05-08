package com.vcredit.jdev.p2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.ClaimPayRecord;

public interface ClaimPayRecordRepository extends JpaRepository<ClaimPayRecord, Long> {

	public ClaimPayRecord findByInvestmentSequenceAndClaimPayNumber(Long investmentSequence, Integer claimPayNumber);

}
