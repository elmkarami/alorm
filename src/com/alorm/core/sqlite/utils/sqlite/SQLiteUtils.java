package com.alorm.core.sqlite.utils.sqlite;


import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class SQLiteUtils {

	public static final Map<String, String> SQLiteType = new HashMap<String, String>() {
		{
			put(String.class.getName(), "varchar(200)");
			put(Long.class.getName(), "Integer");
			put(Integer.class.getName(), "Integer");
			put(int.class.getName(), "Integer");
			put(long.class.getName(), "Integer");
			put(Float.class.getName(), "REAL(7,7)");
			put(float.class.getName(), "REAL(7,7)");
			put(Short.class.getName(), "Integer");
			put(short.class.getName(), "Integer");
			put(Boolean.class.getName(), "bit");
			put(boolean.class.getName(), "bit");
			put(Date.class.getName(),"datetime");
		}
	};
	
}
