package com.spadteam.spad

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.util.*

@Suppress("UNUSED_PARAMETER")
class CurrentEventsActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_READ_MESSAGE = 0
    private val listEvents = LinkedList<String>()
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_events)
    }

    fun onMessageReadClick(v : View) {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS),
                    MY_PERMISSIONS_REQUEST_READ_MESSAGE)
            Toast.makeText(applicationContext, "Needs permission : " + MY_PERMISSIONS_REQUEST_READ_MESSAGE,
                    Toast.LENGTH_LONG).show()
        } else {
            readMessages()
        }
    }

    fun readMessages() {
        val SORT_ORDER = "date DESC"
        val cursor = contentResolver.query(Uri.parse("content://sms/inbox"), arrayOf("_id", "thread_id", "address", "person", "date", "body"), null, null, SORT_ORDER)

        if (cursor!!.moveToFirst()) { // must check the result to prevent exception
            do {
                val messageId = cursor.getLong(0)
                val threadId = cursor.getLong(1)
                val address = cursor.getString(2)
                val contactId = cursor.getLong(3)
                val timeStamp = cursor.getLong(4)
                val body = cursor.getString(5)
                (findViewById(R.id.showSms) as TextView).text = getString(R.string.message_template).format(messageId, threadId, address, contactId, timeStamp, body)
                if(body.contains("[invite]"))
                    listEvents.add(body)
                refresh()
            } while (cursor.moveToNext())
        } else {
            Toast.makeText(this, "no message to display", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }

    fun arrowLeft (v: View) {
        index++
        refresh()
    }

    fun arrowRight (v: View) {
        index++
        refresh()
    }

    fun refresh () {
        try {
            (findViewById(R.id.show_msg_body) as TextView).text = listEvents[index]
        } catch(e: ArrayIndexOutOfBoundsException){}
    }
}
