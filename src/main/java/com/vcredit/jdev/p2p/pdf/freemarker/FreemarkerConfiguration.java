package com.vcredit.jdev.p2p.pdf.freemarker;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.pdf.util.PdfUtil;

import freemarker.template.Configuration;

/**
 * 
 * @author zhaopeijun
 *
 */
@Component
public class FreemarkerConfiguration {

	private static Configuration config = null;
	
	@Autowired
	private PdfUtil pdfUtil;

	public static Configuration getConfiguation(String path) {
		if (config == null) {
			setConfiguation(path);
		}
		return config;
	}
	
	private static void setConfiguation(String path) {
		config = new Configuration();
		try {
			config.setDirectoryForTemplateLoading(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}