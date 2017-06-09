package com.spadteam.spad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fabien on 09/06/2017.
 *
 */

public class SmsListener extends BroadcastReceiver {
    /**
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            System.out.println("received message");
            if (CurrentEventsActivity.instance != null) {
                final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
                executor.schedule(() -> CurrentEventsActivity.instance.getUpdateEvents().invoke(), 1, TimeUnit.SECONDS);
            }
        }
    }
}
