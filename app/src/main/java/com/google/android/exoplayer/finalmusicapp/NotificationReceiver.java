package com.google.android.exoplayer.finalmusicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREV="PREVIOUS";
    public static final String ACTION_PLAY="PLAY";
    public static final String ACTION_FORWARD = "FORWARD";
    public static final String ACTION_REWIND = "REWIND";
    public static final String ACTION_CONTINUE = "CONTINUE";




    @Override
    public void onReceive(Context context, Intent intent) {


        Intent intentNR = new Intent(context, MusicService.class);
        if (intent.getAction() != null){
               switch (intent.getAction()){
                   case ACTION_PLAY:
                       intentNR.putExtra("myActionName", intent.getAction());
                       context.startService(intentNR);
                       break;
                   case ACTION_PREV:
                       intentNR.putExtra("myActionName", intent.getAction());
                       context.startService(intentNR);
                       break;
                   case ACTION_NEXT:
                       intentNR.putExtra("myActionName", intent.getAction());
                       context.startService(intentNR);
                           break;
                   case ACTION_FORWARD:
                       intentNR.putExtra("myActionName", intent.getAction());
                       context.startService(intentNR);
                       break;
                   case ACTION_REWIND:
                       intentNR.putExtra("myActionName", intent.getAction());
                       context.startService(intentNR);
                       break;
                   case ACTION_CONTINUE:
                       intentNR.putExtra("myActionName", intent.getAction());
                       context.startService(intentNR);
                       break;

               }
        }

    }
}
