package com.vcredit.jdev.p2p.chinapnr.util;

import java.io.UnsupportedEncodingException;

import chinapnr.Base64;

public class BaseCodeUtil {
	
    public static String getBase64Encode(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        try {
            byte[] bt = str.getBytes("UTF-8");
            str = String.valueOf(Base64.encode(bt));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getBase64Decode(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        char[] ch = str.toCharArray();
        byte[] bt = Base64.decode(ch);
        try {
            str = new String(bt,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

}
