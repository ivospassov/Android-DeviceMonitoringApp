package com.example.c66_project.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.c66_project.R;
import com.example.c66_project.utils.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SensorManagerService extends Service {
    private final static String DROP_MESSAGE = "Device has fallen at: %s";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy --- HH:mm:ss");
    private final Logger logger = new Logger(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SENSORSERVICE", "Service is started");
        startForeground(2002, setupNotification());
        return START_STICKY;
    }

    private Notification setupNotification() {
        NotificationChannel channel1 = new NotificationChannel(
                "NOTIFICATION_CHANNEL_ID_2",
                "Channel 2",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel1.setDescription("This is channel 2");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);

        return new NotificationCompat.Builder((Context) this, "NOTIFICATION_CHANNEL_ID_2")
                .setContentTitle("Welcome back!")
                .setContentText("Let's get to work")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }

    @Override
    public void onCreate() {
        sensorConfiguration();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        @SuppressLint("DefaultLocale")
        public void onSensorChanged(SensorEvent event) {
            float xValue = event.values[0];
            float yValue = event.values[1];
            float zValue = event.values[2];

            if (calculateTotalAcceleration(xValue, yValue, zValue) < 0.2) {
                @SuppressLint("DefaultLocale") String message =
                        String.format(DROP_MESSAGE, dateTimeFormatter.format(LocalDateTime.now()));
                logger.log(message);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    /**
     * Calculate the standard acceleration by measuring
     * the total value of squares of the acceleration signals,
     * which is by the gravity acceleration (g)
     **/
    private Double calculateTotalAcceleration(float xValue, float yValue, float zValue) {
        return Math.sqrt(Math.pow(xValue, 2) + Math.pow(yValue, 2) + Math.pow(zValue, 2));
    }

    private void sensorConfiguration() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
