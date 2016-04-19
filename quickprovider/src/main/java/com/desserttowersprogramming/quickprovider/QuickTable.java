package com.desserttowersprogramming.quickprovider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import android.support.annotation.StringDef;

public class QuickTable {
    // Types
    public static final String DB_INTEGER = "INTEGER";
    public static final String DB_TEXT = "TEXT";
    public static final String DB_BOOLEAN = "INTEGER";
    public static final String DB_BLOB = "BLOB";

    @Retention(RetentionPolicy.CLASS)
    @StringDef({DB_INTEGER, DB_TEXT, DB_BOOLEAN, DB_BLOB})
    public @interface TableType {}

    //Constrains
    public static final String NOT_NULL = "NOT NULL";
    public static final String REFERENCES = "REFERENCES ";
    public static final String UNIQUE = "UNIQUE";

    @Retention(RetentionPolicy.CLASS)
    @StringDef({NOT_NULL, REFERENCES, UNIQUE})
    public @interface TableConstraint {}

    private final String SPACE = " ";
    private final String PERIOD = ".";
    private final String COMMA = ",";

    private StringBuilder createString;

    private List<String> foreignKeys;

    private String currentTable;

    private ArrayList<String> columns;

    private boolean mClosed = true;

    private static QuickTable mTableBuild;

    private QuickTable() {
        // Private constructor so this class cannot be instantiated
    }

    public static QuickTable getInstance() {
        if (mTableBuild == null)
            mTableBuild = new QuickTable();

        return mTableBuild;
    }

    public String open(String tableName) {
        if (!mClosed)
            throw new IllegalStateException("Table builder must be closed before opening a new one");

        mClosed = false;
        createString = new StringBuilder();
        columns = new ArrayList<>();
        foreignKeys = new ArrayList<>();
        currentTable = tableName;

        createString.append("CREATE TABLE ");
        createString.append(currentTable);
        createString.append(" ( ");
        createString.append(Table.ID);
        createString.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        createString.append(Table.UUID);
        createString.append(SPACE);
        createString.append(DB_TEXT);
        createString.append(SPACE);
        createString.append(NOT_NULL);
        createString.append(SPACE);
        createString.append(UNIQUE);

        columns.add(currentTable + PERIOD + Table.ID);
        columns.add(currentTable + PERIOD + Table.UUID);

        return tableName;
    }

    public String openWithUuidForeignKeyRestraint(String tableName, String referenceTable) {
        if (!mClosed)
            throw new IllegalStateException("Table builder must be closed before opening a new one");

        mClosed = false;
        createString = new StringBuilder();
        columns = new ArrayList<>();
        currentTable = tableName;

        createString.append("CREATE TABLE ");
        createString.append(currentTable);
        createString.append(" ( ");
        createString.append(Table.ID);
        createString.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        createString.append(Table.UUID);
        createString.append(SPACE);
        createString.append(DB_TEXT);
        createString.append(SPACE);
        createString.append(NOT_NULL);
        createString.append(SPACE);
        createString.append(UNIQUE);
        createString.append(" REFERENCES ");
        createString.append(referenceTable);
        createString.append("(");
        createString.append(Table.UUID);
        createString.append(")");

        columns.add(currentTable + PERIOD + Table.ID);
        columns.add(currentTable + PERIOD + Table.UUID);

        return tableName;
    }

    public ColumnBuilder buildTextColumn(String columnName) {
        return new ColumnBuilder().appendText(columnName);
    }

    public ColumnBuilder buildIntColumn(String columnName) {
        return new ColumnBuilder().appendInt(columnName);
    }

    public ColumnBuilder buildBooleanColumn(String columnName) {
        return new ColumnBuilder().appendBoolean(columnName);
    }

    public ColumnBuilder buildBlobColumn(String columnName) {
        return new ColumnBuilder().appendBlob(columnName);
    }

    public void end() {
        createString.append(" ");

        for (String foreignKey : foreignKeys) {
            createString.append(COMMA);
            createString.append(SPACE);
            createString.append(foreignKey);
        }

        createString.append(")");
        mClosed = true;
    }

    public String retrieveDropString() {
        if (!mClosed)
            end();

        return currentTable + " DROP TABLE IF EXISTS ";
    }

    public String retrieveCreateString() {
        if (!mClosed)
            end();

        return createString.toString();
    }

    public class ColumnBuilder {
        private String columnName;

        @TableType
        private String type;

        private List<String> constraints;

        public ColumnBuilder() {
            constraints = new ArrayList<>();
        }

        public ColumnBuilder appendText(String columnName) {
            this.columnName = columnName;
            type = DB_TEXT;

            return this;
        }

        public ColumnBuilder appendInt(String columnName) {
            this.columnName = columnName;

            type = DB_INTEGER;

            return this;
        }

        public ColumnBuilder appendBoolean(String columnName) {
            this.columnName = columnName;

            type = DB_BOOLEAN;

            return this;
        }

        public ColumnBuilder appendBlob(String columnName) {
            this.columnName = columnName;

            type = DB_BLOB;

            return this;
        }

        public ColumnBuilder notNull() {
            constraints.add(NOT_NULL);

            return this;
        }

        public ColumnBuilder foreignKey(String tableName, String columnName) {
            foreignKeys.add("FOREIGN KEY (" + this.columnName + ") " + REFERENCES + tableName + "(" + columnName + ")");

            return this;
        }

        public String build() {
            createString.append(COMMA);
            createString.append(columnName);
            createString.append(SPACE);
            createString.append(type);

            for (String constraint : constraints) {
                createString.append(SPACE);
                createString.append(constraint);
            }

            columns.add(currentTable + PERIOD + columnName);

            return columnName;
        }
    }
}
