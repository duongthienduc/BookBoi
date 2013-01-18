package com.csun.bookboi.service;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class SingletonHttpClient {
	private static HttpClient httpClient;
	private static final int ONE_SECOND = 1000;
	private static final int ONE_MINUTE = ONE_SECOND * 60;
	private static final int TWO_MINUTE = ONE_SECOND * 120;
	
	/**
	 * Prevent user from constructing another instance
	 */
	private SingletonHttpClient() {
		// empty body
	}

	/**
	 * Instance return a singleton object to work 
	 * across entire application. 
	 * @return
	 * 		A custom HttpClient
	 */
	public static synchronized HttpClient instance() {
		if (httpClient == null) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			ConnManagerParams.setTimeout(params, TWO_MINUTE);
			HttpConnectionParams.setConnectionTimeout(params, TWO_MINUTE);
			HttpConnectionParams.setSoTimeout(params, TWO_MINUTE);
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
			httpClient = new DefaultHttpClient(conMgr, params);
		}
		return httpClient;
	}
	
	public static HttpClient newInstance() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		ConnManagerParams.setTimeout(params, TWO_MINUTE);
		HttpConnectionParams.setConnectionTimeout(params, TWO_MINUTE);
		HttpConnectionParams.setSoTimeout(params, TWO_MINUTE);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		return new DefaultHttpClient(conMgr, params);
	}
}