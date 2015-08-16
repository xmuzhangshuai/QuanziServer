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
import com.info.model.CommentModel;
import com.info.post.PostServiceImpl;
import com.info.sys.FastJsonTool;
import com.info.sys.Function;
import com.info.table.CommentTable;
import com.info.table.PostCommentTable;
import com.info.table.UserTable;

@WebServlet(name="postCommentReplyServlet",urlPatterns="/post/comment_reply")
public class PostCommentReplyServlet extends HttpServlet{
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
		
		CommentModel comment = new CommentModel();
		//comment = FastJsonTool.getObject(request.getParameter("comment"), CommentModel.class);
		int postid = Integer.parseInt(request.getParameter(CommentTable.PA_ID));
		int userid = Integer.parseInt(request.getParameter(CommentTable.PA_USERID));
		String type = request.getParameter(CommentTable.COMMENT_TYPE);
		String content = Function.mysql_validate_string(request.getParameter(CommentTable.C_CONTENT));
		int comment_userid = Integer.parseInt(request.getParameter(CommentTable.C_USER_ID));
		int to_userid = Integer.parseInt(request.getParameter(CommentTable.TO_USER_ID));
		comment.setPc_postid(postid);
		comment.setPc_userid(userid);
		comment.setPc_type("'"+type+"'");
		comment.setPc_content("'"+content+"'");
		comment.setPc_comment_userid(comment_userid);
		comment.setPc_to_userid(to_userid);
		
		HashMap<String,Object> result = pImpl.commentReplyPosts(comment);
		
	 	//String	retData = JSONObject.fromObject(result).toString();
	 	
	 	out.print(FastJsonTool.createJsonString(result));
	}
}
