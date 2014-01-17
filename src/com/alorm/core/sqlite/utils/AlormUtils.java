package com.alorm.core.sqlite.utils;

import java.util.List;

import com.alorm.core.sqlite.session.utils.LazyRow;

public class AlormUtils {

	public static MappingClass getMappingClassbyType(Class classType,List<MappingClass> list) {
		for (MappingClass m : list) {
			if (m.getType() == classType)
				return m;
			}
		return null;
	}

	public static Dependency getDependencybyAttribute(MappingClass mc,String attribute,boolean relation){
		for(Dependency dep:mc.getDependencies()){
			if(dep.getName().equals(attribute) && dep.isOne()!=relation) return dep;
		}
		return null;
	}
	
	public static String getEntityClass(String classname){
		String[] t = classname.trim().split("\\.");
		return t[t.length - 1];
	}
	
	public static AssocClass getAssocClassbyMappingClass(MappingClass m1,MappingClass m2,List<AssocClass> list){
		for(AssocClass ac:list){
			if((ac.getM1()==m1 && ac.getM2()==m2) || (ac.getM1()==m2 && ac.getM2()==m1)) return ac;
		}
		return null;
	}
	
	public static Object isLoad(List<LazyRow> list,Class clstype,String id){
		for(LazyRow lr:list){
			if(lr.getClstype()==clstype && lr.getId().equals(id)) return lr.getObj();
		}
		return null;
	}
}
