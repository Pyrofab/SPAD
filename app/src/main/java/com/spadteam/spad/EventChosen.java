package com.spadteam.spad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Clément on 03/06/2017.
 *
 */

public class EventChosen extends Activity{

    private String phoneNo;
    private ArrayAdapter<String> adapter;

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

         System.out.println(CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getSecond());

         adapter = new ArrayAdapter<>(this, R.layout.text_view_event_chosen,
                 CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getSecond());

        ((ListView)findViewById(R.id.event_details_message_list)).setAdapter(
                adapter);

        phoneNo = CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getThird();
            //Button delete = new Button(this);
            delete.setText(R.string.delete_event);
            delete.setOnClickListener((View v) -> {
                //Event.removeEvent(CreateEventActivity.getEventEdited(), this);
                //Event.refreshData(this); //gere la MAJ des evenements apres la suppression
                Toast.makeText(this, CreateEventActivity.sendSMS(phoneNo,
                        "[Cancellation]") ? "Sent cancellation" : "Something failed",
                        Toast.LENGTH_SHORT).show();
                CurrentEventsActivity.Companion.removeSelected();
                setResult(RESULT_OK);
                finish();
            });
//        }

    }

    public void onSendUpdate (View v) {
        String place = ((EditText) findViewById(R.id.place_event)).getText().toString();
        String time = ((EditText) findViewById(R.id.event_time)).getText().toString();
        String description = ((EditText) findViewById(R.id.event_description)).getText().toString();

        String msg = "[Update]";

        if(!place.equals(CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getFirst().getPlace())) {
            msg += " au " + place;
            CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getFirst().setPlace(place);
        }

        if(!time.equals(CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getFirst().getTime())) {
            msg += " a " + time;
            CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getFirst().setTime(time);
        }

        if(!description.equals(CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getFirst().getDescription())) {
            msg += " pour " + description;
            CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getFirst().setDescription(description);
        }

        Toast.makeText(this, CreateEventActivity.sendSMS(phoneNo,
                msg) ? "Sent message" : "Something failed",
                Toast.LENGTH_SHORT).show();
        CurrentEventsActivity.Companion.listEdited();
    }

    public void onSendFollowUp (View v) {
        EditText edit = (EditText) findViewById(R.id.edit_text_follow_up);
        Toast.makeText(this, CreateEventActivity.sendSMS(phoneNo,
                "[FollowUp] " + edit.getText()) ? "Sent message" : "Something failed",
                Toast.LENGTH_SHORT).show();
        CurrentEventsActivity.listEvents.valueAt(CurrentEventsActivity.selected).getSecond().add(edit.getText().toString());
        adapter.notifyDataSetChanged();
        edit.setText("");
    }
}
