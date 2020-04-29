package com.example.smartalarm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class AlarmService extends IntentService {
    private static final String CHANNEL1ID ="channel1";

    private NotificationManagerCompat notificationManagerCompat;
    final Context context = this;




    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Wake Up! Wake Up!");
    }

    private void sendNotification(String msg) {

        Log.d("AlarmService", "Preparing to send notification...: " + msg);

        final String CHANNEL1ID = "channel1";
        final Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        createNotificationChannels();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL1ID)
                .setSmallIcon(R.drawable.ic_1)
                .setContentTitle("Ego")
                .setContentText("Ega")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setSound(alarmUri)
                .build();
        notificationManagerCompat.notify(1, notification);
        startForeground(1001,notification);

        Log.d("AlarmService", "Notification sent.");
    }
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL1ID,
                    "Channel1",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is example");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}