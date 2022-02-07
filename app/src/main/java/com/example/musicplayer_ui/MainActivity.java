package com.example.musicplayer_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import android.util.Log;
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
    private static final String TAG="Main Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        recyclerView = findViewById(R.id.recyclerView);
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().setReorderingAllowed(true)
                    .add(R.id.fragment_holder, RecyclerViewFragment.class, null)
                    .addToBackStack(null)
                    .commit();
        }

        //storagePermission();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"inside in destroy of main");
    }
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

