package com.vcredit.jdev.p2p.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.bizsync.convert.VbsQueryDto;
import com.vcredit.jdev.p2p.repository.BizDataQueryRepository;

@Component
public class BizDataQueryRepositoryImpl implements BizDataQueryRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<VbsQueryDto> getVbsQueryList(String[] sqlArr) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.buss_code as investmentBusinessCode,a.inv_cdate as investmentCreateDate,a.inv_num as investmentNumber,a.inv_stat as investmentStatus,a.inv_sdate as investmentStartDate,a.inv_fdate as investmentFillDate,a.inv_ldate as investmentLostDate,a.inv_cre_date as investmentCreditDate,a.inv_process as investmentProgress,a.res_path as resourcePath,a.rec_stat as recordStatus from");
		sb.append(" (select d.buss_code,v.inv_cdate,v.inv_num,v.inv_stat,v.inv_sdate,v.inv_fdate,v.inv_ldate,v.inv_cre_date,v.inv_process,rs.res_path,d.rec_stat");
		sb.append(" from t_loan_data d");
		sb.append(" left join t_inv_act_ref ref ON ref.t_loan_data_seq = d.t_loan_data_seq");
		sb.append(" left join t_inv v ON v.t_inv_seq = ref.t_inv_seq");
		sb.append(" left join t_res rs ON v.t_res_seq = rs.t_res_seq");
		sb.append(" ) a");
		sb.append(" where exists(");
		sb.append(" select d2.buss_code from t_loan_data d2 where d2.buss_code = a.buss_code and d2.buss_code in (");
		for(int i=0; i<sqlArr.length; i++){
			sb.append("'"+sqlArr[i]+"'");
			if(i < sqlArr.length-1){
				sb.append(",");
			}
		}
		sb.append(")) order by investmentBusinessCode asc , investmentCreateDate desc");
		return entityManager.createNativeQuery(sb.toString(), "VbsQueryDtoMapping").getResultList();
//		Query query = entityManager.createNativeQuery(sb.toString(), VbsQueryDto.class);
//		query.setParameter(1, testString);
//		return query.getResultList();

	}

}
