package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.AccountWelfare;

/**
 * 接口	福利
 * @author 周佩
 * 创建时间	20141212
 */
public interface AccountWelfareRepository extends JpaRepository<AccountWelfare, Long>{

	/**通过用户账号查找用户所拥有的红包*/
	Page<AccountWelfare> findByAccountSequence(Long i, Pageable pageable);

	/**通过用户账号和状态查找用户所拥有的红包*/
	List<AccountWelfare> findByAccountSequenceAndWelfareStatus(Long i,Integer status);
	/**通过用户账号和状态查找用户所拥有的红包*/
	Page<AccountWelfare> findByAccountSequenceAndWelfareStatus(Long i,Integer status, Pageable pageable);
	
}
