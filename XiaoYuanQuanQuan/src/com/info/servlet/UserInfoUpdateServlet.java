package com.info.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.info.basic.DataModelMapper;
import com.info.sys.FastJsonTool;
import com.info.sys.Info;
import com.info.table.UserTable;
import com.info.user.UserServiceImpl;

@WebServlet(name="updateUserInfoServlet",urlPatterns="/user/infoUpdate")
public class UserInfoUpdateServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ApplicationContext ct;
	private String[] configFiles = { "/config/ApplicationContext.xml" };
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
//		//this.doPost(request, response);
//		request.setCharacterEncoding("UTF-8");
//		response.setCharacterEncoding("UTF-8");
//		response.setContentType("text/html;charset=UTF-8");
//		
//		PrintWriter out = response.getWriter();
//		out.println("test");
	} 
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);

		ct = new ClassPathXmlApplicationContext(configFiles);
		
		DataModelMapper dtMapper = (DataModelMapper) ct.getBean("DataModelMapper");
		UserServiceImpl uImpl = new UserServiceImpl(dtMapper);
		
		String name = "";
		HashMap<String,Object> result = new HashMap<String,Object>();
		//根据参数来调取不同的修改函数
		Enumeration<String> parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()){
			name = parameterNames.nextElement().toString();
			if(name.equals(UserTable.U_ID))
				continue;
			else
				break;
		}
		int userid = Integer.parseInt(request.getParameter(UserTable.U_ID));
		if(name.equals(UserTable.U_NICKNAME)){
			String nickName = request.getParameter(name);
			result = uImpl.updateNickName(userid, nickName);
		}
		else if(name.equals(UserTable.U_GENDER)){
			String sex = request.getParameter(name);
			result = uImpl.updateSex(userid, sex);
		}
		else if(name.equals(UserTable.U_BIRTHDAY)){
			String birthday = request.getParameter(name);
//			Date times = null;
//			try {
//				times = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			//
//			//System.out.println(new Date().toString());
//			birthday = new SimpleDateFormat("yyyy-MM-dd").format(times);
			try {
				result = uImpl.updateBithday(userid, birthday);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(name.equals(UserTable.U_SCHOOLID)){
			int schoolid = Integer.parseInt(request.getParameter(name));
			result = uImpl.updateSchool(userid, schoolid);
		}
		else if(name.equals(UserTable.U_LOVE_STATE)){
			String status = request.getParameter(name);
			result = uImpl.updateLoveStatus(userid, status);
		}
		else if(name.equals(UserTable.U_IDENTITY)){
			String identity = request.getParameter(name);
			result = uImpl.updateIdentity(userid, identity);
		}
		else if(name.equals(UserTable.U_INDUSTRY_ITEM)){
			String industry_item = request.getParameter(name);
			result = uImpl.updateIndustry(userid, industry_item);
		}
		else if(name.equals(UserTable.U_SKILL_ITEMS)){
			String skill_items = request.getParameter(name);
			result = uImpl.updateSkill(userid, skill_items);
		}
		else if(name.equals(UserTable.U_INTEREST_ITEMS)){
			String interest_items = request.getParameter(name);
			result = uImpl.updateInterest(userid, interest_items);
		}
		else if(name.equals(UserTable.U_INTRODUCE)){
			String introduction = request.getParameter(name);
			result = uImpl.updateIntroduce(userid, introduction);
		}else if(name.equals(UserTable.U_PASSWORD)){
			//String pwd = request.getParameter(name);
			//result = uImpl.updatePassword(userid, pwd);
		}
	 	out.print(FastJsonTool.createJsonString(result.get(Info.DATA)));
	}
}
