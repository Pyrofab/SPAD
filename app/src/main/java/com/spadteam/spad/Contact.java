package com.spadteam.spad;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
                //recuperation d'un espace de stockage propre à l'application identifié par une clé
                SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE);
                int nbContacts = sharedPref.getInt("number", 0);
                String name;
                String phoneNumber;
                String mailAddress;
                for (int i = 1; i <= nbContacts; i++) {
                    //recuperation des infos stockées dans l'espace de stockage
                    name = sharedPref.getString(i + "#name", "");
                    phoneNumber = (sharedPref.getString(i + "#phone", ""));
                    mailAddress = (sharedPref.getString(i + "#mail", ""));
                    contacts.add(new Contact(name, phoneNumber, mailAddress)); //ajout des infos a la liste de contacts
                }
            } catch (IllegalArgumentException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                contacts = new ArrayList<>();
            }
        }
        return new ArrayList<>(contacts);
    }

    /**
     * add contact into the SharedPreferences and the List
     * @param context the activity that invokes this method
     * @param contact the contact that we want to add
     */
    static void addContact(Contact contact, Context context) {
        contacts.add(contact);
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        //ouverture d'un éditeur pour avoir accès aux méthodes d'édition
        SharedPreferences.Editor editor = sharedPref.edit();
        int nbContacts = contacts.size();
        editor.putInt("number", nbContacts);
        editor.putString(nbContacts + "#name", contact.getName());
        editor.putString(nbContacts + "#phone", contact.getPhoneNo());
        editor.putString(nbContacts + "#mail", contact.getMail());
        editor.apply();
    }

    /**
     * remove contact from the List
     * @param context the activity that invokes this method
     * @param contact the contact that we want to remove
     */
    static void removeContact(Contact contact, Context context) {
        removeContact(contacts.indexOf(contact), context);
    }

    private static void removeContact(int index, Context context) {
        contacts.remove(index);
        Toast.makeText(context, "contact deleted", Toast.LENGTH_SHORT).show();
    }

    /**
     * get a contact
     * @param id the position of the contact in the List
     * @return the contact
     */
    static Contact getContact(int id) {
        return contacts.get(id);
    }


    /**
     * erase the contents of List
     * @param context the activity that invokes this method
     *
     */
    static void clearContacts(Context context) {
        contacts.clear();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }


    /**
     *
     * @param context the activity that invokes this method
     *
     */
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

    Contact(String name, String phoneNo, String mail) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.mail = mail;
    }

    /**
     * get the name of the contact
     *
     * @return the name of the contact
     */
    public String getName() {
        return name;
    }

    /**
     * set the name of the contact
     * @param name the name of the contact
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get the phone number of the contact
     *
     * @return the phone number of the contact
     */
    String getPhoneNo() {
        return phoneNo;
    }

    /**
     * set the phone number of the contact
     * @param phoneNo the phone number of the contact
     *
     */
    void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * get the mail of the contact
     * @return the mail of the contact
     *
     */
    String getMail() {
        return mail;
    }

    /**
     * set the mail of the contact
     * @param mail the mail of the contact
     *
     */
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
    static class PhoneNumber {
        static final PhoneNumber EMPTY = new PhoneNumber("");
        private String phoneNo;
        static Pattern phonePattern;

        PhoneNumber(String phoneNo) {
            if(phonePattern == null)
                phonePattern = Pattern.compile("^(\\+|0)?(\\d){8,12}$");

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
        public static final MailAddress EMPTY = new MailAddress("");
        private String mail;
        private static Pattern mailPattern;

        MailAddress(String mail) {
            if(mailPattern == null)
                mailPattern = Pattern.compile("^(\\w)+@(\\w)+\\.(\\w)+$");

            if(mailPattern.matcher(mail).matches() || mail.equals(""))
                this.mail = mail;
            else
                throw new InvalidParameterException("not a valid mail address");
        }

        @Override
        public String toString() {
            return this.mail;
        }


    }
}
