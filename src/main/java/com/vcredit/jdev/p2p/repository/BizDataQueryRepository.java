package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.vcredit.jdev.p2p.bizsync.convert.VbsQueryDto;

@NoRepositoryBean
public interface BizDataQueryRepository {
	
	public List<VbsQueryDto> getVbsQueryList(String[] sqlArr);

}
