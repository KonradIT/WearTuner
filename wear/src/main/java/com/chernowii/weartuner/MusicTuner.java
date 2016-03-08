package com.chernowii.weartuner;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MusicTuner extends Activity {

    private TextView mTextView;
    MediaPlayer mPlayer;
    boolean isPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_tuner);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Button e_button = (Button) findViewById(R.id.e_note);
                e_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isPlaying){
                            mPlayer.stop();
                        }
                        mPlayer = MediaPlayer.create(MusicTuner.this, R.raw.e_note_sound);
                        mPlayer.start();
                        isPlaying=true;
                    }
                });
                Button a_button = (Button) findViewById(R.id.a_note);
                a_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isPlaying){
                            mPlayer.stop();
                        }
                        mPlayer = MediaPlayer.create(MusicTuner.this, R.raw.a_note_sound);
                        mPlayer.start();
                        isPlaying=true;
                    }
                });

                Button d_button = (Button) findViewById(R.id.d_note);
                d_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isPlaying){
                            mPlayer.stop();
                        }
                        mPlayer = MediaPlayer.create(MusicTuner.this, R.raw.d_note_sound);
                        mPlayer.start();
                        isPlaying=true;
                    }
                });

                Button g_button = (Button) findViewById(R.id.g_note);
                g_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isPlaying){
                            mPlayer.stop();
                        }
                        mPlayer = MediaPlayer.create(MusicTuner.this, R.raw.g_note_sound);
                        mPlayer.start();
                        isPlaying=true;
                    }
                });

                Button b_button = (Button) findViewById(R.id.b_note);
                b_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isPlaying){
                            mPlayer.stop();
                        }
                        mPlayer = MediaPlayer.create(MusicTuner.this, R.raw.b_note_sound);
                        mPlayer.start();
                        isPlaying=true;
                    }
                });
            }
        });
    }
}
