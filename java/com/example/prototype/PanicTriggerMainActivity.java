package com.example.prototype;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Vibrator;
import androidx.core.content.ContextCompat;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ServiceCompat;

import android.os.Bundle;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;



public class PanicTriggerMainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Vibrator v;
    private float lastX, lastY, lastZ;
    private float deltaX, deltaY, deltaZ;
    private float vibrateThreshold = 0.5f; // set the threshold value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panictrigger_activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        displayCurrentValues();
        displayMaxValues();

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just plain noise
        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
        if ((deltaZ > vibrateThreshold) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
            v.vibrate(50);
        }

        // store the current values
        lastX = event.values[0];
        lastY = event.values[1];
        lastZ = event.values[2];
    }

    public void displayCurrentValues() {
        if ( (deltaX>25) || (deltaY>25) || (deltaZ>25) )
            FirebaseDatabase(Float.toString(deltaX),Float.toString(deltaY),Float.toString(deltaZ));
    }

    public void displayMaxValues() {
        if (deltaY > 15){
            EmergencyAlert();
        }
    }

    private void EmergencyAlert() {
        Intent i = new Intent(this, Splash.class);
        startActivity(i);
    }

    public void FirebaseDatabase(String x,String y, String z){
        Long tsLong = System.currentTimeMillis();
        String t = tsLong.toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
        DatabaseReference newnodes = databaseReference.push();
        newnodes.child("x").setValue(x);
        newnodes.child("y").setValue(y);
        newnodes.child("z").setValue(z);
        newnodes.child("t").setValue(t);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}