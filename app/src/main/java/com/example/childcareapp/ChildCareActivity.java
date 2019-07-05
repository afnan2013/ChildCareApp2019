package com.example.childcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ChildCareActivity extends AppCompatActivity {

    private static final String TAG = "ChildCareActivity";
    Child child;
    DatabaseReference mDatabase;
    Button btn_morning_meal, btn_night_meal, btn_noon_meal, btn_morning_temp, btn_noon_temp, btn_night_temp, btn_exit_child, btn_update_child;
    EditText et_morning_meal_value, et_noon_meal_value, et_night_meal_value, et_morning_temp_value, et_noon_temp_value, et_night_temp_value, et_health_child, et_mode_child;
    TextView tv_parent_email;
    Boolean status;

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
        btn_update_child = (Button) findViewById(R.id.btn_update_child);

        et_morning_meal_value = findViewById(R.id.sp_morning_meal);
        et_noon_meal_value = findViewById(R.id.sp_noon_meal);
        et_night_meal_value = findViewById(R.id.sp_night_meal);
        et_morning_temp_value = findViewById(R.id.sp_morning_temp);
        et_noon_temp_value = findViewById(R.id.sp_noon_temp);
        et_night_temp_value = findViewById(R.id.sp_night_temp);
        et_health_child = findViewById(R.id.sp_health_child);
        et_mode_child = findViewById(R.id.sp_mode_child);
        tv_parent_email = findViewById(R.id.tv_email_parent);

        btn_exit_child = (Button) findViewById(R.id.btn_exit_child);

        final Query queryParent = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("childid")
                .equalTo(child.getChildId());

        new Thread(new Runnable() {
            @Override
            public void run() {
                queryParent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            Log.d(TAG, "onDataChange: Full Parent "+dataSnapshot);
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                Log.d(TAG, "onDataChange: " + user);
                                tv_parent_email.setText(user.getEmail());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }).start();

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


                    if(data.get("health") != null ){
                        String health = data.get("health").toString();
                        et_health_child.setText(health);
                    }

                    if(data.get("mode") != null ){
                        String mode = data.get("mode").toString();
                        et_mode_child.setText(mode);
                    }

                    String morning_meal = morning.get("meal").toString();
                    String noon_meal = noon.get("meal").toString();
                    String night_meal = night.get("meal").toString();

                    String morning_temp = morning.get("temperature").toString();
                    String noon_temp = noon.get("temperature").toString();
                    String night_temp = night.get("temperature").toString();

                    if(morning.get("mealvalue") !=null){
                        String morning_meal_value = morning.get("mealvalue").toString();
                        et_morning_meal_value.setText(morning_meal_value);
                    }

                    if(noon.get("mealvalue") != null){
                        String noon_meal_value = noon.get("mealvalue").toString();
                        et_noon_meal_value.setText(noon_meal_value);
                    }

                    if(night.get("mealvalue") != null){
                        String night_meal_value = night.get("mealvalue").toString();
                        et_night_meal_value.setText(night_meal_value);
                    }

                    if(morning.get("temperaturevalue") !=null){
                        String morning_temp_value = morning.get("temperaturevalue").toString();
                        et_morning_temp_value.setText(morning_temp_value);
                    }

                    if(noon.get("temperaturevalue") != null){
                        String noon_temp_value = noon.get("temperaturevalue").toString();
                        et_noon_temp_value.setText(noon_temp_value);
                    }

                    if(night.get("temperaturevalue") != null){
                        String night_temp_value = night.get("temperaturevalue").toString();
                        et_night_temp_value.setText(night_temp_value);
                    }



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

                    status = (Boolean) data.get("status");
                    if(!status){
                        et_morning_meal_value.setFocusable(false);
                        et_noon_meal_value.setFocusable(false);
                        et_night_meal_value.setFocusable(false);
                        et_morning_temp_value.setFocusable(false);
                        et_noon_temp_value.setFocusable(false);
                        et_night_temp_value.setFocusable(false);
                        et_health_child.setFocusable(false);
                        et_mode_child.setFocusable(false);
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
                if(status) {
                    String meal = et_morning_meal_value.getText().toString();
                    if (TextUtils.isEmpty(meal)) {
                        Toast.makeText(getApplicationContext(), "Enter Morning Meal Menu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mDatabase.child("morning").child("meal").setValue("ok");
                    mDatabase.child("morning").child("mealvalue").setValue(meal);
                    Toast.makeText(ChildCareActivity.this, "Morning Meal Done", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ChildCareActivity.this, "Child is not INCARE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_morning_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status) {
                    String temp = et_morning_temp_value.getText().toString();
                    if (TextUtils.isEmpty(temp)) {
                        Toast.makeText(getApplicationContext(), "Enter Morning Temperature Value of Child", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mDatabase.child("morning").child("temperature").setValue("ok");
                    mDatabase.child("morning").child("temperaturevalue").setValue(temp);
                    Toast.makeText(ChildCareActivity.this, "Morning Temperature Checking Done", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ChildCareActivity.this, "Child is not INCARE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_noon_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status) {
                    String meal = et_noon_meal_value.getText().toString();
                    if (TextUtils.isEmpty(meal)) {
                        Toast.makeText(getApplicationContext(), "Enter Noon Meal Menu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mDatabase.child("noon").child("meal").setValue("ok");
                    mDatabase.child("noon").child("mealvalue").setValue(meal);
                    Toast.makeText(ChildCareActivity.this, "Noon Meal Done", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ChildCareActivity.this, "Child is not INCARE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_noon_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status) {
                    String temp = et_noon_temp_value.getText().toString();
                    if (TextUtils.isEmpty(temp)) {
                        Toast.makeText(getApplicationContext(), "Enter Noon Temperature Value of Child", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mDatabase.child("noon").child("temperature").setValue("ok");
                    mDatabase.child("noon").child("temperaturevalue").setValue(temp);
                    Toast.makeText(ChildCareActivity.this, "Noon Temperature Checking Done", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ChildCareActivity.this, "Child is not INCARE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_night_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status) {
                    String meal = et_night_meal_value.getText().toString();
                    if (TextUtils.isEmpty(meal)) {
                        Toast.makeText(getApplicationContext(), "Enter Night Meal Menu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mDatabase.child("night").child("meal").setValue("ok");
                    mDatabase.child("night").child("mealvalue").setValue(meal);
                    Toast.makeText(ChildCareActivity.this, "Night Meal Done", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ChildCareActivity.this, "Child is not INCARE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_night_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status) {
                    String temp = et_night_temp_value.getText().toString();
                    if (TextUtils.isEmpty(temp)) {
                        Toast.makeText(getApplicationContext(), "Enter Night Temperature Value of Child", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mDatabase.child("night").child("temperature").setValue("ok");
                    mDatabase.child("night").child("temperaturevalue").setValue(temp);
                    Toast.makeText(ChildCareActivity.this, "Night Temperature Checking Done", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ChildCareActivity.this, "Child is not INCARE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_exit_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status) {
                    Intent intent = new Intent(ChildCareActivity.this, ChildQRCodeScanActivity.class);
                    intent.putExtra("activity", "exit");
                    intent.putExtra("child", child);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ChildCareActivity.this, "Child is not INCARE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_update_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status) {
                    String h = et_health_child.getText().toString();
                    String m = et_mode_child.getText().toString();
                    if (TextUtils.isEmpty(h)) {
                        Toast.makeText(getApplicationContext(), "Enter The Health and Mode for the child", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(m)) {
                        Toast.makeText(getApplicationContext(), "Enter The Health and Mode for the child", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mDatabase.child("health").setValue(h);
                    mDatabase.child("mode").setValue(m);
                    Toast.makeText(ChildCareActivity.this, "Health and Mode Updated Successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ChildCareActivity.this, "Child is not INCARE", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
