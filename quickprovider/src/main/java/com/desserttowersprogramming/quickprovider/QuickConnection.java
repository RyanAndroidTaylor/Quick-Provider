package com.desserttowersprogramming.quickprovider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.StringDef;

/**
 * Created by ryantaylor on 2/26/16.
 */
public class QuickConnection {

    private Context context;
    private String authority;

    private static QueryBuilder queryBuilder;

    public static final String DESCENDING = " DESC";
    public static final String ASCENDING = " ASC";

    @Retention(RetentionPolicy.CLASS)
    @StringDef({DESCENDING, ASCENDING})
    @interface Sort {
    }

    public QuickConnection(Context context, String authority) {
        this.context = context;
        this.authority = authority;
    }

    public QueryBuilder query(Uri uri) {
        return new QueryBuilder(context.getContentResolver(), uri);
    }

    public void saveItem(Table item) {
        if (item.getId() == Table.NULL_ID)
            context.getContentResolver().insert(item.getContentUri(), item.getContentValues());
        else
            context.getContentResolver().update(item.getContentUri(), item.getContentValues(), Table.WHERE_ID_EQUALS, new String[]{Long.toString(item.getId())});
    }

    public void saveAll(List<? extends Table> items) {
        ArrayList<ContentProviderOperation> insertContentProviderOperations = new ArrayList<>();
        ArrayList<ContentProviderOperation> updateContentProviderOperations = new ArrayList<>();

        for (Table item : items) {
            if (item.getId() == Table.NULL_ID)
                insertContentProviderOperations.add(ContentProviderOperation.newInsert(item.getContentUri()).withValues(item.getContentValues()).build());
            else
                updateContentProviderOperations.add(ContentProviderOperation.newUpdate(item.getContentUri()).withValues(item.getContentValues()).withSelection(Table.WHERE_ID_EQUALS, new String[]{Long.toString(item.getId())}).build());
        }

        try {
            if (insertContentProviderOperations.size() > 0)
                context.getContentResolver().applyBatch(authority, insertContentProviderOperations);
            if (updateContentProviderOperations.size() > 0)
                context.getContentResolver().applyBatch(authority, updateContentProviderOperations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class QueryBuilder {
        private ContentResolver contentResolver;
        private Uri uri;

        private String[] columns;

        private List<String> selectValues;

        private StringBuilder select;
        private String sortOrder;

        private QueryBuilder(ContentResolver contentResolver, Uri uri) {
            this.contentResolver = contentResolver;
            this.uri = uri;

            select = new StringBuilder();
            selectValues = new ArrayList<>();
        }

        public QueryBuilder columns(String... columns) {
            this.columns = columns;

            return this;
        }

        public QueryBuilder allColumns() {
            columns = null;

            return this;
        }

        public QueryBuilder count() {
            columns = new String[]{"count(*)"};

            return this;
        }

        public QueryBuilder whereEquals(String column, String value) {
            return appendSelect(column, "=?", value);
        }

        public QueryBuilder whereGreaterThan(String column, String value) {
            return appendSelect(column, ">", value);
        }

        public QueryBuilder whereLessThan(String column, String value) {
            return appendSelect(column, "<", value);
        }

        public QueryBuilder whereUuidEquals(String uuid) {
            return appendSelect(Table.UUID, "=?", uuid);
        }

        public QueryBuilder appendSelect(String column, String operator, String value) {
            select.append(column);
            select.append(operator);

            selectValues.add(value);

            return this;
        }

        public QueryBuilder and() {
            select.append(" AND ");

            return this;
        }

        public QueryBuilder or() {
            select.append(" OR ");

            return this;
        }

        public QueryBuilder startContain() {
            select.append("(");

            return this;
        }

        public QueryBuilder endContain() {
            select.append(")");

            return this;
        }

        public QueryBuilder sortOrder(String column, @Sort String sort) {
            sortOrder = column + sort;

            return this;
        }

        public Cursor build() {
            String selectStatement = select == null ? null : select.toString();
            String[] selectValuesArray = selectValues == null ? null : selectValues.toArray(new String[selectValues.size()]);

            return contentResolver.query(uri, columns, selectStatement, selectValuesArray, sortOrder);
        }

        public Cursor getFirst() {
            Cursor cursor = build();

            if (cursor != null)
                cursor.moveToFirst();

            return cursor;
        }

        public void delete() {
            String selectStatement = select == null ? null : select.toString();
            String[] selectValuesArray = selectValues == null ? null : selectValues.toArray(new String[selectValues.size()]);

            contentResolver.delete(uri, selectStatement, selectValuesArray);
        }
    }
}
