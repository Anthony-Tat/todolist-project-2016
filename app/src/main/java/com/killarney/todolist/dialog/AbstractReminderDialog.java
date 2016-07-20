package com.killarney.todolist.dialog;

import android.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.killarney.todolist.R;
import com.killarney.todolist.models.Reminder;

/**
 * Created by Anthony on 7/17/2016.
 */
public abstract class AbstractReminderDialog extends DialogFragment implements View.OnClickListener {

    protected OnCreateReminder mListener;

    @Override
    public abstract void onClick(View view);

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setListener(OnCreateReminder listener) {
        mListener = listener;
    }

    public interface OnCreateReminder{
        void setReminder(Reminder reminder);
    }


}
