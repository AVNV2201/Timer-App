package com.example.abhinav.eggtimer;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView counter ;
    SeekBar seekBar ;
    long secondsLeft ;
    CountDownTimer timer ;
    Button button ;

    public void startTimer(final long seconds ){

        timer = new CountDownTimer( seconds*1000, 1000 ) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {

                secondsLeft = millisUntilFinished / 1000 ;
                counter.setText(Long.toString(secondsLeft/60 ) + ":" + Long.toString(secondsLeft % 60 ) ) ;
            }

            @Override
            public void onFinish() {

                counter.setText("00:00");
                button.setText("START") ;
                MediaPlayer.create( getApplicationContext(), R.raw.bomb).start();
            }
        }.start() ;
    }

    private  void pauseTimer(){

        timer.cancel();
    }

    private void resumeTimer(){

        startTimer( secondsLeft ) ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = (TextView)findViewById(R.id.counter);
        seekBar = (SeekBar) findViewById(R.id.seekBar) ;
        button = (Button)findViewById(R.id.button) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( button.getText().equals("START")) {

                    button.setText("PAUSE");
                    startTimer(60);
                }
                else if( button.getText().equals("PAUSE")) {

                    button.setText("RESUME");
                    pauseTimer();
                }
                else if( button.getText().equals("RESUME")){

                    button.setText("PAUSE");
                    resumeTimer();
                }
            }
        });

        seekBar.setMax(300);
        seekBar.setProgress(60);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                counter.setText(Integer.toString(progress/60 ) + ":" + Integer.toString( progress % 60));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                pauseTimer();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                startTimer(seekBar.getProgress());

            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress( (int)secondsLeft);
            }
        },0,1000);
    }
}
