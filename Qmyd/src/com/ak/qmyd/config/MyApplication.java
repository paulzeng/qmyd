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
 * @date 2015-5-9 ����12:20:36
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
	 * �õ��ֻ���Ӳ��id	
	 * @return
	 */
	public String getHardId(){
		return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
	}
	
	/**
	 * �������ݵ�SharedPreferences��xml�ļ���
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
	 * ��activity���뵽������
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		allActivity.add(activity);
	}

	/**
	 * ��activity�Ӽ�����ɾ��
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
		.putString("sessionId", sessionId)//�Ựid
		.putString("dynamic", userInfo.getDynamic()+"")//��̬
		.putString("fans", userInfo.getFans()+"")//��˿
		.putString("interest", userInfo.getInterest()+"")//��ע
		.putString("userId", userInfo.getUserId())//�û�id
		.putString("userName", userInfo.getUserName())//�û���
		.putString("sex", userInfo.getSex())//�Ա�
		.putString("height",userInfo.getHeight()+"")//���
		.putString("weight",userInfo.getWeight()+"")//����
		.putString("integral",userInfo.getIntegral()+"")//����
		.putString("description",userInfo.getDescription())//���
		.putString("thumbnail",userInfo.getThumbnail())//ͷ�� 
		.putString("islogin","yes").commit();//�Ƿ��¼
		
	}
	
	public void saveModifyUserInfo(User userInfo){
		userInfoSp = instance.getSharedPreferences("user_info", MODE_PRIVATE);
		userInfoSp.edit()    
		.putString("userId", userInfo.getUserId())//�û�id
		.putString("userName", userInfo.getUserName())//�û���
		.putString("sex", userInfo.getSex())//�Ա�
		.putString("height",userInfo.getHeight()+"")//���
		.putString("weight",userInfo.getWeight()+"")//����
		.putString("integral",userInfo.getIntegral()+"")//����
		.putString("description",userInfo.getDescription())//���
		.putString("thumbnail",userInfo.getThumbnail())//ͷ��
		.putString("islogin","yes").commit();//�Ƿ��¼ 
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
