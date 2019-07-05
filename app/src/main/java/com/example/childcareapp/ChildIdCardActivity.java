package com.example.childcareapp;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChildIdCardActivity extends AppCompatActivity {

    private static final String TAG = "ChildIdCardActivity";
    private static final String WRITE_EXTERNAL_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String READ_EXTERNAL_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE;

    ImageView img_child,qr_code;
    TextView tv_childid, tv_child_fullname, tv_child_age, tv_child_father_email, tv_child_mother_email,
                tv_joindate, tv_nick_name;
    RelativeLayout idCardView;

    Child child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_id_card);

        Toolbar toolbar = findViewById(R.id.toolbar_child_id_card);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        child = (Child) getIntent().getSerializableExtra("child");
        Log.d(TAG, "onCreate: "+child.getChildId());

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.id_card_save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                saveImage();
                Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void initViews() {
        img_child = findViewById(R.id.img_child_profile);
        tv_childid = findViewById(R.id.child_id);
        tv_child_fullname = findViewById(R.id.child_fullname);
        tv_nick_name = findViewById(R.id.child_nick_name);
        tv_child_age = findViewById(R.id.child_age);
        tv_child_father_email = findViewById(R.id.child_father_email);
        tv_child_mother_email = findViewById(R.id.chid_mother_email);
        tv_joindate = findViewById(R.id.joined_date);
        idCardView = findViewById(R.id.idCardLayout);
        qr_code = findViewById(R.id.qr_code_child);

        tv_childid.setText(child.getChildId());
        tv_child_fullname.setText(child.getFullname());
        tv_nick_name.setText(child.getNickname());
        tv_child_age.setText(child.getAge());
        tv_child_father_email.setText(child.getFatheremail());
        tv_child_mother_email.setText(child.getMotheremail());

        if(child.getImageURL().equals("default")){
            img_child.setImageResource(R.drawable.ic_profile);
        }
        else{
            Glide.with(this).load(child.getImageURL()).into(img_child);
        }

        String QRCODE =  "ID: "+child.getChildId()+"Name: "+child.getFullname()+" Age: "+child.getAge()+"" +
                "Father's Email "+child.getFatheremail()+"Mother's Email "+child.getMotheremail()+" Date Joined: "+child.getDate();

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(QRCODE, BarcodeFormat.QR_CODE,600,600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr_code.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }


    }

    private void saveImage() {
        Layout_to_Image layout_to_image = new Layout_to_Image(ChildIdCardActivity.this, idCardView);
        Bitmap bitmap = layout_to_image.convert_layout();

        if (isWriteStoragePermissionGranted()){
            saveImageToPhone(bitmap);
        }
    }

    private boolean isWriteStoragePermissionGranted() {
        boolean returnValue = false;

        if (Build.VERSION.SDK_INT >= 23){
            if (isExternalStorageWritable()){
                if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    returnValue = true;
                }
                else {
                    ActivityCompat.requestPermissions(this,new String []{WRITE_EXTERNAL_STORAGE},2);
                    returnValue = false;
                }
            }
        }
        else
            returnValue = true;

        return returnValue;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            return true;
        return false;
    }

    private void saveImageToPhone(Bitmap bitmap) {
        String location = Environment.getExternalStorageDirectory().toString();
        String filename = "childIDcard-"+child.getNickname()+".png";
        File myPath = new File(location,filename);

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


