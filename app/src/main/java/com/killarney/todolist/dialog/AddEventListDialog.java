package com.killarney.todolist.dialog;

import android.view.View;
import android.widget.Toast;

import com.killarney.todolist.R;
import com.killarney.todolist.exceptions.InvalidDateException;
import com.killarney.todolist.exceptions.InvalidTitleException;
import com.killarney.todolist.models.EventManager;
import com.killarney.todolist.models.TodoList;

import java.io.InvalidClassException;

/**
 * Created by Anthony on 7/7/2016.
 */
public class AddEventListDialog extends AddEventDialog{
    @Override
    public void onClick(View view){
        if(view.getId() == R.id.add_button){
            try {
                EventManager.getInstance().addEvent(year, month, day, hour, minute, title.getText().toString(), desc.getText().toString(), TodoList.class);
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
}
