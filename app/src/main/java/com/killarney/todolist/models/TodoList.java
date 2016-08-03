package com.killarney.todolist.models;

import com.killarney.todolist.models.reminder.Reminder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Anthony on 5/19/2016.
 */
public class TodoList extends Event implements Iterable<Event>{
    private List<Event> events;

    protected TodoList(String title, String desc, Reminder reminder) {
        super(title, desc, reminder);
        events = new ArrayList<>();
    }

    public void addEvent(Event e){
        events.add(e);
    }

    protected void addEvents(List<Event> events){
        for (Event e : events) {
            this.events.add(e);

        }
    }

    protected List<Event> getEventsModifiable(){
        return events;
    }

    public List<Event> getEvents(){
        return Collections.unmodifiableList(events);
    }

    public int size(){
        return events.size();
    }

    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
