package com.vcredit.jdev.p2p.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.ThirdPaymentAccount;

/**
 * dto接口
 * 
 * @author 周佩
 * 创建时间20141211
 */
public interface CapitalRecordRepository extends JpaRepository<ThirdPaymentAccount, Long> {

	public ThirdPaymentAccount findByAccountSequenceOrThirdPaymentId(Long AccountSequence,String thirdPaymentId);
	
	public ThirdPaymentAccount findByAccountSequence(Long accountSequence);
	
	public ThirdPaymentAccount findByThirdPaymentUniqueId(Long thirdPaymentUniqueId);
	public ThirdPaymentAccount findByThirdPaymentIdActiveStatus(Integer thirdPaymentIdActiveStatus);

	public ThirdPaymentAccount findByThirdPaymentId(String thirdPaymentId);
	/** 根据账户类型获得账户信息 */
	public ThirdPaymentAccount findByThirdPaymentIdType(Integer thirdPaymentIdType);
	/** 获取最近两天的支付账户信息 */
	public List<ThirdPaymentAccount> findByThirdPaymentIdRegisterDateGreaterThan (Date thirdPaymentIdRegisterDate);
	
}
