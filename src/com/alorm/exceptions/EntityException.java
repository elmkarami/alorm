package com.alorm.exceptions;

public class EntityException extends AlormException{

	public EntityException(String cause){
		super(cause);
	}
	
	public EntityException(Throwable e,String cause){
		super(e, cause);
	}
	
	public EntityException(Throwable e){
		super(e);
	}
}
