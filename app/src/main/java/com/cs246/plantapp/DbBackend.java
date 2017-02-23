package com.cs246.plantapp;

/**
 * Created by austingolding on 2/20/17.
 */

import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class DbBackend extends DbObject{
    public DbBackend(Context context) {
        super(context);
    }
    public List<PlantsObject> searchDictionaryWords(String searchWord){
        List<PlantsObject> mItems = new ArrayList<PlantsObject>();
        String query = "Select * from dictionary where word like " + "'%" + searchWord + "%'";
        Cursor cursor = this.getDbConnection().rawQuery(query, null); ArrayList<String> wordTerms = new ArrayList<String>(); if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                mItems.add(new PlantsObject()); }while(cursor.moveToNext());
        }
        cursor.close();
        return mItems;
    }
}
