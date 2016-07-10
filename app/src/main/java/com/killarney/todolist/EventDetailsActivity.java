package com.killarney.todolist;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.killarney.todolist.models.Event;
import com.killarney.todolist.models.EventManager;
import com.killarney.todolist.models.TodoList;

import java.util.ArrayList;

/**
 * Created by Anthony on 7/7/2016.
 */
public class EventDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {

            Bundle b = getIntent().getExtras();

            DetailsFragment details = new DetailsFragment();
            details.setArguments(b);
            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();



        }
    }
}