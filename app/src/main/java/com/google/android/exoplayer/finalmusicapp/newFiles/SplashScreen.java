package com.google.android.exoplayer.finalmusicapp.newFiles;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer.finalmusicapp.userAuth.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       Intent intent = new Intent(this, LoginActivity.class);
       startActivity(intent);
       finish();
    }
}
