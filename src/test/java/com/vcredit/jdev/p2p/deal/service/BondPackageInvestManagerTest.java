package com.vcredit.jdev.p2p.deal.service;

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
import com.vcredit.jdev.p2p.repository.AccountOrderRepository;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class BondPackageInvestManagerTest {

	private static final String TYPE_JSON = "application/json";
	private final String REST_SERVICE_URL = "http://localhost:8080/p2p/data/ws/rest";

	static Client client = null;

	@Autowired
	private DealRepository autoInvestRepository;

	@Autowired
	private BondPackageInvestManager bondPackageInvestManager;

	/** 用户订单 */
	@Autowired
	private AccountOrderRepository accountOrderRepository;

	@Autowired
	private ReleaseCashServiceManager releaseCashServiceManager;

	@Autowired
	TestUtils testUtils;

	@BeforeClass
	public static void init() {
		//register(JacksonFeatures.class).
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);

	}

	@Test
	public void testAutoInvestRepository() {
		releaseCashServiceManager.executeSuccess(108L);
	}

	@Test
	public void autoInvest() {

		//boolean isResult = bondPackageInvestManager.autoInvest();

		//System.out.println(isResult);
	}

	//	@Test
	//	public void investFailure() {
	//
	//		Long investmentSequence = 8L;
	//		bondPackageInvestManager.investFailure(investmentSequence);
	//
	//		List<AccountOrder> accountOrderList = accountOrderRepository.findByCommodityTablePrimaryKeyValueAndOrderStatusAndTradeType(
	//				investmentSequence, OrderStatusEnum.FREEZE_SUCCESS.getCode(), TradeTypeEnum.INVEST.getCode(), TradeTypeEnum.INVEST_MANUAL.getCode());
	//	}
}
