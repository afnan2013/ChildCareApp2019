package com.example.anonymous.childcareadminapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anonymous on 6/6/2019.
 */

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildHolder> {

    public final static String TAG = "ChildAdapter";
    private List<Child> childs;
    private Context mContext;
    int i=1;

    public ChildAdapter(Context c, List<Child> childs){
        this.mContext = c;
        this.childs = childs;
    }

    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_child_list_view, parent, false);
        return new ChildHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildHolder childHolder, int position) {
        final Child currentNote = childs.get(position);

        Log.d(TAG, "onBindViewHolder: "+currentNote);

        childHolder.tx_name.setText(currentNote.getFullname());
        childHolder.tx_entry_time.setText(String.valueOf(currentNote.getEntrytime()));
        childHolder.tx_left_time.setText(String.valueOf(currentNote.getLeavetime()));

        Log.d(TAG, "onBindViewHolder: "+currentNote.getFullname());
        Log.d(TAG, "onBindViewHolder: "+currentNote.getDate());
        Log.d(TAG, "onBindViewHolder: "+currentNote.getChildId());

        if(!currentNote.getStatus())
            childHolder.img_status.setImageResource(R.drawable.ic_offline);

        childHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + currentNote.getChildId());

                //Toast.makeText(mContext, currentNote.getChildId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ChildCareActivity.class);
                intent.putExtra("child", currentNote);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return childs.size();
    }

    public class ChildHolder extends RecyclerView.ViewHolder {

        private TextView tx_name, tx_entry_time, tx_left_time;
        private CardView cardView;
        private ImageView img_status;

        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            tx_name = (TextView) itemView.findViewById(R.id.tv_child_fullname);
            tx_entry_time = (TextView) itemView.findViewById(R.id.tv_entry_time);
            tx_left_time = (TextView) itemView.findViewById(R.id.tv_left_time);
            img_status = (ImageView) itemView.findViewById(R.id.img_status_child);
            cardView= (CardView) itemView.findViewById(R.id.child_list_handling_view);
        }

    }

}
