package com.ak.qmyd.tools;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

 
public class BHttpClient {
	 

	public String post(String url, List<NameValuePair> params,
			StringEntity entity, List<NameValuePair> headers)
			throws IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		DebugUtility.showLog("REQUESTING " + url);

		// add header params
		if (headers != null) {
			for (int i = 0; i < headers.size(); i++) {
				httppost.addHeader(headers.get(i).getName(), headers.get(i)
						.getValue());
			}
		} 

		// add post params
		if (params != null)
			httppost.setEntity(new UrlEncodedFormEntity(params));
		else if (entity != null)
			httppost.setEntity(entity);

		// Execute HTTP Post Request
		HttpResponse response = httpclient.execute(httppost);
		String jsonResponse = EntityUtils.toString(response.getEntity(),
				"UTF-8");

		DebugUtility.showLog("RESPONSE " + jsonResponse + " "
				+ response.getStatusLine().getStatusCode());

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new ApiException(jsonResponse, response.getStatusLine()
					.getStatusCode());
		} else {
			return jsonResponse;
		}
	}

	public String post(String url, List<NameValuePair> params)
			throws IOException {
		return post(url, params, null, null);
	}

	public String post(String url, List<NameValuePair> params,
			List<NameValuePair> headers) throws IOException {
		return post(url, params, null, headers);
	}

}
