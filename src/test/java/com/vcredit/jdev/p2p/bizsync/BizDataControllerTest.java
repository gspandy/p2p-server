package com.vcredit.jdev.p2p.bizsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

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
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.enums.BizSyncEnum;
import com.vcredit.jdev.p2p.util.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BizDataControllerTest {

	@Autowired
	TestUtils testUtils;

	public String REST_SERVICE_URL;

	private static final String TYPE_JSON = "application/json";

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

	@SuppressWarnings("unchecked")
	private HashMap makeData(String i){
		//借款人
		HashMap borrower = new HashMap();
		borrower.put("idCard", "310115198211177300");
		borrower.put("customerType", "个体户");
		borrower.put("name", "张三");
		borrower.put("gender", "男");
		borrower.put("educationDegree", "本科");
		borrower.put("marryStatus", "已婚");
		borrower.put("mobile", "18213727385");
		borrower.put("email", "123@163.com");
//		borrower.put("email", "");
		borrower.put("hometown", "上海市");
		borrower.put("workCity", "北京市");
		borrower.put("companyInustry", "互联网金融");
		borrower.put("companySize", "小型企业（50~300人）");
//		borrower.put("companySize", "");
		borrower.put("workTime", "60");
		borrower.put("department", "销售部");
		borrower.put("accountIndustry", "销售助理");
		borrower.put("companyProperty", "外商独资");
		borrower.put("income", "8000.5");
		borrower.put("salaryDate", "15日");
		borrower.put("houseProperty", "租赁");
		
		//银行卡
		HashMap cardInfo = new HashMap();
		cardInfo.put("bankCardNumber", "1234567890123456");
		cardInfo.put("bankCode", "11");
		cardInfo.put("bankProvince", "130000");
		cardInfo.put("bankCity", "130100");
		
		//借款项目信息
		HashMap borrowInfo = new HashMap();
		borrowInfo.put("investmentSource", "维信");
		borrowInfo.put("investmentBusinessCode", "LBS12221***"+i);
		borrowInfo.put("investmentTarget", "学习进修");
		borrowInfo.put("investmentPeriod", "12");
		borrowInfo.put("accountLoanAmount", "20000.5");
		borrowInfo.put("contractNumber", "ht00001");
		borrowInfo.put("credibilityLevel", "B");
		borrowInfo.put("investmentAnnualInterestRate", "0.13");
		borrowInfo.put("p2pEarlyPayFeeRate", "0.1150");
		borrowInfo.put("p2pPayFeeRate", "0.1155");
		borrowInfo.put("p2pOperateFeeRate", "0.0015");
		borrowInfo.put("signDate", "2015.01.15 13:31:48");
		
		HashMap data = new HashMap();
		data.put("borrower", borrower);
		data.put("cardInfo", cardInfo);
		data.put("borrowInfo", borrowInfo);
		return data;
	}
	
	public HashMap makeDataAfterLoan(String i){
		HashMap billInfo = new HashMap();
		billInfo.put("investmentSource", 1);
		billInfo.put("billType", "2");
		billInfo.put("investmentBusinessCode", "ABCDxxx2");
		billInfo.put("loanPeriod", "");
		billInfo.put("totalShouldGet", "6888.00");
		billInfo.put("totalActulGet", "6889.00");
		billInfo.put("cutShouldDate", "2015.01.16 13:31:48");
		return billInfo;
	}
	
	public HashMap makeDataBeforeLoanQuery(int i){
		HashMap queryInfo = new HashMap();
		queryInfo.put("loanbId", 268888+i);
		return queryInfo;
	}
	
	@Test
	public void testSyncInstant() {
		/*Map body = new HashMap();
		body.put(ConstantsUtil.SyncParam.SYNC_TYPE,BizSyncEnum.sync_instant.getCode());
		body.put(ConstantsUtil.SyncParam.SYNC_DATA, makeData(String.valueOf(Math.random()*100)));
		WebTarget target = client.target(REST_SERVICE_URL).path("/bizsync/dataimport");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);*/
	}

	@Test
	public void testSyncBatch() {
		/*Map body = new HashMap();
		body.put(ConstantsUtil.SyncParam.SYNC_TYPE,BizSyncEnum.sync_batch.getCode());
		List array = new ArrayList();
		for (int i = 1; i < 4; i++) {
			array.add(makeData(String.valueOf(Math.random()*100)));
		}
		body.put(ConstantsUtil.SyncParam.SYNC_DATA, array);
		WebTarget target = client.target(REST_SERVICE_URL).path("/bizsync/dataimport");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);*/
	}
	
	@Test
	public void testSyncAfterLoan(){
		/*Map body = new HashMap();
		List array = new ArrayList();
		for (int i = 1; i < 3; i++) {
			array.add(makeDataAfterLoan(String.valueOf(Math.random()*100)));
		}
		body.put(ConstantsUtil.SyncParam.SYNC_DATA, array);
		WebTarget target = client.target(REST_SERVICE_URL).path("/bizsync/afterloanImport");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);*/
	}
	
	
	@Test
	public void testGetVbsQueryList() {
		Map body = new HashMap();
		List array = new ArrayList();
		for (int i = 1; i < 3; i++) {
			array.add(makeDataBeforeLoanQuery(i));
		}
		body.put("loan_list", array);
		WebTarget target = client.target(REST_SERVICE_URL).path("/bizsync/beforeloanQuery");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}
	
	/*@Test
	public void testPdf() {
		Map body = new HashMap();
		WebTarget target = client.target(REST_SERVICE_URL).path("/test/testPdf");
		String response = target.request(TYPE_JSON).post(Entity.json(body), String.class);
		System.out.println(response);
	}*/
	
}
