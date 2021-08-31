package com.google.android.exoplayer.finalmusicapp;

import android.app.Service;
import android.content.Intent;

import android.os.Binder;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.audioUrl;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.author;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.currentTitle;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.duration;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.notificationManager;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.simpleExoPlayer;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.position;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.editor;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.songPrevPosition;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.thumbnailURL;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.title;


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
           songPrevPosition=  (int)simpleExoPlayer.getCurrentPosition();
        editor.putInt(currentTitle, songPrevPosition);
        editor.apply();
        simpleExoPlayer.release();
        stopSelf();
    }

}