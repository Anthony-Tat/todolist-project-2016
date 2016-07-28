package com.killarney.todolist.models;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An immutable implementation of the reminder interface representing a reminder that repeats on a set interval.
 *
 * Created by Anthony on 7/17/2016.
 */
public final class RepeatReminder implements Reminder{

    public static final String TYPE = "REPEAT";
    private Calendar calendar;
    private Repeat repeat;
    private Set<Day> days;

    public RepeatReminder(Repeat repeat, Calendar calendar){
        this.repeat = repeat;
        this.calendar = calendar;
    }

    public RepeatReminder(Repeat repeat, Calendar calendar, Set<Day> days){
        this.repeat = repeat;
        this.calendar = calendar;
        this.days = days;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public Set<Day> getDays() {
        return Collections.unmodifiableSet(days);
    }

    public Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        return cal;
    }

    @Override
    public String toFormattedString() {
        String str = null;
        switch(repeat){
            case DAILY:
                str = "Daily at " + getTimeString();
                break;
            case WEEKLY:
                str = "Weekly at " + getTimeString() + " on " + daysToString();
                break;
            case MONTHLY:
                str = "Monthly at " + getTimeString() + " on the " + calendar.get(Calendar.DATE);
                break;
            case YEARLY:
                str = "Yearly at " + getTimeString() + " on " + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);
                break;

        }
        return str;
    }

    @Override
    public String getReminderType() {
        return TYPE;
    }

    private String getTimeString(){
        String str="";
        if(calendar.get(Calendar.MINUTE)<10)
            str = str + calendar.get(Calendar.HOUR_OF_DAY) + ":" + "0" + calendar.get(Calendar.MINUTE);
        else
            str = str + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        return str;
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
        if (!(o instanceof RepeatReminder)) return false;

        RepeatReminder that = (RepeatReminder) o;

        if(calendar==null){
            if(that.calendar != null)
                return false;
        }
        else{
            if ((calendar.get(Calendar.HOUR_OF_DAY)) != that.calendar.get(Calendar.HOUR_OF_DAY)) return false;
            if ((calendar.get(Calendar.MINUTE)) != that.calendar.get(Calendar.MINUTE)) return false;

            if(repeat==Repeat.MONTHLY || repeat==Repeat.YEARLY){
                if ((calendar.get(Calendar.DATE)) != that.calendar.get(Calendar.DATE)) return false;
                if ((calendar.get(Calendar.MONTH)) != that.calendar.get(Calendar.MONTH)) return false;
                if ((calendar.get(Calendar.YEAR)) == that.calendar.get(Calendar.YEAR)) return false;
            }
        }
        if (repeat != that.repeat) return false;
        return days != null ? days.equals(that.days) : that.days == null;

    }

    @Override
    public int hashCode() {
        int result = 17;
        if(calendar!=null){
            result = 31 * result + calendar.get(Calendar.HOUR_OF_DAY);
            result = 31 * result + calendar.get(Calendar.MINUTE);

            if(repeat==Repeat.MONTHLY || repeat==Repeat.YEARLY) {
                result = 31 * result + calendar.get(Calendar.DATE);
                result = 31 * result + calendar.get(Calendar.MONTH);
                result = 31 * result + calendar.get(Calendar.YEAR);
            }
            else{
                result = 31 * 31 * 31 * result;
            }
        }

        result = 31 * result + (repeat != null ? repeat.hashCode() : 0);
        result = 31 * result + (days != null ? days.hashCode() : 0);
        return result;
    }
}
