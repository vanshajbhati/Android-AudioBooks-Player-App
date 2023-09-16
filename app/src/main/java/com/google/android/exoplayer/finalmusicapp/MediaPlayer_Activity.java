package com.google.android.exoplayer.finalmusicapp;

import static com.google.android.exoplayer.finalmusicapp.ApplicationClass.ACTION_CONTINUE;
import static com.google.android.exoplayer.finalmusicapp.ApplicationClass.ACTION_FORWARD;
import static com.google.android.exoplayer.finalmusicapp.ApplicationClass.ACTION_NEXT;
import static com.google.android.exoplayer.finalmusicapp.ApplicationClass.ACTION_PLAY;
import static com.google.android.exoplayer.finalmusicapp.ApplicationClass.ACTION_PREV;
import static com.google.android.exoplayer.finalmusicapp.ApplicationClass.ACTION_REWIND;
import static com.google.android.exoplayer.finalmusicapp.ApplicationClass.CHANNEL_ID_2;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.Player;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MediaPlayer_Activity extends AppCompatActivity
        implements ActionPlaying, ServiceConnection , Player.EventListener{



    ImageView next, prev, play, rewind, forward;
    TextView titleTextView;

    //Song image view
    static ImageView songImageURL;

    //notification
   public static NotificationManager notificationManager;

    //sharedPreferencesData constant variables
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    private static final String SHARED_PREFS = "SharedPrefs";


    public static int songPrevPosition;
    public static String currentTitle;
    public static int position =0;



    public static boolean isPlaying = false;
    MusicService musicService;


    //player files
    private TextView textCurrentTime, textTotalDuration, authorTextView;
    private SeekBar playerSeekBar;
    private Handler handler = new Handler();
    public static MediaPlayer mediaPlayer;

    //TODO important variables


   //onCreate problem
   public static int duration=2;
    public static String thumbnailURL, title, author, audioUrl;

    int onetime=0;
    
    MotionLayout motionLayout2;

    ProgressBar progressBarPlayButton;

    LoaderDialog loaderDialog = new LoaderDialog(MediaPlayer_Activity.this);

   TextView speedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        Log.e(String.valueOf(MediaPlayer_Activity.class), "on create" );

        setContentView(R.layout.activity_main);
        next = findViewById(R.id.nextButtonView);
        prev = findViewById(R.id.previousButtonView);
        play = findViewById(R.id.playButtonView);
        forward = findViewById(R.id.forwardButtonView);
        rewind = findViewById(R.id.rewindButtonView);
        titleTextView = findViewById(R.id.titleSongs);
        textCurrentTime = findViewById(R.id.textLiveTime);
        textTotalDuration = findViewById(R.id.textTotalDuration);
        playerSeekBar = findViewById(R.id.seekBar);
        authorTextView = findViewById(R.id.AuthorTextView);
        songImageURL = findViewById(R.id.songImageMAINURL);
        motionLayout2 = findViewById(R.id.motionLayout_mediaPlayer);
        speedText = findViewById(R.id.oneXSpeed);


        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();



        Toast.makeText(this, "Preparing Media", Toast.LENGTH_SHORT).show();


        //mediaPlayer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                motionLayout2.transitionToEnd();
            }
        }, 0);









        Boolean musicBar;
                 Intent intentHome = getIntent();
                   musicBar = intentHome.getBooleanExtra("musicBar",false);


        //loading data from shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();


        if(musicBar){
                   loadSharedPrevFile();
               }else {
                   position = intentHome.getIntExtra("position", 0);
                   duration = intentHome.getIntExtra("duration", 200000);
                   thumbnailURL = intentHome.getStringExtra("imageUrl");
                   title = intentHome.getStringExtra("title");
                   author = intentHome.getStringExtra("author");
                   audioUrl = intentHome.getStringExtra("audioUrl");
               }


                 Intent prevPositionIntent = new Intent(this, Main.class);

                 prevPositionIntent.putExtra("prevPosition",position);


           currentTitle=title;


//TODO load previous position of the book

        loadSharedPrefData();


//TODO starting media
        mediaPlayer = new MediaPlayer();
        thread.start();








                        titleTextView.setText(title);

                        authorTextView.setText(author);

                        textTotalDuration.setText(milliSecondsToTimer(duration));

                        //image loading
                        Loading();


                        //create media session
                        MediaSession mediaSession = new MediaSession(MediaPlayer_Activity.this, "PlayerAudio");

                       // service binding
                        Intent intent = new Intent(MediaPlayer_Activity.this, MusicService.class);
                        bindService(intent, MediaPlayer_Activity.this, BIND_AUTO_CREATE);



                        //seekbar and other features
                        playerSeekBar.setMax(100);
                        playerSeekBar.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                SeekBar seekBar = (SeekBar) view;
                                int playPosition = (int) (duration / 100) * seekBar.getProgress();
                                mediaPlayer.seekTo(playPosition);
                                textCurrentTime.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
                                return false;
                            }
                        });



                        //starting service
                        Picasso.get().load(thumbnailURL).into(target);


                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nextClicked();
                            }
                        });
                        prev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                prevClicked();
                            }
                        });
                        play.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                playClicked();
                            }
                        });
//skip buttons
                        rewind.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rewindClicked();
                            }
                        });
                        forward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                forwardClicked();

                            }
                        });




        isPlaying = false;
        handler.removeCallbacks(updater);
        play.setImageResource(R.drawable.play_button_vector);
     //   playClicked();





    }
//end of onCreate




    public void speedboxOnPressed(View view){
        loaderDialog.startLoaderDialog(MediaPlayer_Activity.this);
    }
    
    public void oneXPressed(View view){


        float speed = 1f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }
        speedText.setText("1X");
        loaderDialog.dismissDialog();
    }

    public void onetwofiveXPressed(View view){
        float speed = 1.2f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }
        speedText.setText("1.25X");
        loaderDialog.dismissDialog();
    }

    public void onefiftyXPressed(View view){
        float speed = 1.5f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }

        speedText.setText("1.5X");
        loaderDialog.dismissDialog();
    }

    public void oneSevenFiveXPressed(View view){
        float speed = 1.75f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }

        speedText.setText("1.75X");
        loaderDialog.dismissDialog();
    }
    public void twoXPressed(View view){
        float speed = 2f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }

        speedText.setText("2.0X");
        loaderDialog.dismissDialog();
    }
    public void zeroEightyXPressed(View view){
        float speed = 0.8f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }
        speedText.setText("0.8X");
        loaderDialog.dismissDialog();
    }
    public void zeroSixtyXPressed(View view){
        float speed = 0.6f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }

        speedText.setText("0.6X");
        loaderDialog.dismissDialog();
    }

    public void zeroFortyXPressed(View view){
        float speed = 0.4f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }

        speedText.setText("0.4X");
        loaderDialog.dismissDialog();
    }
    public void zeroTwoXPressed(View view){
        float speed = 0.2f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
        }
        speedText.setText("0.2X");
        loaderDialog.dismissDialog();
    }




    



//TODO image from url and new show notification

    public Target target = new Target() {

        private Bitmap bitmap;
        private Picasso.LoadedFrom from;

        public Class<? extends Annotation> annotationType() {
            return null;
        }

        public ElementType[] value() {
            return new ElementType[0];
        }


        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            this.bitmap = bitmap;
            this.from = from;
            // Bitmap is loaded, use image here
            songImageURL.setImageBitmap(bitmap);
            int pausePlayButton = 0;
            if (isPlaying) {
                pausePlayButton = android.R.drawable.ic_media_pause;
            } else {
                pausePlayButton = android.R.drawable.ic_media_play;
            }
            showNotification(pausePlayButton, bitmap);
        }


        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

        public void onBitmapFailed() {
            // Fires if bitmap couldn't be loaded.
        }
    };


    //notifation function
    public void showNotification(int playPauseBtn, Bitmap imageBitmap) {

        //Todo ActionButtonIntent
        Intent startMusicService = new Intent(this, MusicService.class);
        this.startService(startMusicService);

        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 0, prevIntent,
                PendingIntent.FLAG_IMMUTABLE);
        Intent playIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent,
                PendingIntent.FLAG_IMMUTABLE);
        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent,
                PendingIntent.FLAG_IMMUTABLE);
        //Todo ButtonIntent
        Intent forwardIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_FORWARD);
        PendingIntent forwardPendingIntent = PendingIntent.getBroadcast(this, 0, forwardIntent,
                PendingIntent.FLAG_IMMUTABLE);
        Intent rewindIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_REWIND);
        PendingIntent rewindPendingIntent = PendingIntent.getBroadcast(this, 0, rewindIntent,
                PendingIntent.FLAG_IMMUTABLE);

        Intent resultIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_CONTINUE);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(this, 0, resultIntent,
                PendingIntent.FLAG_IMMUTABLE);




        Bitmap picture = imageBitmap;

           //notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(picture)
                .setOngoing(true)
                .setContentTitle(title)
                .setContentText(author)
                .setColor(getResources().getColor(R.color.neumorphism_background))
               // .addAction(android.R.drawable.arrow_down_float, "Previous", prevPendingIntent)
                .addAction(R.drawable.ic_baseline_replay_30_24, "Rewind", rewindPendingIntent)
                .addAction(playPauseBtn, "Play", playPendingIntent)
                .addAction(R.drawable.ic_baseline_forward_30_24, "forward", forwardPendingIntent)
         //       .addAction(android.R.drawable.ic_media_next, "Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle()
                                .setShowActionsInCompactView(0,1,2)
                )
                .setOnlyAlertOnce(true)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();



        notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {


            try {
                Log.d("DataSorce", audioUrl);
                try {
                    //MediaItem mediaItem = MediaItem.fromUri(audioUrl);
                    mediaPlayer.setDataSource(audioUrl);
                    Log.d("DataSorce", "no error");
                } catch (Exception e) {
                    Log.e("DataSorce", e.toString());
                }

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mediaPlayer.prepareAsync();
                // mediaPlayer.setWakeMode(this,);

                mediaPlayer.seekTo(songPrevPosition);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        mediaPlayer.start();
                        isPlaying = true;
                        //play_button.setText("Pause");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    });
    //Todo-preparing media
//    public void prepareMedia() {
//        isPlaying=true;
//        simpleExoPlayer = new SimpleExoPlayer.Builder(MediaPlayer_Activity.this).build();
//        simpleExoPlayer.setWakeMode(C.WAKE_MODE_NETWORK);
//        simpleExoPlayer.setHandleWakeLock(true);
//        MediaItem mediaItem = MediaItem.fromUri(audioUrl);
//        simpleExoPlayer.addMediaItem(mediaItem);
//        simpleExoPlayer.prepare();
//        simpleExoPlayer.seekTo(songPrevPosition);
//    }




    //TODO seekBar Functions
    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            textCurrentTime.setText(milliSecondsToTimer(currentDuration));

        }
    };



    private void updateSeekBar() {
        //      if(simpleExoPlayer.isPlaying()){
        playerSeekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / duration) * 100));
        handler.postDelayed(updater, 1000);
    }


    private String milliSecondsToTimer(long milliSeconds) {
        String timerString ;
        String secondsString;
        String minuteString;
        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours >= 1) {
            timerString = hours + ":";
        }else {
            timerString = "";
        }
        if (minutes < 10) {
            minuteString = "0" + minutes;
        } else {
            minuteString = "" + minutes;
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        timerString = timerString  + minuteString+ ":" + secondsString;
        return timerString;
    }

    public void Loading() {
        DownloadImage downloadImage = new DownloadImage();
        try {

            Bitmap bitmap = downloadImage.execute(thumbnailURL).get();
            songImageURL.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //get image from url
    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap
                    = null;
            URL url;
            HttpURLConnection httpURLConnection;
            InputStream in;
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                in = httpURLConnection.getErrorStream();
                bitmap = BitmapFactory.decodeStream(in);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }


    //methods of ActionPlaying
    @Override
    public void nextClicked() {



        int sTime = mediaPlayer.getCurrentPosition();
        int bTime = 360000; // time how much to skip

        if ((sTime + bTime) < duration) {
            sTime = sTime + bTime;
            mediaPlayer.seekTo(sTime);
        } else {
            Toast.makeText(getApplicationContext(), "Cannot jump forward 5 minute", Toast.LENGTH_SHORT).show();
        }
        updateSeekBar();
    }

    @Override
    public void prevClicked() {

        int sTime = mediaPlayer.getCurrentPosition();
        int bTime = 360000; // time how much to skip

        if ((sTime - bTime) > 0) {
            sTime = sTime - bTime;
            mediaPlayer.seekTo(sTime);
        } else {
            Toast.makeText(getApplicationContext(), "Cannot jump backward 5 minute", Toast.LENGTH_SHORT).show();
        }
        updateSeekBar();

    }

    @Override
    public void playClicked() {


        if (!isPlaying) {
            isPlaying = true;
            play.setImageResource(R.drawable.pause_button_vector);
           mediaPlayer.start();
           //simpleexoplayer.play(); this is for continue
            updateSeekBar();

        } else {
            isPlaying = false;

            handler.removeCallbacks(updater);
            play.setImageResource(R.drawable.play_button_vector);
            mediaPlayer.pause();
        }
        Picasso.get().load(thumbnailURL).into(target);

    }



    public void forwardClicked() {

        int sTime = mediaPlayer.getCurrentPosition();
        int bTime = 15000; // time how much to skip

        if ((sTime + bTime) < duration) {
            sTime = sTime + bTime;
            mediaPlayer.seekTo(sTime);
        } else {
            Toast.makeText(getApplicationContext(), "Cannot jump forward 15 seconds", Toast.LENGTH_SHORT).show();
        }
        updateSeekBar();

    }

    public void rewindClicked() {
        int sTime = mediaPlayer.getCurrentPosition();
        int bTime = 15000; // time how much to skip

        if ((sTime - bTime) > 0) {
            sTime = sTime - bTime;
            mediaPlayer.seekTo(sTime);
        } else {
            Toast.makeText(getApplicationContext(), "Cannot jump backward 15 seconds", Toast.LENGTH_SHORT).show();
        }
        updateSeekBar();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;

        musicService = binder.getService();
        musicService.setCallBack(MediaPlayer_Activity.this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }


    //method for sharedPreferences data

    public void saveData(){

        editor.putInt(currentTitle, (int)mediaPlayer.getCurrentPosition());
        editor.apply();
        editor.putString("prevThumbnailUrl", thumbnailURL);
        editor.apply();
        Log.e(String.valueOf(MediaPlayer_Activity.class), "**** "+thumbnailURL );
        editor.putString("prevTitle", title );
        editor.apply();
        Log.e(String.valueOf(MediaPlayer_Activity.class), "**** "+title );
        editor.putString("prevAuthor", author);
        editor.apply();
        Log.e(String.valueOf(MediaPlayer_Activity.class), "**** "+author );
        editor.putString("prevAudioUrl", audioUrl);
        editor.apply();
        Log.e(String.valueOf(MediaPlayer_Activity.class), "**** "+audioUrl);
        editor.putInt("prevDuration",duration);
        editor.apply();
        Log.e(String.valueOf(MediaPlayer_Activity.class), "**** "+duration);
        editor.putInt("prevPosition", position);
        editor.apply();
    }

    public void loadSharedPrefData() {
            songPrevPosition = sharedPreferences.getInt(currentTitle, 0);
    }


    public void loadSharedPrevFile() {
       thumbnailURL=sharedPreferences.getString("prevThumbnailUrl", "https://i1.sndcdn.com/artworks-000112946526-z3lvil-t500x500.jpg");
        title = sharedPreferences.getString("prevTitle", "See You Again");
        author = sharedPreferences.getString("prevAuthor", "Wiz Khalifa & Charlie Puth");
        audioUrl = sharedPreferences.getString("prevAudioUrl", "https://mp3.filmisongs.com/See%20You%20Again%20Mp3%20By%20Charlie%20Puth%20and%20Wiz%20Khalifa.mp3");
        duration = sharedPreferences.getInt("prevDuration", 237000);
        position = sharedPreferences.getInt("prevPosition", 1);
    }

    @Override
    public void onBackPressed() {
        saveData();
        motionLayout2.transitionToStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openHome = new Intent(MediaPlayer_Activity.this, Main.class);
                openHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openHome, 0);
            }
        }, 300);

        Picasso.get().load(thumbnailURL).into(target);
    }


    public void onBackButtonPressed(View view){
        saveData();
        motionLayout2.transitionToStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openHome = new Intent(MediaPlayer_Activity.this, Main.class);
                openHome.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(openHome, 0);
            }
        }, 300);

        Picasso.get().load(thumbnailURL).into(target);

    }

    @Override
    protected void onStop() {
            super.onStop();
            saveData();
        Log.e(String.valueOf(MediaPlayer_Activity.class), "on stop" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
        Log.e(String.valueOf(MediaPlayer_Activity.class), "on destroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        motionLayout2.transitionToEnd();
    }



    private void showUpdateDialog() {
        Dialog dialog;


    }
}
