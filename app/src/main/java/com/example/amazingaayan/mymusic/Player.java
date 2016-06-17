package com.example.amazingaayan.mymusic;

/**
 * Created by Amazing Aayan on 17-Jun-16.
 */

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Player extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    private static MediaPlayer mp;
    ArrayList<File> mySongs;
    int position;
    private Handler mHandler = new Handler();
    SeekBar seekBar;
    Uri uri;
    ImageButton nextSong, previousSong, moveFordward, moveBackward, pause, shuffle, repeat, playListButton;
    TextView playedTime, total_Duration, songTitle;
    private static boolean isShuffle = false;
    private static boolean isRepeat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        nextSong = (ImageButton) findViewById(R.id.btnNext);
        previousSong = (ImageButton) findViewById(R.id.btnPrevious);
        moveBackward = (ImageButton) findViewById(R.id.btnBackward);
        moveFordward = (ImageButton) findViewById(R.id.btnForward);
        pause = (ImageButton) findViewById(R.id.btnPlay);
        shuffle = (ImageButton) findViewById(R.id.btnShuffle);
        repeat = (ImageButton) findViewById(R.id.btnRepeat);
        playListButton = (ImageButton) findViewById(R.id.btnPlaylist);

        total_Duration = (TextView) findViewById(R.id.songTotalDurationLabel);
        playedTime = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTitle = (TextView) findViewById(R.id.songTitle);

        seekBar = (SeekBar) findViewById(R.id.songProgressBar);

        if (mp != null) {
            mp.stop();
            mp.release();
        }

        Intent playerIntent = getIntent();
        Bundle bundle = playerIntent.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songList");
        position = bundle.getInt("pos", 0);

        uri = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), uri);
        mp.start();
        songTitle.setText(mySongs.get(position).getName().toString());
        int Duration = mp.getDuration();
        String total_1Duration = Integer.toString(Duration).toString();
        seekBar.setMax(Duration);
        total_Duration.setText(total_1Duration);
        updateProgressBar();
        mp.setOnCompletionListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

        pause.setOnClickListener(this);
        nextSong.setOnClickListener(this);
        previousSong.setOnClickListener(this);
        moveBackward.setOnClickListener(this);
        moveFordward.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        repeat.setOnClickListener(this);
        playListButton.setOnClickListener(this);

    }

    @Override
    public void onCompletion(MediaPlayer arg0) {

        if (isRepeat) {
            // repeat is on play same song again
            playSong(position);
        } else if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            position = rand.nextInt((mySongs.size() - 1) - 0 + 1) + 0;
            playSong(position);
        } else {
            // no repeat or shuffle ON - play next song
            if (position < (mySongs.size() - 1)) {
                position++;
                playSong(position);
                position++;
            }
        }

    }

    public void playSong(int position){
        mp.reset();
        uri = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), uri);
        mp.start();
        seekBar.setMax(mp.getDuration());
        updateProgressBar();
        songTitle.setText(mySongs.get(position).getName().toString().replace(".mp3",""));
        mp.setOnCompletionListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnPlay:
                if (mp.isPlaying()) {
                    mp.pause();
                    pause.setImageResource(R.drawable.btn_play);
                } else {
                    mp.start();
                    pause.setImageResource(R.drawable.btn_pause);
                }
                break;
            case R.id.btnForward:
                if(mp.getDuration()>mp.getCurrentPosition())
                    mp.seekTo(mp.getCurrentPosition() + 10000);
                break;
            case R.id.btnBackward:
                if(mp.getCurrentPosition()>10000)
                    mp.seekTo(mp.getCurrentPosition() - 10000);
                break;
            case R.id.btnNext:
                position = ((position + 1) % mySongs.size());
                playSong(position);
                pause.setImageResource(R.drawable.btn_pause);
                break;
            case R.id.btnPrevious:
                position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                playSong(position);
                pause.setImageResource(R.drawable.btn_pause);
                break;
            case R.id.btnShuffle:
                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    shuffle.setImageResource(R.drawable.btn_shuffle);
                } else {
                    // make repeat to true
                    isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    shuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    repeat.setImageResource(R.drawable.btn_repeat);
                }
                break;
            case R.id.btnRepeat:
                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    repeat.setImageResource(R.drawable.btn_repeat);
                } else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    repeat.setImageResource(R.drawable.btn_repeat_focused);
                    shuffle.setImageResource(R.drawable.btn_shuffle);
                }
                break;
            case R.id.btnPlaylist:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
    }


    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            total_Duration.setText("" + milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            playedTime.setText("" + milliSecondsToTimer(currentDuration));

            seekBar.setProgress(mp.getCurrentPosition());

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }
}