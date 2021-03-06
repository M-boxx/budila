package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityToSleep extends AppCompatActivity {
    private int counter;
    private boolean checker;
    private static com.example.smartalarm.ActivityToSleep inst;

    public static com.example.smartalarm.ActivityToSleep instance() {
        return inst;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Во время сна возврат невозможен", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        inst = this;
        counter = 11;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        TextView wtextView = (TextView) findViewById(R.id.timetext);
        int hours = MainActivity.instance().hours;
        int minute = MainActivity.instance().minutes;
        wtextView.append("подъем в \n");
        if (hours < 10) {
            wtextView.append("0");
        }
        wtextView.append(Integer.toString(hours));
        wtextView.append(":");
        if (minute < 10) {
            wtextView.append("0");
        }
        wtextView.append(Integer.toString(minute));

        startService(new Intent(this, ForegroundService.class));

        final Handler handler = new Handler();
        final Button button = findViewById(R.id.buttonback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance().alarmToggle.setChecked(false);
                stopService(new Intent(context, ForegroundService.class));
                MainActivity.instance().onToggleClicked(MainActivity.instance().alarmToggle);
                Intent questionIntent = new Intent(ActivityToSleep.this,
                        MainActivity.class);
                startActivityForResult(questionIntent, 0);
                overridePendingTransition(0, R.anim.beta);
            }
        });
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        counter--;
                        handler.postDelayed(this, 1_000L);
                        if (counter <= 0) {
                            button.setVisibility(View.INVISIBLE);
                        } else button.setText("Осталось\n" + counter + " секунд для выхода");
                    }
                });
    }

    void startAlarm() {
        Intent intent = new Intent(this, WalkActivity.class);
        startActivity(intent);
    }
}
