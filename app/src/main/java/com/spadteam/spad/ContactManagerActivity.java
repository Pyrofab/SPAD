package com.spadteam.spad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactManagerActivity extends Activity {

    protected ContactArrayAdapter adapter;
    static final int EDIT_CONTACT_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_manager);

        final ListView listview = (ListView) findViewById(R.id.contact_list);

        adapter = new ContactArrayAdapter(this);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(ContactManagerActivity.this, ContactEditorActivity.class);

            ContactEditorActivity.contactEdited = Contact.getContact(position);

            startActivityForResult(i, EDIT_CONTACT_REQUEST);
        });
    }

    public void newContact(View v) {
        Intent i = new Intent(ContactManagerActivity.this, ContactEditorActivity.class);
        ContactEditorActivity.contactEdited = null;
        startActivityForResult(i, EDIT_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_CONTACT_REQUEST) {
            if(resultCode == RESULT_OK) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void clearContacts(View v) {
        Contact.clearContacts(this);
        Toast.makeText(this, "Contact list cleared !", Toast.LENGTH_SHORT).show();
    }

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
                v = vi.inflate(R.layout.contactlistrow, null);
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
