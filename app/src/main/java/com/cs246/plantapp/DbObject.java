package com.cs246.plantapp;

/**
 * Created by austingolding on 2/20/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * The type Db object.
 */
public class DbObject {
    private static DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    /**
     * Instantiates a new Db object.
     *
     * @param context the context
     */
    public DbObject(Context context) {
        dbHelper = new DatabaseHelper(context);
        this.db = dbHelper.getReadableDatabase();
    }

    /**
     * Gets db connection.
     *
     * @return the db connection
     */
    public SQLiteDatabase getDbConnection() {
        return this.db;
    }

    /**
     * Close db connection.
     */
    public void closeDbConnection() {
        if (this.db != null) {
            this.db.close();
        }
    }
}
