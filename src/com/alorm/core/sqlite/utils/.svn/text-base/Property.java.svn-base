package com.alorm.core.sqlite.utils;

import java.lang.reflect.Field;

import com.alorm.core.sqlite.utils.sqlite.SQLiteUtils;

public class Property {

	private String name;
	private Class type;
	private String column;
	private String column_type;
	
	//Constraints
	private boolean primarykey;
	private boolean autoincrement;
	private boolean notnull;
	private boolean unique;
	private String default_value;

	public Property(Field f){
		name=column=f.getName();
		type=f.getType();
		column_type=SQLiteUtils.SQLiteType.get(f.getType().getName());
	}

	public String generateSQL() {
		String sql = column+" "+column_type;
		if (isPrimarykey()) sql += " primary key";
		if(!isPrimarykey()){
			if(isUnique()) sql+=" UNIQUE";
			else if( !isAutoincrement() && isNotnull()) sql+=" not null";
			else if(default_value!=null) sql+=" default "+default_value;
		}
		return sql;
	}

	
	//Getters & Setters
	
	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public boolean isPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(boolean primarykey) {
		this.primarykey = primarykey;
	}

	public boolean isAutoincrement() {
		return autoincrement;
	}

	public void setAutoincrement(boolean autoincrement) {
		this.autoincrement = autoincrement;
	}

	public String getColumn_type() {
		return column_type;
	}
	
	public String getName() {
		return name;
	}

	public Class getType() {
		return type;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public void setNotnull(boolean notnull) {
		this.notnull = notnull;
	}

	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}

	public boolean isNotnull() {
		return notnull;
	}

	public boolean isUnique() {
		return unique;
	}
	
}
