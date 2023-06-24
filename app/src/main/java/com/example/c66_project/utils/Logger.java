package com.example.c66_project.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.c66_project.services.SensorManagerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class Logger extends ContextWrapper {
    private final static String FILE_NAME = "logs.txt";
    private final static String DIR = "/files/";

    public Logger(Context base) {
        super(base);
    }

    public void log(String data) {

        /**
         * getDataDir() will depict the directory in which our logs.txt file
         * was created
         */
        Log.i("DIR", getDataDir().toString());
        File file = new File(getDataDir() + DIR + FILE_NAME);
        try {
            if (!file.exists()) {
                Files.createDirectories(Paths.get(getDataDir() + DIR));
                Files.createFile(Paths.get(file.getAbsolutePath()));
            }
            try (FileOutputStream stream = new FileOutputStream(file, true)) {
                stream.write(data.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Sensor service requires to be stopped for a small period of time,
         * otherwise the sensor will pick up MULTIPLE variations in the movement of
         * the device, and log in the same event multiple times. However, not all
         * duplicate events can be caught, so they are decreased to minimum
         */
        ServiceManager serviceManager = new ServiceManager(this);
        boolean isSensorServiceRunning = serviceManager.isMyServiceRunning(SensorManagerService.class);
        if (!isSensorServiceRunning) {
            startForegroundService(new Intent(this, SensorManagerService.class));
        }
    }
}
