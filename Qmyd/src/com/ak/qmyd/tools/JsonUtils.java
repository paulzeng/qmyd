package com.ak.qmyd.tools;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.ak.qmyd.bean.VenuesListInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonUtils {

	/**
	 * 场馆预约JSON数据的解析
	 * @return 
	 * @throws JSONException 
	 * */
	public static List<VenuesListInformation> parseJson(Context context,String response) throws JSONException {	
		Gson gson = new Gson();
		ArrayList<VenuesListInformation> venuesList = new ArrayList<VenuesListInformation>();
		JSONObject object=new JSONObject(response);	
		String array;
		Type type = new TypeToken<List<VenuesListInformation>>() {
		}.getType();
		int resultCode = object.getInt("resultCode");
		// 返回的描述信息
		String info = object.getString("resultInfo");
		if(resultCode==0){
			//返回失败
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		}else if(resultCode==1){
			//返回成功		
			array=object.getString("venuesList");	
			//得到集合
			venuesList=gson.fromJson(array, type);		
			//init();
			return venuesList;
		}
		return null;
	}
	
	
}
