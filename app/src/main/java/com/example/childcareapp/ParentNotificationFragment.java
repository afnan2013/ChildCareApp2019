package com.example.childcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.childcareapp.Child;
import com.example.childcareapp.R;
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
    DatabaseReference mDatabase,databaseReference;
    FirebaseAuth mAuth;
    Button btn_morning_meal, btn_night_meal, btn_noon_meal, btn_morning_temp, btn_noon_temp, btn_night_temp;
    TextView et_morning_meal_value, et_noon_meal_value, et_night_meal_value, et_morning_temp_value, et_noon_temp_value, et_night_temp_value;
    TextView childname, health, mode;
    ImageView img_status;

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

        et_morning_meal_value = view.findViewById(R.id.sp_morning_meal);
        et_noon_meal_value = view.findViewById(R.id.sp_noon_meal);
        et_night_meal_value = view.findViewById(R.id.sp_night_meal);
        et_morning_temp_value = view.findViewById(R.id.sp_morning_temp);
        et_noon_temp_value = view.findViewById(R.id.sp_noon_temp);
        et_night_temp_value = view.findViewById(R.id.sp_night_temp);

        childname = view.findViewById(R.id.childname);
        health = view.findViewById(R.id.text_health);
        mode = view.findViewById(R.id.text_mode);

        img_status = view.findViewById(R.id.img_status_child);


        final String userid = mAuth.getCurrentUser().getUid();

        final Query queryStatus = FirebaseDatabase.getInstance().getReference("entrys")
                .orderByChild("status")
                .equalTo(true);


        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userid).child("childid");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d(TAG, "onDataChange: Childid Snapshot"+ dataSnapshot);
                    Log.d(TAG, "onDataChange: Childid Value"+dataSnapshot.getValue(String.class));

                    ChildId = dataSnapshot.getValue(String.class);

                    databaseReference = FirebaseDatabase.getInstance().getReference("child").child(ChildId);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Log.d(TAG, "onDataChange: The ChildNode Snapshot" + dataSnapshot);

                                Child child = dataSnapshot.getValue(Child.class);
                                Log.d(TAG, "onDataChange: the nmae of Child" + child.getFullname());
                                if (child != null) {
                                    String name = child.getFullname();
                                    childname.setText(name);
                                }
                                queryStatus.addValueEventListener(valueEventListener);
                            }
                            else{
                                Toast.makeText(getActivity(), "Your child has been Deleted.\nFirst, Contact with the Authority", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                else{
                    Toast.makeText(getActivity(), "No Pair Child Found.\nFirst, Pair with your Child", Toast.LENGTH_SHORT).show();
                    img_status.setImageResource(R.drawable.ic_offline);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                Log.d(TAG, "onDataChange: QueryStatus "+ dataSnapshot);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: First Child Id: "+snapshot);
                    Child child = snapshot.getValue(Child.class);
                    Log.d(TAG, "onDataChange: First Child Value: "+child.getStatus());
                    String name = child.getFullname();


                    if(ChildId.equals(child.getChildId())){
                        childname.setText(name);
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

                        if(data.get("health") != null){
                            health.setText(data.get("health").toString());
                        }

                        if(data.get("mode") != null){
                            mode.setText(data.get("mode").toString());
                        }

                        if (morning_meal.equals("no")) {
                            btn_morning_meal.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_morning_meal.setBackgroundColor(getResources().getColor(R.color.btn_done));

                            if(morning.get("mealvalue") !=null){
                                String morning_meal_value = morning.get("mealvalue").toString();
                                et_morning_meal_value.setText(morning_meal_value);
                            }
                        }

                        if (morning_temp.equals("no")) {
                            btn_morning_temp.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_morning_temp.setBackgroundColor(getResources().getColor(R.color.btn_done));
                            if(morning.get("temperaturevalue") !=null){
                                String morning_temp_value = morning.get("temperaturevalue").toString();
                                et_morning_temp_value.setText(morning_temp_value);
                            }

                        }

                        if (noon_meal.equals("no")) {
                            btn_noon_meal.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_noon_meal.setBackgroundColor(getResources().getColor(R.color.btn_done));
                            if(noon.get("mealvalue") != null){
                                String noon_meal_value = noon.get("mealvalue").toString();
                                et_noon_meal_value.setText(noon_meal_value);
                            }
                        }

                        if (noon_temp.equals("no")) {
                            btn_noon_temp.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_noon_temp.setBackgroundColor(getResources().getColor(R.color.btn_done));
                            if(noon.get("temperaturevalue") != null){
                                String noon_temp_value = noon.get("temperaturevalue").toString();
                                et_noon_temp_value.setText(noon_temp_value);
                            }
                        }

                        if (night_meal.equals("no")) {
                            btn_night_meal.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_night_meal.setBackgroundColor(getResources().getColor(R.color.btn_done));
                            if(night.get("mealvalue") != null){
                                String night_meal_value = night.get("mealvalue").toString();
                                et_night_meal_value.setText(night_meal_value);
                            }
                        }

                        if (night_temp.equals("no")) {
                            btn_night_temp.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                        } else {
                            btn_night_temp.setBackgroundColor(getResources().getColor(R.color.btn_done));
                            if(night.get("temperaturevalue") != null){
                                String night_temp_value = night.get("temperaturevalue").toString();
                                et_night_temp_value.setText(night_temp_value);
                            }
                        }
                        flag = true;
                    }

                }
                if(!flag){
                    Toast.makeText(getActivity(), "Your Child is not IN-CARE", Toast.LENGTH_SHORT).show();
                    img_status.setImageResource(R.drawable.ic_offline);
                }
            }
            else {
                Toast.makeText(getActivity(), "Your Child is not IN-CARE", Toast.LENGTH_SHORT).show();
                img_status.setImageResource(R.drawable.ic_offline);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    // Creates and displays a notification

}
