package com.example.assignment_2_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity3 extends AppCompatActivity {

    // Global variables
    private static String[] dropdownList = {"Turn on lights", "Turn off lights", "Turn on fan", "Turn off fan", "Increase fan speed", "Decrease fan speed", "Set Thermostat to specified temperature","Gesture for 0","Gesture for 1","Gesture for 2","Gesture for 3","Gesture for 4","Gesture for 5","Gesture for 6","Gesture for 7","Gesture for 8","Gesture for 9"};
    private static Map<String,String> gestureToFilenameMap = new HashMap<String,String>(){
        {
            put("Turn on lights", "LightOn");
            put("Turn off lights", "LightOff");
            put("Turn on fan", "FanOn");
            put("Turn off fan", "FanOff");
            put("Increase fan speed", "FanUp");
            put("Decrease fan speed", "FanDown");
            put("Set Thermostat to specified temperature", "SetThermo");
            put("Gesture for 0", "Num0");
            put("Gesture for 1", "Num1");
            put("Gesture for 2", "Num2");
            put("Gesture for 3", "Num3");
            put("Gesture for 4", "Num4");
            put("Gesture for 5", "Num5");
            put("Gesture for 6", "Num6");
            put("Gesture for 7", "Num7");
            put("Gesture for 8", "Num8");
            put("Gesture for 9", "Num9");
        }
    };

    private static Integer REQUEST_VIDEO_CAPTURE = 1;
    private static Map<String, ArrayList<Uri>> recordMap = new HashMap<String, ArrayList<Uri>>(){{
        put("LightOn",new ArrayList<>());
        put("LightOff",new ArrayList<>());
        put("FanOn",new ArrayList<>());
        put("FanOff",new ArrayList<>());
        put("FanUp",new ArrayList<>());
        put("FanDown",new ArrayList<>());
        put("SetThermo",new ArrayList<>());
        put("Num0",new ArrayList<>());
        put("Num1",new ArrayList<>());
        put("Num2",new ArrayList<>());
        put("Num3",new ArrayList<>());
        put("Num4",new ArrayList<>());
        put("Num5",new ArrayList<>());
        put("Num6",new ArrayList<>());
        put("Num7",new ArrayList<>());
        put("Num8",new ArrayList<>());
        put("Num9",new ArrayList<>());
    }};
    private static String curr_gesture;


    private Uri videoUri;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static String VIDEO_DIRECTORY_NAME = "GESTURE_VIDEOS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get data from prev intent -----------------
        Intent curr_intent = getIntent();
        curr_gesture = curr_intent.getStringExtra("selected_gesture");

        // Set text view to gesture name -------------
        setContentView(R.layout.activity_main3);
        TextView textView6 = findViewById(R.id.textView6);
        textView6.setText(curr_gesture);

        // Button for recording video ----------------
        Button b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Record Video logic
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,5);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                Toast.makeText(MainActivity3.this,"Record button",Toast.LENGTH_SHORT).show();
            }
        });

        // Button for uploading video -----------------
        Button b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Upload logic
                if(checkPermission())
                    UploadFile();
                else {
                    requestPermission();
                    UploadFile();
                }
                Intent goToFirstPage = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(goToFirstPage);
            }
        });

    }
    protected void onActivityResult( int requestCode, int resultCode, Intent intent){
        super.onActivityResult( requestCode,  resultCode,  intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = intent.getData();
            recordMap.get(gestureToFilenameMap.get(curr_gesture)).add(videoUri);
            Log.i("VIDEO_ADDED",gestureToFilenameMap.get(curr_gesture)+"--"+videoUri+" "+recordMap.get(gestureToFilenameMap.get(curr_gesture)).size() );
        }
    }
    private boolean checkPermission(){
        int result= ContextCompat.checkSelfPermission(MainActivity3.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }
    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity3.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity3.this, "Please Give Permission to Upload File", Toast.LENGTH_SHORT).show();
        }
        else{
            ActivityCompat.requestPermissions(MainActivity3.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity3.this, "Permission Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity3.this, "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private String getRealPathFromURI(Uri URI) {
        String fPath;
        Cursor cur = getContentResolver().query(URI, null, null, null, null);
        if (cur == null) {
            fPath = URI.getPath();
        } else {
            cur.moveToFirst();
            int idx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            fPath = cur.getString(idx);
            cur.close();
        }
        return fPath;
    }

    private void UploadFile() {
        UploadTask uploadTask=new UploadTask();
        uploadTask.execute(new String[]{getRealPathFromURI(videoUri)});
    }

    public class UploadTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equalsIgnoreCase("true")){
                Toast.makeText(MainActivity3.this, "File Uploaded", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity3.this, "Failed Upload", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            if(uploadFile(strings[0])){
                return "true";
            }
            else{
                return "false";
            }
        }

        private boolean uploadFile(String path){
            File file=new File(path);
            Log.i("UPLOAD", "uploadFile: " + path );
            String store_filename;
            Integer temp_size = recordMap.get(gestureToFilenameMap.get(curr_gesture)).size();
            if(temp_size < 4 )
                store_filename = gestureToFilenameMap.get(curr_gesture) +"_PRACTICE_"+ temp_size+"_MAMIDI.mp4";
            else {
                ArrayList<Uri> temp = recordMap.get(gestureToFilenameMap.get(curr_gesture));
                while(temp.size()!=1){
                    temp.remove(0);
                }
                recordMap.replace(gestureToFilenameMap.get(curr_gesture),temp);
                store_filename = gestureToFilenameMap.get(curr_gesture) +"_PRACTICE_"+ temp.size()+"_MAMIDI.mp4";
            }
            try{
                RequestBody requestBody=new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("file",store_filename,RequestBody.create(MediaType.parse("video/*"),file))
                        .build();

                Request request=new Request.Builder()
                        .url("http://10.157.123.49:5000/upload_video")
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {

                    }

                });
                return true;
            }
            catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }





}