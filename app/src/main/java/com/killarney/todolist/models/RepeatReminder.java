package com.killarney.todolist.models;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Anthony on 7/17/2016.
 */
public class RepeatReminder implements Reminder{

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

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    public Set<Day> getDays() {
        return days;
    }

    public void setDays(Set<Day> days) {
        this.days = days;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
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
}
