package com.example.childcareapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Anonymous on 6/19/2019.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    public final static String TAG = "UserAdapter";
    private List<User> users;
    private Context mContext;
    int i=1;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public UserAdapter(Context c, List<User> users){
        this.mContext = c;
        this.users = users;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_parent_list_view, parent, false);
        return new UserHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int position) {
        final User currentNote = users.get(position);

        Log.d(TAG, "onBindViewHolder: "+currentNote);

        userHolder.tx_name.setText(currentNote.getUsername());
        userHolder.tx_email.setText(String.valueOf(currentNote.getEmail()));
        if(currentNote.getAdmin()){
            userHolder.tx_join_date.setText("Admin");
        }
        else{
            userHolder.tx_join_date.setText("Parent");
        }


        userHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = currentNote.getUserid();
                databaseReference.child(userid).child("status")
                        .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "User Verification Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, " Unsuccessful User Verification", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Log.d(TAG, "onBindViewHolder: "+currentNote.getUsername());
        Log.d(TAG, "onBindViewHolder: "+currentNote.getEmail());
        Log.d(TAG, "onBindViewHolder: "+currentNote.getStatus());

        //if(!currentNote.getStatus())
            //userHolder.img_status.setImageResource(R.drawable.ic_offline);
        /*
        userHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + currentNote.getUserid());

                //Toast.makeText(mContext, currentNote.getChildId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("user", currentNote);
                mContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        private TextView tx_name, tx_email, tx_join_date;
        private CardView cardView;
        private ImageView img_status;
        private Button btn_accept;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            tx_name = (TextView) itemView.findViewById(R.id.tv_parent_fullname);
            tx_email = (TextView) itemView.findViewById(R.id.tv_parent_email);
            tx_join_date = (TextView) itemView.findViewById(R.id.tv_joined_date);
            //img_status = (ImageView) itemView.findViewById(R.id.img_status_child);
            btn_accept = (Button) itemView.findViewById(R.id.btn_view_parent);
            cardView= (CardView) itemView.findViewById(R.id.parent_list_row);
        }

    }
}
