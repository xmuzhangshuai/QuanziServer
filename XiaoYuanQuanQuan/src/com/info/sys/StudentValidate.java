package com.info.sys;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSONObject;

public class StudentValidate {

	private static final String ACCESS_TOKEN = "7e0ba9afc16377753667ddd50cb039d93a6ad8dd ";
	//获取验证码
	public static String getCapture(String school_id, String stu_id) throws Exception{
		String requestURL = "getCaptcha/" + school_id + "/" + stu_id +"?access_token=" + ACCESS_TOKEN;
		String requestResult = doGet(requestURL);
		JSONObject result = JSONObject.parseObject(requestResult);
		String capture = "";
		//获取验证码
		if(result.getString("code").equals("0")){
			capture = result.getString("data");
		}else if(result.getString("code").equals("1")){
			capture = "该学校不支持";
		}else if(result.getString("code").equals("访问")){
			capture = "访问不合法";
		}else{
			capture="出错";
		}
		return capture;
	}
	//登录
	public static String login(String school_id, String stu_id, String stu_pwd, String capture) throws Exception{
		String requestURL = "login/" + school_id + "/" + stu_id + "/" + stu_pwd + "/" +  capture
				+"?access_token=" + ACCESS_TOKEN;
		String requestResult = doGet(requestURL);
		JSONObject result = JSONObject.parseObject(requestResult);
		String resultCode = result.getString("code");
		return resultCode;
	}
	public static String doGet(String requestURL) throws Exception {
		String root = "http://cooperate.muni.pub/";
		String url = root + requestURL;
		System.out.println(url);
        URL URL = new URL(url);
        URLConnection connection = URL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }
        
        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            
        } finally {
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }
        
        return resultBuffer.toString();
    }
    
}
