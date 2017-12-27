package com.ge.predix.predixinsights.eventhub;

import java.io.Serializable;

public class SerializeObject implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	public SerializeObject(String data){
		super();
		this.data = data;
	}
	
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	} 
	

}