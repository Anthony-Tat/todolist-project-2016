package com.killarney.todolist.models;

import com.killarney.todolist.models.reminder.Reminder;

/**
 * Created by Anthony on 5/19/2016.
 */
public class Event{

    private String title;
    private String desc;
    private Reminder reminder;


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
}
