package com.vcredit.jdev.p2p.pdf.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.vcredit.jdev.p2p.account.modal.MobileManager;
import com.vcredit.jdev.p2p.pdf.convert.PdfInfos;
import com.vcredit.jdev.p2p.pdf.exception.ErrorCodeException.CommonException;
import com.vcredit.jdev.p2p.pdf.util.PdfUtil;

@MessageEndpoint
public class PdfManagerActivitor {

	private static Logger logger = LoggerFactory.getLogger(PdfManagerActivitor.class);

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
	
	/**
	 * 放款成功后生成pdf合同系列事件
	 * @param event
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ServiceActivator(inputChannel = "asyncEventPublishChannel")
	public void handle(Object event) throws Exception {
		if (event instanceof PdfInfos) {
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
		}
	}
	
}