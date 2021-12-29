package com.example.musicplayer_ui.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
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

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Songs> songs;

    // constructor
    public SongsAdapter(List<Songs> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
            viewHolder.albumArtHolder.setImageURI(albumArtUri);

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
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    // custom viewHolder
    public static class SongViewHolder extends RecyclerView.ViewHolder{

        // member variables
        RelativeLayout musicItemLayout;
        ImageView albumArtHolder;
        TextView titleHolder, durationHolder;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            musicItemLayout = itemView.findViewById(R.id.musicItemLayout);
            albumArtHolder = itemView.findViewById(R.id.music_icon);
            titleHolder = itemView.findViewById(R.id.music_name);
            durationHolder = itemView.findViewById(R.id.music_duration);
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
