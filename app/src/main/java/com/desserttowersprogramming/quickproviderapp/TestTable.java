package com.desserttowersprogramming.quickproviderapp;

import com.desserttowersprogramming.quickprovider.QuickTable;
import com.desserttowersprogramming.quickprovider.Table;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by ryantaylor on 4/13/16.
 */
public class TestTable implements Table {

    public static final String TABLE_NAME = QuickTable.getInstance().open("TestTable");

    public static final String SOME_TEXT = QuickTable.getInstance().buildTextColumn("SomeText").build();
    public static final String NAME = QuickTable.getInstance().buildTextColumn("Name").build();
    public static final String NOT_NULL_TEXT = QuickTable.getInstance().buildTextColumn("NoNullText").notNull().build();
    public static final String SOME_NUMBER = QuickTable.getInstance().buildIntColumn("SomeNumber").build();
    public static final String AMOUNT = QuickTable.getInstance().buildIntColumn("Amount").build();

    public static final String CREATE = QuickTable.getInstance().retrieveCreateString();

    public static final Uri CONTENT_URI = Uri.withAppendedPath(MainProvider.AUTHORITY_URI, TABLE_NAME);

    private long mId;
    private String mUuid;
    private String mSomeText;
    private String mNotNullText;
    private String mName;
    private int mSomeNumber;
    private int mAmount;

    public TestTable(String uuid, String someText, String notNullText, String name, int someNumber, int amount) {
        mId = NULL_ID;
        mUuid = uuid;
        mSomeText = someText;
        mNotNullText = notNullText;
        mName = name;
        mSomeNumber = someNumber;
        mAmount = amount;
    }

    public TestTable(Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndex(ID));
        mUuid = cursor.getString(cursor.getColumnIndex(UUID));
        mSomeText = cursor.getString(cursor.getColumnIndex(SOME_TEXT));
        mNotNullText = cursor.getString(cursor.getColumnIndex(NOT_NULL_TEXT));
        mName = cursor.getString(cursor.getColumnIndex(NAME));
        mSomeNumber = cursor.getInt(cursor.getColumnIndex(SOME_NUMBER));
        mAmount = cursor.getInt(cursor.getColumnIndex(AMOUNT));
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public String getUuid() {
        return mUuid;
    }

    public String getSomeText() {
        return mSomeText;
    }

    public String getNotNullText() {
        return mNotNullText;
    }

    public String getName() {
        return mName;
    }

    public int getSomeNumber() {
        return mSomeNumber;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setSomeText(String someText) {
        mSomeText = someText;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setNotNullText(String notNullText) {
        mNotNullText = notNullText;
    }

    public void setSomeNumber(int someNumber) {
        mSomeNumber = someNumber;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(UUID, mUuid);
        contentValues.put(SOME_TEXT, mSomeText);
        contentValues.put(NAME, mName);
        contentValues.put(NOT_NULL_TEXT, mNotNullText);
        contentValues.put(SOME_NUMBER, mSomeNumber);
        contentValues.put(AMOUNT, mAmount);

        return contentValues;
    }

    @Override
    public Uri getContentUri() {
        return CONTENT_URI;
    }

    public static List<TestTable> getItems(Cursor cursor) {
        List<TestTable> items = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                items.add(new TestTable(cursor));
            }
        }

        return items;
    }

    public static TestTable getItem(Cursor cursor) {
        return new TestTable(cursor);
    }
}
