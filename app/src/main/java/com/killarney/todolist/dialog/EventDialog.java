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
 * Created by Anthony on 7/10/2016.
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    public void showDatePickerDialog() {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = new DatePickerFragment();
        dialog.setListener(this);
        dialog.show(manager, "dateDialog");
    }

    public void showTimePickerDialog() {
        FragmentManager manager = getFragmentManager();
        TimePickerFragment dialog = new TimePickerFragment();
        dialog.setListener(this);
        dialog.show(manager, "timeDialog");
    }

    @Override
    public void setTime(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
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
        date.setText((this.month + 1) + "-" + this.day + "-" + this.year, TextView.BufferType.EDITABLE);
    }
}
