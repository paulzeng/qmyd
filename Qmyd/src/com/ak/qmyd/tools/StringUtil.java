package com.ak.qmyd.tools;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.widget.Toast;

public class StringUtil {

	public static String formatNumber(double number, int fractionDigits) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(fractionDigits);
		format.setMinimumFractionDigits(fractionDigits);
		return format.format(number);
	}

	public static String formatNumber(String number, int fractionDigits) {
		double num = 0;
		try {
			num = Double.parseDouble(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return formatNumber(num, fractionDigits);
	}

	// yyyy-MM-dd
	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	// yyyy-MM-dd
	public static String formatDatetime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	// yyyy-MM-dd
	public static Date toDate(String date) {
		String[] dateParts = date.split("-");
		int year = Integer.parseInt(dateParts[0]);
		int month = Integer.parseInt(dateParts[1]);
		int day = Integer.parseInt(dateParts[2]);
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return cal.getTime();
	}

	public static String toKm(String distance) {
		double dt = 0;
		try {
			dt = Double.parseDouble(distance);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		double km = dt / 1000;
		return formatNumber(km, 2);
	}

	public static void toast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static boolean checkPhoneNumber(String phone) {
		return phone.matches("^((\\+86)|(86))?1[3,5,8]{1}\\d{9}$");
	}

	public static boolean checkNumber(String number) {
		return number.matches("^[0-9]*$")
				|| number
						.matches("^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
	}

	public static boolean checkCode(String code) {
		return code.matches("^[1-9][0-9]{5}$");
	}

	public static String hiddenPhone(String phone) {
		if (checkPhoneNumber(phone)) {
			char[] numbers = phone.toCharArray();
			for (int i = numbers.length - 4; i < numbers.length; i++) {
				numbers[i] = '*';
			}
			return new String(numbers);
		} else {
			return phone;
		}
	}
	/**
	 * ÏÞÖÆÌØÊâ×Ö·û
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String stringFilter(String str) throws PatternSyntaxException {
		String regEx = "[/\\:*?<>|\"\n\t]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

}
