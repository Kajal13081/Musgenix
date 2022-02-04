package com.example.musicplayer_ui.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer_ui.R;
import com.example.musicplayer_ui.model.Songs;

import java.io.File;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Songs> songs;

    // constructor
    public SongsAdapter(List<Songs> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.music_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // current song and viewHolder
        Songs song = songs.get(position);
        SongViewHolder viewHolder = (SongViewHolder) holder;

        //set values to views
        viewHolder.titleHolder.setText((song.getName()));
        viewHolder.durationHolder.setText(getDuration(song.getDuration()));

        //album art
        Uri albumArtUri = song.getAlbumArtUri();
        if (albumArtUri != null){
            viewHolder.albumArtHolder.setImageURI(null);
            viewHolder.albumArtHolder.setImageURI(null);

            if (viewHolder.albumArtHolder.getDrawable() == null){   // if that album has nothing, then we use the default album
                viewHolder.albumArtHolder.setImageResource(R.drawable.ic_music);
            }
        }
        else {
            viewHolder.albumArtHolder.setImageResource(R.drawable.ic_music);
        }

        // onClick listener on recyclerView
        viewHolder.musicItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), " Song Selected: " + song.getName(), Toast.LENGTH_LONG).show();

            }
        });

        // OnClickListener for the settings button
        viewHolder.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if permissions for writing settings have been granted, otherwise ask for permissions
                if(!Settings.System.canWrite(view.getContext()))
                {
                    Intent intent=new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:"+view.getContext().getPackageName()));
                    view.getContext().startActivity(intent);
                }
                // Display dialog box with options for setting song as ringtone or alarm tone
                new AlertDialog.Builder(view.getContext()).setTitle("Set song as...").setItems(new String[]{"Ringtone", "Alarm tone"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Check if permissions have been granted
                        if(!Settings.System.canWrite(view.getContext()))
                        {
                            Toast.makeText(view.getContext(), "Permissions not granted", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        switch (i)
                        {
                            // Setting song as ringtone
                            case 0:
                                RingtoneManager.setActualDefaultRingtoneUri(view.getContext(), RingtoneManager.TYPE_RINGTONE,song.getUri());
                                Toast.makeText(view.getContext(), "Song set as ringtone", Toast.LENGTH_SHORT).show();
                                break;

                            // Setting song as alarm tone
                            case 1:
                                RingtoneManager.setActualDefaultRingtoneUri(view.getContext(), RingtoneManager.TYPE_ALARM,song.getUri());
                                Settings.System.putString(view.getContext().getContentResolver(),Settings.System.ALARM_ALERT,song.getUri().toString());
                                Toast.makeText(view.getContext(), "Song set as alarm tone", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    // custom viewHolder
    public static class SongViewHolder extends RecyclerView.ViewHolder{

        // member variables
        RelativeLayout musicItemLayout;
        ImageView albumArtHolder,settingsButton;
        TextView titleHolder, durationHolder;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            musicItemLayout = itemView.findViewById(R.id.musicItemLayout);
            albumArtHolder = itemView.findViewById(R.id.music_icon);
            titleHolder = itemView.findViewById(R.id.music_name);
            durationHolder = itemView.findViewById(R.id.music_duration);
            settingsButton = itemView.findViewById(R.id.settings_img);
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

}
