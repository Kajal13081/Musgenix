package com.example.musicplayer_ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer_ui.adapter.AlbumsTitleAdapter;
import com.example.musicplayer_ui.adapter.SongsAdapter;
import com.example.musicplayer_ui.model.Songs;

import java.util.ArrayList;
import java.util.List;

public class AlbumsSubContentsFragment extends Fragment {
    private static final String TAG="AlbumsSubContentsFragment";
    Context context;
    RecyclerView mRecyclerView;
    private List<Songs> albumsSongsSubContents=new ArrayList<>();
    public AlbumsSubContentsFragment(Context context, List<Songs>albumsSongsSubContents){
        this.context=context;
        this.albumsSongsSubContents=albumsSongsSubContents;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AlbumsFragment.recyclerView.setVisibility(View.INVISIBLE);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.albums_subcontents_fragment,container,false);
        mRecyclerView=(RecyclerView) v.findViewById(R.id.rec_view_albums_subcontents);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SongsAdapter songsAdapter=new SongsAdapter(getContext(),albumsSongsSubContents);
        mRecyclerView.setAdapter(songsAdapter);
        return v;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        AlbumsFragment.recyclerView.setVisibility(View.VISIBLE);
        albumsSongsSubContents.clear();
    }
}
