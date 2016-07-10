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

    public Event(int year, int month, int day, int hours, int mins, String title, String desc){
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

        if (calendar != null ? !calendar.equals(event.calendar) : event.calendar != null)
            return false;
        if (title != null ? !title.equals(event.title) : event.title != null) return false;
        return desc != null ? desc.equals(event.desc) : event.desc == null;

    }

    @Override
    public int hashCode() {
        int result = calendar != null ? calendar.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
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
