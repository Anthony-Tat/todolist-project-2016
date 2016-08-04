package com.killarney.todolist;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.killarney.todolist.dialog.AddEventDialog;
import com.killarney.todolist.dialog.AddEventListDialog;
import com.killarney.todolist.models.Event;
import com.killarney.todolist.models.EventManager;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    EventsFragment eventsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        EventManager.restoreInstance(getApplicationContext());

        //restore to previously open eventsfragment
        eventsFragment = new EventsFragment();
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b==null){
            //check if activity was recreated due to orientation change
            EventManager em = EventManager.getInstance();
            int[] depths = em.getDepthArray();
            if(depths.length>0){
                //remove depths and restore with eventsFragment.showDetail()
                for(int i=0;i<depths.length;i++) {
                    em.removeDepth();
                }
                b = new Bundle();
                b.putIntArray("depths", depths);
            }
        }
        eventsFragment.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.events_list, eventsFragment).commit();
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

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        else if(id == R.id.sort_options){
            final String[] items = getResources().getStringArray(R.array.sort_options_array);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getResources().getString(R.string.sort_title));
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch(item){
                        case 0:
                            EventManager.sort(Event.Comparators.TITLE);
                            break;
                        case 1:
                            EventManager.sort(Event.Comparators.REMINDER);
                            break;
                        case 2:
                            EventManager.reverse();
                            break;
                    }
                    eventsFragment.getListView().invalidateViews();
                    EventManager.saveInstance(getApplicationContext());
                    dialog.dismiss();
                }
            }).show();
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
        EventManager.saveInstance(getApplicationContext());
    }

}
