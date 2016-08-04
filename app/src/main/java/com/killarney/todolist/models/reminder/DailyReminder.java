package com.killarney.todolist.models.reminder;

import java.util.Calendar;

/**
 * Immutable representation of a daily reminder
 *
 * Created by Anthony on 8/2/2016.
 */
public final class DailyReminder extends AbstractRepeatReminder{
    public static final String REPEAT = "DAILY";
    //note that only the hourOfDay and minute are important; other values cannot be guaranteed to be constant
    private final Calendar calendar;
    private final String jsonTag;

    public DailyReminder(Calendar calendar){
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
        return "Daily at " + getTimeString(calendar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyReminder)) return false;

        DailyReminder that = (DailyReminder) o;

        if ((calendar.get(Calendar.HOUR_OF_DAY)) != that.calendar.get(Calendar.HOUR_OF_DAY)) return false;
        return ((calendar.get(Calendar.MINUTE)) == that.calendar.get(Calendar.MINUTE));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + calendar.get(Calendar.HOUR_OF_DAY);
        result = 31 * result + calendar.get(Calendar.MINUTE);
        return result;
    }
}
