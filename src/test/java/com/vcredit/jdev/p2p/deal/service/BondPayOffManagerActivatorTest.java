package com.vcredit.jdev.p2p.deal.service;

import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vcredit.jdev.p2p.Application;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.enums.ClaimPayPlanStatusEnum;
import com.vcredit.jdev.p2p.enums.InvestmentStatusEnum;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanHistoryRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class BondPayOffManagerActivatorTest {

	private static final String TYPE_JSON = "application/json";
	//	private final String REST_SERVICE_URL = "http://localhost:8080/p2p/data/ws/rest";

	static Client client = null;

	@Autowired
	private TestUtils testUtils;

	@Autowired
	private BondPayOffManagerActivator bondPayOffManagerActivator;

	@Autowired
	private ClaimPayPlanHistoryRepository claimPayPlanHistoryRepository;

	@Autowired
	private ClaimPayPlanRepository claimPayPlanRepository;
	@Autowired
	private DealRepository dealRepository;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);

	}

	@Test
	public void isCurrentAccountInvested() {
		List<ClaimPayPlan> claimPayPlanList = claimPayPlanRepository.findTodayAllInvestmentNotPaid(ClaimPayPlanStatusEnum.NOT_PAID.getCode(),
				ClaimPayPlanStatusEnum.OVERDUE.getCode(), ClaimPayPlanStatusEnum.OVERDUE_PAID.getCode(),
				InvestmentStatusEnum.AUTO_WITHDRAW_SUCCESS.getCode());
		final Date sysDate = dealRepository.getSystemTime();
		//bondPayOffManagerActivator.insertClaimPayPlanHistory(sysDate, claimPayPlanList.get(0));
	}
}
