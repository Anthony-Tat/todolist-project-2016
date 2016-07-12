package com.killarney.todolist.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.killarney.todolist.R;

/**
 * Superclass of all dialogs where an event needs to be altered or added
 */
public abstract class EventDialog extends DialogFragment implements View.OnClickListener, TimePickerFragment.TimePickerListener, DatePickerFragment.DatePickerListener {
    EditText time;
    EditText date;
    EditText title;
    EditText desc;
    int year;
    int month;
    int day;
    int hourOfDay;
    int minute;

    /**
     * Child classes should override to set the text of R.id.add_button
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the view and fill with relevant UI elements
        View view = inflater.inflate(R.layout.add_event_dialog, null);
        Button add = (Button) view.findViewById(R.id.add_button);
        add.setOnClickListener(this);
        title = (EditText) view.findViewById(R.id.title_text);
        desc = (EditText) view.findViewById(R.id.desc_text);
        date = (EditText) view.findViewById(R.id.date_text);
        date.setOnClickListener(this);
        time = (EditText) view.findViewById(R.id.time_text);
        time.setOnClickListener(this);

        return view;
    }

    @Override
    public abstract void onClick(View view);

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * Allows user to select a date
     */
    protected void showDatePickerDialog() {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = new DatePickerFragment();
        dialog.setListener(this);
        dialog.show(manager, "dateDialog");
    }

    /**
     * Allows user to select a time
     */
    protected void showTimePickerDialog() {
        FragmentManager manager = getFragmentManager();
        TimePickerFragment dialog = new TimePickerFragment();
        dialog.setListener(this);
        dialog.show(manager, "timeDialog");
    }

    @Override
    public void setTime(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        //format the displayed time
        if(minute >= 10)
            time.setText(this.hourOfDay + ":" + this.minute, TextView.BufferType.EDITABLE);
        else
            time.setText(this.hourOfDay + ":" + this.minute + "0", TextView.BufferType.EDITABLE);
    }

    @Override
    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        //format the displayed text
        date.setText((this.month + 1) + "-" + this.day + "-" + this.year, TextView.BufferType.EDITABLE);
    }
}
