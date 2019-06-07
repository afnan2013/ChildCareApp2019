package com.example.anonymous.childcareadminapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;


/**
 * Created by Anonymous on 6/7/2019.
 */

public class ChildProfileAdapter extends ArrayAdapter {
    List list = new ArrayList();
    String intent_childid;
    public ChildProfileAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public void add(@Nullable Child object) {

        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row = convertView;
        ChildProfileHolder childProfileHolder = new ChildProfileHolder();
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.display_child_list_row, parent,false);
            childProfileHolder.tx_serial = (TextView) row.findViewById(R.id.tv_serial);
            childProfileHolder.tx_name = (TextView) row.findViewById(R.id.tv_child_fullname);
            childProfileHolder.tx_joined_date = (TextView) row.findViewById(R.id.tv_joined_date);
            childProfileHolder.tx_childid = (TextView) row.findViewById(R.id.tv_child_id);
            childProfileHolder.btn_view = (Button) row.findViewById(R.id.btn_view_child);
            row.setTag(childProfileHolder);
        }
        else{
            childProfileHolder = (ChildProfileHolder) row.getTag();
        }

        Child child = (Child) this.getItem(position);
        childProfileHolder.tx_serial.setText(Integer.toString(child.getI()));
        childProfileHolder.tx_name.setText(child.getFullname());
        childProfileHolder.tx_joined_date.setText(child.getDate());
        childProfileHolder.tx_childid.setText(child.getChildId());
        intent_childid = childProfileHolder.tx_childid.getText().toString();
        childProfileHolder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),DisplayChildProfileActivity.class);
                intent.putExtra("id", intent_childid);
                startActivity(intent);
            }
        });

        return row;
    }

    private static class ChildProfileHolder{
        TextView tx_serial, tx_name, tx_joined_date, tx_childid;
        Button btn_view;
    }
}
