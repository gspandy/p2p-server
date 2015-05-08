package com.vcredit.jdev.p2p.merchant.modal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vcredit.jdev.p2p.Application;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.LoanData;
import com.vcredit.jdev.p2p.enums.LoanDataStatusEnum;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.LoanDataRepository;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MerchantControllerTest {
	@Autowired
    TestUtils testUtils;
	public String REST_SERVICE_URL;
	
	static Client client = null;
	@Autowired
	private MerchantManager merchantManager;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private InvestmentRepository investmentRepository;
	@Autowired
	private LoanDataRepository loanDataRepository;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);
	}
	
	@Before
	public void befor(){
		REST_SERVICE_URL = testUtils.getEmbeddedServletContainerBaseURL()+ "/data/ws/rest";
	}
	
	/**
	 * 检索项目
	 */
	@Test
	public void testQueryCapitalInfo() {
//		String path = "/merchant/queryInvestProjectInfo";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Map body = new HashMap();
//		List<Long> loanData=new ArrayList<Long>();
//		loanData.add(12l);
//		body.put("investmentStatus", loanData);
//		body.put("page", "0");
//		body.put("size", "1");
//		body.put("investmentStartDate", "2014-12-18T02:15:16.756Z");
//		body.put("investmentEndDate", "2015-1-18T02:15:16.756Z");
//		
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(body));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		System.out.println("retrieveFile1() statusCode:" + statusCode);
//		assert (statusCode == 200);
	}
//	
//	/**
//	 * 激活项目
//	 */
//	@Test
//	public void testActivateInvestProject() {
//		String path = "/merchant/activateInvestProject";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		for(int i=0;i<3;i++){
//			Investment c = new Investment();
//			c.setInvestmentSequence(i+1l);
//			Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(c));
//			int statusCode = response.getStatus();
//			String content = response.readEntity(String.class);
//			System.out.println("@" + content);
//			System.out.println("retrieveFile1() statusCode:" + statusCode);
//			assert (statusCode == 200);
//		}
//	}
//	
//	/**
//	 * 流标
//	 */
//	@Test
//	public void testInvestmentFlowBid() {
//		String path = "/merchant/investmentFlowBid";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Long c=1l;
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(c));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		System.out.println("retrieveFile1() statusCode:" + statusCode);
//		assert (statusCode == 200);
//	}
//	
//	/**
//	 * 自动上线
//	 */
//	@Test
//	public void investmentOnline() {
//		String path = "/merchant/investmentOnline";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(3));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		System.out.println("retrieveFile1() statusCode:" + statusCode);
//		assert (statusCode == 200);
//	}
//	/**
//	 * 定时上线
//	 */
//	@Test
//	public void testInvestmentTimeingOnline() {
//		String path = "/merchant/investmentTimeingOnline";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Response response = target.request(MediaType.APPLICATION_JSON).post(null);
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		System.out.println("retrieveFile1() statusCode:" + statusCode);
//		assert (statusCode == 200);
//	}
//
//	/**
//	 * 测试组合标的
//	 */
//	@Test
//	public void testComponentProject(){
//		String path = "/merchant/componentProject";
//		Map body = new HashMap();
//		List<LoanData> res=loanDataRepository.findByRecordStatus(LoanDataStatusEnum.COMPLETE_P2P_ACCOUNT_CREATE.getCode(),new PageRequest(0,10));
//		List<Long> loanData=new ArrayList<Long>();
//		for(LoanData l:res){
//			loanData.add(l.getLoanDataSequence());
//		}
//		body.put("loanDataList", loanData);
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(body));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		System.out.println("retrieveFile1() statusCode:" + statusCode);
//		assert (statusCode == 200);
//	}
//	
//	/**
//	 * 测试单个标的
//	 */
//	@Test
//	public void testSignleProject(){
//		String path = "/merchant/signleProject";
//		Map body = new HashMap();
//		List<LoanData> res=loanDataRepository.findByRecordStatus(LoanDataStatusEnum.COMPLETE_P2P_ACCOUNT_CREATE.getCode(),new PageRequest(0,10));
//		List<Long> loanData=new ArrayList<Long>();
//		for(LoanData l:res){
//			loanData.add(l.getLoanDataSequence());
//		}
//		body.put("loanDataList", loanData);
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(body));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		System.out.println("retrieveFile1() statusCode:" + statusCode);
//		assert (statusCode == 200);
//	}
//	
//	/**
//	 * 测试注册
//	 */
//	@Test
//	public void testRegist(){
//		String path = "/acctparam/registerparam";
//		Map body = new HashMap();
//		List<LoanData> res=loanDataRepository.findByRecordStatus(LoanDataStatusEnum.COMPLETE_P2P_ACCOUNT_CREATE.getCode(),new PageRequest(0,10));
//		List<Long> loanData=new ArrayList<Long>();
//		for(LoanData l:res){
//			loanData.add(l.getLoanDataSequence());
//		}
//		body.put("loanDataList", loanData);
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(body));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		System.out.println("retrieveFile1() statusCode:" + statusCode);
//		assert (statusCode == 200);
//	}
}
