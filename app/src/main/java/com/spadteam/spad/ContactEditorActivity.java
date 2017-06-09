package com.spadteam.spad;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Fabien on 20/05/2017.
 * An activity to edit a contact
 */

public class ContactEditorActivity extends Activity {

    static Contact contactEdited;
    private Button confirm;
    private EditText nameField, phoneField, mailField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_editor);

        confirm = (Button) findViewById(R.id.confirm_contact_changes);

        nameField = (EditText) findViewById(R.id.name_input_field);
        phoneField = (EditText) findViewById(R.id.phone_number_input_field);
        mailField = (EditText) findViewById(R.id.mail_input_field);

        phoneField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirm.setEnabled(Contact.isPhoneValid(phoneField.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirm.setEnabled(Contact.isMailValid(mailField.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        if(contactEdited != null) { //quand on veut modifier le contact
            nameField.setText(contactEdited.getName());
            phoneField.setText(contactEdited.getPhoneNo());
            mailField.setText(contactEdited.getMail());

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
            String name = nameField.getText().toString();
            String phoneNo = phoneField.getText().toString();
            String mail = mailField.getText().toString();

            if(!(Contact.isPhoneValid(phoneNo))) {
                Toast.makeText(this, "please input a valid phone number", Toast.LENGTH_SHORT).show();
                confirm.setEnabled(false);
                return;
            }

            if(!(Contact.isMailValid(mail))) {
                Toast.makeText(this, "please input a valid mail address", Toast.LENGTH_SHORT).show();
                confirm.setEnabled(false);
                return;
            }

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
