package com.alorm.core.sqlite.utils;


public class AssocClass {

	private String table;
	private MappingClass m1;
	private MappingClass m2;

	public AssocClass(String table, MappingClass m1, MappingClass m2) {
		this.table = table;
		this.m1 = m1;
		this.m2 = m2;
	}

	public String generateSqlRequest(String incascade1,String incascade2) {
		return "create table if not exists " + table
				+ "(id Integer primary key autoincrement," + m1.getTable()
				+ "_ID "
				+ m1.getID().getColumn_type()
				+ " references " + m1.getTable() + "(" + m1.getID().getColumn()
				+ ") "+incascade1+" ," + m2.getTable() + "_ID "
				+ m2.getID().getColumn_type()
				+ " references " + m2.getTable() + "(" + m2.getID().getColumn()
				+ ") "+incascade2+");";
	}

	public MappingClass getM1() {
		return m1;
	}

	public MappingClass getM2() {
		return m2;
	}

	public String getTable() {
		return table;
	}

	public String getColumn(MappingClass mappingClass) {
		return mappingClass.getTable()+"_ID";
	}
	

}
