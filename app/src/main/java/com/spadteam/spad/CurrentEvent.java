package com.spadteam.spad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Clément on 03/06/2017.
 *
 */

public class CurrentEvent extends Activity {

    protected EventArrayAdapter adapter;
    static final int EDIT_EVENT_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_event);

        final ListView listview = (ListView) findViewById(R.id.event_list);

        adapter = new EventArrayAdapter(this);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent i = new Intent(CurrentEvent.this, EventChosen.class);

            CreateEventActivity.eventEdited = Event.getEvent(position);

            startActivityForResult(i, EDIT_EVENT_REQUEST);
        });
    }




    private class EventArrayAdapter extends BaseAdapter {

        Context context;

        EventArrayAdapter(Context context) {
            super();
            this.context = context;
        }

        @Nullable
        @Override
        public com.spadteam.spad.Event getItem(int position) {
            return com.spadteam.spad.Event.getEvents(getContext()).get(position);
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        Context getContext() {
            return this.context;
        }

        @Override
        public int getCount() {
            return com.spadteam.spad.Event.getEvents(context).size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if(v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.eventlistrow, null);
            }
            com.spadteam.spad.Event e = getItem(position);

            if(e != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.event_row_id);
                TextView tt2 = (TextView) v.findViewById(R.id.event_time); //fait reference a la row d event
                TextView tt3 = (TextView) v.findViewById(R.id.event_description);

                if(tt1 != null) {
                    tt1.setText(e.getPlace());
                }
                if(tt2 != null) {
                    tt2.setText(e.getTime());
                }
                if(tt3 != null) {
                    tt3.setText(e.getDescription());
                }
            }
            return v;
        }
    }
}
