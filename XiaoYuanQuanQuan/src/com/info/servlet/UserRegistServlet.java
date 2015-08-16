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
import com.info.user.UserServiceImpl;

@WebServlet(name="userRegistServlet",urlPatterns="/user/regist")
public class UserRegistServlet extends HttpServlet{
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
		
		UserModel user = FastJsonTool.getObject(request.getParameter("register"), UserModel.class);
		
		System.out.print(request.getParameter("register"));

		
		HashMap<String,Object> result = uImpl.regist(user);

		if(user.getU_id()>0){
			String rs = HuanXin.createUser(String.valueOf(user.getU_id()), user.getU_password());
			if(rs!=null){
				out.print(user.getU_id());
			}
		}else
			out.print(-1);	 			
	 	//out.print(FastJsonTool.createJsonString(result));
	}
}
