package com.desserttowersprogramming.quickproviderapp;

import com.desserttowersprogramming.quickprovider.QuickJoin;
import com.desserttowersprogramming.quickprovider.QuickProvider;

import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by ryantaylor on 4/13/16.
 */
public class MainProvider extends QuickProvider {
    public static final String AUTHORITY = "com.deserttowersprogramming.quickproviderapp.MainProvider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final String LEFT_JOIN_NAME = "LeftJoin";
    public static final Uri LEFT_JOIN_URI = Uri.withAppendedPath(AUTHORITY_URI, LEFT_JOIN_NAME);
    public static final String LEFT_JOIN = QuickJoin.Builder()
            .setFirstTableData(TestTable.TABLE_NAME, TestTable.UUID)
            .setSecondTableData(SubTable.TABLE_NAME, SubTable.UUID)
            .leftJoin()
            .build();

    public static final String MANY_TO_MANY_NAME = "ManyToMany";
    public static final Uri MANY_TO_MANY_URI = Uri.withAppendedPath(AUTHORITY_URI, MANY_TO_MANY_NAME);
    public static final String MANY_TO_MANY_JOIN = QuickJoin.Builder()
            .setFirstTableData(TestTable.TABLE_NAME, TestTable.UUID, TestTable.UUID)
            .setSecondTableData(SubTable.TABLE_NAME, SubTable.UUID)
            .setThirdTableData(SubTestTable.TABLE_NAME, SubTestTable.TEST_TABLE)
            .manyToManyJoin()
            .build();

    @Override
    public boolean onCreate() {
        super.onCreate();

        allowForeignKeyConstrains(true);

        addToUriMatcher(TestTable.TABLE_NAME);
        addToUriMatcher(SubTable.TABLE_NAME);
        addToUriMatcher(SubTestTable.TABLE_NAME);

        addJoinToUriMatcher(LEFT_JOIN_NAME, LEFT_JOIN);
        addJoinToUriMatcher(MANY_TO_MANY_NAME, MANY_TO_MANY_JOIN);

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
