package com.example.childcareapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static androidx.core.view.GravityCompat.START;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "AdminActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();
        String adminid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users")
                .child(adminid).child("admin");
        mDatabase.addValueEventListener(valueEventListener);
        databaseReference = FirebaseDatabase.getInstance().getReference("complains");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_admin_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);



        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = new HomeAdminFragment();
        ft.replace(R.id.content_admin, f);
        ft.commit();
    }

    private void initNotification(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Complain complain = snapshot.getValue(Complain.class);
                                if(complain != null) {
                                    if (!complain.isStatus()) {
                                        addNotification("Child Care App", "A complain has been submitted by " + complain.getUsername());
                                        playSound();
                                        databaseReference.child(complain.getComplainid()).child("status").setValue(true);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_admin_layout);
        if (drawer.isDrawerOpen(START)){
            drawer.closeDrawer(START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.logout){
            mAuth.signOut();
            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNotification(String Title, String Text) {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(Title)
                .setContentText(Text);

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, AdminActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    protected void playSound(){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.quite_impressed);
        mp.start();
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                Boolean f = dataSnapshot.getValue(Boolean.class);
                Log.d(TAG, "onDataChange: Check if it is admin" + f);
                if(f){
                    initNotification();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            // Handle the home action
            fragment = new HomeAdminFragment();
        } else if (id == R.id.nav_profile) {
            fragment = new DisplayUserProfileFragment();
        } else if (id == R.id.nav_entry) {
            fragment = new EntryChildAdminFragment();
        } else if(id == R.id.nav_about){
            fragment = new AdminAboutFragment();
        } else if (id == R.id.nav_add_child) {
            fragment = new AddChildAdminFragment();
        } else if (id == R.id.nav_all_accounts) {
            fragment = new AllAccountsAdminFragment();
        } else if(id == R.id.nav_verify_user){
            fragment = new UserVerfiyAdminFragment();
        } else if(id == R.id.nav_verify_admin){
            fragment = new AdminVerifyFragment();
        } else if (id == R.id.nav_complain_box){
            fragment = new ComplainAdminFragment();
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_admin, fragment);
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_admin_layout);
        drawer.closeDrawer(START);
        return true;
    }
}
