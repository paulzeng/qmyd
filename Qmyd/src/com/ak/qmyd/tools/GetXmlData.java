package com.ak.qmyd.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.ak.qmyd.bean.CityName;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class GetXmlData {
	
	static String fileName="shengshi.xml";
	
	public static List<CityName> parseXml(Context context) throws Exception{
		
		List<CityName> list = null;
		CityName cityName = null;
	
		InputStream is=context.getAssets().open(fileName);
		XmlPullParser parser=Xml.newPullParser();//创建解析器
		parser.setInput(is, "gbk");//设置数据进入我们解析器
		int event = parser.getEventType();
		while(event!=XmlPullParser.END_DOCUMENT){
			switch(event){
			case XmlPullParser.START_DOCUMENT:
				list=new ArrayList<CityName>();
				break;
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if(name.equals("city")){
					cityName=new CityName();				
					cityName.setCode(parser.getAttributeValue(0));
					cityName.setName(parser.getAttributeValue(1));			
					list.add(cityName);
				}	
			}	
			event=parser.next();
		}
		is.close();
		return list;
	}
}
