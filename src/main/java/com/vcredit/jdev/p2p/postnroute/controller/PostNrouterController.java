package com.vcredit.jdev.p2p.postnroute.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.AbstractConfigurableEmbeddedServletContainer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.pdf.convert.PdfInfos;
import com.vcredit.jdev.p2p.pdf.exception.ErrorCodeException.CommonException;
import com.vcredit.jdev.p2p.pdf.model.FtpDataManager;
import com.vcredit.jdev.p2p.pdf.model.MailDataManager;
import com.vcredit.jdev.p2p.pdf.model.PdfDataManager;
import com.vcredit.jdev.p2p.pdf.util.FtpUtils;
import com.vcredit.jdev.p2p.pdf.util.PdfUtil;

/**
 * 发送汇付路由，统一传入参数 json字符窜格式 Ex：jsonStr={"url":
 * "-channelPay-registerparam","param": {"usrId": 22081,"yyy": "abc"}}
 * jsonStr、url、param为定值
 * 
 * @author zhaopeijun
 */
@Controller
@RequestMapping("/acct")
public class PostNrouterController {

	private static Logger logger = LoggerFactory.getLogger(PostNrouterController.class);

	@Autowired
	private FtpUtils ftpUtils;

	@Autowired
	private PdfUtil pdfUtil;
	//	pdfUtil.getTempPdfPath() + pdfName;

	@Autowired
	private MailDataManager mailDataManager;

	@Autowired
	private PdfDataManager pdfDataManager;

	@Autowired
	private FtpDataManager ftpDataManager;

	@Autowired
	AbstractConfigurableEmbeddedServletContainer tomcatEmbeddedServletContainerFactory;

	/**
	 * 发送汇付路由，统一传入参数 json字符窜格式 * Ex：jsonStr={"url":
	 * "-channelPay-registerparam","param": {"usrId": 22081,"yyy": "abc"}}
	 * jsonStr、url、param为定值
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@RequestMapping("/postnroute")
	public String pnRouter(Model model, HttpServletRequest request) throws JsonProcessingException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		String contextPath = tomcatEmbeddedServletContainerFactory.getContextPath();
		logger.debug("tomcatEmbeddedServletContainerFactory.getContextPath()={}", contextPath);
		String baseApi = contextPath + "/data/ws/rest";

		//处理访问p2p后台 url
		String jsonStr = request.getParameter("jsonStr");
		String url = P2pUtil.getJosnByKey(jsonStr, "url");
		url = url.replace("-", "/").replace("\"", "");
		url = baseApi + url;
		logger.debug("收到postnroute请求={}", url);

		//处理访问p2p后台 参数
		String con = "";
		String param = P2pUtil.getJosnByKey(jsonStr, "param");
		con = "{";
		if (!StringUtils.isEmpty(param) && (map = mapper.readValue(param, new TypeReference<HashMap<String, String>>() {
		})).size() > 0) {

			for (Map.Entry<String, String> entry : map.entrySet()) {
				con += "\"" + entry.getKey() + "\"" + ":" + "\"" + entry.getValue() + "\"" + ",";
			}
			con = con.substring(0, con.length() - 1);
		}
		con += "}";
		model.addAttribute("url", url);
		model.addAttribute("con", con);
		logger.debug("处理好请求参数con={},url={},即将访问postnroute.html,系统时间={}", con, url, DateFormatUtil.getCurrentTime(DateFormatUtil.STAND_DATE_FROMAT));

		return "account/postnroute";
	}

	/**
	 * p2p平台链接触发，生成投资人pdf
	 * 
	 * @param response
	 * @param contractId 合同编号
	 * @return
	 */
	@RequestMapping(method = { RequestMethod.GET }, value = "/vesterpdfback")
	public ModelAndView contract(HttpServletResponse response, String contractId) {
		OutputStream out = null;
		String msg = null;
		try {
			logger.debug("step[vesterPdf] begin--- 前台投资人触发 pdf系列事件开始,系统时间={}", DateFormatUtil.getCurrentTime(DateFormatUtil.STAND_DATE_FROMAT));
			//生成投资人pdf
			PdfInfos vo = pdfDataManager.getPdfData(null, contractId);
			logger.debug("step[vesterPdf] 获取投资人合同信息 ok,合同号={}", contractId);

			//added temp
			/*
			 * PdfInfos vo = new PdfInfos(); ArrayList lendList = new
			 * ArrayList(); Lender lender = new Lender();
			 * lender.setInvesterName("Bill");
			 * lender.setInvesterPid("310115198210278512");
			 * lender.setInvesterRealName("比尔盖茨");
			 * lender.setInvestmentAmount(new BigDecimal(12000));
			 * lender.setLendPeriod(12); lender.setMontyPayAmount(new
			 * BigDecimal(1100)); lendList.add(lender);
			 * 
			 * Lender lender2 = new Lender(); lender2.setInvesterName("Bill2");
			 * lender2.setInvesterPid("310115198210278513");
			 * lender2.setInvesterRealName("比尔盖茨2");
			 * lender2.setInvestmentAmount(new BigDecimal(12000));
			 * lender2.setLendPeriod(12); lender2.setMontyPayAmount(new
			 * BigDecimal(1100)); lendList.add(lender2);
			 * 
			 * vo.setLenderList(lendList);
			 * 
			 * vo.setContractNo("V12345-Vester");
			 * vo.setInvestReleaseYmd("2015-3-27");
			 * vo.setBorrowerRealName("许三多");
			 * vo.setBorrowerPid("310115198210278888");
			 * vo.setBorrowerUserName("Xu3duo");
			 * vo.setBorrowerEmail("xingzhiyang2016@163.com");
			 * 
			 * vo.setLoanTarget("大事"); vo.setLoanAmount("12888");
			 * vo.setInvestmentAnnualInterestRate(new BigDecimal(0.1300));
			 * vo.setInvestmentPeriod(12); vo.setMontyPayAmount(new
			 * BigDecimal(1150)); vo.setP2pPayFeeRate(new BigDecimal(0.02));
			 * vo.setP2pPayFee(new BigDecimal(100)); vo.setPayDay("15");
			 * vo.setInvestmentManageFeeRate(new BigDecimal(0.03));
			 * vo.setJusticInterestRate(new BigDecimal(0.001));
			 * vo.setEarlierCleanMonth(6); //
			 * 
			 * Investment inv = new Investment(); inv.setInvestmentSequence(new
			 * Long(9)); vo.setInvestment(inv);
			 */
			//added temp end
			out = response.getOutputStream();
			String pdfName = pdfDataManager.generatePdf4Vester(vo);
			logger.debug("step[vesterPdf] 生成投资人版本pdf ok，pdf名：" + pdfName);

			String filePath = pdfUtil.getTempPdfPath() + pdfName;
			byte[] content = ftpUtils.getBytes(filePath);
			response.setHeader("Content-disposition", "attachment;filename=" + pdfName);//设置文件名
			response.setContentType("application/pdf");
			ByteArrayInputStream in = new ByteArrayInputStream(content);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();

			logger.debug("step[vesterPdf] 生成投资人版本pdf成功。");
			//删除 tmp目录下pdf
			boolean flag = ftpDataManager.deleteTempFile(pdfUtil.getTempPdfPath() + pdfName);
			logger.debug("step删除投资人版本pdf成功：{}", flag);

		} catch (IOException e) {
			logger.error("step[vesterPdf] download contract error：IOException={}", e);
			msg = "生成投资人pdf失败";
		} catch (CommonException e) {
			msg = e.getErrorMsg();
		} catch (Exception e) {
			logger.error("step[vesterPdf] 生成投资人pdf失败:" + e.getLocalizedMessage());
			msg = e.getMessage();
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				logger.error("close resource error", e);
			}
		}

		try {
			if (StringUtils.isNotBlank(msg)) {
				response.setContentType("text/html");
				response.setCharacterEncoding("utf-8");
				out.write(msg.getBytes("utf-8"));
				//                out.flush();
			}
		} catch (IOException e) {
			logger.error("write msg to page error", e);
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				logger.error("close resource error", e);
			}
		}

		logger.debug("step[vesterPdf] end--- 前台投资人触发 pdf系列事件结束");
		return null;
	}

	/**
	 * p2p平台链接触发，生成投资人pdf,带跳转错误页面
	 * @param model
	 * @param response
	 * @param contractId
	 * @return
	 */
	@RequestMapping(method = { RequestMethod.GET }, value = "/vesterpdf")
	public String vesterContractCreate(Model model,HttpServletResponse response, String contractId) {
		OutputStream out = null;
		String msg = null;
		try {
			
			logger.debug("step[vesterPdf] begin--- 前台投资人触发 pdf系列事件开始,系统时间={}", DateFormatUtil.getCurrentTime(DateFormatUtil.STAND_DATE_FROMAT));
			//生成投资人pdf
			PdfInfos vo = pdfDataManager.getPdfData(null, contractId);
			logger.debug("step[vesterPdf] 获取投资人合同信息 ok,合同号={}", contractId);

			String pdfName = pdfDataManager.generatePdf4Vester(vo);
			logger.debug("step[vesterPdf] 生成投资人版本pdf ok，pdf名：" + pdfName);

			out = response.getOutputStream();
			String filePath = pdfUtil.getTempPdfPath() + pdfName;
			byte[] content = ftpUtils.getBytes(filePath);
			response.setHeader("Content-disposition", "attachment;filename=" + pdfName);//设置文件名
			response.setContentType("application/pdf");
			ByteArrayInputStream in = new ByteArrayInputStream(content);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();

			logger.debug("step[vesterPdf] 生成投资人版本pdf成功。");
			//删除 tmp目录下pdf
			boolean flag = ftpDataManager.deleteTempFile(pdfUtil.getTempPdfPath() + pdfName);
			logger.debug("step删除投资人版本pdf成功：{}", flag);

		} catch (IOException e) {
			logger.error("step[vesterPdf] download contract error：IOException={}", e);
//			msg = "生成投资人pdf失败";
			msg = "contractIOhasError";
		} catch (CommonException e) {
//			msg = e.getErrorMsg();
			msg = "contractDataError";
		} catch (Exception e) {
			logger.error("step[vesterPdf] 生成投资人pdf失败:" + e.getLocalizedMessage());
//			msg = e.getMessage();
			msg = "downloadContractError";
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				logger.error("close resource error", e);
			}
		}

		if (StringUtils.isNotBlank(msg)) {
			model.addAttribute("msg", msg);
			return "account/vesterContractRoute";
		}

		logger.debug("step[vesterPdf] end--- 前台投资人触发 pdf系列事件结束");
		return null;
	}

}
