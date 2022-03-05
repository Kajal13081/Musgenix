package com.example.musicplayer_ui;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayer_ui.model.Songs;
import com.gauravk.audiovisualizer.visualizer.BlastVisualizer;
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.widget.Toast;

public class MusicPlayerActivity extends AppCompatActivity {


    Button pausePlay;
    ImageButton nextBtn, previousBtn, btnff, btnfr;
    TextView titleTv, currentTimeTv, totalTimeTv;
    SeekBar seekBar;
    BlastVisualizer audioVisualizer;
    ImageView musicIcon,loop,loop_change;

    ImageView mHeartIcon;

    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer = null;
    int songPosition = 0 ;
    int x=10;
    int count_loop = 0;
    Intent i = getIntent();
    public static  ArrayList<Songs> musicListPA;
    public static Boolean isPlaying = false;
    public static Runnable runnable;
    private Handler mHandler = new Handler();
    public static byte[] SongIcon;
    private int RECORD_PERMS = 993;
// overriding method for making menu items to respond
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // finding item view
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
// finding all layouts
        titleTv = findViewById(R.id.txtsn);
        currentTimeTv = findViewById(R.id.txtsstart);
        totalTimeTv = findViewById(R.id.txtsstop);
        seekBar = findViewById(R.id.seekbar);
        pausePlay = findViewById(R.id.playbtn);
        nextBtn = findViewById(R.id.btnnext);
        previousBtn = findViewById(R.id.btnprev);
        musicIcon = findViewById(R.id.imageview);
        audioVisualizer = findViewById(R.id.audio_visualizer);
        btnff = findViewById(R.id.btnff);
        btnfr = findViewById(R.id.btnfr);
        loop = findViewById(R.id.loop);
        loop_change = findViewById(R.id.loop_repeat);

        titleTv.setSelected(true);
        Intent i = getIntent();

        //Initialize Layout
        songPosition = i.getIntExtra("index", 0);

        Log.e(TAG, "AAAAAAAAAAAAAAAA" + songPosition);
        // fetching data from intent
        switch (i.getStringExtra("class")) {
            case "SongsAdapter": {
                musicListPA = new ArrayList();
                musicListPA.addAll(new RecyclerViewFragment().modifyList);
                SongIcon = i.getByteArrayExtra("SongIcon");
                setLayout();
                createMediaPlayer();


            }

        }

//loop code for playerview
        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    if (loop.isPressed()){
                        count_loop+=1;
                        if (count_loop%2==1){
                            mediaPlayer.setLooping(true);
                            Toast.makeText(getApplicationContext(), "Loop is On", Toast.LENGTH_SHORT).show();
                            loop_change.setVisibility(View.VISIBLE);
                            loop.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please play the music first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loop_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    if (loop_change.isPressed()){
                        count_loop+=1;
                        if (count_loop%2==0){
                            mediaPlayer.setLooping(false);
                            Toast.makeText(getApplicationContext(), "Loop is Off", Toast.LENGTH_SHORT).show();
                            loop_change.setVisibility(View.INVISIBLE);
                            loop.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please play the music first", Toast.LENGTH_SHORT).show();
                }
            }
        });
//ends here



// setting click listener on pause button

        pausePlay.setOnClickListener(v ->{
            if (isPlaying){
                pauseMusic();
            }else {
                playMusic();
            }
        });
        // setting click listener on previous button
        previousBtn.setOnClickListener(v ->{
            prevNextSong(false);
        });
        // setting click listener on next button button
        nextBtn.setOnClickListener(v ->{
            prevNextSong(true);
        });
        // setting click listener on buttons
        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                    // showing a snackbar
                    Snackbar.make(v, "Skipped 10 seconds", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        btnfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                    Snackbar.make(v, "Reversed 10 seconds", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
// using another thread for playing music
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {

            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if(mediaPlayer != null){
                    currentTimeTv.setText(getDuration(mediaPlayer.getCurrentPosition())+"");
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());

                    if(mediaPlayer.isPlaying()){
                        musicIcon.setRotation(x++);
                    }else{
                        musicIcon.setRotation(0);
                    }

                }
                /* Causes the Runnable r to be added to the message queue,
                 to be run after the specified amount of time elapses.
                  The runnable will be run on the thread to which this handler is attached.
                  The time-base is SystemClock.uptimeMillis. Time spent in deep sleep will add an additional delay to execution*/
                mHandler.postDelayed(this, 1000);
            }
        });
// getting drawable used to show seekbar
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        // returning drawable to represent scroll thumb i.e we can scroll back and forth on seekbar
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
// setting seek var change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    if (mediaPlayer!=null)mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
// performing action on completion of a song
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextBtn.performClick();
            }
        });
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED)
        {
            audioVisualizer.setAudioSessionId(mediaPlayer.getAudioSessionId());
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},RECORD_PERMS);
        }
    }


// setting layout of music icon
    private void setLayout(){
        Glide.with(this)
                .load(musicListPA.get(songPosition).getPath())
                .apply(RequestOptions.placeholderOf(R.drawable.music).centerCrop())
                .into(musicIcon);
        titleTv.setText(musicListPA.get(songPosition).getName());
    }
// initialize a media player
    private void createMediaPlayer(){

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicListPA.get(songPosition).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
            pausePlay.setBackgroundResource(R.drawable.ic_baseline_pause_circle_24);
            currentTimeTv.setText(getDuration(mediaPlayer.getCurrentPosition()));
            totalTimeTv.setText(getDuration(mediaPlayer.getDuration()));
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

// methods for performing various actions on buttons clicked
    private void playMusic(){
        pausePlay.setBackgroundResource(R.drawable.ic_baseline_pause_circle_24);
        isPlaying = true;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.start();
        pausePlay.animate().rotationX(-10).setDuration(500);

    }

    private void pauseMusic(){
        pausePlay.setBackgroundResource(R.drawable.ic_baseline_play_circle_24);
        isPlaying = false;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.pause();
        pausePlay.animate().rotationX(10).setDuration(500);

    }

    public void prevNextSong(Boolean increment){
        if (increment){
            setSongPosition(true);
            setLayout();
            createMediaPlayer();
            startAnimation(musicIcon);
        }else {
            setSongPosition(false);
            setLayout();
            createMediaPlayer();
            startAnimation(musicIcon);
        }
    }

    private void setSongPosition(Boolean increment){
        if (increment){
            if (musicListPA.size() -1 == songPosition)
                songPosition = 0;
            else ++songPosition;
        }
        else {
            if (0 == songPosition)
                songPosition = musicListPA.size() -1;
            else --songPosition;
        }
    }

    @SuppressLint("DefaultLocale")
    // getting duration of song being played
    private String getDuration(int totalDuration){
        String totalDurationText;
        int hrs = totalDuration/(1000*60*60);
        int min = (totalDuration%(1000*60*60))/(1000*60);
        int secs = (((totalDuration%(1000*60*60))%(1000*60*60))%(1000*60))/1000;

        if (hrs<1){ totalDurationText = String.format("%02d:%02d", min, secs); }
        else{
            totalDurationText = String.format("%1d:%02d:%02d", hrs, min, secs);
        }
        return  totalDurationText;
    }
// method for animation
    public void startAnimation(View view)
    {
        // constructing an object animator
        ObjectAnimator animator = ObjectAnimator.ofFloat(musicIcon, "rotation", 0f,360f);
        // setting duration of animation
        animator.setDuration(1000);

        ObjectAnimator test2 = ObjectAnimator.ofFloat(view, "rotation", 0f, x++);
        animator.setDuration(500);
        // creating animator set object for playing multiple animations in desired way
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator, test2);
        animatorSet.start();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));


    }
// method for showing song icon
        private byte[] getAlbumArt(String uri){
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            byte[] art = retriever.getEmbeddedPicture();
            retriever.release();
            return art;
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && requestCode==RECORD_PERMS)
        {
            audioVisualizer.setAudioSessionId(mediaPlayer.getAudioSessionId());
        }
        else
        {
            Toast.makeText(this, "Pls give permissions :__:", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(audioVisualizer!=null)
        {
            audioVisualizer.release();
        }
    }
}
