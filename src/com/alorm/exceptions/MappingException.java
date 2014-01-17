package com.alorm.exceptions;

public class MappingException extends AlormException{

	public MappingException(String cause){
		super(cause);
	}
	
	public MappingException(Throwable e,String cause){
		super(e, cause);
	}
	
	public MappingException(Throwable e){
		super(e);
	}

}
