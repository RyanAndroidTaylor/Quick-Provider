package com.desserttowersprogramming.quickproviderapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ryantaylor on 4/13/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String NAME = "com.quickproviderapp.DatabaseHelper";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TestTable.CREATE);
        db.execSQL(SubTable.CREATE);
        db.execSQL(SubTestTable.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
