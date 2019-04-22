package com.mrbell.musicplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import me.itangqi.waveloadingview.WaveLoadingView;

public class Player extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Player";
    WaveLoadingView waveLoadingView;
    ArrayList<File> mysong;
    int position;
    static MediaPlayer mp;
    Uri uri;
    ImageButton previous,play,next,pause;

    SeekBar seekBar;
    Thread thread;
    TextView tstart,tstop;
    int totalTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //Initialize all view widget variable
        previous=findViewById(R.id.previous);
        play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.sekbar);
        tstart=findViewById(R.id.start);
        tstop=findViewById(R.id.stop);
        waveLoadingView=findViewById(R.id.wabloadingview);

        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        play.setOnClickListener(this);

//initialize Intent and Bundle to get data came from other activity
        Intent i = getIntent();
        Bundle b =i.getExtras();
        //get song file into arrayList through Bundle b

        mysong= (ArrayList) b.getParcelableArrayList("data");
        position=b.getInt("pos");
        //check media player is running or not
        if(mp!=null){
            mp.stop();
            mp.release();
        }
//Initialize and set media stuff to start song
        uri=Uri.parse(mysong.get(position).toString());
        mp=MediaPlayer.create(this,uri);
        mp.seekTo(0);
        totalTime=mp.getDuration();
        Log.d(TAG, "onCreate: TotalSongduration=="+totalTime);
        seekBar.setMax(mp.getDuration());
        mp.start();
        play.setImageResource(R.drawable.ic_pause_black_24dp);

  //seekbar listener

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mp.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp!=null){
                    Message message = new Message();
                    message.what=mp.getCurrentPosition();
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
        thread.start();


    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentpossition=msg.what;
            Log.d(TAG, "handleMessage: currentposition="+currentpossition);
            seekBar.setProgress(currentpossition);
            String starttime=createTimelebel(currentpossition);
            tstart.setText(starttime);
            int leftt=totalTime-currentpossition;
            String remianingtime=createTimelebel(leftt);
            tstop.setText("-"+remianingtime);

            int circle=getcircle(currentpossition);
            waveLoadingView.setProgressValue(circle);
            Log.d(TAG, "handleMessage: totalsongtime==="+totalTime);

        }
    };

    private int getcircle(int currentpossition) {
        int min=currentpossition/1000/60;
        int sec=currentpossition/1000%60;
        int arb =min*60+sec;


        if(sec>60){
            min=min+1;
            arb=arb+1;
        }

        if(arb!=0){
            arb=(arb*35)/100;
        }
        Log.d(TAG, "getcircle: min=="+arb);
        Log.d(TAG, "getcircle: sec=="+sec);

        return arb;


    }

    private String createTimelebel(int currentposition) {

        String timelebel="";
        int min = currentposition/1000/60;
        int sec = currentposition/1000%60;
        timelebel=min +":";
        if(sec<10){
            timelebel=timelebel+"0";

        }
        timelebel=timelebel+sec;
        return timelebel;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.play:
                if(mp.isPlaying()){
                    mp.pause();
                    play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    //seekBar.setMax(mp.getDuration());
                }else{
                    mp.start();
                    play.setImageResource(R.drawable.ic_pause_black_24dp);
                    seekBar.setMax(mp.getDuration());
                }
                break;
            case R.id.next:
                mp.stop();
                mp.release();
                position=(position+1)%mysong.size();
                uri=Uri.parse(mysong.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),uri);
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;

            case R.id.previous:
                mp.stop();
                mp.release();
                position=(position-1<0? mysong.size()-1:position-1);
                uri=Uri.parse(mysong.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),uri);
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;
        }
    }
}
