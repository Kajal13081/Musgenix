package com.example.musicplayer_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer_ui.adapter.SongsAdapter;
import com.example.musicplayer_ui.model.Songs;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SongsAdapter songsAdapter;
//    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        storagePermission();
    }

    public void storagePermission(){
        Dexter.withContext(this)
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
    // returns mp3 file from a folder
//    public ArrayList<File> fetchMusic(File file) {
//        ArrayList<File> arrayList = new ArrayList<>();
//        File[] music = file.listFiles();
//        if(music!=null){
//            for(File myFile: music ){
//                if(!myFile.isHidden() && myFile.isDirectory()){
//                    arrayList.addAll(fetchMusic(myFile));
//                }
//                else{
//                    if(myFile.getName().endsWith(".mp3")){
//                        arrayList.add(myFile);
//                    }
//                }
//            }
//        }
//        return arrayList;
//    }

//    public void displaySongs(){
//        ArrayList<File> myMusic = fetchMusic(Environment.getExternalStorageDirectory());
//        items = new String[myMusic.size()];
//        for(int i=0; i< myMusic.size();i++){
//            items[i] = myMusic.get(i).getName().replace(".mp3", " ");
//        }
////        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, items );
////        listView.setAdapter(adapter);
//
//        CustomAdapter customAdapter = new CustomAdapter();
//        listView.setAdapter(customAdapter);
//    }
//
//    class CustomAdapter extends BaseAdapter{
//        @Override
//        public int getCount() {
//            return items.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            @SuppressLint("ViewHolder") View view = getLayoutInflater().inflate(R.layout.music_item, null);
//            TextView musicText = view.findViewById(R.id.music_name);
//            musicText.setSelected(true);
//            musicText.setText(items[position]);
//            return view;
//        }
//    }

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
        try(Cursor cursor = getContentResolver().query(songLibraryUri, projection, null, null, sortOrderOfSongsLists)) {

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
            // Number of songs available
            Toast.makeText(getApplicationContext(), "Number Of Songs " +songs.size(), Toast.LENGTH_SHORT).show();
        }

    }

    private void displaySongs(List<Songs> songs) {
        // layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // adapter
        songsAdapter = new SongsAdapter(songs);
        recyclerView.setAdapter(songsAdapter);
    }
}
