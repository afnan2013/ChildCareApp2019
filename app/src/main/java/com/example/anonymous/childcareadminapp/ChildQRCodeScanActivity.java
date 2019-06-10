package com.example.anonymous.childcareadminapp;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_qrcode_scan);


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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result ");
        builder.setMessage("Validating Child....");
        final String idchild = rawResult.getText().substring(4,24);
        //setContentView(R.layout.activity_child_qrcode_scan);

        Query query = FirebaseDatabase.getInstance().getReference("child")
                .orderByChild("childId")
                .equalTo(idchild);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d(TAG, "onDataChange: datasnapshot -  "+dataSnapshot);
                    Toast.makeText(ChildQRCodeScanActivity.this, "Child Found", Toast.LENGTH_SHORT).show();
                    child = dataSnapshot.child(idchild).getValue(Child.class);

                    Log.d(TAG, "onDataChange: "+child.getChildId());
                    Log.d(TAG, "onDataChange: "+child.getAge());

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    String key_entry = databaseReference.child("entrys").push().getKey();

                    //child entry in entry table
                    DateFormat df = new SimpleDateFormat("HH:mm");
                    String currenttime = df.format(Calendar.getInstance().getTime());
                    String cuurentdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    Child entryChild = new Child(child.getChildId(), child.getFullname(),  key_entry, currenttime , "INCARE", cuurentdate, true);
                    Map<String, Object> entryValues = entryChild.toMapEntry();

                    //String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    //String key_date = databaseReference.child("dates").child(date).push().getKey();
                    //Map<String, Object> dateValues = new HashMap<>();
                    //dateValues.put("id", key_date);
                    //dateValues.put("entryid", key_entry);
                    //dateValues.put("childid", child.getChildId());


                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/entrys/"+ key_entry, entryValues);
                    //childUpdates.put("/dates/" + date + "/" + key_date, dateValues);
                    databaseReference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ChildQRCodeScanActivity.this, "Child Entry Successful", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChildQRCodeScanActivity.this, "Error in Entry", Toast.LENGTH_LONG).show();

                        }
                    });

                }
                else{
                    Toast.makeText(ChildQRCodeScanActivity.this, "Child not found", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onDataChange: datasnapshot - "+dataSnapshot);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(valueEventListener);
        setContentView(R.layout.activity_child_qrcode_scan);
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
