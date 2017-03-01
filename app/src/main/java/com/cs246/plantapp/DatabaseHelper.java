package com.cs246.plantapp;

/**
 * Created by austingolding on 2/20/17.
 */

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DatabaseHelper extends SQLiteAssetHelper {
    private static final String name = "PlantsApp2.db";
    private static final int version = 1;

    public DatabaseHelper(Context context) {
        super(context, name, null, version);
    }
}


