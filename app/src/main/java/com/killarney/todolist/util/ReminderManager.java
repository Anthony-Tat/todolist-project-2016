package com.killarney.todolist.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.killarney.todolist.models.CalendarReminder;
import com.killarney.todolist.models.Day;
import com.killarney.todolist.models.Event;
import com.killarney.todolist.models.Reminder;
import com.killarney.todolist.models.RepeatReminder;

import java.util.Calendar;
import java.util.Set;

/**
 * Utility class to set up the alarm and notification for reminders
 *
 * Created by Anthony on 7/24/2016.
 */
public final class ReminderManager {

    //prevent instantiation
    private ReminderManager(){
    }

    /**
     *
     * @param context
     * @param event
     * @param depths
     * @return true if successfully set
     */
    public static boolean setAlarm(Context context, Event event, int[] depths){
        Reminder reminder = event.getReminder();
        Bundle bundle = new Bundle();
        bundle.putString("title", event.getTitle());
        bundle.putString("desc", event.getDescription());
        bundle.putInt("id", event.hashCode());
        bundle.putIntArray("depths", depths);
        if(reminder!=null){
            if(reminder.getReminderType().equals(CalendarReminder.TYPE)) {
                bundle.putString("calendar", CalendarParser.parseCalendar(((CalendarReminder) reminder).getCalendar()));
            }
            else if(reminder.getReminderType().equals(RepeatReminder.TYPE)){
                RepeatReminder repeatReminder =  ((RepeatReminder) reminder);
                String[] strings = new String[3];
                switch(repeatReminder.getRepeat()){
                    case DAILY:
                        strings[0] = "daily";
                        strings[1] = CalendarParser.parseTime(repeatReminder.getCalendar());
                        break;
                    case WEEKLY:
                        strings[0] = "weekly";
                        strings[1] = CalendarParser.parseDays(repeatReminder.getDays());
                        strings[2] = CalendarParser.parseTime(repeatReminder.getCalendar());
                        break;
                    case MONTHLY:
                        strings[0] = "monthly";
                        strings[1] = CalendarParser.parseCalendar(repeatReminder.getCalendar());
                        break;
                    default:
                        strings[0] = "yearly";
                        strings[1] = CalendarParser.parseCalendar(repeatReminder.getCalendar());
                        break;
                }
                bundle.putStringArray("repeat", strings);
            }
        }
        return setAlarm(context, bundle);
    }

    public static void cancelAlarm(Context context, Event event){
        Intent intent = new Intent(context, NotifyService.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, event.hashCode(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    protected static boolean setAlarm(Context context, Bundle bundle){
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra("title", (String) bundle.get("title"));
        intent.putExtra("desc",(String) bundle.get("desc"));
        intent.putExtra("id",(int) bundle.get("id"));
        intent.putExtra("depths",(int[]) bundle.get("depths"));
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);

        String temp = (String) bundle.get("calendar");
        if(temp!=null){
            Calendar calendar = CalendarParser.unparseCalendar(temp);
            if(calendar!=null){
                PendingIntent pendingIntent = PendingIntent.getService(context, (int) bundle.get("id"), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                return true;
            }
        }

        String[] strings = (String[]) bundle.get("repeat");
        if(strings!=null){
            intent.putExtra("repeat", strings);
            PendingIntent pendingIntent = PendingIntent.getService(context, (int) bundle.get("id"), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar now = Calendar.getInstance();
            Calendar reminderCalendar;
            switch(strings[0]){
                case "daily":
                    reminderCalendar = CalendarParser.unparseTime(strings[1]);
                    reminderCalendar.set(Calendar.SECOND, 0); //prevent notification loop during the initial minute
                    if(reminderCalendar.before(now)){
                        //use the fact that reminderCalendar.getInstance is called to parse, so time will always be today
                        reminderCalendar.add(Calendar.DATE, 1);
                    }
                    break;
                case "weekly":
                    reminderCalendar = CalendarParser.unparseTime(strings[2]);
                    reminderCalendar.set(Calendar.SECOND, 0); //prevent notification loop during the initial minute
                    Set<Day> days = CalendarParser.unparseDays(strings[1]);
                    Day today = CalendarParser.calendarDaytoDay(reminderCalendar.get(Calendar.DAY_OF_WEEK));

                    while(reminderCalendar.before(now) || !days.contains(today)){
                        reminderCalendar.add(Calendar.DATE, 1);
                        today = CalendarParser.calendarDaytoDay(reminderCalendar.get(Calendar.DAY_OF_WEEK));
                    }
                    break;
                case "monthly":
                    reminderCalendar = CalendarParser.unparseCalendar(strings[1]);
                    reminderCalendar.set(Calendar.SECOND, 0); //prevent notification loop during the initial minute
                    while(reminderCalendar.before(now)){
                        reminderCalendar.add(Calendar.MONTH, 1);
                    }
                    break;
                case "yearly":
                    reminderCalendar = CalendarParser.unparseCalendar(strings[1]);
                    reminderCalendar.set(Calendar.SECOND, 0); //prevent notification loop during the initial minute
                    while(reminderCalendar.before(now)){
                        reminderCalendar.add(Calendar.YEAR, 1);
                    }
                    break;
                default:
                    throw new IllegalArgumentException();

            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderCalendar.getTimeInMillis(), pendingIntent);
            return true;
        }
        return false;
    }

}
