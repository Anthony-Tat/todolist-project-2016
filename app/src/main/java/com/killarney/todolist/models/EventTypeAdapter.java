package com.killarney.todolist.models;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        int year = -1, month = -1, dayOfMonth = -1, hourOfDay = -1, minute = -1;
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
                        case "calendar":{
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

        if(title.length()==0 || desc.length()==0 || year == -1 ||
                month == -1 || dayOfMonth == -1 || hourOfDay == -1 || minute == -1){
            throw new IOException();
        }
        else{
            if(className.equals("EVENT"))
                e = new Event(year, month, dayOfMonth, hourOfDay, minute, title, desc);
            else{
                e = new TodoList(year, month, dayOfMonth, hourOfDay, minute, title, desc);
                ((TodoList) e).addEvents(events);
            }
        }
        return e;
    }
}
