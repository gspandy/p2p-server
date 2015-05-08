package com.vcredit.jdev.p2p.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.Welfare;

/**
 * 接口	福利
 * @author 周佩
 * 创建时间	20141212
 */
public interface WelfareRepository extends JpaRepository<Welfare, Long>{

	Page<Welfare> findByWelfareName(String i, Pageable pageable);
}
