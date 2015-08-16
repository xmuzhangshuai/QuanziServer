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

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.info.basic.DataModelMapper;
import com.info.post.PostServiceImpl;
import com.info.sys.FastJsonTool;
import com.info.sys.Info;
import com.info.table.PostFavorTable;
import com.info.table.PostTable;
import com.info.table.UserTable;

@WebServlet(name="postLikeServlet",urlPatterns="/post/like")
public class PostLikeServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ApplicationContext ct;
	private String[] configFiles = { "/config/ApplicationContext.xml" };
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		//this.doPost(request, response);
	} 
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		
		ct = new ClassPathXmlApplicationContext(configFiles);
		DataModelMapper dtMapper = (DataModelMapper) ct.getBean("DataModelMapper");
		
		PostServiceImpl pImpl = new PostServiceImpl(dtMapper);
		
		int post_id = Integer.parseInt(request.getParameter(PostTable.P_POSTID));
		int user_id = Integer.parseInt(request.getParameter(PostTable.P_USERID));
		int like_user_id = Integer.parseInt(request.getParameter(UserTable.U_ID));

		
		HashMap<String,Object> result = pImpl.like(post_id,user_id,like_user_id);
		
	 //	String	retData = JSONObject.fromObject(result).toString();
	 	
	 	out.print(FastJsonTool.createJsonString(result.get(Info.DATA)));
	}
}
