package com.striveen.express.net;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
/**
 * 获取http连接
 * @author Fei
 *
 */
public class HttpConnection {

	private HttpConnection() {
	}

	private static HttpClient httpClient = null;
	/**
	 * 获取http连接
	 * 
	 * @return
	 */
	public static HttpClient getHttpClient() {
//		if (httpClient == null) {
//			httpClient = newHttpClient();
//		}
//		return httpClient;
		return newHttpClient();
	}
	/**
	 * 释放http
	 */
	public static void release() {
		httpClient = null;
	}
	
	/**
	 * 获取http连接
	 * 
	 * @return
	 */
	public static HttpClient newHttpClient() {
		// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
		HttpParams httpParams = new BasicHttpParams();
		// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
		HttpConnectionParams.setConnectionTimeout(httpParams, 15 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 15 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		ConnManagerParams.setTimeout(httpParams, 15 * 1000);
		// 设置重定向，缺省为 true
		HttpClientParams.setRedirecting(httpParams, true);
		// 设置 user agent
		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);
		// 创建一个 HttpClient 实例
		// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
		// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient
		return new DefaultHttpClient(httpParams);
	}

}
