package com.desserttowersprogramming.quickproviderapp;

import com.desserttowersprogramming.quickprovider.QuickTable;
import com.desserttowersprogramming.quickprovider.Table;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by ryantaylor on 4/19/16.
 */
public class SubTestTable implements Table {

    public static final String TABLE_NAME = QuickTable.getInstance().open("SubTestTable");

    public static final String TEST_TABLE = QuickTable.getInstance().buildTextColumn("Test").foreignKey(TestTable.TABLE_NAME, TestTable.UUID).build();
    public static final String SUB_TABLE = QuickTable.getInstance().buildTextColumn("Sub").foreignKey(SubTable.TABLE_NAME, SubTable.UUID).build();
    public static final String LOCATION = QuickTable.getInstance().buildTextColumn("Location").build();

    public static final String CREATE = QuickTable.getInstance().retrieveCreateString();

    public static final Uri CONTENT_URI = Uri.withAppendedPath(MainProvider.AUTHORITY_URI, TABLE_NAME);

    private long mId;
    private String mTestTableUuid;
    private String mSubTableUUid;
    private String mLocation;
    private String mUuid;

    public SubTestTable(String uuid, String testTableUuid, String subTableUUid, String location) {
        mId = -1;
        mUuid = uuid;
        mTestTableUuid = testTableUuid;
        mSubTableUUid = subTableUUid;
        mLocation = location;
    }

    public SubTestTable(Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndex(ID));
        mUuid = cursor.getString(cursor.getColumnIndex(UUID));
        mTestTableUuid = cursor.getString(cursor.getColumnIndex(TEST_TABLE));
        mSubTableUUid = cursor.getString(cursor.getColumnIndex(SUB_TABLE));
        mLocation = cursor.getString(cursor.getColumnIndex(LOCATION));
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public String getUuid() {
        return mUuid;
    }

    public String getTestTableUuid() {
        return mTestTableUuid;
    }

    public String getSubTableUUid() {
        return mSubTableUUid;
    }

    public String getLocation() {
        return mLocation;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(UUID, mUuid);
        contentValues.put(TEST_TABLE, mTestTableUuid);
        contentValues.put(SUB_TABLE, mSubTableUUid);
        contentValues.put(LOCATION, mLocation);

        return contentValues;
    }

    @Override
    public Uri getContentUri() {
        return CONTENT_URI;
    }

    public static List<SubTestTable> getItems(Cursor cursor) {
        List<SubTestTable> items = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                items.add(new SubTestTable(cursor));
            }
        }

        return items;
    }

    public static SubTestTable getItem(Cursor cursor) {
        return new SubTestTable(cursor);
    }
}
