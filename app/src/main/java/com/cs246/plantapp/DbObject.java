package com.cs246.plantapp;

/**
 * Created by austingolding on 2/20/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * The type Db object.
 */
class DbObject {
    private final SQLiteDatabase db;

    /**
     * Instantiates a new Db object.
     *
     * @param context the context
     */
    DbObject(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        this.db = dbHelper.getReadableDatabase();
    }

    /**
     * Gets db connection.
     *
     * @return the db connection
     */
    SQLiteDatabase getDbConnection() {
        return this.db;
    }

    /**
     * Close db connection.
     */
    void closeDbConnection() {
        if (this.db != null) {
            this.db.close();
        }
    }
}
