package com.desserttowersprogramming.quickprovider;

import android.content.ContentValues;
import android.net.Uri;

public interface Table {
    String ID = "_id";
    String UUID = "UUID";

    long NULL_ID = -1;

    long getId();
    String getUuid();
    ContentValues getContentValues();
    Uri getContentUri();

    String WHERE_ID_EQUALS = ID + "=?";
    String WHERE_UUID_EQUALS = UUID + "=?";
}
