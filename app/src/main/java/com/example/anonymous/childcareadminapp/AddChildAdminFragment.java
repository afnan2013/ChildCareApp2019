package com.example.anonymous.childcareadminapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by Anonymous on 6/4/2019.
 */

public class AddChildAdminFragment extends Fragment {
    EditText et_fullname, et_nickname, et_age, et_nationality, et_religion, et_fatheremail, et_motheremail;
    ProgressBar progressBar;

    DatabaseReference databaseReference;

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
        et_nickname = view.findViewById(R.id.et_nickname_child);
        et_age = view.findViewById(R.id.et_age_child);
        et_nationality = view.findViewById(R.id.et_nationality_child);
        et_religion = view.findViewById(R.id.et_religious_child);
        et_fatheremail = view.findViewById(R.id.et_father_email);
        et_motheremail = view.findViewById(R.id.et_mother_email);

        Button btn_register = view.findViewById(R.id.btn_add_child);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = et_fullname.getText().toString().trim();
                String nickname = et_nickname.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String nationality = et_nationality.getText().toString().trim();
                String religion = et_religion.getText().toString().trim();
                String fatheremail = et_fatheremail.getText().toString().trim();
                String motheremail = et_motheremail.getText().toString().trim();
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


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

                Child child = new Child(fullname, nickname, age,
                        nationality, religion, fatheremail, motheremail, date);

                String key = databaseReference.child("child").push().getKey();
                Log.d("AddChildAdminFragment", "onClick: key :"+key);
                if(key != null) {
                    databaseReference.child("child").child(key).setValue(child)
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

                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
