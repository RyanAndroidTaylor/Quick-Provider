package com.desserttowersprogramming.quickproviderapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * Created by ryantaylor on 4/14/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class QuickConnectionTest extends BasicDatabaseTest {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void save() {
        quickConnection.saveItem(TestData.TEST_TABLE_ONE);

        Cursor cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .whereUuidEquals(TestData.TEST_TABLE_ONE_UUID)
                .getFirst();
        TestTable testTableOne = TestTable.getItem(cursor);

        assertEquals(1L, testTableOne.getId());
        assertEquals(TestData.TEST_TABLE_ONE_UUID, testTableOne.getUuid());
        assertEquals(TestData.TEST_TABLE_ONE_TEXT, testTableOne.getSomeText());
        assertEquals(TestData.TEST_TABLE_ONE_NAME, testTableOne.getName());
        assertEquals(TestData.TEST_TABLE_ONE_NUMBER, testTableOne.getSomeNumber());
        assertEquals(TestData.TEST_TABLE_ONE_AMOUNT, testTableOne.getAmount());

        // update
        String updatedText = "updated text";
        String updatedName = "updated name";
        int updatedNumber = 1309;
        int updatedAmount = 14012;

        testTableOne.setSomeText(updatedText);
        testTableOne.setName(updatedName);
        testTableOne.setSomeNumber(updatedNumber);
        testTableOne.setAmount(updatedAmount);

        quickConnection.saveItem(testTableOne);

        cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .whereUuidEquals(TestData.TEST_TABLE_ONE_UUID)
                .getFirst();
        TestTable updatedTestTable = TestTable.getItem(cursor);

        assertEquals(testTableOne.getId(), updatedTestTable.getId());
        assertEquals(testTableOne.getUuid(), updatedTestTable.getUuid());
        assertEquals(updatedText, updatedTestTable.getSomeText());
        assertEquals(updatedName, updatedTestTable.getName());
        assertEquals(updatedNumber, updatedTestTable.getSomeNumber());
        assertEquals(updatedAmount, updatedTestTable.getAmount());

        // Multiple entries
        quickConnection.saveItem(TestData.TEST_TABLE_TWO);

        cursor = quickConnection.query(TestTable.CONTENT_URI)
                .count()
                .getFirst();

        assertEquals(2, cursor.getInt(0));
    }

    @Test
    public void saveAll() {
        List<TestTable> testTables = new ArrayList<>();
        testTables.add(TestData.TEST_TABLE_ONE);
        testTables.add(TestData.TEST_TABLE_TWO);

        quickConnection.saveAll(testTables);

        Cursor cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .build();

        List<TestTable> loadedTestTables = TestTable.getItems(cursor);

        TestTable testTableOne = loadedTestTables.get(0);
        TestTable testTableTwo = loadedTestTables.get(1);

        assertTestTableEquals(TestData.TEST_TABLE_ONE, testTableOne);
        assertTestTableEquals(TestData.TEST_TABLE_TWO, testTableTwo);

        // Update
        String updatedNameOne = "updated name one";
        String updatedNameTwo = "updated name two";

        testTableOne.setName(updatedNameOne);
        testTableTwo.setName(updatedNameTwo);

        testTables.clear();
        testTables.add(testTableOne);
        testTables.add(testTableTwo);

        quickConnection.saveAll(testTables);

        cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .build();

        List<TestTable> updatedTestTables = TestTable.getItems(cursor);

        TestTable updatedTestTableOne = updatedTestTables.get(0);
        TestTable updatedTestTableTwo = updatedTestTables.get(1);

        assertTestTableEquals(testTableOne, updatedTestTableOne);
        assertTestTableEquals(testTableTwo, updatedTestTableTwo);
    }

    @Test
    public void Columns() {
        quickConnection.saveItem(TestData.TEST_TABLE_ONE);
        quickConnection.saveItem(TestData.TEST_TABLE_TWO);
        quickConnection.saveItem(TestData.TEST_TABLE_THREE);

        Cursor cursor = quickConnection.query(TestTable.CONTENT_URI)
                .columns(TestTable.UUID)
                .whereEquals(TestTable.UUID, TestData.TEST_TABLE_ONE_UUID)
                .getFirst();

        String tableOneUuid  = cursor.getString(cursor.getColumnIndex(TestTable.UUID));

        assertEquals(TestData.TEST_TABLE_ONE_UUID, tableOneUuid);

        assertEquals(-1, cursor.getColumnIndex(TestTable.SOME_TEXT));
        assertEquals(-1, cursor.getColumnIndex(TestTable.NAME));
        assertEquals(-1, cursor.getColumnIndex(TestTable.NOT_NULL_TEXT));
        assertEquals(-1, cursor.getColumnIndex(TestTable.SOME_NUMBER));
        assertEquals(-1, cursor.getColumnIndex(TestTable.AMOUNT));

        // Multiple columns
        cursor = quickConnection.query(TestTable.CONTENT_URI)
                .columns(TestTable.SOME_TEXT, TestTable.SOME_NUMBER)
                .whereEquals(TestTable.SOME_TEXT, TestData.TEST_TABLE_THREE_TEXT)
                .getFirst();

        String tableThreeText = cursor.getString(cursor.getColumnIndex(TestTable.SOME_TEXT));
        int tableThreeNumber = cursor.getInt(cursor.getColumnIndex(TestTable.SOME_NUMBER));

        assertEquals(TestData.TEST_TABLE_THREE_TEXT, tableThreeText);
        assertEquals(TestData.TEST_TABLE_THREE_NUMBER, tableThreeNumber);

        assertEquals(-1, cursor.getColumnIndex(TestTable.UUID));
        assertEquals(-1, cursor.getColumnIndex(TestTable.NAME));
        assertEquals(-1, cursor.getColumnIndex(TestTable.NOT_NULL_TEXT));
        assertEquals(-1, cursor.getColumnIndex(TestTable.AMOUNT));
    }

    @Test
    public void allColumns() {
        quickConnection.saveItem(TestData.TEST_TABLE_ONE);
        quickConnection.saveItem(TestData.TEST_TABLE_TWO);
        quickConnection.saveItem(TestData.TEST_TABLE_THREE);

        Cursor cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .whereEquals(TestTable.UUID, TestData.TEST_TABLE_TWO_UUID)
                .getFirst();

        assertEquals(TestData.TEST_TABLE_TWO_UUID, cursor.getString(cursor.getColumnIndex(TestTable.UUID)));
        assertEquals(TestData.TEST_TABLE_TWO_TEXT, cursor.getString(cursor.getColumnIndex(TestTable.SOME_TEXT)));
        assertEquals(TestData.TEST_TABLE_TWO_NAME, cursor.getString(cursor.getColumnIndex(TestTable.NAME)));
        assertEquals(TestData.TEST_TABLE_TWO_NOT_NULL_TEXT, cursor.getString(cursor.getColumnIndex(TestTable.NOT_NULL_TEXT)));
        assertEquals(TestData.TEST_TABLE_TWO_NUMBER, cursor.getInt(cursor.getColumnIndex(TestTable.SOME_NUMBER)));
        assertEquals(TestData.TEST_TABLE_TWO_AMOUNT, cursor.getInt(cursor.getColumnIndex(TestTable.AMOUNT)));
    }

    @Test
    public void count() {
        quickConnection.saveItem(TestData.TEST_TABLE_ONE);
        quickConnection.saveItem(TestData.TEST_TABLE_TWO);
        quickConnection.saveItem(TestData.TEST_TABLE_THREE);

        Cursor cursor = quickConnection.query(TestTable.CONTENT_URI)
                .count()
                .getFirst();

        assertEquals(3, cursor.getInt(0));
    }

    @Test
    public void delete() {
        quickConnection.saveItem(TestData.TEST_TABLE_ONE);
        quickConnection.saveItem(TestData.TEST_TABLE_TWO);
        quickConnection.saveItem(TestData.TEST_TABLE_THREE);

        Cursor cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .whereUuidEquals(TestData.TEST_TABLE_ONE_UUID)
                .getFirst();

        assertEquals(TestData.TEST_TABLE_ONE_UUID, cursor.getString(cursor.getColumnIndex(TestTable.UUID)));

        quickConnection.query(TestTable.CONTENT_URI)
                .whereUuidEquals(TestData.TEST_TABLE_ONE_UUID)
                .delete();

        cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .whereUuidEquals(TestData.TEST_TABLE_ONE_UUID)
                .build();

        assertFalse(cursor.moveToFirst());
    }
}
