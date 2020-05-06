package com.example.smartalarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PermissionActivity extends AppCompatActivity {
    Button button;
    Button button2;
    Button button3;
    PackageManager pm;
    private boolean first;
    private boolean second;
    private boolean third;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Сперва необходимо выдать разрешения!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isAccessGrantedu(android.Manifest.permission.PACKAGE_USAGE_STATS)) {
                button2.setVisibility(View.GONE);
                first=true;
            } else {
                button2.setVisibility(View.VISIBLE);
                first=false;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isAccessGrantedu(android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
                button.setVisibility(View.GONE);
                second=true;
            } else {
                button.setVisibility(View.VISIBLE);
                second=false;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isAccessGrantedu(android.Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                button3.setVisibility(View.GONE);
                third=true;
            } else {
                button3.setVisibility(View.VISIBLE);
                third=false;
            }
        }
        if(first && second && third){
            SharedPreferences sharedPref = MainActivity.instance().sharedPref;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("true", true);
            editor.commit();
            Intent mainIntent = new Intent(this, MainActivity.class);
            Toast.makeText(this, "Все разрешения выданы, спасибо!", Toast.LENGTH_SHORT).show();
            startActivity(mainIntent);
        }
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
        button2 = findViewById(R.id.usage);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });
        button3 = findViewById(R.id.vspliv);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isAccessGrantedu(String permission) {
        boolean granted = false;
        int mode=-1;
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        if(permission.equals(Manifest.permission.PACKAGE_USAGE_STATS)) {
            mode = appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    getPackageName()
            );
        }else if(permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)){
            mode = appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
                    Process.myUid(),
                    getPackageName()
            );
        }
        else{
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        return granted;
    }
}
