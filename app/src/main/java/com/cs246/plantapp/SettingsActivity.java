package com.cs246.plantapp;


import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.content_settings);
        final Switch aSwitch = (Switch) findViewById(R.id.notificationsSwitch);
        SharedPreferences prefs = this.getSharedPreferences("Settings", MODE_PRIVATE);
        if (prefs.contains("Notifications")) {
            if (prefs.getString("Notifications", "").equals("true")) {
                aSwitch.setChecked(true);
            } else {
                aSwitch.setChecked(false);
            }
        }

        aSwitch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               /**
                * http://stackoverflow.com/questions/4315611/android-get-all-pendingintents-set-with-alarmmanager
                */
               if (!aSwitch.isChecked()) {
                   AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

                   Intent updateServiceIntent = new Intent(getApplicationContext(), AlertReceiver.class);
                   PendingIntent pendingUpdateIntent = PendingIntent.getService(getApplicationContext(), 0, updateServiceIntent, 0);

                   // Cancel alarms
                   try {
                       alarmManager.cancel(pendingUpdateIntent);
                       Log.d("Settings", "Notifications cleared");
                   } catch (Exception e) {
                       Log.e("Settings", "AlarmManager update was not canceled. " + e.toString());
                   }
               }
               SharedPreferences prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = prefs.edit();
               editor.putString("Notifications", String.valueOf(aSwitch.isChecked()));
               editor.commit();
           }
       });
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        if (prefs.contains("Days")) {
            Gson gson = new Gson();
            Type list = new TypeToken<List<Boolean>>(){}.getType();
            List<Boolean> days = gson.fromJson(prefs.getString("Days", ""), list);
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if (linearLayout.getChildAt(i) instanceof CheckBox) {
                    ((CheckBox) linearLayout.getChildAt(i)).setChecked(days.get(0));
                    days.remove(0);
                }
            }
        }
        Button button = (Button) findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Boolean> checkDays = new ArrayList<>();
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    if (linearLayout.getChildAt(i) instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                        checkDays.add(checkBox.isChecked());
                    }
                }
                SharedPreferences prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(checkDays);
                editor.putString("Days", json);
                editor.commit();
            }
        });

    }

}
