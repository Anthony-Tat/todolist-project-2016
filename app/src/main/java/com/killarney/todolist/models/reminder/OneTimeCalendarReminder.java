package com.killarney.todolist.models.reminder;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * An immutable implementation of the reminder interface representing a one-time reminder.
 *
 * Created by Anthony on 7/17/2016.
 */
public final class OneTimeCalendarReminder implements Reminder {

    public static final String TYPE = "CALENDAR";
    private Calendar calendar;

    public OneTimeCalendarReminder(Calendar calendar){
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        return cal;
    }

    @Override
    public String toFormattedString() {
        if(calendar.get(Calendar.MINUTE)<10)
            return calendar.get(Calendar.HOUR_OF_DAY) + ":" + "0" + calendar.get(Calendar.MINUTE) + "\t" +
                    (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.YEAR);
        else
            return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "\t" +
                    (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.YEAR);
    }

    @Override
    public String getReminderType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OneTimeCalendarReminder)) return false;

        OneTimeCalendarReminder that = (OneTimeCalendarReminder) o;

        if ((calendar.get(Calendar.HOUR_OF_DAY)) != that.calendar.get(Calendar.HOUR_OF_DAY)) return false;
        if ((calendar.get(Calendar.MINUTE)) != that.calendar.get(Calendar.MINUTE)) return false;
        if ((calendar.get(Calendar.DATE)) != that.calendar.get(Calendar.DATE)) return false;
        if ((calendar.get(Calendar.MONTH)) != that.calendar.get(Calendar.MONTH)) return false;
        return ((calendar.get(Calendar.YEAR)) == that.calendar.get(Calendar.YEAR));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + calendar.get(Calendar.HOUR_OF_DAY);
        result = 31 * result + calendar.get(Calendar.MINUTE);
        result = 31 * result + calendar.get(Calendar.DATE);
        result = 31 * result + calendar.get(Calendar.MONTH);
        result = 31 * result + calendar.get(Calendar.YEAR);
        return result;
    }

    @Override
    public int compareTo(@NonNull Reminder another) {
        if(!getReminderType().equals(another.getReminderType())){
            return this.getReminderType().compareTo(another.getReminderType());
        }
        else{
            return this.getCalendar().compareTo(((OneTimeCalendarReminder) another).getCalendar());
        }
    }
}
