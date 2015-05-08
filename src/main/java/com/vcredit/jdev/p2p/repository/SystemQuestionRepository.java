package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.SystemQuestion;

public interface SystemQuestionRepository extends JpaRepository<SystemQuestion, Long> {

	public List<SystemQuestion> findSystemQuestionListByQuestionStatus(int status);
	
	@Query(nativeQuery=true,value="SELECT s.* from t_sqs s LEFT JOIN t_sqsa a on s.t_sqs_seq=a.t_sqs_seq where a.t_acct_seq =?1 and s.quest_stat=?2")
	public List<SystemQuestion> findSystemQuestionListByAId(Long aid,int status);
}
