package com.info.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		Date time = new Date();
		System.out.println("当前时间："+time);
		Date time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2013-09-09 12:00");
		
		String time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").format("2013-09-09 12:00");
		System.out.println("time2="+time2);
	}

}
