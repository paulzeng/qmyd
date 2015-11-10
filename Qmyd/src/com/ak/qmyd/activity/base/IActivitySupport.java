package com.ak.qmyd.activity.base;


/**
 * Activity帮助支持类接口.
 * 
 * @author HM
 */
public interface IActivitySupport {

	/**
	 * 
	 * 显示toast.
	 * 
	 * @param text
	 *            内容
	 * @param longint
	 *            内容显示多长时间
	 * @author hm
	 * @update 2015-4-18 上午9:19:54
	 */
	public abstract void showToast(String text, int longint);

	/**
	 * 
	 * 短时间显示toast.
	 * 
	 * @param text
	 * @author hm
	 * @update 2015-4-18 上午9:19:54
	 */
	public abstract void showToast(String text);

	/**
	 * 
	 * 短时间显示toast.
	 * 
	 * @param text
	 * @author hm
	 * @update 2015-4-18 上午9:19:54
	 */
	public abstract void showToast(int resId);
	

	void startActivity(Class<?> clazz ,String str);
}
