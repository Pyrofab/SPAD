package com.spadteam.spad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by Clément on 03/06/2017.
 *
 */

public class EventChosen extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_chosen);

            ((TextView) findViewById(R.id.place_event)).setText(
                    CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getFirst().getPlace());
            ((TextView) findViewById(R.id.event_time)).setText(
                    CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getFirst().getTime());
            ((TextView) findViewById(R.id.event_description)).setText(
                    CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getFirst().getDescription());
            Button delete = (Button) findViewById(R.id.button_delete_event);

            delete.setText(R.string.delete_event);
            delete.setOnClickListener((View v) -> {
                //Event.removeEvent(CreateEventActivity.getEventEdited(), this);
                //Event.refreshData(this); //gere la MAJ des evenements apres la suppression
                setResult(RESULT_OK);
                finish();
            });


    }
}
