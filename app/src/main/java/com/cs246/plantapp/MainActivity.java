package com.cs246.plantapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * The type Main activity.
 */
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
                clearAdd();
                Intent i = new Intent(getApplicationContext(), AddPlant.class);
                startActivity(i);
            }
        });

        ListView view = (ListView) findViewById(R.id.listPlantsView);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout() {
                setNotifications();
            }
        });

        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                PlantsObject plant = (PlantsObject) parent.getItemAtPosition(position);
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(plant.getName()).removeValue();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.plantDelete);
                builder.setMessage("Delete this plant?");
                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No", dialogClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Item Position", String.valueOf(position));
                PlantsObject plant = (PlantsObject) parent.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), AddPlant.class);
                SharedPreferences prefs = getSharedPreferences("AddPlant", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(plant);
                editor.putString("tempPlant", json);
                editor.commit();
                startActivity(i);
                finish();
                Log.d("Selected Plant: ", plant.toString());
            }
        });
    }

    /**
     * Clear add.
     */
    private void clearAdd() {
        Intent i = this.getIntent();
        SharedPreferences prefs = this.getSharedPreferences("AddPlant", Context.MODE_PRIVATE);
        i.removeExtra("searchPlant");
        prefs.edit().clear().apply();
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
            finish();
        }

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_login);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets plants.
     */
    private void getPlants()  {
        SharedPreferences prefsSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        PlantsListAdapter plantsListAdapter;
        if (prefsSettings.contains("Measure") && prefsSettings.getString("Measure", "").equals("Imperial")) {
            plantsListAdapter = new PlantsListAdapter(getApplicationContext(), plants, "Imperial");
        } else {
            plantsListAdapter = new PlantsListAdapter(getApplicationContext(), plants, "Metric");
        }
        Log.d("Results", String.valueOf(plantsListAdapter.getCount()));
        ListView view = (ListView) findViewById(R.id.listPlantsView);
        view.setAdapter(plantsListAdapter);
    }

    /**
     * Get Plants Data from Database
     */
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

    /**
     * Set up individual notifications for each plant to water
     */
    private void setNotifications() {
        SharedPreferences prefs = this.getSharedPreferences("Settings", MODE_PRIVATE);
        if (prefs.contains("Notifications")) {
            if (prefs.getString("Notifications", "").equals("true")) {
                ListView view = (ListView) findViewById(R.id.listPlantsView);
                int position = 0;
                for (PlantsObject plantsObject : plants) {
                    TextView water = (TextView) view.getChildAt(position).findViewById(R.id.plants_list_water);
                    Log.d("Retrieved Water ", water.getText().toString());
                    for (int i = 0; i < plantsObject.getCheckDays().size(); i++) {
                        if (plantsObject.getCheckDays().get("Day " + String.valueOf(i))) {
                            Intent intent = new Intent(MainActivity.this, AlertReceiver.class);
                            Notification.Builder builder = new Notification.Builder(this);
                            builder.setContentTitle(plantsObject.getName() + " needs to be watered today");
                            builder.setContentText("This plant needs " + water.getText().toString());
                            builder.setSmallIcon(R.drawable.ic_stat_notify);
                            intent.putExtra(AlertReceiver.NOTIFICATION_ID, 1);
                            intent.putExtra(AlertReceiver.NOTIFICATION, builder.build());
                            scheduleAlarm(i + 1, intent);
                        }
                    }
                    position++;
                }
            }
        }
    }


    /**
     * http://stackoverflow.com/questions/36550991/repeating-alarm-for-specific-days-of-week-android
     *
     * @param dayOfWeek
     * @param intent
     */
    private void scheduleAlarm(int dayOfWeek, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        PendingIntent yourIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, yourIntent);
        Date dater = new Date(calendar.getTimeInMillis());
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        Log.d("Time for Notification", format.format(dater));
    }
}
