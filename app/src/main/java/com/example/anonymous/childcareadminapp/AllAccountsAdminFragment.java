package com.example.anonymous.childcareadminapp;

import android.os.Bundle;
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

/**
 * Created by Anonymous on 6/7/2019.
 */

public class AllAccountsAdminFragment extends Fragment {

    DatabaseReference databaseReference;
    ChildProfileAdapter childProfileAdapter;
    String childid, name,date;
    ListView listView;
    TextView tx_serial;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_accounts_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.child_list_row);
        childProfileAdapter = new ChildProfileAdapter(getContext(), R.layout.display_child_list_row);
        listView.setAdapter(childProfileAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("child");

        tx_serial = view.findViewById(R.id.tv_serial);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=1;
                Log.d("AllAccountsAdminFrag", "onDataChange: First Time All Childs : "+dataSnapshot);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.d("AllAccountsAdminFrag", "onDataChange: First Child Id: "+snapshot);

                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();

                    Log.d("AllAccountsAdminFrag", "onDataChange: id:"+snapshot.getKey());
                    Log.d("AllAccountsAdminFrag", "onDataChange: Name:"+data.get("fullname"));
                    Log.d("AllAccountsAdminFrag", "onDataChange: Join Date:"+data.get("date"));

                    childid = snapshot.getKey();
                    name = data.get("fullname").toString();
                    date = data.get("date").toString();

                    Child child = new Child(childid, name, date, i);
                    childProfileAdapter.add(child);
                    i=i+1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        
    }
}
