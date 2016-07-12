package com.killarney.todolist.models;

import java.util.Calendar;

/**
 * Created by Anthony on 5/19/2016.
 */
public class Event{

    private Calendar calendar;
    private String title;
    private String desc;

    //private ReminderTime


    protected Event(int year, int month, int day, int hours, int mins, String title, String desc){
        calendar = Calendar.getInstance();
        calendar.set(year, month, day, hours, mins);
        this.title = title;
        this.desc = desc;
    }
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return desc;
    }

    public void setDescription(String desc){
        this.desc = desc;
    }

    public Calendar getCalendar(){
        return calendar;
    }

    public void setCalendar(Calendar c){
        calendar = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        int i = event.calendar.get(Calendar.HOUR_OF_DAY);
        int j = calendar.get(Calendar.HOUR_OF_DAY);

        if ((calendar.get(Calendar.HOUR_OF_DAY)) != event.calendar.get(Calendar.HOUR_OF_DAY)) return false;
        if ((calendar.get(Calendar.MINUTE)) != event.calendar.get(Calendar.MINUTE)) return false;
        if ((calendar.get(Calendar.DATE)) != event.calendar.get(Calendar.DATE)) return false;
        if ((calendar.get(Calendar.MONTH)) != event.calendar.get(Calendar.MONTH)) return false;
        if ((calendar.get(Calendar.YEAR)) != event.calendar.get(Calendar.YEAR)) return false;

        if (!title.equals(event.title)) return false;
        return desc.equals(event.desc);

    }

    @Override
    public int hashCode() {
        int result = ((Integer) calendar.get(Calendar.HOUR_OF_DAY)).hashCode();
        result = 31 * result + ((Integer) calendar.get(Calendar.MINUTE)).hashCode();
        result = 31 * result + ((Integer) calendar.get(Calendar.DATE)).hashCode();
        result = 31 * result + ((Integer) calendar.get(Calendar.MONTH)).hashCode();
        result = 31 * result + ((Integer) calendar.get(Calendar.YEAR)).hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + desc.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if(calendar.get(Calendar.MINUTE)<10)
            return title + "\t" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "0\t" +
                    calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.YEAR);
        else
            return title + "\t" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "\t" +
                    calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.YEAR);
    }
}
