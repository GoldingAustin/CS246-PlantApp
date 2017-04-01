package com.cs246.plantapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by austingolding on 2/2/17.
 */
public class SearchableActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView view = (ListView) findViewById(R.id.listView);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlantsObject plant = (PlantsObject) parent.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), AddPlant.class);
                Gson gson = new Gson();
                String json = gson.toJson(plant);
                i.putExtra("searchPlant", json);
                startActivity(i);
                finish();
                Log.d("Selected Plant: ", plant.toString());
            }
        });
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);

        }

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_login);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("Search", query);
            findSearch(query);

        }
    }

    /**
     * Find search.
     *
     * @param query the query
     */
    private void findSearch(String query) {
        DbBackend databaseObject = new DbBackend(SearchableActivity.this);
        ArrayList<PlantsObject> dictionaryObject = databaseObject.searchDictionaryWords(query);
        /*
      The Plants adapter.
     */
        PlantsAdapter plantsAdapter = new PlantsAdapter(getApplicationContext(), dictionaryObject);
        Log.d("Results", String.valueOf(plantsAdapter.getCount()));
        ListView view = (ListView) findViewById(R.id.listView);
        view.setAdapter(plantsAdapter);
    }
}