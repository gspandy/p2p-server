package com.vcredit.jdev.p2p.pdf.model;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.log.SysoCounter;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.pdf.exception.CommonDefinedException;
import com.vcredit.jdev.p2p.pdf.exception.ErrorCodeException.CommonException;
import com.vcredit.jdev.p2p.pdf.util.FtpUtils;
import com.vcredit.jdev.p2p.pdf.util.PdfUtil;

/**
 * 
 * @author zhaopeijun
 *
 */
@Component
public class FtpDataManager {
	private static Logger logger = LoggerFactory.getLogger(FtpDataManager.class);

	@Autowired
	private FtpUtils ftpUtils;

	@Autowired
	private PdfUtil pdfUtil;

	/**
	 * 上传pdf到ftp
	 * @param pdfName
	 * @return pdf 在ftp上path
	 * @throws CommonException 
	 */
	public String uploadPdf(String pdfName) throws CommonException {
		String directory = "";
		try {
			logger.info("step[vbsPdf] 上传pdf = {} in,开始时间={}:",pdfName,DateFormatUtil.getCurrentTime(DateFormatUtil.STAND_DATE_FROMAT));
			//服务端设置 文件夹层次（ymd划分）
			String ymdStr = DateFormatUtil.dateToString(new Date(), DateFormatUtil.YYYYMMDD);
			directory = ftpUtils.IDENTITY_PATH+"/"+ymdStr; 
			ftpUtils.upload(directory, pdfName, ftpUtils.getBytes(pdfUtil.getTempPdfPath() + pdfName));
			logger.info("step[vbsPdf] 上传pdf = {} end,结束时间 = {}:",pdfName,DateFormatUtil.getCurrentTime(DateFormatUtil.STAND_DATE_FROMAT));
		} catch (Exception e) {
			CommonException ce = new CommonDefinedException().PDF_UPLOAD_FAIL;
			ce.setDetail(e.getLocalizedMessage());
			logger.error("step[vbsPdf] VBS版本pdf上传FTP fail"+ " 合同名： " + pdfName + ",错误信息：" +e.getLocalizedMessage()+",发生时间："+DateFormatUtil.getCurrentTime(DateFormatUtil.STAND_DATE_FROMAT));
			throw ce;
		}
		return directory + "/" + pdfName;
	}
	
	/*public void downloadPdf(String remoteFileName, String localDires, String remoteDownLoadPath){
		 ftpUtils.downloadFile(remoteFileName,localDires,remoteDownLoadPath);
	}*/

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteTempFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

}
