package com.killarney.todolist.util;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.killarney.todolist.MainActivity;
import com.killarney.todolist.R;

public class NotifyService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotifyService(String name) {
        super(name);
    }

    public NotifyService() {
        super("notification");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Bundle bundle = intent.getExtras();

        //check for location reminder
        if (bundle.get("entering")!=null){
            //check if user trigger setting matches event (i.e. entering or exiting)
            //no need to create notification if triggers don't match
            if((boolean)intent.getExtras().get(LocationManager.KEY_PROXIMITY_ENTERING)!=(boolean) bundle.get("entering"))
                return;
        }

        Intent activityIntent = new Intent(this, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.putExtra("depths", (int []) bundle.get("depths"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(bundle.getString("title"))
                .setContentText(bundle.getString("desc"))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSmallIcon(R.drawable.ic_menu_send)
                .setTicker(bundle.getString("title"))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        notificationManager.notify(bundle.getInt("id"), notification);
        //schedule next time for reminder if necessary
        String[] repeat = bundle.getStringArray("repeat");
        if(repeat!=null){
            ReminderManager.setAlarm(this, bundle);
        }


    }
}
