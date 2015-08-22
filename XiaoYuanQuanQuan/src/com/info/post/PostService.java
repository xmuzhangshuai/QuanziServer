package com.info.post;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.info.model.CommentModel;

public interface PostService {
    public List<HashMap<String,Object>> getPosts(String condition);
    public HashMap<String,Object> getSchoolPosts(int school_id, int pageNow, int userid, HashMap<String,String> filter);
    public HashMap<String,Object> getQuanziPosts(int userid);
    public HashMap<String,Object> getFilteredPosts(int school_id, int userid, HashMap<String,String> filter, int pageNow);
    public HashMap<String,Object> getPostsContainComments(int school_id, int pageNow);
    
    public HashMap<String,Object> getPostsByUserID(int userid);
    public HashMap<String,Object> getMyLikePosts(int user_id);
    public HashMap<String,Object> addPost(String bigImgPaths, String smallImgPaths, int userid, String content);
    public HashMap<String,Object> deletePost(int post_id, int user_id);
    public HashMap<String,Object> updatePost();
    public HashMap<String,Object> reportPost(int post_id, int user_id, int report_user_id, String report_reason);
    
    public HashMap<String,Object> like(int post_id, int user_id, int like_user_id);
    public HashMap<String,Object> unDoLike(int post_id, int user_id, int like_user_id);
    public HashMap<String,Object> getLikePosts(int user_id);
    public HashMap<String,Object> getLikeUserList(int post_id, int user_id/*, int pageNow*/);
    
    public HashMap<String,Object> commentReplyPosts(CommentModel comment);
    public HashMap<String,Object> getPostComments(int p_postid, int p_userid, int pageNow, int userid);
    public HashMap<String,Object> deleteComment(int commentID);
}
