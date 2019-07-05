package com.example.childcareapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Anonymous on 6/4/2019.
 */

public class AddChildAdminFragment extends Fragment {
    EditText et_fullname, et_nickname, et_age, et_nationality, et_religion, et_fatheremail, et_motheremail;
    ProgressBar progressBar;

    DatabaseReference databaseReference;
    private boolean flag=false;

    private String fullname, nickname, age, nationality, religion, fatheremail, motheremail, date, imageUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_adduser_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        et_fullname = view.findViewById(R.id.et_fullname_child);
        et_nickname = view.findViewById(R.id.et_useremail);
        et_age = view.findViewById(R.id.et_password);
        et_nationality = view.findViewById(R.id.et_nationality_child);
        et_religion = view.findViewById(R.id.et_gender);
        et_fatheremail = view.findViewById(R.id.et_father_email);
        et_motheremail = view.findViewById(R.id.et_mother_email);

        Button btn_register = view.findViewById(R.id.btn_login);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = et_fullname.getText().toString().trim();
                nickname = et_nickname.getText().toString().trim();
                age = et_age.getText().toString().trim();
                nationality = et_nationality.getText().toString().trim();
                religion = et_religion.getText().toString().trim();
                fatheremail = et_fatheremail.getText().toString().trim();
                motheremail = et_motheremail.getText().toString().trim();
                date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                imageUrl = "default";


                if (TextUtils.isEmpty(fullname)) {
                    Toast.makeText(getActivity(), "Enter full name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(nickname)) {
                    Toast.makeText(getActivity(), "Enter nick name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(age)) {
                    Toast.makeText(getActivity(), "Enter age!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(nationality)) {
                    Toast.makeText(getActivity(), "Enter nationality!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(religion)) {
                    Toast.makeText(getActivity(), "Enter religion!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(fatheremail).matches()) {
                    Toast.makeText(getActivity(), "Enter a valid Father email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(motheremail).matches()) {
                    Toast.makeText(getActivity(), "Enter a valid Mother email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                Query query = FirebaseDatabase.getInstance().getReference("child")
                        .orderByChild("fullname")
                        .equalTo(fullname);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Toast.makeText(getActivity(), "Child is Already Registered", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String key = databaseReference.child("child").push().getKey();
                            Child child = new Child(key, fullname, nickname, age,
                                    nationality, religion, fatheremail, motheremail, date, imageUrl);
                            Log.d("AddChildAdminFragment", "onClick: key :" + key);
                            if(key != null) {
                                databaseReference.child("child").child(key).setValue(child)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Child Registration Successful", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Error! Check your internet connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                /*
                if (!flag) {
                    String key = databaseReference.child("child").push().getKey();
                    Child child1 = new Child(key, fullname, nickname, age,
                            nationality, religion, fatheremail, motheremail, date, imageUrl);
                    Log.d("AddChildAdminFragment", "onClick: key :" + key);
                    if (key != null) {
                        databaseReference.child("child").child(key).setValue(child1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Child Registration Successful", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Error! Check your internet connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }else{
                    Toast.makeText(getActivity(), "Child already exists", Toast.LENGTH_SHORT).show();
                }
                */
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
