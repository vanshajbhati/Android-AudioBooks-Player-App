package com.google.android.exoplayer.finalmusicapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Gallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoaderDialog extends AppCompatActivity  {

    private Activity activity;
    private Dialog dialog;



    LoaderDialog(Activity myActivity){
        activity = myActivity;

    }



    void startLoaderDialog(Activity myActivity){



        dialog = new Dialog(myActivity);
       dialog.setContentView(R.layout.custom_loader);
       dialog.setCancelable(true);
       dialog.show();

    }

    void dismissDialog(){
            dialog.dismiss();

    }
}
