package com.info.json;

import java.util.Date;

//�ҵ���Ϣʵ��
public class JsonMyMessage {

	//��Ϣ����
	private int messageid;
	private int type;//0为评论 1为回复 2为赞
	private int pa_type;//0为帖子，1为活动
	private int pa_id;
	private int pa_userid;
	private String pa_image;//���ӻ��ĵ�һ��ͼƬ
	private String pa_content;//���ӻ�������

	private int to_userid;
	//�����˵ľ�������
	private int userid;
	private String username;
	private String gender;
	private String small_avatar;
	private String commentcontent;
	private Date commenttime;
	
	private boolean read;

	public JsonMyMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JsonMyMessage(int messageid, int type, int pa_id, int pa_userid, String pa_iamge, String pa_content,
			int userid, String username, String gender, String small_avatar, String commentcontent, Date commenttime) {
		super();
		this.messageid = messageid;
		this.type = type;
		this.pa_id = pa_id;
		this.pa_userid = pa_userid;
		this.pa_image = pa_iamge;
		this.pa_content = pa_content;
		this.userid = userid;
		this.username = username;
		this.gender = gender;
		this.small_avatar = small_avatar;
		this.commentcontent = commentcontent;
		this.commenttime = commenttime;
	}

	public int getMessageid() {
		return messageid;
	}

	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPa_id() {
		return pa_id;
	}

	public void setPa_id(int pa_id) {
		this.pa_id = pa_id;
	}

	public int getPa_userid() {
		return pa_userid;
	}

	public void setPa_userid(int pa_userid) {
		this.pa_userid = pa_userid;
	}



	public String getPa_content() {
		return pa_content;
	}

	public void setPa_content(String pa_content) {
		this.pa_content = pa_content;
	}

	public int getPa_type() {
		return pa_type;
	}

	public void setPa_type(int pa_type) {
		this.pa_type = pa_type;
	}

	public String getPa_image() {
		return pa_image;
	}

	public void setPa_image(String pa_image) {
		this.pa_image = pa_image;
	}

	
	public int getTo_userid() {
		return to_userid;
	}

	public void setTo_userid(int to_userid) {
		this.to_userid = to_userid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSmall_avatar() {
		return small_avatar;
	}

	public void setSmall_avatar(String small_avatar) {
		this.small_avatar = small_avatar;
	}

	public String getCommentcontent() {
		return commentcontent;
	}

	public void setCommentcontent(String commentcontent) {
		this.commentcontent = commentcontent;
	}

	public Date getCommenttime() {
		return commenttime;
	}

	public void setCommenttime(Date commenttime) {
		this.commenttime = commenttime;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

}
