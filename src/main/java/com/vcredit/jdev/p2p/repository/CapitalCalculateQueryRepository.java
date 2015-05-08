package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.vcredit.jdev.p2p.dto.CapitalCalculateQueryDto;

@NoRepositoryBean
public interface CapitalCalculateQueryRepository {
	
	public List<CapitalCalculateQueryDto> queryInvestment(Long invId);

}
