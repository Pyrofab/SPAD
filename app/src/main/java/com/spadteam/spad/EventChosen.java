package com.spadteam.spad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

        if(CreateEventActivity.getEventEdited() != null) {
            ((TextView) findViewById(R.id.place_event)).setText(CreateEventActivity.getEventEdited().getPlace());
            ((TextView) findViewById(R.id.event_time)).setText(CreateEventActivity.getEventEdited().getTime());
            ((TextView) findViewById(R.id.event_description)).setText(CreateEventActivity.getEventEdited().getDescription());
            Button delete = (Button) findViewById(R.id.button_delete_event);

            //Button delete = new Button(this);
            delete.setText(R.string.delete_event);
            delete.setOnClickListener((View v) -> {
                Event.removeEvent(CreateEventActivity.getEventEdited(), this);
                CreateEventActivity.getEventEdited().refreshData(this); //gere la MAJ des evenements apres la suppression
                setResult(RESULT_OK);
                finish();
            });




        }

    }
}
