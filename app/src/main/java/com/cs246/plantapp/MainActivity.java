package com.cs246.plantapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * The type Main activity.
 */
//Quick change to see merge conflict
public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private final ArrayList<PlantsObject> plants = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getFromDatabase();

        handleIntent(getIntent());


        FloatingActionButton myFab = (FloatingActionButton) this.findViewById(R.id.addPlantButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddPlant.class);
                startActivity(i);
                //setContentView(R.layout.activity_add_plant);

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFromDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    protected void getPlants() {
        PlantsListAdapter plantsListAdapter = new PlantsListAdapter(getApplicationContext(), plants);
        Log.d("Results", String.valueOf(plantsListAdapter.getCount()));
        ListView view = (ListView) findViewById(R.id.listPlantsView);
        view.setAdapter(plantsListAdapter);
    }

    private void getFromDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    plants.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        plants.add(snapshot.getValue(PlantsObject.class));
                    }
                    getPlants();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
