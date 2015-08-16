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

import org.lxh.smart.File;
import org.lxh.smart.SmartUpload;
import org.springframework.context.ApplicationContext;

import com.info.sys.Info;
import com.info.table.PostTable;

@WebServlet(name="addtestPostServlet",urlPatterns="/post/addtest")
public class PostAddTestServlet extends HttpServlet{
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
		response.setContentType("text/html;Charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		
		
		
		String path = Info.POST_IMG_BIG_FOLDER;
		String virtual_path = Info.VIRTUAL_POST_IMG_BIG_FOLDER;
		String big_img_path_list = "";
		
		System.out.println("path="+path);
		java.io.File fpath = new java.io.File(path);
		if (!fpath.exists()) {
			fpath.mkdirs();
		}

		SmartUpload su = new SmartUpload();
	
		su.initialize(this.getServletConfig(), request, response);


		su.setMaxFileSize(1 * 4096 * 4096); 
		su.setAllowedFilesList("gif,png,jpg,jpeg,JPEG"); 
		
		try {
			su.upload();
			int ss = su.getFiles().getCount();
			System.out.println(ss);
			
			int userid = Integer.parseInt(su.getRequest().getParameter(PostTable.P_USERID));
			String n_content = new String(su.getRequest().getParameter(PostTable.P_CONTENT).getBytes("GBK"),"utf-8");
			
			for(int i=0;i<ss;i++){
				File image_file = su.getFiles().getFile(0);
				if (image_file.isMissing()) {
					out.println("missing");
					return;
				}
				String l_fname = new Date().getTime() + (new Random().nextInt(900) + 100) + userid + "."
						+ image_file.getFileExt();

				image_file.saveAs(path +  l_fname);
				big_img_path_list = (path + l_fname + "|");
			}												
			out.println("OK!");			
			
	}catch(Exception e){
		System.out.println(e.getMessage());
	}
		
}

}
