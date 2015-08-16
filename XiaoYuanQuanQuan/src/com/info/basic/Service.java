package com.info.basic;

public class Service {
	private DataModelMapper dtMapper;
	public Service(DataModelMapper dtMapper){
		this.dtMapper = dtMapper;
	}
	public DataModelMapper getDtMapper() {
		return dtMapper;
	}
	public void setDtMapper(DataModelMapper dtMapper) {
		this.dtMapper = dtMapper;
	}
	
}
