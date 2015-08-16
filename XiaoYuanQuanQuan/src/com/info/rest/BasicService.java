package com.info.rest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BasicService {
	private ApplicationContext ct;
	//private String[] configFiles;
	private static String[] configFiles = { "/config/ApplicationContext.xml" };
	public BasicService(){
		this.ct = new ClassPathXmlApplicationContext(configFiles);
	}
	public ApplicationContext getCt() {
		return ct;
	}
	public void setCt(ApplicationContext ct) {
		this.ct = ct;
	}
	/*public String[] getConfigFiles() {
		return configFiles;
	}
	public void setConfigFiles(String[] configFiles) {
		this.configFiles = configFiles;
	}*/
	
}
