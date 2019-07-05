package com.example.childcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChildQRCodeScanResult extends AppCompatActivity {


    private Child child;
    DatabaseReference databaseReference, mDatabase;
    private String idchild;
    boolean flag = false;
    TextView tv_scan_result;
    Button btn_home;

    private static final String TAG = "ChildQRCodeScanResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_qrcode_scan_result);

        idchild = getIntent().getExtras().getString("id");
        //idchild = "-LiYNlJIPhnW2XpVuDJK";
        databaseReference = FirebaseDatabase.getInstance().getReference();

        tv_scan_result = (TextView) findViewById(R.id.tv_scan_result);
        btn_home = (Button) findViewById(R.id.btn_home_admin);

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Query query1 = FirebaseDatabase.getInstance().getReference("entrys")
                    .orderByChild("entrydate")
                    .equalTo(date);
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        Log.d(TAG, "onDataChange: Full Parent " + dataSnapshot);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Child user = snapshot.getValue(Child.class);
                            Log.d(TAG, "onDataChange: " + user);
                            Boolean status = user.getStatus();
                            String childid = user.getChildId();
                            if (status) {
                                if (idchild.equals(childid)) {
                                    flag = true;
                                }
                            }
                        }
                        if (!flag) {
                            Query query = FirebaseDatabase.getInstance().getReference("child")
                                    .orderByChild("childId")
                                    .equalTo(idchild);
                            query.addListenerForSingleValueEvent(valueEventListener);
                        } else {
                            Toast.makeText(ChildQRCodeScanResult.this, "Child is already INCARE", Toast.LENGTH_SHORT).show();
                            tv_scan_result.setText("Child is already INCARE");
                        }
                    }
                    else{
                        Query query = FirebaseDatabase.getInstance().getReference("child")
                                .orderByChild("childId")
                                .equalTo(idchild);
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            btn_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ChildQRCodeScanResult.this, AdminActivity.class));
                }
            });
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                Log.d(TAG, "onDataChange: datasnapshot -  "+dataSnapshot);
                Toast.makeText(ChildQRCodeScanResult.this, "Child Found", Toast.LENGTH_SHORT).show();
                child = dataSnapshot.child(idchild).getValue(Child.class);

                Log.d(TAG, "onDataChange: "+child.getChildId());
                Log.d(TAG, "onDataChange: "+child.getAge());


                String key_entry = databaseReference.child("entrys").push().getKey();

                //child entry in entry table
                DateFormat df = new SimpleDateFormat("HH:mm");
                String currenttime = df.format(Calendar.getInstance().getTime());
                String cuurentdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                Child entryChild = new Child(child.getChildId(), child.getFullname(),  key_entry, currenttime , "INCARE", cuurentdate, true);
                Map<String, Object> entryValues = entryChild.toMapEntry();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/entrys/"+ key_entry, entryValues);
                //childUpdates.put("/dates/" + date + "/" + key_date, dateValues);
                databaseReference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChildQRCodeScanResult.this, "Child Entry Successful", Toast.LENGTH_LONG).show();
                        tv_scan_result.setText("Child Entry Successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChildQRCodeScanResult.this, "Error in Entry", Toast.LENGTH_LONG).show();
                        tv_scan_result.setText("Error in Child Entry");
                    }
                });

            }
            else{
                Toast.makeText(ChildQRCodeScanResult.this, "Child not found", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onDataChange: datasnapshot - "+dataSnapshot);

            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
