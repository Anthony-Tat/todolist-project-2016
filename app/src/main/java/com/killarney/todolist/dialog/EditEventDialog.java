package com.killarney.todolist.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.killarney.todolist.R;
import com.killarney.todolist.exceptions.InvalidDateException;
import com.killarney.todolist.exceptions.InvalidTitleException;
import com.killarney.todolist.models.Event;
import com.killarney.todolist.models.EventManager;

import java.util.Calendar;

/**
 * Created by Anthony on 7/10/2016.
 */
public class EditEventDialog extends EventDialog{
    int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        Button b = (Button) v.findViewById(R.id.add_button);
        b.setText(R.string.add_event_button);

        pos = getArguments().getInt("selected");
        Event e = EventManager.getInstance().getEventAtCurrentDepthAtPos(pos);

        title = (EditText) v.findViewById(R.id.title_text);
        title.setText(e.getTitle());

        desc = (EditText) v.findViewById(R.id.desc_text);
        desc.setText(e.getDescription());

        Calendar c = e.getCalendar();

        setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
        setTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

        return v;
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.add_button){
            try{
                EventManager em = EventManager.getInstance();
                em.editEvent(year, month, day, hourOfDay, minute, title.getText().toString(), desc.getText().toString(), pos);
                dismiss();
            } catch (InvalidDateException e) {
                Toast.makeText(getActivity(), "Invalid Date and/or Time", Toast.LENGTH_SHORT).show();
            } catch (InvalidTitleException e){
                Toast.makeText(getActivity(), "Invalid Title", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId() == R.id.date_text){
            showDatePickerDialog();
        }
        else if(view.getId() == R.id.time_text){
            showTimePickerDialog();
        }
    }
}
