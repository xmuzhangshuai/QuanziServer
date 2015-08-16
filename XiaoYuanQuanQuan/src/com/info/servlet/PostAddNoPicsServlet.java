package com.info.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

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
import com.info.sys.Function;
import com.info.sys.Info;
import com.info.table.PostTable;

@WebServlet(name="postAddNopicsServlet",urlPatterns="/post/add_no_pic")
public class PostAddNoPicsServlet extends HttpServlet{
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
		
		String big_img_path_list = "";
		String small_img_path_list = "";
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		
		int userid = Integer.parseInt(request.getParameter(PostTable.P_USERID));
		String content = Function.mysql_validate_string(request.getParameter(PostTable.P_CONTENT)).trim();
		if(content.equals("")){
			result.put(Info.DATA,-2);//内容为空
		}
		else
			result = pImpl.addPost(big_img_path_list,small_img_path_list,userid,content);
		
	 	//String	retData = JSONObject.fromObject(result).toString();
	 	
	 	out.print(FastJsonTool.createJsonString(result.get(Info.DATA)));
	}
}
