package com.killarney.todolist.models;

import android.app.Activity;

import com.killarney.todolist.ReminderManager;
import com.killarney.todolist.exceptions.InvalidDateException;
import com.killarney.todolist.exceptions.InvalidTitleException;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anthony on 6/26/2016.
 */
public class EventManager{

    public static final int EVENT_ADDED = 0;
    public static final int EVENT_REMOVED = 1;
    public static final int EVENT_CHANGED = 2;
    public static final int MULTIPLE_EVENTS_REMOVED = 3;

    private static boolean status = true; //true if not in the middle of an operation
    private static List<Integer> depths; //list of ordered positions accessed by events fragment
    private static List<Event> events;
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

    /**
     * Restore the previous instance with its events and alarms
     * @param activity Activity to use for setting alarms
     * @param oldEvents events to restore
     */
    public static void restoreInstance(Activity activity, List<Event> oldEvents){
        if(instance==null){
            instance = new EventManager();
            instance.events = oldEvents;
        }

        restoreAlarms(activity, oldEvents, new int[0]);
    }

    private static void restoreAlarms(Activity activity, List<Event> events, int[] depths){
        for(int i=0;i<events.size();i++){
            Event e = events.get(i);
            if(e.getClass()==TodoList.class){
                int[] temp = Arrays.copyOf(depths, depths.length+1);
                temp[depths.length] = i;
                restoreAlarms(activity, ((TodoList) e).getEvents(), temp);
            }
            Reminder r = e.getReminder();
            if(r!=null){
                Calendar now = Calendar.getInstance();
                if(r.getReminderType().equals(CalendarReminder.TYPE)){
                    if(((CalendarReminder) r).getCalendar().after(now)) {
                        ReminderManager.setAlarm(activity, e, depths);
                    }
                }
                else if(r.getReminderType().equals(RepeatReminder.TYPE)){
                    if(((RepeatReminder) r).getCalendar().after(now)){

                    }
                }
            }
        }
    }

    /**
     * adds the event to the list at the current depth
     * @param eventClass type of event to create; i.e. Event or Todolist
     */
    public void addEvent(String title, String desc, Reminder reminder, Class<?> eventClass) throws InvalidDateException, InvalidTitleException, InvalidClassException{
        status = false;
        if(title.trim().length() <= 0){
            throw new InvalidTitleException();
        }
        if(reminder!=null){
            if(reminder.getReminderType()==CalendarReminder.TYPE){
                if(((CalendarReminder) reminder).getCalendar().before(Calendar.getInstance())){
                    throw new InvalidDateException();
                }
            }
        }

        //Find the right spot to add the event
        List<Event> loe = getEvents();
        TodoList tl = null;
        for (int i = 0; i < depths.size(); i++){
            tl = (TodoList) loe.get(depths.get(i));
            loe = tl.getEvents();
        }

        Event e;
        if(eventClass == TodoList.class){
            e = new TodoList(title, desc, reminder);
        }
        else if(eventClass == Event.class){
            e = new Event(title, desc, reminder);
        }
        else{
            throw new InvalidClassException("Class type must be Event or TodoList");
        }

        if(tl!=null){
            tl.addEvent(e);
        }
        else{
            events.add(e);
        }
        status = true;
        notifyListeners(EVENT_ADDED, e);

    }

    /**
     * @param pos position in the list of events at the current depth
     */
    public void editEvent(String title, String desc, Reminder reminder, int pos) throws InvalidDateException, InvalidTitleException{
        status = false;
        if(title.trim().length() <= 0){
            throw new InvalidTitleException();
        }
        if(reminder!=null){
            if(reminder.getReminderType().equals(CalendarReminder.TYPE)){
                if(((CalendarReminder) reminder).getCalendar().before(Calendar.getInstance())){
                    throw new InvalidDateException();
                }
            }
            else if(reminder.getReminderType().equals(RepeatReminder.TYPE)){
                if(((RepeatReminder) reminder).getCalendar().before(Calendar.getInstance())){
                    throw new InvalidDateException();
                }
            }
        }
        Event e = getEventAtCurrentDepthAtPos(pos);
        notifyListeners(EVENT_REMOVED, e);
        e.setDescription(desc);
        e.setTitle(title);
        e.setReminder(reminder);
        notifyListeners(EVENT_ADDED, e);
        status = true;
        notifyListeners(EVENT_CHANGED, e);
    }

    /**
     * @return true if eventManager is not being modified
     */
    public boolean getStatus(){
        return status;
    }

    /**
     * @return unmodifiable list of all the events
     */
    public List<Event> getEvents(){
        return Collections.unmodifiableList(events);
    }

    /**
     * @return description of element at position i of current depth
     */
    public String getDescriptionAt(int i){
        return getEventAtCurrentDepthAtPos(i).getDescription();
    }

    public int[] getDepthArray(){
        int[] ints = new int[depths.size()];
        for (int i=0;i<depths.size();i++){
            ints[i] = depths.get(i);
        }
        return ints;
    }

    public void addDepth(int d){
        depths.add(d);
    }

    /**
     *
     * @return true if a depth was removed, false otherwise
     */
    public boolean removeDepth(){
        if(depths.size()>0){
            depths.remove(depths.size()-1);
            return true;
        }
        return false;
    }

    /**
     * removes element at position i of current depth
     */
    public void remove(int i){
        status = false;
        List<Event> loe = events;
        for (int x = 0; x < depths.size(); x++) {
            loe = ((TodoList) loe.get(depths.get(x))).getEventsModifiable();
        }
        Event e = loe.get(i);
        loe.remove(i);
        status = true;
        if(e.getClass()==Event.class){
            notifyListeners(EVENT_REMOVED, e);
        }
        else{
            notifyListeners(MULTIPLE_EVENTS_REMOVED, e);
        }

    }


    /**
     *
     * @param event
     * @return index of event in current depth or -1 if event not found
     */
    public int indexOf(Event event){
        return getEventsAtCurrentDepth().indexOf(event);
    }

    /**
     * set element at position i of current depth to i
     */
    public void editDescAt(String s, int i){
        getEventAtCurrentDepthAtPos(i).setDescription(s);
    }

    public Event getEventAtCurrentDepthAtPos(int pos){
        return getEventsAtCurrentDepth().get(pos);
    }

    /**
     * @return unmodifiable list of events at current depth
     */
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
        void onEventChanged(int msg, Event e);
    }

    private void notifyListeners(int msg, Event e){
        for (EventChangedListener mListener: mListeners) {
            mListener.onEventChanged(msg, e);
        }
    }
}
