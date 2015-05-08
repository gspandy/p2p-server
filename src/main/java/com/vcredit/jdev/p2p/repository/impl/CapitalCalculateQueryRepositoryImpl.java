package com.vcredit.jdev.p2p.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.bizsync.convert.VbsQueryDto;
import com.vcredit.jdev.p2p.dto.CapitalCalculateQueryDto;
import com.vcredit.jdev.p2p.repository.CapitalCalculateQueryRepository;

@Component
public class CapitalCalculateQueryRepositoryImpl implements CapitalCalculateQueryRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<CapitalCalculateQueryDto> queryInvestment(Long invId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.inv_total*0.02 as operateFee,c.inv_total*0.98 as acReleaseAmt,i.trade_desc as tradeDesc,c.inv_rem as invReleaseAmt,e.col_cvcn - c.inv_pcount as releaseTerms,date_format(c.inv_fdate, '%d') as invRepayDate,truncate(c.inv_total / e.col_cvcn,2) as monthRepayAmt");
		sb.append(" from t_inv_act_ref a join t_inv c ON c.t_inv_seq = a.t_inv_seq and a.t_inv_seq = ");
		sb.append(invId);
		sb.append(" join t_acct b ON a.t_acct_seq = b.t_acct_seq");
		sb.append(" join t_loan_data d ON a.t_loan_data_seq = d.t_loan_data_seq");
		sb.append(" join t_dic e ON e.tab_name = 't_inv' and e.col_name = 'inv_period' and e.col_value = c.inv_period");
		sb.append(" left join (select t_inv_seq, sum(clm_pp_pri + clm_pp_int) as clm_pp_surplus from t_clm_pplan where clm_pp_stat <> 4 group by t_inv_seq) k ON a.t_inv_seq = k.t_inv_seq");
		sb.append(" left join t_act_order h on a.t_inv_seq=h.trade_ctpvalue and h.trade_type=25 and h.order_stat =3 ");
		sb.append(" left join (select a1.trade_ctpvalue,b1.trade_desc from t_act_order a1,t_act_oh b1 where a1.t_act_order_seq=b1.t_act_order_seq and b1.order_stat=4 order by t_act_oh_seq desc limit 1) i on a.t_inv_seq=i.trade_ctpvalue");
/*		sb.append(" left join (select count(cm) as ccm, t_inv_seq from (select t_inv_seq, extract(year_month from (date_add(act_inv_cdate,interval -cast(@rownum:=@rownum+1 as char(9)) month))) as cm");
		sb.append(" from t_act_inv e, (select @rownum:=0) f where extract(year_month from (date_add(act_inv_cdate, interval -cast(@rownum:=@rownum + 1 as char(9)) month))) is not null and e.act_inv_gform = 5) o");
		sb.append(" group by o.t_inv_seq) j on j.t_inv_seq=a.t_inv_seq");*/
		
/*		Query query = entityManager.createNativeQuery(sb.toString(), CapitalCalculateQueryDto.class);
		query.setParameter(1, invId);
		return query.getResultList();*/
		
		return entityManager.createNativeQuery(sb.toString(), "CapitalCalculateQueryDtoMapping").getResultList();
		
	}

}
