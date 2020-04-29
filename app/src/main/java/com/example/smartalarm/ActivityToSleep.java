package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class ActivityToSleep extends AppCompatActivity {
    private int counter;
    private static com.example.smartalarm.ActivityToSleep inst;
    public static com.example.smartalarm.ActivityToSleep instance() {
        return inst;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Во время сна возврат невозможен", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        inst=this;
        counter=11;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        startService(new Intent(this, ForegroundService.class));
        final Handler handler = new Handler();
        final Button button = findViewById(R.id.buttonback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance().alarmToggle.setChecked(false);
                stopService(new Intent(context, ForegroundService.class));
                MainActivity.instance().onToggleClicked(MainActivity.instance().alarmToggle);
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        counter--;
                        handler.postDelayed(this, 1_000L);
                        if(counter<=0){
                            button.setVisibility(View.INVISIBLE);
                        } else button.setText("Осталось\n"+counter+" секунд для выхода");
                    }
                });
    }
}