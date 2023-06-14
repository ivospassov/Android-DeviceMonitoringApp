package com.example.c66_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.c66_project.services.BatteryManagerService;
import com.example.c66_project.services.SensorManagerService;

public class MainActivityApp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("MainActivity", "Main activity started!");

        startForegroundService(new Intent(this, BatteryManagerService.class));
        startForegroundService(new Intent(this, SensorManagerService.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}