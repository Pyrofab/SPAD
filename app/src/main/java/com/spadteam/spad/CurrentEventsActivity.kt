package com.spadteam.spad

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import kotlin.concurrent.thread

@Suppress("UNUSED_PARAMETER")
class CurrentEventsActivity : AppCompatActivity() {

    data class SpadEvent(val place: String, val time: String, val description : String)

    private val MY_PERMISSIONS_REQUEST_READ_MESSAGE = 0
    val listEvents = SparseArray<List<SpadEvent>>()
//    private var index = 0
    lateinit private var adapter: EventArrayAdapter
    internal val EDIT_EVENT_REQUEST = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_events)

        val listView : ListView = findViewById(R.id.event_list) as ListView? ?: return

        adapter = EventArrayAdapter(this, listEvents)
        listView.adapter = adapter

        /*
        listView.setOnItemClickListener { _, _, position, _ ->
            val i = Intent(this, EventChosen::class.java)

            CreateEventActivity.eventEdited = Event.getEvent(position)

            startActivityForResult(i, EDIT_EVENT_REQUEST)
        }*/
    }

    fun onMessageReadClick(v : View) {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS),
                    MY_PERMISSIONS_REQUEST_READ_MESSAGE)
            Toast.makeText(applicationContext, "Needs permission : " + MY_PERMISSIONS_REQUEST_READ_MESSAGE,
                    Toast.LENGTH_LONG).show()
        } else {
            (findViewById(R.id.showSms) as TextView).text = getString(R.string.loading_data)
            Thread({
                val SORT_ORDER = "date DESC"
                val cursor = contentResolver.query(Uri.parse("content://sms/inbox"), arrayOf("_id", "thread_id", "address", "person", "date", "body"), null, null, SORT_ORDER)

//                Looper.prepare()

                if (cursor.moveToFirst()) { // must check the result to prevent exception
                    do {
//                val messageId = cursor.getLong(0)
                        val threadId = cursor.getLong(1)
//                val address = cursor.getString(2)
//                val contactId = cursor.getLong(3)
//                val timeStamp = cursor.getLong(4)
                        val body = cursor.getString(5)
                        //(findViewById(R.id.showSms) as TextView).text = getString(R.string.message_template).format(messageId, threadId, address, contactId, timeStamp, body)
                        if(body.contains("[Invitation]")) {
                            listEvents.put(threadId.toInt(), listOf(SpadEvent("", "", body)))        //Ici j'ajoute le corps du sms a la liste
                        }
                        if(body.contains("[Follow-up]"))
                            listEvents.get(threadId.toInt())?.plus(Event("", "", body))
                        if(body.contains("[Cancellation]"))
                            listEvents.remove(threadId.toInt())

                        (findViewById(R.id.event_list) as ListView?)?.post({adapter.notifyDataSetChanged()})
//                        Toast.makeText(this, listEvents.toString(), Toast.LENGTH_SHORT).show()
                    } while (cursor.moveToNext())
                } else {
//                    Toast.makeText(this, "no event to display", Toast.LENGTH_SHORT).show()
                }
//                Toast.makeText(this, "finished !", Toast.LENGTH_SHORT).show()
                cursor.close()
//                Looper.loop()
//                Looper.myLooper().quit()
            }).start()
        }
    }

    private inner class EventArrayAdapter internal constructor(internal var context: Context, internal var events : SparseArray<List<SpadEvent>>) : BaseAdapter() {

        override fun getItem(position: Int): SpadEvent? {
            //return com.spadteam.spad.Event.getEvents(context)[position]
            //return if(events.valueAt(position) == null) null else events.valueAt(position)[0]
            return events.valueAt(position)?.get(0)
        }

        /**
         * Get the row id associated with the specified position in the list.

         * @param position The position of the item within the adapter's data set whose row id we want.
         * *
         * @return The id of the item at the specified position.
         */
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            //return com.spadteam.spad.Event.getEvents(context).size
            return events.size()
        }

        @SuppressLint("InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var v: View? = convertView

            if (v == null) {
                val vi = LayoutInflater.from(context)
                v = vi.inflate(R.layout.eventlistrow, null)
            }
            val e = getItem(position)

            if (e != null && v != null) {
                val tt1 = v.findViewById(R.id.event_row_id) as TextView
                val tt2 = v.findViewById(R.id.event_time) as TextView //fait reference a la row d event
                val tt3 = v.findViewById(R.id.event_description) as TextView

                tt1.text = e.place
                tt2.text = e.time
                tt3.text = e.description
            }
            return v!!
        }
    }
}
