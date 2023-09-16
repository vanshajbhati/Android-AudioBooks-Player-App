package com.google.android.exoplayer.finalmusicapp;

import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.currentTitle;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.editor;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.notificationManager;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.mediaPlayer;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.songPrevPosition;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class MusicService extends Service {

    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREV = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_FORWARD = "FORWARD";
    public static final String ACTION_REWIND = "REWIND";
    public static final String ACTION_CONTINUE = "CONTINUE";
    ActionPlaying actionPlaying;
    Action action;

    private final IBinder mBinder = new MyBinder();



    @Nullable
    @Override
    public  IBinder onBind(Intent intent) {
        return mBinder;
    }



    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();

    String actionName = intent.getStringExtra("myActionName");

        if (actionName != null) {
            switch (actionName)
            {
                case ACTION_PLAY:
                    if(actionPlaying!= null){
                        if(action!= null){
                        actionPlaying.playClicked();
                        action.playPauseClicked();
                    }}
                    break;
                case ACTION_NEXT:
                    if(actionPlaying != null){
                        actionPlaying.nextClicked();
                    }

                    break;
                case ACTION_PREV:
                    if(actionPlaying != null){
                        actionPlaying.prevClicked();

                    }

                    break;
                case ACTION_FORWARD:
                    if(actionPlaying != null){
                        actionPlaying.forwardClicked();
                    }
                    break;
                case ACTION_REWIND:
                    if(actionPlaying != null){
                        actionPlaying.rewindClicked();
                    }
                    break;
                case ACTION_CONTINUE:
                    Toast.makeText(this, "continue", Toast.LENGTH_SHORT).show();
                        action.continueMediaPlayer();

            }
        }
        return START_STICKY;
    }


    public void setCallBack(ActionPlaying actionPlaying){
        this.actionPlaying =  actionPlaying;
    }


    public void setCallBack(Action action) {
        this.action =  action;
    }

    public void onTaskRemoved(Intent rootIntent) {
            super.onTaskRemoved(rootIntent);
           notificationManager.cancelAll();
           songPrevPosition=  (int)mediaPlayer.getCurrentPosition();
        editor.putInt(currentTitle, songPrevPosition);
        editor.apply();
        mediaPlayer.release();
        stopSelf();
    }

}