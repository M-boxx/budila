package com.example.smartalarm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        com.example.smartalarm.MainActivity inst = com.example.smartalarm.MainActivity.instance();
        inst.startingRoblox();
        com.example.smartalarm.ActivityToSleep inst2 = com.example.smartalarm.ActivityToSleep.instance();
        inst2.startAlarm();

        ComponentName comp = new ComponentName(context.getPackageName(),
                com.example.smartalarm.AlarmService.class.getName());
        setResultCode(Activity.RESULT_OK);
    }
}