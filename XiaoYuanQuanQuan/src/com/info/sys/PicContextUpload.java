package com.info.sys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class PicContextUpload {

	public static void picContextUpload(HttpServletRequest request, HttpServletResponse response, String upLoadPath) throws IOException{
		//获得磁盘文件条目工厂  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //获取文件需要上传到的路径  
        @SuppressWarnings("deprecation")
		String path = request.getRealPath(upLoadPath);  
        System.out.println(path);
        File file=new File(path);
        if(!file.exists()){
        	file.mkdirs();
        }
        factory.setRepository(new File(path));  
        //设置 缓存的大小
        factory.setSizeThreshold(1024*1024) ;  
        //文件上传处理  
        ServletFileUpload upload = new ServletFileUpload(factory);
        //记录多个大图图片的路劲
        List<String> bigPicList = new ArrayList<String>();
        //记录小图图片的路劲
        List<String> thumbPicList = new ArrayList<String>();
        try {  
            //可以上传多个文件  
            List<FileItem> list = (List<FileItem>)upload.parseRequest(request);  
            for(FileItem item : list){  
                //获取属性名字  
                String name = item.getFieldName();  
                //如果获取的 表单信息是普通的 文本 信息  
                if(item.isFormField()){                     
                    //获取用户具体输入的字符串,因为表单提交过来的是 字符串类型的  
                    String value = item.getString() ;  
                    request.setAttribute(name, value);  
                }else{  
                    //获取路径名  
                    String value = item.getName() ;  
                    //索引到最后一个反斜杠  
                    int start = value.lastIndexOf("\\");  
                    //截取 上传文件的 字符串名字，加1是 去掉反斜杠，  
                    String filename = value.substring(start+1); 
                    bigPicList.add(filename);                     
                    //写到磁盘上  
                    item.write( new File(path,filename) );//第三方提供的  
                    System.out.println("上传成功："+filename);
                    //response.getWriter().print(filename);//将路径返回给客户端
                }  
            }  
            request.setAttribute("bigPicList", bigPicList);
            //图片处理，生成小图片并返回小图片的url
            thumbPicList = BigPicToSmall.bigPhotoToSmall(bigPicList);
            request.setAttribute("thumPicList", thumbPicList);             
        } catch (Exception e) {  
        	System.out.println("上传失败");
        	response.getWriter().print("上传失败："+e.getMessage());
        }  
	}
}
