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
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.entity.ClaimPayPlan;
import com.vcredit.jdev.p2p.repository.AccountClaimRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.repository.ClaimPayPlanRepository;
import com.vcredit.jdev.p2p.util.DateUtil;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class BondTransferManagerTest {

	private static final String TYPE_JSON = "application/json";
	private final String REST_SERVICE_URL = "http://localhost:8080/p2p/data/ws/rest";

	static Client client = null;

	@Autowired
	TestUtils testUtils;

	@Autowired
	private BondTransferManager bondTransferManager;

	@Autowired
	private DealRepository autoInvestRepository;
	@Autowired
	private AccountClaimRepository accountClaimRepository;
	@Autowired
	private ClaimPayPlanRepository claimPayPlanRepository;

	@BeforeClass
	public static void init() {
		//register(JacksonFeatures.class).
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);

	}

	@Test
	public void testSearchPrepaidList() {

		//		List<ClaimPayPlan> list = bondTransferManager.searchPrepaidList();
		//		for (ClaimPayPlan o : list) {
		//			System.out.println(o.getClaimPayPlanPeriod().toString());
		//			//			System.out.println(o.getGatherThirdPaymentId().toString());
		//		}
	}

	@Test
	public void bondPrepaid() {
		//boolean isSucc = bondTransferManager.bondPrepaid();
		Date baseDate = null;
		//项目来源是维信
		if (Integer.valueOf(DateFormatUtil.getNowDay()) > 27) {
			StringBuffer source = new StringBuffer();
			source = source.append(DateFormatUtil.getNowYear()).append(DateFormatUtil.getNowMonth()).append(27);
			Date date = DateUtil.toDateType(source.toString());
			baseDate = DateUtil.addDay(date, 1);
		} else {
			baseDate = DateUtil.addDay(new Date(), 1);
		}
		baseDate = DateUtil.toDateType("20150210");

		// 更新债权还款计划 账单日
		List<ClaimPayPlan> cliamPayPlanList = claimPayPlanRepository.findByInvestmentSequence(908L);

		for (ClaimPayPlan claimPayPlan : cliamPayPlanList) {
			Integer claimPayPlanNumber = claimPayPlan.getClaimPayPlanNumber();
			claimPayPlan.setClaimPayPlanNatureDate(DateUtil.addMonth(baseDate, claimPayPlanNumber));
		}

		claimPayPlanRepository.save(cliamPayPlanList);
	}

	@Test
	public void updateOverdueActClm() {
		Long investmentSequence = 813L;
		Integer periodOfTime = 21;

		System.out.println(autoInvestRepository.updateOverdueActClm(investmentSequence, periodOfTime, 1));

		//		WaccountClaimRepository.updateOverdueActClm(ActClmStatusEnum.OVERDUE.getCode(), investmentSequence, periodOfTime);
	}

}
