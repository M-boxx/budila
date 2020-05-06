package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class PermissionActivity extends AppCompatActivity {
    Button button;
    PackageManager pm;

    @Override
    protected void onResume() {
        super.onResume();
        if (pm.checkPermission(Manifest.permission.PACKAGE_USAGE_STATS, this.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            button.setVisibility(View.GONE);
        } else button.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        pm = getPackageManager();
        button = findViewById(R.id.dont);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });
        Button button2 = findViewById(R.id.usage);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });
        Button button3 = findViewById(R.id.vspliv);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivity(intent);
                }
            }
        });
    }
}
