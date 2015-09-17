package com.info.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.info.activity.ActivityServiceImpl;
import com.info.basic.DataModelMapper;
import com.info.sys.FastJsonTool;
import com.info.sys.Function;
import com.info.sys.Info;
import com.info.sys.NarrowImage;
import com.info.table.ActivityTable;

@WebServlet(name="activityAddServlet",urlPatterns="/activity/add")
public class ActivityAddServlet extends HttpServlet{
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
		
		String big_img_path = Info.ACT_IMG_BIG_FOLDER;
		String small_img_path = Info.ACT_IMG_SMALL_FOLDER;
		String virtual_big_path = Info.VIRTUAL_ACT_IMG_BIG_FOLDER;
		String virtual_small_path = Info.VIRTUAL_ACT_IMG_SMALL_FOLDER;
		
		String big_img_path_list = "";
		String small_img_path_list = "";
		
		
		int userid = 0;
		String n_content = null;
		String title = null;
		String type = null;
		String time = null;
		String address = null;
		String target = null;
		
		HashMap<String,Object> result = new HashMap<String,Object>();
		
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
			
			n_content = Function.mysql_validate_string(new String(su.getRequest().getParameter(ActivityTable.A_CONTENT).getBytes(),"utf-8")).trim();
			//处理特殊字符
			if(!n_content.equals("")){
				userid = Integer.parseInt(su.getRequest().getParameter(ActivityTable.A_USERID));
				title = Function.mysql_validate_string(new String(su.getRequest().getParameter(ActivityTable.A_ACT_TITLE).getBytes(),"utf-8"));
				type = new String(su.getRequest().getParameter(ActivityTable.A_ACT_TYPE).getBytes(),"utf-8");
				
				time = new String(su.getRequest().getParameter(ActivityTable.A_ACTTIME));
				System.out.println(time);
				Date times = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);
				//
				System.out.println(new Date().toString());
				time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(times);
				
				System.out.println(time);
				
				address = Function.mysql_validate_string(new String(su.getRequest().getParameter(ActivityTable.A_ADDRESS).getBytes(),"utf-8"));
				target = new String(su.getRequest().getParameter(ActivityTable.A_TARGET).getBytes(),"utf-8");
				
								
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
					
					big_img_path_list = big_img_path_list + (virtual_big_path + l_fname + "|");
					small_img_path_list = small_img_path_list + (virtual_small_path + "small_" + l_fname + "|");
					
				}												
				big_img_path_list = big_img_path_list.substring(0,big_img_path_list.length()-1);
				small_img_path_list = small_img_path_list.substring(0,small_img_path_list.length()-1);
				result = aImpl.addActivity(big_img_path_list,small_img_path_list,userid,n_content,title,type,time,address,target);		 	
			}
			else{
				result.put(Info.DATA, -2);//未输入内容
			}
			
	}catch(SmartUploadException | ParseException e){
		e.printStackTrace();
	}
		
		
	 	out.print(FastJsonTool.createJsonString(result.get(Info.DATA)));
	}
}
