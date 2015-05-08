package com.vcredit.jdev.p2p.pdf.factory;

import java.io.File;
import java.io.IOException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

/**
 * 
 * @author zhaopeijun
 *
 */
@Component
public class ITextRendererObjectFactory extends
		BasePoolableObjectFactory {
	private static GenericObjectPool itextRendererObjectPool = null;
	
	private static Logger logger = LoggerFactory.getLogger(ITextRendererObjectFactory.class);
	
	public static String fontInPath;
	
	@Override
	public Object makeObject() throws Exception {
//		String path = this.getClass().getResource("/").getPath();
//		String path = ctx.getResource("classpath:pdf").getFile().getPath()+File.separator;
		String fontPath = fontInPath;
		ITextRenderer renderer = createTextRenderer(fontPath);
		return renderer;
	}
	
	public static GenericObjectPool getObjectPool(String fontPath){
		synchronized (ITextRendererObjectFactory.class) {
			if(itextRendererObjectPool==null){
				itextRendererObjectPool = new GenericObjectPool(
						new ITextRendererObjectFactory());
				GenericObjectPool.Config config = new GenericObjectPool.Config();
				config.lifo = false;
				config.maxActive = 15;
				config.maxIdle = 5;
				config.minIdle = 1;
				config.maxWait = 5 * 1000;
				itextRendererObjectPool.setConfig(config);
			}
			fontInPath = fontPath;
		}
		
		return itextRendererObjectPool;
	}

	public static synchronized ITextRenderer createTextRenderer(String fontPath)
			throws DocumentException, IOException {
		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();
		addFonts(fontResolver,fontPath);
		return renderer;
	}

	public static ITextFontResolver addFonts(ITextFontResolver fontResolver,String fontPath)
			throws DocumentException, IOException {
		// Font fontChinese = null;
		// BaseFont bfChinese = BaseFont.createFont("STSong-Light",
		// "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		// fontChinese = new Font(bfChinese, 12, Font.NORMAL);
//		ResourceLoaders.getPath("");
//		String path = ResourceLoaders.getPath("pdf/fonts");
		
//		String path = ResourceLoaders.getPath(PdfUtil.PDF_FONTS_PATH);
//		logger.debug("step 生成pdf addFonts方法：获取字体path "+path);
		String path = fontPath;
		logger.debug("step 生成pdf addFonts方法：获取字体path "+path);
		File fontsDir = new File(path);
		if (fontsDir != null && fontsDir.isDirectory()) {
			File[] files = fontsDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (f == null || f.isDirectory()) {
					break;
				}
				fontResolver.addFont(f.getAbsolutePath(), BaseFont.IDENTITY_H,
						BaseFont.NOT_EMBEDDED);
			}
		}
		return fontResolver;
	}
}
