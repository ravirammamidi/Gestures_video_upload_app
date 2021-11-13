package com.example.assignment_2_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
    // Global Values

    private static String[] dropdownList = {"Turn on lights", "Turn off lights", "Turn on fan", "Turn off fan", "Increase fan speed", "Decrease fan speed", "Set Thermostat to specified temperature","Gesture for 0","Gesture for 1","Gesture for 2","Gesture for 3","Gesture for 4","Gesture for 5","Gesture for 6","Gesture for 7","Gesture for 8","Gesture for 9"};
    private static Map<String,String> videoFileNameMap = new HashMap<String,String>(){{
        put("Turn on lights","hlighton");
        put("Turn off lights","hlightoff");
        put("Turn on fan","hfanon");
        put("Turn off fan","hfanoff");
        put("Increase fan speed","hincreasefanspeed");
        put("Decrease fan speed","hdecreasefanspeed");
        put("Set Thermostat to specified temperature","hsetthermo");
        put("Gesture for 0","h0");
        put("Gesture for 1","h1");
        put("Gesture for 2","h2");
        put("Gesture for 3","h3");
        put("Gesture for 4","h4");
        put("Gesture for 5","h5");
        put("Gesture for 6","h6");
        put("Gesture for 7","h7");
        put("Gesture for 8","h8");
        put("Gesture for 9","h9");
    }
    };
    static Integer replay_count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Get the selected gesture name ---------------------
        Intent currPage = getIntent();
        String selected_gesture = currPage.getStringExtra("selected_gesture");
        String video_filename = videoFileNameMap.get(selected_gesture);

        // Setting text view to the selected gesture ---------
        TextView textView4 = findViewById(R.id.textView4);
        textView4.setText(selected_gesture);

        // Video view displaying video -----------------------
        VideoView videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+"raw/"+video_filename);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (replay_count<=3) {
                    videoView.start();
                    replay_count +=1;
                }
                else
                    replay_count = 0;
            }
        });
        // Replay video button
        Button b5 = (Button) findViewById(R.id.button5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoView videoView = findViewById(R.id.videoView);
                Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+"raw/"+video_filename);
                videoView.setVideoURI(uri);
                videoView.start();
            }
        });

        // Practise gesture button ---------------------------
        Button b2= (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToThirdPage = new Intent(getApplicationContext(),MainActivity3.class);
                goToThirdPage.putExtra("selected_gesture",selected_gesture);
                startActivity(goToThirdPage);
            }
        });


    }

}