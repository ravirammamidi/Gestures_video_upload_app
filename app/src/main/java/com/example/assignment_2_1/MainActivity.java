package com.example.assignment_2_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Global variables
    private static String[] dropdownList = {"Turn on lights", "Turn off lights", "Turn on fan", "Turn off fan", "Increase fan speed", "Decrease fan speed", "Set Thermostat to specified temperature","Gesture for 0","Gesture for 1","Gesture for 2","Gesture for 3","Gesture for 4","Gesture for 5","Gesture for 6","Gesture for 7","Gesture for 8","Gesture for 9"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SPINNER ------------------------------------------------------------------
        Spinner dropdown = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropdownList);
        dropdown.setAdapter(adapter);// set these values


        // NEXT PAGE Button
        Button b1=(Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner temp_spin = findViewById(R.id.spinner);
                String selected_gesture = temp_spin.getSelectedItem().toString();
                Toast.makeText(MainActivity.this,"Gesture: " + selected_gesture,Toast.LENGTH_SHORT).show();

                Intent goToSecondPage=new Intent(getApplicationContext(),MainActivity2.class);
                goToSecondPage.putExtra("selected_gesture",selected_gesture);
                startActivity(goToSecondPage);
            }

        });

    }
}