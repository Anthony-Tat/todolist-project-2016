package com.killarney.todolist.models.reminder;

import com.killarney.todolist.models.reminder.Reminder;

import java.util.Calendar;

/**
 * Template/Partial implementation of the Reminder interface for repeating reminders
 * IMPORTANT!! subclasses should have a jsonTag field set to it's repeat type to ensure parsing is correct
 * Created by Anthony on 8/2/2016.
 */
public abstract class AbstractRepeatReminder implements Reminder {
    public static final String TYPE = "REPEAT";
    //subclasses should have a jsonTag field set to it's repeat type to ensure parsing is correct

    @Override
    public String getReminderType() {
        return TYPE;
    }

    public abstract String getRepeatType();

    public abstract Calendar getCalendar();

    protected String getTimeString(Calendar calendar){
        String str="";
        if(calendar.get(Calendar.MINUTE)<10)
            str = str + calendar.get(Calendar.HOUR_OF_DAY) + ":" + "0" + calendar.get(Calendar.MINUTE);
        else
            str = str + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        return str;
    }
}
