package com.example.anonymous.childcareadminapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ChildCareActivity extends AppCompatActivity {

    private static final String TAG = "ChildCareActivity";
    Child child;
    DatabaseReference mDatabase;
    Button btn_morning_meal, btn_night_meal, btn_noon_meal, btn_morning_temp, btn_noon_temp, btn_night_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_care);

        child = (Child) getIntent().getSerializableExtra("child");
        Log.d(TAG, "onCreate: Child Id"+child.getChildId());
        //Toast.makeText(this, child.getChildId()+"\n"+child.getEntryid(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate: Entry Id"+child.getEntryid());

        btn_morning_meal = (Button) findViewById(R.id.tv_morning_meal);
        btn_morning_temp = (Button) findViewById(R.id.tv_morning_temp);
        btn_noon_meal = (Button) findViewById(R.id.tv_noon_meal);
        btn_noon_temp = (Button) findViewById(R.id.tv_noon_temp);
        btn_night_meal = (Button) findViewById(R.id.tv_night_meal);
        btn_night_temp = (Button) findViewById(R.id.tv_night_temp);

        mDatabase = FirebaseDatabase.getInstance().getReference("entrys").child(child.getEntryid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    Map<String, Object> morning = (Map<String, Object>) data.get("morning");
                    Map<String, Object> noon = (Map<String, Object>) data.get("noon");
                    Map<String, Object> night = (Map<String, Object>) data.get("night");
                    Log.d(TAG, "onDataChange: "+data.get("status"));
                    String morning_meal = morning.get("meal").toString();
                    String noon_meal = noon.get("meal").toString();
                    String night_meal = night.get("meal").toString();

                    String morning_temp = morning.get("temperature").toString();
                    String noon_temp = noon.get("temperature").toString();
                    String night_temp = night.get("temperature").toString();

                    if(morning_meal.equals("no")){
                        btn_morning_meal.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                    }
                    else{
                        btn_morning_meal.setBackgroundColor(getResources().getColor(R.color.btn_done));
                    }

                    if(morning_temp.equals("no")){
                        btn_morning_temp.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                    }
                    else{
                        btn_morning_temp.setBackgroundColor(getResources().getColor(R.color.btn_done));
                    }

                    if(noon_meal.equals("no")){
                        btn_noon_meal.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                    }
                    else{
                        btn_noon_meal.setBackgroundColor(getResources().getColor(R.color.btn_done));
                    }

                    if(noon_temp.equals("no")){
                        btn_noon_temp.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                    }
                    else{
                        btn_noon_temp.setBackgroundColor(getResources().getColor(R.color.btn_done));
                    }

                    if(night_meal.equals("no")){
                        btn_night_meal.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                    }
                    else{
                        btn_night_meal.setBackgroundColor(getResources().getColor(R.color.btn_done));
                    }

                    if(night_temp.equals("no")){
                        btn_night_temp.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                    }
                    else{
                        btn_night_temp.setBackgroundColor(getResources().getColor(R.color.btn_done));
                    }
                }

                //morning_meal.setText("done");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_morning_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("morning").child("meal").setValue("ok");
                Toast.makeText(ChildCareActivity.this, "Morning Meal Done", Toast.LENGTH_SHORT).show();
            }
        });

        btn_morning_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("morning").child("temperature").setValue("ok");
                Toast.makeText(ChildCareActivity.this, "Morning Temperature Checking Done", Toast.LENGTH_SHORT).show();
            }
        });

        btn_noon_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("noon").child("meal").setValue("ok");
                Toast.makeText(ChildCareActivity.this, "Noon Meal Done", Toast.LENGTH_SHORT).show();
            }
        });

        btn_noon_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("noon").child("temperature").setValue("ok");
                Toast.makeText(ChildCareActivity.this, "Noon Temperature Checking Done", Toast.LENGTH_SHORT).show();
            }
        });

        btn_night_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("night").child("meal").setValue("ok");
                Toast.makeText(ChildCareActivity.this, "Night Meal Done", Toast.LENGTH_SHORT).show();
            }
        });

        btn_night_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("night").child("temperature").setValue("ok");
                Toast.makeText(ChildCareActivity.this, "Night Temperature Checking Done", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
