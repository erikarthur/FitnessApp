package org.arthurweb.fitnessapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExerciseActivity extends AppCompatActivity {


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle data = intent.getExtras();
            //gpsValues _gpsValues = new gpsValues();
            gpsValues _gpsValues = (gpsValues) intent.getParcelableExtra(Constants.UIDataValue);

            //Log.i(Constants.TAG, String.format("%f", _gpsValues.get_lat()));
            if (_gpsValues != null) {

                TextView speed = (TextView) findViewById(R.id.speedValue);
                speed.setText(String.format("%.2f", _gpsValues.get_speed()));

                TextView distance = (TextView) findViewById(R.id.distanceValue);
                distance.setText(String.format("%.2f", _gpsValues.get_distance()));

                TextView lat = (TextView) findViewById(R.id.latValue);
                lat.setText(String.format("%.3f", _gpsValues.get_lat()));

                TextView lon = (TextView) findViewById(R.id.lonValue);
                lon.setText(String.format("%.3f", _gpsValues.get_lon()));

                TextView elevation = (TextView) findViewById(R.id.elevationValue);
                elevation.setText(String.format("%.0f", _gpsValues.get_elevation()));

                TextView pace = (TextView) findViewById(R.id.paceValue);
                pace.setText(_gpsValues.get_pace());
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        Button endSession = (Button)findViewById(R.id.endSessionButton);
        endSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent endSessionIntent = new Intent(Constants.sessionStop);
                sendBroadcast(endSessionIntent);
                closeActivity();
            }
        });
    }
    @Override protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(Constants.UIData));
    }

    @Override protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);

    }

    public void closeActivity() {
        this.finish();
    }
}
