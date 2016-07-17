package com.killarney.todolist.models;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Anthony on 5/19/2016.
 */
public class TodoList extends Event implements Iterable<Event>{
    private List<Event> events;

    protected TodoList(int year, int month, int day, int hours, int mins, String title, String desc) {
        super(year, month, day, hours, mins, title, desc);
        events = new ArrayList<>();
    }

    public void addEvent(int year, int month, int day, int hours, int mins, String title, String desc, Class<?> eventClass) throws InvalidClassException{
        if(eventClass == TodoList.class){
            events.add(new TodoList(year, month, day, hours, mins, title, desc));
        }
        else if(eventClass == Event.class) {
            events.add(new Event(year, month, day, hours, mins, title, desc));
        }
        else{
            throw new InvalidClassException("Class type must be Event or TodoList");
        }
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

    public void removeEvent(Event e){
        events.remove(e);
    }

    public int size(){
        return events.size();
    }

    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
