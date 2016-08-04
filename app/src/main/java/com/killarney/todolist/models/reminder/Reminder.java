package com.killarney.todolist.models.reminder;

/**
 * A representation of a reminder that will notify users when a set condition is met.
 * The comparison of reminders should assert that the paramter is @Nonnull.
 * Comparison of different subclasses of reminders are compared by getReminderType().
 * Comparison of similar subclasses should be implemented based on their main characteristic (i.e. calendar).
 *
 * Created by Anthony on 7/17/2016.
 */
public interface Reminder extends Comparable<Reminder>{
    String toFormattedString();

    String getReminderType();
}
