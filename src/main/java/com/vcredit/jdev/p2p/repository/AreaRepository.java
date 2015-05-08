package com.vcredit.jdev.p2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {
	
	@Query(nativeQuery=true,value="SELECT ta_id from t_area WHERE ta_name=?1")
	public Integer getAreaIdByAreaName(String areaName);
	
	public Area findByAddressChineseName(String areaName);
}
