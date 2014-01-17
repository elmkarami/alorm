package com.alorm.core.sqlite.utils;



import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alorm.annotation.Column;
import com.alorm.annotation.Entity;
import com.alorm.core.sqlite.utils.sqlite.SQLiteUtils;
import com.alorm.exceptions.MappingException;

public class MappingClass {


	private Class type;
	private String table;
	private List<Property> properties=new ArrayList<Property>();
	private List<Dependency> dependencies=new ArrayList<Dependency>();
	private Property ID;
	
	public MappingClass(Class classType){
		type=classType;
		
		//Check for Default Constructor
		try {
			if(type.getConstructor(null)==null) throw new MappingException("NO DEFAULT CONSTRUCTOR IN "+type);
		} catch (SecurityException e1) {
			throw new MappingException(e1,"DEFAULT CONSTRUCTOR IN "+type+" IS INACCESSIBLE");
		} catch (NoSuchMethodException e1) {
			throw new MappingException(e1,"NO DEFAULT CONSTRUCTOR IN "+type);
		} 
		
		table=AlormUtils.getEntityClass(type.getName());
				
		List<String> columns=new ArrayList<String>(); //For Column Name duplication
		Map<Class,Integer> bodependencies=new HashMap<Class,Integer>(); //For Multiple dependency
		Map<Class,Integer> listdependencies=new HashMap<Class,Integer>(); //For Multiple dependency
		
		for(Field f:getInheritedFields(type)){
			if (SQLiteUtils.SQLiteType.containsKey(f.getType().getName())){
				Property prop=new Property(f);
				
				//Custumise Properties via annotations
				Column column=f.getAnnotation(Column.class);
				
				//Specif ID
				if(column!=null && column.primarykey()==true && !f.getType().isPrimitive()){
					if(ID!=null) ID.setPrimarykey(false);
					prop.setPrimarykey(true);
					ID=prop;
				}
				
				//Set Column Name && Check Column Duplication
				if(column!=null && checkColumnName(column.name())) prop.setColumn(column.name());
				if(columns.contains(prop.getColumn())) throw new MappingException("Column :"+prop.getColumn()+" is Duplicated");
				columns.add(prop.getColumn());
				
				//Set other Property constraints
				if(column!=null){
					if(column.autoincrement()==true){
						if(isValidAI(prop.getType())) prop.setAutoincrement(true);
						else throw new MappingException("AutoIncrement Field must be NUMERIC :"+prop.getName());
					}
					if(column.notnull()==true) prop.setNotnull(true);
					if(column.unique()==true) prop.setUnique(true);
					if(!column.default_value().equals("")) prop.setDefault_value(column.default_value());
				}

				//Add Property to List
				properties.add(prop);
			}else{
				Dependency dep=new Dependency(this, f);
				Column column=f.getAnnotation(Column.class);
				if(column!=null){
					if(!column.attribute_ref().equals("")) 	dep.setAttribute_ref(column.attribute_ref());
					if(column.oncascade()) 	dep.setCascade(true);
					if(column.lazy()==false) dep.setLazy(false);
				}				
				dependencies.add(dep); //Add dependency
			}
		}
		
		//Check if ID exist
		checkPK(); 
		
		//Set Table Name
		Entity e = (Entity) type.getAnnotation(Entity.class);
		if(checkTableName(e.table())) table= e.table();
		
	}
	
	
	public String generateSQLiteRequest() {
		String sql = String.format("create table if not exists %s(", table);
		for (int i = 0; i < properties.size(); i++) {
			if (i == properties.size() - 1) {
				sql += properties.get(i).generateSQL() + ");";
			} else {
				sql += properties.get(i).generateSQL() + ",";
			}
		}
		return sql;
	}

	public void checkMappingdependencies(List<Class> classes){
		for(Dependency dp:dependencies){
			if(!classes.contains(dp.getType())) throw new MappingException("DependencyClass NOTFOUND :"+dp.getType().getName());
		}
	}
	
	public boolean checkListdependency(Class type){
		for(Dependency d:dependencies){
			if(!d.isOne() && d.getType()==type) return true;
		}
		return false;
	}
	
	public Dependency getListdependency(Class type){
		for(Dependency d:dependencies){
			if(!d.isOne() && d.getType()==type) return d;
		}
		return null;
	}
	
	
	//Intern Methods
	
	private boolean isValidAI(Class type){
		if(type==int.class || type==Long.class || type==Integer.class || type==Short.class) return true;
		else return false;
	}
	
	private void checkPK(){
		if(ID==null) throw new MappingException("ID NOT FOUND in:"+type.getName()+" (*Must be an Object)");
	}
	
    private List<Field> getInheritedFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
    
    private boolean checkColumnName(String col){
    	if(!col.contains("_ID") && col.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) return true;
    	else return false;
    }

    private boolean checkTableName(String table){
    	if(table.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) return true;
    	else return false;
    }
    


	//Getters & setters
	public List<Property> getProperties() {
		return properties;
	}

	public List<Dependency> getDependencies() {
		return dependencies;
	}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public Class getType() {
		return type;
	}

	public Property getID() {
		return ID;
	}
}
