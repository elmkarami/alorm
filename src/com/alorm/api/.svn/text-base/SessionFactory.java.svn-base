package com.alorm.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.alorm.core.sqlite.helper.DataBaseOpenHelper;
import com.alorm.core.sqlite.session.SQLiteSession;
import com.alorm.exceptions.AlormException;

/**
 * SessionFactory
 * 
 * @author Karami,Laraki,Sadik,Abbadi,Zaelouk
 * 
 */
public class SessionFactory {

	private static final String SQLITE = "sqlite";
	private static DataBaseOpenHelper dbOH;
	private static Session session;

	private SessionFactory() {

	}

	/**
	 * Get Current Session
	 * 
	 * @param context
	 *            Context of the Current Activity
	 * @return
	 */
	public static Session getSession(Context context) {

		if (session != null)
			return session;
		if (context != null) {
			System.out.println("non null");
		} else {
			System.out.println("null");
		}
		Resources resources = context.getResources();
		AssetManager assetManager = resources.getAssets();

		// Read from the /assets directory
		try {
			InputStream inputStream = assetManager.open("alorm.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			String dbType = properties.getProperty("dbType");
			String dbName = properties.getProperty("dbName");
			String dbVersion = properties.getProperty("dbVersion");
			int version = 0;

			if (!SQLITE.equals(dbType) && dbType != null) {
				throw new AlormException(
						"DataBase : '"
								+ dbType
								+ "' not supported by Alorm, it may be supported soon !");
			} else {
				if (dbName == null)
					throw new AlormException("dataBase name not specified");

				try {
					version = Integer.valueOf(dbVersion);
				} catch (NumberFormatException e) {
					throw new AlormException(e,
							" version format is not valid, dbVersion must be an Integer");
				}

				// All informations are valid

				dbOH = new DataBaseOpenHelper(context, dbName, version);
				session = new SQLiteSession(dbOH);
				return session;

			}

		} catch (IOException e) {
			throw new AlormException(e,
					"Failed to open the property file alorm.properties");
		}
	}

}
