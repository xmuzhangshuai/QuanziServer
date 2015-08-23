package com.info.quanzi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.info.basic.DataModelMapper;
import com.info.basic.Service;
import com.info.json.JsonConcern;
import com.info.json.JsonQuanZiItem;
import com.info.json.JsonUser;
import com.info.sys.Info;

public class QuanziServiceImpl extends Service implements QuanziService {

	public QuanziServiceImpl(DataModelMapper dtMapper) {
		super(dtMapper);
	}

	@Override
	public HashMap<String, Object> concern(int c_userid, int c_beconcerned_userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		DataModelMapper dtMapper = this.getDtMapper();
		
		HashMap<String,Object> selectOp = new HashMap<String,Object>();
		
		selectOp.put("tables", "c_concern");
		selectOp.put("fields", "c_id");
		selectOp.put("where", "c_userid=" + c_userid + " and c_beconcerned_userid=" + c_beconcerned_userid);
		
		//未关注过
		if(dtMapper.selectModels(selectOp).size()==0){
			HashMap<String,Object> insertOp = new HashMap<String,Object>();
			
			HashMap<String,Object> incOp = new HashMap<String,Object>();
			//JSONObject infoJSON = JSONObject.fromObject(info);
			
			String values = c_userid+","+c_beconcerned_userid+","+"NOW()";
			insertOp.put("table", "c_concern");
			insertOp.put("fields", "c_userid,c_beconcerned_userid,c_concern_time");
			insertOp.put("values", values);
			
			incOp.put("table", "u_user");
			try{
				dtMapper.insertModelByValues(insertOp);
				//关注用户关注人数加1
				incOp.put("expression", "u_concern_amount=u_concern_amount+1");
				incOp.put("where", "u_id="+c_userid);			
				dtMapper.updateModels(incOp);
				
				//被关注用户被关注数加1
				incOp.put("expression", "u_beconcern_amount=u_beconcern_amount+1");
				incOp.put("where", "u_id="+c_beconcerned_userid);
				dtMapper.updateModels(incOp);
				
				result.put(Info.STATUS, Info.SUCCEED);
				result.put(Info.DATA, 1);
			}catch(Exception e){
				e.printStackTrace();
				result.put(Info.STATUS, Info.FAILED);
				result.put(Info.DATA, -1);
			}
		}else
			result.put(Info.DATA, -2);
		return result;
	}

	@Override
	public HashMap<String, Object> undoConcern(int c_userid, int c_beconcerned_userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> deletetOp = new HashMap<String,Object>();
		//JSONObject infoJSON = JSONObject.fromObject(info);
		
		HashMap<String,Object> decOp = new HashMap<String,Object>();
		
		DataModelMapper dtMapper = this.getDtMapper();
		
		deletetOp.put("table", "c_concern");
		deletetOp.put("where", "c_userid="+c_userid
				+" and c_beconcerned_userid="+c_beconcerned_userid);
		
		decOp.put("table", "u_user");
		try{
			dtMapper.deleteModel(deletetOp);
			//取消关注用户关注人数加-1
			decOp.put("expression", "u_concern_amount=u_concern_amount-1");
			decOp.put("where", "u_id="+c_userid);			
			dtMapper.updateModels(decOp);
			
			//被取消关注用户被关注数-1
			decOp.put("expression", "u_beconcern_amount=u_beconcern_amount-1");
			decOp.put("where", "u_id="+c_beconcerned_userid);
			dtMapper.updateModels(decOp);
			
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, 1);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.STATUS, -1);
		}
		
		return result;
	}

	@Override
	public HashMap<String, Object> deleteConcern(String info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> addBlackList(int userid, int shielderid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> insertOp = new HashMap<String,Object>();
		//用于修改关注人数等
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> update = new HashMap<String,Object>();
		
		HashMap<String,Object> del = new HashMap<String,Object>();

		insertOp.put("table", "bl_blacklist");
		insertOp.put("fields", "bl_userid,bl_shielderid");
		insertOp.put("values", userid + ","+shielderid);
		
		del.put("table", "c_concern");
		del.put("where", "c_userid="+userid + " and c_beconcerned_userid="+shielderid
				+" or (c_userid=" + shielderid + " and c_beconcerned_userid="+userid + ")");
		//修改关注人数等
		select.put("tables", "c_concern");
		select.put("fields", "c_id");
		
		update.put("table", "u_user");
		
		DataModelMapper dtMapper = this.getDtMapper();
		try{
			dtMapper.insertModelByValues(insertOp);
			//修改人数
			select.put("where", "c_userid="+userid + " and c_beconcerned_userid="+shielderid);
			if(dtMapper.selectModels(select).size()==1){
				update.put("expression", "u_concern_amount=u_concern_amount-1");
				update.put("where", "u_id="+userid);
				dtMapper.updateModels(update);
				
				update.put("expression", "u_beconcern_amount=u_beconcern_amount-1");
				update.put("where", "u_id="+shielderid);
				dtMapper.updateModels(update);
			}
			select.put("where", "c_userid=" + shielderid + " and c_beconcerned_userid=" + userid);
			if(dtMapper.selectModels(select).size()==1){
				update.put("expression", "u_concern_amount=u_concern_amount-1");
				update.put("where", "u_id="+shielderid);
				dtMapper.updateModels(update);
				
				update.put("expression", "u_beconcern_amount=u_beconcern_amount-1");
				update.put("where", "u_id="+userid);
				dtMapper.updateModels(update);
			}
			dtMapper.deleteModel(del);
			result.put("status", "succeed");
			result.put(Info.DATA, 1);
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "error");
			result.put(Info.DATA, -1);
		}
		
		return result;
	}
	
	@Override
	public HashMap<String, Object> deleteBlackList(int userid, int shielderuserid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> deletetOp = new HashMap<String,Object>();
		//JSONObject infoJSON = JSONObject.fromObject(info);
		
		deletetOp.put("table", "bl_blacklist");
		deletetOp.put("where", "bl_userid="+userid
				+" and bl_shielderid="+shielderuserid);
		try{
			this.getDtMapper().deleteModel(deletetOp);
			result.put("status", "succeed");
			result.put(Info.DATA, 1);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		
		return result;
	}
	
	@Override
	public HashMap<String, Object> getMyFollowersByIndutry(int userid,String i_industry) {
		// TODO Auto-generated method stub
		List<HashMap<String,Object>> concerns = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> selectOp = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		//JSONObject infoJSON = JSONObject.fromObject(info);
		
		String where = "c_beconcerned_userid="+userid
				+" and c_userid=u_id"
				+" and u_industry like "+ "'" + i_industry + "'";
		
		selectOp.put("tables", "c_concern,u_user");
		selectOp.put("fields", "u_id,u_nickname,u_small_avatar,c_concern_time");
		selectOp.put("where", where);
		
		try{
			concerns = this.getDtMapper().selectModels(selectOp);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, concerns);
		}catch(Exception e){
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.DATA, Info.SERVER_EXCEPTION);
		}
		
		result.put("concerns", concerns);
		return result;
	}
	
	@Override
	public HashMap<String, Object> getMyFollowersIndustry(int c_beconcerned_userid) {
		// TODO Auto-generated method stub
		List<JsonQuanZiItem> industryList = new ArrayList<JsonQuanZiItem>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> selectOp = new HashMap<String,Object>();
		//JSONObject infoJSON = JSONObject.fromObject(info);
		
		String where= "c_beconcerned_userid="+c_beconcerned_userid
				+" and c_userid=u_id"
				+" and u_industry_item=i_name group by i_id";
		
		selectOp.put("tables", "c_concern,u_user,i_industry");
		selectOp.put("fields", "i_industry.i_id,i_industry.i_name,COUNT(i_id) as amount");
		selectOp.put("where", where);
		
		try{
			industryList = this.getDtMapper().selectQuanZi(selectOp);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, industryList);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.STATUS, Info.SERVER_EXCEPTION);
			result.put(Info.DATA, -1);
		}
		
		//result.put("industryList", industryList);
		return result;
	}	

	
	@Override
	public HashMap<String, Object> getMyConcernsIndustry(int c_userid) {
		// TODO Auto-generated method stub
		List<JsonQuanZiItem> industryList = new ArrayList<JsonQuanZiItem>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> selectOp = new HashMap<String,Object>();
		//JSONObject infoJSON = JSONObject.fromObject(info);
		
		String where= "c_userid="+c_userid
				+" and c_beconcerned_userid=u_id"
				+" and u_industry_item=i_name group by i_id";
		
		selectOp.put("tables", "c_concern,u_user,i_industry");
		selectOp.put("fields", "i_industry.i_id,i_industry.i_name,COUNT(i_id) as amount");
		selectOp.put("where", where);
		
		try{
			industryList = this.getDtMapper().selectQuanZi(selectOp);
			result.put(Info.STATUS, Info.SUCCEED);
			result.put(Info.DATA, industryList);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.STATUS, Info.FAILED);
			result.put(Info.STATUS, -1);
		}
		
		//result.put("industryList", industryList);
		return result;
	}

	@Override
	public HashMap<String, Object> isConcern(int c_userid,
			int c_beconcerned_userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> find = new HashMap<String,Object>();
		
		find.put("tables", "c_concern");
		find.put("fields", "c_id");
		find.put("where", "c_userid=" + c_userid 
				+ " and c_beconcerned_userid=" + c_beconcerned_userid);
		try{
			if(this.getDtMapper().selectModels(find).size()==1)
				result.put(Info.DATA, 1);
			else
				result.put(Info.DATA, 0);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		
		return result;
	}

	/*
	 * 我的主页中需要的数据
	 */
	
	@Override
	public int getConcernersAmount(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		
		select.put("tables", "c_concern");
		select.put("field", "COUNT(*) as amount");
		select.put("where", "c_userid=" + userid);
		
		String amount = "0";
		try{
			amount = this.getDtMapper().selectValue(select);
		}catch(Exception e){
			e.printStackTrace();
			amount = "-1";
		}
		return Integer.parseInt(amount);
	}

	@Override
	public int getFollowersAmount(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		
		select.put("tables", "c_concern");
		select.put("field", "COUNT(*) as amount");
		select.put("where", "c_beconcerned_userid=" + userid);
		
		String amount = "0";
		try{
			amount = this.getDtMapper().selectValue(select);
		}catch(Exception e){
			e.printStackTrace();
			amount = "-1";
		}
		return Integer.parseInt(amount);
	}

	@Override
	public int getNewFollowersAmount(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		
		select.put("tables", "c_concern");
		select.put("field", "COUNT(*) as amount");
		select.put("where", "c_beconcerned_userid=" + userid + " and c_read=0");
		
		String amount = "0";
		try{
			amount = this.getDtMapper().selectValue(select);
		}catch(Exception e){
			e.printStackTrace();
			amount = "-1";
		}
		return Integer.parseInt(amount);
	}

	@Override
	public int getLikePostsAmount(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		
		select.put("tables", "pf_post_favor");
		select.put("field", "COUNT(*) as amount");
		select.put("where", "pf_like_userid=" + userid);
		
		String amount = "0";
		try{
			amount = this.getDtMapper().selectValue(select);
		}catch(Exception e){
			e.printStackTrace();
			amount = "-1";
		}
		return Integer.parseInt(amount);
	}

	@Override
	public int getLikeActAmount(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		
		select.put("tables", "af_act_favor");
		select.put("field", "COUNT(*) as amount");
		select.put("where", "af_like_userid=" + userid);
		
		String amount = "0";
		try{
			amount = this.getDtMapper().selectValue(select);
		}catch(Exception e){
			e.printStackTrace();
			amount = "-1";
		}
		return Integer.parseInt(amount);
	}

	@Override
	public int getLikePAAmount(int userid) {
		// TODO Auto-generated method stub
		int total = getLikePostsAmount(userid) + getLikeActAmount(userid);
		return total;
	}

	@Override
	public HashMap<String, Object> getMyNewFollowers(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> concernSearch = new HashMap<String,Object>();
		HashMap<String,Object> update = new HashMap<String,Object>();
		
		select.put("tables", "u_user,c_concern");
		select.put("fields", "u_id,u_nickname,u_gender,u_small_avatar,c_concern.*");
		select.put("where", "c_beconcerned_userid=" + userid + " and u_id=c_userid order by c_concern_time desc");
		
		concernSearch.put("tables", "c_concern");
		concernSearch.put("fields", "c_id");
		
		update.put("table", "c_concern");
		update.put("expression", "c_read=1");
		update.put("where", "c_beconcerned_userid=" + userid + " and c_read=0");
		
		DataModelMapper dtMapper = this.getDtMapper();
		
		try{
			List<JsonConcern> newFollowers = dtMapper.selectConcernList(select); 
			//同时检查是否关注追随者
			for(JsonConcern concern:newFollowers){
				concernSearch.put("where", "c_userid=" + userid + " and c_beconcerned_userid=" + concern.getUser_id());
				if(dtMapper.selectModels(concernSearch).size()==1)//关注过
					concern.setIs_concern(true);
				else
					concern.setIs_concern(false);
			}
			//设置未读的为已读
			dtMapper.updateModels(update);
			
			result.put(Info.DATA, newFollowers);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		return result;
	}
	//获取关注的用户的列表
	@Override
	public HashMap<String, Object> getQuanziUserListByIndustry(int userid,
			String industry_item) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> concernSearch = new HashMap<String,Object>();
				
		select.put("tables", "u_user,c_concern");
		select.put("fields", "u_id,u_nickname,u_gender,u_small_avatar,c_concern.*");
		select.put("where", "c_userid=" + userid + " and u_id=c_beconcerned_userid and u_industry_item like " + "'" + industry_item + "'" 
				+ " order by c_concern_time desc");
		
		concernSearch.put("tables", "c_concern");
		concernSearch.put("fields", "c_id");
		
		DataModelMapper dtMapper = this.getDtMapper();
		
		try{
			List<JsonConcern> myConcerners = dtMapper.selectConcernList(select); 
//			//同时检查是否关注追随者
//			for(JsonConcern concern:newFollowers){
//				concernSearch.put("where", "c_userid=" + userid + " and c_beconcerned_userid=" + concern.getUser_id());
//				if(dtMapper.selectModels(concernSearch).size()==1)//关注过
//					concern.setIs_concern(true);
//				else
//					concern.setIs_concern(false);
//			}
			result.put(Info.DATA, myConcerners);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> getFollowersListByIndustry(int userid,
			String industry_item) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> concernSearch = new HashMap<String,Object>();
						
		select.put("tables", "u_user,c_concern");
		select.put("fields", "u_id,u_nickname,u_gender,u_small_avatar,c_concern.*");
		select.put("where", "c_beconcerned_userid=" + userid + " and u_id=c_userid and u_industry_item like " + "'" + industry_item + "'" 
				+ " order by c_concern_time desc");
				
		concernSearch.put("tables", "c_concern");
		concernSearch.put("fields", "c_id");
				
		DataModelMapper dtMapper = this.getDtMapper();
				
		try{
			List<JsonConcern> newFollowers = dtMapper.selectConcernList(select); 
			//同时检查是否关注追随者
			for(JsonConcern concern:newFollowers){
				concernSearch.put("where", "c_userid=" + userid + " and c_beconcerned_userid=" + concern.getUser_id());
				if(dtMapper.selectModels(concernSearch).size()==1)//关注过
					concern.setIs_concern(true);
				else
					concern.setIs_concern(false);
			}
			result.put(Info.DATA, newFollowers);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> getIndustryList() {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		select.put("tables", "i_industry");
		select.put("fields", "*" );
		select.put("where", "1=1");
		
		List<String> nameList = new ArrayList<String>();
		try{
			List<HashMap<String,Object>> list = this.getDtMapper().selectModels(select);
			for(HashMap<String,Object> item : list){
				nameList.add(String.valueOf(item.get("i_name")));
			}
			result.put(Info.DATA, nameList);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> getSkillList() {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
				
		select.put("tables", "s_skill");
		select.put("fields", "*" );
		select.put("where", "1=1");
				
		List<String> nameList = new ArrayList<String>();
		try{
			List<HashMap<String,Object>> list = this.getDtMapper().selectModels(select);
			for(HashMap<String,Object> item : list){
				nameList.add(String.valueOf(item.get("s_name")));
			}
			result.put(Info.DATA, nameList);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> getInterestList() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		select.put("tables", "i_interest");
		select.put("fields", "*" );
		select.put("where", "1=1");
		
		List<String> nameList = new ArrayList<String>();
		try{
			List<HashMap<String,Object>> list = this.getDtMapper().selectModels(select);
			for(HashMap<String,Object> item : list){
				nameList.add(String.valueOf(item.get("i_name")));
			}
			result.put(Info.DATA, nameList);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> getBlackList(int userid) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
				
		select.put("tables", "bl_blacklist,u_user");
		select.put("fields", "u_user.*" );
		select.put("where", "bl_userid=" + userid + " and u_id=bl_shielderid");
				
		List<JsonConcern> blUserList = new ArrayList<JsonConcern>();
		try{
			blUserList = this.getDtMapper().selectConcernList(select);
			result.put(Info.DATA, blUserList);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> getSchoolIndustryUsers(int userid, String industry_item,
			int school_id) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				HashMap<String,Object> select = new HashMap<String,Object>();
				HashMap<String,Object> result = new HashMap<String,Object>();
				HashMap<String,Object> concernSearch = new HashMap<String,Object>();
								
				select.put("tables", "u_user");
				select.put("fields", "u_id,u_nickname,u_gender,u_small_avatar");
				select.put("where", "u_schoolid="+school_id+" and u_industry_item='"+industry_item+"'");
						
				concernSearch.put("tables", "c_concern");
				concernSearch.put("fields", "c_id");
						
				DataModelMapper dtMapper = this.getDtMapper();
						
				try{
					List<JsonConcern> users = dtMapper.selectConcernList(select); 
					//同时检查是否关注追随者
					for(JsonConcern concern:users){
						concernSearch.put("where", "c_userid=" + userid + " and c_beconcerned_userid=" + concern.getUser_id());
						if(dtMapper.selectModels(concernSearch).size()==1)//关注过
							concern.setIs_concern(true);
						else
							concern.setIs_concern(false);
					}
					result.put(Info.DATA, users);
				}catch(Exception e){
					e.printStackTrace();
					result.put(Info.DATA, -1);
				}
				return result;
	}

	@Override
	public HashMap<String, Object> findUserByTel(int userid,String tel) {
		// TODO Auto-generated method stub
		HashMap<String,Object> select = new HashMap<String,Object>();
		HashMap<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> concernSearch = new HashMap<String,Object>();
						
		select.put("tables", "u_user");
		select.put("fields", "u_id,u_nickname,u_gender,u_small_avatar");
		select.put("where", "u_tel='"+tel+"'");
				
		concernSearch.put("tables", "c_concern");
		concernSearch.put("fields", "c_id");
				
		DataModelMapper dtMapper = this.getDtMapper();
				
		try{
			List<JsonConcern> users = dtMapper.selectConcernList(select); 
			//同时检查是否关注追随者
			for(JsonConcern concern:users){
				concernSearch.put("where", "c_userid=" + userid + " and c_beconcerned_userid=" + concern.getUser_id());
				if(dtMapper.selectModels(concernSearch).size()==1)//关注过
					concern.setIs_concern(true);
				else
					concern.setIs_concern(false);
			}
			result.put(Info.DATA, users);
		}catch(Exception e){
			e.printStackTrace();
			result.put(Info.DATA, -1);
		}
		return result;
	}
}
