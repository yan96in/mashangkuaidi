package com.striveen.express.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.striveen.express.R;
import com.striveen.express.util.JsonMap;
import com.striveen.express.util.JsonMapOrListJsonMap2JsonUtil;
import com.striveen.express.util.StringUtil;
import com.striveen.express.util.SubstituteEncrypt;

public class GetData{
	/**
	 * 提示toast的展示对象 可直接调用期show方法展示文字
	 */
	private static ResponseCallBack callBack;
	private final String TAG = "GetData";

	private String messageContent = "";
	private String Passport = "Usage=%1$s&Function=%2$s";
	private Context context;

	public GetData(ResponseCallBack callBack1) {
		callBack = callBack1;
	}

	public GetData(Context context) {
		this.context = context;
	}

	/**
	 * get请求FS
	 * 
	 * @param callBack1
	 * @param url
	 * @param mark
	 */
	public void doGet(ResponseCallBack callBack1, String url, int mark) {
		// http地址
		callBack = callBack1;
		String strResult;
		HttpGet httpRequest = null;

		try {
			// HttpGet连接对象
			httpRequest = new HttpGet(url);
			// 请求HttpClient，取得HttpResponse
			HttpResponse httpResponse = HttpConnection.getHttpClient().execute(
					httpRequest);

			Log.d("GetData", "getURI:" + httpRequest.getURI());
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 取得返回的字符串
				strResult = EntityUtils.toString(httpResponse.getEntity());
				// strResult = strResult.substring(strResult.indexOf("{"),
				// strResult.lastIndexOf("}") + 1);
				Message msg = new Message();
				msg.obj = strResult;
				// handler1.sendMessage(handler.obtainMessage(
				// R.id.msgResultSuccess, mark, 0, msg));
				Log.d("GetData", "strResult:" + strResult);
				handler.sendMessage(handler.obtainMessage(
						R.id.msgResultSuccess, mark, 0, msg));
			} else {
				handler.sendEmptyMessage(R.id.msgResultFailure);
				Log.d("GetData", "msgResultFailure:请求失败"
						+ httpResponse.getStatusLine().getStatusCode());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (ConnectException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (SocketException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgServerError);
			HttpConnection.release();
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgServerError);
			HttpConnection.release();
		} finally {

			httpRequest.abort();
		}

	}

	/**
	 * post JSONObject请求
	 * 
	 * @param pathUrl
	 * @param params
	 */
	public void doPost(ResponseCallBack callBack1, String pathUrl,
			JSONObject params, int mark) {
		callBack = callBack1;
		try {
			// 建立连接
			URL url = new URL(pathUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();

			// //设置连接属性
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出
			httpConn.setDoInput(true);// 使用 URL 连接进行输入
			httpConn.setUseCaches(false);// 忽略缓存
			httpConn.setRequestMethod("POST");// 设置URL请求方法

			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致ENCODING_UTF_8
			byte[] requestStringBytes = params.toString().getBytes(HTTP.UTF_8);
			httpConn.setRequestProperty("Content-length", ""
					+ requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			//
			String name = URLEncoder.encode("黄武艺", "utf-8");
			httpConn.setRequestProperty("NAME", name);

			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			// 获得响应状态
			int responseCode = httpConn.getResponseCode();
			Log.d(TAG, "getURL: " + httpConn.getURL());
			Log.d(TAG, "responseCode=" + responseCode);
			if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功

				// 当正确响应时处理数据
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				// 处理响应流，必须与服务器响应流输出的编码一致
				responseReader = new BufferedReader(new InputStreamReader(
						httpConn.getInputStream(), HTTP.UTF_8));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				Message msg = new Message();
				msg.obj = sb.toString().trim();
				Log.d("GetData", "strResult:" + sb.toString());
				handler.sendMessage(handler.obtainMessage(
						R.id.msgResultSuccess, mark, 0, msg));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (ConnectException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (SocketException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgServerError);
			HttpConnection.release();
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgServerError);
			HttpConnection.release();
		}

	}

	/**
	 * post 请求 传参格式 Token={TOKEN}&Content={CONTENT}
	 * 
	 * @param pathUrl
	 * @param requestString
	 * @param params
	 * @param encoding
	 */
	public void doPost(ResponseCallBack callBack1, String pathUrl,
			String requestString, int mark) {
		callBack = callBack1;
		try {
			// 建立连接
			URL url = new URL(pathUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			Log.d(TAG, "requestString: " + requestString);
			// //设置连接属性
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出
			httpConn.setDoInput(true);// 使用 URL 连接进行输入
			httpConn.setUseCaches(false);// 忽略缓存
			httpConn.setRequestMethod("POST");// 设置URL请求方法

			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致ENCODING_UTF_8
			byte[] requestStringBytes = requestString.getBytes(HTTP.UTF_8);
			httpConn.setRequestProperty("Content-length", ""
					+ requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			//
			String name = URLEncoder.encode("黄武艺", "utf-8");
			httpConn.setRequestProperty("NAME", name);

			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			// 获得响应状态
			int responseCode = httpConn.getResponseCode();
			Log.d(TAG, "getURL: " + httpConn.getURL());
			Log.d(TAG, "responseCode=" + responseCode);
			if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功

				// 当正确响应时处理数据
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				// 处理响应流，必须与服务器响应流输出的编码一致
				responseReader = new BufferedReader(new InputStreamReader(
						httpConn.getInputStream(), HTTP.UTF_8));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();
				Message msg = new Message();
				msg.obj = sb.toString().trim();
				Log.d("GetData", "strResult:" + sb.toString());
				handler.sendMessage(handler.obtainMessage(
						R.id.msgResultSuccess, mark, 0, msg));
			} else {
				handler.sendEmptyMessage(R.id.msgResultFailure);
				Log.d("GetData", "msgResultFailure:请求失败");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.d("GetData", "e:" + e);
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			Log.d("GetData", "e:" + e);
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (ConnectException e) {
			e.printStackTrace();
			Log.d("GetData", "e:" + e);
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (SocketException e) {
			e.printStackTrace();
			Log.d("GetData", "e:" + e);
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("GetData", "e:" + e);
			handler.sendEmptyMessage(R.id.msgServerError);
			HttpConnection.release();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("GetData", "e:" + e);
			handler.sendEmptyMessage(R.id.msgServerError);
			HttpConnection.release();
		}
	}

	/**
	 * post 请求
	 * 
	 * @param callBack1
	 *            请求回调
	 * @param pathUrl
	 *            ip路径
	 * @param data
	 *            传参
	 * @param function
	 *            方法名
	 * @param mark
	 *            该次请求下标
	 */
	public void doPost(ResponseCallBack callBack1, String pathUrl,
			HashMap<String, Object> data, String function, int mark) {
		doPost(callBack1, pathUrl, data, null, function, mark);
	}

	/**
	 * post 请求
	 * 
	 * @param callBack1
	 *            请求回调
	 * @param pathUrl
	 *            ip路径
	 * @param data
	 *            传参
	 * @param Usage
	 *            用途
	 * @param function
	 *            方法名
	 * @param mark
	 *            该次请求下标
	 */
	public void doPost(ResponseCallBack callBack1, String pathUrl,
			HashMap<String, Object> data, String Usage, String function,
			int mark) {
		callBack = callBack1;
		if ("-1".equals(StringUtil.detectionMesh(context))) {
			handler.sendMessage(handler.obtainMessage(R.id.msgNetNotConnected,
					mark, 0, ""));
		} else {

			if (TextUtils.isEmpty(Usage)) {
				Usage = "User";
			}
			// Usage=Config&Function=GetAppConfig
			HashMap<String, Object> parame = new HashMap<String, Object>();
			parame.put("Usage", Usage);
			parame.put("Function", function);
			parame.put(
					"Passport",
					SubstituteEncrypt.getInstance().encrypt(
							String.format(Passport, Usage, function)));
			if (null == data) {
				parame.put("MessageContent", "");
			} else {
				String aaString = data.keySet().toArray()[0].toString();
				if ("sBuffer".equals(aaString)) {
					/*处理常规字符串的情形，如接口GetTimeOrderSimplify需要传递的是"1,1,1"*/					
					parame.put("MessageContent", data.get("sBuffer").toString());
				} else {
					parame.put("MessageContent", map2Json1(data));
				}

			}
			messageContent = new JsonMapOrListJsonMap2JsonUtil<String, Object>()
					.map2Json(parame);
			try {
				// 建立连接
				URL url = new URL(pathUrl);
				HttpURLConnection httpConn = (HttpURLConnection) url
						.openConnection();
				// System.setProperty("sun.net.client.defaultConnectTimeout",
				// "1000");
				// System.setProperty("sun.net.client.defaultReadTimeout",
				// "1000");
				httpConn.setConnectTimeout(10000);// jdk 1.5换成这个,连接超时
				httpConn.setReadTimeout(10000);// jdk 1.5换成这个,读操作超时
				// //设置连接属性
				httpConn.setDoOutput(true);// 使用 URL 连接进行输出
				httpConn.setDoInput(true);// 使用 URL 连接进行输入
				httpConn.setUseCaches(false);// 忽略缓存
				httpConn.setRequestMethod("POST");// 设置URL请求方法

				// 设置请求属性
				// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致ENCODING_UTF_8
				byte[] requestStringBytes = messageContent.getBytes(HTTP.UTF_8);
				httpConn.setRequestProperty("Content-length", ""
						+ requestStringBytes.length);
				httpConn.setRequestProperty("Content-Type", "application/json");
				httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
				httpConn.setRequestProperty("Charset", "UTF-8");
				//
				String name = URLEncoder.encode("黄武艺", "utf-8");
				httpConn.setRequestProperty("NAME", name);

				Log.d(TAG, "uriAPI:\n" + httpConn.getURL() + messageContent);
				// 建立输出流，并写入数据
				OutputStream outputStream = httpConn.getOutputStream();
				outputStream.write(requestStringBytes);
				outputStream.close();
				// 获得响应状态
				int responseCode = httpConn.getResponseCode();
				Log.d(TAG, "访问状态 :\nresponseCode=" + responseCode);
				if (HttpURLConnection.HTTP_OK == responseCode) {// 连接成功

					// 当正确响应时处理数据
					StringBuffer sb = new StringBuffer();
					String readLine;
					BufferedReader responseReader;
					// 处理响应流，必须与服务器响应流输出的编码一致
					responseReader = new BufferedReader(new InputStreamReader(
							httpConn.getInputStream(), HTTP.UTF_8));
					while ((readLine = responseReader.readLine()) != null) {
						sb.append(readLine).append("\n");
					}
					responseReader.close();
					Message msg = new Message();
					msg.obj = sb.toString().trim();
					Log.d("GetData", "返回数据 :\n" + sb.toString());
					handler.sendMessage(handler.obtainMessage(
							R.id.msgResultSuccess, mark, 0, sb.toString()));
				} else {
					handler.sendMessage(handler.obtainMessage(
							R.id.msgResultFailure, mark, 0, ""));
					Log.d("GetData", "msgResultFailure:请求失败");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(R.id.msgTimeOut,
						mark, 0, ""));
			} catch (ConnectTimeoutException e) {
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(R.id.msgTimeOut,
						mark, 0, ""));
			} catch (ConnectException e) {
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(R.id.msgTimeOut,
						mark, 0, ""));
			} catch (SocketException e) {
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(R.id.msgTimeOut,
						mark, 0, ""));
			} catch (IOException e) {
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(R.id.msgServerError,
						mark, 0, ""));
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(R.id.msgServerError,
						mark, 0, ""));
			} finally {
				HttpConnection.release();
			}
		}
	}

	/**
	 * 文件上传
	 * 
	 * @param callBack1
	 * @param url
	 * @param file
	 * @param jsonObject
	 */
	public void doUploadFile(ResponseCallBack callBack1, String url, File file,
			JSONObject jsonObject, int mark) {
		callBack = callBack1;
		String result = null;
		int res = 0;
		long requestTime = System.currentTimeMillis();
		long responseTime = 0;

		/** 上传文件 */
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 1000 * 30);
		HttpConnectionParams.setSoTimeout(httpParameters, 1000 * 30);
		HttpConnectionParams.setTcpNoDelay(httpParameters, true);

		// String path = PictureUtil.zipNewImage(file); // 压缩文件后返回的文件路径
		String path = file.getPath();
		byte[] bytes = null;
		InputStream is;
		File myfile = new File(path);
		try {
			is = new FileInputStream(path);
			bytes = new byte[(int) myfile.length()];
			int len = 0;
			int curLen = 0;
			while ((len = is.read(bytes)) != -1) {
				curLen += len;
				is.read(bytes);
			}
			is.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] updata = GpsImagePackage.getPacket(jsonObject.toString(), bytes); // 参数与文件封装成单个数据包
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse;
		// 单个文件流上传
		InputStream input = new ByteArrayInputStream(updata);
		InputStreamEntity reqEntity;
		reqEntity = new InputStreamEntity(input, -1);
		reqEntity.setContentType("binary/octet-stream");
		reqEntity.setChunked(true);
		httpPost.setEntity(reqEntity);
		try {
			httpResponse = httpClient.execute(httpPost);
			responseTime = System.currentTimeMillis();
			requestTime = (int) ((responseTime - requestTime) / 1000);
			res = httpResponse.getStatusLine().getStatusCode();
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				result = EntityUtils.toString(httpResponse.getEntity());
				Message msg = new Message();
				msg.obj = result;
				Log.d("GetData", "返回数据 :\n" + result);
				handler.sendMessage(handler.obtainMessage(
						R.id.msgResultSuccess, mark, 0, msg));
			} else {
				handler.sendEmptyMessage(R.id.msgResultFailure);
				Log.d("GetData", "msgResultFailure:请求失败"
						+ httpResponse.getStatusLine().getStatusCode());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (ConnectException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (SocketException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgTimeOut);
			HttpConnection.release();
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgServerError);
			HttpConnection.release();
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(R.id.msgServerError);
			HttpConnection.release();
		}
	}

	public void doUpload(ResponseCallBack callBack1, String pathUrl,
			String requestString, int mark, byte[] bytes) {
		callBack = callBack1;
		Object result = null;
		try {
			/************* 创建HttpClient及HttpPost对象 **************/
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 60 * 1000);
			httpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 60 * 1000);
			HttpPost httpPost = new HttpPost(pathUrl);
			httpPost.setHeader("Content-Type", "application/json");

			/************* 向服务器写入数据 **************/
			httpPost.setEntity(new ByteArrayEntity(bytes));

			/************* 开始执行命令 ******************/
			HttpResponse httpResponse = httpClient.execute(httpPost);

			/*************** 接收客户端返回请求状态 **************/
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code == 200) {// 200请求成功
				HttpEntity he = httpResponse.getEntity();
				// 得到的返回数据，具体会返回什么内容找你们写服务端的，如果没有返回可以删除
				result = he;
				Log.e("lidm", "具体会返回什么内容找你们写服务端的" + code);
			} else {// 请求失败，code为异常编码
				Log.e("lidm", "网络请求错误码 ：" + code);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("lidm", "ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("lidm", "IOException");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("lidm", "Exception");
		}
	}

	/**
	 * 遍历xml文档
	 * */
	private String readXml(String resultXml) {
		try {
			// 得到DOM解析器的工厂实例
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			// 从DOM工厂中获得DOM解析器
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			// 把要解析的xml文档读入DOM解析器
			Document doc = dbBuilder.parse(new InputSource(new StringReader(
					resultXml)));
			Element root = doc.getDocumentElement();
			return root.getChildNodes().item(0).getNodeValue()
					.replaceAll("\\\\/", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public String sendPost(String url, String params, int mark) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
			Message msg = new Message();
			msg.obj = result;
			// handler1.sendMessage(handler.obtainMessage(
			// R.id.msgResultSuccess, mark, 0, msg));
			Log.d("GetData", "strResult:" + result);
			handler.sendMessage(handler.obtainMessage(R.id.msgResultSuccess,
					mark, 0, msg));
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	@SuppressLint("HandlerLeak")
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case R.id.msgTimeOut:// 请求超时
				// Toast.makeText(context, text, duration)

				if (null != callBack) {
					callBack.response(null, msg.arg1, 0);
				}
				break;
			case R.id.msgNetNotConnected:// 网络未连接

				if (null != callBack) {
					callBack.response(null, msg.arg1, 1);
				}
				break;
			case R.id.msgServerError:// 连接服务器失败

				if (null != callBack) {
					callBack.response(null, msg.arg1, 2);
				}
				break;
			case R.id.msgResultSuccess:// 成功返回结果
				if (null != callBack) {
					String msage = msg.obj.toString();
					// msage = msage.substring(msage.indexOf("obj") + 4,
					// msage.length() - 2);
					callBack.response(msage, msg.arg1, -1);
				}

				break;
			case R.id.msgResultFailure:// 失败
				if (null != callBack) {
					callBack.response(null, msg.arg1, 3);
				}

				break;
			}
		}

	};

	public String map2Json1(Map data) {
		if (data == null || data.size() == 0) {
			return "{}";
		}
		StringBuilder sb = new StringBuilder("{");
		Set ks = data.keySet();
		for (Iterator iterator = ks.iterator(); iterator.hasNext();) {
			Object k = (Object) iterator.next();
			sb.append((new StringBuilder("'")).append(k.toString())
					.append("':").toString());
			Object v = data.get(k);
			if (null == v) {
				sb.append((new StringBuilder("'")).append(v).append("',")
						.toString());
			} else if (v instanceof Number) {
				sb.append((new StringBuilder()).append(v).append(",")
						.toString());
			} else if (v.getClass().equals(ArrayList.class)) {
				sb.append((new StringBuilder(String
						.valueOf(listJsonMap2Json1((List) v)))).append(",")
						.toString());
			} else if (v.getClass().equals(HashMap.class)) {
				sb.append((new StringBuilder(
						String.valueOf(new JsonMapOrListJsonMap2JsonUtil<String, Object>()
								.map2Json((Map) v)))).append(",").toString());
			} else if (v.getClass().equals(JsonMap.class)) {
				sb.append((new StringBuilder(
						String.valueOf(new JsonMapOrListJsonMap2JsonUtil<String, Object>()
								.map2Json((Map) v)))).append(",").toString());
			} else {
				sb.append((new StringBuilder("'")).append(v).append("',")
						.toString());
			}
		}

		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}

	public static String listJsonMap2Json1(List data) {
		if (data == null || data.size() == 0) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		Object map;
		for (Iterator iterator = data.iterator(); iterator.hasNext(); sb
				.append(map + ",")) {
			map = (Object) iterator.next();
		}

		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Toast
	 * @param data
	 * @return
	 */
	public boolean isOk(JsonMap<String, Object> data) {
		return isOk1(data);
	}

	public boolean isOk1(JsonMap<String, Object> data) {
		boolean returnValue = false;
		if ("1".equals(data.getJsonMap("ResponseStatus").getStringNoNull(
				"ErrorCode"))) {
			returnValue = true;
		} else {
			returnValue = false;
		}
		String messageString = data.getJsonMap("ResponseStatus")
				.getStringNoNull("Message");
		if (!TextUtils.isEmpty(messageString)) {
			Toast.makeText(context,messageString, Toast.LENGTH_LONG).show();
		}
		return returnValue;
	}

	public interface ResponseCallBack {
		/**
		 * 
		 * @param msage
		 *            返回请求数据
		 * @param what
		 *            此次请求id
		 * @param index
		 *            请求状态-1为请求成功
		 */
		public void response(String msage, int what, int index);

	}
}
