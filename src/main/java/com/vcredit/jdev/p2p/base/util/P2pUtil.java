package com.vcredit.jdev.p2p.base.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcredit.jdev.p2p.dto.IpAdressDto;
import com.vcredit.jdev.p2p.enums.OrderIDRuleEnum;

/**
 * @ClassName: P2pUtils
 * @Description: P2P项目系统公用类
 * @author dk
 * @date 2014年12月5日 下午2:25:59
 * 
 */
public class P2pUtil {
	private static final Logger logger = LoggerFactory.getLogger(P2pUtil.class);

	/**
	 * @Title: getAddressByIP
	 * @Description: 通过IP获取地址
	 * @param strIP
	 *            ip
	 * @param @return 设定文件
	 * @return IpAdressDto ip所在地bean
	 * @throws
	 */
	public static IpAdressDto getAddressByIP(String strIP) {
		try {
			//String strIP = "0.0.0.0";
			//调用baiduAPI通过IP获取IP所在地址
			URL url = new URL("http://apistore.baidu.com/microservice/iplookup?ip=" + strIP);
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = null;
			StringBuffer result = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			reader.close();
			//从百度中得到的地址对应的中文是unicode码，所以需要转化下
			//String s = "\\u4e2d\\u56fd";
			String sb = unicodeToChinese(result);
			IpAdressDto ipAdressDto = getBeanFromJson(getJosnByKey(sb, "retData"), IpAdressDto.class);
			return ipAdressDto;
		} catch (IOException e) {
			logger.error("从IP中读取地址失败：" + e.getMessage());
			return null;
		}
	}

	/**
	 * @Title: unicodeToChinese
	 * @Description: Unicode编码转化为汉字
	 * @param result
	 * @param @return 设定文件
	 * @return StringBuffer 返回类型
	 * @throws
	 */
	public static String unicodeToChinese(StringBuffer result) {
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("(?i)\\\\u([\\da-f]{4})");
		Matcher m = p.matcher(result);
		while (m.find()) {
			m.appendReplacement(sb, Character.toString((char) Integer.parseInt(m.group(1), 16)));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static <T> List<T> getBeansFromJson(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
		List<T> lst = (List<T>) mapper.readValue(json, javaType);
		return lst;
	}

	public static <T> T getBeanFromJson(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		T t = mapper.readValue(json, clazz);
		return t;
	}

	public static String getJosnByKey(String json, String key) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(json);
		JsonNode fields = root.get(key);
		if (null != fields) {
			return fields.toString();
		} else {
			return "";
		}
	}

	public static String getStringByKey(String json, String key) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(json);
		JsonNode fields = root.get(key);
		if (null != fields) {
			return fields.asText();
		} else {
			return "";
		}
	}

	public static String getBeanToJson(Object obj) throws JsonProcessingException {
		if (obj == null)
			return null;
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}

	public static String generateRandom(int length) {
		Random random = new Random();
		char[] digits = new char[length];
		digits[0] = (char) (random.nextInt(9) + '1');
		for (int i = 1; i < length; i++) {
			digits[i] = (char) (random.nextInt(10) + '0');
		}
		return new String(digits);
	}

	public static String generate20Random() {
		return generateRandom(20);
	}

	/**
	 * @Title: generate20Random
	 * @Description: 通过订单种类规则生成20订单
	 * @param orderRule
	 * @return
	 * @return String 返回类型
	 * @throws
	 */
	public static String generate20Random(OrderIDRuleEnum orderRule) {
		return orderRule.getCode() + generateRandom(19);
	}
	
	public static String getIpAddress() {
        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                System.out.println("DisplayName:" + ni.getDisplayName());
                System.out.println("Name:" + ni.getName());
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ip = ips.nextElement();
                    if (!ip.isLoopbackAddress()) {
                    	System.out.println("ip-------------------->" + ip.getHostAddress());
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
        	logger.error("getIpAddress error:"+e.getMessage());
        }
        return "";
    }
}
