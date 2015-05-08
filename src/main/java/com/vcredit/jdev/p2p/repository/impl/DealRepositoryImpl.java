package com.vcredit.jdev.p2p.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.dto.AutoInvestDto;
import com.vcredit.jdev.p2p.dto.ClaimGatherAmtDto;
import com.vcredit.jdev.p2p.dto.DealDetailListDto;
import com.vcredit.jdev.p2p.entity.ClaimGatherPlan;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.enums.AccountInvestmentStatusEnum;
import com.vcredit.jdev.p2p.enums.ActClmStatusEnum;
import com.vcredit.jdev.p2p.repository.DealRepository;

@Component
public class DealRepositoryImpl implements DealRepository {
	@PersistenceContext
	private EntityManager entityManager;

	//	@Autowired
	//	private EntityManagerFactory entityManagerFactory;

	@Override
	@SuppressWarnings("unchecked")
	public List<AutoInvestDto> getAutoInvestUserList() {
		StringBuffer sb = new StringBuffer("");
		sb.append("select ");
		sb.append("c.air_pmax as airPmax, ");
		sb.append("c.air_period as airPeriod, ");
		sb.append("c.air_clv as airClv, ");
		sb.append("c.air_stat as airStat, ");
		sb.append("c.air_type as airType,");
		sb.append(" c.t_acct_seq as tAcctSeq,");
		sb.append(" a.tac_rdate as tacRdate,");
		sb.append(" b.act_thblc - IFNULL(e.tradeAmt, 0)  as amt,");
		sb.append(" b.act_third as actThird, ");
		sb.append(" IFNULL(d.clm_gptotal, 0) as gptotal ");
		sb.append("from t_acct a ");

		sb.append("inner join t_act_third b ON a.t_acct_seq = b.t_acct_seq ");
		sb.append("inner join t_act_inv_rule c ON a.t_acct_seq = c.t_acct_seq and c.air_stat = '0' ");
		sb.append(" left outer join (");
		sb.append("     select IFNULL(Sum(clm_gptotal),0) as clm_gptotal,t_gacct_seq ");
		sb.append("     from t_clm_grecord  where DATE_FORMAT(clm_gdate, '%Y%m%d') = DATE_FORMAT(now(),'%Y%m%d') ");
		sb.append("     group by t_gacct_seq) d ");
		sb.append("on d.t_gacct_seq=a.t_acct_seq and d.clm_gptotal>50 ");

		sb.append(" left outer  join ( ");
		sb.append("     SELECT  ");
		sb.append("         IFNULL(sum(trade_amount), 0) as tradeAmt, ");
		sb.append("         t_pacct_seq  ");
		sb.append("     from t_act_order  ");
		sb.append("     where order_stat =7 and (trade_type =1 or trade_type =20)  ");
		sb.append("     group by t_pacct_seq ");
		sb.append("     ) e ");
		sb.append("     on e.t_pacct_seq = a.t_acct_seq ");

		sb.append(" WHERE b.act_thblc - IFNULL(e.tradeAmt, 0)   > 0 ");
		sb.append(" ORDER BY d.clm_gptotal desc, c.air_type, c.air_pmax, b.act_thblc - IFNULL(e.tradeAmt, 0) desc,a.tac_rdate ");
		return entityManager.createNativeQuery(sb.toString(), "AutoInvestDtoMapping").getResultList();

	}

	/**
	 * 收款记录的实际收本金
	 * 
	 * @return
	 */
	@Override
	public BigDecimal getSumActualPrincipal(Long actInvSeq) {

		String sqlString = "select IFNULL(sum(b.clm_gnum),0) from t_act_clm a inner join t_clm_grecord b on a.t_act_clm_seq=b.t_act_clm_seq where t_act_inv_seq = ?1";
		Query query = entityManager.createNativeQuery(sqlString, BigDecimal.class);
		query.setParameter(1, actInvSeq);
		return (BigDecimal) query.getSingleResult();

	}

	@Override
	public int insertSelectActClm(Long investmentSequence, Long transferSequence) {
		String sqlString = "insert into t_act_clm(t_act_inv_seq,act_clm_pqt,act_clm_aqt,act_clm_gpp,act_clm_udays,act_clm_ddays,act_clm_status,act_clm_cdate) select ?1 as t_act_inv_seq,act_clm_pqt,act_clm_aqt,act_clm_gpp,act_clm_udays,act_clm_ddays,act_clm_status,act_clm_cdate from t_act_clm where t_act_inv_seq =?2 ";
		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter(1, investmentSequence);
		query.setParameter(2, transferSequence);
		return query.executeUpdate();
	}

	@Override
	public int updateTransferActClm(Long transferSequence) {
		String sqlString = "update t_act_clm set act_clm_status= ?1,act_clm_cdate= now() where t_act_inv_seq =?2 AND act_clm_status <> ?3 ";
		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter(1, ActClmStatusEnum.TRANSFERED);
		query.setParameter(2, transferSequence);
		query.setParameter(3, ActClmStatusEnum.REPAYMENT);
		return query.executeUpdate();
	}

	@Override
	@Modifying
	public int updateOverdueActClm(Long investmentSequence, Integer periodOfTime, int delayDays) {
		//		EntityManager em = entityManagerFactory.createEntityManager();
		//		if (!em.getTransaction().isActive()) {
		//			em.getTransaction().begin();
		//		}
		String sqlString = "update t_act_clm set act_clm_status=?1, act_clm_cdate= now(), act_clm_ddays=?4 where act_clm_gpp = ?3 "
				+ "and t_act_inv_seq in (select  t_act_inv_seq from t_act_inv where t_inv_seq =?2)";
		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter(1, ActClmStatusEnum.OVERDUE.getCode());
		query.setParameter(2, investmentSequence);
		query.setParameter(3, periodOfTime);
		query.setParameter(4, delayDays);
		int cnt = query.executeUpdate();
		//		em.getTransaction().commit();
		return cnt;
	}

	@Override
	@Modifying
	public int updateOverdueActInv(Long investmentSequence, AccountInvestmentStatusEnum accountInvestmentStatusEnum) {
		String sqlString = "update t_act_inv set act_inv_stat=?1, rec_edate= now() where t_inv_seq = ?2 ";

		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter(1, accountInvestmentStatusEnum.getCode());
		query.setParameter(2, investmentSequence);
		int cnt = query.executeUpdate();
		return cnt;
	}

	@Override
	@Modifying
	public int updateActInv(Long investmentSequence, Integer periodOfTime) {
		String sqlString = "update t_act_inv set act_inv_gpp = ?2, rec_edate= now() where t_inv_seq = ?1 ";

		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter(1, investmentSequence);
		query.setParameter(2, periodOfTime);

		int cnt = query.executeUpdate();
		return cnt;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ClaimGatherPlan> findClaimGatherPlan(Long investmentSequence, Integer periodOfTime) {
		String sqlString = "select c.* from t_inv a inner join t_act_inv b on b.t_inv_seq=a.t_inv_seq inner join t_clm_gplan c on  c.t_act_inv_seq=b.t_act_inv_seq where a.t_inv_seq=?1 and c.clm_gp_num = ?2";
		Query query = entityManager.createNativeQuery(sqlString, ClaimGatherPlan.class);
		query.setParameter(1, investmentSequence);
		query.setParameter(2, periodOfTime);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Investment> findRepurchaseList() {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("proc_get_ctn", Investment.class);
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.IN);

		query.setParameter(1, 1);
		query.setParameter(2, "11");
		query.setParameter(3, "1");
		query.setParameter(4, 1);
		query.setParameter(5, 3);

		//		query.registerStoredProcedureParameter("v_t_inv_seq", Integer.class, ParameterMode.IN);
		//		query.registerStoredProcedureParameter("v_t_inv_num", String.class, ParameterMode.IN);
		//		query.registerStoredProcedureParameter("v_buss_code", String.class, ParameterMode.IN);
		//		query.registerStoredProcedureParameter("v_inv_src", Integer.class, ParameterMode.IN);
		//		query.registerStoredProcedureParameter("v_continue_month", Integer.class, ParameterMode.IN);
		//		query.setParameter("v_t_inv_seq", 14);
		//		query.setParameter("v_t_inv_num", "p2p001");
		//		query.setParameter("v_buss_code", "vbs001");
		//		query.setParameter("v_inv_src", 1);
		//		query.setParameter("v_continue_month", 3);

		//		query.setParameter("v_t_inv_seq", 14);
		//		query.setParameter("v_t_inv_num", "");
		//		query.setParameter("v_buss_code", null);
		//		query.setParameter("v_inv_src", 1);
		//		query.setParameter("v_continue_month", 3);
		//		boolean isResult = query.hasMoreResults();
		//		while (isResult) {
		//			return query.getResultList();
		//		}

		boolean isResult = query.hasMoreResults();
		if (isResult) {
			return query.getResultList();
		}
		return null;
	}

	@Override
	public String findAccountThirdBySequence(Long accountSequence) {
		String sqlString = "select b.act_third from t_acct a inner join t_act_third b ON a.t_acct_seq = b.t_acct_seq where a.t_acct_seq = ?1 ";
		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter(1, accountSequence);
		return (String) query.getSingleResult();
	}

	@Override
	public Date getSystemTime() {
		String sqlString = "select now() ";
		Query query = entityManager.createNativeQuery(sqlString);
		return (Date) query.getSingleResult();
	}

	/**
	 * in v_t_acct_seq bigint, 用户流水号<BR>
	 * in v_sdate datetime, 起始时间 <BR>
	 * in v_edate datetime, 结束时间<BR>
	 * in v_order_stat int, 订单状态 <BR>
	 * in v_trade_type int, 交易类型 13红包奖励 14充值 15取现<BR>
	 * in v_date_type int 日期类型 3个月-3 6个月-6 1年-12
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Investment> findDealList(Long accountSeq, Date startDate, Date endDate, Integer orderStat, Integer tradeType, Integer dateType) {

		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("proc_q6");
		query.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(3, Date.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(4, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(5, Integer.class, ParameterMode.IN);
		query.registerStoredProcedureParameter(6, Integer.class, ParameterMode.IN);

		query.setParameter(1, accountSeq);
		query.setParameter(2, startDate);
		query.setParameter(3, endDate);
		query.setParameter(4, orderStat);
		query.setParameter(5, tradeType);
		query.setParameter(6, dateType);
		boolean isResult = query.execute();
		if (isResult) {
			List<Object> list = query.getResultList();
			for (Object object : list) {
				Object[] arr = (Object[]) object;

				//				DealDetailListDto dealListDto = new DealDetailListDto();
				//				dealListDto.setAcctSeq(Long.valueOf(String.valueOf(arr[0])));//付费用户流水号
				//				dealListDto.setActOrderSeq(Long.valueOf(String.valueOf(arr[1])));//用户订单流水号 
				//dealListDto

				//				dealListDto.setTradeDate((Date) arr[2]);
				//				dealListDto.setTradeDate((Date) arr[3]);
				System.out.println(object.toString());
			}
			//			System.out.println(query.getMaxResults());
			return query.getResultList();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DealDetailListDto> findDealDetialList(Long accountSeq, Date startDate, Date endDate, Integer orderStat, Integer tradeType,
			Integer dateType) {

		StringBuffer sb = new StringBuffer("");
		sb.append("select a.tradeDate,  ");
		sb.append("  a.tradeType,  ");
		sb.append("  CONVERT(a.orderId,CHAR) as orderId,  ");
		sb.append("  a.tradeAmount,  ");
		sb.append("  a.balance,  ");
		sb.append("  a.target,  ");
		sb.append("  a.tradeDesc FROM ( ");
		//"############################充值  ;
		sb.append("select   ");
		sb.append("trade_date as tradeDate,    ");
		sb.append("'网银充值' as tradeType,    ");
		sb.append("order_id as orderId,  ");
		sb.append("trade_amount as tradeAmount, ");
		sb.append("act_gthblc as balance,   ");
		sb.append("'无' as target,  ");
		sb.append("'无' as tradeDesc   ");
		sb.append(" from t_act_order where t_pacct_seq=1 and trade_type = 16 and order_stat in(3,4)  ");
		sb.append("  ");
		//sb.append("############################提现  ");
		sb.append("union all  ");
		sb.append("  ");
		sb.append("select ");
		sb.append("trade_date as tradeDate,  ");
		sb.append("'提现' as tradeType,  ");
		sb.append("order_id as orderId,  ");
		sb.append("trade_amount as tradeAmount,  ");
		sb.append("act_gthblc as balance,  ");
		sb.append("'无' as target,  ");
		sb.append("'无' as tradeDesc  ");
		sb.append(" from t_act_order where t_pacct_seq=1 and trade_type = 17 and order_stat in(3,4)  ");
		sb.append("  ");
		//sb.append("############################提现手续费  ");
		sb.append("union all  ");
		sb.append("select   ");
		sb.append("apr_gdate as tradeDate,  ");
		sb.append("'提现手续费' as tradeType,  ");
		sb.append("trade_ctpvalue as orderId ,  ");
		sb.append("apr_amount as tradeAmount,  ");
		sb.append("0 as balance,     ");
		sb.append("'无' as target,  ");
		sb.append("'无' as tradeDesc  ");
		sb.append("  ");
		sb.append(" from t_act_precord   ");
		sb.append("where t_pacct_seq =1 and  ");
		sb.append("apr_type =5 and apr_stat=3   ");
		sb.append("  ");
		//sb.append("############################投资冻结  ");
		sb.append("union all  ");
		sb.append("  ");
		sb.append("select   ");
		sb.append("trade_date as tradeDate,  ");
		sb.append("'投资冻结' as tradeType,  ");
		sb.append("order_id as orderId ,  ");
		sb.append("trade_amount as tradeAmount,  ");
		sb.append("act_pthblc as balance,     ");
		sb.append("'无' as target,  ");
		sb.append("'无' as tradeDesc  ");
		sb.append(" from t_act_order   ");
		sb.append("where t_pacct_seq=1 and order_stat =7 and (trade_type =1 or trade_type =20)   ");
		sb.append("  ");
		//sb.append("############################投资资金解冻  ");
		sb.append("union all  ");
		sb.append("  ");
		sb.append("select   ");
		sb.append("trade_date as tradeDate,  ");
		sb.append("'投资资金解冻' as tradeType,  ");
		sb.append("order_id as orderId ,  ");
		sb.append("trade_amount as tradeAmount,  ");
		sb.append("act_pthblc as balance,     ");
		sb.append("'无' as target,  ");
		sb.append("'无' as tradeDesc  ");
		sb.append(" from t_act_order   ");
		sb.append("where t_pacct_seq=1 and order_stat =12 and trade_type =3   ");
		sb.append("  ");
		//sb.append("############################投资放款  ");
		sb.append("union all  ");
		sb.append("  ");
		sb.append("select   ");
		sb.append("trade_date as tradeDate,  ");
		sb.append("'投资放款' as tradeType,  ");
		sb.append("order_id as orderId ,  ");
		sb.append("trade_amount as tradeAmount,  ");
		sb.append("act_pthblc as balance,     ");
		sb.append("'无' as target,  ");
		sb.append("'无' as tradeDesc  ");
		sb.append(" from t_act_order   ");
		sb.append("where t_pacct_seq=1 and order_stat =3 and trade_type =2   ");
		sb.append("  ");
		//sb.append("############################本息收入  ");
		sb.append("union all  ");
		sb.append("select   ");
		sb.append("trade_date as tradeDate,  ");
		sb.append("'本息收入' as tradeType,  ");
		sb.append("order_id as orderId ,  ");
		sb.append("trade_amount as tradeAmount,  ");
		sb.append("act_pthblc as balance,     ");
		sb.append("'无' as target,  ");
		sb.append("'无' as tradeDesc  ");
		sb.append(" from t_act_order   ");
		sb.append("where t_pacct_seq=1 and order_stat =3 and trade_type =4   ");
		sb.append("  ");
		//sb.append("############################投资管理费  ");
		sb.append("union all  ");
		sb.append("select   ");
		sb.append("apr_adate as tradeDate,  ");
		sb.append("'投资管理费' as tradeType,  ");
		sb.append("trade_ctpvalue as orderId,  ");
		sb.append("apr_amount as tradeAmount,  ");
		sb.append("0 as balance,     ");
		sb.append("'无' as target,  ");
		sb.append("'无' as tradeDesc  ");
		sb.append("from t_act_precord  ");
		sb.append("where t_pacct_seq=1 and apr_stat =3 and apr_type =6   ");
		sb.append("  ");
		//sb.append("############################罚息收入  ");
		sb.append("union all  ");
		sb.append("select   ");
		sb.append("clm_gdate as tradeDate,  ");
		sb.append("'罚息收入' as tradeType,  ");
		sb.append("t_clm_grecord_seq as orderId,  ");
		sb.append("clm_gpjint as tradeAmount,  ");
		sb.append("0 as balance,     ");
		sb.append("'无' as target,  ");
		sb.append("'无' as tradeDesc   ");
		sb.append("from  ");
		sb.append("t_clm_grecord  ");
		sb.append("where t_gacct_seq=10   ");
		//sb.append("############################垫付本息收入  ");
		sb.append("union all  ");
		sb.append("select ");
		sb.append("trade_date as tradeDate,  ");
		sb.append("'本息收入' as tradeType,  ");
		sb.append("order_id as orderId , ");
		sb.append("trade_amount as tradeAmount, ");
		sb.append("act_pthblc as balance,  ");
		sb.append("'无' as target,  ");
		sb.append("'无' as tradeDesc  ");
		sb.append(" from t_act_order   ");
		sb.append(" where t_pacct_seq=1 and order_stat =3 and trade_type =27 ");

		sb.append(" ) a ");
		List<DealDetailListDto> rsltList = entityManager.createNativeQuery(sb.toString(), "DealDetailListDtoMapping").getResultList();
		return rsltList;
	}

	public List<ClaimGatherAmtDto> findSumByGatherAccountSequence(Long accountSequence) {
		StringBuffer sb = new StringBuffer("");

		sb.append("select sum(IFNULL(clm_gpint, 0) + IFNULL(clm_gpjint, 0)) AS clmGptotal, DATE_FORMAT(clm_gdate, '%Y%m') AS clmGnum from t_clm_grecord where t_gacct_seq = ");
		sb.append(accountSequence);
		sb.append(" AND DATE_FORMAT(clm_gdate, '%Y%m') between DATE_FORMAT(DATE_ADD(now(), INTERVAL -5 MONTH),'%Y%m') and DATE_FORMAT(now(),'%Y%m') group by DATE_FORMAT(clm_gdate, '%Y%m')");
		List<ClaimGatherAmtDto> rsltList = entityManager.createNativeQuery(sb.toString(), "ClaimGatherRecordMapping").getResultList();
		return rsltList;
	}
}
