package com.killarney.todolist.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.killarney.todolist.R;
import com.killarney.todolist.models.reminder.AbstractRepeatReminder;
import com.killarney.todolist.models.reminder.DailyReminder;
import com.killarney.todolist.models.reminder.MonthlyReminder;
import com.killarney.todolist.models.reminder.Reminder;
import com.killarney.todolist.models.reminder.CalendarReminder;
import com.killarney.todolist.models.reminder.WeeklyReminder;
import com.killarney.todolist.models.reminder.YearlyReminder;
import com.killarney.todolist.util.CalendarParser;

import java.util.Calendar;

/**
 * Superclass of all dialogs where an event needs to be altered or added
 */
public abstract class EventDialog extends DialogFragment implements View.OnClickListener, ReminderDialog.OnCreateReminder, AdapterView.OnItemSelectedListener {
    EditText title;
    EditText desc;
    TextView reminderText;
    Spinner spinner;
    Reminder reminder;

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
        reminderText = (TextView) view.findViewById(R.id.reminder_text);
        reminderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(reminder.getReminderType()){
                    case CalendarReminder.TYPE:
                        showCalendarReminderDialog();
                        break;
                    case AbstractRepeatReminder.TYPE:
                        showRepeatReminderDialog();
                        break;
                    //TODO other reminders
                }
            }
        });

        spinner = (Spinner) view.findViewById(R.id.reminder_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.reminder_options_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public abstract void onClick(View view);

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO bug can't select same item twice -> can't edit
        switch (position) {
            case 0:
                //none
                reminder = null;
                break;
            case 1:
                //calendar
                showCalendarReminderDialog();
                break;
            case 2:
                //repeated
                showRepeatReminderDialog();
                break;
            case 3:
                //location
                //TODO location
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void setReminder(Reminder reminder) {
        if(this.reminder==null)
            reminderText.setClickable(true);
        this.reminder = reminder;
        if(reminder!=null)
            reminderText.setText(reminder.toFormattedString());
    }

    protected void showCalendarReminderDialog() {
        FragmentManager manager = getFragmentManager();
        TimedReminderDialog dialog = new CalendarReminderDialog();

        //make sure the default values are set to current
        if(reminder!=null){
            if(reminder.getReminderType()==CalendarReminder.TYPE){
                Bundle b = new Bundle();
                Calendar c = ((CalendarReminder) reminder).getCalendar();
                b.putInt("year", c.get(Calendar.YEAR));
                b.putInt("month", c.get(Calendar.MONTH));
                b.putInt("day", c.get(Calendar.DATE));
                b.putInt("hourOfDay", c.get(Calendar.HOUR_OF_DAY));
                b.putInt("minute", c.get(Calendar.MINUTE));
                dialog.setArguments(b);
            }
        }
        dialog.setListener(this);
        dialog.show(manager, "reminderDialog");
    }

    protected void showRepeatReminderDialog() {
        FragmentManager manager = getFragmentManager();
        RepeatReminderDialog dialog = new RepeatReminderDialog();

        //make sure the default values are set to current
        if(reminder!=null){
            if(reminder.getReminderType().equals(AbstractRepeatReminder.TYPE)){
                Bundle b = new Bundle();
                AbstractRepeatReminder repeatReminder = (AbstractRepeatReminder) reminder;
                Calendar c = repeatReminder.getCalendar();

                switch(repeatReminder.getRepeatType()){
                    case DailyReminder.REPEAT: {
                        b.putInt("spinner", 0);
                        b.putInt("hourOfDay", c.get(Calendar.HOUR_OF_DAY));
                        b.putInt("minute", c.get(Calendar.MINUTE));
                        break;
                    }
                    case WeeklyReminder.REPEAT: {
                        b.putInt("spinner", 1);
                        b.putInt("hourOfDay", c.get(Calendar.HOUR_OF_DAY));
                        b.putInt("minute", c.get(Calendar.MINUTE));
                        b.putString("days", CalendarParser.parseDays(((WeeklyReminder) reminder).getDays()));
                        break;
                    }
                    case MonthlyReminder.REPEAT: {
                        b.putInt("spinner", 2);
                        b.putInt("year", c.get(Calendar.YEAR));
                        b.putInt("month", c.get(Calendar.MONTH));
                        b.putInt("day", c.get(Calendar.DATE));
                        b.putInt("hourOfDay", c.get(Calendar.HOUR_OF_DAY));
                        b.putInt("minute", c.get(Calendar.MINUTE));
                        break;
                    }
                    case YearlyReminder.REPEAT: {
                        b.putInt("spinner", 3);
                        b.putInt("year", c.get(Calendar.YEAR));
                        b.putInt("month", c.get(Calendar.MONTH));
                        b.putInt("day", c.get(Calendar.DATE));
                        b.putInt("hourOfDay", c.get(Calendar.HOUR_OF_DAY));
                        b.putInt("minute", c.get(Calendar.MINUTE));
                        break;
                    }
                }
                dialog.setArguments(b);
            }
        }
        dialog.setListener(this);
        dialog.show(manager, "reminderDialog");
    }
}
