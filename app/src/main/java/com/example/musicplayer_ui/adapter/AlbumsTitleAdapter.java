package com.example.musicplayer_ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer_ui.R;

public class AlbumsTitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    public AlbumsTitleAdapter(Context context){
        this.context=context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.album_rec_view,parent,false);
        return new AlbumsTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AlbumsTitleViewHolder albumsTitleViewHolder=(AlbumsTitleViewHolder) holder;
        albumsTitleViewHolder.mTextView.setText("helo");
    }

    @Override
    public int getItemCount() {
        return 5;
    }
    public static class AlbumsTitleViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        public AlbumsTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView=(TextView) itemView.findViewById(R.id.albums_title);
        }
    }

}
