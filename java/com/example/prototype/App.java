package com.example.prototype;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;


import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    public static final String CHANNEL_ID = "Channel_Id";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //Open EmergencyActivity when power button is tapped 4+ times
        startService(new Intent(getApplicationContext(), LockService.class));
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Example Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

}
