package com.info.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostModel {
	private int p_id;
	private int p_postid;
	private int p_userid;
	private String p_post_time;
	private String p_content;
	private String p_thumbnail;
	private String p_big_photo;
	private int p_comment_count;
	private int p_favour_count;
	private String p_status;
	public PostModel(){
		this.p_post_time = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "'";
	}
	public int getP_id() {
		return p_id;
	}
	public void setP_id(int p_id) {
		this.p_id = p_id;
	}
	public int getP_postid() {
		return p_postid;
	}
	public void setP_postid(int p_postid) {
		this.p_postid = p_postid;
	}
	public int getP_userid() {
		return p_userid;
	}
	public void setP_userid(int p_userid) {
		this.p_userid = p_userid;
	}
	public String getP_post_time() {
		return p_post_time;
	}
	public void setP_post_time(String p_post_time) {
		this.p_post_time = p_post_time;
	}
	public String getP_content() {
		return p_content;
	}
	public void setP_content(String p_content) {
		this.p_content = p_content;
	}
	public String getP_thumbnail() {
		return p_thumbnail;
	}
	public void setP_thumbnail(String p_thumbnail) {
		this.p_thumbnail = p_thumbnail;
	}
	public String getP_big_photo() {
		return p_big_photo;
	}
	public void setP_big_photo(String p_big_photo) {
		this.p_big_photo = p_big_photo;
	}
	public int getP_comment_count() {
		return p_comment_count;
	}
	public void setP_comment_count(int p_comment_count) {
		this.p_comment_count = p_comment_count;
	}
	public int getP_favour_count() {
		return p_favour_count;
	}
	public void setP_favour_count(int p_favour_count) {
		this.p_favour_count = p_favour_count;
	}
	public String getP_status() {
		return p_status;
	}
	public void setP_status(String p_status) {
		this.p_status = p_status;
	}

}
