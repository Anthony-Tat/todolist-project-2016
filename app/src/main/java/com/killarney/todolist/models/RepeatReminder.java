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
    private Day day = null;

    public RepeatReminder(Repeat repeat, Calendar calendar){
        this.repeat = repeat;
        this.calendar = calendar;
    }

    public RepeatReminder(Repeat repeat, Calendar calendar, Day day){
        this.repeat = repeat;
        this.calendar = calendar;
        this.day = day;
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

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getSerializedDays(){
        char[] c = "0000000".toCharArray();
        for (Day d : days) {
            c[d.toInt()] = '1';
        }

        return(new String(c));
    }

    public static Set<Day> getDays(String s){
        if(s.length()!=7)
            throw new IllegalArgumentException();
        Set<Day> set = new HashSet<>();
        char[] c = s.toCharArray();
        for (int i=0;i<7;i++){
            if(c[i]=='1'){
                switch (i){
                    case 0:
                        set.add(Day.SUNDAY);
                        break;
                    case 1:
                        set.add(Day.MONDAY);
                        break;
                    case 2:
                        set.add(Day.TUESDAY);
                        break;
                    case 3:
                        set.add(Day.WEDNESDAY);
                        break;
                    case 4:
                        set.add(Day.THURSDAY);
                        break;
                    case 5:
                        set.add(Day.FRIDAY);
                        break;
                    case 6:
                        set.add(Day.SATURDAY);
                        break;

                }
            }
        }
        return set;
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
                str = "Monthly at " + getTimeString();
                if(day!=null)
                    str = str + " on " + day.toString();
                else
                    str = str + " on the " + calendar.get(Calendar.DATE);
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
            str = str + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "0" ;
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
