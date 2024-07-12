package com.example.prototype;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.content.Context;
import android.util.Log;

/**
 * Created by 1505560 on 29-Oct-17.
 */

public class Background extends Service implements SensorEventListener {

    private static final String TAG = Background.class.getSimpleName();

    private SensorManager mSensorManager = null;
    private boolean mLogging = false;

    private static float previousValue;

    private float mThresholdMin, mThresholdMax;

    public static final String KEY_SENSOR_TYPE = "sensor_type";

    public static final String KEY_THRESHOLD_MIN_VALUE = "threshold_min_value";

    public static final String KEY_THRESHOLD_MAX_VALUE = "threshold_max_value";

    public static final String KEY_LOGGING = "logging";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // get sensor manager on starting the service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // have a default sensor configured
        int sensorType = Sensor.TYPE_LIGHT;

        Bundle args = intent.getExtras();

        // get some properties from the intent
        if (args != null) {

            // set sensortype from bundle
            if (args.containsKey(KEY_SENSOR_TYPE))
                sensorType = args.getInt(KEY_SENSOR_TYPE);

            // optional logging
            mLogging = args.getBoolean(KEY_LOGGING);

            // treshold values
            // since we want to take them into account only when configured use min and max
            // values for the type to disable
            mThresholdMin = args.containsKey(KEY_THRESHOLD_MIN_VALUE) ? args.getFloat(KEY_THRESHOLD_MIN_VALUE) : Float.MIN_VALUE;
            mThresholdMax = args.containsKey(KEY_THRESHOLD_MAX_VALUE) ? args.getFloat(KEY_THRESHOLD_MAX_VALUE) : Float.MAX_VALUE;
        }

        // we need the light sensor
        Sensor sensor = mSensorManager.getDefaultSensor(sensorType);

        // TODO we could have the sensor reading delay configurable also though that won't do much
        // in this use case since we work with the alarm manager
        mSensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // ignore this since not linked to an activity
        return null;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // for recording of data use an AsyncTask, we just need to compare some values so no
        // background stuff needed for this

        // Log that information for so we can track it in the console (for production code remove
        // this since this will take a lot of resources!!)
        if (mLogging) {

            // grab the values
            StringBuilder sb = new StringBuilder();
            for (float value : event.values)
                sb.append(value).append(" | ");

            Log.d(TAG, "received sensor valures are: " + sb+ " and previosValue was: "+previousValue);
        }

        // get the value
        // TODO we could make the value index also configurable, make it simple for now
        float sensorValue = event.values[0];

        // if first value is below min or above max threshold but only when configured
        // we need to enable the screen
        if ((previousValue > mThresholdMin && sensorValue < mThresholdMin)
                || (previousValue < mThresholdMax && sensorValue > mThresholdMax)) {

            // and a check in between that there should have been a non triggering value before
            // we can mark a given value as trigger. This is to overcome unneeded wakeups during
            // night for instance where the sensor readings for a light sensor would always be below
            // the threshold needed for day time use.

            // TODO we could even make the actions configurable...
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

            // Create a wake lock with a timeout
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
            // Acquire the wake lock with a timeout of 10 minutes
            wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);

            // Perform your operations

            // Release the wake lock
            wakeLock.release();

            // optional to release screen lock
            //KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(getApplicationContext().KEYGUARD_SERVICE);
            //KeyguardManager.KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock(TAG);
            //keyguardLock.disableKeyguard();
        }

        previousValue = sensorValue;

        // stop the sensor and service
        mSensorManager.unregisterListener(this);
        stopSelf();
    }
}
