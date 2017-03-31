package com.cs246.plantapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

    /**
     * The constant NOTIFICATION_ID.
     */
    public static final String NOTIFICATION_ID = "001";
    /**
     * The constant NOTIFICATION.
     */
    public static final String NOTIFICATION = "plantNotification";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        notification.contentIntent = pendingIntent;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        int id = intent.getIntExtra(NOTIFICATION_ID, 1);
        notificationManager.notify(id, notification);
    }
}
