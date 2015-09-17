package com.info.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import com.info.basic.DataModelMapper;
import com.info.basic.Service;
import com.info.huanxin.HuanXin;
import com.info.json.JsonMyMessage;
import com.info.json.JsonUser;
import com.info.model.UserModel;
import com.info.sys.Info;
import com.info.sys.StudentValidate;
import com.info.table.UserTable;

public class UserServiceImpl extends Service implements UserService{
	
	public UserServiceImpl(DataModelMapper dtMapper){
		super(dtMapper);
	}
	/*
	 * 登录
	 * @see com.info.user.UserService#login(java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String, Object> login(String tel, String pwd) {
		// TODO Auto-generated method stub
		HashMap<String,Object> retData = new HashMap<String,Object>();
		//List<HashMap<String,Object>> userList = new ArrayList<HashMap<String,Object>>();
		JsonUser user = null;
		
		try{
			HashMap<String,Object> c = new HashMap<String,Object>();
			c.put("fields", "*");
			c.put("tables", "u_user");
			c.put("where", "u_tel="+"'"+tel+"' and u_password="+"'"+pwd+"'");
			
			user = this.getDtMapper().getUserList(c).get(0);
						
			if(user==null){
				retData.put(Info.STATUS, Info.FAILED);
				retData.put(Info.DATA, -1);
			}else{
				retData.put(Info.STATUS, Info.SUCCEED);
				retData.put(Info.DATA, user);
				//UserModel user = new UserModel(userList.get(0));
				//retData.put(Info.DATA, user);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			retData.put(Info.STATUS , Info.FAILED);
			retData.put(Info.DATA, -1);
		}

		return retData;
	}
	/*
	 * 注册
	 */
	@Override
	public HashMap<String, Object> regist(UserModel user) {
		// TODO Auto-generated method stub		
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		HashMap<String,Object> c = new HashMap<String,Object>();
		
		DataModelMapper dtMapper = this.getDtMapper();
		/******
		 * 教务处验证
		 */
		String stu_num = user.getU_student_number();
		String pwd = user.getU_student_pass();
		System.out.println("pwd="+pwd);
		//获取教务处学校验证ID
		HashMap<String,Object> s = new HashMap<String,Object>();
		s.put("tables", "s_school");
		s.put("where", "s_id=" + user.getU_schoolid());
		s.put("field", "s_validate_id");
		String validate_id = dtMapper.selectValue(s);
		
		HashMap<String,Object> v_result = new HashMap<String,Object>();
		
		String jw_school_id = null;
		if(user.getU_schoolid()==870){
			jw_school_id = "1";
			//v_result = validatStudentID(stu_num, pwd, jw_school_id);
			v_result.put(Info.DATA, "1");
		}			
		else if(user.getU_schoolid() == 8700){
			jw_school_id = "2";
			v_result.put(Info.DATA, "1");
			user.setU_schoolid(870);
		}	
		if(String.valueOf(v_result.get(Info.DATA)).equals("1")){//登录教务处成功
			try{
				dtMapper.insertUser(user);
				result.put(Info.STATUS, Info.SUCCEED);
				result.put(Info.DATA, user.getU_id());
			}catch(Exception e){
				System.out.println(e.getMessage());
				result.put(Info.STATUS, Info.FAILED);
				result.put(Info.DATA, -1);
			}		
		}
		else
			result = v_result;
		return result;
	}
	/*
	 * 修改信息
	 * @see com.info.user.UserService#updateInfo(java.lang.String)
	 */
	@Override
	public HashMap<String, Object> updateInfo(String unewInfo, String username) {
		// TODO Auto-generated method stub
		JSONObject newInfo = JSONObject.fromObject(unewInfo);
		Iterator keys = newInfo.keys();
		String expression = "";
		while(keys.hasNext()){
			String key = keys.next().toString();
			String value = newInfo.getString(key);
			String exp = key + "=" + "'"+value + "',";
			expression += exp;
		}
		//去除最后一个逗号
		expression.substring(0, expression.length()-1);
		
		HashMap<String,Object> c = new HashMap<String,Object>();
		c.put("expression", expression);
		c.put("table", "u_user");
		c.put("where", "u_username="+"'"+username+"'");
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		try{
			this.getDtMapper().updateModels(c);
			result.put("status", "succeed");
		}catch(Exception e){
			result.put("status", "fail");
		}
		
		return result;
	}
	@Override
	public HashMap<String, Object> getInfo(String info) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		JSONObject infoJSON = JSONObject.fromObject(info);
		
		HashMap<String,Object> selectOp = new HashMap<String,Object>();
		
		selectOp.put("table", "u_user");
		selectOp.put("fields", "*");
		selectOp.put("where","u_id="+infoJSON.getInt("u_id"));
		
		HashMap<String,Object> user = new HashMap<String,Object>();
		try{
			user = this.getDtMapper().selectModels(selectOp).get(0);
			result.put("status", "succeed");
			result.put("userInfo", user);
		}catch(Exception e){
			result.put("status", "error");
		}
		return result;
	}
	@Override
	public HashMap<String, Object> addInterest(int userid, String interest_ids) {
		// TODO Auto-generated method stub
		HashMap<String,Object> insertOp = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		insertOp.put("tables", "u_user");
		insertOp.put("expression", "u_interest_ids="+"'"+interest_ids+"'");
		insertOp.put("where", "u_id="+userid);
		
		try{
			this.getDtMapper().updateModels(insertOp);
			result.put(Info.STATUS, Info.SUCCEED);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> updateInterest(int userid,
			String interest_items) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
				
		update.put("table", "u_user");
		update.put("expression", "u_interest_items="+"'"+interest_items+"'");
		update.put("where", "u_id="+userid);
				
		try{
			this.getDtMapper().updateModels(update);
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
	public HashMap<String, Object> addSkills(int userid, String skills_ids) {
		// TODO Auto-generated method stub
		HashMap<String,Object> insertOp = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
						
		insertOp.put("tables", "u_user");
		insertOp.put("expression", "u_skill_ids="+"'"+skills_ids+"'");
		insertOp.put("where", "u_id="+userid);
						
		try{
			this.getDtMapper().updateModels(insertOp);
			result.put(Info.STATUS, Info.SUCCEED);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> updateSkills(int userid, String skills_ids) {
		// TODO Auto-generated method stub
		HashMap<String,Object> insertOp = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
								
		insertOp.put("tables", "u_user");
		insertOp.put("expression", "u_skill_ids="+"'"+skills_ids+"'");
		insertOp.put("where", "u_id="+userid);
								
		try{
			this.getDtMapper().updateModels(insertOp);
			result.put(Info.STATUS, Info.SUCCEED);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> getInterest(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> selectOp = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		List<HashMap<String,Object>> interests = new ArrayList<HashMap<String,Object>>();
		
		selectOp.put("tables", "u_user");
		selectOp.put("field", "u_interest_ids");
		selectOp.put("where", "u_id="+userid);
		
		String[] interest_ids;
		String inValues="";
		try{
			interest_ids = this.getDtMapper().selectValue(selectOp).split("|");
			for(String interest_id:interest_ids)
				inValues += ("'"+interest_id+"',");
			inValues = inValues.substring(0, inValues.length());
			
			String where = "i_id in"+inValues;
			selectOp.put("tables", "i_interest");
			selectOp.put("fields", "*");
			selectOp.put("where", where);
			interests = this.getDtMapper().selectModels(selectOp);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, interests);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> getSkills(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> selectOp = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
				
		List<HashMap<String,Object>> skills = new ArrayList<HashMap<String,Object>>();
				
		selectOp.put("tables", "u_user");
		selectOp.put("field", "u_skill_ids");
		selectOp.put("where", "u_id="+userid);
				
		String[] interest_ids;
		String inValues="";
		try{
			interest_ids = this.getDtMapper().selectValue(selectOp).split("|");
			for(String interest_id:interest_ids)
				inValues += ("'"+interest_id+"',");
			inValues = inValues.substring(0, inValues.length());
					
			String where = "i_id in"+inValues;
			selectOp.put("tables", "i_industry");
			selectOp.put("fields", "*");
			selectOp.put("where", where);
			skills = this.getDtMapper().selectModels(selectOp);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, skills);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> checkTel(String tel) {
		// TODO Auto-generated method stub
		HashMap<String,Object> selectOp = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		selectOp.put("tables", "u_user");
		selectOp.put("field", "u_tel");
		selectOp.put("where", "u_tel="+"'"+tel+"'");
		
		String u_tel = "";
		
		try{
			u_tel = this.getDtMapper().selectValue(selectOp);
			System.out.println("u_tel="+u_tel);
			if(u_tel==null)
				result.put(Info.STATUS, Info.SUCCEED);
			else{
				result.put(Info.STATUS, Info.FAILED);
				result.put(Info.DATA, -1);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> uploadHeadImg(String bigImgPath,
			String smallImgPath, int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		update.put("table", "u_user");
		update.put("expression", "u_large_avatar=" + "'" + bigImgPath + "'"
				+",u_small_avatar=" + "'" +smallImgPath + "'");
		update.put("where", "u_id="+userid);
		
		try{
			this.getDtMapper().updateModels(update);
			result.put(Info.STATUS, Info.SUCCEED);
			//成功的话，返回大小头像的路径
			HashMap<String,Object> paths = new HashMap<String,Object>();
			paths.put(UserTable.U_LARGE_AVATAR, bigImgPath);
			paths.put(UserTable.U_SMALL_AVATAR, smallImgPath);
			result.put(Info.DATA, paths);
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> updateNickName(int userid, String nickName) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		update.put("table", "u_user");
		update.put("expression", "u_nickname=" + "'" + nickName + "'");
		update.put("where", "u_id=" + userid);
		
		try{
			this.getDtMapper().updateModels(update);
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
	public HashMap<String, Object> updateSex(int userid, String sex) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		update.put("table", "u_user");
		update.put("expression", "u_gender=" + "'" + sex + "'");
		update.put("where", "u_id=" + userid);
		
		try{
			this.getDtMapper().updateModels(update);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);
		}
		return result;
	}
	@SuppressWarnings("deprecation")
	@Override
	public HashMap<String, Object> updateBithday(int userid, String birthday) throws ParseException {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		//计算年龄
		int age;
		
		Date nowDate = new Date();
		Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
		
		age = nowDate.getYear() - birthDate.getYear();
		
		update.put("table", "u_user");
		update.put("expression", "u_birthday=" + "'" + birthday + "'"
				+ ",u_age=" + age);
		update.put("where", "u_id=" + userid);
				
		try{
			this.getDtMapper().updateModels(update);
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
	public HashMap<String, Object> updateSchool(int userid, int schoolid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		HashMap<String,Object> select = new HashMap<String,Object>();
		
		select.put("tables", "s_school,c_city");
		select.put("fields", "s_cityid,c_pid");
		select.put("where", "s_id=" + schoolid + " and s_cityid=c_cityID");
		
		int cityid;
		int provinceid;
		
		try{
			List<HashMap<String,Object>> cityProvinceIDS = this.getDtMapper().selectModels(select);
			HashMap<String,Object> cpIDS = cityProvinceIDS.get(0);
			cityid = Integer.parseInt(cpIDS.get("s_cityid").toString());
			provinceid = Integer.parseInt(cpIDS.get("c_pid").toString());
			
			update.put("table", "u_user");
			update.put("expression", "u_schoolid=" + schoolid
					+ ",u_cityid=" + cityid
					+ ",u_provinceid=" + provinceid);
			update.put("where", "u_id=" + userid);
			
			this.getDtMapper().updateModels(update);
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
	public HashMap<String, Object> updateIdentity(int userid, String identity) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		update.put("table", "u_user");
		update.put("expression", "u_identity=" + "'" + identity + "'");
		update.put("where", "u_id=" + userid);
		
		try{
			this.getDtMapper().updateModels(update);
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
	public HashMap<String, Object> updateLoveStatus(int userid, String status) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		update.put("table", "u_user");
		update.put("expression", "u_love_state=" + "'" + status + "'");
		update.put("where", "u_id=" + userid);
		
		try{
			this.getDtMapper().updateModels(update);
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
	public HashMap<String, Object> updateIntroduce(int userid,
			String introduction) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		update.put("table", "u_user");
		update.put("expression", "u_introduce=" + "'" + introduction + "'");
		update.put("where", "u_id=" + userid);
		
		try{
			this.getDtMapper().updateModels(update);
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
	public HashMap<String, Object> updateIndustry(int userid, String idustry_item) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		update.put("table", "u_user");
		update.put("expression", "u_industry_item=" + "'" + idustry_item + "'");
		update.put("where", "u_id=" + userid);
		
		try{
			this.getDtMapper().updateModels(update);
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
	public HashMap<String, Object> updateSkill(int userid, String skillItems) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
				
		update.put("table", "u_user");
		update.put("expression", "u_skill_items=" + "'" + skillItems + "'");
		update.put("where", "u_id=" + userid);
				
		try{
			this.getDtMapper().updateModels(update);
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
	public HashMap<String, Object> updatePassword(String pwd, int userid, String oldPwd) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		HashMap<String,Object> select = new HashMap<String,Object>();
		
		select.put("tables", "u_user");
		select.put("fields", "u_id");
		select.put("where", "u_id=" + userid + " and u_password=" + "'" + oldPwd + "'");
				
		update.put("table", "u_user");
		update.put("expression", "u_password=" + "'" + pwd + "'");
		update.put("where", "u_id=" + userid + " and u_password=" + "'" + oldPwd + "'");
		DataModelMapper dtMapper = this.getDtMapper();
		if(dtMapper.selectModels(select).size()==1){
			try{
				this.getDtMapper().updateModels(update);
				//跟新环信对应密码
				if(HuanXin.updateuserpwd(userid+"", pwd)!=null){
					result.put(Info.STATUS, Info.SUCCEED);
					result.put(Info.DATA, 1);
				}else
					result.put(Info.DATA, -2);//环信修改错误
			}catch(Exception e){
				System.out.println(e.getMessage());
				result.put(Info.STATUS, Info.SERVER_EXCEPTION);
				result.put(Info.DATA, -3);//服务器错误
			}
		}else{
			result.put(Info.DATA, -1);//旧密码不正确
		}
		return result;
	}
	
	@Override
	public HashMap<String, Object> resetPassword(String pwd, String tel) {
		// TODO Auto-generated method stub
		HashMap<String,Object> update = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		HashMap<String,Object> select = new HashMap<String,Object>();
		
		update.put("table", "u_user");
		update.put("expression", "u_password=" + "'" + pwd + "'");
		update.put("where", "u_tel=" + "'" + tel + "'");
		
		select.put("tables", "u_user");
		select.put("field", "u_id");
		select.put("where", "u_tel=" + "'" + tel + "'");
		
		DataModelMapper dtMapper = this.getDtMapper();
		
		try{
			dtMapper.updateModels(update);
			String u_id = dtMapper.selectValue(select);
			System.out.println("u_id="+u_id);
			//跟新环信对应密码
			String HXresult = HuanXin.updateuserpwd(u_id, pwd);
			if(HXresult!=null){
				result.put(Info.STATUS, Info.SUCCEED);
				result.put(Info.DATA, 1);
			}else
				result.put(Info.DATA, -2);//环信修改错误
		}catch(Exception e){
			System.out.println(e.getMessage());
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);//服务器错误
		}
		return result;
	}	
	
	
	@Override
	public HashMap<String, Object> getInfoByID(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		select.put("tables", "u_user");
		select.put("fields", "*");
		select.put("where", "u_id=" + userid);
		
		try{
			JsonUser user = this.getDtMapper().getUserList(select).get(0);
			result.put(Info.DATA, user);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		return result;
	}
	@Override
	public HashMap<String, Object> getMsg(int userid, int pageNow) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> update = new HashMap<String,Object>();
		
		DataModelMapper dtMapper = this.getDtMapper();
		
		int from = pageNow * Info.NUM_PER_PAGE;
		
		select.put("tables", "m_message");
		select.put("fields", "*");
		select.put("where", "to_userid="+userid
				+ " order by commenttime desc limit " + from + "," + Info.NUM_PER_PAGE);
		
		update.put("table", "m_message");
		update.put("expression", "m_message.read=1");
		update.put("where", "to_userid=" + userid + " and m_message.read=0");
		try{
			
			List<JsonMyMessage> msgList = dtMapper.selectMsg(select);
			this.getDtMapper().updateModels(update);
			result.put(Info.DATA, msgList);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
			
		}
		return result;
	}
	@Override
	public HashMap<String, Object> validatStudentID(String stu_num, String pwd, String jw_school_id) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		String cap = "";
		
		//HashMap<String,Object> search = new HashMap<String,Object>();
		//search.put("tables", "s_school");
		//search.put("fields", "jw_id");
		//search.put("where", value);
		
		try {
			//获取验证码
			cap = StudentValidate.getCapture(jw_school_id, stu_num);
			System.out.println(cap);
			if(cap.equals("null"))
				cap = "NO_CAPTCHA";
			if(cap.equals("该学校不支持")){
				result.put(Info.DATA, -1);//该学校不支持
			}else if(cap.equals("访问不合法")){
				result.put(Info.DATA, -2);//访问不合法
			}else{
				String code = "";
				try {
					code = StudentValidate.login(jw_school_id, stu_num, pwd, cap);
					if(code.equals("0")){
						result.put(Info.DATA, 1);
					}else if(code.equals("10")){
						result.put(Info.DATA, -1);
					}else if(code.equals("1")){
						result.put(Info.DATA, -3);//账号或密码错误
					}else if(code.equals("11")){
						result.put(Info.DATA, -4);//账号不存在
					}else if(code.equals("101")){
						result.put(Info.DATA, -5);//访问不合法
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result.put(Info.DATA, 0);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put(Info.DATA, 0);
		}
		return result;
	}
	
}
