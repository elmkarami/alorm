package com.alorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Attach this Class to ALORM Mapping Configuration 
 * @author Laraki,Karami,Sadik,Abbadi,Zaelouk
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
	/**
	 * Set Table Name, default value is Class Name
	 * @return
	 */
	String table() default "";
}
