package org.arthurweb.fitnessapp;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;

import java.util.Random;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class elevationIntentService extends IntentService {

    public elevationIntentService() {
        super("elevationIntentService");
    }

    public boolean running = true;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.sessionStop) {
                //send stop signals to other services
                running = false;
            }
        }
    };


    @Override
    protected void onHandleIntent(Intent intent) {
        registerReceiver(receiver, new IntentFilter(Constants.sessionStop));
        Random rnd = new Random();
        while (running) {
            try {
                Thread.sleep(1000);
                float elevation = rnd.nextFloat();
                Intent elevationDataIntent = new Intent(Constants.elevationData);
                elevationDataIntent.putExtra(Constants.elevationDataValue, elevation);
                sendBroadcast(elevationDataIntent);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        unregisterReceiver(receiver);
    }

}
