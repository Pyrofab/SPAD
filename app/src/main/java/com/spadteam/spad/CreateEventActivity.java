package com.spadteam.spad;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    EditText placeEvent;
    EditText timeEvent;
    EditText txtMessage;
    protected ContactArrayAdapter adapter;

    private static final int MY_PERMISSIONS_REQUEST_SEND_MESSAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        placeEvent = (EditText) findViewById(R.id.place_event);
        txtMessage = (EditText) findViewById(R.id.event_description);
        timeEvent = (EditText) findViewById(R.id.event_time);

        final ListView listview = (ListView) findViewById(R.id.contact_list2);

        adapter = new ContactArrayAdapter(this);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener((parent, view, position, id) -> {
            findViewById(R.id.checked_contact).performClick();
            Toast.makeText(this, "click !", Toast.LENGTH_SHORT).show();
        });

    }

    public void onMessageButtonClick(View v) {
        String phoneNo = placeEvent.getText().toString();
        String message = txtMessage.getText().toString();

        if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(CreateEventActivity.this,
                Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(CreateEventActivity.this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_MESSAGE);
            Toast.makeText(getApplicationContext(), "Needs permission : " + MY_PERMISSIONS_REQUEST_SEND_MESSAGE,
                    Toast.LENGTH_LONG).show();
        } else {
            sendSMS(phoneNo, "[invite]" + message);
        }
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

  /*  public void readContacts(View v) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);


        for(String s : sharedPref.getAll().keySet()) {
            Toast.makeText(this, s + ":" + sharedPref.getAll().get(s), Toast.LENGTH_SHORT).show();
        }
    }
*/

    private class ContactArrayAdapter extends BaseAdapter {

        Context context;

        ContactArrayAdapter(Context context) {
            super();
            this.context = context;
        }

        @Nullable
        @Override
        public com.spadteam.spad.Contact getItem(int position) {
            return com.spadteam.spad.Contact.getContacts(getContext()).get(position);
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
            return com.spadteam.spad.Contact.getContacts(context).size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if(v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.contactlistforevent, null);
            }
            com.spadteam.spad.Contact c = getItem(position);

            if(c != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.contact_row_id);  // TODO fix this
                TextView tt2 = (TextView) v.findViewById(R.id.contact_description);
                TextView tt3 = (TextView) v.findViewById(R.id.categoryId);

                if(tt1 != null) {
                    tt1.setText(c.getName());
                }
                if(tt2 != null) {
                    tt2.setText(c.getMail());
                }
                if(tt3 != null) {
                    tt3.setText(c.getPhoneNo());
                }
            }
            return v;
        }
    }
}
