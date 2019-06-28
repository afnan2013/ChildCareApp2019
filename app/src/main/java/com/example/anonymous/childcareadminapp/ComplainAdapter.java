package com.example.anonymous.childcareadminapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Anonymous on 6/27/2019.
 */

public class ComplainAdapter extends RecyclerView.Adapter <ComplainAdapter.ComplainHolder> {

    public final static String TAG = "ChildAdapter";
    private List<Complain> complains;
    private Context mContext;
    int i=1;

    public ComplainAdapter(Context c, List<Complain> complains){
        this.mContext = c;
        this.complains = complains;
    }

    @NonNull
    @Override
    public ComplainHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_complain_list_row, parent, false);
        return new ComplainAdapter.ComplainHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplainHolder complainHolder, int position) {
        final Complain currentNote = complains.get(position);

        Log.d(TAG, "onBindViewHolder: "+currentNote);

        complainHolder.tx_serial.setText(String.valueOf(i));
        complainHolder.tx_name.setText(currentNote.getUsername());
        complainHolder.tx_email.setText(String.valueOf(currentNote.getEmail()));
        complainHolder.tx_complain_time.setText(String.valueOf(currentNote.getDate()));

        Log.d(TAG, "onBindViewHolder: "+currentNote.getUsername());
        Log.d(TAG, "onBindViewHolder: "+currentNote.getEmail());
        Log.d(TAG, "onBindViewHolder: "+currentNote.getDate());


        complainHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + currentNote.getUsername());

                //Toast.makeText(mContext, currentNote.getChildId(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Complain From "+currentNote.getUsername());
                builder.setMessage(currentNote.getComplain());
                builder.show();
            }
        });

        i++;
    }

    @Override
    public int getItemCount() {
        return complains.size();
    }

    public class ComplainHolder extends RecyclerView.ViewHolder{

        private TextView tx_serial, tx_name, tx_email,  tx_complain_time;
        private CardView cardView;

        public ComplainHolder(@NonNull View itemView) {
            super(itemView);
            tx_serial = (TextView) itemView.findViewById(R.id.tv_serial);
            tx_name = (TextView) itemView.findViewById(R.id.tv_parent_fullname);
            tx_email = (TextView) itemView.findViewById(R.id.tv_parent_email);
            tx_complain_time = itemView.findViewById(R.id.tv_complain_date);
            cardView= (CardView) itemView.findViewById(R.id.complain_list_row);
        }
    }
}
