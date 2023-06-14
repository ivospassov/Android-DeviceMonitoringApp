package com.example.c66_project.receivers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.c66_project.MainActivityApp;
import com.example.c66_project.services.BatteryManagerService;
import com.example.c66_project.services.SensorManagerService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    public void onReceive(Context context, Intent intent) {
        Log.i("Receiver", "Receiver caught event!");

//        Intent startMainActivity = new Intent(context, MainActivityApp.class);
//        startMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(startMainActivity);

        Intent batteryService = new Intent(context, BatteryManagerService.class);
        Intent sensorService = new Intent(context, SensorManagerService.class);

        context.startForegroundService(batteryService);
        context.startForegroundService(sensorService);
    }
}
