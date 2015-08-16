package com.info.huanxin;

import java.util.HashMap;
import java.util.Map;

import com.easemob.server.example.httpclient.EasemobUserAPI;
import com.easemob.server.example.utils.HttpsUtils;
import com.info.sys.MD5For16;

public class HuanXin {
	public static String updateuserpwd(String ID,String newpwd){
		String result = null;
		//���뻷���û�
		String host = "a1.easemob.com";
		String appKey = "yixianqian#quanzi";

		// ��ȡapp����Աtoken
		Map<String, Object> getAccessTokenPostBody = new HashMap<String, Object>();
		getAccessTokenPostBody.put("grant_type", "client_credentials");
		getAccessTokenPostBody.put("client_id", "YXA6AnmL8CRNEeWGWYUzI8my4Q");
		getAccessTokenPostBody.put("client_secret", "YXA6B3wYenYhsJ4k4SMeNoLXlbhBXnc");
		String appAdminToken = EasemobUserAPI.getAccessToken(host, appKey, false, getAccessTokenPostBody);
		System.out.println(appAdminToken);
		
		Map<String, Object> updateUserPwdPostBody = new HashMap<String, Object>();
		updateUserPwdPostBody.put("newpassword", newpwd);

		result = updateUserPwd(host, appKey, ID, HttpsUtils.Map2Json(updateUserPwdPostBody), appAdminToken);
		System.out.println("result"+result);
		return result;
	}
	
	
	public static String createUser(String ID,String pwd){
		String result = null;
		//���뻷���û�
		String host = "a1.easemob.com";
		String appKey = "yixianqian#quanzi";
		
		Map<String, Object> getAccessTokenPostBody = new HashMap<String, Object>();
		getAccessTokenPostBody.put("grant_type", "client_credentials");
		getAccessTokenPostBody.put("client_id", "YXA6AnmL8CRNEeWGWYUzI8my4Q");
		getAccessTokenPostBody.put("client_secret", "YXA6B3wYenYhsJ4k4SMeNoLXlbhBXnc");
		String appAdminToken = EasemobUserAPI.getAccessToken(host, appKey, false, getAccessTokenPostBody);
		System.out.println("token=" + appAdminToken);

		// �����û�
		Map<String, Object> createNewUserPostBody = new HashMap<String, Object>();
		createNewUserPostBody.put("username", ID);
		createNewUserPostBody.put("password", pwd);
		result = EasemobUserAPI.createNewUser(host, appKey, createNewUserPostBody, appAdminToken);
//		EasemobUserAPI.create
		
		return result;
	}
	
	public static String deleteUser(String ID){
		String result = null;
		
		String host = "a1.easemob.com";
		String appKey = "yixianqian#quanzi";

		// ��ȡapp����Աtoken
		Map<String, Object> getAccessTokenPostBody = new HashMap<String, Object>();
		getAccessTokenPostBody.put("grant_type", "password");
		getAccessTokenPostBody.put("username", "zhangshuai");
		getAccessTokenPostBody.put("password", "e23456");
		String appAdminToken = EasemobUserAPI.getAccessToken(host, appKey, false, getAccessTokenPostBody);
		System.out.println(appAdminToken);
		result = EasemobUserAPI.deleteUser(host, appKey, ID, appAdminToken);
		System.out.println("ɾ�������û���result"+result);
		return result;
	}
	
	
	public static String updateUserPwd(String host, String appKey, String id,String body, String token) {

		String orgName = appKey.substring(0, appKey.lastIndexOf("#"));
		String appName = appKey.substring(appKey.lastIndexOf("#") + 1);

		String rest = orgName + "/" + appName + "/users/" + id+"/password/";

		String reqURL = "https://" + host + "/" + rest;
		String result = HttpsUtils.sendSSLRequest(reqURL, token, body, HttpsUtils.Method_PUT);

		return result;
	}
	
}
