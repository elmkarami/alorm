package com.alorm.core.sqlite.helper;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.OpenableColumns;

import com.alorm.core.sqlite.app.AlormConfiguration;

public class DataBaseOpenHelper extends SQLiteOpenHelper {

	private AlormConfiguration alormConfig;
	private Context ctx;

	public DataBaseOpenHelper(Context context, String dbname, int version) {
		super(context, dbname, null, version);
		ctx = context;
		getWritableDatabase();
	}

	public void onOpen(SQLiteDatabase pdb) {
		pdb.execSQL("PRAGMA foreign_keys=ON;");
		if(alormConfig==null){
			alormConfig = new AlormConfiguration(ctx, pdb, "update");
			alormConfig.CreateDBschema();
		}
	}

	public void onCreate(SQLiteDatabase pdb) {
		alormConfig = new AlormConfiguration(ctx, pdb, "create");
		alormConfig.CreateDBschema();
		pdb.execSQL("PRAGMA foreign_keys=ON;");
	}

	public void onUpgrade(SQLiteDatabase pdb, int oldVersion, int newVersion) {
		onCreate(pdb);
	}

	// getters & setters
	public AlormConfiguration getAlormConfig() {
		return alormConfig;
	}

}
