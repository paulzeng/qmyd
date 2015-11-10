package com.ak.qmyd.config;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;

import com.ak.qmyd.bean.User;
import com.ak.qmyd.bean.result.LoginResult.UserObject;
import com.ak.qmyd.tools.CustomCrashHandler;
import com.ak.qmyd.tools.StringUtil;

/** 
 * @author JGB
 * @date 2015-5-9 下午12:20:36
 */
public class MyApplication extends Application{

	public static MyApplication instance;
	public static String downloadUrl;
	public static SharedPreferences userInfoSp;
	public static List<Activity> allActivity = new LinkedList<Activity>();
	@Override
	public void onCreate() {
		super.onCreate();
		CustomCrashHandler mCustomCrashHandler = CustomCrashHandler
				.getInstance();
		mCustomCrashHandler.setCustomCrashHanler(getApplicationContext());
		instance = this;
		userInfoSp = getSharedPreferences("user_info", MODE_PRIVATE);
	}

	/**
	 * 得到手机的硬件id	
	 * @return
	 */
	public String getHardId(){
		return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
	}
	
	/**
	 * 保存数据到SharedPreferences的xml文件中
	 * 
	 * @param key
	 * @param value
	 */
	public void save(String key, String value) {
		Editor editor = userInfoSp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void save(String key, boolean value) {
		Editor editor = userInfoSp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 把activity加入到集合中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		allActivity.add(activity);
	}

	/**
	 * 把activity从集合中删除
	 * 
	 * @param location
	 */
	public void removeActivity(int location) {
		allActivity.remove(location);
	}
	
	public void removeActivity(){
		for (Activity activity : allActivity) {
			activity.finish();
		}
		allActivity.clear();
	}
	public void exit() {
		for (Activity activity : allActivity) {
			activity.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public void saveUserInfo(UserObject userInfo,String sessionId){
		userInfoSp = instance.getSharedPreferences("user_info", MODE_PRIVATE);
		userInfoSp.edit()
		.putString("sessionId", sessionId)//会话id
		.putString("dynamic", userInfo.getDynamic()+"")//动态
		.putString("fans", userInfo.getFans()+"")//粉丝
		.putString("interest", userInfo.getInterest()+"")//关注
		.putString("userId", userInfo.getUserId())//用户id
		.putString("userName", userInfo.getUserName())//用户名
		.putString("sex", userInfo.getSex())//性别
		.putString("height",userInfo.getHeight()+"")//身高
		.putString("weight",userInfo.getWeight()+"")//体重
		.putString("integral",userInfo.getIntegral()+"")//积分
		.putString("description",userInfo.getDescription())//简介
		.putString("thumbnail",userInfo.getThumbnail())//头像 
		.putString("islogin","yes").commit();//是否登录
		
	}
	
	public void saveModifyUserInfo(User userInfo){
		userInfoSp = instance.getSharedPreferences("user_info", MODE_PRIVATE);
		userInfoSp.edit()    
		.putString("userId", userInfo.getUserId())//用户id
		.putString("userName", userInfo.getUserName())//用户名
		.putString("sex", userInfo.getSex())//性别
		.putString("height",userInfo.getHeight()+"")//身高
		.putString("weight",userInfo.getWeight()+"")//体重
		.putString("integral",userInfo.getIntegral()+"")//积分
		.putString("description",userInfo.getDescription())//简介
		.putString("thumbnail",userInfo.getThumbnail())//头像
		.putString("islogin","yes").commit();//是否登录 
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getUserInfo(){
		userInfoSp = instance.getSharedPreferences("user_info", MODE_PRIVATE);
		return (HashMap<String, Object>) userInfoSp.getAll();
	}
	
	public void clearUserInfo(){
		userInfoSp =instance.getSharedPreferences("user_info", MODE_PRIVATE);
		userInfoSp.edit().clear().commit();
	}
	
	public void saveClickTime(Date date){
		userInfoSp = instance.getSharedPreferences("user_info", MODE_PRIVATE);
		userInfoSp.edit().putString("clickTime", StringUtil.formatDatetime(date)).commit();
	}
	
	public String getClickTime(){
		userInfoSp = instance.getSharedPreferences("user_info", MODE_PRIVATE);
		return userInfoSp.getString("clickTime", StringUtil.formatDatetime(new Date()));
	}
	
	private String typeId;

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
}
