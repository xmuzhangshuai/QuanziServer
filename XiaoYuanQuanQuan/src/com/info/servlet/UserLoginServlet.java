package com.info.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.info.json.JsonUser;
import com.info.quanzi.QuanziServiceImpl;
import com.info.sys.FastJsonTool;
import com.info.sys.Info;
import com.info.table.UserTable;
import com.info.user.UserServiceImpl;


@WebServlet(name="loginServlet",urlPatterns="/user/login")
public class UserLoginServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ApplicationContext ct;
	private String[] configFiles = { "/config/ApplicationContext.xml" };
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		//this.doPost(request, response);
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		out.println("test");
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
		QuanziServiceImpl qImpl = new QuanziServiceImpl(dtMapper);

		HashMap<String,Object> result = uImpl.login(request.getParameter(UserTable.U_TEL),
				request.getParameter(UserTable.U_PASSWORD));

		
	 	if(result.get(Info.STATUS).toString().equals(Info.SUCCEED)){
	 		session.setAttribute("tel", request.getParameter(UserTable.U_TEL));
	 		JsonUser user = (JsonUser) result.get(Info.DATA);
			int newFollower = qImpl.getNewFollowersAmount(user.getU_id());
			int likePostsAmount = qImpl.getLikePostsAmount(user.getU_id());
			user.setU_new_follower_count(newFollower);
			user.setU_my_favor_count(likePostsAmount);
			result.put(Info.DATA, user);
	 	}
		
	 	out.print(FastJsonTool.createJsonString(result.get(Info.DATA)));
	}
}
