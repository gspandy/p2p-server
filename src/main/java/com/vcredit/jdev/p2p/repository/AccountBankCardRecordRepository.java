package com.vcredit.jdev.p2p.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.AccountBankCard;

/**
 * dto接口
 * 
 * @author 周佩 创建时间20141211
 */
public interface AccountBankCardRecordRepository extends JpaRepository<AccountBankCard, Long> {

	public List<AccountBankCard> findByThirdPaymentId(String thirdPaymentId);

	public List<AccountBankCard> findByAccountSequnece(Long accountSequnece);

	public List<AccountBankCard> findByBankCode(Integer accountSequnece);

	public List<AccountBankCard> findBybankCardNumber(String bankCardNumber);

	public List<AccountBankCard> findByBankCardNumberAndAccountSequnece(String bankCardNumber,Long accountSequnece);
	
	public AccountBankCard findByThirdPaymentIdAndBankCardDefaultDrawStatus(String thirdPaymentId, Integer bankCardDefaultDrawStatus);
}
