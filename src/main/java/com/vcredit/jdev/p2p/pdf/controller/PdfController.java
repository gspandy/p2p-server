package com.vcredit.jdev.p2p.pdf.controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.gplatform.sudoor.server.integration.AsyncEventMessageGateway;

import org.apache.commons.lang3.StringUtils;
import org.hsqldb.lib.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcredit.jdev.p2p.account.modal.MobileManager;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.P2pUtil;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.enums.ResourceEnum;
import com.vcredit.jdev.p2p.pdf.convert.PdfInfos;
import com.vcredit.jdev.p2p.pdf.exception.ErrorCodeException.CommonException;
import com.vcredit.jdev.p2p.pdf.model.FtpDataManager;
import com.vcredit.jdev.p2p.pdf.model.MailDataManager;
import com.vcredit.jdev.p2p.pdf.model.PdfDataManager;
import com.vcredit.jdev.p2p.pdf.util.PdfUtil;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;

/**
 * 放款后生成pdf合同
 * 
 * @author zhaopeijun
 *
 */
@MessageEndpoint
@Path("/pdf")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PdfController {

	private static Logger logger = LoggerFactory.getLogger(PdfController.class);

	@Autowired
	private PdfUtil pdfUtil;

	@Autowired
	private PdfDataManager pdfDataManager;

	@Autowired
	private FtpDataManager ftpDataManager;

	@Autowired
	private MailDataManager mailDataManager;

	@Autowired
	private MobileManager mobileManager;
	
	@Autowired
	private AsyncEventMessageGateway asyncEventMessageGateway;
	
	@Autowired
	private InvestmentRepository investmentRepository;

	/**
	 * 放款成功后生成pdf合同系列事件
	 * @param event
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ServiceActivator(inputChannel = "asyncEventPublishChannel")
	public void handle(Object event) throws Exception {
		/*if (event instanceof PdfInfos) {
//			logger.getClass()
			logger.debug("step[vbsPdf] begin--- 放款后触发 pdf系列事件开始");
			String pdfNameVbs = "";
			String pdfNameBorrower = "";
			PdfInfos initPdfInfos = ((PdfInfos) event);
			try {
				//获取合同信息
				initPdfInfos = pdfDataManager.getPdfData(initPdfInfos, "");
				logger.debug("step[vbsPdf] 获取生成VBS、借款人用的合同信息 ok,合同号：" + initPdfInfos.getContractNo());

				//VBS---begin
				//临时目录 生成VBS版本--------------
				logger.debug("step[vbsPdf] 获取到的合同信息"+initPdfInfos);
				pdfNameVbs = pdfDataManager.generatePdf(initPdfInfos, false);
				logger.debug("step[vbsPdf] 生成VBS版本pdf ok，pdf名：" + pdfNameVbs);

				//上传FTP，存档
				String pdfInFtpPath = ftpDataManager.uploadPdf(pdfNameVbs);
				logger.debug("step[vbsPdf] VBS版本pdf上传FTP ok,ftp路径：" + pdfInFtpPath);

				//VBS版本pdf路径 更新 资源表
				pdfDataManager.savePdfResource(initPdfInfos.getInvestment().getInvestmentSequence(), pdfNameVbs);
				logger.debug("step[vbsPdf] VBS版本pdf路径 更新资源表 ok");

				//Borrower---begin， 借款人pdf生成的前提是 VBS版本pdf 成功处理后
				//临时目录 生成借款人版本--------------
//				pdfInfos = ((PdfInfos) event);
//				pdfInfos = initPdfInfos;
				logger.debug("step[vbsPdf] 生成借款人版本pdf in");
				pdfNameBorrower = pdfDataManager.generatePdf(initPdfInfos, true);
				logger.debug("step[vbsPdf] 生成借款人版本pdf ok，pdf名：" + pdfNameBorrower);

				//发送邮件给借款人
				try {
					logger.debug("step[vbsPdf] 发送邮件给借款人 in");
					mailDataManager.sendMail2Borrower(initPdfInfos, pdfNameBorrower);
					logger.debug("step[vbsPdf] 发送邮件给借款人 ok,pdf名："+pdfNameBorrower);
				} catch (Exception e) {
					// 发送邮件失败不影响后续流程，暂记日志处理 学彦确认
					logger.debug("step[vbsPdf] 发送邮件给借款人 fail,pdf名：" + pdfNameBorrower + ",错误详细：" + e.getLocalizedMessage());
				}

				//当邮件发送完成，给借款人发送短信通知
				logger.debug("step[vbsPdf] 当邮件发送完成，给借款人发送短信通知 in");
				mobileManager.sendMoblieMessageToBorrower(initPdfInfos.getBorrowerUserName(), initPdfInfos.getBorrowerEmail());
				logger.debug("step[vbsPdf] 当邮件发送完成，给借款人发送短信通知 ok");

			} catch (CommonException e) {
				logger.error("step处理异常， 放款后生成pdf事件 整体流程失败，合同号：" + initPdfInfos.getContractNo() + ",标的seq：" + initPdfInfos.getInvestment().getInvestmentNumber() + "，错误信息"
						+ e.getErrorMsg());
			} finally {
				//删除临时目录下pdf文件（VBS版本）
				logger.debug("step[vbsPdf] 删除临时目录下pdf文件（VBS版本） in");
				ftpDataManager.deleteTempFile(pdfUtil.getTempPdfPath() + pdfNameVbs);
				logger.debug("step[vbsPdf] 删除临时目录下pdf文件（VBS版本） ok，删除文件：" + pdfUtil.getTempPdfPath() + pdfNameVbs);

				//删除临时目录下pdf文件（借款人版本）
				logger.debug("step 删除临时目录下pdf文件（借款人版本） in");
				ftpDataManager.deleteTempFile(pdfUtil.getTempPdfPath() + pdfNameBorrower);
				logger.debug("step 删除临时目录下pdf文件（借款人版本） ok，删除文件：" + pdfUtil.getTempPdfPath() + pdfNameBorrower);
			}

			logger.debug("step[vesterPdf] end--- 放款后触发 pdf系列事件完成");
		}*/
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/testPdf")
	public Response testPdf(Map<String, Object> paramMap) throws Exception {
		Object ob = paramMap.get("vos");

		ObjectMapper objectMapper = new ObjectMapper();
		String dtoJson = objectMapper.writeValueAsString(ob);
		PdfInfos vo = P2pUtil.getBeanFromJson(dtoJson, PdfInfos.class);
		logger.debug("step junit begin--- 放款后触发 pdf系列事件开始");
		/*
		 * ftpDataManager.downloadPdf("", "", ""); if(1 ==1){
		 * 
		 * return Response.response("test generate pdf ok"); }
		 */
		
		

//		//获取合同信息
//		vo = pdfDataManager.getPdfData(vo, "");
		
		//生成vbs版本------------
		String pdfName = pdfDataManager.generatePdf(vo, false);
		logger.debug("step生成VBS版本pdf ok，pdf名：" + pdfName);
		
		/*if (1 == 1) {
			return Response.response("debug ok");
		}*/

//		//上传FTP，存档
//		String pdfInFtpPath = ftpDataManager.uploadPdf(pdfName);
//		logger.debug("step VBS版本pdf上传FTP ok,ftp路径：" + pdfInFtpPath);
//
//		if (1 == 1) {
//			return Response.response("debug ok");
//		}

//		//更新 资源表
//		pdfDataManager.savePdfResource(vo.getInvestment().getInvestmentSequence(), pdfName);
//		logger.debug("step VBS版本pdf路径 更新资源表 ok");
//
//		//删除临时目录下pdf文件（VBS版本）
//		ftpDataManager.deleteTempFile(pdfUtil.getTempPdfPath() + pdfName);

		//生成借款人版本--------------
		vo = P2pUtil.getBeanFromJson(dtoJson, PdfInfos.class);
		String pdfPathNaBorrower = pdfDataManager.generatePdf(vo, true);
		logger.debug("step 生成借款人版本pdf ok，pdf名：" + pdfPathNaBorrower);

//		//发邮件
//		try {
//			mailDataManager.sendMail2Borrower(vo, pdfPathNaBorrower);
//			logger.debug("step 发送邮件给借款人 ok,pdf名："+pdfPathNaBorrower);
//		} catch (Exception e) {
//			// 发送邮件失败不影响后续流程，暂记日志处理 学彦确认
//			logger.debug("step 发送邮件给借款人 fail,pdf名：" + pdfPathNaBorrower + ",错误详细：" + e.getLocalizedMessage());
//		}

//		//当邮件发送完成，给借款人发送短信通知
//		mobileManager.sendMoblieMessageToBorrower(vo.getBorrowerUserName(), vo.getBorrowerEmail());
//		logger.debug("step 当邮件发送完成，给借款人发送短信通知 ok");
//
//		//删除 tmp目录下pdf
//		ftpDataManager.deleteTempFile(pdfUtil.getTempPdfPath() + pdfPathNaBorrower);
//		logger.debug("step 删除临时目录下pdf文件（借款人版本） ok，删除文件：" + pdfUtil.getTempPdfPath() + pdfPathNaBorrower);
//		logger.debug("step junit end--- 放款后触发 pdf系列事件完成");
		
		return Response.response("test generate pdf ok");
	}
	
	/**
	 * 查找Pdf是否生成成功
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/getPdfResource")
	public Response getPdfResource(Map<String, Object> paramMap) throws Exception{
		if (paramMap.get("investmentID") == null || StringUtil.isEmpty(String.valueOf(paramMap.get("investmentID")))) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, "项目ID不能为空");
		}
		Long number = Long.valueOf((String) paramMap.get("investmentID"));
		
		Map<String ,Object> param = pdfDataManager.findResourceName(number);
		return Response.successResponse(param);
	}
	
	/**
	 * 生成PDF
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Path("/createPdfResource")
	public Response createPdfResource(Map<String, Object> paramMap) throws Exception{
		Map<String ,Object> backparam = new HashMap<String, Object>();
		if (paramMap.get("investmentID") == null || StringUtil.isEmpty(String.valueOf(paramMap.get("investmentID")))) {
			backparam.put("exist", ResourceEnum.IS_NO_FIND.getCode());
			backparam.put("error", ResponseConstants.CommonCode.NOT_DEFINE_CODE);
			return Response.successResponse(backparam);
		}
		
		Long number = Long.valueOf((String) paramMap.get("investmentID"));
		
		PdfInfos pdf = new PdfInfos();
		String param = pdfDataManager.getcontractNumber(number);
		if(StringUtils.isBlank(param)){
			backparam.put("exist", ResourceEnum.IS_NO_FIND.getCode());
			backparam.put("error", ResponseConstants.CommonCode.NOT_DEFINE_CODE);
			return Response.successResponse(backparam);
		}
		pdf.setContractNo(param);
		
		Investment investment = investmentRepository.findOne(number);
		pdf.setInvestment(investment);
		

//		logger.getClass()
		logger.debug("step[vbsPdf] begin--- 放款后触发 pdf系列事件开始");
		String pdfNameVbs = "";
		String pdfNameBorrower = "";
		PdfInfos initPdfInfos = pdf;
		try {
			//获取合同信息
			initPdfInfos = pdfDataManager.getPdfData(initPdfInfos, "");
			logger.debug("step[vbsPdf] 获取生成VBS、借款人用的合同信息 ok,合同号：" + initPdfInfos.getContractNo());

			//VBS---begin
			//临时目录 生成VBS版本--------------
			logger.debug("step[vbsPdf] 获取到的合同信息"+initPdfInfos);
			pdfNameVbs = pdfDataManager.generatePdf(initPdfInfos, false);
			logger.debug("step[vbsPdf] 生成VBS版本pdf ok，pdf名：" + pdfNameVbs);

			//上传FTP，存档
			String pdfInFtpPath = ftpDataManager.uploadPdf(pdfNameVbs);
			logger.debug("step[vbsPdf] VBS版本pdf上传FTP ok,ftp路径：" + pdfInFtpPath);

			//VBS版本pdf路径 更新 资源表
			pdfDataManager.savePdfResource(initPdfInfos.getInvestment().getInvestmentSequence(), pdfNameVbs);
			logger.debug("step[vbsPdf] VBS版本pdf路径 更新资源表 ok");

			//Borrower---begin， 借款人pdf生成的前提是 VBS版本pdf 成功处理后
			//临时目录 生成借款人版本--------------
//			pdfInfos = ((PdfInfos) event);
//			pdfInfos = initPdfInfos;
			logger.debug("step[vbsPdf] 生成借款人版本pdf in");
			pdfNameBorrower = pdfDataManager.generatePdf(initPdfInfos, true);
			logger.debug("step[vbsPdf] 生成借款人版本pdf ok，pdf名：" + pdfNameBorrower);

			//发送邮件给借款人
			try {
				logger.debug("step[vbsPdf] 发送邮件给借款人 in");
				mailDataManager.sendMail2Borrower(initPdfInfos, pdfNameBorrower);
				logger.debug("step[vbsPdf] 发送邮件给借款人 ok,pdf名："+pdfNameBorrower);
			} catch (Exception e) {
				// 发送邮件失败不影响后续流程，暂记日志处理 学彦确认
				logger.debug("step[vbsPdf] 发送邮件给借款人 fail,pdf名：" + pdfNameBorrower + ",错误详细：" + e.getLocalizedMessage());
			}

			//当邮件发送完成，给借款人发送短信通知
			logger.debug("step[vbsPdf] 当邮件发送完成，给借款人发送短信通知 in");
			mobileManager.sendMoblieMessageToBorrower(initPdfInfos.getBorrowerUserName(), initPdfInfos.getBorrowerEmail());
			logger.debug("step[vbsPdf] 当邮件发送完成，给借款人发送短信通知 ok");

		} catch (CommonException e) {
			logger.error("step处理异常， 放款后生成pdf事件 整体流程失败，合同号：" + initPdfInfos.getContractNo() + ",标的seq：" + initPdfInfos.getInvestment().getInvestmentNumber() + "，错误信息"
					+ e.getErrorMsg());
			backparam.put("exist", ResourceEnum.IS_NO_FIND.getCode());
			backparam.put("error", ResponseConstants.CommonCode.EXCEPTION_CODE);
			return Response.successResponse(backparam);
		} finally {
			//删除临时目录下pdf文件（VBS版本）
			logger.debug("step[vbsPdf] 删除临时目录下pdf文件（VBS版本） in");
			ftpDataManager.deleteTempFile(pdfUtil.getTempPdfPath() + pdfNameVbs);
			logger.debug("step[vbsPdf] 删除临时目录下pdf文件（VBS版本） ok，删除文件：" + pdfUtil.getTempPdfPath() + pdfNameVbs);

			//删除临时目录下pdf文件（借款人版本）
			logger.debug("step 删除临时目录下pdf文件（借款人版本） in");
			ftpDataManager.deleteTempFile(pdfUtil.getTempPdfPath() + pdfNameBorrower);
			logger.debug("step 删除临时目录下pdf文件（借款人版本） ok，删除文件：" + pdfUtil.getTempPdfPath() + pdfNameBorrower);
		}

		logger.debug("step[vesterPdf] end--- 放款后触发 pdf系列事件完成");
		backparam.put("exist", ResourceEnum.IS_FIND.getCode());
		backparam.put("error", ResponseConstants.CommonCode.SUCCESS_CODE);
		return Response.successResponse(backparam);
		
	}
}
