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
    public ArrayList<PlantsObject> searchDictionaryWords(String searchWord){
        ArrayList<PlantsObject> mItems = new ArrayList<>();
        String query = "Select * from MyNewTable where Name like " + "'%" + searchWord + "%'";
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                mItems.add(new PlantsObject(cursor.getString(cursor.getColumnIndex("Name"))));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return mItems;
    }
}
