package com.info.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentModel {
	private int pc_commentid;
	private int pc_postid;
	private int pc_userid;
	private String pc_comment_time;
	private String pc_content;
	private String pc_type;
	private int pc_comment_userid;
	private int pc_to_userid;
	
	public CommentModel(int pc_postid, int pc_userid,String pc_content,
			String pc_type,int pc_comment_userid,int pc_to_userid){
		this.pc_postid = pc_postid;
		this.pc_userid = pc_userid;
		this.pc_comment_time = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "'";
		this.pc_content = pc_content;
		this.pc_type = pc_type;
		this.pc_comment_userid = pc_comment_userid;
		this.pc_to_userid = pc_to_userid;
	}
	public CommentModel(){
		this.pc_comment_time = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "'";
	}
	public int getPc_commentid() {
		return pc_commentid;
	}
	public void setPc_commentid(int pc_commentid) {
		this.pc_commentid = pc_commentid;
	}
	public int getPc_postid() {
		return pc_postid;
	}
	public void setPc_postid(int pc_postid) {
		this.pc_postid = pc_postid;
	}
	public int getPc_userid() {
		return pc_userid;
	}
	public void setPc_userid(int pc_userid) {
		this.pc_userid = pc_userid;
	}
	public String getPc_comment_time() {
		return pc_comment_time;
	}
	public void setPc_comment_time(String pc_comment_time) {
		this.pc_comment_time = pc_comment_time;
	}
	public String getPc_content() {
		return pc_content;
	}
	public void setPc_content(String pc_content) {
		this.pc_content = pc_content;
	}
	public String getPc_type() {
		return pc_type;
	}
	public void setPc_type(String pc_type) {
		this.pc_type = pc_type;
	}
	public int getPc_comment_userid() {
		return pc_comment_userid;
	}
	public void setPc_comment_userid(int pc_comment_userid) {
		this.pc_comment_userid = pc_comment_userid;
	}
	public int getPc_to_userid() {
		return pc_to_userid;
	}
	public void setPc_to_userid(int pc_to_userid) {
		this.pc_to_userid = pc_to_userid;
	}
	
	

}
