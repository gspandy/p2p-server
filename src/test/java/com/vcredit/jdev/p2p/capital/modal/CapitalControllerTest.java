package com.vcredit.jdev.p2p.capital.modal;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vcredit.jdev.p2p.Application;
import com.vcredit.jdev.p2p.base.P2pUtilTest;
import com.vcredit.jdev.p2p.dto.ThirdPayUserRequestDto;
import com.vcredit.jdev.p2p.dto.UnForzenCapitalDto;
import com.vcredit.jdev.p2p.repository.CapitalRecordRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CapitalControllerTest {
	private Logger logger = LoggerFactory.getLogger(CapitalControllerTest.class);
	@Autowired
    TestUtils testUtils;
	public String REST_SERVICE_URL;
	
	static Client client = null;

	@Autowired
	private CapitalAccountManager cAccount;
	@Autowired
	private CapitalPlatformManager cPlat;
	@Autowired
	private EventMessageGateway eventMessageGateway;
	@Autowired
	private InvestmentRepository investmentRepository;
	@Autowired
	private CapitalRecordRepository capitalRecordRepository;
	@Autowired
    P2pUtilTest p2pUtilTest;

	@BeforeClass
	public static void init() {
		//register(JacksonFeatures.class).
		client = ClientBuilder.newClient();
		client.register(JacksonJsonProvider.class);
	}
	@Before
	public void befor(){
		REST_SERVICE_URL = testUtils.getEmbeddedServletContainerBaseURL()+ "/data/ws/rest";
	}
	/**
	 * 检索资金账户
	 */
	@Test
	public void testQueryCapitalInfo() {
		String username="sz1108";
		String password="12345678";
		Cookie cookies = p2pUtilTest.getCurrentCookie(client, testUtils, REST_SERVICE_URL,username,password);
		//具体业务
		String path = "/capital/queryCapitalInfo";
		WebTarget target = client.target(REST_SERVICE_URL).path(path);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("accountSequence", "1");
		Invocation.Builder build = target.request(MediaType.APPLICATION_JSON);
		build.cookie(cookies);
		Response response = build.post(Entity.json(map));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@" + content);
		System.out.println("retrieveFile1() statusCode:" + statusCode);
		assert (statusCode == 200);
	}
	
	/**
	 * 获取项目资金信息
	 */
	@Test
	public void testQueryInvest() {
		Long invIdLong = Long.valueOf(167);
		//具体业务
		String path = "/capitalCalculate/queryInvestment";
		WebTarget target = client.target(REST_SERVICE_URL).path(path);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("invId", invIdLong);
		Invocation.Builder build = target.request(MediaType.APPLICATION_JSON);
		Response response = build.post(Entity.json(map));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@" + content);
		System.out.println("retrieveFile1() statusCode:" + statusCode);
		//assert (statusCode == 200);
	}
	
//	/**
//	 * 资金开户
//	 */
//	@Test
//	public void testOpenCapitalAccount() {
//		String path = "/capital/openCapitalAccount";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("isborrower", true);
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		assert (statusCode == 200);
//	}
//	
//	/**
//	 * 投资项目生成之后的激活
//	 */
//	@Test
//	public void testActivateCapitalInfo() {
//		String path = "/capital/activateCapitalInfo";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("investmentId", "11");
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		assert (statusCode == 200);
//	}
//	
//	/**
//	 * 资金冻结
//	 */
//	@Test
//	public void testForzenCapital() {
//		ForzenCapitalDto f=new ForzenCapitalDto();
//		f.setAccountOrderSequence(new BigDecimal(325641l));
//		f.setInvestmentSequence(2l);//投资项目
//		ThirdPaymentAccount tpa=capitalRecordRepository.findOne(8l);
//		f.setOrig(tpa);
//		f.setPrice(new BigDecimal(200.00));
//		cAccount.forzenCapital(f);
//	}
//	
//	/**
//	 * 资金解冻
//	 */
//	@Test
//	public void testUnForzenCapital() {
//		UnForzenCapitalDto f=new UnForzenCapitalDto();
//		f.setAccountOrderSequence(new BigDecimal(325642l));
//		f.setInvestmentSequence(2l);//投资项目
//		ThirdPaymentAccount tpa=capitalRecordRepository.findOne(8l);
//		f.setOrig(tpa);
//		cAccount.disForzenCapital(f);
//	}
//	
//	/**
//	 * 资金放款
//	 */
//	@Test
//	public void testCredit() {
//		cAccount.credit(1l, new BigDecimal(1l), "", "", new BigDecimal(10));
//	}
//	
//	/**
//	 * 资金充值
//	 */
//	@Test
//	public void testRechargeCapital() {
//		String path = "/channelPay/rechargeCapital";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("thirdPaymentAccount", "1");
//		map.put("bankCard", "1211");
//		map.put("price", "12");
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		assert (statusCode == 200);
//	}
//	
//	/**
//	 * 资金提现
//	 */
//	@Test
//	public void testDepositCapital() {
//		String path = "/channelPay/depositCapital";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("bankCard", "set");
//		map.put("price", "1");
//		map.put("thirdPaymentAccount", "testststs");
//		map.put("accountOrderSequence", "1");
//		map.put("bankCard", "1");
//		map.put("cashOperationRecordSequence", "1");
//		map.put("investmentSequence", "1");
//		map.put("openAcctId", "1");
//		map.put("price", "1");
//		map.put("reqExt", "1");
//		map.put("servFee", "1");
//		map.put("servFee", "1");
//		map.put("servFeeAcctId", "1");
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		assert (statusCode == 200);
//	}
//	
//	/**
//	 * 资金提现
//	 */
//	@Test
//	public void testPublishEvent() {
//		SpringIntegrationMessageDto<DepositCapitalDto> dto=new SpringIntegrationMessageDto<DepositCapitalDto>();
//		dto.setMessage(SpringIntegrationMessageEnum.DEPOSITCAPITAL);
//		DepositCapitalDto dto1 = new DepositCapitalDto();
//		dto1.setAccountOrderSequence(1l);
//		dto1.setInvestmentSequence(1l);
//		dto1.setCashOperationRecordSequence(1l);//用户充值/提现记录 流水号
//		
//		dto.setObj(dto1);
//		eventMessageGateway.publishEvent(dto);
//	}
//	
//	/**
//	 * 月收益变动率
//	 */
//	@Test
//	public void testQueryincomeChange() {
//		String path = "/capital/queryincomeChange";
//		WebTarget target = client.target(REST_SERVICE_URL).path(path);
//		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("currentDate", "2014-12-12T14:21:35.123Z");
//		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
//		int statusCode = response.getStatus();
//		String content = response.readEntity(String.class);
//		System.out.println("@" + content);
//		assert (statusCode == 200);
//	}
//	
	/**
	 * 计算金额
	 */
	@Test
	public void testCapitalMath() {
		String path = "/capital/capitalMath";
		WebTarget target = client.target(REST_SERVICE_URL).path(path);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("price", "10000");
		map.put("periods", "12");
		map.put("yearRate", "0.13");
		Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(map));
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);
		System.out.println("@" + content);
		assert (statusCode == 200);
	}
	
	/**
	 * 解冻
	 */
	@Test
	public void testDisForzenCapital() {
//		UnForzenCapitalDto dto = new UnForzenCapitalDto();
//		dto.setOrderId(new BigDecimal("1213145"));
//		dto.setTrxId("201501200000683897");//此值对应的是你主动投标中的frezeOrdId:201501200000383816
//		cAccount.disForzenCapital(dto);
	}
	
//	/**
//	 * 自动投标
//	 * @throws Exception 
//	 */
//	@Test
//	public void testAutoTender() throws Exception {
//		//"investmentId":"2","orderId":"1236584","transAmt":"200.00","usrId":"1","freezeOrdId":"1236584"
//		ThirdPayUserRequestDto dto = new ThirdPayUserRequestDto();
//		dto.setOrderId("1236541");
//		dto.setTransAmt("200.00");
//		dto.setFreezeOrdId("1236541");//此值对应的是你主动投标中的frezeOrdId
//		cAccount.autoTender(2l, dto,1l);
//	}
}
