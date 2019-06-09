package com.example.anonymous.childcareadminapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Anonymous on 6/4/2019.
 */

public class HomeAdminFragment extends Fragment
{
    DatabaseReference databaseReference;
    ChildAdapter childadapter;
    String name,age,religion;
    List<Child> childs;
    StorageReference storageReference;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_child_handling_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        databaseReference = FirebaseDatabase.getInstance().getReference("child");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childs = new ArrayList<Child>();
                Log.d("AllAccountsAdminFrag", "onDataChange: First Time All Childs : "+dataSnapshot);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //Log.d("AllAccountsAdminFrag", "onDataChange: First Child Id: "+snapshot);
                    Child child = snapshot.getValue(Child.class);
                    //Log.d("AllAccountsAdminFrag", "onDataChange: First Child Value: "+child);
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
                childadapter = new ChildAdapter(getActivity(), childs);
                recyclerView.setAdapter(childadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("AllAccountsAdmin", "onCancelled: "+databaseError);
            }
        });
    }
}
