package com.vcredit.jdev.p2p.pdf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import net.gplatform.sudoor.server.integration.AsyncEventMessageGateway;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vcredit.jdev.p2p.Application;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.pdf.convert.Lender;
import com.vcredit.jdev.p2p.pdf.convert.PdfInfos;
import com.vcredit.jdev.p2p.pdf.util.PdfUtil;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PdfControllerTest {
	
	@Autowired
	TestUtils testUtils;

	public String REST_SERVICE_URL;

	private static final String TYPE_JSON = "application/json";
	
	@Autowired
	private AsyncEventMessageGateway asyncEventMessageGateway;
	
	@Autowired
	private InvestmentRepository investmentRepository;

	static Client client = null;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
		client.register(JacksonJsonProvider.class);
	}

	@Before
	public void setUp() throws Exception {
		REST_SERVICE_URL = testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest";
	}
	
	@Autowired
	private PdfUtil pdfUtil;
	
	@Test
	public void testPdf() {
		Map body = new HashMap();
		
		PdfInfos vo = new PdfInfos();
		ArrayList lendList = new ArrayList();
		Lender lender = new Lender();
		lender.setInvesterName("Lin");
		lender.setInvesterPid("310115198210278512");
		lender.setInvesterRealName("王建林");
		lender.setInvestmentAmount(new BigDecimal(12000.58));
		lender.setLendPeriod(12);
		lender.setMontyPayAmount(new BigDecimal(1100.68));
		lendList.add(lender);
		
		Lender lender2 = new Lender();
		lender2.setInvesterName("Cong");
		lender2.setInvesterPid("310115198210278513");
		lender2.setInvesterRealName("王思聪");
		lender2.setInvestmentAmount(new BigDecimal(12000.78));
		lender2.setLendPeriod(9);
		lender2.setMontyPayAmount(new BigDecimal(1100.88));
		lendList.add(lender2);
		
		vo.setLenderList(lendList);
		
		vo.setContractNo("Vht12345bb");
		vo.setInvestReleaseYmd("2015-3-27");
		vo.setBorrowerRealName("test318Realname");
		vo.setBorrowerPid("310115198210278888");
		vo.setBorrowerUserName("test318");
		vo.setBorrowerEmail("xingzhiyang2016@163.com");
		
		vo.setLoanTarget("干大事情");
		vo.setLoanAmount("12888");
		vo.setInvestmentAnnualInterestRate(new BigDecimal(0.1300).multiply(new BigDecimal(100)));
		vo.setInvestmentPeriod(12);
		vo.setMontyPayAmount(new BigDecimal(1150));
		vo.setP2pPayFeeRate(new BigDecimal(0.02));
		vo.setP2pPayFee(new BigDecimal(100));
		vo.setPayDay("2015-03-15");
		vo.setInvestmentManageFeeRate(new BigDecimal(0.03));
		vo.setJusticInterestRate(new BigDecimal(0.001));
		vo.setEarlierCleanMonth(6); //
		
		Investment inv = new Investment();
		inv.setInvestmentSequence(new Long(9));
		vo.setInvestment(inv);
		
		body.put("vos", vo);
		
//		WebTarget target = client.target(REST_SERVICE_URL).path("/pdf/testPdf");
//		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
//		System.out.println(response);
	}
	
	/**
	 * 模拟
	 */
	@Test
	public void testSync(){
		PdfInfos pdfInfos = new PdfInfos();
		pdfInfos.setContractNo("V010200215010000406");
		Investment investment = investmentRepository.findOne(new Long(422));
		pdfInfos.setInvestment(investment);

//		asyncEventMessageGateway.publishEvent(pdfInfos);
	}
	
}
