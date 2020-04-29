package com.example.smartalarm;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by User on 3/2/15.
 */
public class UStats {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    public static final String TAG = UStats.class.getSimpleName();
    @SuppressWarnings("ResourceType")
    public static void getStats(Context context){
        UsageStatsManager usm = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            usm = (UsageStatsManager) context.getSystemService("usagestats");

            int interval = UsageStatsManager.INTERVAL_YEARLY;
            Calendar calendar = Calendar.getInstance();
            long endTime = calendar.getTimeInMillis();
            calendar.add(Calendar.YEAR, -1);
            long startTime = calendar.getTimeInMillis();

            UsageEvents uEvents = usm.queryEvents(startTime, endTime);
            while (uEvents.hasNextEvent()) {
                UsageEvents.Event e = new UsageEvents.Event();
                uEvents.getNextEvent(e);

                if (e != null) {
                    Log.d(TAG, "Event: " + e.getPackageName() + "\t" + e.getTimeStamp());
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();
        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
        return usageStatsList;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<UsageStats> firstprintUsageStats(List<UsageStats> usageStatsList){
        ArrayList<UsageStats> returning = new ArrayList<>();
        for (UsageStats u : usageStatsList){
            if(u.getPackageName().contains("Alarm") || (u.getTotalTimeInForeground() == 0)){
            }else {
                returning.add(u);
            }
        }
        return returning;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }
}
