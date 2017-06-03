package com.spadteam.spad;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clément on 03/06/2017.
 */

public class Event {
    private String place;
    private String time;
    private String description;

    private List<Contact> contactsInvited; //TODO utiliser en fonction des contacts cochés

    private static List<Event> events;

    public Event(String place, String time, String description){
        this.place=place;
        this.time=time;
        this.description=description;
    }


    static List<Event> getEvents(Context context) {
        if(events == null) {
            try {
                events = new ArrayList<>();
                SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key_event),
                        Context.MODE_PRIVATE);
                int nbEvents = sharedPref.getInt("number", 0);
                String lieu;
                String heure;
                String description;
                for (int i = 1; i <= nbEvents; i++) {
                    lieu = sharedPref.getString(i + "#place", "");
                    heure = (sharedPref.getString(i + "#time", ""));
                    description = (sharedPref.getString(i + "#description", ""));
                    events.add(new Event(lieu, heure, description));
                }
            } catch (IllegalArgumentException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                events = new ArrayList<>();
            }
        }
        return new ArrayList<>(events);
    }


    static void addEvents(Event event, Context context) {
        events.add(event);
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key_event),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int nbEvents = events.size();
        editor.putInt("number", nbEvents);
        editor.putString(nbEvents + "#place", event.getPlace());
        editor.putString(nbEvents + "#time", event.getTime());
        editor.putString(nbEvents + "#description", event.getDescription());
        editor.apply();
    }

    static void removeEvent(Event event, Context context) {
        removeEvent(events.indexOf(event), context);
    }

    private static void removeEvent(int index, Context context) {
        events.remove(index);
        Toast.makeText(context, "event deleted", Toast.LENGTH_SHORT).show();
    }

    static void refreshData(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key_event),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putInt("number", events.size());
        for(int i = 1; i <= events.size(); i++) {
            editor.putString(i + "#place", events.get(i-1).getPlace());
            editor.putString(i + "#time", events.get(i-1).getTime());
            editor.putString(i + "#description", events.get(i-1).getDescription());
        }
        editor.apply();
    }
    static Event getEvent(int id) {
        return events.get(id);
    }

    public String getPlace(){return this.place;}

    public void setPlace(String place){this.place=place;}

    public String getTime(){return this.time;}

    public void setTime(String time){this.time=time;}

    public String getDescription(){return this.description;}

    public void setDescription(String description){this.description=description;}

    @Override
    public String toString() {
        return "Event{" +
                "place='" + this.place + '\'' +
                ", time='" + this.time + '\'' +
                ", description='" + this.description + '\'' +
                '}';
    }
}
