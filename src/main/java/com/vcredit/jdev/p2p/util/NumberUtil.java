package com.vcredit.jdev.p2p.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.hsqldb.lib.StringUtil;

/**
 * 数据工具类.<br>
 * 数值共通处理。
 */
public class NumberUtil {

	static final String DELIMITER_HALF_COMMA = ",";

	private NumberUtil() {
	}

	/**
	 * 数值加3位逗号。<BR>
	 * 
	 * @param targetNumber
	 *            编辑对象字符串
	 * @return 逗号编辑后字符串
	 */
	public static String toCommaFormat(BigDecimal targetNumber) {

		if (targetNumber == null) {
			return "";
		}

		DecimalFormat decimalformat = new DecimalFormat("###,##0.#####################");
		return decimalformat.format(targetNumber);
	}

	public static BigDecimal toNumberType(String targetNumber) {
		return new BigDecimal(targetNumber);
	}

	public static BigDecimal toNumberTypeNull(String targetNumber) {
		if (StringUtil.isEmpty(targetNumber)) {
			return null;
		}

		return toNumberType(targetNumber);
	}

	public static BigDecimal toDecimalTypeNull(String targetNumber) {
		return toNumberTypeNull(targetNumber);
	}

	public static BigDecimal toNumberTypeZero(String targetNumber) {
		if (StringUtil.isEmpty(targetNumber)) {
			return new BigDecimal("0");
		}

		return toNumberType(targetNumber);
	}

	public static BigDecimal toDecimalTypeZero(String targetNumber) {
		return toNumberTypeZero(targetNumber);
	}

	/**
	 * 去除逗号。<BR>
	 * 把字符串的数据的逗号去掉。
	 * 
	 * @param targetNumber
	 *            字符串
	 * @return 转换后的字符串
	 */
	public static String removeComma(String targetNumber) {
		if (targetNumber == null) {
			return targetNumber;
		}
		return targetNumber.replaceAll(DELIMITER_HALF_COMMA, "");
	}

	/**
	 * 判断是否为整数
	 * 
	 * @param s
	 * @return
	 */
	public boolean isInteger(Object o) {
		if ((o != null) && (o != ""))
			return o.toString().matches("^[0-9]*$");
		else
			return false;
	}

	/**
	 * 判断字符串是否是浮点数
	 * 
	 * @param value
	 * @return
	 */
	public boolean isDouble(Object value) {
		try {
			Double.parseDouble(String.valueOf(value));
			if (String.valueOf(value).contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 判断是否为数字
	 * @param value
	 * @return
	 */
	public boolean isNumber(Object value) {    
        return isInteger(value) || isDouble(value);    
    } 

}
