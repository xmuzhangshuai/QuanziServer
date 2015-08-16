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

import com.info.activity.ActivityServiceImpl;
import com.info.basic.DataModelMapper;
import com.info.post.PostServiceImpl;
import com.info.sys.FastJsonTool;
import com.info.sys.Info;
import com.info.table.ActivityTable;
import com.info.table.UserTable;

@WebServlet(name="getSchoolActivitiesServlet",urlPatterns="/activity/getSchoolActivities")
public class ActivityGetSchoolActivities extends HttpServlet{
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
		
		ActivityServiceImpl aImpl = new ActivityServiceImpl(dtMapper);
		
		int pageNow = Integer.parseInt(request.getParameter("page"));
		int school_id = Integer.parseInt(request.getParameter(UserTable.U_SCHOOLID));
		int userid = Integer.parseInt(request.getParameter(UserTable.U_ID));
		String actType = request.getParameter(ActivityTable.A_ACT_TYPE);
		
		if(pageNow<0){
			out.print(0);
		}else{
			HashMap<String,Object> result = aImpl.getSchoolActivities(school_id, pageNow, userid, actType);
		 	out.print(FastJsonTool.createJsonString(result.get(Info.DATA)));
		}	
		
	}
}
