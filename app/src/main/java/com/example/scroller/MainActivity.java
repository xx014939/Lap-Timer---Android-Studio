package com.example.scroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // User inputs
    public EditText lapMins;
    public EditText lapSecs;

    public static EditText restMins;
    public static EditText restSecs;

    List<String> laps = new ArrayList<>(); // Laps
    ScrollChoice scrollChoice3; // Laps

    public static int minutesRest;
    public static int secondsRest;

    public static int lapMinutes;
    public static int lapSeconds;

    public static int Laps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton playButton;
        playButton = (ImageButton) findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (restSecs.getText().toString().trim().length() > 0) { // If user has provided an input (determined by measuring the length of user input)
                    secondsRest = Integer.parseInt(restSecs.getText().toString()); // convert it to an int and assign to appropriate variable
                } else { secondsRest = 0;} // if user has not entered a number, set variable to = 0

                // repeat previous code, but for minutes instead of seconds
                if(restMins.getText().toString().trim().length() > 0){
                    minutesRest = Integer.parseInt(restMins.getText().toString());
                } else {minutesRest = 0;}

                // Repeat previous code, but this time for lap duration instead of rest time
                if(lapSecs.getText().toString().trim().length() > 0){
                    lapSeconds = Integer.parseInt(lapSecs.getText().toString());
                } else { lapSeconds = 0;}

                if(lapMins.getText().toString().trim().length() > 0){
                    lapMinutes = Integer.parseInt(lapMins.getText().toString());
                } else { lapMinutes = 0;}


                Intent i = new Intent(MainActivity.this, main_page.class);
                startActivity(i); // Start main_page.java
            }
        });

        loadLaps(); // load laps into scrollview
        initView3();

        lapMinutes(); // load how many minutes in a lap
        lapSeconds(); // load how many seconds in a lap

        restMinutes(); // load how many minutes in a rest period
        restSeconds(); // load how many seconds in a rest period


        scrollChoice3.addItems(laps,0);

        scrollChoice3.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {

                Laps = position + 1;
                System.out.println(Laps);
            }
        });


    }

    /** Load user input into appropriate variables */
    public void restSeconds() { restSecs = (EditText) findViewById(R.id.rest_seconds); }

    public void restMinutes() { restMins = (EditText) findViewById(R.id.rest_minutes); }

    public void lapSeconds() { lapSecs = (EditText) findViewById(R.id.lap_seconds); }

    public void lapMinutes() { lapMins = (EditText) findViewById(R.id.lap_minutes); }



    /** Load scroll choice xml design into a scroll choice object (3rd party API)*/
    private void initView3() {

        scrollChoice3 = (ScrollChoice)findViewById(R.id.scroll_choice3);
    }

    /** Load all laps into laps list */
    private void loadLaps() {
        laps.add("1");
        laps.add("2");
        laps.add("3");
        laps.add("4");
        laps.add("5");
        laps.add("6");
        laps.add("7");
        laps.add("8");
        laps.add("9");
        laps.add("10");
        laps.add("11");
        laps.add("12");
        laps.add("13");
        laps.add("14");
        laps.add("15");
        laps.add("16");
        laps.add("17");
        laps.add("18");
        laps.add("19");
        laps.add("20");

    }


}
