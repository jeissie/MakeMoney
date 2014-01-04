package com.serve.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;
/**
 * @author jie
 *
 */
public class HttpUtil {
	
	public static final String BASE_URL="http://192.168.1.101:8080/Exchange_server/";
		
		//ͨ��url����post���󣬷���������
		public static String queryStringForPost(String url, List<NameValuePair> params) throws UnsupportedEncodingException
		{
			HttpPost request	= new HttpPost(url);
			//����
			HttpEntity httpEntity=new UrlEncodedFormEntity(params,"UTF-8");
			request.setEntity(httpEntity);
			String result = null;
			try {
				HttpResponse response = new DefaultHttpClient().execute(request); //����
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					result = EntityUtils.toString(response.getEntity());
					Log.v("asdfa",result );
					return result;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				result = "�����쳣";
				return result;
			}catch (IOException e) {
				e.printStackTrace();
				result = "�����쳣";
				return result;
			}
			return null;
		}
		
		public static String queryStringForPost(String url) throws UnsupportedEncodingException
		{
			HttpPost request	= new HttpPost(url);
			String result = "";
			try {
				HttpResponse response = new DefaultHttpClient().execute(request); //����
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					result = EntityUtils.toString(response.getEntity());
					Log.v("http_result",result);
					return result;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				result = "�����쳣";
				return result;
			}catch (IOException e) {
				e.printStackTrace();
				result = "�����쳣";
				return result;
			}
			return null;
		}
		
}
