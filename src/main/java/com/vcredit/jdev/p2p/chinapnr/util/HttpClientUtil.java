package com.vcredit.jdev.p2p.chinapnr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chinapnr.Base64;

/**
 * 
 * <p class="detail">
 * 功能：Http Client 工具类
 * </p>
 * 
 * @ClassName: HttpClientUtil
 * @version V1.0
 */
public class HttpClientUtil {
	
   
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    
	private static int connectionTimeOut = 30000;

	private static int socketTimeOut = 60000;

	private static String DEFAULT_CHARSET = "utf-8";

	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut)
			.setConnectTimeout(connectionTimeOut).build();

	// 自定义响应处理
	private static ResponseHandler<String> responseStringHandler = new ResponseHandler<String>() {
		public synchronized String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			String responseBody = "";
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode >= 300) {
				throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
			}
			if (HttpStatus.SC_OK == statusCode) {
				HttpEntity entity = response.getEntity();
				if (null == entity) {
					throw new ClientProtocolException("Response contains no content");
				} else {
					responseBody = getResponseBody(entity);
				}
			}
			return responseBody;
		}
	};

	// 自定义响应处理
	private static ResponseHandler<HttpResponse> responseHandler = new ResponseHandler<HttpResponse>() {
		public synchronized HttpResponse handleResponse(HttpResponse response) throws ClientProtocolException,
				IOException {
			HttpEntity entity = response.getEntity();
			if (null == entity) {
				throw new ClientProtocolException("Response contains no content");
			} else {
				StringEntity stringEntity = new StringEntity(getResponseBody(entity), DEFAULT_CHARSET);
				response.setEntity(stringEntity);
			}
			return response;
		}
	};

	/**
	 * 
	 * <p class="detail">
	 * 功能：新建HttpClient
	 * </p>
	 * 
	 */
	public static HttpClient getNewHttpClient() {
		try {
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(100);
			HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
				public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
					if (executionCount >= 3) {
						// 如果超过最大重试次数，那么就不要继续了
						return false;
					}
					if (exception instanceof InterruptedIOException) {
						// Timeout
						return false;
					}
					if (exception instanceof ConnectTimeoutException) {
						// Connection refused
						return false;
					}
					if (exception instanceof NoHttpResponseException) {
						// 如果服务器丢掉了连接，那么就重试
						return true;
					}
					if (exception instanceof SSLHandshakeException) {
						// 不要重试SSL握手异常
						return false;
					}
					HttpClientContext clientContext = HttpClientContext.adapt(context);
					HttpRequest request = clientContext.getRequest();
					boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
					if (idempotent) {
						return true;
					}
					return false;
				}
			};
			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
					.setRetryHandler(myRetryHandler).build();
			return httpClient;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：Http GET 方法 返回类型为String
	 * </p>
	 * 
	 * @param url
	 *            请求URL
	 * @return String
	 * @throws HttpException
	 * @throws IOException
	 */
	public static String get(String url) throws HttpException, IOException {
		HttpClient httpClient = getNewHttpClient();
		HttpGet getMethod = getMethod(url);
		String responseBody = "";
		try {
			responseBody = httpClient.execute(getMethod, responseStringHandler);
		} finally {
			getMethod.releaseConnection();
		}
		return responseBody;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：Http GET 方法 返回类型为HttpResponse
	 * </p>
	 * 
	 * @param url
	 *            请求URL
	 * @return HttpResponse
	 * @throws HttpException
	 * @throws IOException
	 */
	public static HttpResponse sendGetRequest(String url) throws HttpException, IOException {
		HttpClient httpClient = getNewHttpClient();
		HttpGet getMethod = getMethod(url);
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(getMethod, responseHandler);
		} finally {
			getMethod.releaseConnection();
		}
		return httpResponse;
	}

	/**
	 * <p class="detail">
	 * 功能：POST请求 返回响应体 String 内容
	 * </p>
	 * 
	 * @param url
	 *            请求地址
	 * @param postData
	 *            请求参数
	 * @return String
	 * @throws HttpException
	 */
	public static String post(String url, Map<String, String> postData) throws Exception {
		if (null == postData || postData.isEmpty()) {
			return get(url);
		}
		HttpPost postMethod = null;
		String responseBody = "";
		try {
			HttpClient httpClient = getNewHttpClient();
			postMethod = postMethod(url);
			Set<Entry<String, String>> entrySet = postData.entrySet();
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator<Entry<String, String>> it = entrySet.iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, DEFAULT_CHARSET);
			postMethod.setEntity(entity);
			responseBody = httpClient.execute(postMethod, responseStringHandler);
		} catch (Exception e) {
			throw e;
		} finally {
			postMethod.releaseConnection();
		}
		return responseBody;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：POST请求 返回响应信息
	 * </p>
	 * 
	 * @param url
	 * @param postData
	 *            请求地址
	 * @return 请求参数
	 * @throws Exception
	 */
	public static HttpResponse sendPostRequest(String url, Map<String, String> postData) throws Exception {
		if (null == postData || postData.isEmpty()) {
			return sendGetRequest(url);
		}
		HttpPost postMethod = null;
		HttpResponse httpResponse = null;
		try {
			HttpClient httpClient = getNewHttpClient();
			postMethod = postMethod(url);
			Set<Entry<String, String>> entrySet = postData.entrySet();
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			Iterator<Entry<String, String>> it = entrySet.iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			if (null != formparams && formparams.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, DEFAULT_CHARSET);
				postMethod.setEntity(entity);
			}
			httpResponse = httpClient.execute(postMethod, responseHandler);
		} catch (Exception e) {
			logger.error("sendPostRequest--> error:"+e.getMessage());
			logger.error("sendPostRequest--> error:",e);
			throw e;
		} finally {
			postMethod.releaseConnection();
		}
		return httpResponse;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：得到GET METHOD
	 * </p>
	 * @param url
	 *            请求URL
	 * @return
	 */
	public static HttpGet getMethod(String url) {
		HttpGet getMethod = new HttpGet(url);
		getMethod.setConfig(requestConfig);
		getMethod.setHeaders(getHeaders());
		return getMethod;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：得到POST METHOD
	 * </p>
	 * 
	 * @param url
	 *            请求URL
	 * @return
	 */
	public static HttpPost postMethod(String url) {
		HttpPost postMethod = new HttpPost(url);
		postMethod.setConfig(requestConfig);
		postMethod.setHeaders(getHeaders());
		return postMethod;
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：请求头信息
	 * </p>
	 * 
	 */
	private static Header[] getHeaders() {
		ArrayList<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Accept", "text/html, text/json, text/xml, html/text, */*"));
		headers.add(new BasicHeader("Accept-Language", "zh-cn,en-us,zh-tw,en-gb,en;"));
		headers.add(new BasicHeader("Accept-Charset", "gbk,gb2312,utf-8,BIG5,ISO-8859-1;"));
		headers.add(new BasicHeader("Connection", "keep-alive"));
		headers.add(new BasicHeader("Cache-Control", "no-cache"));
		headers.add(new BasicHeader("Accept-Encoding", "gzip"));
		headers.add(new BasicHeader("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA)"));
		return headers.toArray(new Header[0]);
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：从响应体内容
	 * </p>
	 * 
	 * @param httpResponse
	 *            httpClient 响应
	 * @return 响应体内容
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static String getResponseBody(HttpEntity entity) throws IllegalStateException, IOException {
		StringBuffer respnseBody = new StringBuffer();
		BufferedReader in = null;
		try {
			InputStream is = entity.getContent();
			in = new BufferedReader(new InputStreamReader(is, DEFAULT_CHARSET));
			String line = null;
			while (null != (line = in.readLine())) {
				respnseBody.append(line);
			}
		} catch (IllegalStateException e1) {
			logger.error("getResponseBody-->IllegalStateException:"+e1.getMessage());
			throw e1;
		} catch (IOException e2) {
			logger.error("getResponseBody-->IOException:"+e2.getMessage());
			throw e2;
		} finally {
			in.close();
		}
		return respnseBody.toString();
	}

	/**
	 * 
	 * <p class="detail">
	 * 功能：从响应中得到响应体内容
	 * </p>
	 * 
	 * @param httpResponse
	 *            httpClient 响应
	 * @return 响应体内容
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static String getResponseBody(HttpResponse httpResponse) throws IllegalStateException, IOException {
		String respnseBody = "";
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = httpResponse.getEntity();
			if (null != entity) {
				respnseBody = getResponseBody(entity);
			}
		}
		return respnseBody;
	}
	
    /**
     * MAP类型数组转换成NameValuePair类型
     * 
     */
    public static List<NameValuePair> buildNameValuePair(Map<String, String> params) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        return nvps;
    }
    
    public static String getResult(HttpResponse response){
    	logger.debug("getResult-->HttpEntity to result String begin ...............");
    	String result = "";
    	try {
    		HttpEntity entity = response.getEntity();
    		if (response.getStatusLine().getReasonPhrase().equals("OK")
    				&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
    			result = EntityUtils.toString(entity, "UTF-8");
    		}
    		EntityUtils.consume(entity);
		} catch (Exception e) {
			logger.error("getResult-->HttpEntity to String error:error msg:"+e.getMessage());
			logger.error("getResult-->HttpEntity to String error:error msg:",e);
		}
    	logger.debug("getResult-->HttpEntity to result String:"+result);
    	logger.debug("getResult-->HttpEntity to result String end ...............");
        return result;
    }
    

}
