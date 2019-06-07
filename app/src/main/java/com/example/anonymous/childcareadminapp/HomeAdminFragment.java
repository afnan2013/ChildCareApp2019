package com.example.anonymous.childcareadminapp;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.child_list_view);
        childadapter = new ChildAdapter(getContext(), R.layout.display_child_list_view);
        listView.setAdapter(childadapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("child");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    Log.d("HomeAdminFragment", "onDataChange: Name:"+data.get("nickname"));
                    Log.d("HomeAdminFragment", "onDataChange: Age:"+data.get("age"));
                    Log.d("HomeAdminFragment", "onDataChange: Religious:"+data.get("religion"));
                    name = data.get("nickname").toString();
                    age = data.get("age").toString();
                    religion = data.get("religion").toString();

                    Child child = new Child(name, age, religion);
                    childadapter.add(child);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
