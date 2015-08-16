package com.info.sys;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import com.cloopen.rest.sdk.CCPRestSDK;

public class Msg {
	public static String sendMsg(String tel){
		HashMap<String, Object> result = null;
		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("sandboxapp.cloopen.com", "8883");
		restAPI.setAccount("aaf98f894ebe0e7e014ebf43587b02a4", "3c52bae42ebc4ed0a7c60724eabf008e");
		
		restAPI.setAppId("aaf98f894ebe0e7e014ebf43995d02a6");
		Random random = new Random();
		int number = random.nextInt(900000)+100000;
		String code = number+""; 
		result = restAPI.sendTemplateSMS(tel,"1" ,new String[]{number+"","2"});

		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			@SuppressWarnings("unchecked")
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
		}else{
			////异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			if(result.get("statusCode").equals("112300")){
				code = "1";//接收手机号为空
			}
			else{
				code ="-1";//短信服务商出错
			}
		}
		System.out.println("code="+code);
		return code;
	}
}
