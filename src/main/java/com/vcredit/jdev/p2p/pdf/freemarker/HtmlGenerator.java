package com.vcredit.jdev.p2p.pdf.freemarker;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.pdf.util.PdfUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class HtmlGenerator { 
	
	@Autowired
	private PdfUtil pdfUtil;
	
	private static Logger logger = LoggerFactory.getLogger(HtmlGenerator.class);
         
    /**   
     * Generate html string.   
     *    
     * @param template   the name of freemarker teamlate.   
     * @param variables  the data of teamlate.   
     * @return htmlStr   
     * @throws IOException 
     * @throws TemplateException 
     * @throws Exception   
     */    
    public String generateHtmlContent(String htmlTemplateNa, Map<String,Object> variables) throws IOException, TemplateException{     
        BufferedWriter writer = null;   
        String htmlContent = "";
        try{
        	Configuration config = FreemarkerConfiguration.getConfiguation(pdfUtil.getPdfPath());
        	Template tp = config.getTemplate(htmlTemplateNa);     
        	StringWriter stringWriter = new StringWriter();       
        	writer = new BufferedWriter(stringWriter);  
        	
        	tp.setEncoding("UTF-8");       
        	tp.process(variables, writer);       
        	htmlContent = stringWriter.toString();     
        	writer.flush();       
        	logger.debug("step 模板："+htmlTemplateNa+" 生成html string 成功");
        }finally{
        	if(writer!=null)
        		writer.close();     
        }
        return htmlContent;     
    }     
    
} 