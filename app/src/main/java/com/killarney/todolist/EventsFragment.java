package com.killarney.todolist;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.killarney.todolist.dialog.EditEventDialog;
import com.killarney.todolist.models.Event;
import com.killarney.todolist.models.EventManager;
import com.killarney.todolist.models.TodoList;
import com.killarney.todolist.util.ReminderManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Anthony on 7/7/2016.
 */
public class EventsFragment extends ListFragment implements EventManager.EventChangedListener{
    boolean mDualPane;
    int mCurCheckPosition = 0;

    private List<Event> events;
    //depths to pass to nested events when restoring states
    private int[] depths = null;

    public void setEvents(List<Event> events){
        this.events = events;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(events==null) {
            events = EventManager.getInstance().getEvents();
        }

        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, events));

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }

        EventManager.getInstance().addListener(this);

        //allows user to edit or delete an event by pressing and holding on an item
        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setItems(new CharSequence[]{getString(R.string.edit_event),
                        getString(R.string.delete_event)}, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(i==0){
                            //edit
                            FragmentManager manager = getFragmentManager();
                            EditEventDialog eed = new EditEventDialog();
                            Bundle b = new Bundle();
                            b.putInt("selected", position);
                            eed.setArguments(b);
                            eed.show(manager, "eventDialog");

                        }
                        else if(i==1){
                            //delete
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                            dialogBuilder.setMessage(getString(R.string.confirm_prompt));
                            dialogBuilder.setPositiveButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EventManager.getInstance().remove(position);
                                }
                            });
                            dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            });
                            dialogBuilder.create().show();
                        }
                    }
                });

                dialogBuilder.create().show();
                return true;
            }

        });

        //restore previous depths if opened from notifications
        Bundle b = getArguments();
        if(b!=null){
            int[] depths = b.getIntArray("depths");
            if(depths!=null) {
                if (depths.length > 1) {
                    this.depths = Arrays.copyOfRange(depths, 1, depths.length);
                }
                showDetails(depths[0]);
                getArguments().clear();

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            //prevents an empty list from being used; will cause an error
            if(EventManager.getInstance().getEventsAtCurrentDepth().size()>0) {
                getListView().setItemChecked(index, true);

                Event e = events.get(index);
                //see if selected item is a TodoList or an Event
                if(e.getClass() == TodoList.class) {
                    //create a new EventsFragment to be used that contains the selected list of events
                    EventsFragment f = new EventsFragment();
                    f.setEvents(((TodoList) e).getEvents());
                    if(depths!=null) {
                        Bundle b = new Bundle();
                        b.putIntArray("depths", depths);
                        f.setArguments(b);
                    }
                    //prevent back presses from restoring deleted states
                    //i.e. opening fragments 1->2->3, activity destroyed, backing 3->2->1 would lead to 1->3 if pressed, skipping 2
                    depths = null;
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.events_list, f);
                    //a new details fragment is initialized to prevent previous text to be displayed
                    ft.replace(R.id.details, DetailsFragment.newInstance(-1));
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    EventManager.getInstance().addDepth(index);
                }
                else {
                    // Check what fragment is currently shown, replace if needed.
                    DetailsFragment details = (DetailsFragment)
                            getFragmentManager().findFragmentById(R.id.details);
                    if (details == null || details.getShownIndex() != index) {
                        details = DetailsFragment.newInstance(index);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.details, details);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();
                    }
                }
            }

        }
        else {
            //vertical orientation
            Event e = events.get(index);
            //see if selected item is a TodoList or an Event
            if(e.getClass() == TodoList.class) {
                //replaces the current EventsFragment with a new one containing the selected list of events
                EventsFragment f = new EventsFragment();
                f.setEvents(((TodoList) e).getEvents());
                if(depths!=null){
                    Bundle b = new Bundle();
                    b.putIntArray("depths", depths);
                    f.setArguments(b);
                }
                //prevent back presses from restoring deleted states
                //i.e. opening fragments 1->2->3, activity destroyed, backing 3->2->1 would lead to 1->3 if pressed, skipping 2
                depths = null;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.events_list, f);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                EventManager.getInstance().addDepth(index);

            }
            else {
                //start a new activity to display the DetailsFragment on
                Intent intent = new Intent();
                intent.setClass(getActivity(), EventDetailsActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onEventChanged(int msg, Event e){
        //Ensures that there are views to invalidate
        if(this.isAdded()){
            getListView().invalidateViews();
            EventManager em = EventManager.getInstance();
            String str ="";
            switch(msg){
                case EventManager.EVENT_ADDED:
                    str = "Event Added";
                    int[] temp = em.getDepthArray();
                    int[] depths = Arrays.copyOf(temp, temp.length+1);
                    depths[temp.length] = em.indexOf(e);
                    ReminderManager.setAlarm(getActivity().getApplicationContext(), e, depths);
                    break;
                case EventManager.EVENT_REMOVED:
                    str = "Event Removed";
                    ReminderManager.cancelAlarm(getActivity().getApplicationContext(), e);
                    EventManager.restoreAlarms(getActivity().getApplicationContext(), em.getEventsAtCurrentDepth(), em.getDepthArray());
                    break;
                case EventManager.EVENT_CHANGED:
                    str = "Event Changed";
                    break;
                case EventManager.MULTIPLE_EVENTS_REMOVED:
                    str = "Todolist removed";
                    List<Event> events = ((TodoList) e).getEvents();
                    for (Event event : events) {
                        ReminderManager.cancelAlarm(getActivity().getApplicationContext(), event);
                    }
                    break;
            }
            if(em.isReady()) {
                EventManager.saveInstance(getActivity().getApplicationContext());
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        }
    }


}
