package com.example.anonymous.childcareadminapp;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.storage.StorageTask;

public class DisplayChildProfileActivity extends AppCompatActivity {
    private final static String TAG = "DisplayChildProActiv";
    private final static int IMAGE_REQUSET = 1;



    private Uri imageUri;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_child_profile);
        Intent intent = getIntent();
        String childid = intent.getExtras().getString("id");
        Log.d(TAG, "onCreate: "+childid);
        Toast.makeText(this, "Welcome "+childid, Toast.LENGTH_LONG).show();
    }
}
