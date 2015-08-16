package com.info.activity;

import java.util.HashMap;
import java.util.List;

import com.info.json.JsonActItem;
import com.info.model.ActivityCommentModel;

public interface ActivityService {
	public List<HashMap<String,Object>> getActivities(String condition);
	public HashMap<String,Object> getSchoolActivities(int schoolid, int pageNow, int userid, String type);
	public HashMap<String,Object> getActsByUserID(int userid);
	
	
    public HashMap<String,Object> addActivity(String big_img_path_list,String small_img_path_list,int userid, String n_content, String title, String type, String time, String address, String target);
    public HashMap<String,Object> deleteActivity(int a_id, int a_user_id);
    public HashMap<String,Object> updateActivity();
    public HashMap<String,Object> reportActivityt(int ar_actid,int ar_userid,int ar_report_userid);
    
    public HashMap<String,Object> likeActivity(int a_id, int a_userid, int a_like_userid);
    public HashMap<String,Object> unDoLikeActivity(int af_act_id, int af_user_id, int like_user_id);
    public HashMap<String,Object> getLikeActivities(int user_id);
    public HashMap<String,Object> getLikeUserList(int a_actid, int userid/*, int pageNow*/);
    
    
    public HashMap<String,Object> joinActivity(int a_actid, int a_userid, int a_apply_userid);
    public HashMap<String,Object> getJoinUsers(int a_actid, int a_userid/*, int pageNow*/);
    public HashMap<String,Object> undoJoinActivity(int a_actid, int a_userid, int a_apply_userid);
    
    public List<HashMap<String,Object>> getActivityTypes();
    
    public HashMap<String,Object> comment(ActivityCommentModel comment);
    public HashMap<String,Object> getActComments(int a_actid, int a_userid, int pageNow);
}
