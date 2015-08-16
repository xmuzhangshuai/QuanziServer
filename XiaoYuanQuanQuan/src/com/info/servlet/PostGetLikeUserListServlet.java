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
import com.info.post.PostServiceImpl;
import com.info.sys.FastJsonTool;
import com.info.sys.Info;
import com.info.table.PostTable;
import com.info.table.UserTable;

@WebServlet(name="getLikeUserList",urlPatterns="/post/getLikeUserList")
public class PostGetLikeUserListServlet extends HttpServlet{
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
		
		//int pageNow = Integer.parseInt(request.getParameter("page"));
		int post_id = Integer.parseInt(request.getParameter(PostTable.P_POSTID));
		int user_id = Integer.parseInt(request.getParameter(PostTable.P_USERID));
		//if(pageNow < 0){
		//	out.print(-1);
		//}else{
			HashMap<String,Object> result = pImpl.getLikeUserList(post_id,user_id/*,pageNow*/);		 	
		 	out.print(FastJsonTool.createJsonString(result.get(Info.DATA)));
		//}
		
	}
}
