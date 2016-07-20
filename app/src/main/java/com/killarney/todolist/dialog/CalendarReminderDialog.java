package com.killarney.todolist.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.killarney.todolist.R;
import com.killarney.todolist.models.CalendarReminder;
import com.killarney.todolist.models.Reminder;

import java.util.Calendar;

/**
 * Created by Anthony on 7/17/2016.
 */
public class CalendarReminderDialog extends AbstractTimedReminderDialog {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the view and fill with relevant UI elements
        View view = inflater.inflate(R.layout.calendar_reminder_dialog, null);
        Button add = (Button) view.findViewById(R.id.add_button);
        add.setOnClickListener(this);

        date = (TextView) view.findViewById(R.id.date_text);
        date.setOnClickListener(this);
        time = (TextView) view.findViewById(R.id.time_text);
        time.setOnClickListener(this);

        //restore previous values
        Bundle b = getArguments();
        if(b!=null){
            setDate(b.getInt("year"), b.getInt("month"), b.getInt("day"));
            setTime(b.getInt("hourOfDay"), b.getInt("minute"));
        }

        return view;
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_button){
            if(mListener!=null){
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hourOfDay, minute);
                Reminder reminder = new CalendarReminder(calendar);
                mListener.setReminder(reminder);
            }
            dismiss();

        }
        else if(view.getId() == R.id.date_text){
            showDatePickerDialog();
        }
        else if(view.getId() == R.id.time_text){
            showTimePickerDialog();
        }
    }

}
