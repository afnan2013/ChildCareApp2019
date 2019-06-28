package com.example.anonymous.childcareadminapp;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PairQRCodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final String TAG = "ChildQRCodeScanActivity";
    private static final String ACCESS_CAMERA = android.Manifest.permission.CAMERA;

    Button btn_scan_qrcode;
    ZXingScannerView mscannerView;
    private Child child;
    DatabaseReference databaseReference, mDatabase;
    FirebaseAuth mAuth;
    String checkActivity, idchild;
    String Userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_qrcode_scan);
        Intent intent = getIntent();
        checkActivity = intent.getExtras().getString("activity");
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void onClick(View v){
        mscannerView = new ZXingScannerView(this);
        setContentView(mscannerView);
        mscannerView.setResultHandler(this);
        if(isWriteStoragePermissionGranted()) {
            mscannerView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mscannerView.stopCamera();

    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Log.d(TAG, "handleResult: " + rawResult.getText());

            idchild = rawResult.getText().substring(4, 24);
            //setContentView(R.layout.activity_child_qrcode_scan);
            Log.d(TAG, "handleResult: checkActivity" + checkActivity);

            final Query query = FirebaseDatabase.getInstance().getReference("child")
                    .orderByChild("childId")
                    .equalTo(idchild);

            setContentView(R.layout.activity_pair_qrcode_scan);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    query.addValueEventListener(valueEventListener);
                }
            }).start();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //mscannerView.resumeCameraPreview(this);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                Log.d(TAG, "onDataChange: datasnapshot -  "+dataSnapshot);
                Toast.makeText(PairQRCodeScanActivity.this, "Child Found", Toast.LENGTH_SHORT).show();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/"+checkActivity+"/childid", idchild);
                //childUpdates.put("/dates/" + date + "/" + key_date, dateValues);
                databaseReference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PairQRCodeScanActivity.this, "Child Matching Successful", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PairQRCodeScanActivity.this, "Error in Entry", Toast.LENGTH_LONG).show();

                    }
                });

            }
            else{
                Toast.makeText(PairQRCodeScanActivity.this, "Child not found", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onDataChange: datasnapshot - "+dataSnapshot);

            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private boolean isWriteStoragePermissionGranted() {
        boolean returnValue = false;

        if (Build.VERSION.SDK_INT >= 23){
            if (checkSelfPermission(ACCESS_CAMERA) == PackageManager.PERMISSION_GRANTED){
                returnValue = true;
            }
            else {
                ActivityCompat.requestPermissions(this,new String []{ACCESS_CAMERA},2);
                returnValue = false;
            }
        }
        else
            returnValue = true;
        return returnValue;
    }
}
