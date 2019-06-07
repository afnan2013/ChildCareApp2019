package com.example.anonymous.childcareadminapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DisplayChildProfileActivity extends AppCompatActivity {
    private final static String TAG = "DisplayChildProActiv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_child_profile);
        Intent intent = getIntent();
        String childid = intent.getExtras().getString("id");
        Log.d(TAG, "onCreate: "+childid);
    }
}
