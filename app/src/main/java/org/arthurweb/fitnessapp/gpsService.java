package org.arthurweb.fitnessapp;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class gpsService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Timer sendGuiRegularTimer;
    private Timer sendGuiAverageTimer;

    private TimerTask sendGuiRegularTimerTask;
    private TimerTask sendGuiAverageTimerTask;

    private final Lock averageHrmMutex = new ReentrantLock();
    private final Lock HrmMutex = new ReentrantLock();
    private HRM_Thread t;

    private boolean running = true;
    private boolean firstTime = true;
    private boolean paused = false;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    Location oldLocation = null;
    String mLastUpdateTime;

    private float distanceTravelled = 0;

    private static final String TAG = "Fitness App";
    private static final long INTERVAL = 1000 * 1;
    private static final long FASTEST_INTERVAL = 1000 * 1;

    private long startTime = 0;

    private LocationManager locationManager;

    public gpsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        registerReceiver(receiver, new IntentFilter(Constants.sessionStop));
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.sessionStop) {
                //send stop signals to other services
                running = false;
            }
        }
    };

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        try {
            Thread.sleep(1000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        mGoogleApiClient.connect();
        t = new HRM_Thread();
        t.start();
        return START_STICKY;
    }

    private class HRM_Thread extends Thread{
        @Override
        public void run() {
            super.run();
            long cTime = 0;

            //String time = null;

//            sendGuiAverageTimer = new Timer();
//            sendGuiRegularTimer = new Timer();

           // initializeTimers();

            //main loop for the thread
            while (running) {
                try {
                    sleep(1000);
                    //cTime = System.currentTimeMillis();
                    //Log.d(TAG, "GPS Service in main thread.......................");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
        startTime = System.currentTimeMillis();
    }
    protected void startLocationUpdates() {

        try {
            //add checkPermission from sample
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "Location update started ..............: ");

        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }
    @Override
    public void onLocationChanged(Location location) {

        Intent gpsDataIntent = new Intent(Constants.gpsData);
        gpsDataIntent.putExtra(Constants.gpsDataValues, location);
        sendBroadcast(gpsDataIntent);

    }
}
