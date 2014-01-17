package com.alorm.api;

import java.util.List;

import com.android.internal.util.Predicate;

import android.content.Context;
import android.database.Cursor;

public class AlormTemplate {

	private static Session session;
	private static Context ctx;
	private static AlormTemplate template;

	private AlormTemplate(Context ctx) {
		this.ctx = ctx;
	}

	/**
	 * Get AlormTemplate for Quick Database Operation
	 * 
	 * @param ctx
	 *            Context of the Current Activity
	 * @return
	 */
	public static AlormTemplate getAlormTemplate(Context ctx) {
		if (template == null)
			template = new AlormTemplate(ctx);
		return template;
	}

	/**
	 * Save Object in DataBase
	 * 
	 * @param obj
	 */
	public void save(Object obj) {
		session = SessionFactory.getSession(ctx);
		session.open();
		session.save(obj);
		session.close();

	}

	/**
	 * Find Object Entity in Database
	 * 
	 * @param cls
	 *            Class of the Object
	 * @param pk
	 *            Primary Key Parameter
	 * @return Object ,null if not exist
	 */
	public <U, T> U findByPK(Class<U> obj, T pk) {
		session = SessionFactory.getSession(ctx);
		session.open();
		U res = session.findByPK(obj, pk);
		session.close();
		return res;
	}

	/**
	 * Delete Object in Database
	 * 
	 * @param cls
	 *            Class of the Object
	 * @param pk
	 *            Primary key Parameter
	 */
	public <U, T> void deletebyPK(Class<U> cls, T pk) {
		session = SessionFactory.getSession(ctx);
		session.open();
		session.deletebyPK(cls, pk);
		session.close();

	}

	/**
	 * Update Object
	 * 
	 * @param obj
	 */
	public <U> void update(U obj) {
		session = SessionFactory.getSession(ctx);
		session.open();
		session.update(obj);
		session.close();

	}

	/**
	 * Check if a Record exist in database
	 * 
	 * @param cls
	 *            Class of the Object
	 * @param pk
	 *            Primary key Parameter
	 * @return
	 */
	public <U, T> boolean exists(Class<U> cls, T pk) {
		session = SessionFactory.getSession(ctx);
		session.open();
		boolean res = session.exist(cls, pk);
		session.close();
		return res;
	}

	/**
	 * Get All Object Records
	 * 
	 * @param cls
	 *            Class of the Object
	 * @param args
	 *            where clause parameters
	 * @return
	 */
	public <U, T> List<U> getAll(Class<U> cls, T... args) {
		session = SessionFactory.getSession(ctx);
		session.open();
		List<U> res = session.getAll(cls, args);
		session.close();
		return res;
	}

	/**
	 * Get All Object Records
	 * 
	 * @param cls
	 *            Class of the Object
	 * @param where
	 *            Predicate Parameter for Condition
	 * @return
	 */
	public <U> List<U> getAll(Class<U> cls, Predicate<U> where) {
		session = SessionFactory.getSession(ctx);
		session.open();
		List<U> res = session.getAll(cls, where);
		session.close();
		return res;
	}

	/**
	 * Execute SQL Query Like INSERT,UPDATE,...
	 * 
	 * @param sql
	 *            Query String
	 */
	public void execSql(String sql) {
		session = SessionFactory.getSession(ctx);
		session.open();
		session.execSql(sql);
		session.close();
	}

	/**
	 * Execute Select Sql Query
	 * 
	 * @param sql
	 *            Query String
	 * @param args
	 *            Query Parameters
	 * @return Android Database Cursor
	 */
	public Cursor querySql(String sql, String[] args) {
		session = SessionFactory.getSession(ctx);
		session.open();
		Cursor cursor = session.querySql(sql, args);
		session.close();
		return cursor;
	}

	/**
	 * Delete all the objects
	 * @param cls Objects' class
	 */
	public <U> void deleteAll(Class<U> cls) {
		session = SessionFactory.getSession(ctx);
		session.open();
		session.deleteAll(cls);
		session.close();

	}

}
