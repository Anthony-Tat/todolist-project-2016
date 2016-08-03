package com.killarney.todolist.models.reminder;

import com.killarney.todolist.models.Day;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;

/**
 * Immutable representation of a monthly reminder
 *
 * Created by Anthony on 8/2/2016.
 */
public final class MonthlyReminder extends AbstractRepeatReminder{
    public static final String REPEAT = "MONTHLY";
    private final Calendar calendar;
    private final String jsonTag;

    public MonthlyReminder(Calendar calendar){
        this.calendar = calendar;
        this.jsonTag = REPEAT;
    }

    @Override
    public Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        return cal;
    }

    @Override
    public String getRepeatType(){
        return REPEAT;
    }

    @Override
    public String toFormattedString() {
        return "Monthly at " + getTimeString(calendar) + " on the " + calendar.get(Calendar.DATE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonthlyReminder)) return false;

        MonthlyReminder that = (MonthlyReminder) o;

        if ((calendar.get(Calendar.DATE)) != that.calendar.get(Calendar.DATE)) return false;
        if ((calendar.get(Calendar.MONTH)) != that.calendar.get(Calendar.MONTH)) return false;
        if ((calendar.get(Calendar.YEAR)) == that.calendar.get(Calendar.YEAR)) return false;
        if ((calendar.get(Calendar.HOUR_OF_DAY)) != that.calendar.get(Calendar.HOUR_OF_DAY)) return false;
        return ((calendar.get(Calendar.MINUTE)) == that.calendar.get(Calendar.MINUTE));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + calendar.get(Calendar.DATE);
        result = 31 * result + calendar.get(Calendar.MONTH);
        result = 31 * result + calendar.get(Calendar.YEAR);
        result = 31 * result + calendar.get(Calendar.HOUR_OF_DAY);
        result = 31 * result + calendar.get(Calendar.MINUTE);
        return result;
    }
}
