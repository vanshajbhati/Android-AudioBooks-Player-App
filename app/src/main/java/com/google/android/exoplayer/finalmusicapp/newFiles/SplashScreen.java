package com.google.android.exoplayer.finalmusicapp.newFiles;



import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer.finalmusicapp.MainRecyclerviewActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       Intent intent = new Intent(this, MainRecyclerviewActivity.class);
       startActivity(intent);
       finish();
    }
}
