package com.spadteam.spad;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class ContactManagerActivity extends Activity {

    private ContactArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_manager);

        final ListView listview = (ListView) findViewById(R.id.contact_list);

        adapter = new ContactArrayAdapter(this,
                android.R.layout.simple_list_item_1, Contact.getContacts(this));
        listview.setAdapter(adapter);

        listview.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            setContentView(R.layout.contact_editor);
            ((TextView)findViewById(R.id.name_input_field)).setText(
                    Contact.getContacts(this).get(position).getName());
            ((TextView)findViewById(R.id.phone_number_input_field)).setText(
                    Contact.getContacts(this).get(position).getPhoneNo());
            ((TextView)findViewById(R.id.mail_input_field)).setText(
                    Contact.getContacts(this).get(position).getMail());
        });
    }

    public void newContact(View v) {
        setContentView(R.layout.contact_editor);
    }

    public void confirmContactChanges(View v) {
        try {
            TextView nameField = (TextView) findViewById(R.id.name_input_field);
            TextView phoneField = (TextView) findViewById(R.id.phone_number_input_field);
            TextView mailField = (TextView) findViewById(R.id.mail_input_field);
            String name = nameField.getText().toString();
            String phoneNo = phoneField.getText().toString();
            String mail = mailField.getText().toString();

            if (name.equals("")) {
                Toast.makeText(this, "please input a valid name", Toast.LENGTH_SHORT).show();
                return;
            }

            Contact contact = new Contact(name, phoneNo, mail);

            Contact.addContact(contact, this);

            Toast.makeText(this, "list filled [" + name + "," + phoneNo + "," + mail + "]",
                    Toast.LENGTH_SHORT).show();

            setContentView(R.layout.activity_contact_manager);
        } catch (RuntimeException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelContactChanges(View v) {
        setContentView(R.layout.activity_contact_manager);
        Toast.makeText(this, "operation cancelled", Toast.LENGTH_SHORT).show();
    }

    public void clearContacts(View v) {
        Contact.clearContacts(this);/*
        adapter = new ContactArrayAdapter(this,
                android.R.layout.simple_list_item_1, Contact.getContacts(this));*/
        //adapter.setList(Contact.getContacts(this));
        Toast.makeText(this, "Contact list cleared !", Toast.LENGTH_SHORT).show();
    }

    private class ContactArrayAdapter<Contact> extends BaseAdapter {

        Context context;

        ContactArrayAdapter(Context context, int textViewResourceId,
                                  List<com.spadteam.spad.Contact> objects) {
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

        protected Context getContext() {
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
//                TextView tt2 = (TextView) v.findViewById(R.id.contact_description);
//                TextView tt3 = (TextView) v.findViewById(R.id.contact_row_id);

                if(tt1 != null) {
                    tt1.setText(c.getName());
                    Toast.makeText(getContext(), c.getName(), Toast.LENGTH_SHORT).show();
                }
//                if(tt2 != null) {
//                    tt2.setText(c.getMail());
//                }
//                if(tt3 != null) {
//                    tt3.setText(c.getPhoneNo());
//                }
            }
            return v;
        }
    }
}
