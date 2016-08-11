package com.killarney.todolist.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.killarney.todolist.R;
import com.killarney.todolist.models.Day;
import com.killarney.todolist.models.reminder.DailyReminder;
import com.killarney.todolist.models.reminder.MonthlyReminder;
import com.killarney.todolist.models.reminder.Reminder;
import com.killarney.todolist.models.reminder.ShortDurationReminder;
import com.killarney.todolist.models.reminder.WeeklyReminder;
import com.killarney.todolist.models.reminder.YearlyReminder;
import com.killarney.todolist.util.CalendarParser;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Anthony on 7/17/2016.
 */
public class RepeatReminderDialog extends TimedReminderDialog implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, NumberPicker.OnValueChangeListener {

    public static final int DAILY_INDEX=0, WEEKLY_INDEX=1, MONTHLY_INDEX=2, YEARLY_INDEX=3, SHORT_DURATION_INDEX=4;
    Spinner spinner;
    String repeat = null;
    Set<Day> days;
    LinearLayout checkboxes;
    int hourlyRepeat=0, minuteRepeat=0;
    static final int hourlyMin=0, minuteMin=0, hourlyMax=48, minuteMax=59;
    NumberPicker hourPicker, minutePicker;

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

        hourPicker = (NumberPicker) view.findViewById(R.id.hour_picker);
        hourPicker.setMaxValue(hourlyMax);
        hourPicker.setMinValue(hourlyMin);
        minutePicker = (NumberPicker) view.findViewById(R.id.minute_picker);
        minutePicker.setMaxValue(minuteMax);
        minutePicker.setMinValue(minuteMin);

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
                case DAILY_INDEX:
                    repeat = DailyReminder.REPEAT;
                    break;
                case WEEKLY_INDEX:
                    repeat = WeeklyReminder.REPEAT;
                    setDays(CalendarParser.unparseDays(b.getString("days")));
                    break;
                case MONTHLY_INDEX:
                    repeat = MonthlyReminder.REPEAT;
                    setDate(b.getInt("year"), b.getInt("month"), b.getInt("day"));
                    break;
                case YEARLY_INDEX:
                    repeat =YearlyReminder.REPEAT;
                    setDate(b.getInt("year"), b.getInt("month"), b.getInt("day"));
                    break;
                case SHORT_DURATION_INDEX:
                    repeat = ShortDurationReminder.REPEAT;
                    setDate(b.getInt("year"), b.getInt("month"), b.getInt("day"));
                    setHourAndMinuteRepeat(b.getInt("hourlyRepeat"), b.getInt("minuteRepeat"));
            }
        }


        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_button){
            boolean successful = false;
            String msg = "Unexpected Error";
            if(mListener!=null){
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hourOfDay, minute);
                Reminder reminder = null;
                switch(repeat){
                    case DailyReminder.REPEAT:
                        reminder = new DailyReminder(calendar);
                        successful = true;
                        break;
                    case WeeklyReminder.REPEAT:
                        if(days.size()>0){
                            reminder = new WeeklyReminder(calendar, days);
                            successful = true;
                        }
                        else{
                            msg = "No days have been selected";
                        }
                        break;
                    case MonthlyReminder.REPEAT:
                        reminder = new MonthlyReminder(calendar);
                        successful = true;
                        break;
                    case YearlyReminder.REPEAT:
                        reminder = new YearlyReminder(calendar);
                        successful = true;
                        break;
                    case ShortDurationReminder.REPEAT:
                        if(hourlyRepeat==0 && minuteRepeat==0) {
                            msg = "Invalid Interval";
                        }
                        else{
                            reminder = new ShortDurationReminder(calendar, hourlyRepeat, minuteRepeat);
                            successful = true;
                        }
                        break;
                }
                mListener.setReminder(reminder);
            }
            if(successful){
                dismiss();
            }
            else{
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch(picker.getId()){
            case R.id.hour_picker:
                hourlyRepeat = newVal;
                break;
            case R.id.minute_picker:
                minuteRepeat = newVal;
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        int pid = parent.getId();
        if(pid == R.id.repeat_reminder_spinner){
            //hide current views and show new ones
            if(repeat!=null){
                //don't hide if it's the same selection
                if(!((position==0 && repeat.equals(DailyReminder.REPEAT))||(position==1 && repeat.equals(WeeklyReminder.REPEAT))||
                        (position==2 && repeat.equals(MonthlyReminder.REPEAT))||(position==3 && repeat.equals(YearlyReminder.REPEAT))))
                    hideOptions();
            }
            switch(position){
                case DAILY_INDEX:
                    repeat = DailyReminder.REPEAT;
                    showDailyOptions();
                    break;
                case WEEKLY_INDEX:
                    repeat = WeeklyReminder.REPEAT;
                    showWeeklyOptions();
                    break;
                case MONTHLY_INDEX:
                    repeat = MonthlyReminder.REPEAT;
                    showMonthlyOptions();
                    break;
                case YEARLY_INDEX:
                    repeat = YearlyReminder.REPEAT;
                    showYearlyOptions();
                    break;
                case SHORT_DURATION_INDEX:
                    repeat = ShortDurationReminder.REPEAT;
                    showShortDurationOptions();
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

    private void showShortDurationOptions(){
        showTime();
        showDate();
        showHourPicker();
        showMinutePicker();
    }

    private void hideOptions(){
        //repeat is non-null
        switch(repeat){
            case DailyReminder.REPEAT:
                break;
            case WeeklyReminder.REPEAT:
                hideDays();
                break;
            case MonthlyReminder.REPEAT:
                hideDate();
                break;
            case YearlyReminder.REPEAT:
                hideDate();
                break;
            case ShortDurationReminder.REPEAT:
                hideHourPicker();
                hideMinutePicker();
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

    private void showHourPicker(){
        hourPicker.setVisibility(View.VISIBLE);
        hourPicker.setClickable(true);
        hourPicker.setOnValueChangedListener(this);
    }

    private void hideHourPicker(){
        hourPicker.setVisibility(View.GONE);
        hourPicker.setClickable(false);
        hourPicker.setOnClickListener(null);
    }

    private void showMinutePicker(){
        minutePicker.setVisibility(View.VISIBLE);
        minutePicker.setClickable(true);
        minutePicker.setOnValueChangedListener(this);
    }

    private void hideMinutePicker(){
        minutePicker.setVisibility(View.GONE);
        minutePicker.setClickable(false);
        minutePicker.setOnValueChangedListener(null);
    }

    private void setHourAndMinuteRepeat(int hourlyRepeat, int minuteRepeat) {
        hourPicker.setValue(hourlyRepeat);
        this.hourlyRepeat = hourlyRepeat;
        minutePicker.setValue(minuteRepeat);
        this.minuteRepeat = minuteRepeat;
    }
}
