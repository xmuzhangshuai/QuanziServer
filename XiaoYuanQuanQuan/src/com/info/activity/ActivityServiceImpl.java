package com.info.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.info.basic.DataModelMapper;
import com.info.basic.Service;
import com.info.json.JsonActItem;
import com.info.json.JsonComment;
import com.info.json.JsonPostItem;
import com.info.json.JsonUser;
import com.info.model.ActivityCommentModel;
import com.info.sys.Info;
import com.info.table.UserTable;

public class ActivityServiceImpl extends Service implements ActivityService{
	
	public ActivityServiceImpl(DataModelMapper dtMapper){
		super(dtMapper);
	}
	@Override
	public List<HashMap<String, Object>> getActivities(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> getSchoolActivities(int school_id, int pageNow, int userid, String type) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> selectComment = new HashMap<String,Object>();
		
		HashMap<String,Object> likeSearch = new HashMap<String,Object>();
		List<HashMap<String,Object>> likeItem = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String,Object> applySearch = new HashMap<String,Object>();
		List<HashMap<String,Object>> applyItem = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		List<JsonActItem> actList = new ArrayList<JsonActItem>(); 
		
		int from = pageNow * Info.NUM_PER_PAGE;
		
		String filters = "";
		
		if(!type.equals("全部")){
			filters = " and a_act_type=" + "'" + type + "'";
		}		
		
		select.put("tables", "a_activity,u_user");
		select.put("fields", "a_activity.*,u_id,u_nickname,u_small_avatar,u_large_avatar,u_gender");
		String where = "u_schoolid=" + school_id +
				filters +
				" and a_userid=u_id and a_status='NORMAL' order by a_addtime desc limit "+ from + "," + Info.NUM_PER_PAGE;
		select.put("where", where);
		
		likeSearch.put("tables", "af_act_favor");
		likeSearch.put("fields", "af_id");//如果有数据就代表喜欢该活动
		
		applySearch.put("tables", "aa_act_apply");
		applySearch.put("fields", "aa_id");//如果有数据就代表加入了该活动
		
		try{
			actList = this.getDtMapper().selectSchoolActivity(select);
			//A评论或者回复B
			selectComment.put("tables", "ac_act_comment,u_user A, u_user B");
			selectComment.put("fields", "ac_commentid as commentid,"
					+ "ac_act_id as id,ac_userid as userid,ac_comment_time as comment_time,"
					+ "ac_content as content,ac_type as type,ac_comment_userid as comment_userid,"
					+ "ac_to_userid as to_userid,"
					+ "A.u_small_avatar as Aavatar, A.u_nickname as Anickname, A.u_gender as Agender,"
					+ "B.u_nickname as Bnickname");
			
			for(JsonActItem act:actList){
				String ct;
				ct = "ac_act_id=" + act.getA_actid() + " and ac_userid=" + act.getA_userid()
						+ " and A.u_id=ac_comment_userid and B.u_id=ac_to_userid"
						+ " order by ac_comment_time desc limit 0,2";

				selectComment.put("where", ct);
				List<Map<String,String>> comments = this.getDtMapper().selectModelsValueString(selectComment);
				act.setCommentList(comments);
				
				//查询是否喜欢该活动
				likeSearch.put("where", "af_act_id=" + act.getA_actid()
						+ " and af_user_id=" + act.getA_userid()
						+ " and af_like_userid=" + userid);
				likeItem = this.getDtMapper().selectModels(likeSearch);
				if(likeItem.size()==1)
					act.setLike(true);
				else if(likeItem.size()==0)
					act.setLike(false);
				//查询是否报名该活动
				applySearch.put("where", "aa_act_id=" + act.getA_actid()
						+ " and aa_user_id=" + act.getA_userid()
						+ " and aa_apply_userid=" + userid);
				applyItem = this.getDtMapper().selectModels(applySearch);
				if(applyItem.size()==1)
					act.setApply(true);
				else if(applyItem.size()==0)
					act.setApply(false);
			}
			
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, actList);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);
		}
		return result;
	}	
	
	@Override
	public HashMap<String, Object> addActivity(String big_img_path_list,String small_img_path_list,int userid, String n_content, String title, String type, String time, String address, String target) {
		// TODO Auto-generated method stub
		//JSONObject insertActivity = JSONObject.fromObject(activity);	
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		try{
			HashMap<String,Object> selectOp = new HashMap<String,Object>();
			String where = "u_id="+userid;
			selectOp.put("tables", "u_user");
			selectOp.put("field", "u_act_amount+1 as act_amount");
			selectOp.put("where", where);
			String act_amount = this.getDtMapper().selectValue(selectOp);
			
			HashMap<String,Object> insertOp = new HashMap<String,Object>();
			String insertFields = "a_actid,a_userid,a_acttime,a_act_type,a_act_title,a_content,a_target,a_thumbnail,a_big_photo,a_address,a_addtime";
			String values = act_amount+","
					+userid+","
					+"'"+time+"',"
					+"'"+type+"',"
					+"'"+title+"',"
					+"'"+n_content+"',"
					+"'"+target+"',"
					+"'"+big_img_path_list+"',"
					+"'"+small_img_path_list+"',"
					+"'"+address+"',"
					+"NOW()";
			insertOp.put("table", "a_activity");
			insertOp.put("fields", insertFields);
			insertOp.put("values", values);
			
			this.getDtMapper().insertModelByValues(insertOp);
			
			HashMap<String,Object> updateOp = new HashMap<String,Object>();
			updateOp.put("table", "u_user");
			updateOp.put("expression", "u_act_amount=u_act_amount+1");
			updateOp.put("where", "u_id="+userid);
			
			this.getDtMapper().updateModels(updateOp);
			
			result.put("status", "succeed");
			result.put(Info.DATA, 1);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.DATA, -1);
			result.put("status", "fail");
		}
		return result;
	}

	@Override
	public HashMap<String, Object> deleteActivity(int a_actid, int a_user_id) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		HashMap<String,Object> c = new HashMap<String,Object>();
		//JSONObject jsonCondition = JSONObject.fromObject(condition);
		String where = "a_userid="+a_user_id+"and a_actid="+a_actid;
		c.put("table", "a_activity");
		c.put("where", where);
		c.put("expression", "a_status="+"'DELETE'");
		
		
		try{
			this.getDtMapper().updateModels(c);
			
			HashMap<String,Object> updateOp = new HashMap<String,Object>();
			updateOp.put("table", "u_user");
			updateOp.put("expression", "u_act_amount=u_act_amount-1");
			String u_where = "u_id="+a_user_id;
			updateOp.put("where", u_where);			
			this.getDtMapper().updateModels(updateOp);
			
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, -1);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> updateActivity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> reportActivityt(int ar_actid,int ar_userid,int ar_report_userid) {
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> insertOp = new HashMap<String,Object>();
		try{
			insertOp.put("table", "ar_act_report");
			String fields = "ar_actid,ar_userid,ar_report_userid,ar_report_time";
			insertOp.put("fields", fields);
			//JSONObject infoJSON = JSONObject.fromObject(info);
			String values = "ar_actid="+ar_actid
					+",ar_userid="+ar_userid
					+",ar_report_userid="+ar_report_userid
					+",ar_report_time='NOW()'";
			insertOp.put("values", values);
			this.getDtMapper().insertModelByValues(insertOp);
			
			
			result.put(Info.STATUS, Info.SUCCEED);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		
		return result;
	}

	@Override
	public HashMap<String, Object> likeActivity(int a_id, int a_userid, int a_like_userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> incOp = new HashMap<String,Object>();
		HashMap<String,Object> insertOp = new HashMap<String,Object>();
		HashMap<String,Object> msgInsert = new HashMap<String,Object>();
		
		HashMap<String,Object> selectLike = new HashMap<String,Object>();
		
		DataModelMapper dtMapper = this.getDtMapper();
		
		try{
			insertOp.put("table", "af_act_favor");
			String fields = "af_act_id,af_user_id,af_like_userid,af_time";
			insertOp.put("fields", fields);
			//JSONObject infoJSON = JSONObject.fromObject(info);
			String values = a_id
					+","+a_userid
					+","+a_like_userid
					+","
					+"NOW()";
			insertOp.put("values", values);
			this.getDtMapper().insertModelByValues(insertOp);
			
			incOp.put("table", "a_activity");
			incOp.put("expression", "a_favor_count=a_favor_count+1");
			incOp.put("where", "a_actid="+a_id+" and a_userid="+a_userid);
			this.getDtMapper().updateModels(incOp);
			//查询是否喜欢过
			selectLike.put("tables", "m_message");
			selectLike.put("fields", "messageid");
			selectLike.put("where", "pa_id="+a_id + " and pa_userid=" + a_userid
					+" and userid="+a_like_userid);
			if(dtMapper.selectModels(selectLike).size()==0){
				//插入消息
				msgInsert.put("table", "m_message");
				msgInsert.put("fields", "pa_id,pa_userid,pa_type,type,pa_image,pa_content,"
						+ "to_userid,userid,username,gender,small_avatar,commentcontent,commenttime");
				
				HashMap<String,Object> select = new HashMap<String,Object>();
				select.put("tables", "a_activity");
				select.put("fields", "a_thumbnail,a_content");
				select.put("where", "a_actid=" + a_id + " and a_userid=" + a_userid);
				
				HashMap<String,Object> act = dtMapper.selectModels(select).get(0);
				
				if(act.get("p_thumbnail")!=null)
					act.put("p_thumbnail", String.valueOf(act.get("p_thumbnail")).split("\\|")[0]);
				else
					act.put("p_thumbnail", "");
				
				select.put("tables", "u_user");
				select.put("fields", "u_id,u_nickname,u_gender,u_small_avatar");
				select.put("where", "u_id=" + a_like_userid);
				
				HashMap<String,Object> user = dtMapper.selectModels(select).get(0);
				
				String MSGvalues = a_id + "," + a_userid + ","
						+ 1 + "," + 2 + "," + "'" + act.get("a_thumbnail") +"'," + "'" + act.get("a_content") + "',"
						+ a_userid + "," + a_like_userid + "," + "'" + user.get("u_nickname") + "'," + "'" + user.get("u_gender") + "',"
						+ "'" + user.get("u_small_avatar") + "'," + "'喜欢'," 
						+ "NOW()";
				msgInsert.put("values", MSGvalues);
				
				dtMapper.insertModelByValues(msgInsert);
				
			}
			
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
			result.put(Info.STATUS, Info.FAILED);
		}
		
		return result;
	}

	@Override
	public HashMap<String, Object> unDoLikeActivity(int af_act_id, int af_user_id, int like_user_id) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> decOp = new HashMap<String,Object>();
		HashMap<String,Object> delOp = new HashMap<String,Object>();
		
		try{
			//JSONObject infoJSON = JSONObject.fromObject(info);
			delOp.put("table", "af_act_favor");
			String where = "af_act_id=" + af_act_id 
					+ " and af_user_id=" + af_user_id
					+ " and af_like_userid=" + like_user_id;
			delOp.put("where", where);
			this.getDtMapper().deleteModel(delOp);
			
			decOp.put("table", "a_activity");
			decOp.put("expression", "a_favor_count=a_favor_count-1");
			decOp.put("where", "a_actid="+ af_act_id +" and a_userid="+af_user_id);
			this.getDtMapper().updateModels(decOp);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, -1);
		}		
		return result;
	}

	@Override
	public HashMap<String, Object> joinActivity(int aa_act_id, int aa_user_id, int aa_apply_userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		//JSONObject infoJSON = JSONObject.fromObject(info);
		HashMap<String,Object> insertOp = new HashMap<String,Object>();
		HashMap<String,Object> incOp = new HashMap<String,Object>();
		
		String values = aa_act_id+","+aa_user_id
				+",NOW(),"+aa_apply_userid;
		insertOp.put("table", "aa_act_apply");
		insertOp.put("fields", "aa_act_id,aa_user_id,aa_time,aa_apply_userid");
		insertOp.put("values", values);
		
		incOp.put("table", "a_activity");
		incOp.put("expression", "a_apply_amount=a_apply_amount+1");
		incOp.put("where", "a_actid="+aa_act_id);
		try{
			this.getDtMapper().insertModelByValues(insertOp);
			this.getDtMapper().updateModels(incOp);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, -1);
		}
		
		return result;
	}
	
	@Override
	public HashMap<String, Object> undoJoinActivity(int aa_act_id, int aa_user_id, int aa_apply_userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		//JSONObject infoJSON = JSONObject.fromObject(info);
		HashMap<String,Object> delOp = new HashMap<String,Object>();
		HashMap<String,Object> decOp = new HashMap<String,Object>();
		
		delOp.put("table", "aa_act_apply");
		delOp.put("where", "aa_act_id=" + aa_act_id
				+ " and aa_user_id=" + aa_user_id
				+ " and aa_apply_userid=" + aa_apply_userid);
		
		decOp.put("table", "a_activity");
		decOp.put("expression", "a_apply_amount=a_apply_amount-1");
		decOp.put("where", "a_actid="+aa_act_id+" and a_userid="+aa_user_id);
		try{
			this.getDtMapper().deleteModel(delOp);
			this.getDtMapper().updateModels(decOp);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, -1);
		}
		
		return result;
	}
	@Override
	public List<HashMap<String, Object>> getActivityTypes() {
		// TODO Auto-generated method stub
		List<HashMap<String,Object>> actList = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> queryOp = new HashMap<String,Object>();
		
		queryOp.put("table", "at_act_type");
		queryOp.put("fields", "*");
		queryOp.put("where", "1=1");
		
		actList = this.getDtMapper().selectModels(queryOp);
		
		return actList;
	}
	@Override
	public HashMap<String, Object> getLikeActivities(int user_id) {
		// TODO Auto-generated method stub
		List<HashMap<String,Object>> activities = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> c = new HashMap<String,Object>();
		//JSONObject cJSON = JSONObject.fromObject(condition);
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		c.put("tables", "a_activity,af_act_favor");
		c.put("fields", "a_activity.*,af_act_favor.af_time");
		c.put("where", "af_user_id="+user_id + " and a_status='NORMAL'");
		try{
			activities = this.getDtMapper().selectModels(c);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, activities);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		return result;
	}
	/*
	 * 活动评论与回复
	 * @see com.info.activity.ActivityService#comment(com.info.model.ActivityCommentModel)
	 */
	@Override
	public HashMap<String, Object> comment(ActivityCommentModel comment) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> msgInsert = new HashMap<String,Object>();
		
		DataModelMapper dtMapper = this.getDtMapper();
		
		try{
			dtMapper.insertActComment(comment);
			//System.out.println(comment.getPc_commentid());
			HashMap<String,Object> update = new HashMap<String,Object>();
			update.put("table", "a_activity");
			update.put("expression", "a_comment_count=a_comment_count+1");
			update.put("where", "a_actid="+comment.getAc_actid()+
					" and a_userid="+comment.getAc_comment_userid());
			dtMapper.updateModels(update);
			
			//插入消息
			msgInsert.put("table", "m_message");
			msgInsert.put("fields", "pa_id,pa_userid,pa_type,type,pa_image,pa_content,"
					+ "to_userid,userid,username,gender,small_avatar,commentcontent,commenttime");
			
			HashMap<String,Object> select = new HashMap<String,Object>();
			select.put("tables", "a_activity");
			select.put("fields", "a_thumbnail,a_content");
			select.put("where", "a_actid=" + comment.getAc_actid() + " and a_userid=" + comment.getAc_userid());
			
			HashMap<String,Object> act = dtMapper.selectModels(select).get(0);
			
			if(act.get("a_thumbnail")!=null)
				act.put("a_thumbnail", String.valueOf(act.get("a_thumbnail")).split("\\|")[0]);
			else
				act.put("a_thumbnail", "");
			
			select.put("tables", "u_user");
			select.put("fields", "u_id,u_nickname,u_gender,u_small_avatar");
			select.put("where", "u_id=" + comment.getAc_comment_userid());
			
			HashMap<String,Object> user = dtMapper.selectModels(select).get(0);
			
			String values = comment.getAc_actid() + "," + comment.getAc_userid() + ","
					+ 1 + "," + 0 + "," + "'" + act.get("a_thumbnail") +"'," + "'" + act.get("a_content") + "',"
					+ comment.getAc_to_userid() + "," + comment.getAc_comment_userid() + "," + "'" 
					+ user.get("u_nickname") + "'," + "'" + user.get("u_gender") + "',"
					+ "'" + user.get("u_small_avatar") + "'," + comment.getAc_content() + "," 
					+ comment.getAc_comment_time();
			msgInsert.put("values", values);
			
			dtMapper.insertModelByValues(msgInsert);
			
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, comment);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, -1);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> getJoinUsers(int aa_actid, int aa_userid/*, int pageNow*/) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result= new HashMap<String,Object>();
//		HashMap<String,Object> update = new HashMap<String,Object>();
		
		List<JsonUser> userList = new ArrayList<JsonUser>();
		
//		int from = pageNow * Info.NUM_PER_PAGE;
				
		select.put("tables", "aa_act_apply,u_user");
		select.put("fields", "u_user.*");
		select.put("where", "aa_act_id="+aa_actid+" and aa_apply_userid=u_id"
				/*+ " limit " + from + "," + Info.NUM_PER_PAGE*/);
		
//		update.put("table", "a_activity");
//		update.put("expression", "a_apply_amount=a_apply_amount+1");
//		update.put("where", "a_actid=" + aa_actid + " and a_userid=" + aa_userid);
		try{
			userList = this.getDtMapper().getUserList(select);
			//this.getDtMapper().updateModels(update);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, userList);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, -1);
		}
		
		return result;
	}
	@Override
	public HashMap<String, Object> getLikeUserList(int a_actid, int user_id/*, int pageNow*/) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		List<JsonUser> userList = new ArrayList<JsonUser>();
		
		//int from = pageNow * Info.NUM_PER_PAGE;
		
		select.put("tables", "u_user,af_act_favor");
		select.put("fields", "u_user.*");
		select.put("where", "af_act_id=" + a_actid 
				+ " and af_user_id=" + user_id 
				+ " and u_id=af_like_userid"
				/*+ " limit " + from + "," + Info.NUM_PER_PAGE*/);
		try{
			userList = this.getDtMapper().getUserList(select);
			result.put(Info.STATUS,Info.SUCCEED);
			result.put(Info.DATA, userList);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.STATUS,Info.FAILED);
			result.put(Info.DATA, -1);
		}
		return result;
	}
	@Override
	public HashMap<String,Object> getActComments(
			int a_actid, int a_userid, int pageNow) {
		// TODO Auto-generated method stub
		HashMap<String,Object> selectComment = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		List<JsonComment> commentList = new ArrayList<JsonComment>();
		
		int from = pageNow * Info.NUM_PER_PAGE;
		
		
		try{
			//A评论或者回复B
			selectComment.put("tables", "ac_act_comment,u_user A, u_user B");
			selectComment.put("fields", "ac_commentid as commentid,"
					+ "ac_act_id as id,ac_userid as userid,ac_comment_time as comment_time,"
					+ "ac_content as content,ac_type as type,ac_comment_userid as comment_userid,"
					+ "ac_to_userid as to_userid,"
					+ "A.u_small_avatar as Aavatar, A.u_nickname as Anickname, A.u_gender as Agender,"
					+ "B.u_nickname as Bnickname");
			
			String ct = "ac_act_id=" + a_actid + " and ac_userid=" + a_userid
					+ " and A.u_id=ac_comment_userid and B.u_id=ac_to_userid"
					+ " order by ac_comment_time desc limit "
					+ from
					+ ","
					+ Info.NUM_PER_PAGE;
			selectComment.put("where", ct);
			commentList = this.getDtMapper().selectComments(selectComment);

			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, commentList);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> getActsByUserID(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> selectComment = new HashMap<String,Object>();
		
		HashMap<String,Object> likeSearch = new HashMap<String,Object>();
		List<HashMap<String,Object>> likeItem = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String,Object> applySearch = new HashMap<String,Object>();
		List<HashMap<String,Object>> applyItem = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		List<JsonActItem> actList = new ArrayList<JsonActItem>(); 
		
		select.put("tables", "a_activity,u_user");
		select.put("fields", "a_activity.*,u_id,u_nickname,u_small_avatar,u_large_avatar,u_gender");
		String where = "a_userid="+ userid +" and a_userid=u_id and a_status='NORMAL' order by a_addtime desc";
		select.put("where", where);
		
		likeSearch.put("tables", "af_act_favor");
		likeSearch.put("fields", "af_id");//如果有数据就代表喜欢该活动
		
		applySearch.put("tables", "aa_act_apply");
		applySearch.put("fields", "aa_id");//如果有数据就代表加入了该活动
		
		try{
			actList = this.getDtMapper().selectSchoolActivity(select);
			//A评论或者回复B
			selectComment.put("tables", "ac_act_comment,u_user A, u_user B");
			selectComment.put("fields", "ac_commentid as commentid,"
					+ "ac_act_id as id,ac_userid as userid,ac_comment_time as comment_time,"
					+ "ac_content as content,ac_type as type,ac_comment_userid as comment_userid,"
					+ "ac_to_userid as to_userid,"
					+ "A.u_small_avatar as Aavatar, A.u_nickname as Anickname, A.u_gender as Agender,"
					+ "B.u_nickname as Bnickname");
			
			for(JsonActItem act:actList){
				String ct;
				ct = "ac_act_id=" + act.getA_actid() + " and ac_userid=" + act.getA_userid()
						+ " and A.u_id=ac_comment_userid and B.u_id=ac_to_userid"
						+ " order by ac_comment_time desc limit 0,2";

				selectComment.put("where", ct);
				List<Map<String,String>> comments = this.getDtMapper().selectModelsValueString(selectComment);
				act.setCommentList(comments);
				
				//查询是否喜欢该活动
				likeSearch.put("where", "af_act_id=" + act.getA_actid()
						+ " and af_user_id=" + act.getA_userid()
						+ " and af_like_userid=" + userid);
				likeItem = this.getDtMapper().selectModels(likeSearch);
				if(likeItem.size()==1)
					act.setLike(true);
				else if(likeItem.size()==0)
					act.setLike(false);
				//查询是否报名该活动
				applySearch.put("where", "aa_act_id=" + act.getA_actid()
						+ " and aa_user_id=" + act.getA_userid()
						+ " and aa_apply_userid=" + userid);
				applyItem = this.getDtMapper().selectModels(applySearch);
				if(applyItem.size()==1)
					act.setApply(true);
				else if(applyItem.size()==0)
					act.setApply(false);
			}
			
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, actList);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> deleteComment(int commentID) {
		// TODO Auto-generated method stub
		HashMap<String, Object> result = new HashMap<String,Object>();
		HashMap<String, Object> delete = new HashMap<String,Object>();
		
		delete.put("table", "ac_act_comment");
		delete.put("where", "ac_commentid="+commentID);
		
		try{
			this.getDtMapper().deleteModel(delete);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			result.put(Info.DATA, -1);
		}
		return result;
	}
	
}
