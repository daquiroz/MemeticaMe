package com.mobility42.azurechatr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Recorder extends Activity {


    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer   mPlayer = null;
    String idchat;
    String blobname;


    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };


        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    public Recorder() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/Audio"+ UUID.randomUUID().toString().replace("-", "")+".3gp";
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        idchat =getIntent().getStringExtra("idchat");
        setContentView(R.layout.activity_recorder);

        LinearLayout ll = (LinearLayout)findViewById(R.id.recorder_linearlayout);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        final Button btSave = new Button(this);
        btSave.setText("Send");

        ll.addView(btSave,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendAudio();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
     public void  sendAudio()
     {
    File f = new File(mFileName);
         try {
             final AudioBlob ADB = new AudioBlob(f,this,idchat);
             ADB.execute();
             Thread t = new Thread(new Runnable() {
                 public void run()
                 {
                     while (ADB.getfinished() == false)
                     {
                     }
                     blobname = ADB.blobname;
                     Recorder.this.runOnUiThread(new Runnable() {
                         public void run() {
                             String finished = "Audio enviado";
                             Toast.makeText(getBaseContext(), finished, Toast.LENGTH_SHORT).show();

                         }});

                     try{
                         final DownloadAudioBlob DAB = new DownloadAudioBlob(Recorder.this,idchat, blobname);
                         DAB.execute();
                         Thread t2 = new Thread(new Runnable() {
                             public void run()
                             {
                                 while (DAB.getfinished() == false)
                                 {
                                 }
                                 Recorder.this.runOnUiThread(new Runnable() {
                                     public void run() {
                                         String finished = "Audio bajado!";
                                         Toast.makeText(getBaseContext(), finished, Toast.LENGTH_SHORT).show();
                                         mPlayer = new MediaPlayer();
                                         try {
                                             mPlayer.setDataSource(DAB.blobDir);
                                             mPlayer.prepare();
                                             mPlayer.start();
                                         } catch (Exception e) {
                                             Log.e(LOG_TAG, "prepare() failed");
                                         }
                                     }});

                             }
                         });
                         t2.start();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                 });
             t.start();






             Intent intent = new Intent(Recorder.this,ChatActivity.class);
             intent.putExtra("idchat", idchat);
             startActivity(intent);


         } catch (Exception e) {
             e.printStackTrace();
         }

     }



}