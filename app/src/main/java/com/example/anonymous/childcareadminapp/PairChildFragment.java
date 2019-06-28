package com.example.anonymous.childcareadminapp;

import android.arch.core.executor.DefaultTaskExecutor;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Anonymous on 6/19/2019.
 */

public class PairChildFragment extends Fragment {

    Button btn_pair;
    TextView tv_pair_check;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    boolean flag = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pair_child_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        final String userid = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userid).child("childid");

        btn_pair = (Button) view.findViewById(R.id.btn_pair_child);
        tv_pair_check = (TextView) view.findViewById(R.id.tv_pair_check);

        databaseReference.addValueEventListener(valueEventListener);

        btn_pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    Toast.makeText(getActivity(), "Already paired", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), PairQRCodeScanActivity.class);
                    intent.putExtra("activity", userid);
                    startActivity(intent);
                }
            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                flag=true;
                tv_pair_check.setText("You have already paired with your child");
            }
            else{
                tv_pair_check.setText("Please pair with your child");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
