package com.example.childcareapp;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

public class ChildQRCodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final String TAG = "ChildQRCodeScanActivity";
    private static final String ACCESS_CAMERA = Manifest.permission.CAMERA;

    Button btn_scan_qrcode;
    ZXingScannerView mscannerView;
    private Child child;
    DatabaseReference databaseReference, mDatabase;
    FirebaseAuth mAuth;
    String checkActivity, idchild;
    Boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_qrcode_scan);
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
        Log.d(TAG, "handleResult: "+rawResult.getText());

        idchild = rawResult.getText().substring(4,24);

        Log.d(TAG, "handleResult: checkActivity"+ checkActivity);

        /******Entry Child QrcodeScan******/
        if(checkActivity.equals("entry")) {
            setContentView(R.layout.activity_child_qrcode_scan);
            Intent i = new Intent(ChildQRCodeScanActivity.this, ChildQRCodeScanResult.class);
            i.putExtra("id", idchild);
            startActivity(i);
            finish();
        }

        else if(checkActivity.equals("exit")){
            child = (Child) getIntent().getSerializableExtra("child");
            if(!idchild.equals(child.getChildId())){
                Toast.makeText(ChildQRCodeScanActivity.this, "Invalid ID", Toast.LENGTH_SHORT).show();
            }
            else {
                DateFormat df = new SimpleDateFormat("HH:mm");
                String currenttime = df.format(Calendar.getInstance().getTime());
                mDatabase = FirebaseDatabase.getInstance().getReference("entrys").child(child.getEntryid());

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/status", false);
                childUpdates.put("/leavetime", currenttime);
                mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChildQRCodeScanActivity.this, "Child Exit Successful", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChildQRCodeScanActivity.this, "Error in Exit", Toast.LENGTH_LONG).show();

                    }
                });
            }

            setContentView(R.layout.activity_child_qrcode_scan);
            startActivity(new Intent(ChildQRCodeScanActivity.this, AdminActivity.class));
            finish();
        }

        //mscannerView.resumeCameraPreview(this);

    }


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
