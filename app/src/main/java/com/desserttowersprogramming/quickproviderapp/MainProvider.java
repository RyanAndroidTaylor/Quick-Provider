package com.desserttowersprogramming.quickproviderapp;

import com.desserttowersprogramming.quickprovider.QuickProvider;

import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by ryantaylor on 4/13/16.
 */
public class MainProvider extends QuickProvider {
    public static final String AUTHORITY = "com.deserttowersprogramming.quickproviderapp.MainProvider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    @Override
    public boolean onCreate() {
        super.onCreate();

        allowForeignKeyConstrains(true);

        addToUriMatcher(TestTable.TABLE_NAME);
        addToUriMatcher(SubTable.TABLE_NAME);

        return true;
    }

    @Override
    protected SQLiteOpenHelper getSQLiteOpenHelper() {
        return new DatabaseHelper(getContext());
    }

    @Override
    protected String getAuthorityString() {
        return AUTHORITY;
    }

    @Override
    protected String getMimeTypeName() {
        return "mainprovider";
    }
}
