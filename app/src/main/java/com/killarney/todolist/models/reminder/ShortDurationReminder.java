package com.killarney.todolist.models.reminder;

import java.util.Calendar;

/**
 * Immutable representation of a reminder that repeats every set x hours and x minutes
 *
 * Created by Anthony on 8/2/2016.
 */
public final class ShortDurationReminder extends AbstractRepeatReminder{
    public static final String REPEAT = "SHORT";
    //represents the beginning date
    private final Calendar calendar;
    private final String jsonTag;
    private final int hourlyRepeat;
    private final int minuteRepeat;

    public ShortDurationReminder(Calendar calendar, int hourlyRepeat, int minuteRepeat){
        this.calendar = calendar;
        this.hourlyRepeat = hourlyRepeat;
        this.minuteRepeat = minuteRepeat;
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

    public int getHourlyRepeat() {
        return hourlyRepeat;
    }

    public int getMinuteRepeat() {
        return minuteRepeat;
    }

    @Override
    public String toFormattedString() {
        return "Every " + hourlyRepeat + "h and " + minuteRepeat + "m";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShortDurationReminder)) return false;

        ShortDurationReminder that = (ShortDurationReminder) o;

        if(hourlyRepeat != that.hourlyRepeat) return false;
        if(minuteRepeat != that.minuteRepeat) return false;
        if ((calendar.get(Calendar.DATE)) != that.calendar.get(Calendar.DATE)) return false;
        if ((calendar.get(Calendar.MONTH)) != that.calendar.get(Calendar.MONTH)) return false;
        if ((calendar.get(Calendar.YEAR)) == that.calendar.get(Calendar.YEAR)) return false;
        if ((calendar.get(Calendar.HOUR_OF_DAY)) != that.calendar.get(Calendar.HOUR_OF_DAY)) return false;
        return ((calendar.get(Calendar.MINUTE)) == that.calendar.get(Calendar.MINUTE));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + hourlyRepeat;
        result = 31 * result + minuteRepeat;
        result = 31 * result + calendar.get(Calendar.DATE);
        result = 31 * result + calendar.get(Calendar.MONTH);
        result = 31 * result + calendar.get(Calendar.YEAR);
        result = 31 * result + calendar.get(Calendar.HOUR_OF_DAY);
        result = 31 * result + calendar.get(Calendar.MINUTE);
        return result;
    }
}
