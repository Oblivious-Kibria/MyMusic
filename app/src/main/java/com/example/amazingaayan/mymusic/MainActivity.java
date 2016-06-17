package com.example.amazingaayan.mymusic;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView songsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songsList = (ListView) findViewById(R.id.songsList);
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());

        String[] songItem = new String[mySongs.size()];

        for(int i=0;i<mySongs.size();i++){
            songItem[i]=mySongs.get(i).getName().toString().replace(".mp3", "");
        }



        ArrayAdapter<String> songList_ArrayAdapter = new SongListAdapter(getApplicationContext(), songItem);
        songsList.setAdapter(songList_ArrayAdapter);

        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos",position).putExtra("songList",mySongs));
            }
        });
    }

    private ArrayList<File> findSongs(File root) {
        ArrayList<File> collectedSongs = new ArrayList<File>();
        File[] files = root.listFiles();

        for(File singleFile:files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                collectedSongs.addAll(findSongs(singleFile));
            }
            else {
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".MP3")){
                    collectedSongs.add(singleFile);
                }
                if(singleFile.getName().endsWith(".acc") || singleFile.getName().endsWith(".ACC")){
                    collectedSongs.add(singleFile);
                }
            }
        }

        return collectedSongs;
    }


}