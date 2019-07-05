package com.example.childcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText inputEmail,inputPassword;
    private Button btn_Login,btn_Register,btn_Reset;
    private TextView tv_NameErr,tv_PassErr;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() != null) {
            String userid = auth.getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userid).child("admin");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        boolean condition = dataSnapshot.getValue(Boolean.class);
                        if(condition){
                            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this, MainClientActivity.class));
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        inputEmail = findViewById(R.id.et_id);
        inputPassword = findViewById(R.id.et_Password);
        btn_Login = findViewById(R.id.btnLogin);
        tv_NameErr =  findViewById(R.id.tvNameEr);
        tv_PassErr = findViewById(R.id.tvPassEr);
        btn_Register = findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.progressBar);
        btn_Reset = findViewById(R.id.btn_reset_password);

        tv_NameErr.setVisibility(View.INVISIBLE);
        tv_PassErr.setVisibility(View.INVISIBLE);


        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (inputPassword.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    final String userid = auth.getCurrentUser().getUid();
                                    Query query = mDatabase.orderByChild("userid")
                                            .equalTo(userid);

                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                Log.d(TAG, "onDataChange: datasnapshot -  "+dataSnapshot);
                                                user = dataSnapshot.child(userid).getValue(User.class);

                                                Log.d(TAG, "onDataChange: "+user.getStatus());
                                                Log.d(TAG, "onDataChange: "+user.getAdmin());

                                                Boolean status = user.getStatus();
                                                Boolean admin = user.getAdmin();

                                                if(status) {
                                                    if (admin) {
                                                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Intent intent = new Intent(LoginActivity.this, MainClientActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                                else{
                                                    Toast.makeText(LoginActivity.this, "Registered But Not Authorized User", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                            else{
                                                Toast.makeText(LoginActivity.this, "User account is disable", Toast.LENGTH_LONG).show();
                                                Log.d(TAG, "onDataChange: datasnapshot - "+dataSnapshot);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });

                        //progressBar.setVisibility(View.GONE);
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }


}
