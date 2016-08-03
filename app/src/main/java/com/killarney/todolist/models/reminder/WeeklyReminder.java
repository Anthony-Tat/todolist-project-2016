package com.killarney.todolist.models.reminder;

import com.killarney.todolist.models.Day;

import java.util.Calendar;
import java.util.Collections;
import java.util.Set;

/**
 * Immutable representation of a weekly reminder
 *
 * Created by Anthony on 8/2/2016.
 */
public final class WeeklyReminder extends AbstractRepeatReminder{
    public static final String REPEAT = "WEEKLY";
    //note that only the hourOfDay and minute are important; other values cannot be guaranteed to be constant
    private final Calendar calendar;
    private final Set<Day> days;
    private final String jsonTag;

    public WeeklyReminder(Calendar calendar, Set<Day> days){
        this.calendar = calendar;
        this.days = days;
        this.jsonTag = REPEAT;
    }

    @Override
    public Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        return cal;
    }

    public Set<Day> getDays() {
        return Collections.unmodifiableSet(days);
    }

    @Override
    public String getRepeatType(){
        return REPEAT;
    }

    @Override
    public String toFormattedString() {
        return "Weekly at " + getTimeString(calendar) + " on " + daysToString();
    }

    private String daysToString(){
        String str = "";
        for (Day d : days) {
            if(str.length()==0)
                str = d.toString();
            else
                str = str + ", " + d.toString();
        }
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeeklyReminder)) return false;

        WeeklyReminder that = (WeeklyReminder) o;

        if ((calendar.get(Calendar.HOUR_OF_DAY)) != that.calendar.get(Calendar.HOUR_OF_DAY)) return false;
        if ((calendar.get(Calendar.MINUTE)) != that.calendar.get(Calendar.MINUTE)) return false;
        return days.equals(that.days);

    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + calendar.get(Calendar.HOUR_OF_DAY);
        result = 31 * result + calendar.get(Calendar.MINUTE);
        result = 31 * result + days.hashCode();
        return result;
    }
}
