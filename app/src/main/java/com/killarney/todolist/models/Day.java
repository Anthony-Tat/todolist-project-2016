package com.killarney.todolist.models;

/**
 * Created by Anthony on 7/17/2016.
 */
public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

    @Override
    public String toString() {
        switch(this) {
            case SUNDAY: return "Sundays";
            case MONDAY: return "Mondays";
            case TUESDAY: return "Tuesdays";
            case WEDNESDAY: return "Wednesdays";
            case THURSDAY: return "Thursdays";
            case FRIDAY: return "Fridays";
            case SATURDAY: return "Saturdays";
            default: throw new IllegalArgumentException();
        }
    }

    public int toInt(){
        switch(this) {
            case SUNDAY: return 0;
            case MONDAY: return 1;
            case TUESDAY: return 2;
            case WEDNESDAY: return 3;
            case THURSDAY: return 4;
            case FRIDAY: return 5;
            case SATURDAY: return 6;
            default: throw new IllegalArgumentException();
        }
    }
}
