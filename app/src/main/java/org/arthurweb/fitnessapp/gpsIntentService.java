package org.arthurweb.fitnessapp;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import com.google.android.gms.location.LocationListener;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
//public class gpsIntentService extends IntentService implements LocationListener,
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener {
//
//    LocationRequest mLocationRequest;
//    GoogleApiClient mGoogleApiClient;
//    Location mCurrentLocation;
//    String mLastUpdateTime;
//
//    private static final String TAG = "Fitness App";
//    private static final long INTERVAL = 1000 * 1;
//    private static final long FASTEST_INTERVAL = 1000 * 1;
//
//    private LocationManager locationManager;
//
//    public gpsIntentService() {
//        super("gpsIntentService");
//    }
//
//    public boolean running = true;
//
//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//
//    public void onStatusChanged(String s, int i, Bundle b) {
//        //super.onStatusChanged(this);
//        s = null;
//    }
//
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction() == Constants.sessionStop) {
//                //send stop signals to other services
//                running = false;
//            }
//        }
//    };
//
//    //@Override
//    public void onProviderDisabled(String provider) {
//
//        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        startActivity(intent);
//        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
//                Toast.LENGTH_SHORT).show();
//    }
//
//    //@Override
//    public void onProviderEnabled(String provider) {
//
//        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
//                Toast.LENGTH_SHORT).show();
//    }
//
//    private boolean isGooglePlayServicesAvailable() {
//        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (ConnectionResult.SUCCESS == status) {
//            return true;
//        } else {
//            //GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
//            return false;
//        }
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
//        startLocationUpdates();
//    }
//
//    protected void startLocationUpdates() {
//        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
//        Log.d(TAG, "Location update started ..............: ");
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    protected void stopLocationUpdates() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(
//                mGoogleApiClient, this);
//        Log.d(TAG, "Location update stopped .......................");
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.d(TAG, "Connection failed: " + connectionResult.toString());
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        Log.d(TAG, "Firing onLocationChanged..............................................");
//        mCurrentLocation = location;
//        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//        String msg = "New Latitude: " + location.getLatitude()
//                + "New Longitude: " + location.getLongitude();
//
////        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
//        Intent gpsDataIntent = new Intent(Constants.gpsData);
//        gpsDataIntent.putExtra(Constants.gpsLat, location.getLatitude());
//        gpsDataIntent.putExtra(Constants.gpsLon, location.getLongitude());
//        gpsDataIntent.putExtra(Constants.gpsSpeed, location.getSpeed());
//        gpsDataIntent.putExtra(Constants.gpsHeading, location.getBearing());
//        sendBroadcast(gpsDataIntent);
//
//    }
//
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        registerReceiver(receiver, new IntentFilter(Constants.sessionStop));
//        createLocationRequest();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(onLocationChanged)
//                .addOnConnectionFailedListener(this)
//                .build();
//        mGoogleApiClient.connect();
//        startLocationUpdates();
////        Random rnd = new Random();
//        while (running) {
//            try {
//                Thread.sleep(100);
//
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        stopLocationUpdates();
//        mGoogleApiClient.disconnect();
//        unregisterReceiver(receiver);
//    }
//
//}
