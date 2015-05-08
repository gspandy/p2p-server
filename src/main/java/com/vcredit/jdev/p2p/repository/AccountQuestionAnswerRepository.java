package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.AccountQuestionAnswer;

public interface AccountQuestionAnswerRepository extends JpaRepository<AccountQuestionAnswer, Long> {

	public AccountQuestionAnswer findByAccountSequenceAndSystemQuestionSequence(long accountSequence,long systemQuestionSequence);
	
	public List<AccountQuestionAnswer> findByAccountSequence(long accountSequence);
	
	@Modifying
    @Query(nativeQuery = true, value="delete from t_sqsa where t_acct_seq=?1")
    public int delAccountQuestionAnswerByAccountId(long aid);
}
