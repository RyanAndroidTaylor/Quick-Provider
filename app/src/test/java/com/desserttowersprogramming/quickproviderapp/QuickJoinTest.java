package com.desserttowersprogramming.quickproviderapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.database.Cursor;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ryantaylor on 4/19/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class QuickJoinTest extends BasicDatabaseTest {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void leftJoin() {
        quickConnection.saveItem(TestData.TEST_TABLE_ONE);
        quickConnection.saveItem(new SubTable(TestData.TEST_TABLE_ONE_UUID, "Some data"));

        Cursor cursor = quickConnection.query(MainProvider.LEFT_JOIN_URI)
                .allColumns()
                .getFirst();

        String uuid = cursor.getString(cursor.getColumnIndex(TestTable.UUID));
        String someText = cursor.getString(cursor.getColumnIndex(TestTable.SOME_TEXT));
        String data = cursor.getString(cursor.getColumnIndex(SubTable.DATA));

        assertEquals(TestData.TEST_TABLE_ONE_UUID, uuid);
        assertEquals(TestData.TEST_TABLE_ONE_TEXT, someText);
        assertEquals("Some data", data);
    }

    @Test
    public void manyToManyJoin() {
        quickConnection.saveItem(TestData.TEST_TABLE_ONE);
        quickConnection.saveItem(new SubTable(TestData.TEST_TABLE_ONE_UUID, "some data"));
        quickConnection.saveItem(new SubTestTable("subTestTable uuid", TestData.TEST_TABLE_ONE_UUID, TestData.TEST_TABLE_ONE_UUID, "behind you!!!"));

        Cursor cursor = quickConnection.query(MainProvider.MANY_TO_MANY_URI)
                .allColumns()
                .getFirst();

        String someText = cursor.getString(cursor.getColumnIndex(TestTable.SOME_TEXT));
        String data = cursor.getString(cursor.getColumnIndex(SubTable.DATA));
        String location = cursor.getString(cursor.getColumnIndex(SubTestTable.LOCATION));

        assertEquals(TestData.TEST_TABLE_ONE_TEXT, someText);
        assertEquals("some data", data);
        assertEquals("behind you!!!", location);
    }
}
