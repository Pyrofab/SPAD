package com.spadteam.spad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.spadteam.spad.firebase.FirebaseInit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        FloatingActionButton subscribeButton = (FloatingActionButton) findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(v -> {
            FirebaseMessaging.getInstance().subscribeToTopic("news");
            String msg  = getString(R.string.msg__subscribed);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        FirebaseInit.init();
    }

    public void onClick(View v) {
        Class targetActivity;
        switch (v.getId()) {
            case R.id.create_event_button:
                targetActivity = CreateEventActivity.class; break;
            case R.id.review_event_button:
                targetActivity = CurrentEventsActivity.class; break;  //TODO change this to the right activity
            case R.id.manage_contacts_button:
                targetActivity = ContactManagerActivity.class; break;
            default: return;
        }
        Intent i = new Intent(MainActivity.this, targetActivity);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
