package com.killarney.todolist;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.killarney.todolist.dialog.AddEventDialog;
import com.killarney.todolist.dialog.AddEventListDialog;
import com.killarney.todolist.models.Event;
import com.killarney.todolist.models.EventManager;
import com.killarney.todolist.models.EventTypeAdapter;
import com.killarney.todolist.models.TodoList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    final static String fileName = "eventManager.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventsFragment ef = new EventsFragment();
        getFragmentManager().beginTransaction().add(R.id.events_list, ef).commit();

        //initialize button to create new events
        FloatingActionButton addEventButton = (FloatingActionButton) findViewById(R.id.add_event_button);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(new CharSequence[]{getString(R.string.add_event),
                        getString(R.string.add_event_list)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(i==0){
                            //add event
                            showAddEventDialog();
                        }
                        else if(i==1){
                            //add todolist
                            showAddEventListDialog();
                        }
                    }

                });
                builder.create().show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //restore EventManager
        InputStream in = null;
        try {
            in = this.openFileInput(fileName);
            EventManager.restoreInstance(readJsonStream(in));
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
    }

    private void showAddEventDialog() {
        FragmentManager manager = getFragmentManager();
        AddEventDialog dialog = new AddEventDialog();
        dialog.show(manager, "eventDialog");
    }

    private void showAddEventListDialog() {
        FragmentManager manager = getFragmentManager();
        AddEventListDialog dialog = new AddEventListDialog();
        dialog.show(manager, "eventDialog");
    }

    private List<Event> readJsonStream(InputStream in) throws IOException {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //goes back to previous EventsFragment, if possible
            EventManager.getInstance().removeDepth();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //TODO
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPause(){
        super.onPause();
        //write the list of events in EventManager to file
        FileOutputStream out = null;

        try {
            out = this.openFileOutput(fileName, MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.setIndent("  ");
            saveEvents(EventManager.getInstance().getEvents(), writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out!=null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void saveEvents(List<Event> events, JsonWriter writer) throws IOException {
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
