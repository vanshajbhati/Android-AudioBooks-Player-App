package com.google.android.exoplayer.finalmusicapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationClass extends Application {
    public static final String CHANNEL_ID_1 = "CHANNEL 1";
    public static final String CHANNEL_ID_2 = "CHANNEL 2";
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREV="PREVIOUS";
    public static final String ACTION_PLAY="PLAY";
    public static final String ACTION_FORWARD = "FORWARD";
    public static final String ACTION_REWIND = "REWIND";
    public static final String ACTION_CONTINUE = "CONTINUE";



    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }


        private void createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                 NotificationChannel notificationChannel1 = new NotificationChannel(CHANNEL_ID_1,
                        "Channel(1)", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel1.setDescription("Channel 1 Description");
                NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_ID_2,
                        "Channel(2)", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel2.setDescription("Channel 2 Description");
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(notificationChannel1);
                notificationManager.createNotificationChannel(notificationChannel2);
            }
        }

}
