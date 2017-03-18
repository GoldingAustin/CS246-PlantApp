package com.cs246.plantapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by austingolding on 3/18/17.
 */

/**
 * https://gist.github.com/BrandonSmith/6679223
 */
public class AlertReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "001";
    public static String NOTIFICATION = "plantNotification";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }
}
