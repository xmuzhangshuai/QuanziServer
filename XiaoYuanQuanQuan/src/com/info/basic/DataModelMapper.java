package com.info.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.info.json.JsonActItem;
import com.info.json.JsonComment;
import com.info.json.JsonConcern;
import com.info.json.JsonMyMessage;
import com.info.json.JsonPostComment;
import com.info.json.JsonPostItem;
import com.info.json.JsonPostItemContainComments;
import com.info.json.JsonQuanZiItem;
import com.info.json.JsonUser;
import com.info.model.ActivityCommentModel;
import com.info.model.CommentModel;
import com.info.model.UserModel;

public interface DataModelMapper {
	
	public List<HashMap<String,Object>> selectModels(HashMap<String,Object> c);
	public List<Map<String,String>> selectModelsValueString(HashMap<String,Object> c);
	
	public String selectValue(HashMap<String,Object> c);
	
	public String selectExpression(HashMap<String,Object> c);
	
	public void updateModels(HashMap<String,Object> c);
	
	public void insertModelByValues(HashMap<String,Object> c);
	
	public void insertModelBySelct(HashMap<String,Object> c);
	
	public void deleteModel(HashMap<String,Object> c);
	
	public void insertComment(CommentModel comment);
	
	public void insertActComment(ActivityCommentModel comment);
	
	public void insertUser(UserModel user);
	
	public UserModel selectUser(HashMap<String,Object> c);
	
	public List<JsonPostItem> selectSchoolPost(HashMap<String,Object> c);
	
	public List<JsonActItem> selectSchoolActivity(HashMap<String,Object> c);
	
	public List<JsonPostItemContainComments> selectPostsContainComments(HashMap<String,Object> c);
	public JsonPostComment getComments(HashMap<String,Object> c);
	
	public List<JsonUser> getUserList(HashMap<String,Object> c);
	
	public List<JsonComment> selectComments(HashMap<String,Object> c);
	
	public List<JsonMyMessage> selectMsg(HashMap<String,Object> c);
	
	public List<JsonQuanZiItem> selectQuanZi(HashMap<String,Object> c);
	
	public List<JsonConcern> selectConcernList(HashMap<String,Object> c);
}
