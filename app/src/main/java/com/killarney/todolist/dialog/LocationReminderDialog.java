package com.killarney.todolist.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.killarney.todolist.R;
import com.killarney.todolist.models.reminder.LocationReminder;
import com.killarney.todolist.models.reminder.Reminder;

/**
 * Created by Anthony on 8/4/2016.
 */
public class LocationReminderDialog extends ReminderDialog {

    private final int PLACE_PICKER_REQUEST = 1;
    private static final double offset = 0.001;
    private TextView locationText;

    private String placeName;
    private LatLng latLng = null;
    private int radius = 500;
    private boolean entering = true; //alert when entering if true, exit if false

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the view and fill with relevant UI elements
        View view = inflater.inflate(R.layout.location_reminder_dialog, null);
        Button add = (Button) view.findViewById(R.id.add_button);
        add.setOnClickListener(this);

        locationText = (TextView) view.findViewById(R.id.location_text);
        locationText.setOnClickListener(this);

        //restore previous values
        Bundle b = getArguments();
        if(b!=null){
            latLng = (LatLng) b.get("latLng");
            radius = b.getInt("radius");
            entering = b.getBoolean("entering");
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_button){
            if(mListener!=null){
                Reminder reminder = new LocationReminder(placeName, latLng, radius, entering);
                mListener.setReminder(reminder);
            }
            dismiss();
        }
        else if(view.getId() == R.id.location_text){
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            if(latLng != null){
                Log.d("hello", latLng.toString());
                builder.setLatLngBounds(new LatLngBounds(latLng, new LatLng(latLng.latitude + offset, latLng.longitude + offset)));
            }
            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                placeName = place.getName().toString();
                latLng = place.getLatLng();
                locationText.setText(placeName);
            }
        }
    }

}
