package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;

public interface AccountThirdRepository extends JpaRepository<ThirdPaymentAccount, Long> {

	public ThirdPaymentAccount findByAccountSequence(Long accountSequence);

	public List<ThirdPaymentAccount> findByThirdPaymentIdType(Integer thirdPaymentIdType);
}
