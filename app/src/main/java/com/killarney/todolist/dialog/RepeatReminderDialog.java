package com.killarney.todolist.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.killarney.todolist.R;
import com.killarney.todolist.models.Day;
import com.killarney.todolist.models.Reminder;
import com.killarney.todolist.models.Repeat;
import com.killarney.todolist.models.RepeatReminder;
import com.killarney.todolist.util.CalendarParser;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Anthony on 7/17/2016.
 */
public class RepeatReminderDialog extends TimedReminderDialog implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    Spinner spinner;
    Repeat repeat = null;
    Set<Day> days;
    LinearLayout checkboxes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the view and fill with relevant UI elements
        View view = inflater.inflate(R.layout.repeat_reminder_dialog, null);
        Button add = (Button) view.findViewById(R.id.add_button);
        add.setOnClickListener(this);

        spinner = (Spinner) view.findViewById(R.id.repeat_reminder_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.repeat_options_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        time = (TextView) view.findViewById(R.id.time_text);
        date = (TextView) view.findViewById(R.id.date_text);
        checkboxes = (LinearLayout) view.findViewById(R.id.checkbox_layout);
        days = new HashSet<>();

        //restore previous values
        Bundle b = getArguments();
        if(b!=null){
            int i = b.getInt("spinner");
            spinner.setSelection(i);
            setTime(b.getInt("hourOfDay"), b.getInt("minute"));
            switch(i){
                case 0:
                    //Daily
                    repeat = Repeat.DAILY;
                    break;
                case 1:
                    //Weekly
                    repeat = Repeat.WEEKLY;
                    setDays(CalendarParser.unparseDays(b.getString("days")));
                    break;
                case 2:
                    //Monthly
                    repeat = Repeat.MONTHLY;
                    setDate(b.getInt("year"), b.getInt("month"), b.getInt("day"));
                    break;
                case 3:
                    //Yearly
                    repeat = Repeat.YEARLY;
                    setDate(b.getInt("year"), b.getInt("month"), b.getInt("day"));
                    break;

            }
        }


        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_button){
            if(mListener!=null){
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hourOfDay, minute);
                Reminder reminder = null;
                switch(repeat){
                    case DAILY:
                        reminder = new RepeatReminder(repeat, calendar);
                        break;
                    case WEEKLY:
                        reminder = new RepeatReminder(repeat, calendar, days);
                        break;
                    case MONTHLY:
                        //specific date
                        reminder = new RepeatReminder(repeat, calendar);
                        break;
                    case YEARLY:
                        reminder = new RepeatReminder(repeat, calendar, days);
                        break;
                }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        int pid = parent.getId();
        if(pid == R.id.repeat_reminder_spinner){
            //hide current views and show relevant ones
            if(repeat!=null){
                //don't hide if it's the same selection
                if(!((position==0 && repeat==Repeat.DAILY)||(position==1 && repeat==Repeat.WEEKLY)||
                        (position==2 && repeat==Repeat.MONTHLY)||(position==3 && repeat==Repeat.YEARLY)))
                    hideOptions();
            }
            switch(position){
                case 0:
                    //Daily
                    repeat = Repeat.DAILY;
                    showDailyOptions();
                    break;
                case 1:
                    //Weekly
                    repeat = Repeat.WEEKLY;
                    showWeeklyOptions();
                    break;
                case 2:
                    //Monthly
                    repeat = Repeat.MONTHLY;
                    showMonthlyOptions();
                    break;
                case 3:
                    //Yearly
                    repeat = Repeat.YEARLY;
                    showYearlyOptions();
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            switch (buttonView.getId()){
                case R.id.sunday:
                    days.add(Day.SUNDAY);
                    break;
                case R.id.monday:
                    days.add(Day.MONDAY);
                    break;
                case R.id.tuesday:
                    days.add(Day.TUESDAY);
                    break;
                case R.id.wednesday:
                    days.add(Day.WEDNESDAY);
                    break;
                case R.id.thursday:
                    days.add(Day.THURSDAY);
                    break;
                case R.id.friday:
                    days.add(Day.FRIDAY);
                    break;
                case R.id.saturday:
                    days.add(Day.SATURDAY);
                    break;
            }
        }
        else{
            switch (buttonView.getId()){
                case R.id.sunday:
                    days.remove(Day.SUNDAY);
                    break;
                case R.id.monday:
                    days.remove(Day.MONDAY);
                    break;
                case R.id.tuesday:
                    days.remove(Day.TUESDAY);
                    break;
                case R.id.wednesday:
                    days.remove(Day.WEDNESDAY);
                    break;
                case R.id.thursday:
                    days.remove(Day.THURSDAY);
                    break;
                case R.id.friday:
                    days.remove(Day.FRIDAY);
                    break;
                case R.id.saturday:
                    days.remove(Day.SATURDAY);
                    break;
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void showDailyOptions() {
        showTime();
    }

    private void showWeeklyOptions() {
        showTime();
        showDays();
    }

    private void showMonthlyOptions() {
        showTime();
        showDate();
    }

    private void showYearlyOptions() {
        showTime();
        showDate();
    }

    private void hideOptions(){
        //repeat is non-null
        switch(repeat){
            case DAILY:
                break;
            case WEEKLY:
                hideDays();
                break;
            case MONTHLY:
                hideDate();
                break;
            case YEARLY:
                hideDate();
                break;
        }
    }

    private void showDays(){
        LinearLayout l = (LinearLayout) getView().findViewById(R.id.checkbox_layout);
        l.setClickable(true);
        l.setVisibility(View.VISIBLE);
        for(int i=0; i<l.getChildCount(); i++){
            ((CheckBox) l.getChildAt(i)).setOnCheckedChangeListener(this);
        }
    }

    private void hideDays(){
        View v = getView();
        LinearLayout l = (LinearLayout) v.findViewById(R.id.checkbox_layout);
        l.setClickable(false);
        l.setVisibility(View.GONE);
        for(int i=0; i<l.getChildCount(); i++){
            ((CheckBox) l.getChildAt(i)).setOnCheckedChangeListener(null);
        }
    }

    private void showTime(){
        time.setVisibility(View.VISIBLE);
        time.setClickable(true);
        time.setOnClickListener(this);
    }

    private void showDate(){
        date.setVisibility(View.VISIBLE);
        date.setClickable(true);
        date.setOnClickListener(this);
    }

    private void hideDate(){
        date.setVisibility(View.GONE);
        date.setClickable(false);
        date.setOnClickListener(null);
    }

    private void setDays(Set<Day> days) {
        for (Day d : days) {
            ((CheckBox) checkboxes.getChildAt(d.toInt())).setChecked(true);
            this.days.add(d); //has to be called since onCheckChanged listener will not be set up until after onCreateView
        }
    }

}
