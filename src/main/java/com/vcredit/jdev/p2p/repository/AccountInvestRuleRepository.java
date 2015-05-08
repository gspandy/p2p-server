package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.AccountInvestRule;

public interface AccountInvestRuleRepository extends JpaRepository<AccountInvestRule, Long> {

	public List<AccountInvestRule> findByStatusAndType(Integer status, Integer type);

	public AccountInvestRule findByAccountSequnece(Long accountSequnece);
}
