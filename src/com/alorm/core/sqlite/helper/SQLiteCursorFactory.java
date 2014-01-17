package com.alorm.core.sqlite.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

public class SQLiteCursorFactory implements CursorFactory {

    private boolean debugQueries;

    public SQLiteCursorFactory() {
        this.debugQueries = true;
    }

    public SQLiteCursorFactory(boolean debugQueries) {
        this.debugQueries = debugQueries;
    }

    @Override
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, 
                            String editTable, SQLiteQuery query) {
        if (debugQueries) {
            Log.d("SQLQuery", query.toString());
        }
        return new SQLiteCursor(db, masterQuery, editTable, query);
    }
}