package com.alorm.core.sqlite.session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alorm.api.Session;
import com.alorm.core.sqlite.helper.DataBaseOpenHelper;
import com.alorm.core.sqlite.session.utils.InsertQuery;
import com.alorm.core.sqlite.session.utils.LazyRow;
import com.alorm.core.sqlite.session.utils.UpdateQuery;
import com.alorm.core.sqlite.utils.AlormUtils;
import com.alorm.core.sqlite.utils.AssocClass;
import com.alorm.core.sqlite.utils.Dependency;
import com.alorm.core.sqlite.utils.MappingClass;
import com.alorm.core.sqlite.utils.Property;
import com.alorm.exceptions.AlormException;
import com.alorm.exceptions.EntityException;
import com.android.internal.util.Predicate;

public class SQLiteSession implements Session {

	private static SQLiteDatabase db;
	private List<MappingClass> listMapping;
	private List<AssocClass> listassoc;
	private DataBaseOpenHelper DBhelper;
	
	public SQLiteSession(DataBaseOpenHelper dh) {
		DBhelper=dh;
		listMapping = dh.getAlormConfig().getListe_mapping();
		listassoc = dh.getAlormConfig().getListe_assoc();
	}
	
	public void open(){
		db=DBhelper.getWritableDatabase();
	}
	
	public void close(){
		db.close();
	}

	public void beginTransaction() {
		db.beginTransaction();
	}

	public void rollBack() {
		db.endTransaction();
	}

	public void commit() {
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void save(Object obj) {

		// get the obj's Class
		Class cls = obj.getClass();
		// Search for Class's MappingClass
		MappingClass mc = AlormUtils.getMappingClassbyType(cls, listMapping);
		if (mc != null) {
			ContentValues cv = new ContentValues();

			List<UpdateQuery> updatequerys = new ArrayList<UpdateQuery>();
			List<InsertQuery> insertquerys = new ArrayList<InsertQuery>();

			Object objID;
			if(mc.getID().isAutoincrement()) objID=GetAIPrimaryKey(mc.getTable(), mc.getID().getColumn());
			else objID=getProp(obj, mc.getID().getName());
			
			// Iterating each property
			for (Property p : mc.getProperties()) {
				if (!p.isAutoincrement()) {
					Object res = getProp(obj, p.getName());
					if(res!=null)cv.put(p.getColumn(), res.toString());
				}
			}
			
			// Iterating each dependency
			for (Dependency d : mc.getDependencies()) {
				Object res = getProp(obj, d.getName());
				if (res != null && d.isCascade()) {
					if (d.isOne()) {
						Object res2 = getProp(res, d.getMappingClass().getID().getName());
						if(res2==null && d.getMappingClass().getID().isAutoincrement()) res2=GetAIPrimaryKey(d.getMappingClass().getTable(), d.getMappingClass().getID().getColumn());
						if (res2 != null) {
							if (exist(d.getType(), res2))
								cv.put(d.getColumn(), res2.toString());
							else {
								save(res);
								cv.put(d.getColumn(), res2.toString());
							}
						}
					} else {
						List<Object> list = (List) res;
						if (d.getMappingClass().checkListdependency(
								mc.getType())) {
							for (Object object : list) {
								Object res_1 = getProp(object, d.getMappingClass().getID().getName());
								if(res_1==null && d.getMappingClass().getID().isAutoincrement()) res_1=GetAIPrimaryKey(d.getMappingClass().getTable(), d.getMappingClass().getID().getColumn());
								if (!exist(d.getType(), res_1))
									save(object);
								if (res_1 != null && objID != null) {
									// Insertion in AssocTable
									AssocClass ac = AlormUtils.getAssocClassbyMappingClass(mc,d.getMappingClass(),listassoc);
									ContentValues cv2 = new ContentValues();
									cv2.put(d.getMappingClass().getTable()+ "_ID", res_1.toString());
									cv2.put(mc.getTable() + "_ID",objID.toString());
									insertquerys.add(new InsertQuery(ac.getTable(), cv2));
								}
							}
						} else {
							for (Object object : list) {
								Object res3 = getProp(object, d.getMappingClass().getID().getName());
								if(res3==null && d.getMappingClass().getID().isAutoincrement()) res3=GetAIPrimaryKey(d.getMappingClass().getTable(), d.getMappingClass().getID().getColumn());
								if (!exist(d.getType(), res3)) {
									// Insertion of the new object with
									// dependency value set
									save(object);
									if(objID!=null && res3!=null){
									ContentValues cv2 = new ContentValues();
									cv2.put(d.getColumn(), objID.toString());
									updatequerys.add(new UpdateQuery(d.getMappingClass().getTable(), cv2,d.getMappingClass().getID().getColumn()+ "=?", new String[] { res3.toString() }));
									}
								}
							}
						}
					}
				}
			}
			if (cv.size() > 0 || objID!=null) {
				db.insert(mc.getTable(), null, cv);
				for (UpdateQuery uq : updatequerys) {
					db.update(uq.getTable(), uq.getCv(), uq.getWhereclause(),
							uq.getWhereargs());
				}
				for (InsertQuery iq : insertquerys) {
					db.insert(iq.getTable(), null, iq.getCv());
				}
			}
		}
	}

	public <U, T> U findByPK(Class<U> cls, T pk) {
		List<LazyRow> list=new ArrayList<LazyRow>();
		return getBypk(cls, pk, list);
	}
	
		
	public <U, T> List<U> getAll(Class<U> cls, T... args) {
        if (args.length >= 1 && args[0].getClass() != String.class)
            throw new AlormException("the Where clause must be String");
        String[] whereArgs = null;
        String whereClause = null;
        if (args.length == 1)
            whereClause = (String) args[0];
        if (args.length > 1) {
            whereClause = (String) args[0];
            int nbParams=getNBParams(whereClause);
            if (nbParams == 0)
                throw new AlormException("Arguments out of range");
            if (args.length - 1 != nbParams)
                throw new AlormException("Arguments' numbers must match");

            whereArgs = new String[nbParams];
            for (int i = 1; i != args.length; i++) {
                whereArgs[i - 1] = args[i].toString();
            }
        }

        String table = null;
        String pkColumn = null;
        MappingClass mp=AlormUtils.getMappingClassbyType(cls, listMapping);
        table = mp.getTable();
        pkColumn = mp.getID().getColumn();
        Cursor cursor = db.query(table, new String[] { pkColumn }, whereClause,
                whereArgs, null, null, null);

        List<U> resList = null;
        if (cursor.moveToFirst()) {

            resList = new ArrayList<U>();
            do {
            	Object pk=getPropinCursor(mp.getID().getType(), cursor, 0);
            	resList.add(findByPK(cls, pk));

            } while (cursor.moveToNext());

        }
        return resList;
    }
   
	public <U> List<U> getAll(Class<U> cls,Predicate<U> where){
		MappingClass mc=AlormUtils.getMappingClassbyType(cls, listMapping);
		if(mc!=null){
			Cursor c=db.query(mc.getTable(), new String[]{mc.getID().getColumn()},null,null,null,null,null);
			List<U> list=new ArrayList<U>();
			if(c.moveToFirst()){
				do{
					Object pk=getPropinCursor(mc.getID().getType(), c, 0);
					U res=(U) findByPK(cls,pk);
					if(where.apply(res)) list.add(res); 
				}while(c.moveToNext());
			}
			return list;
		}
		return null;
	}
			
	public <U> void update(U obj) {
		if (obj == null)
			return;
		// get the obj's Class
		Class cls = obj.getClass();
		// Search for Class's MappingClass
		MappingClass mc = AlormUtils.getMappingClassbyType(cls, listMapping);
		if (mc != null) {
			U dbobj;
			// Find the object in Database
			Object ID = getProp(obj, mc.getID().getName());
			dbobj = (U) findByPK(cls, ID);
			if (dbobj != null) {
				ContentValues cv = new ContentValues();
				for (Property p : mc.getProperties()) {
					if (p != mc.getID()) {
						Class clst = p.getType();
						Object o1 = clst.cast(getProp(dbobj, p.getName()));
						Object o2 = clst.cast(getProp(obj, p.getName()));
						if (!o1.equals(o2)) cv.put(p.getColumn(), o2.toString());
					}
				}

				for (Dependency d : mc.getDependencies()) {
					Object res = getProp(obj, d.getName());
					if(d.isCascade()){
					if (res != null) {
						if (d.isOne()) {
							update(res);
						} else {
								List res2 = (List) res;
								//save if not exist
								for (Object object : res2) update(object);
						}
					}
				   }
				}

				if (cv.size() > 0)
					db.update(mc.getTable(), cv, mc.getID().getColumn() + "=?",
							new String[] { ID.toString() });
			}
		}
	}

	public <U, T> boolean exist(Class<U> cls, T pk) {
		if (pk == null)
			return false;
		MappingClass mc = AlormUtils.getMappingClassbyType(cls, listMapping);
		if (mc == null || mc.getID().getType() != pk.getClass())
			return false;
		Cursor it = db.query(mc.getTable(), null,
				mc.getID().getColumn() + "=?", new String[] { pk.toString() },
				null, null, null);
		if (it.moveToFirst())
			return true;
		return false;
	}
	
	public <U, T> void deletebyPK(Class<U> cls, T pk) {
		if (exist(cls, pk)) {
			MappingClass mc = AlormUtils
					.getMappingClassbyType(cls, listMapping);
			db.delete(mc.getTable(), mc.getID().getColumn() + "=?",
					new String[] { pk.toString() });
		}
	}	

	public <U> void deleteAll(Class<U> cls) {
		MappingClass mc=AlormUtils.getMappingClassbyType(cls, listMapping);
		if(mc!=null){
			db.execSQL("delete from "+mc.getTable());
		}
	}
	
	public <U> void deleteAll(Class<U> cls,Predicate<U> where) {
		MappingClass mc=AlormUtils.getMappingClassbyType(cls, listMapping);
		if(mc!=null){
			Cursor c=db.query(mc.getTable(), new String[]{mc.getID().getColumn()},null,null,null,null,null);
			if(c.moveToFirst()){
				do{
					Object pk=getPropinCursor(mc.getID().getType(), c, 0);
					U res=(U) findByPK(cls,pk);
					if(where.apply(res)) db.delete(mc.getTable(), mc.getID().getColumn() + "=?",new String[] { pk.toString() });
				}while(c.moveToNext());
			}
		}
	}
	
	public void execSql(String sql){
		db.execSQL(sql);
	}
	
	public Cursor querySql(String sql,String[]args){
		return db.rawQuery(sql, args);
	}
	
	
	//Private Session Methods
	private <U, T> U getBypk(Class<U> cls, T pk,List<LazyRow> list) {
		MappingClass mc = AlormUtils.getMappingClassbyType(cls, listMapping);
		if (mc == null || mc.getID().getType() != pk.getClass())
			return null;
		Cursor it = db.query(mc.getTable(), null,mc.getID().getColumn() + "=?", new String[] { pk.toString() },null, null, null);
		if (it.moveToFirst()) {
			U obj;
			try {
				obj = cls.newInstance();
			} catch (InstantiationException e1) {
				throw new EntityException(e1);
			} catch (IllegalAccessException e1) {
				throw new EntityException(e1);
			}
			for (Property p : mc.getProperties()) {
				setProp(obj,p.getName(),getPropinCursor(p.getType(), it,it.getColumnIndex(p.getColumn())),p.getType());
			}

			list.add(new LazyRow(mc.getType(), pk.toString(),obj)); //Add to Lazy Rows List
			
			for (Dependency d : mc.getDependencies()) {
				// Check the relation & Lazy Loading
				if (!d.isLazy() ) {
					try {
						if (d.isOne()) {
							Object id = it.getString(it.getColumnIndex(d.getColumn()));
							if(id!=null){
								Object objc=AlormUtils.isLoad(list, d.getType(), id.toString());
								if(objc!=null) 	setProp(obj, d.getName(), objc,d.getType());
								else setProp(obj, d.getName(), getBypk(d.getType(), id,list),d.getType());
							}
						} else {
							String table;
							String pkColumn;
							String fkColumn;
							if (d.getMappingClass().checkListdependency(mc.getType())) {
								AssocClass ac = AlormUtils.getAssocClassbyMappingClass(d.getMappingClass(), mc,listassoc);
								table = ac.getTable();
								pkColumn = ac.getColumn(d.getMappingClass());
								fkColumn = ac.getColumn(mc);
							} else {
								table = d.getMappingClass().getTable();
								pkColumn = d.getMappingClass().getID().getColumn();
								fkColumn = d.getColumn();
							}
							Cursor it2 = db.query(table,
									new String[] { pkColumn }, fkColumn + "=?",
									new String[] { pk.toString() }, null, null,
									null);
							List<Object> res = new ArrayList<Object>();
							if (it2.moveToFirst()) {
								do {
									Object dependency = d.getType().newInstance();
									Object id2 = it2.getString(0);
									if(id2!=null){
										Object objc=AlormUtils.isLoad(list, d.getType(), id2.toString());
										if(objc!=null) dependency=objc;
										else dependency = getBypk(d.getType(), id2,list);
										res.add(dependency);
									}
								} while (it2.moveToNext());
							}
							setListProp(obj, d.getName(), res);
						}

					} catch (SecurityException e) {
						throw new EntityException(e);
					} catch (IllegalArgumentException e) {
						throw new EntityException(e);
					} catch (InstantiationException e) {
						throw new EntityException(e);
					} catch (IllegalAccessException e) {
						throw new EntityException(e);
					}
				}
			}

			return obj;
		}
		return null;
	}


	// Intern Methods
    private boolean isNull(String s) {

        char[] c = s.toCharArray();
        for (int i = 0; i != c.length; i++)
            if (c[i] != 0) {
                return false;
            }
        return true;
    }

    private int getNBParams(String s){
    	int nb=0;
    	for(char c : s.toCharArray())
    		if(c=='?')
    			nb++;
    	
    	return nb;
    }
	private Object GetAIPrimaryKey(String table,String id){
		int i=0;
		Cursor c=db.rawQuery("select max("+id+") from "+table,null);
		if(c.moveToFirst()) i= c.getInt(0);
		return i+1;
	}
	private String capitalize(String s) {
		return (String.valueOf(s.charAt(0))).toUpperCase()
				+ s.substring(1, s.length());
	}

	private void setProp(Object bean, String p, Object value,Class type) {
		Method m;
		try {
			m = bean.getClass().getMethod("set" + capitalize(p),type);
			m.invoke(bean, value);
		} catch (SecurityException e) {
			throw new EntityException(e);
		} catch (NoSuchMethodException e) {
			throw new EntityException(e);
		} catch (IllegalArgumentException e) {
			throw new EntityException(e);
		} catch (IllegalAccessException e) {
			throw new EntityException(e);
		} catch (InvocationTargetException e) {
			throw new EntityException(e);
		}

	}

	private void setListProp(Object bean, String p, Object value) {
		Method m;
		try {
			m = bean.getClass().getMethod("set" + capitalize(p), List.class);
			m.invoke(bean, value);
		} catch (SecurityException e) {
			throw new EntityException(e);
		} catch (NoSuchMethodException e) {
			throw new EntityException(e);
		} catch (IllegalArgumentException e) {
			throw new EntityException(e);
		} catch (IllegalAccessException e) {
			throw new EntityException(e);
		} catch (InvocationTargetException e) {
			throw new EntityException(e);
		}

	}

	private Object getProp(Object bean, String p) {
		try {
			Method m = bean.getClass().getMethod("get" + capitalize(p));
			return m.invoke(bean);
		} catch (SecurityException e) {
			throw new EntityException(e);
		} catch (NoSuchMethodException e) {
			throw new EntityException(e);
		} catch (IllegalArgumentException e) {
			throw new EntityException(e);
		} catch (IllegalAccessException e) {
			throw new EntityException(e);
		} catch (InvocationTargetException e) {
			throw new EntityException(e);
		}
	}

	private <P> P getPropinCursor(Class<P> type, Cursor cursor, int index) {
		if (type == Integer.class || type==int.class)
			return (P)Integer.valueOf(cursor.getInt(index));	
		else if (type == Long.class || type==long.class)
			return (P)Long.valueOf(cursor.getLong(index));
		else if (type == Short.class || type==short.class)
			return (P)Short.valueOf(cursor.getShort(index));
		else if (type == Double.class || type==double.class)
			return (P)Double.valueOf(cursor.getDouble(index));
		else if (type == Float.class || type==float.class)
			return (P)Float.valueOf(cursor.getFloat(index));
		else if(type==Date.class) 
			return (P)Date.valueOf(cursor.getString(index));
		else if(type==Boolean.class || type==boolean.class)
			return (P)Boolean.valueOf(cursor.getString(index));
		else return (P)type.cast(cursor.getString(index));
	}
	
	

}