package com.killarney.todolist.models;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.killarney.todolist.models.reminder.CalendarReminder;
import com.killarney.todolist.models.reminder.DailyReminder;
import com.killarney.todolist.models.reminder.MonthlyReminder;
import com.killarney.todolist.models.reminder.Reminder;
import com.killarney.todolist.models.reminder.ShortDurationReminder;
import com.killarney.todolist.models.reminder.WeeklyReminder;
import com.killarney.todolist.models.reminder.YearlyReminder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TypeAdapter to read Json for the Event/TodoList hierarchy
 */
public class EventTypeAdapter extends TypeAdapter<Event>{

    @Override
    public void write(JsonWriter out, Event value) throws IOException {
        //not going to be used
    }

    @Override
    public Event read(JsonReader in) throws IOException {
        Event e;
        String className = "EVENT", title="", desc="";
        Reminder reminder = null;
        List<Event> events = new ArrayList<>();
        JsonToken token = in.peek();
        if(token.equals(JsonToken.BEGIN_OBJECT)){
            in.beginObject();
            while(!in.peek().equals(JsonToken.END_OBJECT)){
                if(in.peek().equals(JsonToken.NAME)){
                    String str = in.nextName();
                    switch(str){
                        case "events":{
                            className = "TODOLIST";
                            in.beginArray();
                            while(!in.peek().equals(JsonToken.END_ARRAY)){
                                events.add(read(in));
                            }
                            in.endArray();
                            break;
                        }
                        case "reminder":{
                            reminder = parseReminder(in);
                            break;
                        }
                        case "desc":{
                            desc = in.nextString();
                            break;
                        }
                        case "title": {
                            title = in.nextString();
                            break;
                        }
                        default: {
                            //should never be in here..
                            in.skipValue();
                        }
                    }
                }
            }
            in.endObject();
        }


        if(className.equals("EVENT")){
            e = new Event(title, desc, reminder);
        }
        else{
            e = new TodoList(title, desc, reminder);
            ((TodoList) e).addEvents(events);
        }

        return e;
    }

    private Reminder parseReminder(JsonReader in) throws IOException{
        Reminder reminder = null;
        Set<Day> days = null;
        String repeat = null;
        Calendar calendar = null;
        int hourlyRepeat = -1, minuteRepeat = -1;
        in.beginObject();
        while(!in.peek().equals(JsonToken.END_OBJECT)){
            switch(in.nextName()){
                case "calendar":{
                    calendar = parseCalendar(in);
                    break;
                }
                case "days": {
                    days = parseDays(in);
                    break;
                }
                case "jsonTag":{
                    repeat = in.nextString();
                    break;
                }
                case "hourlyRepeat":{
                    hourlyRepeat = in.nextInt();
                    break;
                }
                case "minuteRepeat":{
                    minuteRepeat = in.nextInt();
                    break;
                }
            }
        }
        //determine reminder
        if(repeat!=null){
            switch(repeat){
                case DailyReminder.REPEAT:
                    reminder = new DailyReminder(calendar);
                    break;
                case WeeklyReminder.REPEAT:
                    reminder = new WeeklyReminder(calendar, days);
                    break;
                case MonthlyReminder.REPEAT:
                    reminder = new MonthlyReminder(calendar);
                    break;
                case YearlyReminder.REPEAT:
                    reminder = new YearlyReminder(calendar);
                    break;
                case ShortDurationReminder.REPEAT:
                    reminder = new ShortDurationReminder(calendar, hourlyRepeat, minuteRepeat);
            }
        }
        else{
            reminder = new CalendarReminder(calendar);
        }
        in.endObject();
        return reminder;
    }

    private Calendar parseCalendar(JsonReader in) throws IOException{
        Calendar calendar = Calendar.getInstance();
        int year = -1, month = -1, dayOfMonth = -1, hourOfDay = -1, minute = -1;
        in.beginObject();
        while(!in.peek().equals(JsonToken.END_OBJECT)){
            switch(in.nextName()){
                case "year":
                    year = in.nextInt();
                    break;
                case "month":
                    month = in.nextInt();
                    break;
                case "dayOfMonth":
                    dayOfMonth = in.nextInt();
                    break;
                case "hourOfDay":
                    hourOfDay = in.nextInt();
                    break;
                case "minute":
                    minute = in.nextInt();
                    break;
                default:
                    in.nextInt();
            }
        }
        in.endObject();
        calendar.set(year, month, dayOfMonth, hourOfDay, minute);
        return calendar;
    }

    private Set<Day> parseDays(JsonReader in) throws IOException{
        Set<Day> days = new HashSet<>();
        in.beginArray();
        while(!in.peek().equals(JsonToken.END_ARRAY)){
            switch(in.nextString()){
                case "SUNDAY":
                    days.add(Day.SUNDAY);
                    break;
                case "MONDAY":
                    days.add(Day.MONDAY);
                    break;
                case "TUESDAY":
                    days.add(Day.TUESDAY);
                    break;
                case "WEDNESDAY":
                    days.add(Day.WEDNESDAY);
                    break;
                case "THURSDAY":
                    days.add(Day.THURSDAY);
                    break;
                case "FRIDAY":
                    days.add(Day.FRIDAY);
                    break;
                case "SATURDAY":
                    days.add(Day.SATURDAY);
                    break;
            }
        }
        in.endArray();
        return days;
    }

    private Day parseDay(JsonReader in) throws IOException{
        Day day = null;
        switch(in.nextString()){
            case "SUNDAY":
                day = Day.SUNDAY;
                break;
            case "MONDAY":
                day = Day.MONDAY;
                break;
            case "TUESDAY":
                day = Day.TUESDAY;
                break;
            case "WEDNESDAY":
                day = Day.WEDNESDAY;
                break;
            case "THURSDAY":
                day = Day.THURSDAY;
                break;
            case "FRIDAY":
                day = Day.FRIDAY;
                break;
            case "SATURDAY":
                day = Day.SATURDAY;
                break;
        }
        return day;
    }
}
