package com.alorm.api;

import java.util.List;

import android.database.Cursor;

import com.android.internal.util.Predicate;

/**
 * Represents the Session
 * 
 * @author Karami,Laraki,Sadik,Abbadi,Zaelouk
 * 
 */
public interface Session {

	/**
	 * Open Session
	 */
	public void open();
	/**
	 * Close Session
	 */
	public void close();
	/**
	 * Begin a transaction
	 */
	public void beginTransaction();
	/**
	 * Commit changes
	 */
	public void commit();
	/**
	 * Rollback
	 */
	public void rollBack();
	/**
	 * Save Object in DataBase
	 * @param obj
	 */
	public void save(Object obj);
	/**
	 * Find Object Entity in Database
	 * @param cls Class of the Object
	 * @param pk Primary Key Parameter
	 * @return Object ,null if not exist
	 */
	public <U,T> U findByPK(Class<U> cls,T pk);
	/**
	 * Delete Object in Database
	 * @param cls Class of the Object
	 * @param pk Primary key Parameter
	 */
	public <U, T> void deletebyPK(Class<U> cls, T pk);
	/**
	 * Update Object
	 * @param obj
	 */
	public <U> void update(U obj);
	/**
	 * Check if a Record exist in database
	 * @param cls Class of the Object
	 * @param pk Primary key Parameter
	 * @return
	 */
	public <U, T> boolean exist(Class<U> cls, T pk);
	/**
	 * Get All Object Records
	 * @param cls Class of the Object
	 * @param args where clause parameters
	 * @return
	 */
	public <U, T> List<U> getAll(Class<U> cls, T... args);
	/**
	 * Get All Object Records
	 * @param cls Class of the Object
	 * @param where Predicate Parameter for Condition Clause
	 * @return
	 */
	public <U> List<U> getAll(Class<U> cls,Predicate<U> where);
	/**
	 * Execute SQL Query Like INSERT,UPDATE,...
	 * @param sql Query String
	 */
	public void execSql(String sql);
	/**
	 * Execute Select Sql Query 
	 * @param sql Query String
	 * @param args Query Parameters
	 * @return Android Database Cursor
	 */
	public Cursor querySql(String sql,String[]args);
	/**
	 * Delete All Object Records
	 * @param cls Class of the Object
	 */
	public <U> void deleteAll(Class<U> cls);
	/**
	 * Delete All Object Records
	 * @param cls Class of the Object
	 * @param where Predicate Parameter for Condition Clause
	 * @return
	 */
	public <U> void deleteAll(Class<U> cls,Predicate<U> where);
}
