package com.vcredit.jdev.p2p.chinapnr.deal;

import static org.junit.Assert.*;

import org.apache.commons.lang3.math.NumberUtils;
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
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.chinapnr.query.QueryManager;
import com.vcredit.jdev.p2p.repository.AccountCashOperationRecordRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class LoansManagerTest {
	
	@Autowired
	private LoansManager loansManager;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testTransfer() {
        String ordId = P2pUtil.generate20Random();
        String outCustId = "6000060000622535";
        String outAcctId = "SDT000003";
        String transAmt = "0.50";
        String inCustId = "6000060000622535";
        String inAcctId = "MDT000001";
        //System.out.println( loansManager.transfer(ordId, outCustId, outAcctId, transAmt, inCustId, inAcctId,""));
       
	}

	@Test
	public void testRepay() {
        String merCustId = "6000060000622535";
        String ordId = P2pUtil.generate20Random();
        String ordDate = "20141225";
        
        String outCustId = "6000060000670670";
        String subOrdId = "1452864198";
        String subOrdDate = "20141224";
        String transAmt = "20.01";
        String fee = "0.00";
        
        String inCustId = "6000060000657800";
        //System.out.println( loansManager.repay(ordId, ordDate, outCustId, transAmt, fee, subOrdId, subOrdDate, inCustId, "", "",""));
		
	}

	@Test
	public void testLoans() {
		
	        String merCustId = "6000060000622535";
	        String ordId = P2pUtil.generate20Random();
	        String ordDate = "20141014";
	        String outCustId = "6000060000657800";
	        String transAmt = "20.01";
	        String fee = "0.00";
	        String subOrdId = "1452864198";
	        String subOrdDate = "20141224";
	        String inCustId = "6000060000670670";
	        String divDetails = "";
	        String isDefault = "N";
	       // System.out.println( loansManager.loans(ordId, ordDate, outCustId, transAmt, fee, subOrdId, subOrdDate, inCustId, divDetails, isDefault, isUnFreeze, unFreezeOrdId, freezeTrxId, feeObjFlag));
		
	}

}
