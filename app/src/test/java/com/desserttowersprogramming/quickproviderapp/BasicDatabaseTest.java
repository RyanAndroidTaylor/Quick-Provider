package com.desserttowersprogramming.quickproviderapp;

import com.desserttowersprogramming.quickprovider.QuickConnection;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;

import android.app.Application;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ryantaylor on 4/14/16.
 */
public class BasicDatabaseTest {

    Application application;
    QuickConnection quickConnection;

    public void setup() {
        MainProvider provider = new MainProvider();
        provider.onCreate();
        ShadowContentResolver.registerProvider(MainProvider.AUTHORITY, provider);
        application = RuntimeEnvironment.application;
        quickConnection = new QuickConnection(application, MainProvider.AUTHORITY);
    }

    protected void assertTestTableEquals(TestTable expected, TestTable actual) {
        assertEquals(expected.getUuid(), actual.getUuid());
        assertEquals(expected.getSomeText(), actual.getSomeText());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSomeNumber(), actual.getSomeNumber());
        assertEquals(expected.getAmount(), actual.getAmount());
    }
}
