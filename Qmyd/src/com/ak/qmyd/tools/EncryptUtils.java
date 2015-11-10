package com.ak.qmyd.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;

/** 
 * @author JGB
 * @date 2015-5-25 上午10:09:12
 */
public class EncryptUtils {
	
	/** 
	* 
	* 先MD5加密 再base64
	*/  
	public static String getMD5AndBase64Str(String str) {       
	      MessageDigest messageDigest = null;       
	     
	      try {       
	          messageDigest = MessageDigest.getInstance("MD5");       
	     
	          messageDigest.reset();       
	     
	          messageDigest.update(str.getBytes("UTF-8"));       
	      } catch (NoSuchAlgorithmException e) {       
	          System.exit(-1);       
	      } catch (UnsupportedEncodingException e) {       
	          e.printStackTrace();       
	      }       
	     
	      byte[] byteArray = messageDigest.digest();       
	     
	      StringBuffer md5StrBuff = new StringBuffer();       
	        
	      for (int i = 0; i < byteArray.length; i++) {                   
	          if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)       
	              md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));       
	          else       
	              md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));       
	      }       
	    //16位加密，从第9位到25位  然后base64
	      String md5Str = md5StrBuff.toString().toLowerCase();
	      DebugUtility.showLog("md5...."+md5Str);
	      return Base64.encodeToString(md5Str.getBytes(), Base64.DEFAULT);      
	  }    
}
