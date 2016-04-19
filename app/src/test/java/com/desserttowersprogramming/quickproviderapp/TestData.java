package com.desserttowersprogramming.quickproviderapp;

/**
 * Created by ryantaylor on 4/14/16.
 */
public class TestData {
    public static final String TEST_TABLE_ONE_UUID = "test table one uuid";
    public static final String TEST_TABLE_ONE_TEXT = "test table one text";
    public static final String TEST_TABLE_ONE_NOT_NULL_TEXT = "test table one not null text";
    public static final String TEST_TABLE_ONE_NAME = "test table one name";
    public static final int TEST_TABLE_ONE_NUMBER = 10;
    public static final int TEST_TABLE_ONE_AMOUNT = 110;

    public static final TestTable TEST_TABLE_ONE = new TestTable(TEST_TABLE_ONE_UUID, TEST_TABLE_ONE_TEXT, TEST_TABLE_ONE_NOT_NULL_TEXT, TEST_TABLE_ONE_NAME, TEST_TABLE_ONE_NUMBER, TEST_TABLE_ONE_AMOUNT);

    public static final String TEST_TABLE_TWO_UUID = "test table two uuid";
    public static final String TEST_TABLE_TWO_TEXT = "test table two text";
    public static final String TEST_TABLE_TWO_NOT_NULL_TEXT = "test table two not null text";
    public static final String TEST_TABLE_TWO_NAME = "test table two name";
    public static final int TEST_TABLE_TWO_NUMBER = 20;
    public static final int TEST_TABLE_TWO_AMOUNT = 220;

    public static final TestTable TEST_TABLE_TWO = new TestTable(TEST_TABLE_TWO_UUID, TEST_TABLE_TWO_TEXT, TEST_TABLE_TWO_NOT_NULL_TEXT, TEST_TABLE_TWO_NAME, TEST_TABLE_TWO_NUMBER, TEST_TABLE_TWO_AMOUNT);

    public static final String TEST_TABLE_THREE_UUID = "test table three uuid";
    public static final String TEST_TABLE_THREE_TEXT = "test table three text";
    public static final String TEST_TABLE_THREE_NOT_NULL_TEXT = "test table three not null text";
    public static final String TEST_TABLE_THREE_NAME = "test table three name";
    public static final int TEST_TABLE_THREE_NUMBER = 30;
    public static final int TEST_TABLE_THREE_AMOUNT = 330;

    public static final TestTable TEST_TABLE_THREE = new TestTable(TEST_TABLE_THREE_UUID, TEST_TABLE_THREE_TEXT, TEST_TABLE_THREE_NOT_NULL_TEXT, TEST_TABLE_THREE_NAME, TEST_TABLE_THREE_NUMBER, TEST_TABLE_THREE_AMOUNT);
}
