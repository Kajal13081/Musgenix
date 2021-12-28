package com.example.musicplayer_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        storagePermission();
    }

    public void storagePermission(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // runtime permission given to store songs from external storage to app
                        displaySongs();
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
    public ArrayList<File> fetchMusic(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] music = file.listFiles();
        if(music!=null){
            for(File myFile: music ){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arrayList.addAll(fetchMusic(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3")){
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;
    }

    public void displaySongs(){
        ArrayList<File> myMusic = fetchMusic(Environment.getExternalStorageDirectory());
        items = new String[myMusic.size()];
        for(int i=0; i< myMusic.size();i++){
            items[i] = myMusic.get(i).getName().replace(".mp3", " ");
        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, items );
//        listView.setAdapter(adapter);

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") View view = getLayoutInflater().inflate(R.layout.music_item, null);
            TextView musicText = view.findViewById(R.id.music_name);
            musicText.setSelected(true);
            musicText.setText(items[position]);
            return view;
        }
    }
}
