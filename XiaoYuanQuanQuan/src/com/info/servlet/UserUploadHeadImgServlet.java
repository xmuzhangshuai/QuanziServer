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

import org.lxh.smart.File;
import org.lxh.smart.Files;
import org.lxh.smart.SmartUpload;
import org.lxh.smart.SmartUploadException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.info.basic.DataModelMapper;
import com.info.sys.FastJsonTool;
import com.info.sys.Info;
import com.info.sys.NarrowImage;
import com.info.table.UserTable;
import com.info.user.UserServiceImpl;

@WebServlet(name="uploadHeadImgServlet",urlPatterns="/user/uploadHeadImg")
public class UserUploadHeadImgServlet extends HttpServlet{
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
		//存储在服务器物理磁盘上的图片路径
		String big_img_path = Info.HEAD_BIG_IMG_FOLDER;
		String small_img_path = Info.HEAD_SMALL_IMG_FOLDER;
		String virtual_big_path = Info.VIRTUAL_HEAD_IMG_BIG_FOLDER;
		String virtual_small_path = Info.VIRTUAL_HEAD_IMG_SMALL_FOLDER;
		//存储在数据库中的大小头像的路劲(虚拟路径+图片名称)
		String bigImgPath = "";
		String smallImgPath = "";
		
		int userid = 0;
		
		float resizeTimes = 0.5f;

		java.io.File fpath = new java.io.File(big_img_path);
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
			
			//n_content = new String(su.getRequest().getParameter(PostTable.P_CONTENT));
			userid = Integer.parseInt(su.getRequest().getParameter(UserTable.U_ID));
							
			Files files = su.getFiles();
			for(int i=0;i<ss;i++){
				File image_file = files.getFile(i);
				if (image_file.isMissing()) {
					out.println("missing");
					return;
				}
				String l_fname = new Date().getTime() + (new Random().nextInt(900) + 100) + userid + "."
						+ image_file.getFileExt();

				image_file.saveAs(big_img_path +  l_fname);
				
				NarrowImage.narrowImage(big_img_path +  l_fname, resizeTimes, small_img_path + "small_" + l_fname);
				
				bigImgPath = virtual_big_path + l_fname;
				smallImgPath = virtual_small_path + "small_" + l_fname;
			}												
			
	}catch(SmartUploadException e){
		e.printStackTrace();
	}
		
		HashMap<String,Object> result = uImpl.uploadHeadImg(bigImgPath, smallImgPath, userid);		 	
	 	out.print(FastJsonTool.createJsonString(result.get(Info.DATA)));
	}
}
