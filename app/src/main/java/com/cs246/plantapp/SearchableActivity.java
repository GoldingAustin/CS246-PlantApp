package com.cs246.plantapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by austingolding on 2/2/17.
 */
public class SearchableActivity extends AppCompatActivity {

    private ListView listView;
    private DbBackend databaseObject;
    PlantsAdapter plantsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        databaseObject = new DbBackend(SearchableActivity.this);
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("test", query);
            findSearch(query);

        }
    }

    protected void findSearch(String query) {
        ArrayList<PlantsObject> dictionaryObject = databaseObject.searchDictionaryWords(query);
        plantsAdapter = new PlantsAdapter(getApplicationContext(), dictionaryObject);
        ListView view = (ListView) findViewById(R.id.listView);
        view.setAdapter(plantsAdapter);
    }
}