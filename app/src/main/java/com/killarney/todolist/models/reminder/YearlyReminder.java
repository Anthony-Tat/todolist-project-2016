package com.killarney.todolist.models.reminder;

import java.util.Calendar;

/**
 * Immutable representation of a yearly reminder
 *
 * Created by Anthony on 8/2/2016.
 */
public final class YearlyReminder extends AbstractRepeatReminder{
    public static final String REPEAT = "YEARLY";
    private final Calendar calendar;
    private final String jsonTag;

    public YearlyReminder(Calendar calendar){
        this.calendar = calendar;
        this.jsonTag = REPEAT;
    }

    @Override
    public Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        return cal;
    }

    @Override
    public String getRepeatType(){
        return REPEAT;
    }

    @Override
    public String toFormattedString() {
        return "Yearly at " + getTimeString(calendar) + " on " + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YearlyReminder)) return false;

        YearlyReminder that = (YearlyReminder) o;

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
