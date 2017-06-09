package com.spadteam.spad

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*
import java.util.regex.Pattern


@Suppress("UNUSED_PARAMETER")
class CurrentEventsActivity : AppCompatActivity() {

    data class SpadEvent(var place: String, var time: String, var description : String)

    private val MY_PERMISSIONS_REQUEST_READ_MESSAGE = 0
//    private var index = 0
    internal val EDIT_EVENT_REQUEST = 0
//    lateinit var progressBar : ContentLoadingProgressBar

    companion object {
        lateinit private var adapter: EventArrayAdapter
        @JvmField
        val listEvents = SparseArray<Triple<SpadEvent, MutableList<String>, String>>()
        @JvmField
        var selected = 0
        @JvmField
        var instance : CurrentEventsActivity? = null

        val patternNewEvent : Pattern = Pattern.compile("^\\[Invitation](.*?)(\\sau\\s(.*?))?(\\s[aà]\\s(.*?))?(\\spour\\s(.*?))?$")
        val patternFollowUp : Pattern = Pattern.compile("^\\[FollowUp](.*)")
        val patternUpdate : Pattern = Pattern.compile("^\\[Update].*?(\\sau\\s(.*?))?(\\s[aà]\\s(.*?))?(\\spour\\s(.*?))?$")
        val patternCancel : Pattern = Pattern.compile("^\\[Cancellation].*")

        fun removeSelected() {
            listEvents.removeAt(selected)
            adapter.notifyDataSetChanged()
        }

        fun listEdited() {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_events)

        instance = this

        val listView : ListView = findViewById(R.id.event_list) as ListView? ?: return

        adapter = EventArrayAdapter(this, listEvents)
        listView.adapter = adapter

//        val layout = findViewById(R.id.linear_layout_current_events) as LinearLayout
//        progressBar = ContentLoadingProgressBar(this)
//        layout.addView(progressBar, 1)

        listView.setOnItemClickListener { _, _, position, _ ->
            val i = Intent(this, EventChosen::class.java)

            selected = position

            startActivityForResult(i, EDIT_EVENT_REQUEST)
        }
    }

    fun onMessageReadClick(v : View) {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS),
                    MY_PERMISSIONS_REQUEST_READ_MESSAGE)
            (findViewById(R.id.showSms) as TextView).text = getString(R.string.access_denied)
            /*Toast.makeText(applicationContext, "Needs permission : " + MY_PERMISSIONS_REQUEST_READ_MESSAGE,
                    Toast.LENGTH_LONG).show()*/

        } else {
            (findViewById(R.id.showSms) as TextView).text = getString(R.string.loading_data)
            Thread(updateEvents).start()
        }
    }

    val updateEvents = {
        val SORT_ORDER = "date ASC"
        val cursor = contentResolver.query(Uri.parse("content://sms"), arrayOf("_id", "thread_id", "address", "person", "date", "body"), null, null, SORT_ORDER)

        val liste = findViewById(R.id.event_list)

        if (cursor.moveToFirst()) { // must check the result to prevent exception

//            progressBar.max = cursor.count
//            progressBar.show()

            liste?.post({(findViewById(R.id.determinateBar) as ContentLoadingProgressBar).show()})
            liste?.post({(findViewById(R.id.determinateBar) as ProgressBar).progress = 0})
            liste?.post({(findViewById(R.id.determinateBar) as ProgressBar).max = cursor.count })

            do {
//                val messageId = cursor.getLong(0)
                val threadId = cursor.getLong(1)
                val address = cursor.getString(2)
//                val contactId = cursor.getLong(3)
//                val timeStamp = cursor.getLong(4)
                val body = cursor.getString(5)

                val m1 = patternNewEvent.matcher(body)
                if(m1.matches()) {
                    System.out.println("Invitation (thread=${threadId.toInt()}, address=$address)")
                    liste?.post({listEvents.put(threadId.toInt(), Triple(SpadEvent(m1.group(3) ?: "No place specified",
                            m1.group(5) ?: "No time specified", m1.group(7) ?: m1.group(1) ?: "No description provided"),
                            LinkedList<String>(), address))})        //Ici j'ajoute le corps du sms a la liste
                }

                val m2 = patternFollowUp.matcher(body)
                if(m2.matches()) {
                    liste?.post({listEvents.get(threadId.toInt())?.second?.add(m2.group(1))})
                    System.out.println("list: ${listEvents.get(threadId.toInt())?.second} group: ${m2.group(1)}")
                }

                val m3 = patternUpdate.matcher(body)
                if(m3.matches()) {
                    if (m3.group(2) != null)
                        liste?.post({listEvents.get(threadId.toInt())?.first?.place = m3.group(2)})
                    if(m3.group(4) != null)
                        liste?.post({listEvents.get(threadId.toInt())?.first?.time = m3.group(4)})
                    if(m3.group(6) != null)
                        liste?.post({listEvents.get(threadId.toInt())?.first?.description = m3.group(6)})
                }

                val m4 = patternCancel.matcher(body)
                if(m4.matches()) {
                    System.out.println("Cancellation (thread=${threadId.toInt()}, address=$address)")
                    liste?.post({listEvents.remove(threadId.toInt())})
                }

                liste?.post({adapter.notifyDataSetChanged()})

                liste?.post({(findViewById(R.id.determinateBar) as ProgressBar).incrementProgressBy(1)})
//                liste?.post({progressBar.incrementProgressBy(1)})
            } while (cursor.moveToNext())
//            liste?.post({progressBar.hide()})
            liste?.post({(findViewById(R.id.determinateBar) as ContentLoadingProgressBar).hide()})
        } else {
            liste.post({(findViewById(R.id.showSms) as TextView).text = getString(R.string.no_current_event)})
        }
        (findViewById(R.id.showSms) as TextView).post({(findViewById(R.id.showSms) as TextView).text =
                if(listEvents.size() > 0) getString(R.string.loading_finished)
                else getString(R.string.no_current_event)})
        cursor.close()
    }

    inner class SmsListener : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "android.provider.Telephony.SMS_RECEIVED")
                Thread(updateEvents).start()
        }
    }

    private inner class EventArrayAdapter internal constructor(internal var context: Context, internal var events : SparseArray<Triple<SpadEvent, MutableList<String>, String>>) : BaseAdapter() {

        override fun getItem(position: Int): SpadEvent? {
            //return com.spadteam.spad.Event.getEvents(context)[position]
            //return if(events.valueAt(position) == null) null else events.valueAt(position)[0]
            return if(events.valueAt(position) is Triple) events.valueAt(position)?.first else null
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
