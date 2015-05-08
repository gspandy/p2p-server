package com.vcredit.jdev.p2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.Area;
import com.vcredit.jdev.p2p.entity.Resource;

public interface ResouceRepository extends JpaRepository<Resource, Long> {
	
	@Query(nativeQuery=true , value="select r.* from t_res r ,t_inv i where r.t_res_seq = i.t_res_seq and i.t_inv_seq =?1")
	public Resource findInvestmentID (Long investmentId);
}
