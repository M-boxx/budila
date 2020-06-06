package com.example.smartalarm;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WalkActivity extends AppCompatActivity implements SensorEventListener {
    private ProgressBar progressBar;
    private SensorManager sensorManager;
    private Sensor gps;
    private Ringtone ringtone;
    private int counter;
    private NotificationManager mNotificationManager;
    private AudioManager audioManager;
    private int volume;
    private int maxVolume;
    private final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

    private static com.example.smartalarm.WalkActivity inst;
    public static com.example.smartalarm.WalkActivity instance() {
        return inst;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Во время пробуждения возврат невозможен", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart() {
        super.onStart();
        final Context context = this;
        inst=this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        volume=audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, maxVolume-2, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.setLooping(true);
        }

        if(ActivityToSleep.instance()!=null){
            ActivityToSleep.instance().finish();
        }
        //создание сенсора
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gps = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (gps != null) {
            sensorManager.registerListener(this, gps, sensorManager.SENSOR_DELAY_FASTEST);
            Toast.makeText(this, "У вас есть счётчик шагов", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "У вас нет счётчика шагов :c", Toast.LENGTH_SHORT).show();
        }
        //прогрессбар
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.vertical));
        if(prefs.getBoolean("сложность", false))
        progressBar.setMax(5000);
        else progressBar.setMax(2500);
    }
    @Override
    public void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
        progressBar.setProgress(0);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        sensorManager.unregisterListener(this);
        progressBar.setProgress(0);
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        //дефолт
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.fly);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WalkActivity.this, "Ты молодец! Хорошего дня!", Toast.LENGTH_SHORT).show();
                stopService(new Intent(WalkActivity.this, ForegroundService.class));
                Intent intent = new Intent(WalkActivity.this, MainActivity.class);
                inst=null;
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, 0);
                progressBar.setProgress(0);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ringtone.setLooping(false);
                }
                finish();
            }
        });

        ImageView view2 = findViewById(R.id.animView2);
        AnimationDrawable animation2 = (AnimationDrawable) view2.getBackground();
        animation2.setOneShot(false);
        animation2.start();

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
        //создание сенсора
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gps = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (gps != null) {
            sensorManager.registerListener(this, gps, sensorManager.SENSOR_DELAY_FASTEST);
            Toast.makeText(this, "У вас есть счётчик шагов", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "У вас нет счётчика шагов :c", Toast.LENGTH_SHORT).show();
            floatingActionButton.setVisibility(View.VISIBLE);
        }
    }


    //имплементация методов
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(prefs.getBoolean("вибрация", true)) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
        }
        Log.d("MainActivity", "Сенсор изменился");
        float[] values = event.values;
        if (counter >= 3) {
            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, maxVolume/3*2, 0);
            int valuesone = (int) (values[0]);
            int nowprogress = progressBar.getProgress() + valuesone;
            progressBar.setProgress(nowprogress);
            if (progressBar.getProgress() >= progressBar.getMax()/3)
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.vertical3));
            if (progressBar.getProgress() >= (progressBar.getMax()/3)*2)
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.vertical2));
            if (progressBar.getProgress() >= progressBar.getMax()) {
                Toast.makeText(this, "Ты молодец! Хорошего дня!", Toast.LENGTH_SHORT).show();
                stopService(new Intent(this, ForegroundService.class));
                Intent intent = new Intent(this, MainActivity.class);
                inst=null;
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, 0);
                progressBar.setProgress(0);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ringtone.setLooping(false);
                }
                finish();
            }
        } else event.values[0] = 0;
        counter++;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void stopLoop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.setLooping(false);
            ringtone.stop();
            finish();
        }
    }

    //функции для UStats
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    protected void changeInterruptionFiler(int interruptionFilter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                mNotificationManager.setInterruptionFilter(interruptionFilter);
            } else {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }
}