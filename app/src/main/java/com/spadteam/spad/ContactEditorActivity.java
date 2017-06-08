package com.spadteam.spad;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Fabien on 20/05/2017.
 * An activity to edit a contact
 */

public class ContactEditorActivity extends Activity {

    static Contact contactEdited;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_editor);

        if(contactEdited != null) { //quand on veut modifier le contact
            ((TextView) findViewById(R.id.name_input_field)).setText(
                    contactEdited.getName());
            ((TextView) findViewById(R.id.phone_number_input_field)).setText(
                    contactEdited.getPhoneNo());
            ((TextView) findViewById(R.id.mail_input_field)).setText(
                    contactEdited.getMail());
            Button delete = new Button(this);
            delete.setText(R.string.delete);
            delete.setOnClickListener((View v) -> {
                Contact.removeContact(contactEdited, this);
                setResult(RESULT_OK);
                finish();
            });
            ((LinearLayout)findViewById(R.id.edit_contact_layout)).addView(delete);
        }
    }

    /**
     * action of the button : to confirm the changes of the contact
     * @param v the view where the button is
     *
     */
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

            if(contactEdited == null) {
                Contact contact = new Contact(name, phoneNo, mail);
                Contact.addContact(contact, this);

            } else {
                contactEdited.setName(name);
                contactEdited.setMail(mail);
                contactEdited.setPhoneNo(phoneNo);
                contactEdited = null;
                Contact.refreshData(this);
            }
        } catch (RuntimeException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     *  action of the button : to cancel the changes of a contact
     *
     * @param v the view where the button is
     */
    public void cancelContactChanges(View v) {
        Toast.makeText(this, "operation cancelled", Toast.LENGTH_SHORT).show();
        contactEdited = null;
        setResult(RESULT_CANCELED);
        finish();
    }
}
