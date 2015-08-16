package com.info.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityCommentModel {
	private int ac_commentid;
	private int ac_actid;
	private int ac_userid;
	private String ac_comment_time;
	private String ac_content;
	private String ac_type;
	private int ac_comment_userid;
	private int ac_to_userid;
	
	public ActivityCommentModel(int ac_actid, int ac_userid,String ac_content,
			String ac_type,int ac_comment_userid,int ac_to_userid){
		this.ac_actid = ac_actid;
		this.ac_userid = ac_userid;
		this.ac_comment_time = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "'";
		this.ac_content = ac_content;
		this.ac_type = ac_type;
		this.ac_comment_userid = ac_comment_userid;
		this.ac_to_userid = ac_to_userid;
	}
	public ActivityCommentModel(){
		this.ac_comment_time = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "'";
	}
	public int getAc_commentid() {
		return ac_commentid;
	}
	public void setAc_commentid(int ac_commentid) {
		this.ac_commentid = ac_commentid;
	}
	public int getAc_actid() {
		return ac_actid;
	}
	public void setAc_actid(int ac_actid) {
		this.ac_actid = ac_actid;
	}
	public int getAc_userid() {
		return ac_userid;
	}
	public void setAc_userid(int ac_userid) {
		this.ac_userid = ac_userid;
	}
	public String getAc_comment_time() {
		return ac_comment_time;
	}
	public void setAc_comment_time(String ac_comment_time) {
		this.ac_comment_time = ac_comment_time;
	}
	public String getAc_content() {
		return ac_content;
	}
	public void setAc_content(String ac_content) {
		this.ac_content = ac_content;
	}
	public String getAc_type() {
		return ac_type;
	}
	public void setAc_type(String ac_type) {
		this.ac_type = ac_type;
	}
	public int getAc_comment_userid() {
		return ac_comment_userid;
	}
	public void setAc_comment_userid(int ac_comment_userid) {
		this.ac_comment_userid = ac_comment_userid;
	}
	public int getAc_to_userid() {
		return ac_to_userid;
	}
	public void setAc_to_userid(int ac_to_userid) {
		this.ac_to_userid = ac_to_userid;
	}
	
	
}
