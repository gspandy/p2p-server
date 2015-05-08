package com.vcredit.jdev.p2p.deal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

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
import com.vcredit.jdev.p2p.entity.InvestmentAccountReference;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.repository.DealRepository;
import com.vcredit.jdev.p2p.repository.InvestmentAccountReferenceRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;
import com.vcredit.jdev.p2p.util.CollectionUtil;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class ReleaseCashTest {

	private static final String TYPE_JSON = "application/json";
	//	private final String REST_SERVICE_URL = "http://localhost:8080/p2p/data/ws/rest";

	static Client client = null;

	@Autowired
	private TestUtils testUtils;

	@Autowired
	private DealRepository dealRepository;
	@Autowired
	private ReleaseCashServiceManager releaseCashServiceManager;
	@Autowired
	private LoanDataRepository loanDataRepository;
	@Autowired
	private InvestmentAccountReferenceRepository investmentAccountReferenceRepository;

	private final static String ACCOUNT_SEQ = "accountSeq";
	private final static String TRADE_AMT = "tradeAmt";
	private final static String GATHER_PER_MONTH = "gatherPerMonth";

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);
	}

	@Test
	public void testDailyPaymentStatus() {
		Map<String, Object> body = new HashMap<String, Object>();

		String beginDate = "Tue Jan 12 2014 00:00:00 GMT+0800 (中国标准时间)";
		String endDate = "Tue Jan 13 2016 13:20:21 GMT+0800 (中国标准时间)";

		body.put("beginDate", beginDate);
		body.put("endDate", endDate);

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/dailyPaymentStatus");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void testDailyPaymentStatusNULL() {
		Map<String, Object> body = new HashMap<String, Object>();

		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/dailyPaymentStatus");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void releaseCash() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("investmentSequence", "30");//20 23
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/releaseCash");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	@Test
	public void autoWithdraw() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("investmentSequence", "7");
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/deal/autoWithdraw");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}

	//	@Test
	//	public void executeSuccess() {
	//		releaseCashServiceManager.executeSuccess(56L);
	//	}

	@Test
	public void saveAccountContract() {
		Long investmentSequence = 215L;
		Long borrowerAccountSequence = null;
		LoanData loanData = null;

		List<InvestmentAccountReference> investmentAccountReferenceList = investmentAccountReferenceRepository
				.findByInvestmentSequence(investmentSequence);
		if (!CollectionUtil.isEmpty(investmentAccountReferenceList)) {
			//贷款人不能为空
			loanData = loanDataRepository.findOne(investmentAccountReferenceList.get(0).getLoanDataSequence());

			borrowerAccountSequence = investmentAccountReferenceList.get(0).getAccountSequence();
		}

		Map<String, String> hashmap = new HashMap<String, String>();
		hashmap.put(ACCOUNT_SEQ, String.valueOf(102329L));
		hashmap.put(TRADE_AMT, String.valueOf(500));
		hashmap.put(GATHER_PER_MONTH, String.valueOf(100));

		List<Map<String, String>> usersMap = new ArrayList<Map<String, String>>();
		usersMap.add(hashmap);
		Integer investmentPeriod = 12;

		List<Long> toUsers = new ArrayList<Long>();
		//生成合同
		releaseCashServiceManager.saveAccountContract(investmentSequence, 0.13D, loanData, investmentPeriod, borrowerAccountSequence, new Date(),
				usersMap, toUsers, loanData.getP2pPayFeeRate());
	}

}
