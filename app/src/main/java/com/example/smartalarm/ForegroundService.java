package com.example.smartalarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ForegroundService extends Service {
    final String LOG_TAG = "Service";
    List<UsageStats> firstList;
    private boolean whiler = true;
    private int counter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
        firstList = UStats.getUsageStatsList(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1001, createNotification());
        Log.d(LOG_TAG, "onStartCommand");
        firstList = UStats.getUsageStatsList(this);
        someTask(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        whiler = false;
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    void someTask(final Context context) {
        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void run() {
                while (whiler) {
                    try {
                        List<UsageStats> lastList = UStats.getUsageStatsList(context);
                        if (counter == 1) {
                            firstList = lastList;
                            counter--;
                        }
                        for (int i = 0; i < lastList.size(); i++) {
                            if (!whiler) {
                                break;
                            }
                            if (firstList.get(i).getTotalTimeInForeground() != lastList.get(i).getTotalTimeInForeground() && !firstList.get(i).getPackageName().equals("com.example.smartalarm")) {
                                Log.d("ALAAAARM", "ALARM!");
                                vibrator.vibrate(3000);
                                counter++;
                                Intent intent = new Intent(context, ActivityTwo.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                break;
                            }
                        }
                        TimeUnit.SECONDS.sleep(8);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Notification createNotification() {
        final int notificationIconResource = R.drawable.ic_1;
        Bitmap icon = BitmapFactory.decodeResource(getResources(), notificationIconResource);

        String channelId = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel("my_service", "My Background Service");
        }

        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Слежу за твоим сном")
                .setTicker("Не заходи в другие приложения")
                .setContentText("Спи!")
                .setSmallIcon(notificationIconResource)
                .setOngoing(true).build();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

}