package com.alorm.core.sqlite.session.utils;

import android.content.ContentValues;

public class InsertQuery {
	
	private String table;
	private ContentValues cv;
	public InsertQuery(String table, ContentValues cv) {
		super();
		this.table = table;
		this.cv = cv;
	}
	public String getTable() {
		return table;
	}
	public ContentValues getCv() {
		return cv;
	}

}
