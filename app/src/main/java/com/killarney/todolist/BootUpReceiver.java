package com.killarney.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUpReceiver extends BroadcastReceiver {
    public BootUpReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
            Intent appIntent = new Intent(context, MainActivity.class);
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);
        }
    }
}
