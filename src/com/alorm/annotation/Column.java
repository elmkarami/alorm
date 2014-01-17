package com.alorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Column Specifications
 * @author Laraki,Karami,Sadik,Abbadi,Zaelouk
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	
	//For Properties
	/**
	 * Name of Column (Must Enter a Valid conventional Name) 
	 * @return
	 */
	String name() default "";
	/**
	 * Set True if this Attribute is a Primary Key, default value is false
	 * @return
	 */
	boolean primarykey() default false;
	/**
	 * Set True if this Attribute is Auto-Increment, default value is false
	 * @return
	 */
	boolean autoincrement() default false;
	/**
	 * Set True if this Attribute has NOTNULL Constraint, default value is false
	 * @return
	 */
	boolean notnull() default false;
	/**
	 * Set True if this Attribute has UNIQUE Constraint, default value is false
	 * @return
	 */
	boolean unique() default false;
	
	//For Dependencies
	/**
	 * Set Attribute Name reference of Related Class in case of Bidirectionnel Relation
	 * @return
	 */
	String attribute_ref() default "";
	/**
	 * Set True if Cascade is enabled, default value is false
	 * @return
	 */
	boolean oncascade() default false;
	/**
	 * Set False if Lazy is disabled, default value is true
	 * @return
	 */
	boolean lazy() default true;
	
	/**
	 * Set Default value
	 * @return
	 */
	String default_value() default "";

}
