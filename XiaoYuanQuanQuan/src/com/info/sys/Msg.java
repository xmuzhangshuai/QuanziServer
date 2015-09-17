package com.info.sys;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import com.cloopen.rest.sdk.CCPRestSDK;

public class Msg {
	public static String sendMsg(String tel){
		HashMap<String, Object> result = null;
		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount("8a48b5514f4fc588014f6a98b17d3a93", "5058339b1174440083b464fa2fc21c4d");
		
		restAPI.setAppId("8a48b5514f73ea32014f78b06534078b");
		Random random = new Random();
		int number = random.nextInt(900000)+100000;
		String code = number+""; 
		result = restAPI.sendTemplateSMS(tel,"34067" ,new String[]{number+"","2"});
		//result = restAPI.sendSMS(tel, "欢迎注册校园圈子你的验证码是:"+number+"请及时输入。【校园圈子】");

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
