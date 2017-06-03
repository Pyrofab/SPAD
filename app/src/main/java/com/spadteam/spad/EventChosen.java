package com.spadteam.spad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Clément on 03/06/2017.
 */

public class EventChosen extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_chosen);

        if(CreateEvent.getEventEdited() != null) {
            ((TextView) findViewById(R.id.place_event)).setText(CreateEvent.getEventEdited().getPlace());
            ((TextView) findViewById(R.id.event_time)).setText(CreateEvent.getEventEdited().getTime());
            ((TextView) findViewById(R.id.event_description)).setText(CreateEvent.getEventEdited().getDescription());
            ((Button) findViewById(R.id.button_delete_event)).setText(R.string.delete_event);
          /*  delete.setOnClickListener((View v1) -> {
                Event.removeEvent(CreateEvent.getEventEdited(), this);
                setResult(RESULT_OK);
                finish();
            });
            ((LinearLayout)findViewById(R.id.edit_contact_layout)).addView(delete);
        */}

    }
}
