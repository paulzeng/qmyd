package com.ak.qmyd.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

	/**
	 * 将时间格式化到毫秒值
	 * @return
	 */
	public static String MSformat(long time){
		/* SimpleDateFormat format;
		if(time < 3600*1000){
			format=new SimpleDateFormat("mm:ss"); 
		}else{
			format=new SimpleDateFormat("HH:mm:ss");
		}
//		format=new SimpleDateFormat("HH:mm:ss");
//		format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String t1=format.format(time);
		return t1;*/ 
        
		long hour = time/(60*60*1000);
		long minute = (time - hour*60*60*1000)/(60*1000);
		long second = (time - hour*60*60*1000 - minute*60*1000)/1000;
		if(second >= 60 )
		{
		  second = second % 60;
		  minute+=second/60;
		}
		if(minute >= 60)
		{
		  minute = minute %60;
		  hour += minute/60;
		}
		String sh = "";
		String sm ="";
		String ss = "";
		if(hour <10)
		{
		   sh = "0" + String.valueOf(hour);
		}else
		{
		   sh = String.valueOf(hour);
		}
		if(minute <10)
		{
		   sm = "0" + String.valueOf(minute);
		}else
		{
		   sm = String.valueOf(minute);
		}
		if(second <10)
		{
		   ss = "0" + String.valueOf(second);
		}else
		{
		   ss = String.valueOf(second);
		}
//		return sh +":"+ sm +":"+ ss;
		return  sm +":"+ ss;
	}
}
