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



    void startLoaderDialog(){

        View view = getLayoutInflater().inflate(R.layout.custom_loader, null);
        dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
       dialog.setContentView(view);
       dialog.show();

    }

    void dismissDialog(){
            dialog.dismiss();

    }
}
