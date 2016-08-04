package com.killarney.todolist.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.killarney.todolist.R;
import com.killarney.todolist.exceptions.InvalidDateException;
import com.killarney.todolist.exceptions.InvalidTitleException;
import com.killarney.todolist.models.Event;
import com.killarney.todolist.models.EventManager;
import com.killarney.todolist.models.reminder.AbstractRepeatReminder;
import com.killarney.todolist.models.reminder.OneTimeCalendarReminder;


/**
 * Created by Anthony on 7/10/2016.
 */
public class EditEventDialog extends EventDialog {
    int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        Button b = (Button) v.findViewById(R.id.add_button);
        b.setText(R.string.edit_event_save);

        pos = getArguments().getInt("selected");
        Event e = EventManager.getInstance().getEventAtCurrentDepthAtPos(pos);

        title.setText(e.getTitle());
        desc.setText(e.getDescription());
        setReminder(e.getReminder());

        //TODO need better solution for setting default value for spinner
        if(reminder!=null){
            switch(reminder.getReminderType()){
                case OneTimeCalendarReminder.TYPE:
                    spinner.setSelection(1);
                    break;
                case AbstractRepeatReminder.TYPE:
                    spinner.setSelection(2);
                    break;
            }
        }
        return v;
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.add_button){
            try{
                EventManager em = EventManager.getInstance();
                em.editEvent(title.getText().toString(), desc.getText().toString(), reminder, pos);
                dismiss();
            } catch (InvalidDateException e) {
                Toast.makeText(getActivity(), "Invalid Date and/or Time", Toast.LENGTH_SHORT).show();
            } catch (InvalidTitleException e){
                Toast.makeText(getActivity(), "Invalid Title", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
