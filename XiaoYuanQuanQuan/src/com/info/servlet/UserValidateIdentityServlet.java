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
import com.info.huanxin.HuanXin;
import com.info.model.UserModel;
import com.info.sys.FastJsonTool;
import com.info.sys.Info;
import com.info.table.UserTable;
import com.info.user.UserServiceImpl;

@WebServlet(name="userValidateServlet",urlPatterns="/user/validateID")
public class UserValidateIdentityServlet extends HttpServlet{
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
		
		UserServiceImpl uImpl = new UserServiceImpl(dtMapper);
		
		String xuehao = request.getParameter("student_number");
		String pwd = request.getParameter("student_pwd");
		
//获取学校代码
		String school_id = request.getParameter(UserTable.U_SCHOOLID);
		
		HashMap<String,Object> result = uImpl.validatStudentID(xuehao, pwd, school_id);

					
	 	out.print(FastJsonTool.createJsonString(result.get(Info.DATA)));
	}
}
