package com.ak.qmyd.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;

public class RegisterResource {  

	public String register(String phone) throws Exception {
		BHttpClient http = new BHttpClient();

		String apiUrl = Config.USER_REGISTER_URL;
		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("phone", phone)); 
		params.add(new BasicNameValuePair("hardId", MyApplication.instance.getHardId()));
		String json = null;
		try {
			json = http.post(apiUrl,params);
			return json.toString();
		} catch (ApiException a) {
			DebugUtility.showLog(a.getMessage());
		}
		return null;
	}

	
}
