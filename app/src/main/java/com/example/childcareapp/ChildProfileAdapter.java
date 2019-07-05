package com.example.childcareapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Anonymous on 6/8/2019.
 */

public class ChildProfileAdapter extends RecyclerView.Adapter<ChildProfileAdapter.ChildProfileHolder>{

    DatabaseReference databaseReference;
    public final static String TAG = "ChildProfileAdapter";
    private List<Child> childs;
    private Context c;
    int i=1;

    public ChildProfileAdapter(Context c, List<Child> childs){
        this.c = c;
        this.childs = childs;
    }

    @NonNull
    @Override
    public ChildProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_child_list_row, parent, false);
        return new ChildProfileHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildProfileHolder childProfileHolder, int position) {
        final Child currentNote = childs.get(position);

        Log.d(TAG, "onBindViewHolder: "+currentNote);
        childProfileHolder.tx_serial.setText(String.valueOf(i));
        childProfileHolder.tx_name.setText(currentNote.getFullname());
        childProfileHolder.tx_joined_date.setText(String.valueOf(currentNote.getDate()));
        childProfileHolder.tx_childid.setText(String.valueOf(currentNote.getChildId()));

        Log.d(TAG, "onBindViewHolder: "+i);
        Log.d(TAG, "onBindViewHolder: "+currentNote.getFullname());
        Log.d(TAG, "onBindViewHolder: "+currentNote.getDate());
        Log.d(TAG, "onBindViewHolder: "+currentNote.getChildId());

        childProfileHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference =  FirebaseDatabase.getInstance().getReference("child").child(currentNote.getChildId());
                //Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(c, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(c, "error!! Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        childProfileHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + currentNote.getChildId());

                Toast.makeText(c, currentNote.getChildId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(c, DisplayChildProfileActivity.class);
                intent.putExtra("child", currentNote);
                c.startActivity(intent);
            }
        });

        i = i+1;

    }


    @Override
    public int getItemCount() {
        return childs.size();
    }

    public void setChilds(List<Child> childs) {
        this.childs = childs;
    }

    public Child getChildAt(int position) {
        return childs.get(position);
    }

    class ChildProfileHolder extends RecyclerView.ViewHolder {
        private TextView tx_serial, tx_name, tx_joined_date, tx_childid;
        private CardView cardView;
        private Button btn_delete;


        public ChildProfileHolder(@NonNull View itemView) {
            super(itemView);
            tx_serial = (TextView) itemView.findViewById(R.id.tv_serial);
            tx_name = (TextView) itemView.findViewById(R.id.tv_parent_fullname);
            tx_joined_date = (TextView) itemView.findViewById(R.id.tv_joined_date);
            tx_childid = (TextView) itemView.findViewById(R.id.tv_parent_email);
            cardView= (CardView) itemView.findViewById(R.id.child_list_row);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete_child);

        }


    }

}
