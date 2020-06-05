package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class ActivityTwo extends AppCompatActivity {

    private static com.example.smartalarm.ActivityTwo inst;
    public static com.example.smartalarm.ActivityTwo instance() {
        return inst;
    }

    private static Random random = new Random();
    public boolean egor = false;
    int counter;
    Context context;
    Intent intent;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Во время наказания возврат невозможен", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart(){
        super.onStart();
        counter=random.nextInt(41)+10;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        Button button = findViewById(R.id.angry_button);
        context=this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter>0){
                    Toast.makeText(context, "Вам осталось " + counter + " нажатий", Toast.LENGTH_SHORT).show();
                    counter--;
                } else {
                    Toast.makeText(context, "Нажатий достаточно!", Toast.LENGTH_SHORT).show();
                    if(WalkActivity.instance()!=null){
                        WalkActivity.instance().stopLoop();
                        Intent intent = new Intent(context, WalkActivity.class);
                        counter=random.nextInt(41)+10;
                        startActivity(intent);
                    } else{
                        Intent intent = new Intent(context, ActivityToSleep.class);
                        startActivity(intent);
                    }
                }
                }
        });
    }
}