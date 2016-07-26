package com.killarney.todolist;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.killarney.todolist.models.CalendarReminder;
import com.killarney.todolist.models.Event;
import com.killarney.todolist.models.Reminder;
import com.killarney.todolist.models.RepeatReminder;

/**
 * Utility class to set up the alarm and notification for reminders
 *
 * Created by Anthony on 7/24/2016.
 */
public final class ReminderManager {

    //prevent instantiation
    private ReminderManager(){
    }

    public static void setAlarm(Activity activity, Event event, int[] depths){
        Intent intent = new Intent(activity, NotifyService.class);
        intent.putExtra("title", event.getTitle());
        intent.putExtra("desc", event.getDescription());
        intent.putExtra("id", event.hashCode());
        intent.putExtra("depths", depths);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Activity.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(activity, event.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Reminder reminder = event.getReminder();
        if(reminder!=null){
            if(reminder.getReminderType().equals(CalendarReminder.TYPE)){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, ((CalendarReminder) reminder).getCalendar().getTimeInMillis(), pendingIntent);
            }
            else if(reminder.getReminderType().equals(RepeatReminder.TYPE)){
                //TODO

            }
        }
    }

    public static void cancelAlarm(Activity activity, Event event){
        Intent intent = new Intent(activity, NotifyService.class);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Activity.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(activity, event.hashCode(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }

}
