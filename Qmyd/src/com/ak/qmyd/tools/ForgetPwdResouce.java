package com.ak.qmyd.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;

/** 
 * @author JGB
 * @date 2015-5-27 ÏÂÎç8:45:10
 */
public class ForgetPwdResouce {
	  

		public String forgetPwd(String phone) throws Exception {
			BHttpClient http = new BHttpClient();

			String apiUrl = Config.USER_FORGET_PWD_URL;
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
