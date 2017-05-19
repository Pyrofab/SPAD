package com.spadteam.spad;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateEvent extends AppCompatActivity {

    EditText txtphoneNo;
    EditText txtMessage;
    private String phoneNo;
    private String message;

    private static final int MY_PERMISSIONS_REQUEST_SEND_MESSAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        txtphoneNo = (EditText) findViewById(R.id.phone_number_input);
        txtMessage = (EditText) findViewById(R.id.event_content_input);
    }

    public void onMessageButtonClick(View v) {
        System.out.println("slt");
        phoneNo = txtphoneNo.getText().toString();
        message = txtMessage.getText().toString();

        if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(CreateEvent.this,
                Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(CreateEvent.this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_MESSAGE);
            Toast.makeText(getApplicationContext(), "Needs permission : " + MY_PERMISSIONS_REQUEST_SEND_MESSAGE,
                    Toast.LENGTH_LONG).show();
        } else {
            sendSMS(phoneNo, message);
        }
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void readContacts(View v) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        for(String s : sharedPref.getAll().keySet()) {
            Toast.makeText(this, s + ":" + sharedPref.getAll().get(s), Toast.LENGTH_SHORT).show();
        }
    }
}
