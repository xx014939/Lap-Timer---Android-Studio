package com.example.scroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import static com.example.scroller.MainActivity.Laps;
import static com.example.scroller.MainActivity.lapMinutes;
import static com.example.scroller.MainActivity.lapSeconds;
import static com.example.scroller.MainActivity.minutesRest;
import static com.example.scroller.MainActivity.secondsRest;


public class main_page extends AppCompatActivity {

    MediaPlayer mp;

    // Image buttons used for the lap timer
    ImageButton reset;
    ImageButton pause;
    ImageButton playTime;

    private CountDownTimer mCountDownTimer; // Countdown timer object
    private boolean mTimerRunning; // boolean used to determine if lap timer is currently running

    private boolean lapFinished = false;
    boolean mTimerPaused = false; // lap timer paused?
    boolean restTimerPaused = false;

    private long minutesM = lapMinutes * 60 * 1000; // 60,000 mili seconds in a minute
    private long secondsM = lapSeconds * 1000;// 6000 mili seconds in a second
    private long mTimeLeftInMillis =  minutesM + secondsM; // Total lap time in mili seconds
    private long initialTime = minutesM + secondsM; // Save total lap time in another variable, reserved for a timer reset


    // Same as previous code, but for rest timer
    private CountDownTimer restCountDown;
    private boolean restTimerRunning;
    private long minutesRestM = minutesRest * 60 * 1000;
    private long secondsRestM = secondsRest * 1000;
    private long restTimeLeftInMillis = minutesRestM + secondsRestM + 1000;
    private long restInitialTime = minutesRestM + secondsRestM + 1000;

    private int totalLaps = Laps; // total initial laps, used to reset lap number later on

    private TextView lapTimeTitle;
    private TextView restTimeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        lapTimeTitle = (TextView) findViewById(R.id.lapTimeRemaining);
        restTimeTitle = (TextView) findViewById(R.id.restTimeRemaining);

        restTimerRunning = false;

        TextView textView2 = (TextView) findViewById(R.id.timer2);
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", lapMinutes, lapSeconds); // format minutes and seconds variables two two decimal places
        textView2.setText(timeLeftFormatted); // set formatted time in text view

        // Set initial number of laps based on user input "Laps" from MainActivity.java
        String lapNumber = Long.toString(Laps);
        TextView textView1 = (TextView) findViewById(R.id.laps);
        textView1.setText(lapNumber);

        // PLAY BUTTON
        playTime = (ImageButton) findViewById(R.id.playTimer);

        playTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Button has been pressed");

                if(Laps != 0 && mTimerPaused == false && restTimerPaused == false){ // At start of app, when neither times have been paused
                    startTimer();
                }


                if(Laps != 0 && mTimerPaused == true) { // If the lap timer is paused, and user presses the play button
                    startTimer();
                    mTimerPaused = false;
                }

            }
        });

        Button config;
        config = (Button) findViewById(R.id.configButton);

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(main_page.this, MainActivity.class);
                startActivity(i);
            }
        });

        // PAUSE BUTTON
        pause = (ImageButton) findViewById(R.id.pauseButton);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(restTimerRunning == false){ // If the rest timer is not running (means that the lap time is running)
                pauseTimer();} // Pause the lap timer

                if(restTimerRunning == true && mTimerRunning == false){ // if the rest timer is running, and the lap timer has stopped
                    System.out.println("Function has started");
                    pauseRestTimer();
                }
            }
        });

        // RESET BUTTON
        reset = (ImageButton) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        // Exit App
        Button exit;
        exit = (Button) findViewById(R.id.exitButton);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });
    }

    /** startTimer function starts the lap timer. Updates the textview's, and also calls the rest timer upon finishing*/
    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 950) {
            @Override
            public void onTick(long millisUntilFinished) {

                reset.setVisibility(View.VISIBLE);
                playTime.setVisibility(View.VISIBLE);
                pause.setVisibility(View.VISIBLE);
                mTimeLeftInMillis = millisUntilFinished; // Set start time
                lapTimeTitle.setVisibility(View.VISIBLE); // Show title text
                updateCountDownText(); // Update countdown text dynamically
            }

            @Override
            public void onFinish() {

                mTimeLeftInMillis = initialTime +1000; // reset time

                mTimerRunning=false;
                System.out.println("Timer has finished!");

                lapFinished = true; // Signal that the lap has finished using boolean variable
                lapUpdate(); // Run lap update function, which updates lap number based on boolean variable

                lapTimeTitle.setVisibility(View.INVISIBLE); // Make title invisible again

            }

    }.start();
};
    /** pauseTimer, used to pause the lap timer when it is running via the pause button*/
    private void pauseTimer() {

            mCountDownTimer.cancel(); // Cancel main timer countdown
            mTimerRunning = false;
            mTimerPaused = true;

    };

    /** pauseRestTimer, used to pause the rest timer when in is running via the pause button*/
    private void pauseRestTimer() {

        restCountDown.cancel(); // Cancel rest timer countdown
        restTimerRunning = false;
        restTimerPaused = true;


    };


    /** resetTimer, used to reset lap timer when reset button is pressed*/
    private void resetTimer() {

        // Reset lap timer
            pauseTimer();
            mTimeLeftInMillis = initialTime;
            updateCountDownText();
        // Reset laps
            Laps = totalLaps + 1;
            lapFinished = true;
            lapUpdate();
        // Pause rest timer
            pauseRestTimer();
    };

    /** startRestTimer is responsible for starting the rest timer, settting the appropriate buttons and textviews
     * to visible or not visible, as well as starting the lap timer upon finishing*/
    private void startRestTimer(){
        restCountDown = new CountDownTimer(restTimeLeftInMillis, 925) {
            @Override
            public void onTick(long millisUntilFinished) {
                reset.setVisibility(View.INVISIBLE);
                playTime.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.INVISIBLE);
                restTimerRunning = true;
                restTimeTitle.setVisibility(View.VISIBLE);
                restTimeLeftInMillis = millisUntilFinished;
                restCountDownText();
            }

            @Override
            public void onFinish() {

                restTimeLeftInMillis = restInitialTime; // reset rest timer variable

                restTimerRunning=false;
                restTimeTitle.setVisibility(View.INVISIBLE);

                if(Laps != 0) // If laps have NOT finished
                {
                startTimer(); // Start lap timer for next lap
                } else{
                    TextView textView2 = (TextView) findViewById(R.id.timer2);
                    textView2.setText("FINISH");
                }


            }

        }.start();

    }

    /** Specifically used to dynamically update countdown text during rest timer intervals*/
    private void restCountDownText() { // Dynamically update rest countdown text
        int restMinutes = (int) restTimeLeftInMillis /1000/60;
        int restSeconds = (int) (restTimeLeftInMillis) /1000 % 60;

        TextView textView2 = (TextView) findViewById(R.id.timer2);

        String timeLeftFormatted3 = String.format(Locale.getDefault(),"%02d:%02d", restMinutes, restSeconds); // Format text to two decimal places and assign to a string
        textView2.setText(timeLeftFormatted3);
    };


    /** Specifically used to dynamically update countdown text during lap timer intervals*/
    private void updateCountDownText() { // Dynamically update lap countdown text
        int minutes = (int) mTimeLeftInMillis /1000/60;
        int seconds = (int) (mTimeLeftInMillis) /1000 % 60;

        TextView textView2 = (TextView) findViewById(R.id.timer2);

        String timeLeftFormatted2 = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds); // Format text to two decimal places and assign to a string
        textView2.setText(timeLeftFormatted2);

    };

    /** Updates the total amount of laps remaining, and prints this number to the screen dynamically */
   private void lapUpdate(){

       if(lapFinished == true){ // if the lap has finished
           Laps = Laps -1; // new lap number is -1 from the old
           String lapsRemaining = Integer.toString(Laps); // set lapsRemaining String to = the number
           TextView lapNumber = (TextView) findViewById(R.id.laps); // Locate appropriate TextView
           lapNumber.setText(lapsRemaining); // set String as TextView
           lapFinished = false; // set variable to false to reset loop for next time
           startRestTimer();
       }

    }

}