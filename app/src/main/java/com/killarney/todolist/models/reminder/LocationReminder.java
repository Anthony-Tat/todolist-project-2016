package com.killarney.todolist.models.reminder;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

/**
 * An immutable implementation of the reminder interface representing a location-based reminder.
 *
 * Created by Anthony on 8/4/2016.
 */
public final class LocationReminder implements Reminder{

    public static final String TYPE = "Location";
    private final String placeName;
    private final LatLng latLng;
    private final int radius;
    private final boolean entering; //alert when entering if true, exit if false

    public LocationReminder(String placeName, LatLng latLng, int radius, boolean entering){
        this.placeName = placeName;
        this.latLng = latLng;
        this.radius = radius;
        this.entering = entering;
    }

    public LatLng getLatLng(){
        return latLng;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isEntering() {
        return entering;
    }

    @Override
    public String toFormattedString() {
        String action = entering ? "Entering" : "Exiting";
        return action +  " " + radius + " km of " + placeName;
    }

    @Override
    public String getReminderType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationReminder)) return false;

        LocationReminder that = (LocationReminder) o;

        if (!placeName.equals(that.placeName)) return false;
        return latLng.equals(that.latLng);

    }

    @Override
    public int hashCode() {
        int result = placeName.hashCode();
        result = 31 * result + latLng.hashCode();
        return result;
    }

    @Override
    public int compareTo(@NonNull Reminder another) {
        if(!getReminderType().equals(another.getReminderType())){
            return this.getReminderType().compareTo(another.getReminderType());
        }
        else{
            return this.placeName.compareTo(((LocationReminder) another).placeName);
        }
    }
}
