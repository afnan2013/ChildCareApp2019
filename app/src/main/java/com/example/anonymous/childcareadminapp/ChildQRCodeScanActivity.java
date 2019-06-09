package com.example.anonymous.childcareadminapp;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ChildQRCodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final String TAG = "ChildQRCodeScanActivity";
    private static final String ACCESS_CAMERA = Manifest.permission.CAMERA;

    Button btn_scan_qrcode;
    ZXingScannerView mscannerView;

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
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("Scan Result");
        builer.setMessage(rawResult.getText());
        AlertDialog alerdialog = builer.create();
        alerdialog.show();

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
