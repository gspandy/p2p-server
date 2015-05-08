package com.vcredit.jdev.p2p.pdf.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.alert.service.AccountMessageChannelEnum;
import com.vcredit.jdev.p2p.alert.service.AccountMessageManager;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateData;
import com.vcredit.jdev.p2p.alert.service.AccountMessageTemplateEnum;
import com.vcredit.jdev.p2p.pdf.convert.PdfInfos;
import com.vcredit.jdev.p2p.pdf.exception.CommonDefinedException;
import com.vcredit.jdev.p2p.pdf.exception.ErrorCodeException.CommonException;
import com.vcredit.jdev.p2p.pdf.util.PdfUtil;

/**
 * 
 * @author zhaopeijun
 *
 */
@Component
public class MailDataManager {

	private static Logger logger = LoggerFactory.getLogger(MailDataManager.class);
	
	@Autowired
	private PdfUtil pdfUtil;
	
	@Autowired
	private AccountMessageManager accountMessageManager;
	
	/**
	 * 发送邮件给借款人
	 * @param info
	 * @param pdfName
	 */
	public void sendMail2Borrower(PdfInfos info,String pdfName){
		//临时路径生成pdf文件
		String tempPathNa = pdfUtil.getTempPdfPath() + pdfName;
		String period = info.getInvestmentPeriod().toString();		
		sendBorrowerEmailMessage(info.getBorrowerEmail(),info.getBorrowerUserName(),info.getLoanAmount(),period,info.getPayDay(),tempPathNa);
	}
	
	/**
	 * @Title: sendBorrowerEmailMessage
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param email
	 *            借款人Email
	 * @param borrowerUserName
	 *            借款人用户名
	 * @param total
	 *            总金额
	 * @param period
	 *            期限
	 * @param payDay
	 *            还款日
	 * @param filePath
	 *            协议附件完整路径
	 * @return void 返回类型
	 * @throws
	 */
	public void sendBorrowerEmailMessage(String email, String borrowerUserName, String total, String period, String payDay, String filePath) {
		logger.debug("step[vbsPdf] sendBorrowerEmailMessage begin .............");
		AccountMessageTemplateData data = new AccountMessageTemplateData();
		data.setP1(borrowerUserName); //[借款人用户名]
		data.setP2(total);//[总金额]
		data.setP3(period);//[期限]
		data.setP4(payDay+"日");//[还款日]
		data.setAttachPath(filePath); //协议附件

		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI_JKR, data, AccountMessageChannelEnum.EMAIL, email);
		logger.debug("step[vbsPdf] sendBorrowerEmailMessage end .............");
		//		String toUser = "dongkun01@vcredit.com";// 
		//		AccountMessageTemplateData data = new AccountMessageTemplateData();
		//
		//		data.setP1("EDEN"); //[借款人用户名]
		//		data.setP2("10000");//[总金额]
		//		data.setP3("36");//[期限]
		//		data.setP4("15");//[还款日]
		//
		//		data.setAttachPath("d:\\user.json"); //协议附件
//
		//		accountMessageManager.sendAccMessage(AccountMessageTemplateEnum.FANG_KUAN_TONG_ZHI_JKR, data, AccountMessageChannelEnum.EMAIL, toUser);

	}

}
