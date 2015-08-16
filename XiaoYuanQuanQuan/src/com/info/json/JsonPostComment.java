package com.info.json;

import java.util.Date;

public class JsonPostComment {
	private int pc_commentid;
	private int pc_postid;
	private int pc_userid;
	private Date pc_comment_time;
	private String pc_content;
	private String pc_type;
	private int pc_comment_userid;
	private int pc_to_userid;
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
	public Date getPc_comment_time() {
		return pc_comment_time;
	}
	public void setPc_comment_time(Date pc_comment_time) {
		this.pc_comment_time = pc_comment_time;
	}
	public String getPc_content() {
		return pc_content;
	}
	public void setPc_cotent(String pc_cotent) {
		this.pc_content = pc_cotent;
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
