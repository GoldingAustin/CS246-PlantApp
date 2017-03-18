package com.cs246.plantapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
                Intent i = new Intent(getApplicationContext(), AddPlant.class);
                startActivity(i);
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

    /**
     * Gets plants.
     */
    protected void getPlants() {
        PlantsListAdapter plantsListAdapter = new PlantsListAdapter(getApplicationContext(), plants);
        Log.d("Results", String.valueOf(plantsListAdapter.getCount()));
        ListView view = (ListView) findViewById(R.id.listPlantsView);
        view.setAdapter(plantsListAdapter);
        setNotifications();
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
                for (PlantsObject plantsObject : plants) {
                    for (int i = 0; i < plantsObject.getCheckDays().size(); i++) {
                        if (plantsObject.getCheckDays().get(i)) {
                            Intent intent = new Intent(MainActivity.this, AlertReceiver.class);
                            Notification.Builder builder = new Notification.Builder(this);
                            builder.setContentTitle("Water Your Plant Today");
                            builder.setContentText("You've scheduled to water your " + plantsObject.getName() + "today");
                            builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
                            intent.putExtra(AlertReceiver.NOTIFICATION_ID, 1);
                            intent.putExtra(AlertReceiver.NOTIFICATION, builder.build());
                            scheduleAlarm(i, intent);
                        }
                    }
                }
            }
        }
    }

    /**
     * http://stackoverflow.com/questions/36550991/repeating-alarm-for-specific-days-of-week-android
     * @param dayOfWeek
     * @param intent
     */
    private void scheduleAlarm(int dayOfWeek, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }

        PendingIntent yourIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, yourIntent);
        Date date = new Date(calendar.getTimeInMillis());
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        Log.d("Time for Notification", format.format(date));
    }
}
