package com.example.childcareapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Anonymous on 6/26/2019.
 */

public class DisplayUserProfileFragment extends Fragment {
    private static final String TAG = "DisplayUserProfileFragm";

    private final static int IMAGE_REQUSET = 1;

    StorageReference storageReference;
    DatabaseReference databaseReference, mdatabase;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Uri imageUri=null;
    private StorageTask uploadTask;

    EditText et_username,et_email, et_age, et_nationality, et_religion, et_fatheremail, et_motheremail;
    ProgressBar progressBar;
    Button btn_update,btn_delete;
    ImageView img_profile;
    User user;

    String userid;

    private String updatedimageUrl = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userid = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "onCreate: "+userid);

        et_username = view.findViewById(R.id.et_username);
        et_email = view.findViewById(R.id.et_useremail);
        btn_delete = view.findViewById(R.id.btn_delete_user);
        btn_update = view.findViewById(R.id.btn_update_user);
        img_profile = view.findViewById(R.id.img_user_profile);


        storageReference = FirebaseStorage.getInstance().getReference("images/");
        mdatabase = FirebaseDatabase.getInstance().getReference("users").child(userid);
        //Toast.makeText(this, "Welcome "+child.getChildId(), Toast.LENGTH_LONG).show();

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    user = dataSnapshot.getValue(User.class);
                    et_username.setText(user.getUsername());
                    et_email.setText(user.getEmail());
                    if(user.getImageURL().equals("default")){
                        img_profile.setImageResource(R.mipmap.ic_launcher_round);
                    }
                    else{
                        Glide.with(getActivity()).load(user.getImageURL()).into(img_profile);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                mdatabase.removeValue();
                mAuth.getCurrentUser().delete();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
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

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUSET);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadImage(){

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));
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
                        mdatabase.child("imageURL").setValue(updatedimageUrl);
                        Log.d(TAG, "onComplete: Download URL - "+updatedimageUrl);

                        //Toast.makeText(getActivity().this, "Upload Image Successful", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Upload Image Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Upload Image Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateData(){
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userid);
        User tempUser= new User(
                et_username.getText().toString(),
                et_email.getText().toString()
                );
        Map<String, Object> result = tempUser.toMap();
        databaseReference.updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Update Complete", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUSET && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();
            Log.d(TAG, "onActivityResult: imageUri: "+imageUri);
            uploadImage();
            Glide.with(this).load(user.getImageURL()).into(img_profile);
        }
    }
}
