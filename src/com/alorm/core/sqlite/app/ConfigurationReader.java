package com.alorm.core.sqlite.app;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import android.content.Context;
import com.alorm.annotation.Entity;
import com.alorm.core.sqlite.utils.AlormUtils;
import com.alorm.core.sqlite.utils.Dependency;
import com.alorm.core.sqlite.utils.MappingClass;
import com.alorm.exceptions.MappingException;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

public class ConfigurationReader {

	public static List<MappingClass> getMappingconfiguration(Context ctx){

		List<Class> classes = FindEntityclasses(ctx);
		List<MappingClass> list_mapping = new ArrayList<MappingClass>();
		List<String> tables = new ArrayList<String>();
		for (Class c : classes) {
			MappingClass m = new MappingClass(c);
			m.checkMappingdependencies(classes); // check dependencies
			if (tables.contains(m.getTable())) throw new MappingException("Table Name Already Exist "+ m.getTable()); // check Table duplication
			else tables.add(m.getTable());
			list_mapping.add(m); // Add to List
		}

		setDependencies(list_mapping); // Set MappingClass for each dependendy &
										// search for bidirectionel relations

		return list_mapping;
	}

	private static List<Class> FindEntityclasses(Context c){
		List<Class> classes=new ArrayList<Class>();
	    try {
	    	Field dexField = PathClassLoader.class.getDeclaredField("mDexs");
	        dexField.setAccessible(true);
	        PathClassLoader classLoader = (PathClassLoader) Thread.currentThread().getContextClassLoader();
	   
	        DexFile[] dexs = (DexFile[]) dexField.get(classLoader);
	        for (DexFile dex : dexs) {
	          Enumeration<String> entries = dex.entries();
	          while (entries.hasMoreElements()) {
	            String entry = entries.nextElement();
	            Class<?> entryClass = dex.loadClass(entry, classLoader);
	            if (entryClass != null) {
	              Entity annotation = entryClass.getAnnotation(Entity.class);
	              if (annotation != null) {
	            	  classes.add(entryClass);
	              }
	            }
	          }
	        }
	      } catch (Exception e) {
	      }		
		return classes;
	}

	private static void setDependencies(List<MappingClass> listm) {
		for (MappingClass mc : listm) {
			for (Dependency d : mc.getDependencies()) {
				MappingClass md = AlormUtils.getMappingClassbyType(d.getType(),listm);
				d.setMappingClass(md);
				Dependency dr = AlormUtils.getDependencybyAttribute(md,d.getAttribute_ref(), d.isOne());
				if (dr != null
						&& ((dr.getAttribute_ref() != null && 
						dr.getAttribute_ref().equals(d.getName())) ||
						dr.getAttribute_ref() == null)) {
					if (!d.isOne())
						d.setColumn(dr.getColumn());
					else
						dr.setColumn(d.getColumn());
				}
			}
		}
	}

}
