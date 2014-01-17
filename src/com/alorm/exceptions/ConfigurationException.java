package com.alorm.exceptions;

public class ConfigurationException extends AlormException{


	public ConfigurationException(String cause){
		super(cause);
	}
	
	public ConfigurationException(Throwable e,String cause){
		super(e, cause);
	}
	
	public ConfigurationException(Throwable e){
		super(e);
	}

}
