package com.vcredit.jdev.p2p.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
/**
 * 字符串工具类
 * @author 周佩
 *
 */
public class StringUtil {
	
	public static String decode(String str,String charset) throws UnsupportedEncodingException{
		String localset="UTF-8";
		if(str==null){
			return "";
		}
		if(charset!=null){
			localset=charset;
		}
		return URLDecoder.decode(str, localset);
	}
}
