package com.info.post;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.info.basic.DataModelMapper;
import com.info.basic.Service;
import com.info.json.JsonComment;
import com.info.json.JsonPostItem;
import com.info.json.JsonPostItemContainComments;
import com.info.json.JsonUser;
import com.info.model.CommentModel;
import com.info.sys.Info;
import com.info.table.CommentTable;
import com.info.table.UserTable;


public class PostServiceImpl extends Service implements PostService{

	public PostServiceImpl(DataModelMapper dtMapper) {
		super(dtMapper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<HashMap<String, Object>> getPosts(String condition) {
		// TODO Auto-generated method stub
		List<HashMap<String,Object>> postList = new ArrayList<HashMap<String,Object>>();
		
		
		//JSONObject qCondition = JSONObject.fromObject(condition);
		
		//String where = 
		
		HashMap<String,Object> c = new HashMap<String,Object>();
		
		return null;
	}
	/*
	 * (non-Javadoc)获取学校帖子
	 * @see com.info.post.PostService#getSchoolPosts(int, int, int, java.util.HashMap)
	 */
	@Override
	public HashMap<String, Object> getSchoolPosts(int school_id, int pageNow, int userid, HashMap<String,String> filter) {
		// TODO Auto-generated method stub
		HashMap<String,Object> selectPost = new HashMap<String,Object>();
		HashMap<String,Object> selectComment = new HashMap<String,Object>();
		
		HashMap<String,Object> likeSearch = new HashMap<String,Object>();
		List<HashMap<String,Object>> likeItem = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String,Object> concernSearch = new HashMap<String,Object>();
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		List<JsonPostItem> postList = new ArrayList<JsonPostItem>(); 
		
		int from = pageNow * Info.NUM_PER_PAGE;
		
		String filters = "";
		
		if(!String.valueOf(filter.get(UserTable.U_GENDER)).equals("全部")){
			filters = " and u_gender=" + "'" + String.valueOf(filter.get(UserTable.U_GENDER)) + "'";
		}
		if(!String.valueOf(filter.get(UserTable.U_LOVE_STATE)).equals("全部")){
			filters += " and";
			filters += ( " u_love_state=" + "'" + String.valueOf(filter.get(UserTable.U_LOVE_STATE)) + "'" );
		}
		
		selectPost.put("tables", "p_post,u_user");
		selectPost.put("fields", "p_post.*,u_id,u_nickname,u_small_avatar,u_large_avatar,u_gender");
		String where = "u_user.u_schoolid=" + school_id +
				filters +
				" and p_userid=u_id and p_post_status='NORMAL' "+
				//筛选出未关注用户的帖子
				" and u_id not in("+
				" select c_beconcerned_userid from c_concern where c_userid=" + userid + ")" +
				" order by p_post_time desc limit "+ from + "," + Info.NUM_PER_PAGE;
		selectPost.put("where", where);
		
		System.out.println(where);
		
		likeSearch.put("tables", "pf_post_favor");
		likeSearch.put("fields", "pf_id");//如果有数据就代表喜欢该帖子
		
		concernSearch.put("tables", "c_concern");
		concernSearch.put("fields", "c_id");
		
		DataModelMapper dtMapper = this.getDtMapper();
		
		try{
			postList = dtMapper.selectSchoolPost(selectPost);
			//A评论或者回复B
			selectComment.put("tables", "pc_post_comment,u_user A, u_user B");
			selectComment.put("fields", "pc_commentid as commentid,"
					+ "pc_postid as id,pc_userid as userid,pc_comment_time as comment_time,"
					+ "pc_content as content,pc_type as type,pc_comment_userid as comment_userid,"
					+ "pc_to_userid as to_userid,"
					+ "A.u_small_avatar as Aavatar, A.u_nickname as Anickname, A.u_gender as Agender,"
					+ "B.u_nickname as Bnickname");
			for(JsonPostItem post:postList){
				String ct;
				ct = "pc_postid=" + post.getP_postid() + " and pc_userid=" + post.getP_userid()
						+ " and A.u_id=pc_comment_userid and B.u_id=pc_to_userid"
						+ " and (pc_comment_userid=" + userid
						+ " or pc_to_userid=" + userid + ")"
						+ " order by pc_comment_time desc limit 0,2";

				selectComment.put("where", ct);
				List<Map<String,String>> comments = dtMapper.selectModelsValueString(selectComment);
//				for(int i = 0; i < comments.size(); i++){
//					Map<String,String> c = new HashMap<String,String>();
//					c.put(CommentTable.C_ID, String.valueOf(comments.get(i).get("pc_commentid")));
//					c.put(CommentTable.P_ID, String.valueOf(comments.get(i).get("pc_postid")));
//					c.put(CommentTable.P_USERID, String.valueOf(comments.get(i).get("pc_userid")));
//					//c.put(CommentTable.p, comments.get(i).get("pc_comment_time").toString());
//					c.put(CommentTable.C_CONTENT, comments.get(i).get("pc_content").toString());
//					c.put(CommentTable.COMMENT_TYPE, comments.get(i).get("pc_type").toString());
//					c.put(CommentTable.C_USER_ID, String.valueOf(comments.get(i).get("pc_comment_userid")));
//					c.put(CommentTable.C_USER_AVATAR, comments.get(i).get("Aavatar").toString());
//					c.put(CommentTable.C_USER_GENDER, comments.get(i).get("Agender").toString());
//					c.put(CommentTable.C_USER_NICKNAME, comments.get(i).get("Anickname").toString());
//					//TimeStamp comment_ts = (TimeStamp)comments.get(i).get("pc_comment_time");
//					//Date comment_time = new ((TimeStamp)(comments.get(i).get("pc_comment_time")).ge)
//					//c.put(CommentTable.C_TIME, comments.get(i).get("pc_comment_time").toString());
//					c.put(CommentTable.TO_USER_ID, String.valueOf(comments.get(i).get("pc_to_userid")));
//					c.put(CommentTable.TO_USER_NICKNAME, comments.get(i).get("Bnickname").toString());
//	
//				}
				post.setCommentList(comments);
				//查询是否喜欢该帖子
				likeSearch.put("where", "pf_postid=" + post.getP_postid()
						+ " and pf_userid=" + post.getP_userid()
						+ " and pf_like_userid=" + userid);
				likeItem = dtMapper.selectModels(likeSearch);
				if(likeItem.size()==1)
					post.setLike(true);
				else if(likeItem.size()==0)
					post.setLike(false);
				//查询是否关注该发帖人
				concernSearch.put("where", "c_userid=" + userid + " and c_beconcerned_userid=" + post.getP_userid());
				List<HashMap<String,Object>> concernItem = dtMapper.selectModels(concernSearch);
				if(concernItem.size()==1){
					post.setConcerned(true);
				}else if(concernItem.size()==0){
					post.setConcerned(false);
				}
				
			}
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, postList);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);
		}
		return result;
	}
	
	@Override
	public HashMap<String, Object> getPostsContainComments(int school_id,
			int pageNow) {
		// TODO Auto-generated method stub
				HashMap<String,Object> select = new HashMap<String,Object>();
				HashMap<String,Object> result = new HashMap<String,Object>();
				List<JsonPostItemContainComments> postList = new ArrayList<JsonPostItemContainComments>(); 
				
				int from = pageNow * Info.NUM_PER_PAGE;
				
				select.put("tables", "p_post,u_user");
				select.put("fields", "p_post.*,u_id,u_nickname,u_small_avatar,u_large_avatar,u_gender");
				String where = "u_user.u_schoolid=" + school_id +
						" and p_post.p_userid=u_user.u_id order by p_post.p_post_time desc limit "+ from + "," + Info.NUM_PER_PAGE;
				select.put("where", where);
				
				try{
					postList = this.getDtMapper().selectPostsContainComments(select);
					result.put(Info.STATUS, Info.SUCCEED);
					result.put(Info.DATA, postList);
				}catch(Exception e){
					System.out.println(e.getMessage());
					result.put(Info.STATUS, Info.SERVER_EXCEPTION);
					result.put(Info.DATA, -1);
				}
				return result;
	}

	@Override
	public HashMap<String,Object> addPost(String bigImgPaths, String smallImgPaths, int userid, String content) {
		// TODO Auto-generated method stub
		//JSONObject insertPost = JSONObject.fromObject(post);	
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		try{
			HashMap<String,Object> selectOp = new HashMap<String,Object>();
			String where = "u_id="+userid;
			selectOp.put("tables", "u_user");
			selectOp.put("field", "u_post_amount+1 as post_amount");
			selectOp.put("where", where);
			String post_amount = this.getDtMapper().selectValue(selectOp);
			
			
			String p_thumbnail_list_string="";
			String p_big_photo_list_string = "";
			
			p_thumbnail_list_string = smallImgPaths;
			p_big_photo_list_string = bigImgPaths;
			
			HashMap<String,Object> insertOp = new HashMap<String,Object>();
			String insertFields = "p_postid,p_userid,p_post_time,p_content,p_thumbnail,p_big_photo";
			String values = post_amount+","+userid+","
					+"NOW()"+","+"'"+content+"',"
					+"'"+p_thumbnail_list_string+"',"
					+"'"+p_big_photo_list_string+"'";
			insertOp.put("table", "p_post");
			insertOp.put("fields", insertFields);
			insertOp.put("values", values);
			
			this.getDtMapper().insertModelByValues(insertOp);
			
			HashMap<String,Object> updateOp = new HashMap<String,Object>();
			updateOp.put("table", "u_user");
			updateOp.put("expression", "u_post_amount=u_post_amount+1");
			updateOp.put("where", "u_id="+userid);
			
			this.getDtMapper().updateModels(updateOp);
			
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);
		}
		return result;
	}

	@Override
	public HashMap<String,Object> deletePost(int post_id, int user_id) {
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		HashMap<String,Object> c = new HashMap<String,Object>();
		//JSONObject jsonCondition = JSONObject.fromObject(condition);
		String where = "p_userid="+user_id+" and p_postid="+post_id;
		c.put("table", "p_post");
		c.put("where", where);
		c.put("expression", "p_post_status="+"'DELETE'");

		try{
			this.getDtMapper().updateModels(c);;
			
			HashMap<String,Object> updateOp = new HashMap<String,Object>();
			updateOp.put("table", "u_user");
			updateOp.put("expression", "u_post_amount=u_post_amount-1");
			String u_where = "u_id="+user_id;
			updateOp.put("where", u_where);			
			this.getDtMapper().updateModels(updateOp);
			
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
	public HashMap<String, Object> updatePost() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<String, Object> reportPost(int post_id, int user_id, int report_user_id, String report_reason) {
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> insertOp = new HashMap<String,Object>();
		try{
			insertOp.put("table", "pr_post_report");
			String fields = "pr_postid,pr_userid,pr_report_userid,pr_report_time,pr_report_reason";
			insertOp.put("fields", fields);
			//JSONObject infoJSON = JSONObject.fromObject(info);
			String values = "pr_postid="+post_id
					+",pr_userid="+user_id
					+",pr_report_userid="+report_user_id
					+",pr_report_time='NOW()'"
					+",pr_report_reason="+"'"+report_reason+"'";
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
	public HashMap<String, Object> like(int post_id, int user_id, int like_user_id) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> incOp = new HashMap<String,Object>();
		HashMap<String,Object> insertOp = new HashMap<String,Object>();
		
		HashMap<String,Object> msgInsert = new HashMap<String,Object>();
		HashMap<String,Object> selectLike = new HashMap<String,Object>();
		
		DataModelMapper dtMapper= this.getDtMapper();
		
		try{
			insertOp.put("table", "pf_post_favor");
			String fields = "pf_postid,pf_userid,pf_like_userid,pf_time";
			insertOp.put("fields", fields);
			//JSONObject infoJSON = JSONObject.fromObject(info);
			String values = post_id
					+","+user_id
					+","+like_user_id+","
					+"NOW()";
			insertOp.put("values", values);
			dtMapper.insertModelByValues(insertOp);
			
			incOp.put("table", "p_post");
			incOp.put("expression", "p_favor_count=p_favor_count+1");
			incOp.put("where", "p_postid="+post_id+" and p_userid="+user_id);
			dtMapper.updateModels(incOp);
			//检查之前是否喜欢过
			selectLike.put("tables", "m_message");
			selectLike.put("fields", "messageid");
			selectLike.put("where", "pa_id="+post_id+" and pa_userid="+user_id
					+" and userid="+like_user_id);
			//之前没有喜欢过，则插入，喜欢之后再取消再喜欢仍然只进行一次插入操作
			if(dtMapper.selectModels(selectLike).size()==0){
				//插入消息
				msgInsert.put("table", "m_message");
				msgInsert.put("fields", "pa_id,pa_userid,pa_type,type,pa_image,pa_content,"
						+ "to_userid,userid,username,gender,small_avatar,commentcontent,commenttime");
				
				HashMap<String,Object> select = new HashMap<String,Object>();
				select.put("tables", "p_post");
				select.put("fields", "p_thumbnail,p_content");
				select.put("where", "p_postid=" + post_id + " and p_userid=" + user_id);
				
				HashMap<String,Object> post = dtMapper.selectModels(select).get(0);
				
				if(post.get("p_thumbnail")!=null)
					post.put("p_thumbnail", String.valueOf(post.get("p_thumbnail")).split("\\|")[0]);
				else
					post.put("p_thumbnail", "");
				
				select.put("tables", "u_user");
				select.put("fields", "u_id,u_nickname,u_gender,u_small_avatar");
				select.put("where", "u_id=" + like_user_id);
				
				HashMap<String,Object> user = dtMapper.selectModels(select).get(0);
				
				String MSGvalues = post_id + "," + user_id + ","
						+ 0 + "," + 2 + "," + "'" + post.get("p_thumbnail") +"'," + "'" + post.get("p_content") + "',"
						+ user_id + "," + like_user_id + "," + "'" + user.get("u_nickname") + "'," + "'" 
						+ user.get("u_gender") + "'," + "'" + user.get("u_small_avatar") + "'," + "'喜欢'," 
						+ "NOW()";
				msgInsert.put("values", MSGvalues);
				
				dtMapper.insertModelByValues(msgInsert);
			}			
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA,1);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, -1);
		}
		
		return result;
	}

	@Override
	public HashMap<String, Object> unDoLike(int post_id, int user_id, int like_user_id) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> decOp = new HashMap<String,Object>();
		HashMap<String,Object> delOp = new HashMap<String,Object>();
		
		try{
			//JSONObject infoJSON = JSONObject.fromObject(info);
			delOp.put("table", "pf_post_favor");
			String where = "pf_postid=" + post_id 
					+ " and pf_userid=" + user_id
					+ " and pf_like_userid=" + like_user_id;
			delOp.put("where", where);
			this.getDtMapper().deleteModel(delOp);
			
			decOp.put("table", "p_post");
			decOp.put("expression", "p_favor_count=p_favor_count-1");
			decOp.put("where", "p_postid="+post_id+" and p_userid="+user_id);
			this.getDtMapper().updateModels(decOp);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, -1);
		}
		
		return result;
	}

	@Override
	public HashMap<String, Object> getLikePosts(int user_id) {
		// TODO Auto-generated method stub
		HashMap<String,Object> selectPost = new HashMap<String,Object>();
		HashMap<String,Object> selectComment = new HashMap<String,Object>();
		
		HashMap<String,Object> concernSearch = new HashMap<String,Object>();
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		List<JsonPostItem> postList = new ArrayList<JsonPostItem>(); 
		
		//int from = pageNow * Info.NUM_PER_PAGE;
		
		selectPost.put("tables", "p_post,u_user,pf_post_favor");
		selectPost.put("fields", "p_post.*,u_id,u_nickname,u_small_avatar,u_large_avatar,u_gender");
		String where = "pf_like_userid=" + user_id +
				" and p_postid=pf_postid and p_userid=pf_userid and u_id=pf_like_userid" +
				" and p_post_status='NORMAL' order by p_post_time desc";// limit "+ from + "," + Info.NUM_PER_PAGE;
		selectPost.put("where", where);
		
		concernSearch.put("tables", "c_concern");
		concernSearch.put("fields", "c_id");
		
		DataModelMapper dtMapper = this.getDtMapper();
		
		try{
			postList = dtMapper.selectSchoolPost(selectPost);
			//A评论或者回复B
			selectComment.put("tables", "pc_post_comment,u_user A, u_user B");
			selectComment.put("fields", "pc_commentid as commentid,"
					+ "pc_postid as id,pc_userid as userid,pc_comment_time as comment_time,"
					+ "pc_content as content,pc_type as type,pc_comment_userid as comment_userid,"
					+ "pc_to_userid as to_userid,"
					+ "A.u_small_avatar as Aavatar, A.u_nickname as Anickname, A.u_gender as Agender,"
					+ "B.u_nickname as Bnickname");
			for(JsonPostItem post:postList){
				String ct;
				ct = "pc_postid=" + post.getP_postid() + " and pc_userid=" + post.getP_userid()
						+ " and A.u_id=pc_comment_userid and B.u_id=pc_to_userid"
						+ " and (pc_comment_userid=" + user_id
						+ " or pc_to_userid=" + user_id +")"
						+ " order by pc_comment_time desc limit 0,2";

				selectComment.put("where", ct);
				List<Map<String,String>> comments = dtMapper.selectModelsValueString(selectComment);
				
				post.setCommentList(comments);
				post.setLike(true);
				//查询是否关注该发帖人
				concernSearch.put("where", "c_userid=" + user_id + " and c_beconcerned_userid=" + post.getP_userid());
				List<HashMap<String,Object>> concernItem = dtMapper.selectModels(concernSearch);
				if(concernItem.size()==1){
					post.setConcerned(true);
				}else if(concernItem.size()==0){
					post.setConcerned(false);
				}
				
			}
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, postList);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> commentReplyPosts(CommentModel comment) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		HashMap<String,Object> msgInsert = new HashMap<String,Object>();
		
		DataModelMapper dtMapper = this.getDtMapper();
		try{
			dtMapper.insertComment(comment);
			
			HashMap<String,Object> update = new HashMap<String,Object>();
			update.put("table", "p_post");
			update.put("expression", "p_comment_count=p_comment_count+1");
			update.put("where", "p_postid="+comment.getPc_postid()+
					" and p_userid="+comment.getPc_userid());
			dtMapper.updateModels(update);			
			//插入消息
			msgInsert.put("table", "m_message");
			msgInsert.put("fields", "pa_id,pa_userid,pa_type,type,pa_image,pa_content,"
					+ "to_userid,userid,username,gender,small_avatar,commentcontent,commenttime");
			
			HashMap<String,Object> select = new HashMap<String,Object>();
			select.put("tables", "p_post");
			select.put("fields", "p_thumbnail,p_content");
			select.put("where", "p_postid=" + comment.getPc_postid() + " and p_userid=" + comment.getPc_userid());
			
			HashMap<String,Object> post = dtMapper.selectModels(select).get(0);
			
			if(post.get("p_thumbnail")!=null)
				post.put("p_thumbnail", String.valueOf(post.get("p_thumbnail")).split("\\|")[0]);
			else
				post.put("p_thumbnail", null);
			
			select.put("tables", "u_user");
			select.put("fields", "u_id,u_nickname,u_gender,u_small_avatar");
			select.put("where", "u_id=" + comment.getPc_comment_userid());
			
			HashMap<String,Object> user = dtMapper.selectModels(select).get(0);
			
			String values = comment.getPc_postid() + "," + comment.getPc_userid() + ","
					+ 0 + "," + 0 + "," + "'" + post.get("p_thumbnail") +"'," + "'" + post.get("p_content") + "',"
					+ comment.getPc_to_userid() + "," + comment.getPc_comment_userid() + "," + "'" + user.get("u_nickname") + "'," + "'" + user.get("u_gender") + "',"
					+ "'" + user.get("u_small_avatar") + "'," + comment.getPc_content() + "," 
					+  comment.getPc_comment_time();
			msgInsert.put("values", values);
			
			dtMapper.insertModelByValues(msgInsert);
			
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, comment);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		
		return result;
	}

	@Override
	public HashMap<String, Object> getMyLikePosts(int user_id) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		List<HashMap<String,Object>> postList = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		select.put("tables", "pf_post_favor,p_post");
		select.put("fields", "p_post.*");
		select.put("where", "pf_userid="+user_id+"and p_postid=pf_postid and p_userid=pf_userid");
		try{
			postList = this.getDtMapper().selectModels(select);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, postList);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		return null;
	}

	@Override
	public HashMap<String, Object> getLikeUserList(int post_id, int user_id/*, int pageNow*/) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		List<JsonUser> userList = new ArrayList<JsonUser>();
		
		//int from = pageNow * Info.NUM_PER_PAGE;
		
		select.put("tables", "u_user,pf_post_favor");
		select.put("fields", "u_user.*");
		select.put("where", "pf_postid=" + post_id 
				+ " and pf_userid=" + user_id 
				+ " and u_id=pf_like_userid"
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
	public HashMap<String, Object> getPostComments(int p_postid, int p_userid, int pageNow, int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> selectComment = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		List<JsonComment> commentList = new ArrayList<JsonComment>();
		
		int from = pageNow * Info.NUM_PER_PAGE;
		
		
		try{
			//A评论或者回复B
			selectComment.put("tables", "pc_post_comment,u_user A, u_user B");
			selectComment.put("fields", "pc_commentid as commentid,"
					+ "pc_postid as id,pc_userid as userid,pc_comment_time as comment_time,"
					+ "pc_content as content,pc_type as type,pc_comment_userid as comment_userid,"
					+ "pc_to_userid as to_userid,"
					+ "A.u_small_avatar as Aavatar, A.u_nickname as Anickname, A.u_gender as Agender,"
					+ "B.u_nickname as Bnickname");
			String ct = "pc_postid=" + p_postid + " and pc_userid=" + p_userid
						+ " and A.u_id=pc_comment_userid and B.u_id=pc_to_userid"
						//只能看到发帖和自己的互动
						+ " and (pc_comment_userid=" + userid
						+ " or pc_to_userid=" + userid + ")"
						+ " order by pc_comment_time desc limit "
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
	public HashMap<String, Object> getPostsByUserID(int userid, int my_userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> selectPost = new HashMap<String,Object>();
		HashMap<String,Object> selectComment = new HashMap<String,Object>();
		DataModelMapper dtMapper = this.getDtMapper();
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		List<JsonPostItem> postList = new ArrayList<JsonPostItem>(); 
		
		HashMap<String,Object> likeSearch = new HashMap<String,Object>();
		List<HashMap<String,Object>> likeItem = new ArrayList<HashMap<String,Object>>();
		
		selectPost.put("tables", "p_post,u_user");
		selectPost.put("fields", "p_post.*,u_id,u_nickname,u_small_avatar,u_large_avatar,u_gender");
		String where = "p_userid=" + userid + " and p_userid=u_id and p_post_status='NORMAL' order by p_post_time desc";
		selectPost.put("where", where);
		
		likeSearch.put("tables", "pf_post_favor");
		likeSearch.put("fields", "pf_id");//如果有数据就代表喜欢该帖子
		
		try{
			postList = dtMapper.selectSchoolPost(selectPost);
			//A评论或者回复B
			selectComment.put("tables", "pc_post_comment,u_user A, u_user B");
			selectComment.put("fields", "pc_commentid as commentid,"
					+ "pc_postid as id,pc_userid as userid,pc_comment_time as comment_time,"
					+ "pc_content as content,pc_type as type,pc_comment_userid as comment_userid,"
					+ "pc_to_userid as to_userid,"
					+ "A.u_small_avatar as Aavatar, A.u_nickname as Anickname, A.u_gender as Agender,"
					+ "B.u_nickname as Bnickname");
			for(JsonPostItem post:postList){
				String ct;
				ct = "pc_postid=" + post.getP_postid() + " and pc_userid=" + post.getP_userid()
						+ " and A.u_id=pc_comment_userid and B.u_id=pc_to_userid"
						+ " and (pc_comment_userid=" + userid
						+ " or pc_to_userid=" + userid +")"
						+ " order by pc_comment_time desc limit 0,2";

				selectComment.put("where", ct);
				List<Map<String,String>> comments = dtMapper.selectModelsValueString(selectComment);
				
				post.setCommentList(comments);
				//查询是否喜欢该帖子
				likeSearch.put("where", "pf_postid=" + post.getP_postid()
						+ " and pf_userid=" + post.getP_userid()
						+ " and pf_like_userid=" + my_userid);
				likeItem = dtMapper.selectModels(likeSearch);
				if(likeItem.size()==1)
					post.setLike(true);
				else if(likeItem.size()==0)
					post.setLike(false);				
			}
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, postList);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);
		}
		return result;
	}
/*
 * 获取某一用户圈子的帖子(non-Javadoc)
 * @see com.info.post.PostService#getQuanziPosts(int)
 */
	@Override
	public HashMap<String, Object> getQuanziPosts(int userid) {
		// TODO Auto-generated method stub
				HashMap<String,Object> selectPost = new HashMap<String,Object>();
				HashMap<String,Object> selectComment = new HashMap<String,Object>();
				
				HashMap<String,Object> likeSearch = new HashMap<String,Object>();
				List<HashMap<String,Object>> likeItem = new ArrayList<HashMap<String,Object>>();
				
				HashMap<String,Object> result = new HashMap<String,Object>();
				
				List<JsonPostItem> postList = new ArrayList<JsonPostItem>(); 
				
				//int from = pageNow * Info.NUM_PER_PAGE;
				
				selectPost.put("tables", "p_post,u_user,c_concern");
				selectPost.put("fields", "distinct p_post.*,u_id,u_nickname,u_small_avatar,u_large_avatar,u_gender");
				String where = "(p_userid=" + userid +
						" and u_id=p_userid) or" +
						" c_userid=" + userid +
						" and p_userid=c_beconcerned_userid and u_id=p_userid " +
						" and p_post_status='NORMAL' order by p_post_time desc";// limit "+ from + "," + Info.NUM_PER_PAGE;
				selectPost.put("where", where);
				
				likeSearch.put("tables", "pf_post_favor");
				likeSearch.put("fields", "pf_id");//如果有数据就代表喜欢该帖子
				
				DataModelMapper dtMapper = this.getDtMapper();
				
				try{
					postList = dtMapper.selectSchoolPost(selectPost);
					//A评论或者回复B
					selectComment.put("tables", "pc_post_comment,u_user A, u_user B");
					selectComment.put("fields", "pc_commentid as commentid,"
							+ "pc_postid as id,pc_userid as userid,pc_comment_time as comment_time,"
							+ "pc_content as content,pc_type as type,pc_comment_userid as comment_userid,"
							+ "pc_to_userid as to_userid,"
							+ "A.u_small_avatar as Aavatar, A.u_nickname as Anickname, A.u_gender as Agender,"
							+ "B.u_nickname as Bnickname");
					for(JsonPostItem post:postList){
						String ct;
						ct = "pc_postid=" + post.getP_postid() + " and pc_userid=" + post.getP_userid()
								+ " and A.u_id=pc_comment_userid and B.u_id=pc_to_userid"
								//只能看到自己和发帖人之间的互动
								+ " and (pc_comment_userid=" + userid
								+ " or pc_to_userid=" + userid +")"
								+ " order by pc_comment_time desc limit 0,2";

						selectComment.put("where", ct);
						List<Map<String,String>> comments = dtMapper.selectModelsValueString(selectComment);
						
						post.setCommentList(comments);
						//查询是否喜欢该帖子
						likeSearch.put("where", "pf_postid=" + post.getP_postid()
								+ " and pf_userid=" + post.getP_userid()
								+ " and pf_like_userid=" + userid);
						likeItem = dtMapper.selectModels(likeSearch);
						if(likeItem.size()==1)
							post.setLike(true);
						else if(likeItem.size()==0)
							post.setLike(false);					
					}
					result.put(Info.STATUS, Info.SUCCEED);
					result.put(Info.DATA, postList);
				}catch(Exception e){
					System.out.println(e.getMessage());
					result.put(Info.STATUS, Info.SERVER_EXCEPTION);
					result.put(Info.DATA, -1);
				}
				return result;
	}

	@Override
	public HashMap<String, Object> getFilteredPosts(int school_id,int userid,
			HashMap<String, String> filter, int pageNow) {
		// TODO Auto-generated method stub
				HashMap<String,Object> selectPost = new HashMap<String,Object>();
				HashMap<String,Object> selectComment = new HashMap<String,Object>();
				
				HashMap<String,Object> likeSearch = new HashMap<String,Object>();
				List<HashMap<String,Object>> likeItem = new ArrayList<HashMap<String,Object>>();
				
				HashMap<String,Object> concernSearch = new HashMap<String,Object>();
				
				HashMap<String,Object> result = new HashMap<String,Object>();
				
				List<JsonPostItem> postList = new ArrayList<JsonPostItem>(); 
				
				int from = pageNow * Info.NUM_PER_PAGE;
				String filters = "";
				
				if(!String.valueOf(filter.get("u_gender")).equals("全部")){
					filters = " and u_gender=" + "'" + String.valueOf(filter.get("u_gender")) + "'";
				}
				if(!String.valueOf(filter.get("u_love_state")).equals("全部")){
					filters += " and";
					filters += ( " u_lova_state=" + "'" + String.valueOf(filter.get(filter.get("u_love_state"))) + "'" );
				}
				
				selectPost.put("tables", "p_post,u_user");
				selectPost.put("fields", "p_post.*,u_id,u_nickname,u_small_avatar,u_large_avatar,u_gender");
				String where = "u_user.u_schoolid=" + school_id +
						filters +
						" and p_post.p_userid=u_user.u_id and p_post_status='NORMAL' order by p_post.p_post_time desc limit "+ from + "," + Info.NUM_PER_PAGE;
				selectPost.put("where", where);
				
				likeSearch.put("tables", "pf_post_favor");
				likeSearch.put("fields", "pf_id");//如果有数据就代表喜欢该帖子
				
				concernSearch.put("tables", "c_concern");
				concernSearch.put("fields", "c_id");
				
				DataModelMapper dtMapper = this.getDtMapper();
				
				try{
					postList = dtMapper.selectSchoolPost(selectPost);
					//A评论或者回复B
					selectComment.put("tables", "pc_post_comment,u_user A, u_user B");
					selectComment.put("fields", "pc_commentid as commentid,"
							+ "pc_postid as id,pc_userid as userid,pc_comment_time as comment_time,"
							+ "pc_content as content,pc_type as type,pc_comment_userid as comment_userid,"
							+ "pc_to_userid as to_userid,"
							+ "A.u_small_avatar as Aavatar, A.u_nickname as Anickname, A.u_gender as Agender,"
							+ "B.u_nickname as Bnickname");
					for(JsonPostItem post:postList){
						String ct;
						ct = "pc_postid=" + post.getP_postid() + " and pc_userid=" + post.getP_userid()
								+ " and A.u_id=pc_comment_userid and B.u_id=pc_to_userid"
								+ " and (pc_comment_userid=" + userid
								+ " or pc_to_userid=" + userid +")"
								+ " order by pc_comment_time desc limit 0,2";

						selectComment.put("where", ct);
						List<Map<String,String>> comments = dtMapper.selectModelsValueString(selectComment);

						post.setCommentList(comments);
						//查询是否喜欢该帖子
						likeSearch.put("where", "pf_postid=" + post.getP_postid()
								+ " and pf_userid=" + post.getP_userid()
								+ " and pf_like_userid=" + userid);
						likeItem = dtMapper.selectModels(likeSearch);
						if(likeItem.size()==1)
							post.setLike(true);
						else if(likeItem.size()==0)
							post.setLike(false);
						//查询是否关注该发帖人
						concernSearch.put("where", "c_userid=" + userid + " and c_beconcerned_userid=" + post.getP_userid());
						List<HashMap<String,Object>> concernItem = dtMapper.selectModels(concernSearch);
						if(concernItem.size()==1){
							post.setConcerned(true);
						}else if(concernItem.size()==0){
							post.setConcerned(false);
						}
						
					}
					result.put(Info.STATUS, Info.SUCCEED);
					result.put(Info.DATA, postList);
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
		
		delete.put("table", "pc_post_comment");
		delete.put("where", "pc_commentid="+commentID);
		
		try{
			this.getDtMapper().deleteModel(delete);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			result.put(Info.DATA, -1);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> getPost(int postid, int userid, int my_userid) {
		// TODO Auto-generated method stub
				HashMap<String,Object> selectPost = new HashMap<String,Object>();
				HashMap<String,Object> selectComment = new HashMap<String,Object>();
				DataModelMapper dtMapper = this.getDtMapper();
				
				HashMap<String,Object> result = new HashMap<String,Object>();
				
				List<JsonPostItem> postList = new ArrayList<JsonPostItem>(); 
				
				HashMap<String,Object> likeSearch = new HashMap<String,Object>();
				List<HashMap<String,Object>> likeItem = new ArrayList<HashMap<String,Object>>();
				
				selectPost.put("tables", "p_post,u_user");
				selectPost.put("fields", "p_post.*,u_id,u_nickname,u_small_avatar,u_large_avatar,u_gender");
				String where = "p_userid=" + userid + " and p_postid=" + postid + " and u_id=p_userid and p_post_status='NORMAL' order by p_post_time desc";
				selectPost.put("where", where);
				
				likeSearch.put("tables", "pf_post_favor");
				likeSearch.put("fields", "pf_id");//如果有数据就代表喜欢该帖子
				
				try{
					postList = dtMapper.selectSchoolPost(selectPost);
					System.out.println(postList.size());
					//A评论或者回复B
					selectComment.put("tables", "pc_post_comment,u_user A, u_user B");
					selectComment.put("fields", "pc_commentid as commentid,"
							+ "pc_postid as id,pc_userid as userid,pc_comment_time as comment_time,"
							+ "pc_content as content,pc_type as type,pc_comment_userid as comment_userid,"
							+ "pc_to_userid as to_userid,"
							+ "A.u_small_avatar as Aavatar, A.u_nickname as Anickname, A.u_gender as Agender,"
							+ "B.u_nickname as Bnickname");
					for(JsonPostItem post:postList){
						String ct;
						ct = "pc_postid=" + post.getP_postid() + " and pc_userid=" + post.getP_userid()
								+ " and A.u_id=pc_comment_userid and B.u_id=pc_to_userid"
								+ " and (pc_comment_userid=" + userid
								+ " or pc_to_userid=" + userid +")"
								+ " order by pc_comment_time desc limit 0,5";//显示五条

						selectComment.put("where", ct);
						List<Map<String,String>> comments = dtMapper.selectModelsValueString(selectComment);
						
						post.setCommentList(comments);
						//查询是否喜欢该帖子
						likeSearch.put("where", "pf_postid=" + post.getP_postid()
								+ " and pf_userid=" + post.getP_userid()
								+ " and pf_like_userid=" + my_userid);
						likeItem = dtMapper.selectModels(likeSearch);
						if(likeItem.size()==1)
							post.setLike(true);
						else if(likeItem.size()==0)
							post.setLike(false);				
					}
					result.put(Info.STATUS, Info.SUCCEED);
					result.put(Info.DATA, postList);
				}catch(Exception e){
					System.out.println(e.getMessage());
					result.put(Info.STATUS, Info.SERVER_EXCEPTION);
					result.put(Info.DATA, -1);
				}
				return result;
	}

	

	
	
}
