package com.info.quanzi;

import java.util.HashMap;
import java.util.List;

public interface QuanziService {
	public HashMap<String,Object> concern(int c_userid, int c_beconcerned_userid);
	public HashMap<String,Object> undoConcern(int c_userid, int c_beconcerned_userid);
	
	public HashMap<String,Object> isConcern(int c_userid, int c_beconcerned_userid);
	
	public HashMap<String,Object> deleteConcern(String info);
	public HashMap<String,Object> addBlackList(int userid, int shielderid);//加入黑名单
	public HashMap<String,Object> getBlackList(int userid);
	public HashMap<String,Object> deleteBlackList(int userid, int shielderid);
	
	public HashMap<String,Object> getMyFollowersByIndutry(int userid,String i_industry);
	public HashMap<String,Object> getMyFollowersIndustry(int c_beconcerned_userid);
	
	public int getConcernersAmount(int userid);//获取圈子人数（用户关注的人数）
	public int getFollowersAmount(int userid);//获取新的追随者人数（被关注的人数）
	public int getNewFollowersAmount(int userid);//获取新的追随者人数
	public int getLikePostsAmount(int userid);
	public int getLikeActAmount(int userid);
	public int getLikePAAmount(int userid); 
	//获得某个学校里面某个行业的所有用户
	public HashMap<String,Object> getSchoolIndustryUsers(int userid, String industry_item, int school_id);
	
	public HashMap<String,Object> getMyNewFollowers(int userid);//获取我的新追随者用户列表
	public HashMap<String,Object> getQuanziUserListByIndustry(int userid, String industry_item);
	public HashMap<String,Object> getFollowersListByIndustry(int userid, String industry_item);
	
	public HashMap<String,Object> getMyConcernsIndustry(int c_userid);
	
	//获取行业列表
	public HashMap<String,Object> getIndustryList();
	public HashMap<String,Object> getSkillList();
	public HashMap<String,Object> getInterestList();
	//根据电话号码找人
	public HashMap<String,Object> findUserByTel(int userid, String tel);
}
