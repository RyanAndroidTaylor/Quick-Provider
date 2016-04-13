package com.desserttowersprogramming.quickprovider;

public class QuickJoin {
    private final String LEFT_JOIN = " LEFT JOIN ";
    private final String INNER_JOIN = " INNER JOIN ";
    private final String ON = " ON ";
    private final String PERIOD = ".";
    private final String EQUALS = "=";

    private String mSecondTableJoin = LEFT_JOIN;
    private String mThirdTableJoin = LEFT_JOIN;

    private StringBuilder mJoinString;

    private String mFirstTable;
    private String mSecondTable;
    private String mThirdTable;

    private String mConnectionToSecondTable;
    private String mConnectionToThirdTable;
    private String mSecondColumn;
    private String mThirdColumn;

    private QuickJoin() {
        mJoinString = new StringBuilder();
    }

    public static QuickJoin Builder() {
        return new QuickJoin();
    }

    /**
     * Sets the type of join used between the first table and second table on a {@link #manyToManyJoin()}.
     * By default a left join is used.
     * @param secondTableJoin The type of join to be used. Use either {@link #INNER_JOIN} or {@link #LEFT_JOIN}
     */
    public void setSecondTableJoin(String secondTableJoin) {
        mSecondTableJoin = secondTableJoin;
    }

    /**
     * Sets the type of join used between the first table and third table on a {@link #manyToManyJoin()}.
     * By default a left join is used.
     * @param thirdTableJoin The type of join to be used. Use either {@link #INNER_JOIN} or {@link #LEFT_JOIN}
     */
    public void setThirdTableJoin(String thirdTableJoin) {
        mThirdTableJoin = thirdTableJoin;
    }

    /**
     * Sets the first tables name and the columns names to be used when connecting to the other tables.
     * This method is used to set up the first tables data when doing a normal {@link #leftJoin()} or {@link #innerJoin()} join.
     * The first tables name will also be the table named used when setting the table in the query
     * @see android.database.sqlite.SQLiteQueryBuilder#setTables(String)
     *
     * @param tableName The of the table to be used
     * @param secondTableConnection The name of the column used to connect to the second table
     * @return this so methods can be chained
     */
    public QuickJoin setFirstTableData(String tableName, String secondTableConnection) {
        mFirstTable = tableName;
        mConnectionToSecondTable = secondTableConnection;

        return this;
    }

    /**
     * Sets the first tables name and the columns names to be used when connecting to the other tables.
     * This method is used to set up the first tables data when doing a {@link #manyToManyJoin()}.
     * The first tables name will also be the table named used when setting the table in the query
     * @see android.database.sqlite.SQLiteQueryBuilder#setTables(String)
     *
     * @param tableName The of the table to be used
     * @param secondTableConnection The name of the column used to connect to the second table
     * @param thirdTableConnection The name of the column used to connect to the third table
     * @return this so methods can be chained
     */
    public QuickJoin setFirstTableData(String tableName, String secondTableConnection, String thirdTableConnection) {
        mFirstTable = tableName;
        mConnectionToSecondTable = secondTableConnection;
        mConnectionToThirdTable = thirdTableConnection;

        return this;
    }

    /**
     * Sets the seconds tables table name and column name. This table is used in {@link #leftJoin()}, {@link #innerJoin()} and {@link #manyToManyJoin()}
     * @param tableName The name of the table to be used in the join
     * @param columnName The name of the column to be used in the join
     * @return this so methods can be chained
     */
    public QuickJoin setSecondTableData(String tableName, String columnName) {
        mSecondTable = tableName;
        mSecondColumn = columnName;

        return this;
    }

    /**
     * Sets the third tables table name and column name. This table is used in {@link #manyToManyJoin()}
     * @param tableName
     * @param columnName
     * @return
     */
    public QuickJoin setThirdTableData(String tableName, String columnName) {
        mThirdTable = tableName;
        mThirdColumn = columnName;

        return this;
    }

    /**
     * Creates a left join between the first table and second table using the {@link #mConnectionToSecondTable} and {@link #mSecondColumn}
     * @return this so methods can be chained
     */
    public QuickJoin leftJoin() {
        mJoinString.append(mFirstTable);
        mJoinString.append(LEFT_JOIN);
        mJoinString.append(mSecondTable);

        connect(mFirstTable, mConnectionToSecondTable, mSecondTable, mSecondColumn);

        return this;
    }

    /**
     * Creates a inner join between the first table and second table using the {@link #mConnectionToSecondTable} and {@link #mSecondColumn}
     * @return this so methods can be chained
     */
    public QuickJoin innerJoin() {
        mJoinString.append(mFirstTable);
        mJoinString.append(INNER_JOIN);
        mJoinString.append(mSecondTable);

        connect(mFirstTable, mConnectionToSecondTable, mSecondTable, mSecondColumn);

        return this;
    }

    /**
     * Allows you to append a left join onto any normal join.
     * You must make sure that either the fist or second table name is the name of a table already in the join statement.
     * @param firstTable The name of the first table to be joined.
     * @param firstColumn The name of the column to be used from the first table.
     * @param secondTable The name of the second table to be joined.
     * @param secondColumn The name of the column to be used from the second table.
     * @return this so methods can be chained
     */
    public QuickJoin appendLeftJoin(String firstTable, String firstColumn, String secondTable, String secondColumn) {
        mJoinString.append(LEFT_JOIN);
        mJoinString.append(secondTable);

        connect(firstTable, firstColumn, secondTable, secondColumn);

        return this;
    }

    /**
     * Allows you to append a inner join onto any normal join.
     * You must make sure that either the fist or second table name is the name of a table already in the join statement.
     * @param firstTable The name of the first table to be joined.
     * @param firstColumn The name of the column to be used from the first table.
     * @param secondTable The name of the second table to be joined.
     * @param secondColumn The name of the column to be used from the second table.
     * @return this so methods can be chained
     */
    public QuickJoin appendInnerJoin(String firstTable, String firstColumn, String secondTable, String secondColumn) {
        mJoinString.append(INNER_JOIN);
        mJoinString.append(secondTable);

        connect(firstTable, firstColumn, secondTable, secondColumn);

        return this;
    }

    /**
     * Creates a many to many join between the first table, second table and third table using the {@link #mConnectionToSecondTable} + {@link #mSecondColumn}
     * and {@link #mConnectionToThirdTable} + {@link #mThirdColumn}
     * @return this so methods can be chained
     */
    public QuickJoin manyToManyJoin() {
        mJoinString.append(mFirstTable);
        mJoinString.append(mSecondTableJoin);
        mJoinString.append(mSecondTable);

        connect(mFirstTable, mConnectionToSecondTable, mSecondTable, mSecondColumn);

        mJoinString.append(mThirdTableJoin);
        mJoinString.append(mThirdTable);

        connect(mFirstTable, mConnectionToThirdTable, mThirdTable, mThirdColumn);

        return this;
    }

    /**
     * Adds a connection between columnOne and columnTwo in the join.
     * @param tableOne The table name of columnOne
     * @param tableTwo The table name of columnTwo
     */
    private void connect(String tableOne, String columnOne, String tableTwo, String columnTwo) {
        mJoinString.append(ON);
        mJoinString.append(tableOne);
        mJoinString.append(PERIOD);
        mJoinString.append(columnOne);
        mJoinString.append(EQUALS);
        mJoinString.append(tableTwo);
        mJoinString.append(PERIOD);
        mJoinString.append(columnTwo);
    }

    /**
     * @return A string representing the join that was built.
     */
    public String build() {
        return mJoinString.toString();
    }
}
