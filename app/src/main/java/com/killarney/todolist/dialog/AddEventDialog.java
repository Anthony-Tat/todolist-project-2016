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
import android.widget.Toast;

import com.killarney.todolist.R;
import com.killarney.todolist.exceptions.InvalidDateException;
import com.killarney.todolist.exceptions.InvalidTitleException;
import com.killarney.todolist.models.Event;
import com.killarney.todolist.models.EventManager;
import com.killarney.todolist.models.TodoList;

import java.io.InvalidClassException;
import java.util.List;

/**
 * Created by Anthony on 7/7/2016.
 */
public class AddEventDialog extends DialogFragment implements View.OnClickListener, TimePickerFragment.TimePickerListener, DatePickerFragment.DatePickerListener {

    EditText time, date, title, desc;
    int year, month, day, hour, minute;

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
    public void onClick(View view){
        if(view.getId() == R.id.add_button){
            try{
                EventManager em = EventManager.getInstance();
                em.addEvent(year, month, day, hour, minute, title.getText().toString(), desc.getText().toString(), Event.class);
                dismiss();
            } catch (InvalidDateException e) {
                Toast.makeText(getActivity(), "Invalid Date and/or Time", Toast.LENGTH_SHORT).show();
            } catch (InvalidTitleException e){
                Toast.makeText(getActivity(), "Invalid Title", Toast.LENGTH_SHORT).show();
            } catch (InvalidClassException e) {
                e.printStackTrace();
            }

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
        hour = hourOfDay;
        this.minute = minute;
        if(minute >= 10)
            time.setText(this.hour + ":" + this.minute, TextView.BufferType.EDITABLE);
        else
            time.setText(this.hour + ":" + this.minute + "0", TextView.BufferType.EDITABLE);
    }

    @Override
    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        date.setText((this.month + 1) + "-" + this.day + "-" + this.year, TextView.BufferType.EDITABLE);
    }

}
