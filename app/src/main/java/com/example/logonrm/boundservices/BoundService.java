package com.example.logonrm.boundservices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Chronometer;

/**
 * Created by logonrm on 12/06/2017.
 */

public class BoundService extends Service {
    private static String LOG_TAG = "BoundServices";
    private IBinder myBinder = new MyBinder();
    private Chronometer chronometer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return myBinder;
    }

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "in onCreate");
        super.onCreate();
        chronometer = new Chronometer(this);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "in onDestroy");
        chronometer.stop();
    }

    public String getTimestamp() {
        long elapsedMilis = SystemClock.elapsedRealtime() - chronometer.getBase();
        int hours = (int) (elapsedMilis / 3600000);
        int minutes = (int) (elapsedMilis - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMilis - hours * 3600000 - minutes * 60000) / 1000;
        int milis = (int) (elapsedMilis - hours * 3600000 - minutes * 60000 - seconds * 1000);
        return hours + ":" + minutes + ":" + seconds + ":" + milis;
    }

    public class MyBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }
}
