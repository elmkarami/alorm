package com.alorm.core.sqlite.app;

import android.content.Context;


import android.database.Cursor;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alorm.core.sqlite.utils.AssocClass;
import com.alorm.core.sqlite.utils.Dependency;
import com.alorm.core.sqlite.utils.MappingClass;
import com.alorm.exceptions.ConfigurationException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AlormConfiguration {

	private SQLiteDatabase db;
	private String startaction;
	
	private List<MappingClass> liste_mapping;
	private List<AssocClass> liste_assoc=new ArrayList<AssocClass>();
	
	public AlormConfiguration(Context c,SQLiteDatabase pdb,String pstartaction){
			liste_mapping=ConfigurationReader.getMappingconfiguration(c);
			db=pdb;
			startaction=pstartaction;
	}
	
	
	/**
	 * Creating DB schema
	 */
	public void CreateDBschema(){
		try {
			if(startaction.equals("create")){
				DropDBTables();
				createSQLiteRequest(); //Create Tables
			}
			updateDependencySQliteRequest(); //Update Dependencies
		} catch (SQLException e) {
			throw new ConfigurationException(e,"Error in DataBase Schema Creation");
		}
	}
	
	/**
	 * Generates the SQLite request to create tables
	 * @return
	 */
	private void createSQLiteRequest() {

		for (MappingClass m : liste_mapping) {
			String sql=m.generateSQLiteRequest();
			db.execSQL(sql);
			Log.d("SQLQuery", sql);
		}
	}
	
	/**
	 * Generates the SQLite request to complete DB schema with dependencies
	 * @return SQliterequest
	 * @throws SQLException 
	 */
	private void updateDependencySQliteRequest() throws SQLException{

		for(MappingClass m:liste_mapping){

			for(Dependency p:m.getDependencies()){
				String incascade=" ON DELETE CASCADE";
				if(!p.isCascade()) incascade="";
				if(p.isOne()){
					if(!column_exist(m.getTable(), p.getColumn())){
						String sql="alter table "+m.getTable()+" add column "+p.getColumn()+" "+p.getMappingClass().getID().getColumn_type()+" NULL REFERENCES "+p.getMappingClass().getTable()+"("+p.getMappingClass().getID().getColumn()+")"+incascade+";";
						db.execSQL(sql);
						Log.d("SQLQuery", sql);
					}
				}
				else{
						if(p.getMappingClass().checkListdependency(m.getType())){
							if(!assoc_exist(m)){
								String incascade2="";
								if(p.getMappingClass().getListdependency(m.getType()).isCascade()) incascade2=" ON DELETE CASCADE";
								AssocClass ac=new AssocClass(m.getTable()+"_"+p.getMappingClass().getTable(), m, p.getMappingClass());
								liste_assoc.add(ac);
								String sql=ac.generateSqlRequest(incascade,incascade2);
								db.execSQL(sql);
								Log.d("SQLQuery", sql);
							}
						}else{
							if(!column_exist(p.getMappingClass().getTable(), p.getColumn())){
								String sql="alter table "+p.getMappingClass().getTable()+" add column "+p.getColumn()+" "+m.getID().getColumn_type()+" NULL REFERENCES "+m.getTable()+"("+m.getID().getColumn()+")"+incascade+";";
								db.execSQL(sql);
								Log.d("SQLQuery", sql);
							}
						}	
					}
				}	
		}
	}
	
	/**
	 * True if the AssociationClass already exist, false if not
	 * @param mappingClass
	 * @return
	 */
	private boolean assoc_exist(MappingClass mappingClass) {
		for(AssocClass a:liste_assoc){
			if(a.getM1()==mappingClass || a.getM2()==mappingClass) return true;
		}
		return false;
	}

	/**
	 * True if Column Name exist in the Table
	 * @param table
	 * @param column
	 * @return
	 * @throws SQLException
	 */
	private boolean column_exist(String table,String column) throws SQLException{
	    Cursor ti = db.rawQuery("PRAGMA table_info("+table+")", null);
	    if ( ti.moveToFirst() ) {
	        do {
	        	if(ti.getString(1).equals(column)) return true;
	        } while (ti.moveToNext());
	    }
		return false;
	}
	
	/**
	 * Drop tables (Case for Create)
	 * @throws SQLException
	 */
	private void DropDBTables() throws SQLException{
		Cursor ti = db.rawQuery("select name from sqlite_master where type='table' ;", null);
		List<String> tabs=new ArrayList<String>();
	    if ( ti.moveToFirst() ) {
	        do {
	        	if(!ti.getString(0).equals("sqlite_sequence")) tabs.add(ti.getString(0));
	        } while (ti.moveToNext());
	    }
	    
		for(String s:tabs){
			db.execSQL("drop table "+s+" ;");		
		}
	}

	//getters & setters
	public List<MappingClass> getListe_mapping() {
		return liste_mapping;
	}

	public List<AssocClass> getListe_assoc() {
		return liste_assoc;
	}

}
