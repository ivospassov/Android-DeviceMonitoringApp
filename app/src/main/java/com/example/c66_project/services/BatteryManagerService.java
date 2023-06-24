package com.example.c66_project.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.c66_project.R;
import com.example.c66_project.utils.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BatteryManagerService extends Service {

    private final static String CONNECTED_MESSAGE = "Date when charge started: %s|Battery Status when connected: %d percent\n";
    private final static String DISCONNECTED_MESSAGE = "Date when charge stopped: %s|Battery Status after disconnected: %d percent\n";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy --- HH:mm:ss");
    private final Logger logger = new Logger(this);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(2001, setupNotification());

        return START_NOT_STICKY;
    }

    private Notification setupNotification() {
        NotificationChannel channel1 = new NotificationChannel(
                "NOTIFICATION_CHANNEL_ID",
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel1.setDescription("This is channel 1");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);

        return new NotificationCompat.Builder((Context) this, "NOTIFICATION_CHANNEL_ID")
                .setContentTitle("Welcome back!")
                .setContentText("Let's get to work")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("BATTERYSERVICE", "Service is started");
        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("BATTERYSERVICE", "Service is stopped");
        unregisterReceiver(receiver);
    }

    private Integer acquireBatteryPercentage() {
        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            @SuppressLint("DefaultLocale")
            String info = intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)
                    ? String.format(CONNECTED_MESSAGE,
                    dateTimeFormatter.format(LocalDateTime.now()),
                    acquireBatteryPercentage())
                    : String.format(DISCONNECTED_MESSAGE,
                    dateTimeFormatter.format(LocalDateTime.now()),
                    acquireBatteryPercentage());

//            -------------------------------------------------------------------
            Log.i("BatteryService", "Battery service activated!");
            logger.log(info);
        }
    };
}
