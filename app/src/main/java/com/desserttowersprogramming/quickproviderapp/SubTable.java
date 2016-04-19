package com.desserttowersprogramming.quickproviderapp;

import com.desserttowersprogramming.quickprovider.QuickTable;
import com.desserttowersprogramming.quickprovider.Table;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by ryantaylor on 4/14/16.
 */
public class SubTable implements Table {

    public static final String TABLE_NAME = QuickTable.getInstance().openWithUuidForeignKeyRestraint("SubTable", TestTable.TABLE_NAME);

    public static final String DATA = QuickTable.getInstance().buildTextColumn("Data").build();

    public static final String CREATE = QuickTable.getInstance().retrieveCreateString();

    public static final Uri CONTENT_URI = Uri.withAppendedPath(MainProvider.AUTHORITY_URI, TABLE_NAME);

    private long mId;
    private String mData;
    private String mUuid;

    public SubTable(String uuid, String data) {
        mId = NULL_ID;
        mUuid = uuid;
        mData = data;
    }

    public SubTable(Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndex(ID));
        mUuid = cursor.getString(cursor.getColumnIndex(UUID));
        mData = cursor.getString(cursor.getColumnIndex(DATA));
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public String getUuid() {
        return mUuid;
    }

    public String getData() {
        return mData;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(UUID, mUuid);
        contentValues.put(DATA, mData);

        return contentValues;
    }

    @Override
    public Uri getContentUri() {
        return CONTENT_URI;
    }

    public static List<SubTable> getItems(Cursor cursor) {
        List<SubTable> items = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                items.add(new SubTable(cursor));
            }
        }

        return items;
    }

    public static SubTable getItem(Cursor cursor) {
        return new SubTable(cursor);
    }
}
