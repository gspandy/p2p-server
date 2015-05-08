package com.vcredit.jdev.p2p.pdf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.base.util.DateFormatUtil;

/**
 * FTP 上传工具
 */
@Component
public class FtpUtils {
	private static final Logger logger = LoggerFactory.getLogger(FtpUtils.class);
	private static final FTPClient client = new FTPClient();
	/**
	 * 服务器IP
	 */
	public static final String FTP_SERVER = "10.1.8.76";

	/*
	 * @Value("${sudoor.ftp.host}") String FTP_SERVER;
	 */

	public static final Integer FTP_PORT = 21;
	/**
	 * 服务器账号&密码
	 */
	public static final String USERNAME = "go";
	public static final String PASSWORD = "p2p_deployer";

	/**
	 * 存放pdf目录
	 */
	//	public static final String IDENTITY_PATH = "/ftp_dir/resources/pdf";
	public static final String IDENTITY_PATH = "/resources/pdf";
	
	@Autowired
	private PdfUtil pdfUtil;

	/**
	 * 连接服务器
	 * 
	 * @throws Exception
	 */
	private static void connect() throws Exception {
		try {
			logger.info("step[vbsPdf] start to connect to ftp server");
			client.connect(FTP_SERVER, FTP_PORT);
		} catch (Exception e) {
			logger.info("step[vbsPdf] start to connect to ftp server fail "+e.getLocalizedMessage());
			throw new Exception("step connect to ftp server[" + FTP_SERVER + "] fail", e);
		}
		try {
			logger.info("step[vbsPdf] start to login ftp server");
			client.login(USERNAME, PASSWORD);

			if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
				client.disconnect();
				logger.error("step 登录FTP服务失败！");
				throw new Exception("fail login ftp server");
			}
			logger.info("step[vbsPdf] login ftp server success");

			client.changeWorkingDirectory("/");
			logger.info("step[vbsPdf] change ftp server working directory");

			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.setControlEncoding("UTF-8");
		} catch (IOException e) {
			logger.debug("step[vbsPdf] login to ftp server[" + FTP_SERVER + "] fail" + e.getLocalizedMessage());
			throw new Exception("step[vbsPdf] login to ftp server[" + FTP_SERVER + "] fail", e);
		}
	}

	/**
	 * 通用上传文件
	 * 
	 * @param directory
	 * @param fileName
	 * @param localInstream
	 * @throws Exception
	 */
	public static void upload(String directory, String fileName, InputStream localInstream) throws Exception {
		try {
			connect();
			logger.info("step[vbsPdf] ftp server prepare success");
			//			client.mkd(directory);
			
			client.changeWorkingDirectory(IDENTITY_PATH);
			//返回true证明创建成功，即在执行创建命令前ftp上不存在此目录;返回false证明创建失败，即ftp上已存在此目录
			client.makeDirectory(directory);
			client.changeWorkingDirectory(directory);
			
			logger.info("step[vbsPdf] start to upload the file to server,begin time:{}",DateFormatUtil.getCurrentTime(DateFormatUtil.STAND_DATE_FROMAT));
			client.storeFile(fileName, localInstream);
			logger.info("step[vbsPdf] finish to upload the file to server,end time:{}",DateFormatUtil.getCurrentTime(DateFormatUtil.STAND_DATE_FROMAT));
		} catch (Exception e) {
			throw new Exception("step[vbsPdf] upload fail", e);
		} finally {
			try {
				logger.info("step[vbsPdf] close the ftp server connect");
				client.disconnect();
			} catch (IOException e) {
				throw new Exception("step[vbsPdf] close connect fail", e);
			}
		}
	}

	/**
	 * 字节数组形式上传文件
	 * 
	 * @param directory
	 * @param fileName
	 * @param content
	 * @throws Exception
	 */
	public static void upload(String directory, String fileName, byte[] content) throws Exception {
		upload(directory, fileName, new ByteArrayInputStream(content));
	}

	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 下载文件
	 * @param remoteFileName 待下载文件名称
	 * @param localDires 下载到当地那个路径下
	 * @param remoteDownLoadPath remoteFileName所在的路径
	 * @return
	 */
	/*public boolean downloadFile(String remoteFileName, String localDires, String remoteDownLoadPath) {
		try {
			connect();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		localDires = pdfUtil.getTempPdfPath();
		localDires = "D://";
		remoteFileName = "Vht12345.pdf";
		String strFilePath = localDires + remoteFileName;
		BufferedOutputStream outStream = null;
		boolean success = false;
		try {
			
			client.changeWorkingDirectory(IDENTITY_PATH);
			outStream = new BufferedOutputStream(new FileOutputStream(strFilePath));
			logger.info(remoteFileName + "开始下载....");
			success = client.retrieveFile(remoteFileName, outStream);
			if (success == true) {
				logger.info(remoteFileName + "成功下载到" + strFilePath);
				return success;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(remoteFileName + "下载失败");
		} finally {
			if (null != outStream) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (success == false) {
			logger.error(remoteFileName + "下载失败!!!");
		}
		return success;
	}*/
}
