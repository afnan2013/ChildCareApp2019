package com.example.anonymous.childcareadminapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Anonymous on 6/7/2019.
 */

public class AllAccountsAdminFragment extends Fragment {
    public static final String TAG = "AllAccountAdminFrag";
    private DatabaseReference databaseReference;
    private ChildProfileAdapter childProfileAdapter;
    private RecyclerView recyclerView;
    String childid, name,date;
    private List<Child> childs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_accounts_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_child);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("child");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childs= new ArrayList<Child>();
                Log.d("AllAccountsAdminFrag", "onDataChange: First Time All Childs : "+dataSnapshot);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.d("AllAccountsAdminFrag", "onDataChange: First Child Id: "+snapshot);
                    Child child = snapshot.getValue(Child.class);
                    Log.d("AllAccountsAdminFrag", "onDataChange: First Child Value: "+child);
                    /*
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    Log.d("AllAccountsAdminFrag", "onDataChange: id:"+snapshot.getKey());
                    Log.d("AllAccountsAdminFrag", "onDataChange: Name:"+data.get("fullname"));
                    Log.d("AllAccountsAdminFrag", "onDataChange: Join Date:"+data.get("date"));
                    childid = snapshot.getKey();
                    name = data.get("fullname").toString();
                    date = data.get("date").toString();
                    Child child = new Child(childid, name, date, i);
                    */

                    child.setChildId(snapshot.getKey());
                    childs.add(child);
                }
                childProfileAdapter = new ChildProfileAdapter(getActivity(), childs);
                recyclerView.setAdapter(childProfileAdapter);
                childProfileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("AllAccountsAdmin", "onCancelled: "+databaseError);
            }
        });

    }
}
