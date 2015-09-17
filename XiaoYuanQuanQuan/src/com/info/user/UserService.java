package com.info.user;

import java.text.ParseException;
import java.util.HashMap;

import com.info.model.UserModel;

public interface UserService {
	public HashMap<String,Object> login(String tel, String pwd);
	public HashMap<String,Object> regist(UserModel user);
	public HashMap<String,Object> updateInfo(String newInfo, String username);
	
	public HashMap<String,Object> uploadHeadImg(String bigImgPath, String smallImgPath, int userid);
	
	public HashMap<String,Object> checkTel(String tel);
	
	public HashMap<String,Object> getInfo(String info);
	public HashMap<String,Object> getInfoByID(int userid);

	public HashMap<String,Object> updateNickName(int userid, String nickName);	
	public HashMap<String,Object> updateSex(int userid, String sex);	
	public HashMap<String,Object> updateBithday(int userid, String birthday) throws ParseException;
	public HashMap<String,Object> updateSchool(int userid, int schoolid);
	public HashMap<String,Object> updateIdentity(int userid, String identity);
	public HashMap<String,Object> updateLoveStatus(int userid, String status);
	public HashMap<String,Object> updateIndustry(int userid, String idustry_item);
	public HashMap<String,Object> updateSkill(int userid, String skillItems);
	public HashMap<String,Object> updateInterest(int userid, String interest_ids);
	public HashMap<String,Object> updateIntroduce(int userid, String introduction);
	
	public HashMap<String,Object> updatePassword(String pwd, int userid, String oldPwd);
	public HashMap<String,Object> resetPassword(String pwd, String tel);
	
	
	public HashMap<String,Object> addInterest(int userid, String interest_ids);

	public HashMap<String,Object> getInterest(int userid);
	
	public HashMap<String,Object> addSkills(int userid, String skills_ids);
	public HashMap<String,Object> updateSkills(int userid, String skills_ids);
	public HashMap<String,Object> getSkills(int userid);
	
	public HashMap<String,Object> getMsg(int userid, int pageNow);
	
	public HashMap<String,Object> validatStudentID(String stu_num, String pwd, String school_id);
	}
