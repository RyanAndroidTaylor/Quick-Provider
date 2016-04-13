package com.desserttowersprogramming.quickprovider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public abstract class QuickProvider extends ContentProvider {
    private static final String DIR_PREFIX_PATH = "vnd.android.cursor.dir/vnd.";
    private static final String ITEM_PREFIX_PATH = "vnd.android.cursor.item/vnd.";

    private UriMatcher uriMatcher;

    private List<MatchedUriData> mTypes;

    private SQLiteOpenHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mDatabaseHelper = getSQLiteOpenHelper();
        mTypes = new ArrayList<>();
        return false;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int matchedType = uriMatcher.match(uri);

        if (matchedType < mTypes.size())
            return mTypes.get(matchedType).mMimeType;
        else
            throw new IllegalArgumentException("There was not matching URI for " + uri);
    }

    protected void addToUriMatcher(String path) {
        uriMatcher.addURI(getAuthorityString(), path, mTypes.size());
        mTypes.add(new MatchedUriData(path, DIR_PREFIX_PATH + getMimeTypeName() + path, MatchedUriData.TYPE_DIR, null));


        uriMatcher.addURI(getAuthorityString(), path + "/#", mTypes.size());
        mTypes.add(new MatchedUriData(path, ITEM_PREFIX_PATH + getMimeTypeName() + path, MatchedUriData.TYPE_ITEM, null));
    }

    protected void addJoinToUriMatcher(String path, String join) {
        uriMatcher.addURI(getAuthorityString(), path, mTypes.size());
        mTypes.add(new MatchedUriData(path, DIR_PREFIX_PATH + getMimeTypeName() + path, MatchedUriData.TYPE_DIR, join));


        uriMatcher.addURI(getAuthorityString(), path + "/#", mTypes.size());
        mTypes.add(new MatchedUriData(path, ITEM_PREFIX_PATH + getMimeTypeName() + path, MatchedUriData.TYPE_ITEM, join));
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final SQLiteDatabase databaseConnection = mDatabaseHelper.getWritableDatabase();

        MatchedUriData data = mTypes.get(uriMatcher.match(uri));

        switch (data.mType) {
            case MatchedUriData.TYPE_ITEM:
                queryBuilder.appendWhere(Table.ID + "=" + uri.getPathSegments().get(1));
            case MatchedUriData.TYPE_DIR:
                queryBuilder.setTables(data.getJoin());
                break;
        }

        Cursor cursor = queryBuilder.query(databaseConnection, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final SQLiteDatabase databaseConnection = mDatabaseHelper.getWritableDatabase();

        try {
            databaseConnection.beginTransaction();

            ContentProviderResult[] results = super.applyBatch(operations);

            databaseConnection.setTransactionSuccessful();
            return results;
        } finally {
            databaseConnection.endTransaction();
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase databaseConnection = mDatabaseHelper.getWritableDatabase();

        try {
            databaseConnection.beginTransaction();

            MatchedUriData data = mTypes.get(uriMatcher.match(uri));

            return insert(databaseConnection, data.mTableName, uri, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            databaseConnection.endTransaction();
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return null;
    }

    private Uri insert(SQLiteDatabase dataBaseConnection, String tableName, Uri contentUri, ContentValues values) throws SQLException {
        final long id = dataBaseConnection.insertOrThrow(tableName, null, values);
        final Uri uri = ContentUris.withAppendedId(contentUri, id);
        getContext().getContentResolver().notifyChange(uri, null);
        dataBaseConnection.setTransactionSuccessful();
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase databaseConnection = mDatabaseHelper.getWritableDatabase();
        int deleteCount = 0;

        try {
            databaseConnection.beginTransaction();

            MatchedUriData data = mTypes.get(uriMatcher.match(uri));

            switch (data.mType) {
                case MatchedUriData.TYPE_DIR:
                    deleteCount = deleteSelection(databaseConnection, data.mTableName, selection, selectionArgs);
                    break;
                case MatchedUriData.TYPE_ITEM:
                    deleteCount = deleteId(databaseConnection, data.mTableName, uri);
                    break;
            }

        } finally {
            databaseConnection.endTransaction();
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleteCount;
    }

    private int deleteSelection(SQLiteDatabase databaseConnection, String tableName, String selection, String[] selectionArgs) {
        int deleteCount = databaseConnection.delete(tableName, selection, selectionArgs);
        databaseConnection.setTransactionSuccessful();
        return deleteCount;
    }

    private int deleteId(SQLiteDatabase databaseConnection, String tableName, Uri uri) {
        int deleteCount = databaseConnection.delete(tableName, Table.WHERE_ID_EQUALS, new String[]{uri.getPathSegments().get(1)});
        databaseConnection.setTransactionSuccessful();
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase databaseConnection = mDatabaseHelper.getWritableDatabase();
        int updateCount = 0;

        try {
            databaseConnection.beginTransaction();

            MatchedUriData data = mTypes.get(uriMatcher.match(uri));

            switch (data.mType) {
                case MatchedUriData.TYPE_DIR:
                    updateCount = updateSelection(databaseConnection, data.mTableName, values, selection, selectionArgs);
                    break;
                case MatchedUriData.TYPE_ITEM:
                    updateCount = updateId(databaseConnection, data.mTableName, values, uri, selection, selectionArgs);
                    break;
            }

        } finally {
            databaseConnection.endTransaction();
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updateCount;
    }

    private int updateSelection(SQLiteDatabase databaseConnection, String tableName, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount = databaseConnection.update(tableName, values, selection, selectionArgs);
        databaseConnection.setTransactionSuccessful();
        return updateCount;
    }

    private int updateId(SQLiteDatabase databaseConnection, String tableName, ContentValues values, Uri uri, String selection, String[] selectionArgs) {
        final Long id = ContentUris.parseId(uri);
        int updateCount = databaseConnection.update(tableName, values, Table.ID + "=" + id + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
        databaseConnection.setTransactionSuccessful();
        return updateCount;
    }

    protected abstract SQLiteOpenHelper getSQLiteOpenHelper();

    protected abstract String getAuthorityString();

    protected abstract String getMimeTypeName();

    private class MatchedUriData {
        public static final int TYPE_DIR = 0;
        public static final int TYPE_ITEM = 1;

        private final int mType;
        private final String mMimeType;
        private final String mTableName;
        private final String mJoin;

        public MatchedUriData(String tableName, String mimeType, int type, String join) {
            mTableName = tableName;
            mMimeType = mimeType;
            mType = type;
            mJoin = join;
        }

        /**
         * Returns the join string. If no join string exists returns the table name
         */
        public String getJoin() {
            if (mJoin != null)
                return mJoin;

            return mTableName;
        }
    }
}
