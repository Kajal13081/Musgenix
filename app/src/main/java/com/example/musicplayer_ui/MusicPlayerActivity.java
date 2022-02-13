package com.example.musicplayer_ui;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayer_ui.model.Songs;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.widget.Toast;

public class MusicPlayerActivity extends AppCompatActivity {


    Button pausePlay, nextBtn, previousBtn, btnff, btnfr;
    TextView titleTv, currentTimeTv, totalTimeTv;
    SeekBar seekBar;
    ImageView musicIcon;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer = null;
    int songPosition = 0 ;
    int x=10;
    int count_loop = 0; //for counting the no of times loop button is pressed
    Intent i = getIntent();
    public static  ArrayList<Songs> musicListPA;
    public static Boolean isPlaying = false;
    public static Runnable runnable;
    private Handler mHandler = new Handler();
    public static byte[] SongIcon;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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

        titleTv = findViewById(R.id.txtsn);
        currentTimeTv = findViewById(R.id.txtsstart);
        totalTimeTv = findViewById(R.id.txtsstop);
        seekBar = findViewById(R.id.seekbar);
        pausePlay = findViewById(R.id.playbtn);
        nextBtn = findViewById(R.id.btnnext);
        previousBtn = findViewById(R.id.btnprev);
        musicIcon = findViewById(R.id.imageview);
        // mVisualizer = findViewById(R.id.blast);
        btnff = findViewById(R.id.btnff);
        btnfr = findViewById(R.id.btnfr);
        ImageView loop = findViewById(R.id.loop);

        titleTv.setSelected(true);
        Intent i = getIntent();

        //Initialize Layout
        songPosition = i.getIntExtra("index", 0);

        Log.e(TAG, "AAAAAAAAAAAAAAAA" + songPosition);
        switch (i.getStringExtra("class")) {
            case "SongsAdapter": {
                musicListPA = new ArrayList();
                musicListPA.addAll(new RecyclerViewFragment().modifyList);
                SongIcon = i.getByteArrayExtra("SongIcon");
                setLayout();
                createMediaPlayer();


            }

        }


        pausePlay.setOnClickListener(v ->{
            if (isPlaying){
                pauseMusic();
            }else {
                playMusic();
            }
        });
        previousBtn.setOnClickListener(v ->{
            prevNextSong(false);
        });
        nextBtn.setOnClickListener(v ->{
            prevNextSong(true);
        });
        //for looping the music
        loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    if (loop.isPressed()){
                        count_loop+=1;
                        if (count_loop%2==1){
                            mediaPlayer.setLooping(true);
                            Toast.makeText(getApplicationContext(), "Loop is On", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            mediaPlayer.setLooping(false);
                            Toast.makeText(getApplicationContext(), "Loop is Off", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please play the music first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
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
                mHandler.postDelayed(this, 1000);
            }
        });

        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

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

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextBtn.performClick();
            }
        });

    }



    private void setLayout(){
        Glide.with(this)
                .load(musicListPA.get(songPosition).getPath())
                .apply(RequestOptions.placeholderOf(R.drawable.music).centerCrop())
                .into(musicIcon);
        titleTv.setText(musicListPA.get(songPosition).getName());
    }

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

    public void startAnimation(View view)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(musicIcon, "rotation", 0f,360f);
        animator.setDuration(1000);

        ObjectAnimator test2 = ObjectAnimator.ofFloat(view, "rotation", 0f, x++);
        animator.setDuration(500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator, test2);
        animatorSet.start();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));


    }

        private byte[] getAlbumArt(String uri){
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            byte[] art = retriever.getEmbeddedPicture();
            retriever.release();
            return art;
        }
}
