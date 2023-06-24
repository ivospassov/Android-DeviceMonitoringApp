package com.example.c66_project.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;

public class ServiceManager extends ContextWrapper {

    public ServiceManager(Context base) {
        super(base);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo currentService : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(currentService.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
