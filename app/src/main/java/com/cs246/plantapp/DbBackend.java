package com.cs246.plantapp;

/**
 * Created by austingolding on 2/20/17.
 */

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class DbBackend extends DbObject {
    public DbBackend(Context context) {
        super(context);
    }

    public ArrayList<PlantsObject> searchDictionaryWords(String searchWord) {
        ArrayList<PlantsObject> mItems = new ArrayList<>();
        String query = "Select * from MyNewTable where Name like " + "'%" + searchWord + "%'";
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                mItems.add(new PlantsObject(cursor.getString(cursor.getColumnIndex("Name")), cursor.getString(cursor.getColumnIndex("Soil pH Requirements")), cursor.getString(cursor.getColumnIndex("Category")),  cursor.getString(cursor.getColumnIndex("Spacing")), cursor.getString(cursor.getColumnIndex("Sun Exposure")), cursor.getString(cursor.getColumnIndex("Water Requirements")), cursor.getString(cursor.getColumnIndex("image"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("Plants", mItems.get(0).getName());
        return mItems;
    }
}
