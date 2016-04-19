package com.desserttowersprogramming.quickproviderapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.database.Cursor;
import android.util.Log;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by ryantaylor on 4/14/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class QuickTableTest extends BasicDatabaseTest {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void testAutoIncrementingId() {
        quickConnection.saveItem(TestData.TEST_TABLE_ONE);
        quickConnection.saveItem(TestData.TEST_TABLE_TWO);
        quickConnection.saveItem(TestData.TEST_TABLE_THREE);

        // TestTable 1
        Cursor cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .whereUuidEquals(TestData.TEST_TABLE_ONE_UUID)
                .getFirst();

        TestTable testTable = TestTable.getItem(cursor);

        assertEquals(1L, testTable.getId());

        // TestTable 2
        cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .whereUuidEquals(TestData.TEST_TABLE_TWO_UUID)
                .getFirst();

        testTable = TestTable.getItem(cursor);

        assertEquals(2L, testTable.getId());

        // TestTable3
        cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .whereUuidEquals(TestData.TEST_TABLE_THREE_UUID)
                .getFirst();

        testTable = TestTable.getItem(cursor);

        assertEquals(3L, testTable.getId());
    }

    @Test
    public void uuidNotNull() {
        TestTable testTable = new TestTable(null, "text", "not null", "name",  1, 1);

        quickConnection.saveItem(testTable);

        Cursor cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .build();

        assertFalse(cursor.moveToFirst());
    }

    @Test
    public void uniqueUuid() {
        quickConnection.saveItem(TestData.TEST_TABLE_ONE);

        Cursor cursor = quickConnection.query(TestTable.CONTENT_URI)
                .count()
                .getFirst();

        int count = cursor.getInt(0);

        assertEquals(1, count);

        quickConnection.saveItem(TestData.TEST_TABLE_ONE);

        cursor = quickConnection.query(TestTable.CONTENT_URI)
                .count()
                .getFirst();

        count = cursor.getInt(0);
        assertEquals(1, count);
    }

    @Test
    public void notNullConstraint() {
        TestTable testTable = new TestTable("uuid", "some text", null, "name", 1, 1);

        quickConnection.saveItem(testTable);

        Cursor cursor = quickConnection.query(TestTable.CONTENT_URI)
                .allColumns()
                .build();

        assertFalse(cursor.moveToFirst());
    }

    @Test
    public void columnForeignKeyConstraint() {
        quickConnection.saveItem(TestData.TEST_TABLE_ONE);

        SubTable subTable = new SubTable("uuid", "bad foreign key");

        quickConnection.saveItem(subTable);

        Cursor cursor = quickConnection.query(SubTable.CONTENT_URI)
                .allColumns()
                .build();

        assertFalse(cursor.moveToFirst());

        subTable.setForeignKeyColumn(TestData.TEST_TABLE_ONE_UUID);

        quickConnection.saveItem(subTable);

        cursor = quickConnection.query(SubTable.CONTENT_URI)
                .allColumns()
                .build();

        assertTrue(cursor.moveToFirst());
    }
}
