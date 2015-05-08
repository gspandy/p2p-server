package com.vcredit.jdev.p2p.chinapnr.query;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.vcredit.jdev.p2p.Application;
import com.vcredit.jdev.p2p.chinapnr.util.SignUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class QueryManagerTest{

	@Autowired
	private QueryManager balanceManager;
	
	@Autowired
	private SignUtils sUtils;
	
	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void testGetQueryAcctDetailsParams() throws Exception {
		//System.out.println(balanceManager.getQueryAcctDetailsParams("6000060000657800"));
	}

	@Test
	public void testQueryBalanceBg() {
		System.out.println(balanceManager.queryBalanceBg("6000060000670670"));
	}

	@Test
	public void testQueryTransStat() {
		System.out.println(balanceManager.queryTransStat("003754", "20141014", "LOANS"));
	}


	@Test
	public void testQueryAccts() {
		System.out.println(balanceManager.queryAccts());
	}

	@Test
	public void testQueryTenderPlan() {
		System.out.println(balanceManager.queryTenderPlan("6000060000622535"));
	}

	@Test
	public void testQueryReturnDzFee() {
		System.out.println(balanceManager.queryReturnDzFee("20141201", "20141230"));
	}

	@Test
	public void testQueryCardInfo() {
		System.out.println(balanceManager.queryCardInfo("6000060000670670", ""));
	}

	@Test
	public void testCorpRegisterQuery() {
		//System.out.println(balanceManager.corpRegisterQuery(busiCode));
	}

	@Test
	public void testCreditAssignReconciliation() {
		//System.out.println(balanceManager.creditAssignReconciliation(ordId, beginDate, endDate, pageNum, pageSize, sellCustId, buyCustId));
	}
	


	@Test
	public void testQueryTrfReconciliation() {
		System.out.println(balanceManager.queryTrfReconciliation("20141201", "20141230", "1", "100"));
	}

	@Test
	public void testQueryCashReconciliation() {
		System.out.println(balanceManager.queryCashReconciliation("20141201", "20141230", "1", "100"));
	}

	@Test
	public void testQuerySaveReconciliation() {
		System.out.println(balanceManager.querySaveReconciliation("20141201", "20141230", "1", "100"));
	}

	@Test
	public void testQueryReconciliation() {
		System.out.println(balanceManager.queryReconciliation("20141201", "20141230", "1", "100","LOANS"));
	}
	
	@Test
	public void testQueryUsrInfo() {
		System.out.println(balanceManager.queryUsrInfo("31011019840430325X"));
	}
	
	@Test
	public void testQueryTransDetail() {
		System.out.println(balanceManager.queryTransDetail("96189499977146214451", "SAVE"));
	}


}
