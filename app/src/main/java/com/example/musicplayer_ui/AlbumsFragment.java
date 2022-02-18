package com.example.musicplayer_ui;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer_ui.adapter.AlbumsTitleAdapter;
import com.example.musicplayer_ui.model.Songs;

import java.util.ArrayList;
import java.util.List;

public class AlbumsFragment extends Fragment {
    private static final String TAG="AlbumsFragment";
    private RecyclerView mRecyclerView;
    public static RecyclerView recyclerView;
    private List<Songs> songsForAlbums=new ArrayList<>();
    private List<String> albumsTitle=new ArrayList<>();
    // method for creating fragment from outside class
    public static AlbumsFragment newInstance(){
        return new AlbumsFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG,"inside on create of albums fragment");
        fetchSongsForAlbums();
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        // inflating view of fragment
        View view=inflater.inflate(R.layout.fragment_holder_for_albumstab,container,false);
        Log.d(TAG,"inside on create view of albums fragment");
        // finding recycler view associated with view
        mRecyclerView=(RecyclerView) view.findViewById(R.id.rec_view_albums);
        recyclerView=mRecyclerView;
        // setting layout manager and adapter to recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AlbumsTitleAdapter albumsAdapter=new AlbumsTitleAdapter(getContext(),albumsTitle,songsForAlbums);
        mRecyclerView.setAdapter(albumsAdapter);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        // inflating menu bar
        inflater.inflate(R.menu.fragment_menu,menu);
        MenuItem item=menu.findItem(R.id.three_dots);

    }
    public void fetchSongsForAlbums(){
// fetching songs again for albums fragment
        Uri songLibraryUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            songLibraryUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }else {
            songLibraryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME, // song name
                MediaStore.Audio.Media.DURATION, // song duration
                MediaStore.Audio.Media.ALBUM_ID, // song image
                MediaStore.Audio.Media.DATA,
        };
        String sortOrderOfSongsLists = MediaStore.Audio.Media.DATE_ADDED + " DESC ";
        try(Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(songLibraryUri, projection, null, null, sortOrderOfSongsLists)) {

            //cache the cursor indices
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);  // index of the cursor
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
            // taking path index
            int path=cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            //getting the values from cursor indices
            while (cursor.moveToNext()) {
                //get values of columns for a give audio file
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                long albumId = cursor.getLong(albumIdColumn);
                // storing path to string
                String songPath=cursor.getString(path);
                //song uri
                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                //album art uri
                Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumArt"), albumId);

                //remove .mp3 extension from song name
                name = name.substring(0, name.lastIndexOf("."));

                // get songs item from model class
                Songs song = new Songs(id, uri, name, duration, albumId, albumArtUri,songPath);
                // add songs to song Arraylist
                songsForAlbums.add(song);
            }
            // checking out songs received
//            for(int i=0;i<songsForAlbums.size();i++){
//                Log.d(TAG,"!!!!!! "+songsForAlbums.get(i).getPath());
//            }
            segregateSongs(songsForAlbums);


        }
    }
    public void segregateSongs(List<Songs> segSongs){
        // grouping songs list on basis of different locations i.e downloads, whats app, shared etc.
        String str,str1;
        int last;
        for(int i=0;i<segSongs.size();i++){
            last=segSongs.get(i).getPath().lastIndexOf("/");
            str=segSongs.get(i).getPath().substring(0,last);
            str1=str.substring(str.lastIndexOf("/")+1,str.length());
            if(!albumsTitle.contains(str1))
                albumsTitle.add(str1);
            Log.d(TAG,"@@@@@@@@@@@"+str);
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"inside on start");
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.d(TAG,"inside on attach");
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"inside on stop");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"inside on resume");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"iNSIDE on destroy");
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d(TAG,"iNSIDE on destroy view");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"inside on pAUSE");
    }
    @Override
    public void onDetach(){
        super.onDetach();
        Log.d(TAG,"iNSIDE on detach");
    }
}
