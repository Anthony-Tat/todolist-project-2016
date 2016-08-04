package com.killarney.todolist.models;

import android.support.annotation.NonNull;

import com.killarney.todolist.models.reminder.Reminder;

import java.util.Comparator;

/**
 * Created by Anthony on 5/19/2016.
 */
public class Event implements Comparable<Event>{

    private String title;
    private String desc;
    private Reminder reminder;
    //probably want an urgency field


    protected Event(String title, String desc, Reminder reminder){
        this.reminder = reminder;
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

    public Reminder getReminder(){
        return reminder;
    }

    public void setReminder(Reminder reminder){
        this.reminder = reminder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (!title.equals(event.title)) return false;
        if (!desc.equals(event.desc)) return false;
        return reminder != null ? reminder.equals(event.reminder) : event.reminder == null;

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + desc.hashCode();
        result = 31 * result + (reminder != null ? reminder.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if(reminder!=null)
            return title + "\t" + reminder.toFormattedString();
        else
            return title;
    }

    @Override
    public int compareTo(@NonNull Event another) {
        return Comparators.TITLE.compare(this, another);
    }

    public static class Comparators{
        public static final Comparator<Event> TITLE = new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.title.compareTo(e2.title);
            }
        };
        public static final Comparator<Event> REMINDER = new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                if(e1.reminder == e2.reminder){
                    return 0;
                }
                else if(e1.reminder == null){
                    return 1;
                }
                else if(e2.reminder == null){
                    return -1;
                }
                else{
                    //e1.reminder != e2.reminder != null
                    return e1.reminder.compareTo(e2.reminder);
                }
            }
        };

    }
}
