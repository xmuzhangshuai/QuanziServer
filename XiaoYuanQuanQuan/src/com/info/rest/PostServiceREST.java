package com.info.rest;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;





import com.info.basic.DataModelMapper;
import com.info.post.PostServiceImpl;

@Path("/PostService")
public class PostServiceREST {
	private ApplicationContext ct;
	private String[] configFiles = { "/config/ApplicationContext.xml" };
	
	@GET
	@Path("/queryPostByConditions")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getPosts(String conditions, @Context HttpServletRequest req){
		String retData;
		ct = new ClassPathXmlApplicationContext(configFiles);
		DataModelMapper dtMapper = (DataModelMapper) ct.getBean("DataModelMapper");
		
		PostServiceImpl pImpl = new PostServiceImpl(dtMapper);
		List<HashMap<String,Object>> result = pImpl.getPosts(conditions);
		
		retData = JSONObject.fromObject(result).toString();
		return retData;
	}
}
