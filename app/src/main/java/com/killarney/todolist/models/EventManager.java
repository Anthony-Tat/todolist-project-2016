package com.killarney.todolist.models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.killarney.todolist.exceptions.InvalidDateException;
import com.killarney.todolist.exceptions.InvalidTitleException;
import com.killarney.todolist.models.reminder.AbstractRepeatReminder;
import com.killarney.todolist.models.reminder.OneTimeCalendarReminder;
import com.killarney.todolist.models.reminder.Reminder;
import com.killarney.todolist.util.ReminderManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Anthony on 6/26/2016.
 */
public class EventManager{

    public static final int EVENT_ADDED = 0;
    public static final int EVENT_REMOVED = 1;
    public static final int EVENT_CHANGED = 2;
    public static final int MULTIPLE_EVENTS_REMOVED = 3;
    public static final int EVENTS_SORTED = 4;

    private final static String fileName = "eventManager.txt";
    private static boolean ready = true; //true if not in the middle of an operation
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
     * @param context to use for setting alarms
     */
    public static void restoreInstance(Context context){
        if(instance==null){
            List<Event> oldEvents = new ArrayList<>();
            instance = new EventManager();
            InputStream in = null;
            try {
                in = context.openFileInput(fileName);
                oldEvents = readJsonStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(in!=null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            instance.events = oldEvents;
            restoreAlarms(context, oldEvents, new int[0]);
        }
    }

    public static void saveInstance(Context context){
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.setIndent("  ");
            saveEvents(EventManager.getInstance().getEvents(), writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void restoreAlarms(Context context, List<Event> events, int[] depths){
        for(int i=0;i<events.size();i++){
            Event e = events.get(i);
            if(e.getClass()==TodoList.class){
                int[] temp = Arrays.copyOf(depths, depths.length+1);
                temp[depths.length] = i;
                restoreAlarms(context, ((TodoList) e).getEvents(), temp);
            }
            Reminder r = e.getReminder();
            if(r!=null){
                Calendar now = Calendar.getInstance();
                int[] newDepths = Arrays.copyOf(depths, depths.length + 1);
                newDepths[depths.length] = i;
                if(r.getReminderType().equals(OneTimeCalendarReminder.TYPE)){
                    if(((OneTimeCalendarReminder) r).getCalendar().after(now)) {
                        ReminderManager.setAlarm(context, e, newDepths);
                    }
                }
                else if(r.getReminderType().equals(AbstractRepeatReminder.TYPE)){
                    if(((AbstractRepeatReminder) r).getCalendar().after(now)) {
                        ReminderManager.setAlarm(context, e, newDepths);
                    }
                }
            }
        }
    }

    public static void sort(Comparator<Event> comparator){
        Collections.sort(events, comparator);
    }

    public static void reverse(){
        Collections.reverse(events);
    }

    /**
     * adds the event to the list at the current depth
     * @param eventClass type of event to create; i.e. Event or Todolist
     */
    public void addEvent(String title, String desc, Reminder reminder, Class<?> eventClass) throws InvalidDateException, InvalidTitleException, InvalidClassException{
        ready = false;
        if(title.trim().length() <= 0){
            throw new InvalidTitleException();
        }
        if(reminder!=null){
            if(reminder.getReminderType().equals(OneTimeCalendarReminder.TYPE)){
                if(((OneTimeCalendarReminder) reminder).getCalendar().before(Calendar.getInstance())){
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
        ready = true;
        notifyListeners(EVENT_ADDED, e);

    }

    /**
     * @param pos position in the list of events at the current depth
     */
    public void editEvent(String title, String desc, Reminder reminder, int pos) throws InvalidDateException, InvalidTitleException{
        ready = false;
        if(title.trim().length() <= 0){
            throw new InvalidTitleException();
        }
        if(reminder!=null){
            if(reminder.getReminderType().equals(OneTimeCalendarReminder.TYPE)){
                if(((OneTimeCalendarReminder) reminder).getCalendar().before(Calendar.getInstance())){
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
        ready = true;
        notifyListeners(EVENT_CHANGED, e);
    }

    /**
     * @return true if eventManager is not being modified
     */
    public boolean isReady(){
        return ready;
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

    /**
     * @return current depth in an array
     */
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
        ready = false;
        List<Event> loe = events;
        for (int x = 0; x < depths.size(); x++) {
            loe = ((TodoList) loe.get(depths.get(x))).getEventsModifiable();
        }
        Event e = loe.get(i);
        loe.remove(i);
        ready = true;
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

    private static List<Event> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Event> events = new ArrayList<>();

        reader.beginArray();
        Gson gson = new GsonBuilder().registerTypeAdapter(Event.class, new EventTypeAdapter()).create();
        while (reader.hasNext()) {
            Event event;
            event = gson.fromJson(reader, Event.class);
            events.add(event);
        }
        reader.endArray();

        return events;
    }

    private static void saveEvents(List<Event> events, JsonWriter writer) throws IOException {
        Gson gson = new Gson();
        writer.beginArray();
        for (Event e : events) {
            if(e.getClass()==Event.class){
                gson.toJson(e, Event.class, writer);
            }
            else if(e.getClass()== TodoList.class){
                gson.toJson(e, TodoList.class, writer);
            }
        }
        writer.endArray();
    }
}
