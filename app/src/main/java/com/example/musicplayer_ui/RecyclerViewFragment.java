package com.example.musicplayer_ui;

import android.Manifest;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer_ui.adapter.SongsAdapter;
import com.example.musicplayer_ui.model.SearchFragment;
import com.example.musicplayer_ui.model.Songs;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment {
    RecyclerView mRecyclerView;
    SongsAdapter mSongsAdapter;
    ArrayList<String> sendSongs=new ArrayList<>();
    private static final String TAG="RecyclerViewFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }
@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    View view=inflater.inflate(R.layout.list_of_files,container,false);
    Log.d(TAG,"inside on create view");
     mRecyclerView=(RecyclerView) view.findViewById(R.id.music_list_recView);
    //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    storagePermission();
    return view;
}
    public void storagePermission(){
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // runtime permission given to store songs from external storage to app
//                        displaySongs();
                        fetchSongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                })
                .check();
    }
    private void fetchSongs(){
        // define list to carry the songs
        List<Songs> songs = new ArrayList<>();
        Uri songLibraryUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            songLibraryUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }else {
            songLibraryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        // projection : what do i need to select from media store, because media store is a collection of database/tables which have media items in user's device
        // basically what i need from user's device
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME, // song name
                MediaStore.Audio.Media.DURATION, // song duration
                MediaStore.Audio.Media.ALBUM_ID, // song image
        };

        // sort order of songs
        String sortOrderOfSongsLists = MediaStore.Audio.Media.DATE_ADDED + " DESC ";  // sorting date wise in descending order

        //Querying
        try(Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(songLibraryUri, projection, null, null, sortOrderOfSongsLists)) {

            //cache the cursor indices
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);  // index of the cursor
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

            //getting the values from cursor indices
            while (cursor.moveToNext()) {
                //get values of columns for a give audio file
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                long albumId = cursor.getLong(albumIdColumn);

                //song uri
                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                //album art uri
                Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumArt"), albumId);

                //remove .mp3 extension from song name
                name = name.substring(0, name.lastIndexOf("."));

                // get songs item from model class
                Songs song = new Songs(id, uri, name, duration, albumId, albumArtUri);
                // add songs to song Arraylist
                songs.add(song);
            }

            // display songs in recyclerView
            displaySongs(songs);
            for(int i=0;i<songs.size();i++)
                sendSongs.add(songs.get(i).getName());
            // Number of songs available
            for(int i=0;i<songs.size();i++){
                Log.d(TAG,"!!!!!! "+songs.get(i).getName()+"  ??"+songs.get(i).getAlbumArtUri());
            }
            Toast.makeText(getContext(), "Number Of Songs " +songs.size(), Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
//        inflater.inflate(R.menu.fragment_menu,menu);
//        MenuItem item=menu.findItem(R.id.search_action);
//        View view= MenuItemCompat.getActionView((item));


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        return true;
    }

    private void displaySongs(List<Songs> songs) {
        // layout manager
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // adapter
        mSongsAdapter = new SongsAdapter(songs);
        mRecyclerView.setAdapter(mSongsAdapter);

    }
    @Override
    public void onStop(){
        super.onStop();
        getActivity().finishAffinity();
    }
    @Override
    public void onDestroyView(){
    super.onDestroyView();
        //getActivity().finishAffinity();
    }
}
