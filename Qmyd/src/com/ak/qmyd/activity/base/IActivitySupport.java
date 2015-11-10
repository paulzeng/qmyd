package com.ak.qmyd.activity.base;


/**
 * Activity����֧����ӿ�.
 * 
 * @author HM
 */
public interface IActivitySupport {

	/**
	 * 
	 * ��ʾtoast.
	 * 
	 * @param text
	 *            ����
	 * @param longint
	 *            ������ʾ�೤ʱ��
	 * @author hm
	 * @update 2015-4-18 ����9:19:54
	 */
	public abstract void showToast(String text, int longint);

	/**
	 * 
	 * ��ʱ����ʾtoast.
	 * 
	 * @param text
	 * @author hm
	 * @update 2015-4-18 ����9:19:54
	 */
	public abstract void showToast(String text);

	/**
	 * 
	 * ��ʱ����ʾtoast.
	 * 
	 * @param text
	 * @author hm
	 * @update 2015-4-18 ����9:19:54
	 */
	public abstract void showToast(int resId);
	

	void startActivity(Class<?> clazz ,String str);
}
