package com.alorm.core.sqlite.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class Dependency {

	private MappingClass mappingClass;
	private Class type;
	private String name;
	private String column; //Column Name
	private boolean one; // Relation = T:one , F :many
	
	private String attribute_ref; //For biderectionnel relation
	private boolean cascade;
	private boolean lazy=true;
	
	public Dependency(MappingClass m,Field f){
		name=f.getName();
		if (f.getGenericType() instanceof ParameterizedType) {
			ParameterizedType p = (ParameterizedType) f.getGenericType();
			type = (Class<?>) p.getActualTypeArguments()[0];
			column="fk_"+AlormUtils.getEntityClass(m.getType().getName())+"_"+name;
			one=false;
		}else{
			type=f.getType();
			column=AlormUtils.getEntityClass(type.getName())+"_"+name;
			one=true;
		}
	}

	
	//getters & setters
	public MappingClass getMappingClass() {
		return mappingClass;
	}

	public Class getType() {
		return type;
	}

	public boolean isOne() {
		return one;
	}

	public String getColumn() {
		return column;
	}

	public String getAttribute_ref() {
		return attribute_ref;
	}

	public void setAttribute_ref(String attribute_ref) {
		this.attribute_ref = attribute_ref;
	}

	public String getName() {
		return name;
	}

	public void setColumn(String column) {
		this.column = column;
	}
	
	public void setCascade(boolean cascade) {
		this.cascade = cascade;
	}

	public void setMappingClass(MappingClass mappingClass) {
		this.mappingClass = mappingClass;
	}

	public boolean isCascade() {
		return cascade;
	}

	public boolean isLazy() {
		return lazy;
	}

	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}
	
}
