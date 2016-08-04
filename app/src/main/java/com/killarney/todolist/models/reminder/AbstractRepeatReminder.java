package com.killarney.todolist.models.reminder;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Template/Partial implementation of the Reminder interface for repeating reminders
 * IMPORTANT!! subclasses should have a jsonTag field set to it's repeat type to ensure parsing is correct
 * Comparison of different subclasses are compared based on getRepeatType()
 * Comparison of similar subclasses are compared based on getCalendar()
 *
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

    @Override
    public int compareTo(@NonNull Reminder another) {
        if (!getReminderType().equals(another.getReminderType())) {
            return this.getReminderType().compareTo(another.getReminderType());
        } else {
            AbstractRepeatReminder o = ((AbstractRepeatReminder) another);
            if(!getRepeatType().equals(o.getRepeatType())){
                return this.getRepeatType().compareTo(o.getRepeatType());
            }
            else{
                return this.getCalendar().compareTo(o.getCalendar());
            }
        }
    }

}
