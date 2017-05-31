package com.spadteam.spad;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabien on 18/05/2017.
 * Class describing a contact for the application
 */

public class Contact {
    private String name;
    private String phoneNo;
    private String mail;
    private static List<Contact> contacts;

    /**
     *
     * @param context the activity that invokes this method
     * @return a read-only version of the contact list
     */
    static List<Contact> getContacts(Context context) {
        if(contacts == null) {
            try {
                contacts = new ArrayList<>();
                SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE);
                int nbContacts = sharedPref.getInt("number", 0);
                String name;
                String phoneNumber;
                String mailAddress;
                for (int i = 1; i <= nbContacts; i++) {
                    name = sharedPref.getString(i + "#name", "");
                    phoneNumber = (sharedPref.getString(i + "#phone", ""));
                    mailAddress = (sharedPref.getString(i + "#mail", ""));
                    contacts.add(new Contact(name, phoneNumber, mailAddress));
                }
            } catch (IllegalArgumentException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                contacts = new ArrayList<>();
            }
        }
        return new ArrayList<>(contacts);
    }

    static void addContact(Contact contact, Context context) {
        contacts.add(contact);
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int nbContacts = contacts.size();
        editor.putInt("number", nbContacts);
        editor.putString(nbContacts + "#name", contact.getName());
        editor.putString(nbContacts + "#phone", contact.getPhoneNo());
        editor.putString(nbContacts + "#mail", contact.getMail());
        editor.apply();
    }

    static void removeContact(Contact contact, Context context) {
        removeContact(contacts.indexOf(contact), context);
    }

    static void removeContact(int index, Context context) {
        contacts.remove(index);
        Toast.makeText(context, "contact deleted", Toast.LENGTH_SHORT).show();
    }

    static Contact getContact(int id) {
        return contacts.get(id);
    }

    static void clearContacts(Context context) {
        contacts.clear();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    static void refreshData(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putInt("number", contacts.size());
        for(int i = 1; i <= contacts.size(); i++) {
            editor.putString(i + "#name", contacts.get(i-1).getName());
            editor.putString(i + "#phone", contacts.get(i-1).getPhoneNo());
            editor.putString(i + "#mail", contacts.get(i-1).getMail());
        }
        editor.apply();
    }
/*
    public static void forEach(Consumer<Contact> fun) {
        for(Contact c : contacts)
            fun.accept(c);
    }
*/
/*
    public Contact(String name) {
        this(name, PhoneNumber.EMPTY.toString(), MailAddress.EMPTY.toString());
    }

    public Contact(String name, PhoneNumber phoneNo) {
        this(name, phoneNo.toString(), MailAddress.EMPTY.toString());
    }

    public Contact(String name, MailAddress mail) {
        this(name, PhoneNumber.EMPTY.toString(), mail.toString());
    }
*/
    Contact(String name, String phoneNo, String mail) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getPhoneNo() {
        return phoneNo;
    }

    void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    String getMail() {
        return mail;
    }

    void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
/*
    static class PhoneNumber {
        static final PhoneNumber EMPTY = new PhoneNumber("");
        private String phoneNo;
        static final Pattern phonePattern = Pattern.compile("^(\\+)?(\\d){9,11}$");

        PhoneNumber(String phoneNo) {
            if(phonePattern.matcher(phoneNo).matches() || phoneNo.equals(""))
                this.phoneNo = phoneNo;
            else
                throw new InvalidParameterException("not a valid phone number");
        }

        @Override
        public String toString() {
            return this.phoneNo;
        }
    }

    static class MailAddress {
        static final MailAddress EMPTY = new MailAddress("");
        private String mail;
        static final Pattern mailPattern = Pattern.compile("^(\\w)+@(\\w)+\\.(\\w)+$");

        MailAddress(String mail) {
            if(mailPattern.matcher(mail).matches() || mail.equals(""))
                this.mail = mail;
            else
                throw new InvalidParameterException("not a valid mail address");
        }

        @Override
        public String toString() {
            return this.mail;
        }


    }*/
}
