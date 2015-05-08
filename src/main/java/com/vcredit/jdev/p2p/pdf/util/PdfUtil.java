package com.vcredit.jdev.p2p.pdf.util;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zhaopeijun
 *
 */
@Component
public class PdfUtil {
	
	private static Logger logger = Logger.getLogger(PdfUtil.class);
	
	@Autowired
	ApplicationContext ctx;

	/** html模版名称 **/
	@Value("${pdf.templateName}")
	String htmlName;
	
	/** html后缀 */
	public static final String HTML_SUFFIX = ".html";
	
	/** pdf后缀 */
	public static final String PDF_SUFFIX = ".pdf";
	
	public static final String PDF_FONTS_PATH = "pdf/fonts";
	
	/**
	 * 取htmltemp所在路径
	 * **/
	public String getPdfPath(){
		String path = null;
		try {
			path = ctx.getResource("classpath:pdf").getFile().getPath()+File.separator;
		} catch (IOException e) {
			logger.info("取pdf模板所在路径报错:=====get pdfTemplate path error....");
			e.printStackTrace();
		}
		logger.info("pdf path:"+path+"####################");
		return path;
	}
	
	/**
	 * 返回html模版文件名
	 * @return
	 */
	public String getHtmlTemplateNa(){
		return htmlName+HTML_SUFFIX;
	}
	
	/**
	 * 返回html模版文件名（路径+后缀）
	 * @return
	 */
	public String getHtmlTemplatePath(){
		return getPdfPath()+htmlName+HTML_SUFFIX;
	}
	
	/**
	 * 返回pdf临时文件路径
	 * @return
	 */
	public String getTempPdfPath(){
		return getPdfPath()+"tmp"+File.separator;
	}
	
//	public static String getFontsPath(String path){
//		
//	}
	
    /**
     * 获取马赛克数据-身份证
     * @param data 原文
     * @param start *字符开始位，正数
     * @param end *字符结束位，倒数
     * @return 马赛克数据
     */
    public static String getMaskData(String data, int start, int end) {
        
        int len = data.length();
        String beforeStr = data.substring(0, start);
        String midleStr = data.substring(start, data.length()-end);
        
        for(int i=0; i<midleStr.length(); i++){
        	beforeStr += "*";
        }
        String afterStr = data.substring(data.length()-end,data.length());
        beforeStr += afterStr;
        return beforeStr;
        
    }
	
}
