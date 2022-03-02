package com.example.musicplayer_ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer_ui.AlbumsFragment;
import com.example.musicplayer_ui.AlbumsSubContentsFragment;
import com.example.musicplayer_ui.R;
import com.example.musicplayer_ui.model.Songs;

import java.util.ArrayList;
import java.util.List;

public class AlbumsTitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG="AlbumsTitleAdapter";
    Context context;
    private List<String> titleAlbums=new ArrayList<>();
    private List<Songs> albumSongs=new ArrayList<>();
    private List<Songs> albumsSubContents=new ArrayList<>();
    public AlbumsTitleAdapter(Context context, List<String> titleAlbums, List<Songs> albumsSongs){
        this.context=context;
        this.titleAlbums=titleAlbums;
        this.albumSongs=albumsSongs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating layout to be shown in recycler view
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.album_rec_view,parent,false);
        return new AlbumsTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // giving data to various components of layout to be displayed
        AlbumsTitleViewHolder albumsTitleViewHolder=(AlbumsTitleViewHolder) holder;
        albumsTitleViewHolder.mTextView.setText(titleAlbums.get(position));
        albumsTitleViewHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1,str2;
                int lastOcc;
                String str=albumsTitleViewHolder.mTextView.getText().toString();
                for(int i=0;i<albumSongs.size();i++){
                    lastOcc=albumSongs.get(i).getPath().lastIndexOf("/");
                    str1=albumSongs.get(i).getPath().substring(0,lastOcc);
                    str2=str1.substring(str1.lastIndexOf("/")+1,str1.length());
                    if(str2.equals(str)){
                        albumsSubContents.add(albumSongs.get(i));

                    }
                }
//                for(int i=0;i<albumsSubContents.size();i++){
//                    Log.d(TAG,"got songs _______"+albumsSubContents.get(i).getName());
//                }
                // launching another fragment from existing one
                FragmentManager fm=((AppCompatActivity)context).getSupportFragmentManager();

                Fragment fragment= new AlbumsSubContentsFragment(v.getContext(), albumsSubContents);
                fm.beginTransaction().add(R.id.fragment_holder_for_albumstab,fragment)
                        .addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return titleAlbums.size();
    }
    public static class AlbumsTitleViewHolder extends RecyclerView.ViewHolder{
        // finding all different components of layout to be inflated
        TextView mTextView;
        ImageView mImageView;
        RelativeLayout mRelativeLayout;
        public AlbumsTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView=(TextView) itemView.findViewById(R.id.albums_title);
            mImageView=(ImageView) itemView.findViewById(R.id.arrow_albums);
            mRelativeLayout=(RelativeLayout) itemView.findViewById(R.id.albums_title_layout);

        }

    }



}
