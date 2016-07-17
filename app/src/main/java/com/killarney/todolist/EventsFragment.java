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

import com.killarney.todolist.dialog.EditEventDialog;
import com.killarney.todolist.models.Event;
import com.killarney.todolist.models.EventManager;
import com.killarney.todolist.models.TodoList;

import java.util.List;

/**
 * Created by Anthony on 7/7/2016.
 */
public class EventsFragment extends ListFragment implements EventManager.EventChangedListener{
    boolean mDualPane;
    int mCurCheckPosition = 0;

    List<Event> events;

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
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
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
                            //TODO
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
    public void onEventChanged(int msg){
        //Ensures that there are views to invalidate
        if(this.isAdded())
            getListView().invalidateViews();
    }


}
