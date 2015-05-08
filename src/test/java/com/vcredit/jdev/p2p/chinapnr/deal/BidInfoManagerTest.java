package com.vcredit.jdev.p2p.chinapnr.deal;

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
import com.vcredit.jdev.p2p.base.util.P2pUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class BidInfoManagerTest {
	@Autowired
	private BidInfoManager bidInfoManager;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAddBidInfo() {
		//4150797903098128
        String proId = P2pUtil.generateRandom(16);
        String borrCustId = "6000060000670670";
        String borrTotAmt = "1000.00";
        String yearRate = "0.10";
        String retType = "01";
        String bidStartDate = "20141225000000";
        String bidEndDate =   "20150126000000";
        
        String retAmt = "154.00";
        String retDate = "20151025";
        String proArea = "1100";
        System.out.println(bidInfoManager.addBidInfo(proId, borrCustId, borrTotAmt, yearRate, retType, bidStartDate, bidEndDate, retAmt, retDate, "", "", proArea));
		
	}

	@Test
	public void testAutoTender() {
	}

}
