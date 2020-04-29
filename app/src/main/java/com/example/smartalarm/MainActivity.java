package com.example.smartalarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL1ID = "channel1";
    private final Context context = this;

    private NotificationManagerCompat notificationManagerCompat;
    private AlarmManager alarmManager;
    public ToggleButton alarmToggle;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private Intent intent2;
    private static com.example.smartalarm.MainActivity inst;

    public static com.example.smartalarm.MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();

        inst = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //дефолт
        getSupportActionBar().hide();
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //рингтон
        final Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        createNotificationChannels();

        //gif
        final ImageView view = findViewById(R.id.animView);
        view.callOnClick();
        AnimationDrawable animation = (AnimationDrawable) view.getBackground();
        animation.setOneShot(false);
        animation.start();

        notificationManagerCompat = NotificationManagerCompat.from(this);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmToggle = findViewById(R.id.alarmToggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        FloatingActionButton floatingActionButton = findViewById(R.id.fly);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2 = new Intent(context, SettingActivity.class);
                startActivity(intent2);
            }
        });
        //кнопка Ega
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification = new NotificationCompat.Builder(context, CHANNEL1ID)
                        .setSmallIcon(R.drawable.ic_1)
                        .setContentTitle("Ego")
                        .setContentText("Ega")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setSound(alarmUri)
                        .build();
                notificationManagerCompat.notify(1, notification);
            }
        });

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

    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            Log.d("MyActivity", "Alarm On");
            Calendar calendar = Calendar.getInstance();
            long now = Calendar.getInstance().getTimeInMillis();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

            Intent myIntent = new Intent(com.example.smartalarm.MainActivity.this, com.example.smartalarm.AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(com.example.smartalarm.MainActivity.this, 0, myIntent, 0);
            if (now > calendar.getTimeInMillis()) {
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis() + 86400000, pendingIntent);
                Log.d("Завтра", Long.toString(calendar.getTimeInMillis() + 86400000));
                Intent questionIntent = new Intent(MainActivity.this,
                        ActivityToSleep.class);
                startActivityForResult(questionIntent, 0);
                overridePendingTransition(R.anim.diagonal, R.anim.alpha);
            } else {
                Log.d("Сегодня", Long.toString(calendar.getTimeInMillis()));
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                Intent questionIntent = new Intent(MainActivity.this,
                        ActivityToSleep.class);
                startActivityForResult(questionIntent, 0);
                overridePendingTransition(0, R.anim.alpha);
            }
        } else {
            alarmManager.cancel(pendingIntent);
            Log.d("MyActivity", "Alarm Off");
        }
    }

    public void startingRoblox() {
        alarmToggle.setChecked(false);
        Intent intent = new Intent(this, WalkActivity.class);
        startActivity(intent);
    }
}