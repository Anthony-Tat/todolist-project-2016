package com.killarney.todolist.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.killarney.todolist.exceptions.InvalidDateException;
import com.killarney.todolist.exceptions.InvalidTitleException;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Anthony on 6/26/2016.
 */
public class EventManager implements Iterable<Event>{

    public static final int EVENT_ADDED = 0;
    public static final int EVENT_REMOVED = 1;

    private static List<Integer> depths; //list of ordered positions accessed by events fragment

    private List<Event> events;
    private static EventManager instance;
    private List<EventChangedListener> mListeners;

    private EventManager() {
        events = new ArrayList<>();
        mListeners = new ArrayList<>();
        depths = new ArrayList<>();
    }

    public static EventManager getInstance(){
        if(instance==null){
            instance = new EventManager();
        }

        return instance;
    }

    /*public static EventManager getInstance(Activity a){
        if(instance==null) {
            SharedPreferences mPrefs = a.getPreferences(Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("eventManager", "");
            instance = gson.fromJson(json, EventManager.class);

            if(instance==null){
                instance = new EventManager();
            }
        }

        return instance;
    }*/

    public void addEvent(int year, int month, int day, int hours, int mins, String title, String desc, Class<?> eventClass) throws InvalidDateException, InvalidTitleException, InvalidClassException{
        Calendar date = Calendar.getInstance();
        date.set(year, month, day, hours, mins);

        if(date.after(Calendar.getInstance())) {
            if(title.trim().length() > 0){

                //Find the right spot to add the event
                List<Event> loe = getEvents();
                TodoList tl = null;
                for (int i = 0; i < depths.size(); i++){
                    tl = (TodoList) loe.get(depths.get(i));
                    loe = tl.getEvents();
                }

                if(tl!=null) {
                    tl.addEvent(year, month, day, hours, mins, title, desc, eventClass);
                }
                else{
                    if(eventClass == TodoList.class){
                        events.add(new TodoList(year, month, day, hours, mins, title, desc));
                    }
                    else if(eventClass == Event.class){
                        events.add(new Event(year, month, day, hours, mins, title, desc));
                    }
                    else{
                        throw new InvalidClassException("Class type must be Event or TodoList");
                    }
                }
                notifyListeners(EVENT_ADDED);
            }
            else{
                throw new InvalidTitleException();
            }
        }
        else{
            throw new InvalidDateException();
        }
    }

    public List<Event> getEvents(){
        return Collections.unmodifiableList(events);
    }

    public String getDescriptionAt(int i){
        return getEventAtCurrentDepthAtPos(i).getDescription();
    }

    public void addDepth(int d){
        depths.add(d);
    }

    public void removeDepth(){
        if(depths.size()>0){
            depths.remove(depths.size()-1);
        }
    }

    public void remove(int i){
        List<Event> loe = events;
        for (int x = 0; x < depths.size(); x++) {
            loe = ((TodoList) loe.get(depths.get(x))).getEventsModifiable();
        }
        loe.remove(i);
        notifyListeners(EVENT_REMOVED);
    }

    public int size(){
        return events.size();
    }

    public void editDescAt(String s, int i){
        getEventAtCurrentDepthAtPos(i).setDescription(s);
    }

    public Event getEventAtCurrentDepthAtPos(int pos){
        return getEventsAtCurrentDepth().get(pos);
    }

    public List<Event> getEventsAtCurrentDepth(){
        List<Event> loe = getEvents();
        for (int i = 0; i < depths.size(); i++){
            loe = ((TodoList) loe.get(depths.get(i))).getEvents();
        }
        return loe;
    }

    public void addListener(EventChangedListener listener){
        mListeners.add(listener);
    }

    public interface EventChangedListener{
        void onEventChanged(int msg);
    }

    private void notifyListeners(int msg){
        for (EventChangedListener mListener: mListeners) {
            mListener.onEventChanged(msg);
        }
    }

    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
