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

import java.io.InvalidClassException;

/**
 * Created by Anthony on 7/7/2016.
 */
public class AddEventDialog extends AbstractEventDialog {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        Button b = (Button) v.findViewById(R.id.add_button);
        b.setText(R.string.add_event_button);
        return v;
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.add_button){
            try{
                EventManager em = EventManager.getInstance();
                em.addEvent(title.getText().toString(), desc.getText().toString(), reminder, Event.class);
                dismiss();
            } catch (InvalidDateException e) {
                Toast.makeText(getActivity(), "Invalid Date and/or Time", Toast.LENGTH_SHORT).show();
            } catch (InvalidTitleException e){
                Toast.makeText(getActivity(), "Invalid Title", Toast.LENGTH_SHORT).show();
            } catch (InvalidClassException e) {
                e.printStackTrace();
            }

        }
    }

}
