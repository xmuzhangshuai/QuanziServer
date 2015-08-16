package com.info.sys;

public class Function {
public static String mysql_validate_string(String s){
	s.replaceAll("'","''");
	return s;
}
}
