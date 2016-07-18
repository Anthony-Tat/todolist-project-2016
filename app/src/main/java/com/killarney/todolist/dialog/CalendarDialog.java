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
import com.killarney.todolist.models.CalendarReminder;
import com.killarney.todolist.models.Reminder;

import java.util.Calendar;

/**
 * Created by Anthony on 7/17/2016.
 */
public class CalendarDialog extends DialogFragment implements View.OnClickListener, TimePickerFragment.TimePickerListener, DatePickerFragment.DatePickerListener {

    EditText time;
    EditText date;
    int year;
    int month;
    int day;
    int hourOfDay;
    int minute;

    private CalendarDialogListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the view and fill with relevant UI elements
        View view = inflater.inflate(R.layout.calendar_dialog, null);
        Button add = (Button) view.findViewById(R.id.add_button);
        add.setOnClickListener(this);

        date = (EditText) view.findViewById(R.id.date_text);
        date.setOnClickListener(this);
        time = (EditText) view.findViewById(R.id.time_text);
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
                //TODO Reminder Exception
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

    public void setListener(CalendarDialogListener listener) {
        mListener = listener;
    }

    public interface CalendarDialogListener{
        void setReminder(Reminder reminder);
    }
}
