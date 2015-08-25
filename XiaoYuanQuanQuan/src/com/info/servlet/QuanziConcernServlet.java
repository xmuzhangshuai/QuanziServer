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
import com.info.quanzi.QuanziServiceImpl;
import com.info.sys.FastJsonTool;
import com.info.sys.Info;
import com.info.table.QuanziTable;

@WebServlet(name="quanZiConcernServlet",urlPatterns="/quanzi/concern")
public class QuanziConcernServlet extends HttpServlet{
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
		
		QuanziServiceImpl qImpl = new QuanziServiceImpl(dtMapper);
		
		int c_userid = Integer.parseInt(request.getParameter(QuanziTable.C_USERID));
		int c_beconcerned_userid = Integer.parseInt(request.getParameter(QuanziTable.C_BECONCERNED_USERID));
		if(c_userid==c_beconcerned_userid){
			out.print(FastJsonTool.createJsonString(-3));//不能自己关注自己
		}else{
			HashMap<String,Object> result = qImpl.concern(c_userid, c_beconcerned_userid);
			
		 	//String	retData = JSONObject.fromObject(result).toString();
		 	
		 	out.print(FastJsonTool.createJsonString(result.get(Info.DATA)));
		}
		
	}
}
