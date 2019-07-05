package com.example.childcareapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Anonymous on 6/27/2019.
 */

public class ComplainUserFragment extends Fragment {

    private static final String TAG = "ComplainUserFragment";

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private String userid;
    String complain;
    EditText et_complain;
    Button btn_submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complain_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_complain = view.findViewById(R.id.et_user_complain);
        btn_submit = view.findViewById(R.id.btn_complain_submit);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        userid = mAuth.getCurrentUser().getUid();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c = et_complain.getText().toString();
                if(TextUtils.isEmpty(c)){
                    Toast.makeText(getActivity(), "Please Enter your Complain", Toast.LENGTH_SHORT).show();
                    return;
                }

                Query queryParent = FirebaseDatabase.getInstance().getReference("users")
                        .orderByChild("userid")
                        .equalTo(userid);

                queryParent.addValueEventListener(valueEventListener);

            }
        });


    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                Log.d(TAG, "onDataChange: datasnapshot -  " + dataSnapshot);

                User user = dataSnapshot.child(userid).getValue(User.class);

                Log.d(TAG, "onDataChange: " + user.getUsername());
                Log.d(TAG, "onDataChange: " + user.getEmail());

                DateFormat df = new SimpleDateFormat("HH:mm");
                String currenttime = df.format(Calendar.getInstance().getTime());
                String cuurentdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                String key = databaseReference.child("complains").push().getKey();
                Complain entryComplain = new Complain(key , user.getUserid(), user.getUsername(),
                        user.getEmail(), et_complain.getText().toString(), cuurentdate, false);

                if (key != null) {
                    databaseReference.child("complains").child(key).setValue(entryComplain)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Complain Submitted Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error in Complain Submission", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
