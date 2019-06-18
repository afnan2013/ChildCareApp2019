package com.example.anonymous.childcareadminapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by Anonymous on 6/15/2019.
 */

public class ParentNotificationFragment extends Fragment {

    private static final String TAG = "ParentNotificationFragm";

    Child child;
    String ChildId;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Button btn_morning_meal, btn_night_meal, btn_noon_meal, btn_morning_temp, btn_noon_temp, btn_night_temp;
    boolean flag = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parent_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        btn_morning_meal = (Button) view.findViewById(R.id.tv_morning_meal);
        btn_morning_temp = (Button) view.findViewById(R.id.tv_morning_temp);
        btn_noon_meal = (Button) view.findViewById(R.id.tv_noon_meal);
        btn_noon_temp = (Button) view.findViewById(R.id.tv_noon_temp);
        btn_night_meal = (Button) view.findViewById(R.id.tv_night_meal);
        btn_night_temp = (Button) view.findViewById(R.id.tv_night_temp);


        final Query queryStatus = FirebaseDatabase.getInstance().getReference("entrys")
                .orderByChild("status")
                .equalTo(true);

        final String userid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userid).child("childid");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d(TAG, "onDataChange: Childid Snapshot"+ dataSnapshot);
                    Log.d(TAG, "onDataChange: Childid Value"+dataSnapshot.getValue(String.class));

                    ChildId = dataSnapshot.getValue(String.class);
                    queryStatus.addValueEventListener(valueEventListener);
                }
                else{
                    Toast.makeText(getActivity(), "No Pair Child Found.\nFirst, Pair with your Child", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                Log.d(TAG, "onDataChange: QueryStatus "+ dataSnapshot);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: First Child Id: "+snapshot);
                    Child child = snapshot.getValue(Child.class);
                    Log.d(TAG, "onDataChange: First Child Value: "+child.getStatus());
                    if(ChildId.equals(child.getChildId())){
                        Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                        Map<String, Object> morning = (Map<String, Object>) data.get("morning");
                        Map<String, Object> noon = (Map<String, Object>) data.get("noon");
                        Map<String, Object> night = (Map<String, Object>) data.get("night");
                        Log.d(TAG, "onDataChange: " + data.get("status"));
                        String morning_meal = morning.get("meal").toString();
                        String noon_meal = noon.get("meal").toString();
                        String night_meal = night.get("meal").toString();

                        String morning_temp = morning.get("temperature").toString();
                        String noon_temp = noon.get("temperature").toString();
                        String night_temp = night.get("temperature").toString();

                        if (morning_meal.equals("no")) {
                            btn_morning_meal.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_morning_meal.setBackgroundColor(getResources().getColor(R.color.btn_done));
                        }

                        if (morning_temp.equals("no")) {
                            btn_morning_temp.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_morning_temp.setBackgroundColor(getResources().getColor(R.color.btn_done));
                        }

                        if (noon_meal.equals("no")) {
                            btn_noon_meal.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_noon_meal.setBackgroundColor(getResources().getColor(R.color.btn_done));
                        }

                        if (noon_temp.equals("no")) {
                            btn_noon_temp.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_noon_temp.setBackgroundColor(getResources().getColor(R.color.btn_done));
                        }

                        if (night_meal.equals("no")) {
                            btn_night_meal.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_night_meal.setBackgroundColor(getResources().getColor(R.color.btn_done));
                        }

                        if (night_temp.equals("no")) {
                            btn_night_temp.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_night_temp.setBackgroundColor(getResources().getColor(R.color.btn_done));
                        }
                        flag = true;
                    }

                }
                if(!flag){
                    Toast.makeText(getActivity(), "Your Child is not IN-CARE", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    // Creates and displays a notification

}
