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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainClientActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainClientActivity";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String ChildId;
    String text="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = new ParentNotificationFragment();
        ft.replace(R.id.content_main_client, f);
        ft.commit();

        String userid = mAuth.getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference("users").child(userid).child("childid");
        query.addValueEventListener(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Log.d(TAG, "onDataChange: "+dataSnapshot.getValue());
                ChildId = dataSnapshot.getValue(String.class);
                Query queryStatus = FirebaseDatabase.getInstance().getReference("entrys")
                        .orderByChild("status")
                        .equalTo(true);
                queryStatus.addValueEventListener(valueEventListener1);
            }
            else{
                Log.d(TAG, "onDataChange: You are not match with your children");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
            startActivity(new Intent(MainClientActivity.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_notification) {
            fragment = new ParentNotificationFragment();
            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            fragment = new DisplayUserProfileFragment();

        } else if (id == R.id.nav_about) {
            fragment = new AdminAboutFragment();
        } else if (id == R.id.nav_pair_child) {
            fragment = new PairChildFragment();

        } else if (id == R.id.nav_complain_parent) {
            fragment = new ComplainUserFragment();

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main_client, fragment);
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addNotification(String Title, String Text) {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(Title)
                .setContentText(Text);

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, MainClientActivity.class);
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

    ValueEventListener valueEventListener1 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                Log.d(TAG, "onDataChange: QueryStatus "+ dataSnapshot);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: First Child Id: " + snapshot);
                    Child child = snapshot.getValue(Child.class);
                    Log.d(TAG, "onDataChange: First Child Value: " + child.getStatus());
                    if (ChildId.equals(child.getChildId())) {
                        boolean flag=false;
                        Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                        Map<String, Object> morning = (Map<String, Object>) data.get("morning");
                        Map<String, Object> noon = (Map<String, Object>) data.get("noon");
                        Map<String, Object> night = (Map<String, Object>) data.get("night");
                        Log.d(TAG, "onDataChange: " + data.get("status"));
                        Map<String, Object> childUpdates = new HashMap<>();
                        String morning_meal = morning.get("meal").toString();
                        String noon_meal = noon.get("meal").toString();
                        String night_meal = night.get("meal").toString();

                        String morning_temp = morning.get("temperature").toString();
                        String noon_temp = noon.get("temperature").toString();
                        String night_temp = night.get("temperature").toString();
                        //morning
                        if(morning_meal.equals("ok")){
                            morning.put("meal", "notify");
                            flag = true;
                            text = "Morning meal done";
                        }
                        if(morning_temp.equals("ok")){
                            morning.put("temperature", "notify");
                            flag = true;
                            text = "Morning temperature done";
                        }
                        //noon
                        if(noon_meal.equals("ok")){
                            noon.put("meal", "notify");
                            flag = true;
                            text = "Noon meal done";
                        }
                        if(noon_temp.equals("ok")){
                            noon.put("temperature", "notify");
                            flag = true;
                            text = "Noon temperature done";
                        }
                        //night
                        if(night_meal.equals("ok")){
                            night.put("meal", "notify");
                            flag = true;
                            text = "Night meal done";
                        }
                        if(night_temp.equals("ok")){
                            night.put("temperature", "notify");
                            flag = true;
                            text = "Night temperature done";
                        }

                        if(flag){
                            childUpdates.put("/entrys/"+child.getEntryid()+"/morning", morning);
                            childUpdates.put("/entrys/"+child.getEntryid()+"/noon", noon);
                            childUpdates.put("/entrys/"+child.getEntryid()+"/night", night);
                            mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onComplete: notification text"+ text);
                                    addNotification("ChildCareApp", text);
                                    playSound();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };



}
