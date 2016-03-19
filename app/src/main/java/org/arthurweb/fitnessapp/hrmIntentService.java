package org.arthurweb.fitnessapp;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Random;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class hrmIntentService extends IntentService {

    public hrmIntentService() {
        super("hrmIntentService");
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
                float hrm = rnd.nextFloat();
                Intent hrmDataIntent = new Intent(Constants.hrmData);
                hrmDataIntent.putExtra(Constants.hrmDataValue, hrm);
                sendBroadcast(hrmDataIntent);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        unregisterReceiver(receiver);
    }
}
