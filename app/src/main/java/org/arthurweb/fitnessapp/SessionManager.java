package org.arthurweb.fitnessapp;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SessionManager extends IntentService {

    private boolean running = true;
    private static final int MAX_AVAILABLE = 1;
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);
    private long startTime  = 0;
    private float metersToMPH = 2.236936f;
    private float metersToMiles = 0.000621371f;

    private gpsValues _gpsValues = new gpsValues();

    public SessionManager() {
        super("SessionManager");
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constants.sessionStop:
                    running = false;
                    break;
                case Constants.gpsData:
                    if (intent.hasExtra(Constants.gpsDataValues)) {
                        try {
                            available.acquire();
                            //Bundle data = intent.getExtras();
                            _gpsValues = (gpsValues) intent.getParcelableExtra(Constants.gpsDataValues);
                            _gpsValues.set_elapsedTime(_gpsValues.get_elapsedTime() - startTime);
                            _gpsValues.set_speed(_gpsValues.get_speed() * metersToMPH);
                            _gpsValues.set_distance(_gpsValues.get_distance() * metersToMiles);
                            available.release();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Constants.elevationData:
                    if (intent.hasExtra(Constants.elevationDataValue)) {
                        try {
                            available.acquire();
                            _gpsValues.set_elevation(intent.getFloatExtra(Constants.elevationDataValue, Float.MIN_VALUE));
                            available.release();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Constants.hrmData:
                    if (intent.hasExtra(Constants.hrmDataValue)) {
                        try {
                            available.acquire();
                            _gpsValues.set_hrm(intent.getFloatExtra(Constants.hrmDataValue, Float.MIN_VALUE));
                            available.release();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onHandleIntent(Intent intent) {

        registerReceiver(receiver, new IntentFilter(Constants.sessionStop));
        registerReceiver(receiver, new IntentFilter(Constants.gpsData));

        //intent
        Intent exerciseUiIntent = new Intent(this, ExerciseActivity.class);
        exerciseUiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exerciseUiIntent);

        //Intent gpsIntent = new Intent(this, gpsIntentService.class);
        //startService(gpsIntent);
        final Intent gpsIntent = new Intent(this, gpsService.class);
        Thread t = new Thread(){
            public void run() {
                startService(gpsIntent);
            }
        };
        t.start();

        startTime = System.currentTimeMillis();

        Intent elevationIntent = new Intent(this, elevationIntentService.class);
        startService(elevationIntent);

        Intent hrmIntent = new Intent(this, hrmIntentService.class);
        startService(hrmIntent);

        //start a timertask that records data every second
        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 3000, 1000);

        while (running) {
            try {
                Thread.sleep(100);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        stopService(gpsIntent);
        myTask.cancel();
        myTimer.cancel();
        unregisterReceiver(receiver);
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            //send data to UI activity
            Intent updateUiIntent = new Intent(Constants.UIData);
            try {
                available.acquire();
                updateUiIntent.putExtra(Constants.UIDataValue, _gpsValues);
                available.release();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            sendBroadcast(updateUiIntent);
        }
    }


}
