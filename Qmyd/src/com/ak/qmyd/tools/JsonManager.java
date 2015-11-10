package com.ak.qmyd.tools;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ak.qmyd.bean.PushInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Json数据处理的工具类
 * 
 * @author Administrator
 * 
 */
public class JsonManager {
	public static Object getJsonItem(JSONArray array, int index, String key) {
		Object value = null;
		if (array != null && array.length() > index) {
			try {
				JSONObject obj = array.getJSONObject(index);
				value = obj.get(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static Object getJsonItem(JSONObject jsonObj, String key) {
		Object value = null;
		if (jsonObj != null) {
			try {
				value = jsonObj.get(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static List<HashMap<String, Object>> jsonArr2List(JSONArray arr) {
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		int len = arr.length();
		try {
			for (int i = 0; i < len; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject jsonObject = arr.getJSONObject(i);
				Iterator<?> it = jsonObject.keys();
				while (it.hasNext()) {
					String key = (String) it.next();
					map.put(key, jsonObject.get(key));
				}
				result.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合并jsonArray
	 * 
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static JSONArray combineJSONArray(JSONArray arr1, JSONArray arr2) {
		if (arr1 == null)
			return arr2;
		String res = null;
		String str1 = arr1.toString().trim();
		String str2 = arr2.toString().trim();
		res = str1.substring(0, str1.length() - 1) + ","
				+ str2.substring(1, str1.length());
		try {
			return new JSONArray(res);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<PushInfo> getListFromJSON(String str, Class<PushInfo> type) {
	      Type listType = new TypeToken<ArrayList<PushInfo>>() {
	      }.getType();
	      List<PushInfo> list = new Gson().fromJson(str, listType);
	      return list;
	  }
}