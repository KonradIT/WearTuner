package com.chernowii.weartuner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class WearTuner extends Activity {


    private SeekBar seekBar;
    private final int duration = 10;
    private final int sampleRate = 8000;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];
    private double freqOfTone = 0;

    private final byte generatedSnd[] = new byte[2 * numSamples];

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_tuner);
        if(!watchHasSpeaker()){
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Watch does not have a speaker! Uninstall the app")
                    .setPositiveButton("close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Button minus10Hz = (Button) findViewById(R.id.minusTenHz);
                minus10Hz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //decreaseBy10Hz();
                    }
                });
                Button plus10Hz = (Button) findViewById(R.id.plusTenHz);
                plus10Hz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        increaseBy10Hz();
                    }
                });
                seekBar = (SeekBar) findViewById(R.id.frequency_slider);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                        progress = progresValue;
                        TextView status = (TextView)findViewById(R.id.mhzFreq);
                        freqOfTone = progress*100;
                        status.setText(freqOfTone+"mHz");
                        // Use a new tread as this can take a while
                        final Thread thread = new Thread(new Runnable() {
                            public void run() {
                                genTone();
                                handler.post(new Runnable() {

                                    public void run() {
                                        playSound();
                                    }
                                });
                            }
                        });
                        thread.start();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        });
    }
    void increaseBy10Hz(){
        freqOfTone=freqOfTone+10;
        seekBar = (SeekBar) findViewById(R.id.frequency_slider);
        seekBar.setProgress(seekBar.getProgress()+10);

        final Thread thread = new Thread(new Runnable() {
            public void run() {
                genTone();
                handler.post(new Runnable() {

                    public void run() {
                        playSound();
                    }
                });
            }
        });
        thread.start();
    }
    public boolean watchHasSpeaker(){
        PackageManager packageManager = this.getPackageManager();
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

// Check whether the device has a speaker.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check FEATURE_AUDIO_OUTPUT to guard against false positives.
            if (!packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
                return false;
            }

            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo device : devices) {
                if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                    return true;
                }
            }
        }
        return false;
    }
    void genTone(){
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    void playSound(){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }

    //Code from stack overflow
}
