package com.vcredit.jdev.p2p.cms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.Message;

/**
 * @ClassName: MessageRepository
 * @Description: 富文本JPA接口
 * @author ChenChang
 * @date 2014年12月24日 下午3:20:58
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query(nativeQuery = true, value = "SELECT * from t_msg where msg_itype =2 and msg_stats =0 and t_msg_seq =?1 ORDER BY msg_sdate DESC  ")
	public Message findOnePublishedAnnouncement(Long msgId);

	@Query(nativeQuery = true, value = "SELECT * from t_msg where msg_itype =2 and msg_stats =0  ORDER BY msg_sdate DESC ")
	public List<Message> findAllPublishedAnnouncement();

}
