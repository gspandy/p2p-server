package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	public Account findByUserName(String userName);
	
	public Account findByMobile(String mobile);
	
	public Account findByUserNameAndMobile(String userName,String mobile);
	
	public Account findByPid(String pid);
	
	public Account findByAccountSequenceAndLoanTypeIn(Long accountId,List<Integer> loanType);
	
	//@Query(nativeQuery=true,value="select * from t_acct where tac_uname=?1 or tac_mobile=?1")
    //public Account findByUserNameOrMobile(String userName);
    
	public Account findByUserNameOrMobile(String userName,String mobile);
    /**
	 * email
	 */
	@Modifying
	@Query(nativeQuery=true,value="update t_acct set tac_email=?2,tac_emstat=?3 where t_acct_seq=?1")
	public int updateAccountEamil(Long id,String email,Integer mobileBindStatus);
	
	
	@Modifying
	@Query(nativeQuery=true,value="update t_acct set tac_emstat=?2 where t_acct_seq=?1")
	public int bindAccountEamil(Long id,Integer mobileBindStatus);
	
	/**
	 * 手机
	 */
	@Modifying
	@Query(nativeQuery=true,value="update t_acct set tac_mobile=?2 where t_acct_seq=?1")
	public int updateAccountMobile(Long id,String mobile);
	
	
	/**
	 * 最高学历
	 */
	@Modifying
	@Query(nativeQuery=true,value="update t_acct set tac_edu_degree=?2 where t_acct_seq=?1")
	public int updateAccountEducationDegree(Long id,Integer educationDegree);
	
	/**
	 * 职业
	 */
	@Modifying
	@Query(nativeQuery=true,value="update t_acct set tac_inds=?2 where t_acct_seq=?1")
	public int updateAccountIndustry(Long id,String industry);
	
	/**
	 * 月收入
	 */
	@Modifying
	@Query(nativeQuery=true,value="update t_acct set tac_income=?2 where t_acct_seq=?1")
	public int updateAccountIncome(Long id,Integer income);
	
	/**
	 * 联系地址
	 */
	@Modifying
	@Query(nativeQuery=true,value="update t_acct set tac_addr=?2 where t_acct_seq=?1")
	public int updateAccountAddress(Long id,String address);
	
	
	/**
	 * 安全等级
	 */
	@Modifying
	@Query(nativeQuery=true,value="update t_acct set tac_slv=?2 where t_acct_seq=?1")
	public int updateAccountSafety(Long id,Integer safety);
	
	
	/**
	 * 联系地址所在城市
	 */
	@Modifying
	@Query(nativeQuery=true,value="update t_acct set tac_tprov=?2,tac_tcity=?3 where t_acct_seq=?1")
	public int updateAccountTouchProvinceAndCity(Long id,Integer provinceCode,Integer cityCode);
	
	@Query(nativeQuery=true,value="SELECT * from t_acct WHERE tac_pid is not null and tac_pid <>'' and act_third  is null or act_third =''")
	public List<Account> findByPidNotNullAndAccountThirdPaymentIdIsNull();
}
