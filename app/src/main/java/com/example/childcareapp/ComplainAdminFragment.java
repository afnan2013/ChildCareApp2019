package com.example.childcareapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anonymous on 6/27/2019.
 */


public class ComplainAdminFragment extends Fragment {

    public static final String TAG = "AllAccountAdminFrag";
    private DatabaseReference databaseReference;
    private ComplainAdapter complainAdapter;
    private RecyclerView recyclerView;
    String childid, name, date;
    private List<Complain> complains;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complain_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_complains);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("complains");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complains = new ArrayList<>();
                Log.d("AllAccountsAdminFrag", "onDataChange: First Time All Childs : "+dataSnapshot);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.d("AllAccountsAdminFrag", "onDataChange: First Child Id: "+snapshot);
                    Complain complain = snapshot.getValue(Complain.class);
                    Log.d("AllAccountsAdminFrag", "onDataChange: First Child Value: "+complain);
                    complains.add(complain);
                }
                complainAdapter = new ComplainAdapter(getActivity(), complains);
                recyclerView.setAdapter(complainAdapter);
                complainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("AllAccountsAdmin", "onCancelled: "+databaseError);
            }
        });
    }


}
