package com.google.android.exoplayer.finalmusicapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.List;


import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.author;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.position;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.simpleExoPlayer;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.thumbnailURL;
import static com.google.android.exoplayer.finalmusicapp.MediaPlayer_Activity.title;

public class MainRecyclerviewActivity extends AppCompatActivity implements Action, ServiceConnection {

    ArrayList<TrackFiles> trackFilesArrayList = new ArrayList<>();
    EditText editText;
    FirebaseFirestore db;
    RecyclerViewAdapter myAdapter;
    String message;

    FirebaseRemoteConfig remoteConfig;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    private static final String SHARED_PREFS = "SharedPrefs";
    public static int prevPosition=0;

    public static final String ACTION_PLAY="PLAY";
    MusicService musicService;
    ImageView playPauseButton;

    ImageView musicBarImageView;
    TextView musicBarTitle, musicBarAuthor;

    LoaderDialog loaderDialog = new LoaderDialog(MainRecyclerviewActivity.this);
    private Dialog dialog;


   public static MotionLayout motionLayout;


    private AdView adView;
    private FrameLayout frameLayout;

    
    String  versionUri;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(String.valueOf(MainRecyclerviewActivity.class), "on create" );
        setContentView(R.layout.activity_main_recyclerview);


  /*      View view = getLayoutInflater().inflate(R.layout.custom_loader, null);
        dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(view);
        dialog.show();*/


        motionLayout = findViewById(R.id.motionLayout_recyclerViewActivity);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

       /* frameLayout = findViewById(R.id.bannerAd_recyclerview);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_adunit_admob));
        frameLayout.addView(adView) ;
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize= getAdSize ();
        adView.setAdSize (adSize);
        adView.loadAd(adRequest) ;*/

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "4BP2MQ5X2S3XSKFQBKKW");


        musicBarImageView = findViewById(R.id.musicBar_book_img_id);
        musicBarTitle = findViewById(R.id.musicBar_title_textview);
        musicBarAuthor = findViewById(R.id.musicBar_Author);

        playPauseButton = findViewById(R.id.musicBar_button_playPause);



        //loading data from shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loadSharedPrefData();


        Log.e(String.valueOf(MainRecyclerviewActivity.class), "prevPosition is "+prevPosition );


        Intent intentService = new Intent(MainRecyclerviewActivity.this, MusicService.class);
        bindService(intentService, MainRecyclerviewActivity.this, BIND_AUTO_CREATE);

        int currentVersionCode;
        currentVersionCode = getCurrentVersionCode();
        Log.d("myApp", String.valueOf(currentVersionCode));



        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings =new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5).build();

        remoteConfig.setConfigSettingsAsync(configSettings);


        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()){
                    final String new_version_code = remoteConfig.getString("new_version_code");
                    if(Integer.parseInt(new_version_code) > getCurrentVersionCode()){
                        showUpdateDialog();
                    }
                }
            }
        });



        editText = findViewById(R.id.editTextTextPersonName2);

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);




        db = FirebaseFirestore.getInstance();

        myrv.setHasFixedSize(true);
        myrv.setLayoutManager(new GridLayoutManager(this, 3));


        db.collection("TrackFiles")
               .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {
                            TrackFiles obj = d.toObject(TrackFiles.class);
                            trackFilesArrayList.add(obj);
                        }

                        //  myAdapter.notifyDataSetChanged();
                        myAdapter = new RecyclerViewAdapter(MainRecyclerviewActivity.this, trackFilesArrayList);
                        myrv.setAdapter(myAdapter);

                        Glide.with(MainRecyclerviewActivity.this).load(trackFilesArrayList.get(prevPosition).getThumbnailUrl()).into(musicBarImageView);

                        musicBarAuthor.setText(trackFilesArrayList.get(prevPosition).getSinger());
                        musicBarTitle.setText(trackFilesArrayList.get(prevPosition).getTitle());
/* dialog.dismiss();*/
                    }
                });


        //TODO search feature
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }






    private void showUpdateDialog() {
        final AlertDialog dialog;
        new AlertDialog.Builder(  this)
                .setTitle("New Update Available")
                .setMessage("Update Now")
        .setPositiveButton( "Update", new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which){
            try {
                db.collection("versionUri").document("VDx1ZMV9fgX59vHSVg2k").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                 versionUri = documentSnapshot.getString("new_version_code");
                                Toast.makeText(MainRecyclerviewActivity.this, "Please Download Latest Version", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(versionUri)));
                            }
                        });


            } catch (Exception e){
                Toast.makeText(MainRecyclerviewActivity.this, "update dialog update intent is wrong", Toast.LENGTH_SHORT).show();
            }
        }
        }).show();
    }

    private int getCurrentVersionCode() {
        PackageInfo packageInfo = null;

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        }  catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageInfo.versionCode;
    }


    public void musicBarOnPressed( View view ) throws InterruptedException {
        motionLayout.transitionToEnd();

        if(simpleExoPlayer==null){
            Boolean musicBarBoolean=true;
            Intent startMediaPlayer = new Intent(this, MediaPlayer_Activity.class);
            startMediaPlayer.putExtra("musicBar",musicBarBoolean);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(startMediaPlayer);
                }
            }, 300);


        }else {
        Intent openMediaPlayer = new Intent(MainRecyclerviewActivity.this, MediaPlayer_Activity.class);
        openMediaPlayer.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityIfNeeded(openMediaPlayer, 0);
                }
            }, 300);

        }
    }

    public void musicBarSet(){

        if(thumbnailURL!=null) {
            Glide.with(this).load(thumbnailURL).into(musicBarImageView);
            musicBarTitle.setText(title);
            musicBarAuthor.setText(author);
        }else
            Log.e(String.valueOf(MainRecyclerviewActivity.class), "null intent");
    }

    public void forwardClicked(View view) {

        long sTime = simpleExoPlayer.getCurrentPosition();
        long bTime = 15000; // time how much to skip

        if ((sTime + bTime) < trackFilesArrayList.get(position).getduration()) {
            sTime = sTime + bTime;
            simpleExoPlayer.seekTo(sTime);
        } else {
            Toast.makeText(getApplicationContext(), "Cannot jump forward 15 seconds", Toast.LENGTH_SHORT).show();
        }

    }

    public void rewindClicked(View view) {
        long sTime = simpleExoPlayer.getCurrentPosition();
        long bTime = 15000; // time how much to skip

        if ((sTime - bTime) > 0) {
            sTime = sTime - bTime;
            simpleExoPlayer.seekTo(sTime);
        } else {
            Toast.makeText(getApplicationContext(), "Cannot jump backward 15 seconds", Toast.LENGTH_SHORT).show();
        }
    }


    public void playClicked(View view) throws InterruptedException {

        if(simpleExoPlayer==null){
            motionLayout.transitionToEnd();
            Boolean musicBarBoolean=true;
            Intent startMediaPlayer = new Intent(this, MediaPlayer_Activity.class);
            startMediaPlayer.putExtra("musicBar",musicBarBoolean);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(startMediaPlayer);
                }
            }, 300);

        }else {
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra("myActionName", ACTION_PLAY);
            this.startService(intent);
        }

    }



    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(String.valueOf(MainRecyclerviewActivity.class),"on start");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        motionLayout.transitionToStart();
        Log.e(String.valueOf(MainRecyclerviewActivity.class), "on restart");
        musicBarSet();
        try{
        if (!simpleExoPlayer.isPlaying()) {
            playPauseButton.setImageResource(R.drawable.play_button_vector);

        } else {
            playPauseButton.setImageResource(R.drawable.pause_button_vector);
        }}
        catch (Exception e){
            Log.e(String.valueOf(MainRecyclerviewActivity.class), "Exception in recyclerviewActivity in on Restart" );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
        Log.e(String.valueOf(MainRecyclerviewActivity.class),"on destroy");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(String.valueOf(MainRecyclerviewActivity.class), "on stop" );
    }

    @Override
    public void playPauseClicked() {

        Log.e(String.valueOf(MainRecyclerviewActivity.class), "playPauseClicked Action" );
        if (!simpleExoPlayer.isPlaying()) {
             playPauseButton.setImageResource(R.drawable.play_button_vector);
        } else {
            playPauseButton.setImageResource(R.drawable.pause_button_vector);
        }

    }

    @Override
    public void continueMediaPlayer() {
        Intent openMediaPlayer = new Intent(MainRecyclerviewActivity.this, MediaPlayer_Activity.class);
        openMediaPlayer.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMediaPlayer, 0);
    }

    //shared pref methods
    public void saveData(){
        prevPosition =position;
        editor.putInt("prevPosition ", prevPosition);
        editor.apply();
        Log.e(String.valueOf(MainRecyclerviewActivity.class), "prevPosition is "+prevPosition );
    }

    public void loadSharedPrefData() {
            prevPosition = sharedPreferences.getInt("prevPosition ", 0);
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;

        musicService = binder.getService();
        musicService.setCallBack(MainRecyclerviewActivity.this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }

  /*  private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }*/

}