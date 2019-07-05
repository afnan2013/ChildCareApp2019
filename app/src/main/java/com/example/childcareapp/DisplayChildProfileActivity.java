package com.example.childcareapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

public class DisplayChildProfileActivity extends AppCompatActivity {
    private final static String TAG = "DisplayChildProActiv";
    private final static int IMAGE_REQUSET = 1;

    StorageReference storageReference;
    DatabaseReference databaseReference, mdatabase;
    private Uri imageUri=null;
    private StorageTask uploadTask;

    EditText et_fullname, et_nickname, et_age, et_nationality, et_religion, et_fatheremail, et_motheremail;
    ProgressBar progressBar;
    Button btn_update,btn_seeidcard;
    ImageView img_profile;
    Child child;

    private String updatedimageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_child_profile);

        child = (Child) getIntent().getSerializableExtra("child");
        Log.d(TAG, "onCreate: "+child.getChildId());

        storageReference = FirebaseStorage.getInstance().getReference("images/");
        mdatabase = FirebaseDatabase.getInstance().getReference("child");
        //Toast.makeText(this, "Welcome "+child.getChildId(), Toast.LENGTH_LONG).show();
        initViews();
        btn_seeidcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayChildProfileActivity.this,ChildIdCardActivity.class);
                intent.putExtra("child", child);
                startActivity(intent);
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
    }

    void initViews(){
        et_fullname = findViewById(R.id.et_fullname_child);
        et_nickname = findViewById(R.id.et_useremail);
        et_age = findViewById(R.id.et_password);
        et_nationality = findViewById(R.id.et_nationality_child);
        et_religion = findViewById(R.id.et_gender);
        et_fatheremail = findViewById(R.id.et_father_email);
        et_motheremail = findViewById(R.id.et_mother_email);

        img_profile = findViewById(R.id.img_child_profile);

        et_fullname.setText(child.getFullname());
        et_nickname.setText(child.getNickname());
        et_age.setText(child.getAge());
        et_nationality.setText(child.getNationality());
        et_religion.setText(child.getReligion());
        et_fatheremail.setText(child.getFatheremail());
        et_motheremail.setText(child.getMotheremail());

        if(child.getImageURL().equals("default")){
            img_profile.setImageResource(R.mipmap.ic_launcher_round);
        }
        else{
            Glide.with(this).load(child.getImageURL()).into(img_profile);
        }

        btn_update = findViewById(R.id.btn_update_child);
        btn_seeidcard = findViewById(R.id.btn_create_id_card);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUSET);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadImage(){

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));
            Glide.with(this).load(imageUri).into(img_profile);
            uploadTask = fileReference.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        updatedimageUrl = downloadUri.toString();
                        //databaseReference = FirebaseDatabase.getInstance().getReference("child");
                        mdatabase.child(child.getChildId()).child("imageURL").setValue(updatedimageUrl);
                        Log.d(TAG, "onComplete: Download URL - "+updatedimageUrl);

                        Toast.makeText(DisplayChildProfileActivity.this, "Upload Image Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DisplayChildProfileActivity.this, "Upload Image Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DisplayChildProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateData(){
        databaseReference = FirebaseDatabase.getInstance().getReference("child").child(child.getChildId());
        Child tempChild = new Child(
                et_fullname.getText().toString(),
                et_nickname.getText().toString(),
                et_age.getText().toString(),
                et_nationality.getText().toString(),
                et_religion.getText().toString(),
                et_fatheremail.getText().toString(),
                et_motheremail.getText().toString(),
                child.getDate());
        Map<String, Object> result = tempChild.toMap();
        databaseReference.updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DisplayChildProfileActivity.this, "Update Complete", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DisplayChildProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUSET && resultCode == RESULT_OK
                 && data != null && data.getData() != null){
            imageUri = data.getData();
            Log.d(TAG, "onActivityResult: imageUri: "+imageUri);
            uploadImage();

        }
    }
}
