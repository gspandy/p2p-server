package com.vcredit.jdev.p2p.alert.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.vcredit.jdev.p2p.entity.AccountMessage;

/**
 * @ClassName: AccountMessageRepository
 * @Description:
 * @author ChenChang
 * @date 2014年12月26日 下午2:41:11
 */
public interface AccountMessageRepository extends JpaRepository<AccountMessage, Long> {

	@Transactional(rollbackFor = { Exception.class, RuntimeException.class })
	@Modifying
	@Query(nativeQuery = true, value = "delete from t_act_msg where t_acct_seq =?1 ")
	public void deleteAccountMessageAll(Long aid);

	/**
	 * 未读取的站内信
	 * 
	 * @param userId
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT count(1) from t_act_msg where t_acct_seq =?1 and amsg_rstat =1 ")
	public Long queryAccountMessageCount4SiteMsg(Long userId);

	@Query(nativeQuery = true, value = "SELECT distinct * from t_act_msg where t_acct_seq =?1 order by amsg_rdate desc ")
	public List<AccountMessage> findAccountMessageAllByUserId(Long userId);

	@Query(nativeQuery = true, value = "SELECT distinct * from t_act_msg where t_acct_seq =?1 and amsg_rdate >=?2 and amsg_rdate<=?3 order by amsg_rdate desc ")
	public List<AccountMessage> findAccountMessageAllByUserIdDate(Long userId, Date beginDate, Date endDate);
}
